import java.util.*;

class Order {
    private static Scanner scanner = new Scanner(System.in);

    private String orderId;
    private static List<Item> items;
   // List to hold both Dish and Drink objects
    private boolean served;
    private boolean canceled;
    private boolean dineIn; // Indicates if the order is for dine-in or take-out
    public List<Person> customers;
    public Seat seat;
    public Table table; // Reference to the table this order belongs to
    private double totalPrice;
    private Person customer;
    private GroupOfPeople group;


    public Order(boolean dineIn) {
        this.orderId = IdGenerator.generateUniqueId("OR");;
        this.items = new ArrayList<>();
        this.served = false; // Initially, the order is not served
        this.canceled = false; // Initially, the order is not canceled
        this.dineIn = dineIn;
        this.customers = new ArrayList<>();
        this.table  = null;
        this.totalPrice=getTotalPrice();
        this.customer=null;
    }

    // Getter and setter for the customer field
    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public void addCustomer(Person customer) {
        customers.add(customer);
    }
    public void setGroup(GroupOfPeople group) {
        this.group = group;
    }
    public GroupOfPeople getGroup() {
        return group;
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


    public String getOrderId() {
        return orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item, int quantity) {
        for (Item existingItem : items) {
            if (existingItem.equals(item)) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity); // Update quantity
                System.out.println(existingItem.getQuantity()); // (Optional debug output)
                return; // No need to continue the loop
            }
        }
        // If we reach here, the item is new
        item.setQuantity(quantity); // Set initial quantity
        items.add(item);
    }
    public static double calculateTotalPrice() {
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        return totalPrice;
    }

    private static double calculateDishPrice(Dish dish) {
        return dish.getPrice() * dish.getQuantity();
    }
    public double getTotalPrice() {
        totalPrice=calculateTotalPrice();
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private static double calculateDrinkPrice(Drink drink) {
        return drink.getPrice() * drink.getQuantity();
    }


    public String getCustomerNames() {
        StringBuilder names = new StringBuilder("Customer(s): "); // Add "Customer(s): "
        int count = 0;

        for (Person customer : customers) {
            if (count < 5) {
                names.append(customer.getName()).append(", ");
            } else {
                int remainingCustomers = customers.size() - count;
                names.append("and ").append(remainingCustomers).append(" ").append("others    ");
                break;
            }
            count++;
        }

        // Remove the trailing comma and space if any names were added
        if (names.length() > "Customer(s): ".length()) { // Check if names exist
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
        if (canceled && dineIn) {
            // If the order is canceled and it is for dine-in, remove all persons from the order
            for ( Person customerE : this.customers){
                customerE.getSeat().setOccupant(null);
                customerE.getSeat().setOccupied(false);
            }


        }
        if (canceled) {
            // If the order is canceled, add it to the list of canceled orders
            RestaurantSimulation.canceledOrders.add(this);

        }
        System.out.println("Order marked as canceled.");
    }



    public boolean isDineIn() {
        return dineIn;
    }

    public void setDineIn(boolean dineIn) {
        this.dineIn = dineIn;
    }
    public void removeCustomersWithProbability(Queue<Order> servedOrders, double probability, Queue doneDiningOrders) {
        Random random = new Random();
        for (Order order : servedOrders) {
            setCanceled(true);
            if (random.nextDouble() < probability) {

                doneDiningOrders.add(servedOrders);
                System.out.println("Customer finished dining");
            }
        }


    }

    public void updateOrder(Order order) {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Add Addtional Order item");
            System.out.println("2. Remove item");
            System.out.println("3. Mark as served");
            System.out.println("4. Mark as canceled");
            System.out.println("5. Update dine-in status");
            System.out.println("6. Exit update menu");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add items (Modify existing order)
                    RestaurantSimulation.addOrder(RestaurantSimulation.dishes, RestaurantSimulation.drinks, dineIn,order.getCustomer());
                    break;
                case 2:
                    // Remove item
                    RestaurantSimulation.removeItemFromOrder(scanner,order);
                    break;
                case 3:
                    // Mark as served
                    if(!this.isServed()) {
                        setServed(true);
                        System.out.println("Order marked as served.");
                        RestaurantSimulation.servedOrders.add(order);
                    }else {
                        System.out.println("This is already served");
                    }
                    break;
                case 4:
                    // Mark as canceled
                    if(!this.isServed()) {
                        setCanceled(true);
                        System.out.println("Order marked as cancelled.");
                    }else {
                        System.out.println("Order already cancelled.");

                    }
                    break;
                case 5:
                    // Update dine-in status
                    System.out.print("Is this order for dine-in? (true/false): ");
                    boolean newDineInStatus = scanner.nextBoolean();
                    System.out.println(newDineInStatus);
                    System.out.println(this.dineIn);
                    if (newDineInStatus != this.dineIn) {
                        if (dineIn) {
                            // If previous status was dine-in, mark the seat as false occupied and remove the occupants
                            for ( Person customerE : this.customers){
                                customerE.getSeat().setOccupant(null);
                                customerE.getSeat().setOccupied(false);
                            }

                            System.out.println("Seat marked as occupied and occupants removed.");
                        } else {
                            // If previous status was not dine-in, ask for the number of customers and seat them

                            GroupOfPeople group = RestaurantSimulation.addPersonAndCompanions(order.getCustomer());
                            order.setGroup(group);
                            RestaurantSimulation.assignGroup(RestaurantSimulation.tables,group,order);
                        }
                        dineIn = newDineInStatus; // Update dine-in status
                        System.out.println("Dine-in status updated.");
                    } else {
                        System.out.println("Dine-in status remains unchanged.");
                    }
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    public int getTotalOccupiedSeats() {
        int occupiedSeats = 0;
        for (Person person : customers) {
            if (person.getSeat() != null && person.getSeat().isOccupied()) {
                occupiedSeats++;
            }
        }
        return occupiedSeats;
    }




    public List<Person> getCustomers() {
        return customers;
    }
    public void setCustomers(GroupOfPeople group) {
        this.customers= group.getPeople();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Number of People: ").append(customers.size()).append("\n");
        sb.append("Items:\n");
        for (Object item : items) {
            if (item instanceof Dish) {
                Dish dish = (Dish) item;
                sb.append("- Dish: ").append(dish.getName()).append(" (ID: ").append(dish.getItemId())
                        .append(", Price: $").append(dish.getPrice()).append(", Quantity: ").append(dish.getQuantity()).append(")\n");
            }
            if (item instanceof Drink) {
                Drink drink = (Drink) item;
                sb.append("- Drink: ").append(drink.getName()).append(" (ID: ").append(drink.getItemId())
                        .append(", Price: $").append(drink.getPrice()).append(", Quantity: ").append(drink.getQuantity()).append(")\n");
            }
        }
        sb.append("Served: ").append(served ? "Yes" : "No").append("\n");
        sb.append("Canceled: ").append(canceled ? "Yes" : "No").append("\n");
        sb.append("Dine-in: ").append(dineIn ? "Yes" : "No").append("\n");
        sb.append("Total Price: $").append(calculateTotalPrice()).append("\n"); // Total price

        return sb.toString();
    }

    public static Order findOrderById(Queue<Order> orders) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the order ID: ");
        String orderId = scanner.nextLine();

        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }

        System.out.println("Order not found!");
        return null; // Return null if order is not found
    }

}
