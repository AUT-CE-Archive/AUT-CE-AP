/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.io.FileWriter;

/***
 * Settings Class
 * @author Keivan Ipchi Hagh
 * @version 1.0.0
 */
public class Settings extends JFrame {

    /**
     * Object declarations
     */
    private final JPanel container;
    public JButton save;
    boolean isDark, activeRedirect, closeOnExit;

    /**
     * Constructor
     * Initializes the components and variables if any exists
     */
    public Settings() {

        // JFrame configurations
        this.setTitle("Settings");
        this.setSize(420, 250);
        this.setPreferredSize(new Dimension(400, 500));
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        // Initialize container JPanel
        this.container = new JPanel();
        this.container.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.container.setBackground(new Color(232, 234, 238));

        this.setContentPane(container);

        // Load data
        try {
            File readFile = new File("Data//Settings.txt");
            Scanner scanner = new Scanner(readFile);
            List<String> data = new ArrayList<>();
            while (scanner.hasNextLine())
                data.add(scanner.nextLine());

            activeRedirect = data.get(0).contains("true");
            closeOnExit = data.get(1).contains("close");
            isDark = data.get(2).contains("dark");

        } catch (Exception ex) {
            ExceptionHandler.handle(4);
        }
    }

    /**
     * Main Dialog for showing the components
     */
    public void showDialog() {

        // Title JLabel
        JLabel title = new JLabel("Redirect Status");
        title.setBackground(new Color(232, 234, 238));
        title.setFont(new Font("Calibri", Font.BOLD, 15));

        // Title JPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(232, 234, 238));
        titlePanel.setPreferredSize(new Dimension(this.getWidth() - 20, 25));
        titlePanel.add(title);
        container.add(titlePanel);

        // Redirect status
        JCheckBox redirectBox = new JCheckBox("Follow Redirect");
        redirectBox.setLayout(new FlowLayout(FlowLayout.LEFT));
        redirectBox.setName("Redirect");
        redirectBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        redirectBox.setBackground(new Color(232, 234, 238));
        redirectBox.setSelected(activeRedirect);
        container.add(redirectBox);

        // Title JLabel
        title = new JLabel("Exit Mode");
        title.setBackground(new Color(232, 234, 238));
        title.setFont(new Font("Calibri", Font.BOLD, 15));
        container.add(title);

        // Title JPanel
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(232, 234, 238));
        titlePanel.setPreferredSize(new Dimension(this.getWidth() - 20, 25));
        titlePanel.add(title);
        container.add(titlePanel);

        // Exit Mode
        ButtonGroup bg = new ButtonGroup();
        JRadioButton hide = new JRadioButton("Hide on system tray");
        hide.setName("Hide");
        hide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hide.setBackground(new Color(232, 234, 238));
        JRadioButton close = new JRadioButton("Close form");
        close.setName("Close");
        close.setBackground(new Color(232, 234, 238));
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.setSelected(closeOnExit);
        hide.setSelected(!closeOnExit);
        bg.add(close);
        bg.add(hide);
        container.add(hide);
        container.add(close);

        // Title JLabel
        title = new JLabel("Theme");
        title.setBackground(new Color(232, 234, 238));
        title.setFont(new Font("Calibri", Font.BOLD, 15));
        container.add(title);

        // Title JPanel
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(232, 234, 238));
        titlePanel.setPreferredSize(new Dimension(this.getWidth() - 20, 25));
        titlePanel.add(title);
        container.add(titlePanel);

        // Theme Mode
        bg = new ButtonGroup();
        JRadioButton light = new JRadioButton("Light");
        light.setName("Light");
        light.setBackground(new Color(232, 234, 238));
        light.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JRadioButton dark = new JRadioButton("Dark");
        dark.setName("Dark");
        dark.setBackground(new Color(232, 234, 238));
        dark.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(light);
        bg.add(dark);
        dark.setSelected(isDark);
        light.setSelected(!isDark);
        container.add(light);
        container.add(dark);

        // Save JButton
        save = new JButton("Save");
        save.setName("Save");
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        save.setPreferredSize(new Dimension(this.getWidth() / 4, 25));
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        save.addActionListener(e -> save());
        container.add(save);

        // Save JPanel
        JPanel savePanel = new JPanel();
        savePanel.setBackground(new Color(232, 234, 238));
        savePanel.setPreferredSize(new Dimension(this.getWidth() - 20, 30));
        savePanel.add(save);
        container.add(savePanel);
    }

    /**
     * Handles the save function
     * Save the entire settings in a file called "Settings.txt", that's all
     */
    private void save() {

        try {
            File dir = new File("Data");
            dir.mkdirs();

            File data = new File("Data//.txt"); // Create new file if does not already exist
            FileWriter writer = new FileWriter("Data//Settings.txt", false);
            HashMap<String, String> map = new HashMap<>();

            for (Component component : container.getComponents()) {
                if (component instanceof JCheckBox && component.getName().equals("Redirect"))   // Redirect
                    map.put("Redirect ", String.valueOf(((JCheckBox) component).isSelected()));

                if (component instanceof JRadioButton) {
                    // Exit Mode
                    if (component.getName().equals("Close") && ((JRadioButton) component).isSelected())
                        map.put("Exit ", "close");
                    if (component.getName().equals("Hide") && ((JRadioButton) component).isSelected())
                        map.put("Exit ", "hide");

                    // Theme
                    if (component.getName().equals("Light") && ((JRadioButton) component).isSelected())
                        map.put("Theme ", "light");
                    if (component.getName().equals("Dark") && ((JRadioButton) component).isSelected())
                        map.put("Theme ", "dark");
                }
            }

            for (Map.Entry<String, String> stringStringEntry : map.entrySet())
                writer.write(((Map.Entry) stringStringEntry).getKey() + "| " + ((Map.Entry) stringStringEntry).getValue().toString() + "\n");
            writer.close();

            JOptionPane.showMessageDialog(this, "Settings modified successfully. A reboot is required to apply changes.", "Reboot Required", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ExceptionHandler.handle(3);
            ex.printStackTrace();
        }
    }
}