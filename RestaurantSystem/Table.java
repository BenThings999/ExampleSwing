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

    public void occupySeat(int seatNumber, Person person) {
        seats.get(seatNumber - 1).setOccupied(true);
        seats.get(seatNumber - 1).setOccupant(person);
    }

    public void vacateSeat(int seatNumber) {
        seats.get(seatNumber - 1).setOccupied(false);
        seats.get(seatNumber - 1).setOccupant(null);
    }
    public boolean assignGroup(GroupOfPeople group) {
        // Check if the table has enough seats for the group
        if (group.getPeople().size() > seats.size()) {
            return false; // Not enough seats available
        }

        // Find consecutive available seats for the group
        List<Seat> availableSeats = findConsecutiveAvailableSeats(group.getPeople().size());
        if (availableSeats == null) {
            return false; // Not enough consecutive seats available
        }

        // Assign seats to the group members
        for (int i = 0; i < group.getPeople().size(); i++) {
            Seat seat = availableSeats.get(i);
            seat.setOccupied(true);
            seat.setOccupant(group.getPeople().get(i));
        }

        return true; // Seats assigned successfully
    }

    private List<Seat> findConsecutiveAvailableSeats(int groupSize) {
        List<Seat> consecutiveSeats = new ArrayList<>();
        int consecutiveCount = 0;

        for (Seat seat : seats) {
            if (!seat.isOccupied()) {
                consecutiveSeats.add(seat);
                consecutiveCount++;

                if (consecutiveCount == groupSize) {
                    return consecutiveSeats;
                }
            } else {
                consecutiveSeats.clear();
                consecutiveCount = 0;
            }
        }

        return null; // No consecutive available seats found
    }
}