/**
 * Imports
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since Tuesday, May 4, 2020
 */
public class PreviewFrame extends Canvas {

    /**
     * Fields, Objects, variables, ...
     */
    static JComponent page;
    private BufferedImage image;
    private JPanel panel;
    private String url;

    /**
     * This is the constructor as you can see it for yourself
     * It initializes all the things we need in order to proceed
     * @param url This part is a trick. So Shhhhh (:
     */
    public PreviewFrame(String url) { this.url = url; }

    /**
     * Well, this section (method) decides whether to show image or show the HTML contents of the page
     * The mechanic is different, but hopefully one day I marge these two sections into one (Hopefully)
     * @param g
     */
    public void paint(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = null;
        try {
            i = ImageIO.read(new URL(url));
            g.drawImage(i, 0, 0, 400, 400, this);
        } catch (IOException e) { ExceptionHandler.handle(16); }
    }

    /**
     * Um... well what do you expect me to write? SHOW. it does what it says, Duh
     * @param isImg
     */
    public void show(boolean isImg) {

        if (isImg) {
            PreviewFrame m = new PreviewFrame(url);
            JFrame f = new JFrame();
            f.add(m);
            f.setSize(400, 400);
            f.setVisible(true);
        } else {
            JEditorPane jep = new JEditorPane();
            jep.setEditable(false);

            jep.setContentType("html/text");
            try {
                jep.setPage(url);
            } catch (Exception ex) {
                jep.setText("<html>Oops! Something didn't go quite as planned!</html>");
                ex.printStackTrace();
            }

            JScrollPane scrollPane = new JScrollPane(jep);
            JFrame frame = new JFrame("Preview: " + url);
            frame.getContentPane().add(scrollPane);
            frame.setSize(500, 500);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
            frame.show();
        }
    }
}