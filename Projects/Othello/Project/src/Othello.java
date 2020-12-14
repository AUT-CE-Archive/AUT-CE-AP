import java.util.Random;
import java.util.Scanner;

class Othello {

    public static Scanner scanner;
    public static Random rnd = new Random();

    public static void main(final String[] args) throws InterruptedException {

        System.out.println(ConsoleColors.GREEN_BRIGHT + "\n\nWelcome to Othello,\n ● I strongly recommend you run the project on Intellij for its graphical display (VS does NOT support GUI). Players discs are colored as (Red & Blue in graphical, Black & White in console).\n ● Yellow discs represent the available options for each player and Black cells are empty." + ConsoleColors.RESET);

        scanner = new Scanner(System.in);
        String gameMode = "";
        String player1Name = "", player2Name = "";
        String difficulty = "";

        while (true) {
            System.out.print("\n ● Select Game Mode (singleplayer, multiplayer, ai_vs_ai): ");
            gameMode = scanner.nextLine();
            if (gameMode.equals("singleplayer"))
                break;
            else if (gameMode.equals("multiplayer"))
                break;
            else if (gameMode.equals("ai_vs_ai"))
                break;
            else
                System.out.println(ConsoleColors.RED_BRIGHT
                        + "'Invalid GameMode exception' thrown by Game_ExceptionHandler (Select among the options)"
                        + ConsoleColors.RESET);
        }

        if (gameMode.equals("ai_vs_ai")) {
            player1Name = getSaltString();
            player2Name = getSaltString();
        }  else if (gameMode.equals("singleplayer")) {
            System.out.print(" ● Enter Player Name: ");
            player1Name = scanner.nextLine();
            player2Name = getSaltString();
            System.out.println(" ● AI Name: " + player2Name);

            while (true) {
                System.out.print(" ● Select difficulty (easy(Random), medium(Alpha Beta), hard(Neural Network)): ");
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
            System.out.print(" ● Would you like the graphical display or the console display? (" + ConsoleColors.RED_BOLD_BRIGHT + "Graphical recommended for Intellij, Console recommended for Visual Studio - Run in Intellij!!!!" + ConsoleColors.RESET + ") (graphical, console): ");
            String RunMode = scanner.nextLine();
            if (RunMode.equals("console")) {
                run(player1Name, player2Name, false, gameMode, difficulty);
                break;
            } else if (RunMode.equals("graphical")) {
                run(player1Name, player2Name, true, gameMode, difficulty);
                break;
            } else
                System.out.println(ConsoleColors.RED_BRIGHT
                        + "'Invalid GameMode exception' thrown by Game_ExceptionHandler (Select among the options)"
                        + ConsoleColors.RESET);
        }
    }

    public static void run(String player1Name, String player2Name, boolean GUI, String gameMode, String difficulty) throws InterruptedException {
        // Initialize Game dependencies
        Game game = new Game(player1Name, player2Name, GUI, gameMode, difficulty);
        game.play();
    }

    public static String getSaltString() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 10)
            salt.append(SALTCHARS.charAt((int) (rnd.nextFloat() * SALTCHARS.length())));
        return salt.toString();
    }
}