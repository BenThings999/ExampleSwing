import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Table {
    private int tableNumber;
    private List<Seat> seats;

    public Table(int tableNumber, int numberOfSeats) {
        this.tableNumber = tableNumber;
        this.seats = new ArrayList<>(numberOfSeats);
        for (int i = 1; i <= numberOfSeats; i++) {
            seats.add(new Seat(i));
        }

    }


    public int getTableNumber() {
        return tableNumber;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public boolean isSeatOccupied(int seatNumber) {
        return seats.get(seatNumber - 1).isOccupied();
    }
    public static List<Table> sortTablesByAvailableSeats(List<Table> tables) {
        tables.sort(Comparator.comparingInt(table -> {
            int unoccupiedSeats = 0;
            for (Seat seat : table.getSeats()) {
                if (!seat.isOccupied()) {
                    unoccupiedSeats++;
                }
            }
            return unoccupiedSeats;
        }));
        return tables;
    }


}