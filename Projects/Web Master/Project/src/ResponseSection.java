/**
 * Imports
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConfigSection class creates and adds different components to the main Form; This class handles all the heavy works such as the designs, event handlers and other methods
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Tuesday, April 28, 2020
 */
public class ResponseSection {

    /**
     * Variables, components, ...
     */
    private static JPanel container, status, timeFrame, batch;
    public static JLabel statusLabel, timeFrameLabel, batchLabel;
    public static JTabbedPane pane;
    public static JPanel raw, headers, preview;
    public static JTextArea rawTA;
    private static List<JPanel> headersList;
    public static JButton copy;
    public static HashMap<String, String> allHeaders = new HashMap<>();

    /**
     * Initialize method, news components, variables, objects, loads data and handles events
     * @param frame Parent frame
     * @param width width
     * @param bgColor   back Color
     * @param frColor   fore Color
     */
    public static void initialize(JFrame frame, int width, Color bgColor, Color frColor) {

        headersList = new ArrayList<>();

        /**
         * Container setup
         */
        container = new JPanel();
        FlowLayout layout = (FlowLayout) container.getLayout();
        layout.setVgap(0);
        container.setName("ConfigSection_container");
        container.setBounds(width, 0, 320, frame.getHeight());
        container.setBackground(bgColor);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setPreferredSize(new Dimension(container.getWidth() - 30, 10));
        container.add(panel);

        /**
         * Status panel and label
         */
        status = new JPanel(new GridBagLayout());
        status.setPreferredSize(new Dimension(150, 25));
        status.setBackground(new Color(225, 225, 225));
        status.setBorder(BorderFactory.createEtchedBorder());
        statusLabel = new JLabel("Status");
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setFont(new Font("Cambria", Font.PLAIN, 15));
        statusLabel.addPropertyChangeListener(evt -> {
            switch (statusLabel.getText()) {
                case "200 OK":
                    status.setBackground(Color.GREEN);
                    break;
                case "403 FORBIDDEN":
                case "400 Bad Request":
                    status.setBackground(Color.RED);
                    break;
                case "404 NOT FOUND":
                    status.setBackground(Color.YELLOW);
                    break;
                default:
                    status.setBackground(new Color(225, 225, 225));
                    break;
            }
        });
        status.add(statusLabel);

        /**
         * Time frame panel and label
         */
        timeFrame = new JPanel(new GridBagLayout());
        timeFrame.setPreferredSize(new Dimension(75, 25));
        timeFrame.setBackground(new Color(225, 225, 225));
        timeFrame.setBorder(BorderFactory.createEtchedBorder());
        timeFrameLabel = new JLabel("Ping");
        timeFrameLabel.setForeground(Color.BLACK);
        timeFrameLabel.setFont(new Font("Cambria", Font.PLAIN, 15));
        timeFrame.add(timeFrameLabel);

        /**
         * Batch panel and label
         */
        batch = new JPanel(new GridBagLayout());
        batch.setPreferredSize(new Dimension(75, 25));
        batch.setBackground(new Color(225, 225, 225));
        batch.setBorder(BorderFactory.createEtchedBorder());
        batchLabel = new JLabel("Batch");
        batchLabel.setForeground(Color.BLACK);
        batchLabel.setFont(new Font("Cambria", Font.PLAIN, 15));
        batch.add(batchLabel);

        /**
         * Rapper
         */
        JPanel rapper = new JPanel(new FlowLayout(FlowLayout.LEFT));    // Manual control over JPanel's layout
        rapper.setLocation(0, 0);
        rapper.setBackground(bgColor);
        rapper.setPreferredSize(new Dimension(container.getWidth(), (int)(container.getHeight() * 0.08)));
        rapper.setBorder(new EmptyBorder(0, 0, 0, 0));
        rapper.add(status);
        rapper.add(timeFrame);
        rapper.add(batch);
        container.add(rapper);

        /**
         * Tapped pane
         */
        pane = new JTabbedPane();
        pane.setBounds(666, 0, container.getWidth(), container.getHeight());
        pane.setPreferredSize(new Dimension(container.getWidth(), container.getHeight() - rapper.getHeight()));
        pane.setBackground(new Color(50, 50, 50));
        panel.setForeground(frColor);
        container.add(pane);

        /**
         * Raw panel
         */
        raw = new JPanel();
        raw.setSize(new Dimension(pane.getWidth(), pane.getHeight()));
        raw.setForeground(frColor);
        raw.setBackground(new Color(50, 50, 50));
        pane.add("Raw", raw);

        /**
         * Raw Text Area
         */
        rawTA = new JTextArea();
        rawTA.setSize(new Dimension(raw.getWidth(), raw.getHeight() - 100));
        rawTA.setPreferredSize(new Dimension(raw.getWidth(), raw.getHeight() - 100));
        rawTA.setFont(new Font("Cambria", Font.PLAIN, 12));
        rawTA.setEnabled(false);
        rawTA.setForeground(bgColor);
        rawTA.setBackground(new Color(50, 50, 50));
        raw.add(rawTA);

        /**
         * Preview panel
         */
        preview = new JPanel();
        preview.setSize(new Dimension(pane.getWidth(), pane.getHeight()));
        preview.setForeground(frColor);
        preview.setBackground(new Color(50, 50, 50));
        pane.add("Preview", preview);

        JButton _preview = new JButton("Preview");
        _preview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        _preview.addActionListener(e -> {

            boolean isImg = false;
            for (Map.Entry<String, String> entry: allHeaders.entrySet()) {
                try {
                    if (entry.getKey().equals("Content-Type") && entry.getValue().contains("image")) {
                        isImg = true;
                        break;
                    }
                } catch (Exception ignored) { }
            }

            // Queries
            String query = "?";
            try {
                for (Map.Entry<String, String> stringStringEntry : BodySection.queryMap.entrySet()) {
                    if (stringStringEntry.getValue().split(" | ")[2].equals("ON"))
                        query += stringStringEntry.getKey() + "=" + stringStringEntry.getValue().split(" | ")[0] + "&";
                }
                if (query.equals("?"))
                    query = "";
                else
                    query = query.substring(0, query.length() - 1);
            } catch (Exception ex) { query = ""; };

            PreviewFrame previewFrame = new PreviewFrame(BodySection.targetField.getText() + query);
            previewFrame.show(isImg);
        });
        preview.add(_preview);

        /**
         * Headers panel
         */
        headers = new JPanel();
        headers.setSize(new Dimension(pane.getWidth(), pane.getHeight()));
        headers.setForeground(frColor);
        headers.setBackground(new Color(50, 50, 50));
        pane.add("Headers", headers);

        /**
         * Clipboard button
         */
        copy = new JButton("Copy To ClipBoard");
        copy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        copy.setForeground(frColor);
        copy.addActionListener(e -> {
            String content = "";

            // Traverse the list for Keys and Values
            for (JPanel p : headersList)
                for (Component component : p.getComponents())
                    if (component instanceof JTextField && component.getName().equals("Key"))
                        content += (((JTextField) component).getText() + " ");
                    else if (component instanceof JTextField && component.getName().equals("Value"))
                        content += (((JTextField) component).getText() + "\n");

            // Copy
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(content), null);

            JOptionPane.showMessageDialog(frame,"Successfully copied to ClipBoard.");
        });

