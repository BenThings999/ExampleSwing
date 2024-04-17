import java.util.*;


public class RestaurantSimulation {
    private static List<Table> tables = new ArrayList<>();

    private static Queue<Person> customerQueue = new LinkedList<>();
    private static Queue<Order> orderQueue = new LinkedList<>();
    private static Queue<Order> servedOrders = new LinkedList<>(); // Queue for served orders
    // private static Queue<Order> seatChoice = new LinkedList<>(); // Queue for
    private static int orderIdCounter = 1;
    private static List<Dish> dishes = new ArrayList<>();
    private static List<Drink> drinks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    public static void main(String[] args) {
        // Creating a list of dishes

        dishes.add(new Dish("Chicken Alfredo", 12.99));
        dishes.add(new Dish("Margherita Pizza", 9.99));
        dishes.add(new Dish("Caesar Salad", 8.49));
        dishes.add(new Dish("Beef Burger", 11.99));

        // Creating a list of drinks

        drinks.add(new Drink("Coke", 1.99, false));
        drinks.add(new Drink("Mojito", 7.49, true));
        drinks.add(new Drink("Orange Juice", 3.49, false));
        drinks.add(new Drink("Margarita", 8.99, true));

        // Print menu
        printDishes(dishes);
        printDrinks(drinks);
        boolean exit = false;
        createTables();
        addInitialCustomers();
        while (!exit) {
            showAvailableTables();
            showListOfCustomers();
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
                        boolean dineIn = isDineIn();
                        if(dineIn) {

                            addOrder(dishes, drinks, dineIn);
                        }else {

                        }
                    }catch(Exception e){
                        System.out.println();
                    }
                    break;
                case 2:
                    // viewOrders();
                    break;
                case 3:
                    // updateOrder();
                    break;
                case 4:
                    // cancelOrder();
                    break;
                case 5:
                    // deleteOrder();
                    break;
                case 6:
                    // markOrderAsServed();
                    break;
                case 7:
                    // viewServedOrders();
                    break;
                case 8:
                    // viewCanceledOrders();
                    break;
                case 9:
                    // uncancelOrder();
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

    // Overloaded method to print dishes
    private static void printDishes(List<Dish> dishes) {
        System.out.println("Dishes:");
        System.out.println("------------------------------------------------");
        System.out.printf("| %-4s | %-20s | %-8s |\n", "No.", "Name", "Price");
        System.out.println("------------------------------------------------");
        int dishCounter = 1;
        for (Dish dish : dishes) {
            printItem(dishCounter, dish.getName(), dish.getPrice());
            dishCounter++; // Increment counter for the next dish
        }
        System.out.println("------------------------------------------------");
    }

    private static void printDrinks(List<Drink> drinks) {
        System.out.println("Drinks:");
        System.out.println("------------------------------------------------");
        System.out.printf("| %-4s | %-20s | %-8s |\n", "No.", "Name", "Price");
        System.out.println("------------------------------------------------");
        int drinkCounter = 1;
        for (Drink drink : drinks) {
            printItem(drinkCounter, drink.getName(), drink.getPrice());
            drinkCounter++; // Increment counter for the next drink
        }
        System.out.println("------------------------------------------------");
    }


    private static void printItem(int i, String name, double price) {
        // Truncate name if it exceeds 20 characters
        if (name.length() > 20) {
            name = name.substring(0, 17) + "...";
        }
        // Format and print the item with its number
        System.out.printf("| %-4s | %-20s | $%-7.2f |\n", i, name, price);
    }


    private static void createTables() {
        // Create 10 tables with 4 seats each
        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i, 4));
        }

        // Create 10 tables with 2 seats each
        for (int i = 11; i <= 20; i++) {
            tables.add(new Table(i, 2));
        }

        // Create 10 tables with 6 seats each
        for (int i = 21; i <= 30; i++) {
            tables.add(new Table(i, 6));
        }
    }
    private static void addInitialCustomers() {
        Random random = new Random();
        while (customerQueue.size() < 3) {
            int randomNum = 10000 + random.nextInt(90000); // Generate random 5-digit number
            customerQueue.offer(new Person("Customer" + randomNum));
        }
    }
    private static void showAvailableTables() {
        // Count the number of tables with available seats of different capacities
        int[] availableSeatsCount = new int[7]; // Assuming maximum seat capacity is 6 seats

        for (Table table : tables) {
            int availableSeats = 0;
            for (Seat seat : table.getSeats()) {
                if (!seat.isOccupied()) {
                    availableSeats++;
                }
            }
            availableSeatsCount[Math.min(availableSeats, 6)]++;
        }

        // Print the number of available tables with their available seats
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            if (availableSeatsCount[i] > 0) {
                result.append(availableSeatsCount[i]).append(" Tables with ").append(i).append(" seats available | ");
            }
        }
        System.out.println(result.toString());
    }

    private static boolean isDineIn() {
        System.out.print("Is this a dine-in order? (yes/no): ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("yes");
    }

    private static void showListOfCustomers() {
        System.out.println("List of Customers:");
        for (Person customer : customerQueue) {
            System.out.println(customer.getName());
        }
    }
    private static Person automaticallyGetCustomer() {
        if (!customerQueue.isEmpty()) {
            return customerQueue.peek(); // Return the first person in the queue without removing it
        } else {
            System.out.println("No customers in the queue.");
            return null;
        }
    }
    private static Order addOrder(List<Dish> dishes, List<Drink> drinks, Boolean dineIn) {
        scanner.nextLine(); // Consume newline character



        // Create a new order
        Order order = new Order(orderIdCounter++, dineIn);

        // Add items to the order
        boolean addMoreItems = true;
        while (addMoreItems) {
            System.out.println("\nSelect item type:");
            System.out.println("1. Dish");
            System.out.println("2. Drink");
            System.out.println("3. Finish adding items");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    printDishes(dishes);
                    System.out.print("Enter the number of the dish: ");
                    int dishIndex = scanner.nextInt();
                    System.out.print("Enter the quantity: ");
                    int quantity = scanner.nextInt();
                    Dish selectedDish = dishes.get(dishIndex - 1);
                    for (int i = 0; i < quantity; i++) {
                        order.addItem(selectedDish);
                    }
                    System.out.println(quantity + " " + selectedDish.getName() + " added to the order.");
                    break;
                case 2:
                    printDrinks(drinks);
                    System.out.print("Enter the number of the drink: ");
                    int drinkIndex = scanner.nextInt();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    Drink selectedDrink = drinks.get(drinkIndex - 1);
                    for (int i = 0; i < quantity; i++) {
                        order.addItem(selectedDrink);
                    }
                    System.out.println(quantity + " " + selectedDrink.getName() + " added to the order.");
                    break;
                case 3:
                    addMoreItems = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    break;
            }
        }

        // Calculate total price
        double totalPrice = calculateTotalPrice(order.getItems());
        System.out.println("Total price: $" + totalPrice);

        // Ask for the customer's money input
        System.out.print("Enter customer's money: $");
        double customerMoney = scanner.nextDouble();

        // Calculate change
        double change = customerMoney - totalPrice;
        System.out.println("Change: $" + change);

        // Return the order
        return order;
    }

    private static double calculateTotalPrice(List<Object> items) {
        double totalPrice = 0;
        for (Object item : items) {
            if (item instanceof Dish) {
                totalPrice += ((Dish) item).getPrice();
            } else if (item instanceof Drink) {
                totalPrice += ((Drink) item).getPrice();
            }
        }
        return totalPrice;
    }
    private static void addPersonAndCompanions(Person person) {
        Random random = new Random();

        // Ask how many people are with the customer
        System.out.print("How many people are currently with you (including yourself): ");
        int numberOfPeople = scanner.nextInt();

        // Check if there are enough seats available
        int totalAvailableSeats = tables.stream().mapToInt(table -> table.getSeats().size()).sum();
        if (totalAvailableSeats < numberOfPeople) {
            System.out.println("Sorry, there are not enough seats available for your group.");
            return;
        }

        // Create a group of people and add the initial person
        GroupOfPeople group = new GroupOfPeople();
        group.addPerson(person);

        // Add companions to the group
        for (int i = 1; i < numberOfPeople; i++) {
            int randomNum = 10000 + random.nextInt(90000); // Generate random 5-digit number
            group.addPerson(new Person("Customer" + randomNum));
        }

        // Assign seats to the group
        assignSeats(group);
    }
    private static void assignSeats(GroupOfPeople group) {
        for (Person person : group.getPeople()) {
            boolean seated = false;
            for (Table table : tables) {
                for (Seat seat : table.getSeats()) {
                    if (!seat.isOccupied()) {
                        // Assign the seat to the person
                        table.occupySeat(seat.getSeatNumber(), person);
                        seated = true;
                        break;
                    }
                }
                if (seated) {
                    break;
                }
            }
            if (!seated) {
                // Add the person to the customerQueue
                customerQueue.offer(person);
            }
        }
    }





