import javax.swing.*;

public class Main {

    /**
     * Well, here is where it all begins basically (:
     * We set the Look and Feel here! which is really important and handle some rare errors!!!!
     * @param args Arguments which have o use, at all
     */
    public static void main(String[] args) {

        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) { ExceptionHandler.handle(0); }

        try { Frame frame = new Frame(); } catch (Exception exception) { ExceptionHandler.handle(2); }
    }
}