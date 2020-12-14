public class Hall extends NameGenerator implements ConsoleColor {

    private String hallName;
    private int hallRowCount, hallColumnCount;
    private Seat[][] hallSeats;

    public Hall(String hallName, int hallRowCount, int hallColumnCount) {
        this.hallName = hallName;
        this.hallRowCount = hallRowCount;
        this.hallColumnCount = hallColumnCount;
        this.hallSeats = new Seat[hallRowCount][hallColumnCount];

        for (int i = 0; i < hallRowCount; i++)
            for (int j = 0; j < hallColumnCount; j++)
                hallSeats[i][j] = new Seat(i, j, new Attender("", getSaltNumber(11)), false);     // Initialize owner and location
    }

    public void printHallStatus() {
        for (int i = 0; i < hallRowCount; i++) {
            for (int j = 0; j < hallColumnCount; j++)
                if (hallSeats[i][j].getAttenderName().equals(""))
                    System.out.print("(" + i + ", " + j + "):" + GREEN_BOLD + "Clear  " + RESET);
                else
                    System.out.print("(" + i + ", " + j + "):" + RED_BOLD + "Taken  " + RESET);

            System.out.println();
        }
        System.out.println();
    }

    public boolean isSeatAvailable(int row, int column) {
        return !hallSeats[row][column].isReserved;
    }

    public void reserveSeat(int row, int column, Attender attender) { hallSeats[row][column] = new Seat(row, column, attender, true); }

    public String getHallName() { return hallName; }

    public int getHallRowCount() { return hallRowCount; }

    public int getHallColumnCount() { return hallColumnCount; }

    public int getFreeSeatsCount() {
        int count = 0;
        for (int i = 0; i < hallRowCount; i++)
            for (int j = 0; j < hallColumnCount; j++)
                if (!hallSeats[i][j].isReserved)
                    count++;
        return count;
    }
}