        frame.getContentPane().add(container, BorderLayout.LINE_END);
        //frame.add(container, BorderLayout.LINE_END);
    }

    /**
     * Adds a new submitted header
     * This works like a template, like in web and stuff. More much less cooler cause it's WRITTEN IN JAVAAAAAAA!
     * @param keyStr    Map Key
     * @param valueStr  Map Value
     * @param bgColor   back color
     * @param frColor   fore color
     */
    public static void addHeader(String keyStr, String valueStr, Color bgColor, Color frColor) {

        JPanel rapper = new JPanel();
        rapper.setBackground(new Color(50, 50, 50));
        rapper.setSize(headers.getWidth(), headers.getHeight());
        rapper.setPreferredSize(new Dimension(headers.getWidth(), 25));

        JTextField key = new JTextField(keyStr);
        key.setName("Key");
        key.setPreferredSize(new Dimension((int)(container.getWidth() * 0.4), 20));
        key.setBackground(bgColor);
        key.setEditable(false);
        key.setForeground(frColor);
        key.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextField value = new JTextField(valueStr.replace(" | ON", "").replace(" | OFF", ""));
        value.setName("Value");
        value.setForeground(frColor);
        value.setBackground(bgColor);
        value.setEditable(false);
        value.setPreferredSize(new Dimension((int)(container.getWidth() * 0.4), 20));
        value.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        rapper.add(key);
        rapper.add(value);

        headersList.add(rapper);
        headers.add(rapper);
    }
}