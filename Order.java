import java.util.ArrayList;
import java.util.List;

class Order {
    private int orderId;
    private List<Object> items; // List to hold both Dish and Drink objects
    private boolean served;
    private boolean canceled;
    private boolean dineIn; // Indicates if the order is for dine-in or take-out
    private List<Person> customers;


    public Order(int orderId, boolean dineIn) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
        this.served = false; // Initially, the order is not served
        this.canceled = false; // Initially, the order is not canceled
        this.dineIn = dineIn;
        this.customers = new ArrayList<>();

    }
    public void addCustomer(Person customer) {
        customers.add(customer);
    }

    public void assignSeats(Table table) {
        int customerIndex = 0;
        for (Seat seat : table.getSeats()) {
            if (!seat.isOccupied() && customerIndex < customers.size()) {
                table.occupySeat(seat.getSeatNumber(), customers.get(customerIndex));
                customerIndex++;
            }
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public List<Object> getItems() {
        return items;
    }

    public void addItem(Object item) {
        items.add(item);
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isDineIn() {
        return dineIn;
    }

    public void setDineIn(boolean dineIn) {
        this.dineIn = dineIn;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", items=" + items +
                ", served=" + served +
                ", canceled=" + canceled +
                ", dineIn=" + dineIn +
                '}';
    }
}
