/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;

/**
 * 'Modify_Parameters' class receives user inputs and returns the data as a HashMap.
 * Constructor, Initializes the objects and designs the look and feel
 * ShowDialog, based on the frame type set's properties of the frame
 * addItem, adds a new Rapper JPanel with the frame title and functionality
 * checkCreate, automatically creates a new Rapper JPanel if needed
 * refreshTitle, reWrites titles after a Rapper is deleted
 * getContent, converts the user data inputs to a HashMap and returns the result
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Tuesday, May 4, 2020
 */
public class Modify_Parameters extends JFrame {

    /**
     * Object declarations
     */
    private final ParametersType parametersType;
    private final JPanel container;
    private final JPanel content;
    private final List<JPanel> items;
    private String itemTitle;
    public JButton save;
    private HashMap<String, String> map;

    /**
     * Object constructor
     *
     * @param parametersType Parameter type (Frame type)
     * @param map HashMap of user data
     */
    public Modify_Parameters(ParametersType parametersType, HashMap<String, String> map) {

        // JFrame configurations
        this.setSize(420, 500);
        this.setPreferredSize(new Dimension(400, 500));
        this.setVisible(true);
        this.setLayout(null);
        this.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        // Initialize frame type
        this.parametersType = parametersType;

        // Initialize container JPanel
        this.container = new JPanel();
        this.container.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.container.setBackground(new Color(232, 234, 238));

        // Initialize content JPanel
        this.content = new JPanel();
        this.content.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.83)));
        this.content.setBackground(new Color(232, 234, 238));

        // Initialize controls JPanel
        JPanel controls = new JPanel();
        controls.setPreferredSize(new Dimension(this.getWidth(), (int) (this.getHeight() * 0.17)));

        // Save JButton
        save = new JButton("Save");
        save.setPreferredSize(new Dimension(this.getWidth() / 4, 25));
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        controls.add(save);

        //this.add(container);
        this.setContentPane(container);
        container.add(content);
        container.add(controls);

        items = new ArrayList<>();
        this.map = map;
    }

    /**
     * Based on the frame type set's properties of the frame
     */
    public void showDialog() {
        this.requestFocus();    // Request focus to avoid creating new rapper when initializing

        // Set title
        switch (parametersType) {
            case HEADER:
                this.setTitle("Modify Headers");
                itemTitle = "Header";
                break;
            case FORM:
                this.setTitle("Modify Form Data");
                itemTitle = "Form Data";
                break;
            case QUERY:
                this.setTitle("Modify Queries");
                itemTitle = "Query";
                break;
            case AUTH:
                this.setTitle("Modify Authentications");
                itemTitle = "Authentication";
                break;
            case DOCS:
                this.setTitle("Modify Documents");
                itemTitle = "Document";
            case JSON:
                this.setTitle("Modify JSON");
                itemTitle = "JSON";
                break;
        }

        // Load the data if there are any
        for (Map.Entry<String, String> stringStringEntry : map.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                addItem(((Map.Entry) stringStringEntry).getKey().toString(), ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", ""), true);
            else
                addItem(((Map.Entry) stringStringEntry).getKey().toString(), ((Map.Entry) stringStringEntry).getValue().toString().replace(" | OFF", ""), false);

        // Add one last empty item
        addItem("Key", "Value", true);
    }

    /**
     * Adds a new Rapper JPanel with the frame title and functionality
     * @param key  Loaded key
     * @param value Loaded value
     */
    private void addItem(String key, String value, boolean active) {
        // Rapper JPanel
        JPanel rapper = new JPanel();
        rapper.setPreferredSize(new Dimension(this.getWidth(), 65));
        rapper.setBackground(new Color(232, 234, 238));
        content.add(rapper);

        // Title JLabel
        JLabel title = new JLabel("- " + itemTitle + " #" + (items.size() + 1));
        title.setBackground(new Color(232, 234, 238));
        title.setFont(new Font("Calibri", Font.BOLD, 15));

        // Title JPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(232, 234, 238));
        titlePanel.setPreferredSize(new Dimension(this.getWidth() - 20, 25));
        titlePanel.add(title);
        rapper.add(titlePanel);

        // Status JCheckBox
        JCheckBox checkBox = new JCheckBox("");
        checkBox.setName("status");
        checkBox.setBackground(new Color(232, 234, 238));
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.setSelected(active);
        rapper.add(checkBox);

        // Key JTextField
        JTextField keyField = new JTextField(key);
        keyField.setName("Key");
        keyField.setPreferredSize(new Dimension(140, 25));
        keyField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        keyField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        keyField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (keyField.getText().equals("Key"))
                    keyField.setText("");

                checkCreate();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (keyField.getText().equals(""))
                    keyField.setText("Key");
            }
        });
        rapper.add(keyField);

        // Value JTextField
        JTextField valueField = new JTextField(value);
        valueField.setName("Value");
        valueField.setPreferredSize(new Dimension(140, 25));
        valueField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        valueField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        valueField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (valueField.getText().equals("Value"))
                    valueField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (valueField.getText().equals(""))
                    valueField.setText("Value");
            }
        });
        rapper.add(valueField);

        // Delete JButton
        JButton deleteButton = new JButton("Del");
        deleteButton.setPreferredSize(new Dimension(50, 25));
        deleteButton.setName(title.getText());
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteButton.setFont(new Font(deleteButton.getFont().getFamily(), Font.BOLD, deleteButton.getFont().getSize()));
        deleteButton.addActionListener(e -> {

            if (items.size() != 1) {
                boolean isRemoved = false;
                for (JPanel panel : items) {
                    for (Component component : panel.getComponents()) {
                        if (component.getName() != null && component.getName().equals(deleteButton.getName())) {
                            items.remove(panel);
                            content.remove(panel);

                            content.revalidate();
                            content.repaint();
                            isRemoved = true;
                            break;
                        }
                    }
                    if (isRemoved)
                        break;
                }
                refreshTitles();
            }
        });
        rapper.add(deleteButton);

        // Add upload JButton if type is FORM
        if (parametersType == ParametersType.FORM) {
            JButton uploadButton = new JButton("...");
            uploadButton.setPreferredSize(new Dimension(30, 25));
            uploadButton.setFont(new Font(deleteButton.getFont().getFamily(), Font.BOLD, deleteButton.getFont().getSize()));
            uploadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            uploadButton.addActionListener(e -> {
                FileDialog dialog = new FileDialog(this, "Select binary files to load");
                dialog.setMode(FileDialog.LOAD);
                dialog.setVisible(true);
                String file = dialog.getFile();
            });
            rapper.add(uploadButton);
        }

        // Add the rapper tp the List
        items.add(rapper);
    }

    /**
     * Automatically creates a new Rapper JPanel if needed
     */
    private void checkCreate() {
        int count = 0;
        for (JPanel panel : items) {
            int temppairs = 0;
            for (Component component : panel.getComponents())
                if (component instanceof JTextField && (((JTextField) component).getText().equals("Key") || ((JTextField) component).getText().equals("Value") || ((JTextField) component).getText().equals("")))
                    temppairs++;
            if (temppairs == 2)
                count++;
        }

        if (count <= 1) {
            addItem("Key", "Value", true);
            container.revalidate();
            container.repaint();
        }
    }

    /**
     * rewrites all titles after a Rapper is deleted
     */
    private void refreshTitles() {
        for (JPanel panel : items)
            for (Component component : panel.getComponents())
                if (component instanceof JPanel)
                    for (Component item : ((JPanel) component).getComponents())
                        if (item instanceof JLabel)
                            ((JLabel) item).setText("- " + itemTitle + " #" + (items.indexOf(panel) + 1));
    }

    /**
     * Getter: Converts the user data inputs to a HashMap and returns the result
     *
     * @return HashMap of user data
     */
    public HashMap<String, String> getContent() {

        HashMap<String, String> map = new HashMap<>();

        for (JPanel panel : items) {
            String key = null, value = null, status = null;
            for (Component component : panel.getComponents()) {
                if (component instanceof JTextField && component.getName().equals("Key"))   // Key
                    key = ((JTextField) component).getText();
                if (component instanceof JTextField && component.getName().equals("Value"))     // Value
                    value = ((JTextField) component).getText();
                if (component instanceof JCheckBox && component.getName().equals("status"))     // Status
                    if (((JCheckBox) component).isSelected())
                        status = " | ON";
                    else
                        status = " | OFF";
            }

            assert key != null; // Just a precaution

            if ((key.equals("Key") && !Objects.equals(value, "Value")) || (!key.equals("Key") && Objects.equals(value, "Value"))) {
                JOptionPane.showMessageDialog(this, "All fields must be filled before saving!","Err",JOptionPane.WARNING_MESSAGE);
                return null;
            } else if (!key.equals("Key") && !value.equals("Value"))
                map.put(key, value + status);
        }

        return map;
    }
}