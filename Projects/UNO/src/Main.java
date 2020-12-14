import java.util.Random;
import java.util.Scanner;

public class Main {

    private static Scanner scanner;
    private static Random random;

    public static void main(String[] args) throws InterruptedException {

        scanner = new Scanner(System.in);
        random = new Random();

        System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Welcome to UNO, please read the PDF file located in the project folder before use (IMPORTANT!)." + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "It is strongly recommended to run the project on IntelliJ," + ConsoleColors.RED_BOLD_BRIGHT + " VS Code is unable to process the graphics." + ConsoleColors.RESET);
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You can play the game with any number of players, however a 4-player-game is strongly recommended for the special GUI (:" + ConsoleColors.RESET + "\n\n");

        // Get player count
        int playerCount;
        while (true) {
            System.out.print("- Enter number of player (4 Recommended): ");
            playerCount = scanner.nextInt();

            if (playerCount >= 2)
                break;
            else
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "InvalidInput exception, thrown by GameExceptionHandler (Must be 2 or more)" + ConsoleColors.RESET);
        }

        if (playerCount != 4)
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You have not chosen 4 players, GUI is reduced to original!" + ConsoleColors.RESET);
        else
            System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "You have chosen 4 players! GUI is sky rocketing now!" + ConsoleColors.RESET);

        int AICount;
        while (true) {
            System.out.print("- How many players must be AIs? (0, ..., " + playerCount + "): ");
            AICount = scanner.nextInt();
            if (AICount < 0 || AICount > playerCount)
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "InvalidInput exception, thrown by GameExceptionHandler (Input cannot be below 0 and must be in range)" + ConsoleColors.RESET);
            else
                break;
        }

        // Get player names
        scanner.nextLine();
        String[] names = new String[playerCount];
        for (int i = 0; i < playerCount - AICount; i++) {
            System.out.print("- Enter player " + (i + 1) + " name: ");
            names[i] = scanner.nextLine();
        }

        // Configure AI names
        for (int i = playerCount - AICount; i < playerCount; i++) {
            names[i] = getSaltString();
            System.out.println("- Player" + (i + 1) + "(AI) name: " + names[i]);
        }

        UNO uno;
        if (playerCount == 4)
            uno = new UNO(playerCount, AICount, names, true);
        else
            uno = new UNO(playerCount, AICount, names, false);
        uno.play();
    }

    private static String getSaltString() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 5)
            salt.append(SALTCHARS.charAt((int) (random.nextFloat() * SALTCHARS.length())));
        return salt.toString();
    }
}