//
//    private static void viewOrders() {
//        System.out.println("=== Orders ===");
//        if (orderQueue.isEmpty()) {
//            System.out.println("No orders available.");
//            return;
//        }
//        for (Order order : orderQueue) {
//            if (!order.isCanceled()) {
//                System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName() + ", Table Place: Table no." + order.getTablePlace()
//                        + ", Seat: " + order.getSeat() + " person");
//            }
//        }
//    }
//
//    private static void updateOrder() {
//        System.out.print("Enter order ID to update: ");
//        int orderId = scanner.nextInt();
//        boolean found = false;
//        for (Order order : orderQueue) {
//            if (order.getOrderId() == orderId && !order.isCanceled()) {
//                scanner.nextLine(); // Consume newline character
//                System.out.print("Enter new dish name: ");
//                String newDishName = scanner.nextLine();
//                System.out.print("Enter new table place: ");
//                int newTablePlace = scanner.nextInt();
//                System.out.print("How many person: ");
//                int newSeat = scanner.nextInt();
//                order.setDishName(newDishName);
//                order.setTableNumber(newTablePlace);
//                order.setPersonSeat(newSeat);
//                found = true;
//                System.out.println("Order updated successfully!");
//                break;
//            }
//        }
//        if (!found) {
//            System.out.println("Order ID not found or already canceled!");
//        }
//    }
//
//    private static void cancelOrder() {
//        System.out.print("Enter order ID to cancel: ");
//        int orderId = scanner.nextInt();
//        boolean found = false;
//        for (Order order : orderQueue) {
//            if (order.getOrderId() == orderId && !order.isCanceled()) {
//                order.setCanceled(true);
//                found = true;
//                System.out.println("Order canceled successfully!");
//                break;
//            }
//        }
//        if (!found) {
//            System.out.println("Order ID not found or already canceled!");
//        }
//    }
//
//    private static void deleteOrder() {
//        System.out.print("Enter order ID to delete: ");
//        int orderId = scanner.nextInt();
//        boolean removed = false;
//        Order orderToRemove = null;
//        for (Order order : orderQueue) {
//            if (order.getOrderId() == orderId) {
//                orderToRemove = order;
//                removed = true;
//                break;
//            }
//        }
//        if (removed) {
//            orderQueue.remove(orderToRemove);
//            System.out.println("Order deleted successfully!");
//        } else {
//            System.out.println("Order ID not found!");
//        }
//    }
//
//    private static void markOrderAsServed() {
//        if (orderQueue.isEmpty()) {
//            System.out.println("No orders to serve!");
//            return;
//        }
//        Order servedOrder = orderQueue.poll(); // Retrieve and remove the order at the front of the queue
//        servedOrder.setServed(true);
//        servedOrders.offer(servedOrder); // Add the served order to the servedOrders queue
//        System.out.println("Order ID: " + servedOrder.getOrderId() + " served successfully!");
//    }
//
//    private static void viewServedOrders() {
//        System.out.println("=== Served Orders ===");
//        if (servedOrders.isEmpty()) {
//            System.out.println("No served orders available.");
//            return;
//        }
//        for (Order order : servedOrders) {
//            System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName());
//        }
//    }
//
//    private static void viewCanceledOrders() {
//        System.out.println("=== Canceled Orders ===");
//        boolean canceledOrdersExist = false;
//        for (Order order : orderQueue) {
//            if (order.isCanceled()) {
//                System.out.println("Order ID: " + order.getOrderId() + ", Dish Name: " + order.getDishName());
//                canceledOrdersExist = true;
//            }
//        }
//        if (!canceledOrdersExist) {
//            System.out.println("No canceled orders available.");
//        }
//    }
//
//    private static void uncancelOrder() {
//        System.out.print("Enter order ID to uncancel: ");
//        int orderId = scanner.nextInt();
//        boolean found = false;
//        for (Order order : orderQueue) {
//            if (order.getOrderId() == orderId && order.isCanceled()) {
//                order.setCanceled(false);
//                found = true;
//                System.out.println("Order uncanceled successfully!");
//                break;
//            }
//        }
//        if (!found) {
//            System.out.println("Order ID not found or not canceled!");
//        }
//    }

}
