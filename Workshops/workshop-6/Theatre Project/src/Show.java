import java.util.ArrayList;
import java.util.List;

public class Show extends NameGenerator implements ConsoleColor {

    private String showName;
    private String dateTime;
    private Hall hall;
    private List<Reserve> reserves;

    public Show(String showName, Hall hall, String dateTime) {
        this.showName = showName;
        this.hall = hall;
        this.dateTime = dateTime;
        this.reserves = new ArrayList<>();
    }

    public String getShowName() {
        return showName;
    }

    public void addAttender(Attender attender, List<Seat> seats) {
        for (Seat seat : seats)
            if (hall.isSeatAvailable(seat.getSeatRow(), seat.getSeatColumn()))
                hall.reserveSeat(seat.getSeatRow(), seat.getSeatColumn(), attender);
            else
                System.out.println(ConsoleColor.RED_BOLD + "Error: SeatNotAvailable for '" + attender.getName() + "' (Seat has been reserved by another attender - (" + seat.getSeatRow() + ", " + seat.getSeatColumn() + "))" + ConsoleColor.RESET);
    }

    public void showDetails(boolean printHall) {
        System.out.println("Show Name: '" + showName + "'\t\tShow DateTime: '" + dateTime + "'\t\t Hall Name:'" + hall.getHallName() + "'\t\tSeats available:" + hall.getFreeSeatsCount());
        if (printHall)
            hall.printHallStatus();
    }
}