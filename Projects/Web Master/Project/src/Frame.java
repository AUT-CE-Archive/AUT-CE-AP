/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main JFrame of the program
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Tuesday, April 28, 2020
 */
public class Frame {

    /**
     * Objects, variables, fields, ...
     */
    JFrame frame;
    boolean isDark, activeRedirect, closeOnExit;

    /**
     * Constructor
     * Initialize objects, variables, set them and load data
     * This is ground 0, all things are build on top of this dude right here (;
     */
    public Frame() {
        // Frame attributes
        this.frame = new JFrame("Web Master Project - Developed by Keivan Ipchi Hagh");
        this.frame.setSize(new Dimension(1000, 500));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation(dim.width / 2 - this.frame.getSize().width / 2, dim.height / 2 - this.frame.getSize().height / 2);

        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        try {
            File readFile = new File("Data//Settings.txt");
            Scanner scanner = new Scanner(readFile);
            List<String> data = new ArrayList<>();
            while (scanner.hasNextLine())
                data.add(scanner.nextLine());

            activeRedirect = data.get(0).contains("true");
            closeOnExit = data.get(1).contains("close");
            isDark = data.get(2).contains("dark");

            // Config closing action
            if (data.get(1).contains("hide"))
                frame.addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent windowEvent) {
                        frame.setExtendedState(JFrame.ICONIFIED);
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                });

        } catch (Exception ex) {
            ExceptionHandler.handle(5);
        }

        // Initialize inner components
        MenuSection.initialize(frame);

        if (isDark)
            HistorySection.initialize(frame,  new Color(50, 50, 50), Color.white);
        else
            HistorySection.initialize(frame, new Color(232, 234, 238), Color.BLACK);

        DividerSection.initialize(frame, HistorySection.container.getWidth());

        if (isDark)
            BodySection.initialize(frame, frame.getWidth() * 2 / 3, new Color(50, 50, 50), Color.white);
        else
            BodySection.initialize(frame, frame.getWidth() * 2 / 3, new Color(232, 234, 238), Color.BLACK);

        DividerSection.initialize(frame, frame.getWidth() * 2 / 3);

        if (isDark)
            ResponseSection.initialize(frame, 667, new Color(50, 50, 50), Color.white);
        else
            ResponseSection.initialize(frame, 667, new Color(232, 234, 238), Color.BLACK);

        // Frame layout
        this.frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        this.frame.setVisible(true);
    }
}