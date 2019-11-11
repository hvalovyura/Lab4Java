package Lab4VarA;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainFrame extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;

    private GraphicsDisplay display = new GraphicsDisplay();

    private boolean fileLoaded = false;

    public MainFrame()
    {
        super("Построение графиков функций на основе подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        setExtendedState(MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        fileMenu.add(openGraphicsAction);

        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);

        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(false);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        getContentPane().add(display, BorderLayout.CENTER);
    }

    public static void main(String[] args)
    {
        System.out.println("qq");
    }

    public class GraphicsMenuListener implements MenuListener
    {

        @Override
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    }
}
