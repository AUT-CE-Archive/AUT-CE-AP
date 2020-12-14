import java.util.ArrayList;
import java.util.List;

public class ReserveSystem implements ConsoleColor {

    private List<Show> shows;
    private List<Reserve> reserves;

    public ReserveSystem() {
        shows = new ArrayList<>();
        reserves = new ArrayList<>();
    }

    public void createShow(String showName, Hall hall, String dateTime) { shows.add(new Show(showName, hall, dateTime)); }

    public void reserve(Reserve reserve) {

        boolean success = false;
        for (Show show: shows)
            if (show.getShowName().equals(reserve.getShowName())) {
                show.addAttender(reserve.getAttender(), reserve.getSeats());
                success = true;

                reserves.add(reserve);  // Store in the Reserve system
            }

        if (!success)
            System.out.println(ConsoleColor.RED_BOLD + "Error: ShowNotFound (Selected show does not exist in the system)");
    }

    public void printShows(boolean printHall) {
        for (Show show: shows)
            show.showDetails(printHall);
    }

    public boolean checkShowExists(String name) {
        for (Show show : shows)
            if (show.getShowName().equals(name))
                return true;
        return false;
    }
}