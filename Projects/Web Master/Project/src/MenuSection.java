/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * MenuSection class creates and adds different components to the main Form; This class handles all the heavy works such as the designs, event handlers and other methods
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Monday, May 5, 2020
 */
public class MenuSection {

    /**
     * This method initializes, designs and adds the functionality to the frame meu
     * The top nav bar you see, well it's all here
     */
    public static void initialize(JFrame frame) {

        // Initialize menu bar
        /**
         * Components
         */
        JMenuBar menuBar = new JMenuBar();
        menuBar.setName("menuSection_menuBar");

        // Initialize menu sections
        JMenu application = new JMenu("Application");
        application.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenu edit = new JMenu("Edit");
        edit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenu view = new JMenu("View");
        view.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenu help = new JMenu("Help");
        help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Initialize menu items
        JMenuItem application_settings = new JMenuItem("Settings");
        application_settings.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        application_settings.addActionListener(e -> {
            Settings settings = new Settings();
            settings.showDialog();

            settings.save.addActionListener(ee -> {
                settings.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
            });
        });

        JMenuItem application_quit = new JMenuItem("Quit");
        application_quit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        application_quit.addActionListener(e -> {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
        });

        JMenuItem edit_copy = new JMenuItem("Copy");
        edit_copy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit_copy.setAccelerator(KeyStroke.getKeyStroke("control C"));

        JMenuItem edit_paste = new JMenuItem("Paste");
        edit_paste.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit_paste.setAccelerator(KeyStroke.getKeyStroke("control V"));

        JMenuItem edit_selectAll = new JMenuItem("Select All");
        edit_selectAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit_selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));

        JMenuItem edit_cut = new JMenuItem("Cut");
        edit_cut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        edit_cut.setAccelerator(KeyStroke.getKeyStroke("control X"));

        JMenuItem view_fullScreen = new JMenuItem("Toggle FullScreen");
        view_fullScreen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        view_fullScreen.setAccelerator(KeyStroke.getKeyStroke("F11"));
        view_fullScreen.addActionListener(e -> {
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);   // Maximize frame
        });

        JMenuItem view_sideBar = new JMenuItem("Toggle SideBar");
        view_sideBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        view_sideBar.setAccelerator(KeyStroke.getKeyStroke("F10"));

        JMenuItem help_about = new JMenuItem("About");
        help_about.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        help_about.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Developed by: Keivan Ipchi Hagh\nStudent ID: 9831073\nE-mail: ipchi1380@gmail.com", "About Developer", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem help_help = new JMenuItem("Help");
        help_help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        help_help.setAccelerator(KeyStroke.getKeyStroke("control H"));
        help_help.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Nothing to show for now ):", "Help", JOptionPane.INFORMATION_MESSAGE);
        });

        // Add sections to bar
        menuBar.add(application);
        menuBar.add(edit);
        menuBar.add(view);
        menuBar.add(help);

        // Add items to sections
        application.add(application_settings);
        application.add(application_quit);

        edit.add(edit_copy);
        edit.add(edit_paste);
        edit.add(edit_cut);
        edit.add(edit_selectAll);

        view.add(view_fullScreen);
        view.add(view_sideBar);

        help.add(help_about);
        help.add(help_help);

        // Add menu bar to frame
        frame.setJMenuBar(menuBar);
    }
}