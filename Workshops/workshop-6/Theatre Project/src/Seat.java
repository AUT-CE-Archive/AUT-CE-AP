public class Seat {

    private Attender attender;
    private int seatRow, seatColumn;
    public boolean isReserved;

    public Seat(int seatColumn, int seatRow, Attender attender, boolean isReserved) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.attender = attender;
        this.isReserved = isReserved;
    }

    public String getAttenderName() { return this.attender.getName(); }

    public String getSeatLocation() {return this.seatRow + " " + this.seatColumn; }

    public int getSeatRow() { return seatRow; }

    public int getSeatColumn() { return seatColumn; }
}