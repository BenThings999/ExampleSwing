class Seat {
    private int seatNumber;
    private boolean occupied;
    private Person occupant;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.occupied = false;
        this.occupant = null;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Person getOccupant() {
        return occupant;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void setOccupant(Person occupant) {
        this.occupant = occupant;
    }
}