import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyStore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Client {
    public static final String BOLD_RED = "\u001B[1;31m";
    public static final String RESET = "\u001B[0m";
    public static Customer loginCustomer;
    public static String loggedInUsername;
    public static Scanner scanner = new Scanner(System.in);
    public static FOSInterface stub = null;
    public static void main(String[] args) {
        try {
            // Load keystore for the client
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream("./rmi-client.p12"), "password".toCharArray());

            // Set up key and trust managers
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, "password".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Set up SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Create SSL socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create RMI client socket factory
            CustomRMIClientSocketFactory customClientSocketFactory = new CustomRMIClientSocketFactory(sslSocketFactory);

            // Get the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1100, customClientSocketFactory);

            // Lookup the remote object
            stub = (FOSInterface) registry.lookup("FOSInterface");

            while (true) {
                    System.out.println("            ______________________");
                    System.out.println("           /        MCGEE         \\");
                    System.out.println("          /       RESTAURANT       \\");
                    System.out.println("         /__________________________\\");
                    System.out.println("         |    ______    ____    ____|");
                    System.out.println("         |   |      |  |    |  |    |");
                    System.out.println("         |___|______|__|____|__|____|");
                    System.out.println("         |         WELCOME!         |");
                    System.out.println("         |__________________________|");
                System.out.println("╔═══════════════════════════════════════════╗\n" +
                        "║         Welcome to McGee Restaurant       ║\n" +
                        "╚═══════════════════════════════════════════╝ \n" +
                        "Select your role:\n" +
                        "1. Admin\n" +
                        "2. Customer\n" +
                        "3. Exit\n" +
                        "\nEnter your choice: ");
                Scanner scanner = new Scanner(System.in);
                int role;
                try {
                    role = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println(BOLD_RED + "Invalid input. Please enter 1 or 2 to select your role." + RESET);
                    continue;
                }

                if (role == 1) {
                    while (true) {
                        System.out.println("\n╔═════════════════════════════════╗\n" +
                                "║           Admin Login           ║\n" +
                                "╚═════════════════════════════════╝");
                        System.out.println("Enter admin password or enter -1 to exit:");
                        String password = scanner.nextLine();
                        if (password.equals("-1")) break;
                        if (password.equals("1234")) {
                            System.out.println("Admin login successfully.");
                            while (true) {
                                System.out.println("\n╔═════════════════════════════════╗\n" +
                                        "║            Admin Menu           ║\n" +
                                        "╚═════════════════════════════════╝\n" +
                                        "1. Food Category \n2. Food Items \n3. View Food Order \n4. View Total Sales \n5. Exit");
                                System.out.println("Enter number to choose:");
                                int answer = 0;
                                try {
                                    answer = Integer.parseInt(scanner.nextLine());
                                    if(answer<1 || answer > 5) {
                                        System.out.println(BOLD_RED + "Please choose from the menu.\n" + RESET);
                                    }
                                    if (answer == 5) break;
                                } catch (NumberFormatException e) {
                                    System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                }
                                if (answer == 1) {
                                    while (true) {
                                        System.out.println("\n------------------------------------\n" +
                                                "      Admin Edit Food Category      \n" +
                                                "------------------------------------\n" +
                                                "1. View Food Category\n2. Create Food Categories\n3. Update Food Categories \n4. Delete Food Categories \n5. Exit");
                                        System.out.println("Enter menu number to choose: ");
                                        int choice = 0;
                                        try {
                                            choice = Integer.parseInt(scanner.nextLine());

                                        } catch (NumberFormatException e) {
                                            System.out.println(BOLD_RED + "Invalid input. Please enter a number.\n" + RESET);
                                            continue;
                                        }

                                        switch (choice) {
                                            case 1:
                                                Map<String, ?> categories = stub.viewFoodData("FoodCategory");
                                                if (categories.isEmpty()) {
                                                    System.out.println(BOLD_RED + "No food categories found." + RESET);
                                                    System.out.println("*".repeat(40));
                                                    break;
                                                } else {
                                                    System.out.println("\n═════════════ View Food Category ══════════════");
                                                    for (Map.Entry<String, ?> entry : categories.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue());
                                                    }
                                                }
                                                break;
                                            case 2:
                                                System.out.println("\n═════════════ Create Food Category ══════════════\nIf you want to exit, please enter -1");
                                                System.out.println("Enter food category ID to add:");
                                                String categoryID = scanner.nextLine();
                                                if (categoryID.equals("-1")) break;
                                                System.out.println("Enter food category name:");
                                                String categoryName = scanner.nextLine();
                                                stub.createFoodCategory(categoryID, categoryName);
                                                System.out.println("Food category created successfully.");
                                                break;
                                            case 3:
                                                while (true) {
                                                    Map<String, ? > categories1 = stub.viewFoodData("FoodCategory");
                                                    if (categories1.isEmpty()) {
                                                        System.out.println(BOLD_RED + "\nNo food categories found." + RESET);
                                                        System.out.println("*".repeat(40));
                                                        break;
                                                    } else {
                                                        System.out.println("\n═════════════ Update Food Category ══════════════");
                                                        for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                        }
                                                    }
                                                    System.out.println("Enter food category ID to modify or -1 to exit: ");
                                                    String categoryID1 = scanner.nextLine();
                                                    if (categoryID1.equals("-1")) break;
                                                    if (!categoryID1.isEmpty() && categories1.containsKey(categoryID1)) {
                                                        System.out.println("Do you want to change food category ID or food category name? \n1. Food Category ID \n2. Food Category Name \n3. Back");
                                                        int answer1;
                                                        try {
                                                            answer1 = Integer.parseInt(scanner.nextLine());
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                                            continue;
                                                        }
                                                        if (answer1 == 1) {
                                                            System.out.println("Enter new food category ID:");
                                                            String newCategoryID = scanner.nextLine();
                                                            stub.updateFoodCategoryID(categoryID1, newCategoryID);
                                                            System.out.println("\nFood category ID updated successfully.");
                                                        } else if (answer1 == 2) {
                                                            System.out.println("Enter new food category Name:");
                                                            String newCategoryName = scanner.nextLine();
                                                            stub.updateFoodCategoryName(categoryID1, newCategoryName);
                                                            System.out.println("\nFood category name updated successfully.");
                                                        } else if (answer1 == 3) {
                                                            break;
                                                        } else {
                                                            System.out.println("Please choose from the menu number.");
                                                        }
                                                    } else {
                                                        System.out.println(BOLD_RED + "\nFood Category ID not found. Please try again or enter -1 to exit."+ RESET);
                                                    }
                                                }
                                                break;
                                            case 4:
                                                while (true) {
                                                    System.out.println("\n═════════════ Update Food Category ══════════════");
                                                    Map<String, ?> categories1 = stub.viewFoodData("FoodCategory");
                                                    if (categories1.isEmpty()) {
                                                        System.out.println(BOLD_RED + "No food categories found." + RESET);
                                                    } else {
                                                        for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                        }
                                                    }
                                                    System.out.println("Enter food category ID to delete or enter -1 to exit:");
                                                    String categoryIDToDelete = scanner.nextLine();
                                                    if (categoryIDToDelete.equals("-1")) break;
                                                    if (!categoryIDToDelete.isEmpty() && categories1.containsKey(categoryIDToDelete)) {
                                                        stub.delete(categoryIDToDelete, "category");
                                                        System.out.println("Food category deleted successfully");
                                                    }else{
                                                        System.out.println(BOLD_RED + "Food Category ID not found. Please try again." + RESET);
                                                    }
                                                }
                                                break;
                                            case 5:
                                                break;
                                            default:
                                                System.out.println(BOLD_RED + "Please choose from the menu."+ RESET);
                                                break;
                                        }if (choice == 5){
                                            break;
                                        }
                                    }
                                } else if (answer == 2) {
                                    System.out.println("\n---------------------------------\n" +
                                            "      Admin Edit Food Items      \n" +
                                            "---------------------------------\n" +
                                            "1. View Food Items\n2. Create Food Items\n3. Update Food Items \n4. Delete Food Items \n5. Exit");
                                    System.out.println("Enter menu number to choose:");
                                    int choice;
                                    try {
                                        choice = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                        continue;
                                    }

                                    switch (choice){
                                        case 1:
                                            Map<String, ?> categories = stub.viewFoodData("FoodItems");
                                            Map<String, ?> categories2 = stub.viewFoodData("DrinkItems");

                                            if (categories.isEmpty()) {
                                                System.out.println(BOLD_RED + "\nNo food categories found." + RESET);
                                            } else {
                                                System.out.println("\n═════════════ View Food Items ══════════════");
                                                if (categories.isEmpty()) {
                                                    System.out.println("No food items found.");
                                                    System.out.println("*".repeat(40));
                                                } else {
                                                    System.out.println("Food Items:");
                                                    for (Map.Entry<String, ?> entry : categories.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                    System.out.println("*".repeat(40));
                                                }
                                                if (categories2.isEmpty()) {
                                                    System.out.println("No drink items found.");
                                                    System.out.println("*".repeat(40));
                                                } else {
                                                    System.out.println("Drink Items:");
                                                    for (Map.Entry<String, ?> entry : categories2.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                    System.out.println("*".repeat(40));
                                                }
                                            }
                                            break;
                                        case 2:
                                            while (true) {
                                                System.out.println("\n═════════════ Create Food Items ══════════════\n");
                                                System.out.println("Create Food Item for McGee");
                                                System.out.println("1. Food Item \n2. Drink Item \n3. Exit");
                                                int choice1 = 0;
                                                try {
                                                    choice1 = Integer.parseInt(scanner.nextLine());
                                                    if (choice1 < 1 || choice1 > 3) {
                                                        System.out.println(BOLD_RED + "Invalid choice" + RESET);
                                                        continue;
                                                    }
                                                } catch (NumberFormatException e) {
                                                    System.out.println(BOLD_RED+"Invalid input. Please enter a number."+RESET);
                                                    continue;
                                                }

                                                switch (choice1) {
                                                    case 1:
                                                        System.out.println("Enter food item name to add:");
                                                        String foodName = scanner.nextLine().trim();
                                                        if (foodName.equals("-1")) break;
                                                        System.out.println("Enter food price:");
                                                        double foodPrice = Double.parseDouble(scanner.nextLine());
                                                        if (foodPrice == -1) break;
                                                        Map<String, ?> categories1 = stub.viewFoodData("FoodCategory");
                                                        if (categories1.isEmpty()) {
                                                            System.out.println("*".repeat(40));
                                                            System.out.println("No food categories found.");
                                                            System.out.println("*".repeat(40));
                                                        } else {
                                                            System.out.println("Food Categories:");
                                                            for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                                System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                            }
                                                            System.out.println("*".repeat(40));
                                                        }
                                                        System.out.println("Enter food category ID for the new food item or -1 to exit:");
                                                        String foodCategoryID = scanner.nextLine().trim();
                                                        if (foodPrice == -1) break;

                                                        if (categories1.containsKey(foodCategoryID)) {
                                                            FoodCategory foodCategory = (FoodCategory) categories1.get(foodCategoryID);
                                                            String categoryName = foodCategory.getCategoryName();
                                                            if (!foodCategoryID.isEmpty() && !foodName.isEmpty() && foodPrice > 0) {
                                                                stub.createFoodItems(foodName, foodPrice, categoryName);
                                                                System.out.println("Food Item created successfully.");
                                                                System.out.println("*".repeat(40));
                                                            } else {
                                                                System.out.println("Please select food category ID from the list.");
                                                                System.out.println("*".repeat(40));
                                                            }
                                                        } else {
                                                            System.out.println("Please select food category ID from the list.");
                                                            System.out.println("*".repeat(40));
                                                        }
                                                        break;
                                                    case 2:
                                                        System.out.println("Enter drink item name to add or enter -1 to exit:");
                                                        String drinkName = scanner.nextLine().trim();
                                                        if (drinkName.equals("-1")) break;
                                                        System.out.println("Enter drink price:");
                                                        double drinkPrice = Double.parseDouble(scanner.nextLine());
                                                        if (drinkPrice == -1) break;
                                                        if (drinkPrice > 0) {
                                                            stub.createDrinkItems(drinkName, drinkPrice);
                                                            System.out.println("Drink Item created successfully.");
                                                            System.out.println("*".repeat(40));
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }

                                                if (choice1 == 3) {
                                                    break;
                                                }
                                            }
                                            break;
                                        case 3:
                                            while (true) {
                                                Map<String,?> categories1 = stub.viewFoodData("FoodItems");
                                                if (categories1.isEmpty()) {
                                                    System.out.println(BOLD_RED + "\nNo food items found." + RESET);
                                                } else {
                                                    System.out.println("\n═════════════ Update Food Items ══════════════");
                                                    for (Map.Entry<String,?> entry : categories1.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                }

                                                System.out.println("Enter food item ID to modify or enter -1 to exit:");
                                                String foodID = scanner.nextLine().trim();
                                                if (foodID.equals("-1")) break;
                                                if (categories1.containsKey(foodID)) {
                                                    System.out.println("Do you want to change food item name or food item price? \n1. Food Item Name \n2. Food Item Price \n3. Back");
                                                    int answer2;
                                                    try {
                                                        answer2 = Integer.parseInt(scanner.nextLine());
                                                    } catch (NumberFormatException e) {
                                                        System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                                        continue;
                                                    }

                                                    if (answer2 == 1) {
                                                        System.out.println("Enter new food item name:");
                                                        String newFoodName = scanner.nextLine().trim();
                                                        try {
                                                            stub.updateFoodItemName(foodID, newFoodName);
                                                            System.out.println("Food item name updated successfully.");
                                                        } catch (RemoteException e) {
                                                            System.out.println(BOLD_RED + "Error updating food item name: " + e.getMessage() + RESET);

                                                        }
                                                    } else if (answer2 == 2) {
                                                        System.out.println("Enter new food item price:");
                                                        double newFoodPrice;
                                                        try {
                                                            newFoodPrice = Double.parseDouble(scanner.nextLine());
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(BOLD_RED + "Invalid input. Please enter a valid price." + RESET);
                                                            continue;
                                                        }
                                                        try {
                                                            stub.updateFoodItemPrice(foodID, newFoodPrice);
                                                            System.out.println("Food item price updated successfully.");
                                                        } catch (RemoteException e) {
                                                            System.out.println(BOLD_RED + "Error updating food item price: " + e.getMessage() + RESET);
                                                        }
                                                    } else if (answer2 == 3) {
                                                        break;
                                                    } else {
                                                        System.out.println("Please choose from the menu number.");
                                                    }
                                                } else {
                                                    System.out.println(BOLD_RED +"Food item ID not found. Please try again." + RESET);
                                                }
                                            }
                                            break;

                                        case 4:
                                            while (true) {
                                                Map<String, ?> categories1 = stub.viewFoodData("FoodItems");
                                                if (categories1.isEmpty()) {
                                                    System.out.println(BOLD_RED +"\nNo food item found." + RESET);
                                                } else {
                                                    System.out.println("\n═════════════ Delete Food Items ══════════════:");
                                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                }
                                                System.out.println("\nEnter food item ID to delete or enter -1 to exit:");
                                                String foodID = scanner.nextLine().trim();
                                                if (foodID.equals("-1")) break;
                                                if (foodID.isEmpty()) {
                                                    System.out.println(BOLD_RED + "Invalid input. Please enter food item ID from the list." + RESET);
                                                    continue;
                                                }
                                                if (categories1.containsKey(foodID)) {
                                                    stub.delete(foodID, "item");
                                                } else {
                                                    System.out.println(BOLD_RED + "Food item ID not found." + RESET);
                                                }
                                            }
                                        case 5:
                                            break;
                                        default:
                                            System.out.println(BOLD_RED + "Invalid choice." + RESET);
                                            break;
                                    }
                                } else if (answer ==3){
                                    while (true) {
                                        System.out.println("\n-----------------------------------------\n" +
                                                "     McGee Restaurant Food Orders      \n" +
                                                "-----------------------------------------");
                                        Map<String, ?> categories1 = stub.viewFoodData("FoodOrder");
                                        if (categories1.isEmpty()) {
                                            System.out.println(BOLD_RED + "No food order found." + RESET);
                                            System.out.println("*".repeat(40));
                                            break;
                                        } else {
                                            boolean hasPendingOrders = false;
                                            System.out.println("Food Orders:");
                                            for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                Order foodItem = (Order) entry.getValue();
                                                if (foodItem.getStatus().equals("Pending")) {
                                                    System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    hasPendingOrders = true;
                                                }
                                            }
                                            if (!hasPendingOrders) {
                                                System.out.println(BOLD_RED +"No incoming food orders." + RESET);
                                                break;
                                            }
                                            System.out.println("Enter food order ID that you have completed or enter -1 to exit:");
                                            String foodOrderID = scanner.nextLine();
                                            if (foodOrderID.equals("-1")) break;
                                            if (!foodOrderID.isEmpty() && categories1.containsKey(foodOrderID)) {
                                                stub.updateOrderStatus(foodOrderID, "Completed");
                                                System.out.println("Order status updated to Completed.");
                                            } else {
                                                System.out.println(BOLD_RED + "Please enter a valid food order ID." + RESET);
                                            }
                                        }
                                    }
                                }else if (answer==4){
                                    while (true) {
                                        System.out.println("\n-----------------------------------\n" +
                                                "         View Total Sales      \n" +
                                                "-----------------------------------\n" +
                                                "1. View total sales per day" + "\n2. View total sales per week" + "\n3. View total sales per month" + "\n4. Exit");
                                        System.out.println("Enter number to choose:");
                                        int salesChoice = 0;
                                        try {
                                            salesChoice = Integer.parseInt(scanner.nextLine());
                                            if (salesChoice > 4 || salesChoice < 1) {
                                                System.out.println(BOLD_RED + "Invalid choice. Please choose from menu" + RESET);
                                                continue;
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                        }

                                        switch (salesChoice) {
                                            case 1:
                                                while (true) {
                                                    try {
                                                        System.out.print("\nEnter date (YYYY-MM-DD) to view sales for the day or enter -1 to exit: ");
                                                        String dateInput = scanner.nextLine();
                                                        if (dateInput.equals("-1")) break;
                                                        LocalDate date = LocalDate.parse(dateInput);
                                                        double dailySales = 0.0;
                                                        try {
                                                            Map<String, ?> orders = stub.viewFoodData("FoodOrder");
                                                            for (Map.Entry<String, ?> entry : orders.entrySet()) {
                                                                Order order = (Order) entry.getValue();
                                                                LocalDateTime orderTime = order.getOrderTime();
                                                                if (orderTime != null && orderTime.toLocalDate().equals(date)) {
                                                                    dailySales += order.getPrice();
                                                                }
                                                            }
                                                        } catch (RemoteException e) {
                                                            System.out.println(BOLD_RED + "Error retrieving sales data: " + e.getMessage() + RESET);
                                                        }
                                                        System.out.println("Total sales for " + date + ": " + dailySales);
                                                    } catch (Exception ex) {
                                                        System.out.println(BOLD_RED + "Incorrect date format. Please enter the date in 'YYYY-MM-DD' format." + RESET);
                                                        continue;
                                                    }
                                                }
                                                break;
                                            case 2:
                                                while (true) {
                                                    try {
                                                        System.out.print("\nEnter date (YYYY-MM-DD) to view sales for the week or enter -1 to exit: ");
                                                        String weekDateInput = scanner.nextLine();
                                                        if (weekDateInput.equals("-1")) break;
                                                        LocalDate weekDate = LocalDate.parse(weekDateInput);
                                                        double weeklySales = 0.0;
                                                        try {
                                                            Map<String, ?> orders = stub.viewFoodData("FoodOrder");
                                                            for (Map.Entry<String, ?> entry : orders.entrySet()) {
                                                                Order order = (Order) entry.getValue();
                                                                LocalDateTime orderTime = order.getOrderTime();
                                                                if (orderTime != null && orderTime.toLocalDate().isAfter(weekDate.minusDays(1)) &&
                                                                        orderTime.toLocalDate().isBefore(weekDate.plusDays(7))) {
                                                                    weeklySales += order.getPrice();
                                                                }
                                                            }
                                                        } catch (RemoteException e) {
                                                            System.out.println("Error retrieving sales data: " + e.getMessage());
                                                        }
                                                        System.out.println("Total sales for the week starting " + weekDate + ": " + weeklySales);
                                                    } catch (Exception ex) {
                                                        System.out.println(BOLD_RED + "Incorrect date format. Please enter the date in 'YYYY-MM-DD' format." + RESET);
                                                        continue;
                                                    }
                                                }
                                                break;
                                            case 3:
                                                while (true) {
                                                    try {
                                                        System.out.print("\nEnter year and month (YYYY-MM) to view sales for the month or enter -1 to exit: ");
                                                        String monthInput = scanner.nextLine();
                                                        if (monthInput.equals("-1")) break;
                                                        LocalDate monthDate = LocalDate.parse(monthInput + "-01");
                                                        double monthlySales = 0.0;
                                                        try {
                                                            Map<String, ?> orders = stub.viewFoodData("FoodOrder");
                                                            for (Map.Entry<String, ?> entry : orders.entrySet()) {
                                                                Order order = (Order) entry.getValue();
                                                                LocalDateTime orderTime = order.getOrderTime();
                                                                if (orderTime != null && orderTime.getMonth().equals(monthDate.getMonth()) &&
                                                                        orderTime.getYear() == monthDate.getYear()) {
                                                                    monthlySales += order.getPrice();
                                                                }
                                                            }
                                                        } catch (RemoteException e) {
                                                            System.out.println(BOLD_RED + "Error retrieving sales data: " + e.getMessage() + RESET);
                                                            break;
                                                        }
                                                        System.out.println("Total sales for " + monthDate.getMonth() + " " + monthDate.getYear() + ": " + monthlySales);
                                                    } catch (Exception e) {
                                                        System.out.println(BOLD_RED + "Incorrect date format. Please enter the date in 'YYYY-MM' format." + RESET);
                                                        continue;
                                                    }
                                                }
                                                break;
                                            case 4:
                                                break;
                                            default:
                                                break;
                                        }if (salesChoice == 4){
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            System.out.println(BOLD_RED + "Incorrect password. Please try again." + RESET);
                        }
                    }
                } else if (role == 2) {
                        while(true) {
                            System.out.println("\n╔════════════════════════════════════════════════════════╗\n" +
                                    "║          Welcome to McGee Food Ordering System         ║\n" +
                                    "╚════════════════════════════════════════════════════════╝");
                            System.out.println("1. Register");
                            System.out.println("2. Login");
                            System.out.println("3. Exit");

                            int userInput = 0;
                                try {
                                    System.out.print("Enter your option: ");
                                    userInput = Integer.parseInt(scanner.next());
                                    if (userInput == 3) {
                                        break;
                                    }
                                    if (userInput < 1 || userInput > 3) {
                                        throw new Exception(BOLD_RED + "Invalid option selected. Please enter a number between 1 to 3." + RESET);
                                    }
                                } catch (InputMismatchException e) {
                                    System.err.println(BOLD_RED + "Please only key in an integer" + RESET);
                                    scanner.nextLine();
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    System.err.println(e.getMessage());
                                    Thread.sleep(1000);
                                }


                            switch (userInput) {
                                case 1:
                                    String username;
                                    String password;
                                    String firstName;
                                    String lastName;
                                    String icOrPassportNumber;
                                    String phoneNumber;
                                    String address;

                                    scanner.nextLine();
                                    username = Validation.getValidInput("Enter your username: ", Validation.USERNAME_PATTERN, Validation.USERNAME_ERROR);

                                    if(username==null) break;

                                    boolean isDuplicate = stub.checkDuplicate(username);

                                    while (isDuplicate) {
                                        System.out.println(BOLD_RED + "The username you have chosen is already taken." + RESET);
                                        username = Validation.getValidInput("Enter your username: ", Validation.USERNAME_PATTERN, Validation.USERNAME_ERROR);
                                        if(username==null) break;
                                        isDuplicate = stub.checkDuplicate(username);
                                    }

                                    if (!isDuplicate) {
                                        password = Validation.getValidInput("Enter your password: ", Validation.PASSWORD_PATTERN, Validation.PASSWORD_ERROR);
                                        if(password==null)break;
                                        firstName = Validation.getValidInput("Enter your first name: ", Validation.NAME_PATTERN, Validation.NAME_ERROR);
                                        if(firstName==null) break;
                                        lastName = Validation.getValidInput("Enter your last name: ", Validation.NAME_PATTERN, Validation.NAME_ERROR);
                                        if(lastName==null) break;;
                                        icOrPassportNumber = Validation.getValidInput("Please enter your Identity Card (IC) or Passport Number: ", Validation.IC_PATTERN, Validation.IC_ERROR);
                                        if(icOrPassportNumber==null) break;;
                                        phoneNumber = Validation.getValidInput("Enter your phone number(01x-xx...): ",Validation.PHONE_PATTERN, Validation.PHONE_ERROR);
                                        if(phoneNumber==null) break;
                                        address = Validation.getValidInput("Enter your address: ", Validation.ADDRESS_PATTERN, Validation.ADDRESS_ERROR);
                                        if(address==null) break;

                                        stub.register(username, password, firstName, lastName, icOrPassportNumber, phoneNumber, address);
                                    }
                                    System.out.println("Register Successful. Please login to your account.");
                                    break;

                                case 2:
                                    String inputUsername;
                                    String inputPassword;
                                    int count = 3;
                                    scanner.nextLine();
                                    System.out.print("Enter your username: ");
                                    inputUsername = scanner.nextLine();
                                    if(inputUsername.equals("-1"))break;
                                    System.out.print("Enter your password: ");
                                    inputPassword = scanner.nextLine();
                                    if(inputPassword.equals("-1"))break;

                                    boolean isLoginSuccess = stub.login(inputUsername, inputPassword);

                                    while (!isLoginSuccess) {
                                        if (count < 1) {
                                            System.out.println("You have exceeded the maximum of 3 attempts. Press any key to continue.");
                                            break;
                                        } else {
                                            System.out.println(BOLD_RED + "Login failed. Please check your username and password.You only have " + count + " attempts left." + RESET);
                                            System.out.print("Enter your username: ");
                                            inputUsername = scanner.nextLine();
                                            if(inputUsername.equals("-1"))break;
                                            System.out.print("Enter your password: ");
                                            inputPassword = scanner.nextLine();
                                            if(inputPassword.equals("-1"))break;
                                            isLoginSuccess = stub.login(inputUsername, inputPassword);
                                            count--;
                                        }
                                    }

                                    if (isLoginSuccess) {
                                        loginCustomer = stub.getCurrentLoginCustomer();
                                        loggedInUsername = loginCustomer.getUsername();

                                        System.out.println("Login successful! ");
                                        while (true) {
                                            System.out.println("\n╔════════════════════════════════════════════════════════╗\n" +
                                                    "║          Welcome to McGee Food Ordering System         ║\n" +
                                                    "╚════════════════════════════════════════════════════════╝");
                                            System.out.println("1. Food Menu");
                                            System.out.println("2. Drink Menu");
                                            System.out.println("3. Shopping Cart");
                                            System.out.println("4. Food Order History");
                                            System.out.println("5. Check Balance");
                                            System.out.println("6. Add Balance");
                                            System.out.println("7. My profile");
                                            System.out.println("8. Exit");
                                            System.out.println("Enter number of action to perform:");

                                            int actionNum = 0;
                                            try {
                                                actionNum = Integer.parseInt(scanner.nextLine());
                                                if (actionNum == 8) {
                                                    break;
                                                }

                                                if (actionNum < 1 || actionNum > 8) {
                                                    System.out.println(BOLD_RED + "Please enter number between 1 to 8 to choose." + RESET);
                                                }
                                            } catch (NumberFormatException e) {
                                                System.out.println(BOLD_RED + "Invalid input. Please enter a number." + RESET);
                                            }

                                            switch (actionNum) {
                                                case 1:
                                                    double totalPrice = 0;
                                                    List<Cart> selectedItems = new ArrayList<>();
                                                    double price = 0;

                                                    while (true) {
                                                        Map<String, ?> foodItemsMap = stub.viewFoodData("FoodItems");
                                                        Map<String, ?> foodCategoriesMap = stub.viewFoodData("FoodCategory");

                                                        if (foodItemsMap.isEmpty()) {
                                                            System.out.println(BOLD_RED + "No food items found." + RESET);
                                                            System.out.println("*".repeat(40));
                                                            break;
                                                        } else {
                                                            System.out.println("\n-----------------------------------\n" +
                                                                    "            Food Menu        \n" +
                                                                    "-----------------------------------");
                                                            for (Map.Entry<String, ?> entry : foodItemsMap.entrySet()) {
                                                                FoodItems foodItem = (FoodItems) entry.getValue();
                                                                FoodCategory foodCategory = (FoodCategory) foodCategoriesMap.get(foodItem.getFoodCategory());
                                                                System.out.println(foodItem.getFoodID() + ": " + foodItem.getFoodName() + " (" + foodItem.getFoodPrice() + ") - " + foodItem.getFoodCategory());
                                                            }
                                                        }

                                                        System.out.println("Enter food ID to add to cart or enter -1 to exit and add all selected items into shopping cart:");
                                                        String foodID = scanner.nextLine();

                                                        if (foodID.equals("-1")) {
                                                            break;
                                                        }

                                                        FoodItems selectedItem = (FoodItems) foodItemsMap.get(foodID);
                                                        if (selectedItem == null) {
                                                            System.out.println(BOLD_RED + "Invalid food ID. Please try again." + RESET);
                                                            continue;
                                                        }

                                                        System.out.println("Enter quantity:");
                                                        int foodQuantity;

                                                        try {
                                                            foodQuantity = Integer.parseInt(scanner.nextLine());
                                                            if(foodQuantity==-1)break;
                                                            if (foodQuantity < 0) {
                                                                System.out.println(BOLD_RED + "Please enter correct quantity." + RESET);
                                                                break;
                                                            }
                                                            if (foodQuantity == -1) break;
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(BOLD_RED + "Invalid quantity. Please enter a valid number." + RESET);
                                                            continue;
                                                        }

                                                        price = selectedItem.getFoodPrice() * foodQuantity;
                                                        totalPrice += price;
                                                        selectedItems.add(new Cart(null, loggedInUsername, selectedItem, foodQuantity, price));
                                                        System.out.println("Items selected successfully.");
                                                    }
                                                    if (!selectedItems.isEmpty()) {
                                                        System.out.println("*".repeat(40));
                                                        System.out.println("Selected items:");
                                                        for (Cart item : selectedItems) {
                                                            double itemTotalPrice = item.getFoodItem().getFoodPrice() * item.getQuantity();
                                                            System.out.println(item.getFoodItem().getFoodName() + " - Quantity: " + item.getQuantity() + ", Total price: " + itemTotalPrice);
                                                        }
                                                        System.out.println("Total price for all selected items: " + totalPrice);

                                                        System.out.println("Do you want to add these selected items into your cart? Enter y or n");
                                                        String answer = scanner.nextLine();
                                                        if (answer.equalsIgnoreCase("y")) {
                                                            for (Cart item : selectedItems) {
                                                                stub.createCart(loggedInUsername, item.getFoodItem(), item.getQuantity(), item.getPrice());
                                                            }
                                                            System.out.println("Items added to cart successfully.");
                                                        } else {
                                                            System.out.println(BOLD_RED + "Items not added to cart." + RESET);
                                                        }
                                                        System.out.println("*".repeat(40));
                                                    }
                                                    break;
                                                case 2:
                                                    double drinkTotalPrice = 0;
                                                    List<Cart> drinkSelectedItem = new ArrayList<>();
                                                    double drinkprice = 0;
                                                    List<Cart> drinkSelectedItems = new ArrayList<>();

                                                    while (true) {
                                                        Map<String, ?> foodItemsMap = stub.viewFoodData("DrinkItems");

                                                        if (foodItemsMap.isEmpty()) {
                                                            System.out.println(BOLD_RED + "No food items found." + RESET);
                                                            System.out.println("*".repeat(40));
                                                            break;
                                                        } else {
                                                            System.out.println("\n-----------------------------------\n" +
                                                                    "            Drink Menu        \n" +
                                                                    "-----------------------------------");
                                                            for (Map.Entry<String, ?> entry : foodItemsMap.entrySet()) {
                                                                DrinkItems foodItem = (DrinkItems) entry.getValue();
                                                                System.out.println(foodItem.getFoodID() + ": " + foodItem.getFoodName() + " (" + foodItem.getFoodPrice() + ") - " );
                                                            }
                                                        }

                                                        System.out.println("Enter drink ID to add to cart or enter -1 to exit and add all selected items into shopping cart:");
                                                        String foodID = scanner.nextLine();

                                                        if (foodID.equals("-1")) {
                                                            break;
                                                        }

                                                        DrinkItems selectedItem = (DrinkItems) foodItemsMap.get(foodID);
                                                        if (selectedItem == null) {
                                                            System.out.println(BOLD_RED + "Invalid drink ID. Please try again." + RESET);
                                                            continue;
                                                        }

                                                        System.out.println("Enter quantity:");
                                                        int foodQuantity;

                                                        try {
                                                            foodQuantity = Integer.parseInt(scanner.nextLine());
                                                            if(foodQuantity==-1)break;
                                                            if (foodQuantity < 0) {
                                                                System.out.println(BOLD_RED + "Please enter correct quantity." + RESET);
                                                                break;
                                                            }
                                                            if (foodQuantity == -1) break;
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(BOLD_RED + "Invalid quantity. Please enter a valid number." + RESET);
                                                            continue;
                                                        }

                                                        price = selectedItem.getFoodPrice() * foodQuantity;
                                                        drinkTotalPrice += price;
                                                        drinkSelectedItems.add(new Cart(null, loggedInUsername, selectedItem, foodQuantity, price));
                                                        System.out.println("Items selected successfully.");
                                                    }
                                                    break;
                                                case 3:
                                                    while (true) {
                                                        System.out.println("\n-----------------------------------\n" +
                                                                "           Shopping Cart        \n" +
                                                                "-----------------------------------");
                                                        System.out.print("1. Check Out \n2. Remove Items from Shopping Cart \n3. Exit \n");
                                                        System.out.println("Enter number of action to perform:");
                                                        int choice = 0;
                                                        try {
                                                            choice = Integer.parseInt(scanner.nextLine());
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(BOLD_RED + "Please enter number to choose" + RESET);
                                                        }
                                                        if (choice == 3) break;
                                                        switch (choice) {
                                                            case 1:
                                                                while (true) {
                                                                    Map<String, ?> cartItems = stub.viewFoodData("ShoppingCart");
                                                                    if (cartItems.isEmpty()) {
                                                                        System.out.println(BOLD_RED + "No items found in the shopping cart." + RESET);
                                                                        break;
                                                                    } else {
                                                                        System.out.println("*".repeat(40));
                                                                        System.out.println("Shopping Cart:");
                                                                        int i = 0;
                                                                        List<String> cartIDs = new ArrayList<>();
                                                                        double totalOrderPrice = 0;
                                                                        for (Map.Entry<String, ?> entry : cartItems.entrySet()) {
                                                                            Cart cartItem = (Cart) entry.getValue();
                                                                            if (cartItem.getCustomerName().equals(loggedInUsername)) {
                                                                                i++;
                                                                                cartIDs.add(entry.getKey());
                                                                                totalOrderPrice += cartItem.getPrice();
                                                                                System.out.println(cartItem.getFoodID() + ": " + cartItem.getFoodItem() + " X " + cartItem.getQuantity() + " Price:" + cartItem.getPrice());
                                                                            }
                                                                        }

                                                                        if (totalOrderPrice == 0) {
                                                                            System.out.println(BOLD_RED + "No items found in the shopping cart." + RESET);
                                                                            System.out.println("*".repeat(40));
                                                                            break;
                                                                        }

                                                                        System.out.println("Total price for the order: " + totalOrderPrice);

                                                                        System.out.println("Enter cart number to choose or enter -1 to exit:");
                                                                        int cartNumber = 0;

                                                                        try {
                                                                            cartNumber = Integer.parseInt(scanner.nextLine());
                                                                            if (cartNumber == -1) {
                                                                                break;
                                                                            }
                                                                        } catch (NumberFormatException e) {
                                                                            System.out.println(BOLD_RED + "Please enter a valid number to choose." + RESET);
                                                                            continue;
                                                                        }

                                                                        if (cartItems.containsKey(String.valueOf(cartNumber))) {
                                                                            Cart selectedCartItem = (Cart) cartItems.get(String.valueOf(cartNumber));
                                                                            if (selectedCartItem != null) {
                                                                                System.out.println("Please select your order type. Enter 1 for dine in, 2 for pickup, or -1 to exit");
                                                                                int orderType = 0;
                                                                                try {
                                                                                    orderType = Integer.parseInt(scanner.nextLine());
                                                                                } catch (NumberFormatException e) {
                                                                                    System.out.println(BOLD_RED + "Please enter number to choose" + RESET);
                                                                                }
                                                                                if (orderType == -1) break;

                                                                                String orderStatus = null;
                                                                                if (orderType == 1) {
                                                                                    orderStatus = "Dine In";
                                                                                } else if (orderType == 2) {
                                                                                    orderStatus = "Pick Up";
                                                                                } else {
                                                                                    System.out.println(BOLD_RED + "Please select 1 or 2 to order" + RESET);
                                                                                    break;
                                                                                }

                                                                                double orderPrice = selectedCartItem.getPrice();
                                                                                double currentBalance = stub.getBalance(loggedInUsername);

                                                                                if (currentBalance < orderPrice) {
                                                                                    System.out.println(BOLD_RED + "Insufficient balance to place the order." + RESET);
                                                                                    break;
                                                                                }

                                                                                System.out.println("Selected Cart Item:");
                                                                                System.out.println("Food Item: " + selectedCartItem.getFoodItem() + " X " + selectedCartItem.getQuantity() + " Price: " + selectedCartItem.getPrice());

                                                                                System.out.println("Do you want to place the order for this food item? Enter y or n");
                                                                                String answer1 = scanner.nextLine();
                                                                                if (answer1.equalsIgnoreCase("y")) {
                                                                                    try {
                                                                                        stub.createOrder(loggedInUsername, selectedCartItem.getFoodItem().getFoodName(), selectedCartItem.getQuantity(), selectedCartItem.getPrice(), orderStatus, "Pending");
                                                                                        stub.setBalance(loggedInUsername, currentBalance - orderPrice);
                                                                                        cartItems.remove(String.valueOf(cartNumber));
                                                                                        stub.updateCartData(cartItems);
                                                                                        System.out.println("Order placed successfully!");
                                                                                        System.out.println("Updated Balance: " + (currentBalance - orderPrice));
                                                                                        break;
                                                                                    } catch (RemoteException e) {
                                                                                        System.err.println(BOLD_RED + "Error creating order: " + e.getMessage() + RESET);
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                System.out.println(BOLD_RED +"Invalid cart ID selected." + RESET);
                                                                                break;
                                                                            }
                                                                        } else {
                                                                            System.out.println(BOLD_RED +"Invalid cart number selected." + RESET);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                break;
                                                            case 2:
                                                                while (true) {
                                                                    try {
                                                                        Map<String, ?> cartItems1 = stub.viewFoodData("ShoppingCart");
                                                                        if (cartItems1.isEmpty()) {
                                                                            System.out.println(BOLD_RED + "No items found in the shopping cart." + RESET);
                                                                            break;
                                                                        } else {
                                                                            System.out.println("Shopping Cart:");
                                                                            for (Map.Entry<String, ?> entry : cartItems1.entrySet()) {
                                                                                Cart cartItem = (Cart) entry.getValue();
                                                                                if (cartItem.getCustomerName().equals(loggedInUsername)) {
                                                                                    System.out.println(entry.getKey() + ": " + cartItem.getFoodItem() + " X " + cartItem.getQuantity() + " Price: " + cartItem.getPrice());
                                                                                }
                                                                            }
                                                                        }

                                                                        System.out.println("Enter cart ID to delete or enter -1 to exit:");
                                                                        String cartID = scanner.nextLine().trim();
                                                                        if (cartID.equals("-1")) break;
                                                                        if (cartItems1.containsKey(cartID)) {
                                                                            stub.delete(cartID, "cart");
                                                                            System.out.println("Items deleted successfully");
                                                                            stub.viewFoodData("ShoppingCart");
                                                                            break;
                                                                        } else {
                                                                            System.out.println(BOLD_RED +"Cart ID not found"+ RESET);
                                                                        }

                                                                    } catch (Exception e) {
                                                                        System.out.println(BOLD_RED + "Invalid input. Please try again." + RESET);
                                                                        break;
                                                                    }
                                                                }
                                                                break;
                                                            default:
                                                                System.out.println(BOLD_RED + "Invalid choice" + RESET);
                                                        }
                                                    }
                                                    break;
                                                case 4:
                                                    System.out.println("\n-------------------------------------\n" +
                                                            "          Food Order History        \n" +
                                                            "--------------------------------------");
                                                    Map<String, ?> categories1 = stub.viewFoodData("FoodOrder");
                                                    boolean hasOrder = false;
                                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                        String value = entry.getValue().toString();
                                                        if (value.contains("Customer: " + loggedInUsername)) {
                                                            System.out.println("Order ID: " + entry.getKey() + ", " + value);
                                                            hasOrder = true;
                                                        }
                                                    }
                                                    if (!hasOrder) {
                                                        System.out.println(BOLD_RED + "No food order found for " + loggedInUsername + "." + RESET);
                                                    }
                                                    System.out.println("*".repeat(40));
                                                    break;
                                                case 5:
                                                    double balance = stub.getBalance(loggedInUsername);
                                                    System.out.println("\n--------------------------------------\n" +
                                                            "            Check Balance        \n" +
                                                            "--------------------------------------");
                                                    System.out.println("Your current balance is: " + balance);
                                                    break;
                                                case 6:
                                                    balance = stub.getBalance(loggedInUsername);
                                                    System.out.println("\n--------------------------------------\n" +
                                                            "             Add Balance        \n" +
                                                            "--------------------------------------");
                                                    System.out.println("Your current balance is: " + balance);
                                                    System.out.println("Enter amount to add or -1 to exit:");
                                                    double amount;
                                                    try {
                                                        amount = Double.parseDouble(scanner.nextLine());
                                                    } catch (NumberFormatException e) {
                                                        System.out.println(BOLD_RED + "Invalid amount. Please enter a valid number." + RESET);
                                                        break;
                                                    }

                                                    double currentBalance = stub.getBalance(loggedInUsername);
                                                    stub.setBalance(loggedInUsername, currentBalance + amount);
                                                    System.out.println("Balance updated successfully! New balance: " + (currentBalance + amount));
                                                case 7:
                                                    boolean continueProfileUpdate = true;
                                                    while (true) {
                                                        System.out.println("\n--------------------------------------\n" +
                                                                "             Profile        \n" +
                                                                "--------------------------------------");
                                                        System.out.println();
                                                        loginCustomer = stub.getCurrentLoginCustomer();
                                                        System.out.println("Username: " + loginCustomer.getUsername());
                                                        System.out.println("Password: " + loginCustomer.getPassword());
                                                        System.out.println("First Name: " + loginCustomer.getFirstName());
                                                        System.out.println("Last Name: " + loginCustomer.getLastName());
                                                        System.out.println("icOrPassportNumber: " + loginCustomer.getIcOrPassportNumber());
                                                        System.out.println("Phone Number: " + loginCustomer.getPhoneNumber());
                                                        System.out.println("Address: " + loginCustomer.getAddress());

                                                        System.out.println();
                                                        System.out.println("1. Edit Username");
                                                        System.out.println("2. Edit Password");
                                                        System.out.println("3. Edit First Name");
                                                        System.out.println("4. Edit Last Name");
                                                        System.out.println("5. Edit icOrPassportNumber");
                                                        System.out.println("6. Edit Phone Number");
                                                        System.out.println("7. Edit Address");
                                                        System.out.println("8. Back to previous");

                                                        boolean editProfileOptionError = true;
                                                        int editProfileOption = 0;
                                                            try {
                                                                System.out.println("Enter your option: ");
                                                                editProfileOption = Integer.parseInt(scanner.nextLine());
                                                                if (editProfileOption == 8) break;
                                                                if (editProfileOption < 1 || editProfileOption > 8) {
                                                                    throw new Exception(BOLD_RED + "Invalid option selected. Please enter a number between 1 to 8." + RESET);
                                                                }
                                                                editProfileOptionError = false;
                                                            } catch (InputMismatchException e) {
                                                                System.out.println(BOLD_RED + "Please only key in integer"+RESET);
                                                            } catch (Exception e) {
                                                                System.out.println(BOLD_RED+"Invalid input. Please try again."+RESET);
                                                                continue;
                                                            }


                                                            switch (editProfileOption) {
                                                                case 1:
                                                                    username = Validation.getValidInput("Enter your username: ", Validation.USERNAME_PATTERN, Validation.USERNAME_ERROR);
                                                                    if (username==null) break;
                                                                    isDuplicate = stub.checkDuplicate(username);
                                                                    while (isDuplicate) {
                                                                        System.out.println(BOLD_RED + "The username you have chosen is already taken." + RESET);
                                                                        username = Validation.getValidInput("Enter your username: ", Validation.USERNAME_PATTERN, Validation.USERNAME_ERROR);
                                                                        isDuplicate = stub.checkDuplicate(username);
                                                                    }
                                                                    loginCustomer.setUsername(username);
                                                                    System.out.println("Username updated successfully");
                                                                    continue;
                                                                case 2:
                                                                    password = Validation.getValidInput("Enter your password: ", Validation.PASSWORD_PATTERN, Validation.PASSWORD_ERROR);
                                                                    if (password==null) break;
                                                                    loginCustomer.setPassword(password);
                                                                    System.out.println("Password updated successfully");
                                                                    continue;
                                                                case 3:
                                                                    firstName = Validation.getValidInput("Enter your first name: ", Validation.NAME_PATTERN, Validation.NAME_ERROR);
                                                                    if (firstName.equals("-1")) break;
                                                                    loginCustomer.setFirstName(firstName);
                                                                    System.out.println("First name updated successfully");
                                                                    continue;
                                                                case 4:
                                                                    lastName = Validation.getValidInput("Enter your last name: ", Validation.NAME_PATTERN, Validation.NAME_ERROR);
                                                                    if (lastName.equals("-1")) break;
                                                                    loginCustomer.setLastName(lastName);
                                                                    System.out.println("Last name updated successfully");
                                                                    continue;
                                                                case 5:
                                                                    icOrPassportNumber = Validation.getValidInput("Please enter your Identity Card (IC) : ", Validation.IC_PATTERN, Validation.IC_ERROR);
                                                                    if (icOrPassportNumber.equals("-1")) break;
                                                                    loginCustomer.setIcOrPassportNumber(icOrPassportNumber);
                                                                    System.out.println("IC updated successfully");
                                                                    continue;
                                                                case 6:
                                                                    phoneNumber = Validation.getValidInput("Enter your phone number(01x-xx...): ", Validation.PHONE_PATTERN, Validation.PHONE_ERROR);
                                                                    if (phoneNumber.equals("-1")) break;
                                                                    loginCustomer.setPhoneNumber(phoneNumber);
                                                                    System.out.println("Phone number updated successfully");
                                                                    continue;
                                                                case 7:
                                                                    address = Validation.getValidInput("Enter your address: ", Validation.ADDRESS_PATTERN, Validation.ADDRESS_ERROR);
                                                                    if (address.equals("-1")) break;
                                                                    loginCustomer.setAddress(address);
                                                                    System.out.println("Address updated successfully");
                                                                    continue;
                                                                case 8:
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                            if (editProfileOption != 8) {
                                                                stub.updateProfile(loginCustomer.getCustomerId(), loginCustomer);
                                                                scanner.nextLine();
                                                            }

                                                    }
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                case 3:
                                    break;
                            }

                        }
                } else if(role==3) {
                    System.exit(0);
                }else {
                    System.out.println("Please enter 1 or 2 to select your role.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
