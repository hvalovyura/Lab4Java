package Lab4VarA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel
{
    private int selectedMarker = -1;
    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showMarkers = true;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private double scale;

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;

    private static DecimalFormat formatter=(DecimalFormat) NumberFormat.getInstance();

    private Font axisFont;
    private Font labelsFont;

    public GraphicsDisplay()
    {
        setBackground(Color.WHITE);

        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {12, 3}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
        labelsFont = new Font("Serif",0,10);
        addMouseMotionListener(new MouseMotionHandler());
    }

    public void showGraphics(Double[][] graphicsData)
    {
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    protected Point2D.Double xyToPoint(double x, double y)
    {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY)
    {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    protected void paintGraphics(Graphics2D canvas)
    {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();
        for(int i = 0; i < graphicsData.length; i++)
        {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);

            if(i > 0)
            {
                graphics.lineTo(point.getX(), point.getY());
            }
            else
            {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    protected void paintAxis(Graphics2D canvas)
    {

        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        if(minX <= 0.0 && maxX >= 0.0)
        {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
        }
        if(minY <= 0.0 && maxY >= 0.0)
        {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10), (float)(labelPos.getY() + bounds.getY()));
            if (selectedMarker >= 0)
            {
                Point2D.Double point = xyToPoint(graphicsData[selectedMarker][0].doubleValue(), graphicsData[selectedMarker][1].doubleValue());
                String label = "X=" + formatter.format(graphicsData[selectedMarker][0]) +
                        ", Y=" + formatter.format(graphicsData[selectedMarker][1]);
                bounds = labelsFont.getStringBounds(label, context);
                canvas.setColor(Color.BLACK);
                canvas.drawString(label, (float)(point.getX() + 5.0D), (float)(point.getY() - bounds.getHeight()));
            }
        }

    }

    protected void paintMarkers(Graphics2D canvas)
    {
        canvas.setStroke(markerStroke);
        //canvas.setColor(Color.RED);
        //canvas.setPaint(Color.RED);
        for(Double[] point: graphicsData)
        {
            String in = (point[1] + "").replace(".", "");
            char[] ch = in.toCharArray();
            canvas.setColor(Color.BLACK);
            for (int i = 0; i < ch.length - 1; i++) {
                if (ch[i] > ch[i+1]){
                    canvas.setColor(Color.RED);
                    break;
                }
            }
//            Ellipse2D.Double marker = new Ellipse2D.Double();
//            Point2D.Double center = xyToPoint(point[0], point[1]);
//            Point2D.Double corner = shiftPoint(center, 3, 3);
//            marker.setFrameFromCenter(center, corner);
//            canvas.draw(marker);
//            canvas.fill(marker);
            GeneralPath element = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(point[0], point[1]);
            element.moveTo(lineEnd.getX() + 5, lineEnd.getY() + 5);
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX(), element.getCurrentPoint().getY() - 11);
            element.lineTo(element.getCurrentPoint().getX() + 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX(), element.getCurrentPoint().getY() + 11);
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY() - 11);
            element.moveTo(element.getCurrentPoint().getX() + 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY() + 11);
            canvas.draw(element);
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(graphicsData == null || graphicsData.length == 0) return;

        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        for(int i = 1; i < graphicsData.length; i++)
        {
            if(graphicsData[i][1] < minY)
            {
                minY = graphicsData[i][1];
            }
            if(graphicsData[i][1] > maxY)
            {
                maxY = graphicsData[i][1];
            }
        }

        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);

        if(scale == scaleX)
        {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if(scale == scaleY)
        {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        Graphics2D canvas = (Graphics2D)g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if(showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if(showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    protected int findSelectedPoint(int x, int y)
    {
        if (graphicsData == null) return -1;
        int pos = 0;
        for(Double[] point : graphicsData)
        {
            Point2D.Double screenPoint = xyToPoint(point[0].doubleValue(), point[1].doubleValue());
            double distance = (screenPoint.getX()-x)*(screenPoint.getX()-x)+(screenPoint.getY()-y)*(screenPoint.getY()-y);
            if(distance < 100) return pos;
            pos++;
        }
        return -1;

    }

    public class MouseMotionHandler implements MouseMotionListener
    {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            selectedMarker = findSelectedPoint(e.getX(), e.getY());
            if(selectedMarker >= 1)
            {
                setCursor(Cursor.getPredefinedCursor(12));
            }
            else
            {
                setCursor(Cursor.getPredefinedCursor(0));
            }
            repaint();
        }
    }

}
