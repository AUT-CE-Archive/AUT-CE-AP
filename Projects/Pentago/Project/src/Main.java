import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Scanner scanner;
    public static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {

        // Initialize Scanner
        scanner = new Scanner(System.in);

        String gameMode = "";
        String player1Name = "", player2Name = "";
        String difficulty = "";

        while (true) {
            System.out.print("\n ● Select Game Mode (singleplayer, multiplayer, ai_vs_ai): ");
            gameMode = scanner.nextLine();
            if (gameMode.equals("singleplayer") || gameMode.equals("multiplayer") || gameMode.equals("ai_vs_ai"))
                break;
            else
                System.out.println(ConsoleColors.RED_BRIGHT
                        + "'Invalid GameMode exception' thrown by Game_ExceptionHandler (Select among the options)"
                        + ConsoleColors.RESET);
        }

        if (gameMode.equals("ai_vs_ai")) {
            player1Name = getSaltString();
            player2Name = getSaltString();
        } else if (gameMode.equals("singleplayer")) {
            System.out.print(" ● Enter Player Name: ");
            player1Name = scanner.nextLine();
            player2Name = getSaltString();
            System.out.println(" ● AI Name: " + player2Name);

            while (true) {
                System.out.print(" ● Select difficulty (easy, medium): ");
                difficulty = scanner.nextLine();
                if (difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard"))
                    break;
                else
                    System.out.println(ConsoleColors.RED_BRIGHT
                            + "'Invalid GameMode exception' thrown by Game_ExceptionHandler (Select among the options)"
                            + ConsoleColors.RESET);
            }
        } else {
            System.out.print("\n ● Enter Player1 Name: ");
            player1Name = scanner.nextLine();
            System.out.print(" ● Enter Player2 Name: ");
            player2Name = scanner.nextLine();
        }

        while (true) {
            System.out.print("- Would you like Graphical display or Console display? (" + ConsoleColors.RED_BRIGHT + "GUI is for Intellij, Console is for VS, RUN IN Intellij!!!!" + ConsoleColors.RESET + ") (graphical, console): ");
            String RunMode = scanner.nextLine();
            if (RunMode.equals("console")) {
                run(player1Name, player2Name, false, gameMode, difficulty);
                break;
            } else if (RunMode.equals("graphical")) {
                run(player1Name, player2Name, true, gameMode, difficulty);
                break;
            } else
                System.out.println(ConsoleColors.RED_BRIGHT + "\'InvalidInput exception\' thrown by GameExceptionHandler (Choose among the options)" + ConsoleColors.RESET);
        }
    }

    public static void run(String player1Name, String player2Name, Boolean GUI, String gameMode, String difficulty) throws InterruptedException {

        System.out.println("\nWelcome both players, here are some syntax examples which you can use in the game: ");
        System.out.println("- 4 4 TL true (X: 4, Y: 4, Top-Left Quadrant, ClockWise Rotation)");
        System.out.println("- 1 5 BR true (X: 1, Y: 5, Bottom-Right Quadrant, ClockWise Rotation)");
        System.out.println("- 2 3 BL false (X: 2, Y: 3, Bottom-Left Quadrant, CounterClockWise Rotation)");
        System.out.println("- 4 2 TR false (X: 4, Y: 2, Top-Right Quadrant, CounterClockWise Rotation)");
        System.out.println("- 5 5 TL true (X: 5, Y: 5, Top-Left Quadrant, ClockWise Rotation)");
        System.out.println("- 1 8 BL false (X: 1, Y: 8, Bottom-Left Quadrant, CounterClockWise Rotation)");

        // Initialize game
        Pentago pentago = new Pentago(player1Name, player2Name, GUI, gameMode, difficulty);
        pentago.play();
    }

    public static String getSaltString() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 10)
            salt.append(SALTCHARS.charAt((int) (random.nextFloat() * SALTCHARS.length())));
        return salt.toString();
    }
}