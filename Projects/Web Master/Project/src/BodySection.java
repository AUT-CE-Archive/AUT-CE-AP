/**
 * Imports
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

/**
 * BodySection class creates and adds different components to the main Form; This class handles all the heavy works such as the designs, event handlers and other methods
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Tuesday, April 28, 2020
 */
public class BodySection {

    /**
     * Object variables and attributes
     */
    public static JComboBox methodSelector;
    public static JTextField targetField;
    private static JButton send;
    public static HashMap<String, String> formMap, queryMap, authMap, headerMap, docsMap, JSONMap;
    public static JTree treeView;
    public static DefaultMutableTreeNode formNode, authsNode, headersNode, queriesNode, docsNode, JSONNode, fileNode, URL;
    private static File uploadFile;

    /**
     * Initializes variables, properties, attributes and designs
     *
     * @param frame  Parent frame
     * @param width Initial width
     * @param bgColor   Theme backColor
     * @param frColor   Theme foreColor
     */
    public static void initialize(JFrame frame, int width, Color bgColor, Color frColor) {
        headerMap = new HashMap<>();
        formMap = new HashMap<>();
        queryMap = new HashMap<>();
        authMap = new HashMap<>();
        headerMap = new HashMap<>();
        docsMap = new HashMap<>();
        JSONMap = new HashMap<>();

        // Container JPanel setup
        JPanel container = new JPanel();
        FlowLayout layout = (FlowLayout) container.getLayout();
        layout.setVgap(0);
        container.setName("ConfigSection_container");
        container.setBounds(0, 0, width, frame.getHeight());
        container.setBackground(bgColor);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLocation(0, 0);
        panel.setPreferredSize(new Dimension(container.getWidth(), 10));
        container.add(panel);

        // ComboBox JComboBox
        String[] methods = new String[]{"GET", "POST", "PUT", "PATCH", "DELETE"};
        methodSelector = new JComboBox(methods);
        methodSelector.setLocation(0, 0);
        methodSelector.setFont(new Font("Cambria", Font.PLAIN, 15));
        methodSelector.setBackground(bgColor);
        methodSelector.setForeground(Color.BLACK);
        methodSelector.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Target fields JTextField
        targetField = new JTextField();
        targetField.setForeground(frColor);
        targetField.setBackground(bgColor);
        targetField.setLocation(0, 0);
        targetField.setFont(new Font("Cambria", Font.PLAIN, 15));
        targetField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        targetField.setPreferredSize(new Dimension((int) (container.getWidth() * 0.365), (int) (container.getHeight() * 0.04)));
        targetField.setText("URL");
        targetField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (targetField.getText().equals("URL")) {
                    targetField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (targetField.getText().equals("")) {
                    targetField.setText("URL");
                }
            }
        });

        // Send JButton
        send = new JButton("Send");
        send.setLocation(0, 0);
        send.setPreferredSize(new Dimension((int) (container.getWidth() * 0.1), (int) (container.getHeight() * 0.05)));
        send.setForeground(Color.BLACK);
        send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        send.addActionListener(e -> {

            // Send Request
            try {
                // Headers
                String headers = " -H ";
                try {
                    boolean headerIsSet = false;
                    for (Map.Entry<String, String> stringStringEntry : headerMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            headers += stringStringEntry.getKey() + ":" + stringStringEntry.getValue().split(" | ")[0] + ";";
                            headerIsSet = true;
                        }
                    if (!headerIsSet)
                        headers = "";

                    if (headerMap.size() == 0)
                        headers = "";
                } catch (Exception ex) { headers = ""; };

                // Form Data
                String formData = " -d ";
                try {
                    boolean formDataIsSet = false;
                    for (Map.Entry<String, String> stringStringEntry : formMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            formData += stringStringEntry.getKey() + "=" + stringStringEntry.getValue().split(" | ")[0] + "&";
                            formDataIsSet = true;
                        }
                    if (!formDataIsSet)
                        formData = "";

                    if (formMap.size() == 0)
                        formData = "";
                } catch (Exception ex) { formData = ""; };

                // Auths
                String authKey = "", authValue = "";
                try {
                    for (Map.Entry<String, String> stringStringEntry : authMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            authKey = stringStringEntry.getKey();
                            authValue = stringStringEntry.getValue().split(" | ")[0];
                        }
                } catch (Exception ex) { authKey = ""; authValue = ""; };

                // JSON
                String json = " -j ";
                try {
                    int count = 0;
                    for (Map.Entry<String, String> stringStringEntry : JSONMap.entrySet()) {
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            json += stringStringEntry.getKey() + ":" + stringStringEntry.getValue().split(" | ")[0] + ",";
                            count++;
                        }
                    }
                    if (JSONMap.size() == 0 || count == 0)
                        json = "";
                    else
                        json = json.substring(0, json.length() - 1);
                } catch (Exception ex) { json = ""; };

                // Queries
                String query = "?";
                try {
                    for (Map.Entry<String, String> stringStringEntry : queryMap.entrySet()) {
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON"))
                            query += stringStringEntry.getKey() + "=" + stringStringEntry.getValue().split(" | ")[0] + "&";
                    }
                    if (query.equals("?"))
                        query = "";
                    else
                        query = query.substring(0, query.length() - 1);
                } catch (Exception ex) { query = ""; };

                // Upload File
                String path = "";
                try {
                    path = uploadFile.getAbsolutePath();
                } catch (Exception ignored) { }

                // ----------------------------------------------------------------------------
                initiateRequest(query, headers, formData, json, authKey, authValue, path, bgColor, frColor);
                // ----------------------------------------------------------------------------

            } catch (Exception ex) { ExceptionHandler.handle(10); ex.printStackTrace(); }
        });

        // Border
        JPanel border = new JPanel();
        border.setBackground(Color.BLACK);
        border.setPreferredSize(new Dimension(container.getWidth(), 1));

        // Rapper JPanel
        JPanel rapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));    // Manual control over JPanel's layout
        rapper.setLocation(0, 0);
        rapper.setBackground(bgColor);
        rapper.setPreferredSize(new Dimension(container.getWidth(), (int) (container.getHeight() * 0.1)));
        rapper.setBorder(new EmptyBorder(0, 0, 0, 0));
        rapper.add(methodSelector);
        rapper.add(targetField);
        rapper.add(send);
        rapper.add(border);
        container.add(rapper);

        // Tree view Data Initialization
        URL = new DefaultMutableTreeNode("Content");
        formNode = new DefaultMutableTreeNode("Form");
        authsNode = new DefaultMutableTreeNode("Auths");
        queriesNode = new DefaultMutableTreeNode("Queries");
        headersNode = new DefaultMutableTreeNode("Headers");
        docsNode = new DefaultMutableTreeNode("Docs");
        JSONNode = new DefaultMutableTreeNode("JSON");
        fileNode = new DefaultMutableTreeNode("File(s)");
        URL.add(formNode);
        URL.add(JSONNode);
        URL.add(headersNode);
        URL.add(authsNode);
        URL.add(queriesNode);
        URL.add(docsNode);
        URL.add(fileNode);

        // Tree view
        treeView = new JTree(URL);
        treeView.setBackground(bgColor);
        treeView.setForeground(frColor);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeView.getCellRenderer();
        renderer.setTextSelectionColor(frColor);
        renderer.setBackgroundSelectionColor(frColor);
        renderer.setBorderSelectionColor(frColor);
        renderer.setForeground(frColor);

        rapper = new JPanel();
        rapper.setPreferredSize(new Dimension(container.getWidth() * 2 / 3, container.getHeight()));
        rapper.setBorder(BorderFactory.createEmptyBorder(0, 250, 50, 50));
        rapper.setBackground(bgColor);
        rapper.add(treeView);
        container.add(rapper);

        // Modify body form JButton
        JButton modifyForm = new JButton("Modify Form");
        modifyForm.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyForm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyForm.addActionListener(e -> {
            if (formMap == null)
                formMap = new HashMap<>();
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.FORM, formMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                formMap = modifyParameters.getContent();
                if (formMap != null) {
                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Modify headers JButton
        JButton modifyHeaders = new JButton("Modify Headers");
        modifyHeaders.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyHeaders.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyHeaders.addActionListener(e -> {
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.HEADER, headerMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                headerMap = modifyParameters.getContent();
                if (headerMap != null) {

                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Modify queries JButton
        JButton modifyQueries = new JButton("Modify Queries");
        modifyQueries.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyQueries.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyQueries.addActionListener(e -> {
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.QUERY, queryMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                queryMap = modifyParameters.getContent();
                if (queryMap != null) {
                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Modify auths JButton
        JButton modifyAuths = new JButton("Modify Auths");
        modifyAuths.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyAuths.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyAuths.addActionListener(e -> {
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.AUTH, authMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                authMap = modifyParameters.getContent();
                if (authMap != null) {
                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Modify docs JButton
        JButton modifyDocs = new JButton("Modify Docs");
        modifyDocs.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyDocs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyDocs.addActionListener(e -> {
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.DOCS, docsMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                docsMap = modifyParameters.getContent();
                if (docsMap != null) {
                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Modify JSON JButton
        JButton modifyJSON = new JButton("Modify JSON");
        modifyJSON.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        modifyJSON.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        modifyJSON.addActionListener(e -> {
            Modify_Parameters modifyParameters = new Modify_Parameters(ParametersType.JSON, JSONMap);
            modifyParameters.showDialog();
            modifyParameters.save.addActionListener(ee -> {
                JSONMap = modifyParameters.getContent();
                if (JSONMap != null) {
                    modifyParameters.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // Close dialog frame
                    loadTree();
                }
            });
        });

        // Upload File JButton
        JButton uploader = new JButton("Upload File");
        uploader.setPreferredSize(new Dimension(container.getWidth() / 5, 22));
        uploader.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        uploader.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt", "text"));

            if (fileChooser.showOpenDialog(uploader) == JFileChooser.APPROVE_OPTION) {
                uploadFile = fileChooser.getSelectedFile();
                fileNode.add(new DefaultMutableTreeNode(uploadFile.getName()));

                // Force reload
                DefaultTreeModel model = (DefaultTreeModel) (treeView.getModel());
                model.reload();
                treeView.revalidate();
                treeView.repaint();
            }
        });

        // Rapper JPanel for JButtons
        rapper = new JPanel();
        rapper.setPreferredSize(new Dimension(container.getWidth() / 5, container.getHeight()));
        rapper.setBackground(bgColor);
        rapper.add(modifyForm);
        rapper.add(modifyJSON);
        rapper.add(modifyHeaders);
        rapper.add(uploader);
        rapper.add(modifyAuths);
        rapper.add(modifyQueries);
        rapper.add(modifyDocs);
        container.add(rapper);

        JButton save = new JButton("Save");
        save.setLocation(0, 0);
        save.setPreferredSize(new Dimension((int) (container.getWidth() * 0.1), (int) (container.getHeight() * 0.05)));
        save.setForeground(Color.BLACK);
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        save.addActionListener(e -> {

            // Send Request
            try {
                // Headers
                String headers = " -H ";
                try {
                    boolean headerIsSet = false;
                    for (Map.Entry<String, String> stringStringEntry : headerMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            headers += stringStringEntry.getKey() + ":" + stringStringEntry.getValue().split(" | ")[0] + ";";
                            headerIsSet = true;
                        }
                    if (!headerIsSet)
                        headers = "";

                    if (headerMap.size() == 0)
                        headers = "";
                } catch (Exception ex) { headers = ""; };

                // Form Data
                String formData = " -d ";
                try {
                    boolean formDataIsSet = false;
                    for (Map.Entry<String, String> stringStringEntry : formMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            formData += stringStringEntry.getKey() + "=" + stringStringEntry.getValue().split(" | ")[0] + "&";
                            formDataIsSet = true;
                        }
                    if (!formDataIsSet)
                        formData = "";

                    if (formMap.size() == 0)
                        formData = "";
                } catch (Exception ex) { formData = ""; };

                // Auths
                String authKey = "", authValue = "";
                try {
                    for (Map.Entry<String, String> stringStringEntry : authMap.entrySet())
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON")) {
                            authKey = stringStringEntry.getKey();
                            authValue = stringStringEntry.getValue().split(" | ")[0];
                        }
                } catch (Exception ex) { authKey = ""; authValue = ""; };

                // JSON
                String json = " -j ";
                try {
                    for (Map.Entry<String, String> stringStringEntry : JSONMap.entrySet()) {
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON"))
                            json += stringStringEntry.getKey() + ":" + stringStringEntry.getValue().split(" | ")[0] + ",";
                    }
                    if (JSONMap.size() == 0)
                        json = "";
                    else
                        json = json.substring(0, json.length() - 1);
                } catch (Exception ex) { json = ""; };

                // Queries
                String query = "?";
                try {
                    for (Map.Entry<String, String> stringStringEntry : queryMap.entrySet()) {
                        if (stringStringEntry.getValue().split(" | ")[2].equals("ON"))
                            query += stringStringEntry.getKey() + "=" + stringStringEntry.getValue().split(" | ")[0] + "&";
                    }
                    if (query.equals("?"))
                        query = "";
                    else
                        query = query.substring(0, query.length() - 1);
                } catch (Exception ex) { query = ""; };

                // Upload File
                String path = "";
                try {
                    path = uploadFile.getAbsolutePath();
                } catch (Exception ignored) { }

                // ----------------------------------------------------------------------------
                initiateRequest(query + " -save " + HistorySection.latestClickedWorkspace.toString(), headers, formData, json, authKey, authValue, path, bgColor, frColor);
                // ----------------------------------------------------------------------------

            } catch (Exception ex) { ExceptionHandler.handle(10); ex.printStackTrace(); }
        });
        container.add(save);

        frame.getContentPane().add(container, BorderLayout.CENTER);
        //frame.add(container, BorderLayout.CENTER);
    }

    /**
     * Loads the lists into the tree, Update existing tree or remove items.
     * Also forces the refresh manually, Java is shit you have to do all things manually :| WTF
     */
    public static void loadTree() {

        headersNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : headerMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                headersNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        authsNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : authMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                authsNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        formNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : formMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                formNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        docsNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : docsMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                docsNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        queriesNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : queryMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                queriesNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        JSONNode.removeAllChildren();
        for (Map.Entry<String, String> stringStringEntry : JSONMap.entrySet())
            if (((Map.Entry) stringStringEntry).getValue().toString().contains("ON"))
                JSONNode.add(new DefaultMutableTreeNode(((Map.Entry) stringStringEntry).getKey() + " | " + ((Map.Entry) stringStringEntry).getValue().toString().replace(" | ON", "")));

        // Refresh tree
        treeView.revalidate();
        treeView.repaint();

        // Force reload
        DefaultTreeModel model = (DefaultTreeModel) (treeView.getModel());
        model.reload();
    }

    /**
     * Checks wheter settings for auto follow redirect is on or off by scanning through the settings file located in "Data//Settings.txt"
     * @return
     */
    public static boolean isFollowRedirect() {
        try {
            Scanner myReader = new Scanner(new File("Data//Settings.txt"));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.contains("Redirect"))
                    return data.contains("true");
            }
            myReader.close();
        } catch (Exception ex) { return false; }

        return false;
    }

    /**
     * Initiates the request
     */
    private static void initiateRequest(String query, String headers, String formData, String json, String authKey, String authValue, String path, Color bgColor, Color frColor) {

        SwingWorker sw1 = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                // Send request & handle the stuff & Set follow redirect
                URLConnection.main(new String[]{"curl " + targetField.getText() + query + " -M " + methodSelector.getSelectedItem().toString() + headers + formData + json + ((isFollowRedirect()) ? " -f" : ""), authKey, authValue, path});
                return true;
            }

            @Override
            protected void done() {
                // Print Response
                ResponseSection.rawTA.setText(URLConnection.getResponseContent());

                String time = URLConnection.getResponseTime();
                try {
                    time = time.substring(0, 4);
                } catch (Exception ignored) {}
                ResponseSection.statusLabel.setText(URLConnection.getResponseCode());
                ResponseSection.batchLabel.setText(URLConnection.getResponseLength());
                ResponseSection.timeFrameLabel.setText(time + "s");

                // Send headers data to ResponseSection
                HashMap<String, String> combinedHeaders = URLConnection.getResponseHeaders();
                combinedHeaders.putAll(headerMap);
                ResponseSection.allHeaders = combinedHeaders;
                for (Map.Entry<String, String> stringStringEntry : combinedHeaders.entrySet())
                    try {
                        ResponseSection.addHeader(((Map.Entry) stringStringEntry).getKey().toString(), ((Map.Entry) stringStringEntry).getValue().toString(), bgColor, frColor);
                    } catch (Exception ignored) {}

                // Add copy to clipboard button
                if (headerMap.size() != 0)
                    ResponseSection.headers.add(ResponseSection.copy);
            }
        };

        sw1.execute();
    }
}