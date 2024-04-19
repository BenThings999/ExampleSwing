import java.util.*;


public class RestaurantSimulation {
    private static List<Table> tables = new ArrayList<>();

    static Queue<Person> customerQueue = new LinkedList<>();
    private static Queue<Order> orderQueue = new LinkedList<>();
    private static Queue<Order> servedOrders = new LinkedList<>(); // Queue for served orders

    static List<Dish> dishes = new ArrayList<>();
    static List<Drink> drinks = new ArrayList<>();
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

        while (!exit) {
            addInitialCustomers();
            showAvailableTables();
            showListOfCustomers();
            System.out.println("\n=== Restaurant Management System ===");
            System.out.println("1. Add Order");
            System.out.println("2. View Orders");
            System.out.println("3. Update Order");
            System.out.println("4. Cancel Order");// will be removed
            System.out.println("5. Delete Order");// will be removed
            System.out.println("6. Serve Order");// will be removed
            System.out.println("7. View Served Orders");
            System.out.println("8. View Canceled Orders");
            System.out.println("9. Uncancel Order");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:

                    try{
                        Person personQ = automaticallyGetCustomer();
                        boolean dineIn = isDineIn();
                        if(dineIn) {
                            GroupOfPeople group = addPersonAndCompanions(personQ);
                            Order order = addOrder(dishes, drinks, dineIn,personQ);
                            order.getCustomerNames();
                            orderQueue.add(order);
                            assignGroup(tables,group,order);
                            System.out.println(orderQueue);
                        }else {
                            Order order = addOrder(dishes, drinks, dineIn,personQ);
                            order.getCustomerNames();
                            orderQueue.add(order);
                            System.out.println(orderQueue);
                        }
                    }catch(Exception e){
                        System.out.println();
                    }
                    break;
                case 2:
                    viewOrders(orderQueue);
                    break;
                case 3:
                    Order order = Order.findOrderById(orderQueue);
                    order.updateOrder(order);
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
        // Create 10 tables with 2 seats each
        for (int i = 11; i <= 20; i++) {
            tables.add(new Table(i, 2));
        }
        // Create 10 tables with 4 seats each
        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i, 4));
        }
        // Create 10 tables with 6 seats each
        for (int i = 21; i <= 30; i++) {
            tables.add(new Table(i, 6));
        }
    }
    private static void addInitialCustomers() {
        Random random = new Random();
        int remainingCustomers = 3 - customerQueue.size(); // Calculate the remaining customers needed
        for (int i = 0; i < remainingCustomers; i++) {
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
        scanner.nextLine();
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
    // Original addOrder method
    public static Order addOrder(List<Dish> dishes, List<Drink> drinks, Boolean dineIn, Person person) {
        // Create a new order
        Order order = new Order(dineIn);
        System.out.printf("Name: %s%n", person.getName());

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
                    order.addItem(selectedDish, quantity);
                    System.out.println(quantity + " " + selectedDish.getName() + " added to the order.");
                    break;
                case 2:
                    printDrinks(drinks);
                    System.out.print("Enter the number of the drink: ");
                    int drinkIndex = scanner.nextInt();
                    System.out.print("Enter the quantity: ");
                    quantity = scanner.nextInt();
                    Drink selectedDrink = drinks.get(drinkIndex - 1);
                    order.addItem(selectedDrink, quantity);
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
        double totalPrice = order.calculateTotalPrice();
        System.out.println("Total price: $" + totalPrice);

        // Ask for the customer's money input
        System.out.print("Enter customer's money: $");
        double customerMoney = scanner.nextDouble();
        order.setCustomer(person);

        // Calculate change
        double change = customerMoney - totalPrice;
        System.out.println("Change: $" + change);

        // Return the order
        return order;
    }

    // Overloaded addOrder method for updating an existing order
    public static void addItemToOrder(Scanner scanner, Order order, List<Dish> dishes, List<Drink> drinks) {
        System.out.println("Available Dishes:");
        printDishes(dishes);

        System.out.println("Available Drinks:");
        printDrinks(drinks);

        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter item type (1 for Dish, 2 for Drink): ");
        int itemType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter item quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (itemType == 1) { // Dish
            for (Dish dish : dishes) {
                if (dish.getName().equalsIgnoreCase(itemName)) {
                    order.addItem(dish, quantity);
                    System.out.println(quantity + " " + dish.getName() + " added to the order.");
                    return;
                }
            }
            System.out.println("Dish not found!");
        } else if (itemType == 2) { // Drink
            for (Drink drink : drinks) {
                if (drink.getName().equalsIgnoreCase(itemName)) {
                    order.addItem(drink, quantity);
                    System.out.println(quantity + " " + drink.getName() + " added to the order.");
                    return;
                }
            }
            System.out.println("Drink not found!");
        } else {
            System.out.println("Invalid item type.");
        }
    }

    public static void removeItemFromOrder(Scanner scanner, Order order) {
        System.out.println("Current Order Items:");
        List<Item> items = order.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getName());
        }

        System.out.print("Enter the index of item to remove: ");
        int indexToRemove = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (indexToRemove >= 1 && indexToRemove <= items.size()) {
            Item removedItem = items.remove(indexToRemove - 1);
            System.out.println(removedItem.getName() + " removed successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }



//    private static double calculateTotalPrice(List<Item> items) {
//        double totalPrice = 0;
//        for (Object item : items) {
//            if (item instanceof Dish) {
//                totalPrice += calculateDishPrice((Dish) item);
//            } else if (item instanceof Drink) {
//                totalPrice += calculateDrinkPrice((Drink) item);
//            }
//        }
//        return totalPrice;
//    }

    private static double calculateDishPrice(Dish dish) {
        return dish.getPrice() * dish.getQuantity();
    }

    private static double calculateDrinkPrice(Drink drink) {
        return drink.getPrice() * drink.getQuantity();
    }


    private static GroupOfPeople addPersonAndCompanions(Person person) {
        Random random = new Random();

        // Ask how many people are with the customer
        System.out.print("How many people are currently with you (including yourself): ");
        int numberOfPeople = scanner.nextInt();

        // Check if there are enough seats available
        int totalAvailableSeats = tables.stream().mapToInt(table -> table.getSeats().size()).sum();
        if (totalAvailableSeats > 1 && totalAvailableSeats < numberOfPeople) {
            System.out.println("Sorry, there are not enough seats available.");
            System.out.println("Do you still want to order for takeout?");
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
        return group;

    }
    public static boolean assignGroup(List<Table> tables, GroupOfPeople group, Order order) {
        // Print the size of the customerQueue
        System.out.println("Customer Queue Size: " + RestaurantSimulation.customerQueue.size());
        int groupSize = group.getPeople().size();
        tables = Table.sortTablesByAvailableSeats(tables);
        System.out.println(tables);
        for (Person person : group.getPeople()) {
            order.addCustomer(person);
        }
        do {
            for (Table table : tables) {
                if (groupSize <= 2 && table.getSeats().size() == 2) { // If group size is 1 or 2, seat them on 2-seat tables
                    for (Seat seat : table.getSeats()) {
                        if (!seat.isOccupied()) {
                            seat.setOccupied(true);
                            seat.setOccupant(group.getPeople().remove(0));
                            order.setTable(table);
                            order.setSeat(seat);
                            groupSize--;
                        }
                        if (groupSize == 0) {
                            return true; // All members seated
                        }
                    }
                }
                if (groupSize <= 4 && table.getSeats().size() == 4) { // If group size is 3 or 4, seat them on 4-seat tables
                    for (Seat seat : table.getSeats()) {
                        if (!seat.isOccupied()) {
                            seat.setOccupied(true);
                            seat.setOccupant(group.getPeople().remove(0));
                            order.setTable(table);
                            order.setSeat(seat);
                            groupSize--;
                        }
                        if (groupSize == 0) {
                            return true; // All members seated
                        }
                    }
                }
                if (groupSize >= 5 && table.getSeats().size() >= 6) { // If group size is 5 or more, seat them on 6-seat tables
                    for (Seat seat : table.getSeats()) {
                        if (!seat.isOccupied()) {
                            seat.setOccupied(true);
                            seat.setOccupant(group.getPeople().remove(0));
                            order.setTable(table);
                            order.setSeat(seat);
                            groupSize--;
                        }
                        if (groupSize == 0) {
                            return true; // All members seated
                        }
                    }
                }else {
                    for (Seat seat : table.getSeats()) {
                        if (!seat.isOccupied()) {
                            seat.setOccupied(true);
                            seat.setOccupant(group.getPeople().remove(0));
                            order.setTable(table);
                            order.setSeat(seat);
                            groupSize--;
                        }
                        if (groupSize == 0) {
                            return true; // All members seated
                        }
                    }
                }
            }
        }while (groupSize!=0);



        return false; // Not enough available seats for the entire group
    }


    public static void sortTablesBySeats(List<Table> tables) {
        // Create a custom comparator to compare tables based on the number of seats
        Comparator<Table> comparator = new Comparator<Table>() {
            @Override
            public int compare(Table table1, Table table2) {
                return Integer.compare(table1.getSeats().size(), table2.getSeats().size());
            }
        };

        // Sort the tables list using the custom comparator
        Collections.sort(tables, comparator);
    }


    // Other methods...





    private static void viewOrders(Queue<Order> orderQueue) {
        for (Order order : orderQueue) {
            if(order.isDineIn()) {
                System.out.println("Table ID: " + order.getTable().getTableNumber());
                System.out.println("Occupied Seat: " + order.getSeat().getSeatNumber());
            }
            System.out.println("Items  Ordered:");
            for (Object item : order.getItems()) {
                if (item instanceof Dish) {
                    Dish dish = (Dish) item;
                    System.out.println("- Dish: " + dish.getName() + " (ID: " + dish.getItemId() + ", Price: $" + dish.getPrice() + ", Quantity: " + dish.getQuantity() + ")");
                }
                if (item instanceof Drink) {
                    Drink drink = (Drink) item;
                    System.out.println("- Drink: " + drink.getName() + " (ID: " + drink.getItemId() + ", Price: $" + drink.getPrice() + ", Quantity: " + drink.getQuantity() + ")");
                }
            }
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("Customer(s): " + order.getCustomerNames());
            System.out.println("Total Price: " + order.getTotalPrice());

            System.out.println(); // Add a blank line between orders for better readability
        }
    }
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
