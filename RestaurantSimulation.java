import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Order {
    private int orderId, tablePlace, seatPerson;
    private String dishName;
    private boolean served;
    private boolean canceled;

    public Order(int orderId, String dishName, int tablePlace, int seatPerson) {
        this.orderId = orderId;
        this.dishName = dishName;
        this.tablePlace = tablePlace;
        this.seatPerson = seatPerson;
        this.served = false; // Initially, the order is not served
        this.canceled = false; // Initially, the order is not canceled
    }

    public int getOrderId() {
        return orderId;
    }

    public String getDishName() {
        return dishName;
    }

    public int getTablePlace(){ return tablePlace; }

    public int getSeat(){ return seatPerson; }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setTableNumber(int tablePlaceString) {
        this.tablePlace = tablePlace;
    }

    public void setPersonSeat(int personSeat) {
        this.seatPerson = personSeat;
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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", dishName=" + dishName + '\'' +
                ", served=" + served +
                ", canceled=" + canceled +
                '}';
    }
}


public class RestaurantSimulation {
    private static Queue<Order> orderQueue = new LinkedList<>();
    private static Queue<Order> servedOrders = new LinkedList<>(); // Queue for served orders
    // private static Queue<Order> seatChoice = new LinkedList<>(); // Queue for
    private static int orderIdCounter = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Restaurant Management System ===");
            System.out.println("1. Add Order");
            System.out.println("2. View Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Delete Order");
            System.out.println("6. Serve Order");
            System.out.println("7. View Served Orders");
            System.out.println("8. View Canceled Orders");
            System.out.println("9. Uncancel Order");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try{
                        addOrder();
                    }catch(Exception e){
                        System.out.println();
                    }
                    break;
                case 2:
                    viewOrders();
                    break;
                case 3:
                    updateOrder();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    deleteOrder();
                    break;
                case 6:
                    markOrderAsServed();
                    break;
                case 7:
                    viewServedOrders();
                    break;
                case 8:
                    viewCanceledOrders();
                    break;
                case 9:
                    uncancelOrder();
                    break;
                case 10:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using the Restaurant Management System!");
    }

    private static void addOrder() {
        System.out.println("<---------------MENU DISHES---------->");
        String menu[] = {"Grilled Liempo",
                "Porkchop", "Ice Cream", "Latte", "Spaghetti Bilao", "Combo Meals"};

        for(int i = 0; i < menu.length; i++) {
            System.out.println(i + 1 +"." +menu[i]);
        }
        System.out.println("<------------------------------------>");

        scanner.nextLine(); // Consume newline character
        System.out.print("\nEnter dish name: ");
        String dishName = scanner.nextLine();
        System.out.print("Enter table place: ");
        int tablePlace = scanner.nextInt();
        System.out.print("How many person: ");
        int seat = scanner.nextInt();

        Order order = new Order(orderIdCounter++, dishName, tablePlace, seat);
        orderQueue.offer(order);
        System.out.println("Order added successfully!");
    }

    private static void viewOrders() {
        System.out.println("=== Orders ===");
        if (orderQueue.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }
        for (Order order : orderQueue) {
            if (!order.isCanceled()) {
                System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName() + ", Table Place: Table no." + order.getTablePlace()
                        + ", Seat: " + order.getSeat() + " person");
            }
        }
    }

    private static void updateOrder() {
        System.out.print("Enter order ID to update: ");
        int orderId = scanner.nextInt();
        boolean found = false;
        for (Order order : orderQueue) {
            if (order.getOrderId() == orderId && !order.isCanceled()) {
                scanner.nextLine(); // Consume newline character
                System.out.print("Enter new dish name: ");
                String newDishName = scanner.nextLine();
                System.out.print("Enter new table place: ");
                int newTablePlace = scanner.nextInt();
                System.out.print("How many person: ");
                int newSeat = scanner.nextInt();
                order.setDishName(newDishName);
                order.setTableNumber(newTablePlace);
                order.setPersonSeat(newSeat);
                found = true;
                System.out.println("Order updated successfully!");
                break;
            }
        }
        if (!found) {
            System.out.println("Order ID not found or already canceled!");
        }
    }

    private static void cancelOrder() {
        System.out.print("Enter order ID to cancel: ");
        int orderId = scanner.nextInt();
        boolean found = false;
        for (Order order : orderQueue) {
            if (order.getOrderId() == orderId && !order.isCanceled()) {
                order.setCanceled(true);
                found = true;
                System.out.println("Order canceled successfully!");
                break;
            }
        }
        if (!found) {
            System.out.println("Order ID not found or already canceled!");
        }
    }

    private static void deleteOrder() {
        System.out.print("Enter order ID to delete: ");
        int orderId = scanner.nextInt();
        boolean removed = false;
        Order orderToRemove = null;
        for (Order order : orderQueue) {
            if (order.getOrderId() == orderId) {
                orderToRemove = order;
                removed = true;
                break;
            }
        }
        if (removed) {
            orderQueue.remove(orderToRemove);
            System.out.println("Order deleted successfully!");
        } else {
            System.out.println("Order ID not found!");
        }
    }

    private static void markOrderAsServed() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders to serve!");
            return;
        }
        Order servedOrder = orderQueue.poll(); // Retrieve and remove the order at the front of the queue
        servedOrder.setServed(true);
        servedOrders.offer(servedOrder); // Add the served order to the servedOrders queue
        System.out.println("Order ID: " + servedOrder.getOrderId() + " served successfully!");
    }

    private static void viewServedOrders() {
        System.out.println("=== Served Orders ===");
        if (servedOrders.isEmpty()) {
            System.out.println("No served orders available.");
            return;
        }
        for (Order order : servedOrders) {
            System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName());
        }
    }

    private static void viewCanceledOrders() {
        System.out.println("=== Canceled Orders ===");
        boolean canceledOrdersExist = false;
        for (Order order : orderQueue) {
            if (order.isCanceled()) {
                System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName());
                canceledOrdersExist = true;
            }
        }
        if (!canceledOrdersExist) {
            System.out.println("No canceled orders available.");
        }
    }

    private static void uncancelOrder() {
        System.out.print("Enter order ID to uncancel: ");
        int orderId = scanner.nextInt();
        boolean found = false;
        for (Order order : orderQueue) {
            if (order.getOrderId() == orderId && order.isCanceled()) {
                order.setCanceled(false);
                found = true;
                System.out.println("Order uncanceled successfully!");
                break;
            }
        }
        if (!found) {
            System.out.println("Order ID not found or not canceled!");
        }
    }
}