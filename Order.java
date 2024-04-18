import java.util.ArrayList;
import java.util.List;

class Order {
    private String orderId;
    private List<Object> items; // List to hold both Dish and Drink objects
    private boolean served;
    private boolean canceled;
    private boolean dineIn; // Indicates if the order is for dine-in or take-out
    private List<Person> customers;
    private Seat seat;
    private Table table; // Reference to the table this order belongs to

    public Order(int orderId, boolean dineIn) {
        this.orderId = IdGenerator.generateUniqueId("OR");;
        this.items = new ArrayList<>();
        this.served = false; // Initially, the order is not served
        this.canceled = false; // Initially, the order is not canceled
        this.dineIn = dineIn;
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Person customer) {
        customers.add(customer);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void assignSeats(Table table) {
        int customerIndex = 0;
        for (Seat seat : table.getSeats()) {
            if (!seat.isOccupied() && customerIndex < customers.size()) {
                table.occupySeat(seat.getSeatNumber(), customers.get(customerIndex));
                setTable(table); // Set the table for this order
                setSeat(seat); // Set the seat for this order
                customerIndex++;
            }
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public List<Object> getItems() {
        return items;
    }

    public void addItem(Object item) {
        items.add(item);
    }

    public String getCustomerNames() {
        StringBuilder names = new StringBuilder();
        for (Person customer : customers) {
            names.append(customer.getName()).append(", ");
        }
        // Remove the trailing comma and space
        if (names.length() > 0) {
            names.setLength(names.length() - 2);
        }
        return names.toString();
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
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Items:\n");
        for (Object item : items) {
            if (item instanceof Dish) {
                Dish dish = (Dish) item;
                sb.append("- Dish: ").append(dish.getName()).append(" (ID: ").append(dish.getDishId())
                        .append(", Price: $").append(dish.getPrice()).append(", Quantity: ").append(dish.getQuantity()).append(")\n");
            }
            if (item instanceof Drink) {
                Drink drink = (Drink) item;
                sb.append("- Drink: ").append(drink.getName()).append(" (ID: ").append(drink.getDrinkId())
                        .append(", Price: $").append(drink.getPrice()).append(", Quantity: ").append(drink.getQuantity()).append(")\n");
            }
        }
        sb.append("Served: ").append(served ? "Yes" : "No").append("\n");
        sb.append("Canceled: ").append(canceled ? "Yes" : "No").append("\n");
        sb.append("Dine-in: ").append(dineIn ? "Yes" : "No").append("\n");
        return sb.toString();
    }

}
