import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Main extends NameGenerator {

    /**
     * The encapsulated system of reservations
     */
    private static ReserveSystem reserveSystem;

    /**
     * List of hall objects
     */
    private static List<Hall> halls;

    /**
     * Date and time, of each show
     */
    private static List<String> localDateTimes;

    /**
     * Date time formatter
     */
    private static DateTimeFormatter formatter;

    /**
     * List of all attenders
     */
    private static List<Attender> attenders;

    /**
     * A list of all reservations
     */
    private static List<Reserve> reserves;

    /**
     * Scanner Obj
     */
    private static Scanner scanner;

    /**
     * Driver function
     * @param args
     */
    public static void main(String[] args) {

        // Initialize Objects
        reserveSystem = new ReserveSystem();
        halls = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        localDateTimes = new ArrayList<>();
        attenders = new ArrayList<>();
        reserves = new ArrayList<>();
        scanner = new Scanner(System.in);

        /* Start - Random initialization - A natural selection of attenders */

        // Halls entry
        for (int i = 0; i < 10; i++)
            halls.add(new Hall("Hall_" + (i + 1), 10, 10));

        // Date and time entry
        for (int i = 0; i < 10; i++)
            localDateTimes.add(formatter.format(LocalDateTime.now()));

        // Show name entry
        String[] showNames = new String[]{"The Fault In Our Stars", "V for Vendetta", "The Perks of Being a Wallflower", "Due date", "Doctor Sleep", "Spectral", "The Nice Guys", "Inside Out", "Skyfall", "Fury"};

        // Attenders entry
        for (int i = 0; i < 10; i++)
            attenders.add(new Attender(getSaltString(10), getSaltNumber(11)));

        // #1 Reservations entry
        for (int i = 0; i < 10; i++) {
            List<Seat> seats = new ArrayList<>();
            for (int j = 0; j < new Random().nextInt(35) + 20; j++)
                seats.add(new Seat(new Random().nextInt(10), new Random().nextInt(10), attenders.get(new Random().nextInt(attenders.size())), true));

            reserves.add(new Reserve(attenders.get(i), seats, showNames[i]));
        }

        // Show entry
        for (int i = 0; i < 10; i++)
            reserveSystem.createShow(showNames[i], halls.get(i), localDateTimes.get(i));

        // #2 Reservations entry
        for (int i = 0; i < 10; i++)
            reserveSystem.reserve(reserves.get(i));

        reserveSystem.printShows(true);

        /* End - Random initialization */

        System.out.println("Using Pool Selection Distribution, Some seats have already been assigned!\n\n");
    }
}