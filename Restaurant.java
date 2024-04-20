import java.util.*;
class Restaurant {
    private List<Seat> seats;

    public Restaurant(int numSeats) {
        seats = new ArrayList<>();
        for (int i = 1; i <= numSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    // Method to get the total occupied seats
    public int getTotalOccupiedSeats() {
        int occupiedSeats = 0;
        for (Seat seat : seats) {
            if (seat.isOccupied()) {
                occupiedSeats++;
            }
        }
        return occupiedSeats;
    }

    // Other methods of the Restaurant class...
}
