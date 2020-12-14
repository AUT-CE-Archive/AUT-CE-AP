import java.util.List;

public class Reserve {

    private Attender attender;
    private List<Seat> seats;
    private String showName;

    public Reserve(Attender attender, List<Seat> seats, String showName) {
        this.attender = attender;
        this.seats = seats;
        this.showName = showName;
    }

    public Attender getAttender() { return attender; }

    public List<Seat> getSeats() { return seats; }

    public String getShowName() { return showName; }
}