package Lab4VarA;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class MainFrame extends JFrame
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;

    private GraphicsDisplay display = new GraphicsDisplay();

    private boolean fileLoaded = false;

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
