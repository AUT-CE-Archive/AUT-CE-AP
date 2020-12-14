/**
 * Imports
 */
import org.json.simple.JSONObject;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.util.*;
import java.util.List;

/**
 * NavSection class creates and adds different components to the main Form; This class handles all the heavy works such as the designs, event handlers and other methods
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Monday, April 27, 2020
 */
public class HistorySection {

    /**
     * Object Variables
     */
    public static JPanel container;
    private static JTextField searchBox;
    private static JPopupMenu popupMenu;
    private static JTree treeView;
    private static List<DefaultMutableTreeNode> workspaces;
    private static HashMap<String, DefaultMutableTreeNode> records;
    public static DefaultMutableTreeNode latestClickedWorkspace;

    /**
     * Initializes the components and loads the required data into the frame
     * All the designs go through here, also all the initial values and theme.
     *
     * @param frame Frame object
     */
    public static void initialize(JFrame frame, Color bgColor, Color frColor) {

        /**
         * Container
         */
        container = new JPanel();
        container.setSize(new Dimension(frame.getWidth() / 4, frame.getHeight()));
        container.setBackground(bgColor);

        /**
         * Label and logo
         */
        JLabel logo = new JLabel("WEB MASTER");
        logo.setForeground(frColor);
        logo.setFont(new Font("Cambria", Font.PLAIN, 24));
        logo.setPreferredSize(new Dimension((int)((frame.getWidth() / 4) * 0.6), 45));
        container.add(logo);

        /**
         * Search box text field
         */
        searchBox = new JTextField("Search");
        searchBox.setLocation(50, 0);
        searchBox.setBackground(bgColor);
        searchBox.setForeground(frColor);
        searchBox.setFont(new Font("Cambria", Font.PLAIN, 15));
        searchBox.setPreferredSize(new Dimension((int)(container.getWidth() * 0.7), 25));   // Override size control
        searchBox.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        searchBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBox.getText().equals("Search")) {
                    searchBox.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchBox.getText().equals("")) {
                    searchBox.setText("Search");
                }
            }
        });
        searchBox.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
            }

            public void removeUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                if (!searchBox.getText().equals("") && !searchBox.getText().equals("Search")) {
                    DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                    traverse(root, searchBox.getText());
                    System.out.println("\n\n");
                }
            }
        });
        container.add(searchBox);

        /**
         * New button as panel
         */
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setLocation(0, 55);
        panel.setBackground(bgColor);
        panel.setPreferredSize(new Dimension(20, 25));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel label = new JLabel("+");
        label.setFont(new Font("Calibri", Font.PLAIN, 30));
        label.setForeground(frColor);
        panel.add(label);
        container.add(panel);

        /**
         * Pop up menu
         */
        popupMenu = new JPopupMenu("Popup Menu");
        JMenuItem newRequest = new JMenuItem("New Request");
        newRequest.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JMenuItem newWorkspace = new JMenuItem("New Workspace");
        newWorkspace.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JMenuItem rename = new JMenuItem("Rename");
        rename.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        popupMenu.add(newRequest);
        popupMenu.add(newWorkspace);
        popupMenu.add(rename);
        JPanel finalPanel = panel;

        /**
         * Border panel
         */
        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLocation(0, 0);
        panel.setPreferredSize(new Dimension(container.getWidth() - 30, 1));
        container.add(panel);

        /**
         * Divider panel
         */
        panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLocation(0, 0);
        panel.setPreferredSize(new Dimension(container.getWidth() - 30, (int)(container.getHeight() * 0.03)));
        container.add(panel);

        /**
         * Load Workspaces and records
         */
        workspaces = new ArrayList<>();
        records = new HashMap<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Records");
        DefaultMutableTreeNode uncategorized = new DefaultMutableTreeNode("Uncategorized");

        // Lot's of hard-coded none-sense brute-forced codes, I suppose (:
        try {
            FileReader reader = new FileReader("Data//WorkSpaces.txt");
            Scanner scanner = new Scanner(reader);

            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                DefaultMutableTreeNode workspace = new DefaultMutableTreeNode(temp);
                root.add(workspace);    // Always add the workspace to root node!!!

                List<JSONObject> jsonObjects = URLConnection.readJSON();
                int count = 0;
                for (JSONObject object : jsonObjects) {
                    count++;
                    if (object.get("workspace").equals(temp)) {

                        // Save record to HashMap
                        records.put(object.get("workspace").toString(), new DefaultMutableTreeNode(object.get("-m") + " | " + object.get("url")));

                        // Add records to the workspace
                        workspace.add(new DefaultMutableTreeNode(object.get("-m") + " | " + object.get("url")));
                        count--;
                    }

                    // JIC: Add to Root node if not signed
                    if (count == jsonObjects.size()) {
                        // Save record to HashMap
                        records.put(object.get("workspace").toString(), new DefaultMutableTreeNode(object.get("-m") + " | " + object.get("url")));

                        // Add records to the workspace
                        uncategorized.add(new DefaultMutableTreeNode(object.get("-m") + " | " + object.get("url")));
                    }
                }

                // Save workspace to List
                workspaces.add(workspace);

                if (uncategorized.getChildCount() > 0)
                    root.add(uncategorized);
            }
        } catch (Exception ignored) { };

        /**
         * Tree view
         */
        UIManager.put("Tree.rendererFillBackground", false);
        treeView = new JTree(root);
        treeView.setLocation(0, 0);
        treeView.setPreferredSize(new Dimension((int)(container.getWidth() * 0.8), treeView.getPreferredScrollableViewportSize().height));
        treeView.setBackground(bgColor);
        treeView.setForeground(Color.white);
        treeView.setRootVisible(true);     // Hide the root node
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeView.getCellRenderer();
        renderer.setTextSelectionColor(Color.BLACK);
        renderer.setBackgroundSelectionColor(Color.BLACK);
        renderer.setBorderSelectionColor(Color.black);
        treeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(treeView, e.getX(), e.getY());
                }
            }
        });
        container.add(popupMenu);

        treeView.addTreeSelectionListener(e -> {

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeView.getLastSelectedPathComponent();

            // New request
            newRequest.addActionListener(e1 -> {

                String requestName = JOptionPane.showInputDialog(frame, "Enter Request Name");
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("GET | " + requestName);

                selectedNode.add(newNode);

                // Force reload
                DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
                model.reload((DefaultMutableTreeNode) model.getRoot());
            });

            // New work space
            newWorkspace.addActionListener(e1 -> {

                String workspaceName = JOptionPane.showInputDialog(frame, "Enter Workspace Name");
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(workspaceName);

                selectedNode.add(newNode);

                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode("GET | UnsavedRequest");
                newNode.add(childNode);

                // Force reload
                DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
                model.reload((DefaultMutableTreeNode) model.getRoot());
                treeView.revalidate();
                treeView.repaint();
            });

            // Rename request / work space
            rename.addActionListener(e1 -> {
                DefaultTreeModel model = (DefaultTreeModel) treeView.getModel();
                selectedNode.setUserObject(JOptionPane.showInputDialog(frame, "Enter The New Name"));
                model.nodeChanged(selectedNode);
            });

            // Load the selected Record
            for (JSONObject object : Objects.requireNonNull(URLConnection.readJSON()))
                if ((object.get("-m") + " | " + object.get("url")).equals(selectedNode.getUserObject().toString()) && object.get("workspace").equals(latestClickedWorkspace.getUserObject().toString())) {

                    System.out.println(object.toJSONString());  // Print Full JSON
                    try {
                        BodySection.targetField.setText(object.get("url").toString().substring(0, object.get("url").toString().indexOf("?")));  //  Set URL & Format query
                    } catch (Exception ex) {
                        BodySection.targetField.setText(object.get("url").toString());  //  Set URL
                    }

                    switch (object.get("-m").toString()) {  // Set Method
                        case "GET":
                            BodySection.methodSelector.setSelectedIndex(0);
                            break;
                        case "POST":
                            BodySection.methodSelector.setSelectedIndex(1);
                            break;
                        case "PUT":
                            BodySection.methodSelector.setSelectedIndex(2);
                            break;
                        case "PATCH":
                            BodySection.methodSelector.setSelectedIndex(3);
                            break;
                        case "DELETE":
                            BodySection.methodSelector.setSelectedIndex(4);
                            break;
                    }

                    // Set headers, queries, form, auth, ...
                    try {
                        HashMap<String, String> query = new HashMap<>();
                        for (String str: object.get("url").toString().substring(object.get("url").toString().indexOf("?") + 1).split("&"))
                            query.put(str.split("=")[0], str.split("=")[1] + " | ON");
                        BodySection.queryMap = query;
                    } catch (Exception ignored) { };

                    BodySection.headerMap = (HashMap<String, String>) object.get("-H");
                    BodySection.formMap = (HashMap<String, String>) object.get("-d");
                    BodySection.JSONMap = (HashMap<String, String>) object.get("-j");

                    // Set visual treeview
                    BodySection.headersNode.removeAllChildren();
                    BodySection.formNode.removeAllChildren();
                    BodySection.JSONNode.removeAllChildren();
                    BodySection.queriesNode.removeAllChildren();
                    BodySection.authsNode.removeAllChildren();
                    BodySection.docsNode.removeAllChildren();
                    BodySection.loadTree();
                }

            // Save the last selected workspace
            if (selectedNode.getChildCount() > 0)
                latestClickedWorkspace = selectedNode;
        });
        container.add(treeView);

        frame.getContentPane().add(container, BorderLayout.LINE_START);
        //frame.add(container, BorderLayout.LINE_START);
    }

    /**
     * Traverses through the tree and highlights the queries
     * IDK how this works really but i like it, it's a nice algorithm
     *
     * @param node         Root node
     * @param searchPhrase Search phrase
     */
    public static void traverse(DefaultMutableTreeNode node, String searchPhrase) {

        int childCount = node.getChildCount();
        System.out.println("---" + node.toString() + "---");

        for (int i = 0; i < childCount; i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (childNode.getChildCount() > 0)
                traverse(childNode, searchPhrase);
            else if (childNode.toString().contains(searchPhrase)) {
                System.out.println(childNode.toString());
            }
        }

        System.out.println("+++" + node.toString() + "+++");
    }
}