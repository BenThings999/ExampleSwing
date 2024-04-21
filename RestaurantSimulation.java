// import java.beans.IndexedPropertyChangeEvent;
import java.util.*;
import java.util.regex.Pattern;


public class RestaurantSimulation {
    public static List<Table> tables = new ArrayList<>();

    static Queue<Person> customerQueue = new LinkedList<>();
    public static Queue<Order> orderQueue = new LinkedList<>();
    public static Queue<Order> servedOrders = new LinkedList<>(); // Queue for served orders
    public static List<Order> canceledOrders = new ArrayList<>();

    static List<Dish> dishes = new ArrayList<>();
    static List<Drink> drinks = new ArrayList<>();
    static Queue<Order> doneDiningOrders = new LinkedList<>();

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    static int choice;
    public static void main(String[] args) {
        // Creating a list of dishes

        dishes.add(new Dish("Beef Pares Overload (Unli Rice)", 99.99));
        dishes.add(new Dish("Pork Inihaw", 119.99));
        dishes.add(new Dish("Chicken", 69.99));
        dishes.add(new Dish("Barbeque", 35.99));
        dishes.add(new Dish("Longganisa", 21.99));

        // Creating a list of drinks

        drinks.add(new Drink("Coke", 20.99, false));
        drinks.add(new Drink("Gin", 60.55, true));
        drinks.add(new Drink("Orange Juice", 10.0, false));
        drinks.add(new Drink("Beer", 70.99, true));
        drinks.add(new Drink("Milk Tea", 39, true));

        boolean exit = false;
        double probability = 0;
        createTables();
        // Print menu
        System.out.println();
        printDishes(dishes);
        printDrinks(drinks);
        System.out.println();

        while(!exit){
            addInitialCustomers();
            if (!servedOrders.isEmpty()) {
                System.out.println("PROBABS"+probability);
                probability = removeCustomersWithProbability(servedOrders, probability, doneDiningOrders);
                System.out.println("PROBABS"+probability);
            }
            showListOfCustomers();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            printAvailableTablesAndSeats(tables);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println();
            showListOfCustomers();
            System.out.println();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            restaurantMenu();
            int choice;
            String blank = "".strip();

            while (true){
                // scanner.nextLine();
                System.out.print("\nEnter your choice: ");
                blank = scanner.nextLine();

                if(blank.trim().isBlank() || blank.trim().equals(" ")){
                    System.out.print("Blank space is not allowed.");
                    scanner.nextLine().trim();
                } else {

                    try {
                        choice = Integer.parseInt(blank);
                        switch (choice) {
                            case 1:
                                try{
                                    Person personQ = automaticallyGetCustomer();
                                    boolean dineIn = isDineIn();
                                    GroupOfPeople tempGroup = null;
                                    if(dineIn && calculateAvailableSeats()<121){
                                        tempGroup = addPersonAndCompanions(personQ);
                                    }else {
                                        System.out.println("Sorry, there are no available seats as of the moment.");
                                        while (true) {
                                            System.out.println("Do you still want to order for takeout? (yes/no)");
                                            String takeoutChoice = scanner.nextLine().toLowerCase();
                                            if (takeoutChoice.equals("yes")) {
                                                dineIn=false;
                                                break;
                                            } else if (takeoutChoice.equals("no")) {
                                                break; // Proceed to create the group for takeout
                                            } else {
                                                System.out.println("Please enter a valid input.");
                                            }
                                        }
                                    }
                                    if (tempGroup == null){
                                        dineIn=false;
                                    }
                                    if(dineIn && tempGroup != null) {

                                        Order tempOrder = addOrder(dishes, drinks, dineIn,personQ);
                                        tempOrder.getCustomerNames();
                                        if (!tempOrder.getItems().isEmpty()){
                                            System.out.println("Succesfully removed the first person in the Customers Queue "+customerQueue.remove().getName());
                                            Order order = tempOrder;
                                            GroupOfPeople group = tempGroup;
                                            order.getCustomerNames();
                                            order.setGroup(group);
                                            orderQueue.add(order);
                                            assignGroup(tables,tempGroup,order);

                                        }else {
                                            while (true){
                                                System.out.println("Do you want to remove this person from the customer queue?(yes/no)");
                                                String remove = scanner.nextLine();
                                                if(remove.equalsIgnoreCase("yes")){
                                                    System.out.println("Succesfully removed the first person in the Customers Queue "+customerQueue.remove().getName());
                                                    break;
                                                }else if(remove.equalsIgnoreCase("no")){
                                                    System.out.println("Customer order cancelled but stays in queue.");
                                                    break;
                                                }else {
                                                    System.out.println("Please enter a valid input.");
                                                }
                                            }
                                        }

                                    }else {
                                        System.out.println("Order for Take Out");
                                        Order order = addOrder(dishes, drinks, dineIn,personQ);


                                        if (!order.getItems().isEmpty()){
                                            System.out.println("Succesfully removed the first person in the Customers Queue "+customerQueue.remove().getName());
                                        }else {
                                            while (true){
                                                System.out.println("Do you want to remove this person from the customer queue?(yes/no)");
                                                String remove = scanner.nextLine();
                                                if(remove.equalsIgnoreCase("yes")){
                                                    System.out.println("Succesfully removed the first person in the Customers Queue "+customerQueue.remove().getName());
                                                    orderQueue.remove(order);
                                                    break;
                                                }else if(remove.equalsIgnoreCase("no")){
                                                    System.out.println("Customer order cancelled but stays in queue.");
                                                    break;
                                                }else {
                                                    System.out.println("Please enter a valid input.");
                                                }
                                            }
                                        }
                                    }
                                }catch(Exception e){
                                    System.out.println();
                                }
                                break;
                            case 2:
                                viewOrders((Queue<Order>) orderQueue);
                                break;
                            case 3:
                                Order order = Order.findOrderById(orderQueue);
                                order.updateOrder(order);
                                break;
                            case 4:
                                viewServedOrders(servedOrders);
                                break;
                            case 5:
                                viewCanceledOrders();
                                break;
                            case 6:
                                exit = true;
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        // scanner.nextLine();
                    }
                }
            }
        }

    }

    private static void restaurantMenu() {
        System.out.println("\n=== Restaurant Management System ===");
        System.out.println("1. Add Order");
        System.out.println("2. View Orders");
        System.out.println("3. Update Order");
        System.out.println("4. View Served Orders");
        System.out.println("5. View Canceled Orders");
        System.out.println("6. Exit");
    }

    // Overloaded method to print dishes
    private static void printDishes(List<Dish> dishes) {
        System.out.println("Dishes:");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-50s | %-8s |\n", "No.", "Name", "Price");
        System.out.println("-------------------------------------------------------------------------");
        int dishCounter = 1;
        for (Dish dish : dishes) {
            printItem(dishCounter, dish.getName(), dish.getPrice());
            dishCounter++; // Increment counter for the next dish
        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-61s |\n", "0", "Cancel");
        System.out.println("-------------------------------------------------------------------------");
    }

    private static void printDrinks(List<Drink> drinks) {
        System.out.println("Drinks:");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-50s | %-8s |\n", "No.", "Name", "Price");
        System.out.println("-------------------------------------------------------------------------");
        int drinkCounter = 1;
        for (Drink drink : drinks) {
            printItem(drinkCounter, drink.getName(), drink.getPrice());
            drinkCounter++; // Increment counter for the next drink
        }
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("| %-4s | %-61s |\n", "0", "Cancel");
        System.out.println("-------------------------------------------------------------------------");
    }


    private static void printItem(int i, String name, double price) {
        // Truncate name if it exceeds 20 characters
        if (name.length() > 50) {
            name = name.substring(0, 17) + "...";
        }
        // Format and print the item with its number
        System.out.printf("| %-4s | %-50s | ₱%-7.2f |\n", i, name, price);
    }

    private static void createTables() {
        // Create 10 tables with 2 seats each
        for (int i = 21; i <= 30; i++) {
            tables.add(new Table(i, 2));
        }
        // Create 10 tables with 4 seats each
        for (int i = 11; i <= 20; i++) {
            tables.add(new Table(i, 4));
        }
        // Create 10 tables with 6 seats each
        for (int i = 1; i <= 10; i++) {
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

     private static void showAvailableTablesAndSeats() {
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

    public static void updateAvailableTables() {
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


    private static Boolean isDineIn() {
        do{
            System.out.print("====================================\n");
            System.out.print("Is this a dine-in order? (yes/no): ");
            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("yes")){
                return true;
            } else if(input.equalsIgnoreCase("no")){
                return false;
            } else {
                System.out.println("The input you put is invalid");
            }
        }while(true);
    }

    private static void showListOfCustomers() {
        System.out.println("{ List of Customers }");
        for (Person customer : customerQueue) {
            System.out.println("-> "+ customer.getName());
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
    public static Order addOrder(List<Dish> dishes, List<Drink> drinks, Boolean dineIn, Person person) {
        Scanner scanner = new Scanner(System.in);
        // Create a new order
        Order order = new Order(dineIn);
        System.out.print("=====================================\n");
        System.out.printf("Name: %s%n", person.getName());
        // Add items to the order
        boolean addMoreItems = true;
        int choice;
        HashMap<Item, Integer> tempOrderItems = new HashMap<>(); // Temporary order items

        while (addMoreItems) {
            String option[] = {"Dish", "Drink", "Finish Adding Items","Cancel Order"};
            System.out.println("\n========= Select Item Type ========");
            for (int i = 0; i < option.length; i++) {
                System.out.println((i + 1) + ". " + option[i]);
            }
            int dishIndex;
            int quantity;
            System.out.println("=====================================");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice <= 0 && choice >= 4) {
                    System.out.println("Invalid choice ");
                } else {

                    switch (choice) {
                        case 1:
                            printDishes(dishes);
                            boolean flag = false;
                            while (!flag) {
                                try {
                                    while (true){
                                        System.out.print("Enter the number of the dish: ");

                                        dishIndex = Integer.parseInt(scanner.nextLine().trim());
                                        if(dishIndex==0){
                                            break;
                                        }
                                        if(dishIndex >= 1 && dishIndex <= dishes.size() ){
                                            break;
                                        }else {
                                            System.out.println("Please enter a valid dish");
                                        }
                                    }

                                    while (!flag) {
                                        try {
                                            Dish selectedDish = dishes.get(dishIndex - 1);
                                            System.out.print("Enter the quantity: ");
                                            quantity = Integer.parseInt(scanner.nextLine().trim());

                                            if (quantity >= 1) {
                                                System.out.println(quantity + " " + selectedDish.getName() + " added to the order.");
                                                tempOrderItems.put(selectedDish, quantity); // Add to temporary order
                                                flag = true;
                                                break;
                                            } else {
                                                System.out.println("Quantity cannot be less than 1");
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input. Please enter the valid input. ");
                                            scanner.nextLine();
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter the valid input. ");

                                }
                            }
                            break;
                        case 2:
                            int drinkIndex;
                            printDrinks(drinks);
                            boolean drinkFlag = false;
                            while (!drinkFlag) {
                                try {
                                    System.out.print("Enter the number of the drink: ");
                                    drinkIndex = Integer.parseInt(scanner.nextLine().trim());
                                    if(drinkIndex==0){
                                        break;
                                    }
                                    if (drinkIndex < 1 || drinkIndex > drinks.size()) {
                                        System.out.print("The number input is not a drink number.\nPlease enter a drink number between 1 and " + drinks.size() + ".\n\n");
                                        continue;
                                    }else {
                                        System.out.println("Please enter a valid drink");
                                    }
                                    while (!drinkFlag) {
                                        try {
                                            Drink selectedDrink = drinks.get(drinkIndex - 1);
                                            System.out.print("Enter the quantity: ");
                                            quantity = Integer.parseInt(scanner.nextLine().trim());
                                            if (quantity > 0) {
                                                System.out.println(quantity + " " + selectedDrink.getName() + " added to the order.");
                                                tempOrderItems.put(selectedDrink, quantity); // Add to temporary order
                                                drinkFlag = true;
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input. Please enter the valid input. ");
                                            // scanner.nextLine();
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter the valid input. ");
                                    // scanner.nextLine();
                                }
                            }
                            break;

                        case 3:
                            double totalPrice = calculateTotalPrice(tempOrderItems);
                            if (totalPrice == 0) {
                                System.out.println("=================================================");
                                System.out.print("|| No items ordered. Kindly order first. ||\n");
                                System.out.println("=================================================");
                            } else {
                                System.out.println("Total price: ₱" + totalPrice);

                                System.out.print("Enter customer's money: ₱");
                                double customerMoney = 0;
                                boolean checkTheMoney = false;
                                while (!checkTheMoney) {
                                    try {
                                        customerMoney = scanner.nextDouble();

                                        if (customerMoney < totalPrice) {
                                            System.out.print("Insufficient amount of money\n");
                                        } else if (customerMoney >= totalPrice) {
                                            double change = customerMoney - totalPrice;
                                            if (order != null) {
                                                System.out.printf("Change: ₱ %.2f\n", change);
                                                // Add temporary items to the order
                                                for (Map.Entry<Item, Integer> entry : tempOrderItems.entrySet()) {
                                                    order.addItem(entry.getKey(), entry.getValue());
                                                }
                                                tempOrderItems.clear(); // Clear temporary order
                                                checkTheMoney = true;
                                                addMoreItems = false;
                                            }
                                        }

                                    } catch (InputMismatchException e) {
                                        System.out.print("Invalid input you must enter money.");
                                        System.out.println();
                                        scanner.nextLine();
                                    }
                                }
                                order.addCustomer(person);

                            }
                            break;
                        case 4:
                            while (true) {
                                int i=1;
                                if(!tempOrderItems.isEmpty()){
                                    System.out.println("List of selected items:");
                                }
                                for (Map.Entry<Item, Integer> entry : tempOrderItems.entrySet()){
                                    System.out.println("["+i+"]Item: "+entry.getKey().getName()+" Quantity: "+entry.getValue());
                                    i++;
                                }
                                System.out.println("Are you sure you want to cancel your orders?(yes/no)");
                                String answer = scanner.nextLine();
                                if(answer.equalsIgnoreCase("yes")) {
                                    addMoreItems = false; // Exit the loop
                                    tempOrderItems.clear(); // Clear temporary order
                                    break;
                                }else if(answer.equalsIgnoreCase("no")){
                                    break;
                                }else {
                                    System.out.println("Please enter a valid input");
                                }


                            }
                            break;

                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                            break;
                    }
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
        return order;
    }


    // Overloaded addOrder method for updating an existing order
    public static void addItemToOrder(Scanner scanner, Order order, List<Dish> dishes, List<Drink> drinks){
        Pattern createPattern = Pattern.compile("\\S*");

        System.out.println("Available Dishes:");
        printDishes(dishes);
        System.out.println("Available Drinks:");
        printDrinks(drinks);


        String itemName;

        while(true){
            System.out.print("Enter item name: ");
            itemName = scanner.nextLine();
            if(createPattern.matcher(itemName).matches()){
                break;
            } else {
                System.out.print("Item can't be empty nor integer/ symbolic.");
            }
        }

        int itemType, quantity;

        while(true){
            System.out.print("Enter item type (1 for Dish, 2 for Drink): ");
            try{
                itemType = Integer.parseInt(scanner.nextLine().trim());
                if(itemType != 1 && itemType != 2){
                    throw new IllegalArgumentException("Invalid item.");
                }
                break;
            } catch(NumberFormatException e){
                System.out.println("Please enter 1 or 2 only.");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }

        while(true){
            System.out.print("Enter item quantity: ");
            try{
                quantity  = Integer.parseInt(scanner.nextLine().trim());
                if(quantity <= 0){
                    throw new IllegalArgumentException("Quantity of items must be above 0 ");
                }
                break;
            } catch(NumberFormatException e){
                System.out.println("Please enter valid value");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }



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

    public static double removeCustomersWithProbability(Queue<Order> servedOrders, double probability, Queue doneDiningOrders) {
        Random random = new Random();
        for (Order order : servedOrders) {
            if (random.nextDouble() < probability && order.isServed()) {
                for (Person customer : order.customers) {
                    customer.getSeat().setOccupant(null);
                    customer.getSeat().setOccupied(false);
                }
                doneDiningOrders.add(servedOrders);
                System.out.println("Customer finished dining");
                probability = 0; // probability will reset to zero
            }else {
                probability+=25;

            }
        }
        return probability;



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
        // }
    }


    public static GroupOfPeople addPersonAndCompanions(Person person) {
        Random random = new Random();
        int numberOfPeople;
        // Ask how many people are with the customer
        while(true){
            try{
                System.out.print("Number of people(including the customer): ");
                numberOfPeople = Integer.parseInt(scanner.nextLine());
                if (numberOfPeople < 1) {
                    System.out.println("Please enter a valid Natural Number");
                } else if (numberOfPeople > calculateAvailableSeats()) {
                    System.out.println("Sorry, there are not enough seats available.");
                    while (true) {
                        System.out.println("Do you still want to order for takeout? (yes/no)");
                        String takeoutChoice = scanner.nextLine().toLowerCase();
                        if (takeoutChoice.equals("yes")) {
                            System.out.println("Exiting group creation.");
                            return null; // Don't create group if no seats and no takeout
                        } else if (takeoutChoice.equals("no")) {
                            break; // Proceed to create the group for takeout
                        } else {
                            System.out.println("Please enter a valid input.");
                        }
                    }
                } else {
                    break; // Input is valid and enough seats are available
                }

            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Invalid input.");
            }

        }

        // Create a group of people and add the initial person
        GroupOfPeople group = new GroupOfPeople();
        group.addPerson(person);

        // Add companions to the group
        for (int i = 1; i < numberOfPeople; i++) {
            int randomNum = 10000 + random.nextInt(90000);
            group.addPerson(new Person("Customer" + randomNum));
        }

        return group; // Return the group (even for takeout)
    }

    public static void printAvailableTablesAndSeats(List<Table> tables) {
        System.out.println("Available Tables and Seats:");
        for (Table table : tables) {
            System.out.println("Table " + table.getTableNumber() + ":");
            for (Seat seat : table.getSeats()) {
                if (!seat.isOccupied()) {
                    System.out.println("  Seat " + seat.getSeatNumber() + ": Available");
                } else {
                    System.out.println("  Seat " + seat.getSeatNumber() + ": Occupied by " + seat.getOccupant().getName());
                }
            }
        }
    }

    public static boolean assignGroup(List<Table> tables, GroupOfPeople group, Order order) {
        int groupSize = group.getPeople().size();
        tables = Table.sortTablesByAvailableSeats(tables);
        for (Person person : group.getPeople()) {
            order.addCustomer(person);
            person.setGroup(group);
        }

        for (Table table : tables) {
            for (Seat seat : table.getSeats()) {
                if (!seat.isOccupied()) {
                    seat.setOccupied(true);

                    group.getPeople().get(0).setSeat(seat);
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
        return false; // Not enough available seats for the entire group
    }



    public static double calculateTotalPrice(Map<Item, Integer> tempOrderItems) {
        double totalPrice = 0;
        for (Map.Entry<Item, Integer> entry : tempOrderItems.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;
        }
        return totalPrice;
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
        if(orderQueue.isEmpty()){
            System.out.println("There's no order available try to add first");
        } else {
            for (Order order : orderQueue) {
                if(order.isDineIn()) {
                    // System.out.println("Table ID: " + order.getTable().getTableNumber());
                    System.out.println("Occupied Seat: " + order.getTotalOccupiedSeats());
                }
                System.out.println("Items  Ordered:");
                for (Object item : order.getItems()) {
                    if (item instanceof Dish) {
                        Dish dish = (Dish) item;
                        System.out.println("- Dish: " + dish.getName() + " (ID: " + dish.getItemId() + ", Price: ₱" + dish.getPrice() + ", Quantity: " + dish.getQuantity() + ")");
                    }
                    if (item instanceof Drink) {
                        Drink drink = (Drink) item;
                        System.out.println("- Drink: " + drink.getName() + " (ID: " + drink.getItemId() + ", Price: ₱" + drink.getPrice() + ", Quantity: " + drink.getQuantity() + ")");
                    }
                }
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Customer(s): " + order.getCustomerNames());
                System.out.println("Total Price: " + order.getTotalPrice());
                System.out.println(); // Add a blank line between orders for better readability
            }
        }

    }

    private static void viewServedOrders(Queue<Order> servedOrders) {
        System.out.println("=== Served Orders ===");
        if (servedOrders.isEmpty()) {
            System.out.println("No served orders available.");
            return;
        }
        for (Order order : servedOrders) {
            System.out.println(order);
            System.out.println(); // Add an empty line for readability
        }
    }
    public static void viewCanceledOrders() {
        System.out.println("=== Canceled Orders ===");
        if (canceledOrders.isEmpty()) {
            System.out.println("No canceled orders available.");
        } else {
            for (Order order : canceledOrders) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Customer(s): " + order.getCustomerNames());
                System.out.println("Canceled Items:");
                for (Object item : order.getItems()) {
                    if (item instanceof Dish) {
                        Dish dish = (Dish) item;
                        System.out.println("- Dish: " + dish.getName() + " (Price: ₱" + dish.getPrice() + ", Quantity: " + dish.getQuantity() + ")");
                    } else if (item instanceof Drink) {
                        Drink drink = (Drink) item;
                        System.out.println("- Drink: " + drink.getName() + " (Price: ₱" + drink.getPrice() + ", Quantity: " + drink.getQuantity() + ")");
                    }
                }
                System.out.println(); // Add a blank line between orders for better readability
            }
        }
    }
    public static int calculateAvailableSeats() {
        int availableSeats = 0;
        for (Table table : tables) {
            for (Seat seat : table.getSeats()) {
                if (!seat.isOccupied()) {
                    availableSeats++;
                }
            }
        }
        return availableSeats;
    }

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