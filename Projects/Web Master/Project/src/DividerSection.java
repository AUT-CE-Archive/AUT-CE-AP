/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;

/**
 * DividerSection creates a transparent JPanel to divide main sections
 * Simple separator for the different parts of UI
 */
public class DividerSection {

    /**
     * Initialize method
     * @param frame Parent frame
     * @param x X-Axis
     */
    public static void initialize(JFrame frame, int x) {
        JPanel panel = new JPanel();
        panel.setLocation(x, 0);
        panel.setPreferredSize(new Dimension(1, frame.getHeight()));
        panel.setBackground(Color.BLACK);
        panel.setSize(1, frame.getHeight());
        frame.add(panel);
    }

}
