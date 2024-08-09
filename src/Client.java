import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // Load keystore for the client
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream("C:\\Users\\yeefei\\IdeaProjects\\DCOM_Assignment\\rmi-client.p12"), "password".toCharArray());

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
            Registry registry = LocateRegistry.getRegistry("localhost", 1099, customClientSocketFactory);

            // Lookup the remote object
            FOSInterface stub = (FOSInterface) registry.lookup("FOSInterface");

            while (true) {
                System.out.println("Welcome to McGee restaurant \nSelect your role:\n1. Admin\n2. Customer \n3. Exit");
                Scanner scanner = new Scanner(System.in);
                int role;
                try {
                    role = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter 1 or 2 to select your role.");
                    continue;
                }

                if (role == 1) {
                    while (true) {
                        System.out.println("Enter admin password or enter -1 to exit:");
                        String password = scanner.nextLine();
                        if (password.equals("-1")) break;
                        if (password.equals("1234")) {
                            while (true) {
                                System.out.println("Admin Edit Food Menu \n1. Food Category \n2. Food Items \n3. View food order \n4. Exit");
                                int answer;
                                try {
                                    answer = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a number.");
                                    continue;
                                }
                                if (answer == 1) {
                                    while (true) {
                                        System.out.println("Admin Edit Food Category:\n1. View Food Category\n2. Create Food Categories\n3. Update Food Categories \n4. Delete Food Categories \n5. Exit");
                                        int choice;
                                        try {
                                            choice = Integer.parseInt(scanner.nextLine());
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input. Please enter a number.");
                                            continue;
                                        }
                                        switch (choice) {
                                            case 1:
                                                Map<String, ?> categories = stub.viewFoodData("FoodCategory");
                                                if (categories.isEmpty()) {
                                                    System.out.println("No food categories found.");
                                                    System.out.println("*".repeat(40));
                                                    break;
                                                } else {
                                                    System.out.println("Food Categories:");
                                                    for (Map.Entry<String, ?> entry : categories.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue());
                                                    }
                                                }
                                                break;
                                            case 2:
                                                System.out.println("Create Food Category for McGee\nIf you want to exit, please enter -1");
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
                                                        System.out.println("No food categories found.");
                                                        System.out.println("*".repeat(40));
                                                        break;
                                                    } else {
                                                        System.out.println("Food Categories:");
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
                                                            System.out.println("Invalid input. Please enter a number.");
                                                            continue;
                                                        }
                                                        if (answer1 == 1) {
                                                            System.out.println("Enter new food category ID:");
                                                            String newCategoryID = scanner.nextLine();
                                                            stub.updateFoodCategoryID(categoryID1, newCategoryID);
                                                            System.out.println("Food category ID updated successfully.");
                                                        } else if (answer1 == 2) {
                                                            System.out.println("Enter new food category Name:");
                                                            String newCategoryName = scanner.nextLine();
                                                            stub.updateFoodCategoryName(categoryID1, newCategoryName);
                                                            System.out.println("Food category name updated successfully.");
                                                        } else if (answer1 == 3) {
                                                            break;
                                                        } else {
                                                            System.out.println("Please choose from the menu number.");
                                                        }
                                                    } else {
                                                        System.out.println("Food Category ID not found. Please try again or enter -1 to exit.");
                                                    }
                                                }
                                                break;
                                            case 4:
                                                while (true) {
                                                    System.out.println("Admin delete food category");
                                                    Map<String, ?> categories1 = stub.viewFoodData("FoodCategory");
                                                    if (categories1.isEmpty()) {
                                                        System.out.println("No food categories found.");
                                                    } else {
                                                        System.out.println("Food Categories:");
                                                        for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                            System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                        }
                                                    }
                                                    System.out.println("Enter food category ID to delete or enter -1 to exit:");
                                                    String categoryIDToDelete = scanner.nextLine();
                                                    if (categoryIDToDelete.equals("-1")) break;
                                                    if (!categoryIDToDelete.isEmpty() && categories1.containsKey(categoryIDToDelete)) {
                                                        stub.delete(categoryIDToDelete, "category");
                                                    }else{
                                                        System.out.println("Food Category ID not found. Please try again.");
                                                    }
                                                }
                                                break;
                                            case 5:
                                                break;
                                            default:
                                                System.out.println("Invalid choice.");
                                                break;
                                        }
                                        if (choice == 5) break;
                                    }
                                } else if (answer == 2) {
                                    System.out.println("Admin Edit Food Items");
                                    System.out.println("Admin Edit Food Items:\n1. View Food Items\n2. Create Food Items\n3. Update Food Items \n4. Delete Food Items \n5. Exit");
                                    int choice;
                                    try {
                                        choice = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a number.");
                                        continue;
                                    }

                                    switch (choice){
                                        case 1:
                                            Map<String, ?> categories = stub.viewFoodData("FoodItems");
                                            if (categories.isEmpty()) {
                                                System.out.println("No food categories found.");
                                            } else {
                                                System.out.println("Food Items:");
                                                for (Map.Entry<String, ?> entry : categories.entrySet()) {
                                                    System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                }
                                            }
                                            break;
                                        case 2:
                                            while (true) {
                                                System.out.println("Create Food Category for McGee\nIf you want to exit, please enter -1");
                                                System.out.println("Enter food item name to add:");
                                                String foodName = scanner.nextLine().trim();
                                                if (foodName.equals("-1")) break;
                                                System.out.println("Enter food price:");
                                                double foodPrice = Double.parseDouble(scanner.nextLine());
                                                Map<String, ?> categories1 = stub.viewFoodData("FoodCategory");
                                                if (categories1.isEmpty()) {
                                                    System.out.println("No food categories found.");
                                                } else {
                                                    System.out.println("Food Categories:");
                                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                }
                                                System.out.println("Enter food category ID for the new food item:");
                                                String foodCategoryID = scanner.nextLine().trim();
                                                if (foodPrice == -1) break;
                                                if(categories1.containsKey(foodCategoryID)) {
                                                    if (!foodCategoryID.isEmpty() && !foodName.isEmpty() && foodPrice > 0) {
                                                        stub.createFoodItems(foodName, foodPrice, foodCategoryID);
                                                    }
                                                }else{
                                                    System.out.println("Please select food category ID from the list.");
                                                }
                                            }
                                            break;
                                        case 3:

                                            while (true) {

                                                Map<String,?> categories1 = stub.viewFoodData("FoodItems");

                                                if (categories1.isEmpty()) {

                                                    System.out.println("No food items found.");

                                                } else {

                                                    System.out.println("Food Items:");

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

                                                        System.out.println("Invalid input. Please enter a number.");

                                                        continue;

                                                    }

                                                    if (answer2 == 1) {
                                                        System.out.println("Enter new food item name:");
                                                        String newFoodName = scanner.nextLine().trim();
                                                        try {
                                                            stub.updateFoodItemName(foodID, newFoodName);
                                                            System.out.println("Food item name updated successfully.");
                                                        } catch (RemoteException e) {
                                                            System.out.println("Error updating food item name: " + e.getMessage());

                                                        }
                                                    } else if (answer2 == 2) {
                                                        System.out.println("Enter new food item price:");
                                                        double newFoodPrice;
                                                        try {
                                                            newFoodPrice = Double.parseDouble(scanner.nextLine());
                                                        } catch (NumberFormatException e) {
                                                            System.out.println("Invalid input. Please enter a valid price.");
                                                            continue;
                                                        }
                                                        try {
                                                            stub.updateFoodItemPrice(foodID, newFoodPrice);
                                                            System.out.println("Food item price updated successfully.");
                                                        } catch (RemoteException e) {
                                                            System.out.println("Error updating food item price: " + e.getMessage());
                                                        }
                                                    } else if (answer2 == 3) {
                                                        break;
                                                    } else {
                                                        System.out.println("Please choose from the menu number.");
                                                    }
                                                } else {
                                                    System.out.println("Food item ID not found. Please try again.");
                                                }
                                            }
                                            break;

                                        case 4:
                                            while (true) {
                                                Map<String, ?> categories1 = stub.viewFoodData("FoodItems");
                                                if (categories1.isEmpty()) {
                                                    System.out.println("No food item found.");
                                                } else {
                                                    System.out.println("Food Items:");
                                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                    }
                                                }
                                                System.out.println("Admin delete food items \nEnter food item ID to delete or enter -1 to exit:");
                                                String foodID = scanner.nextLine().trim();
                                                if (foodID.equals("-1")) break;
                                                if (foodID.isEmpty()) {
                                                    System.out.println("Invalid input. Please enter food item ID from the list.");
                                                    continue;
                                                }
                                                if (categories1.containsKey(foodID)) {
                                                    stub.delete(foodID, "item");
                                                } else {
                                                    System.out.println("Food item ID not found.");
                                                }
                                            }
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Invalid choice.");
                                            break;
                                    }
                                } else if (answer ==3){
                                    while (true) {
                                        System.out.println("McGee Restaurant Food Orders");
                                        Map<String, ?> categories1 = stub.viewFoodData("FoodOrder");
                                        if (categories1.isEmpty()) {
                                            System.out.println("No food order found.");
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
                                                System.out.println("No incoming food orders.");
                                                break;
                                            }
                                            System.out.println("Enter food order ID that you have completed or enter -1 to exit:");
                                            String foodOrderID = scanner.nextLine();
                                            if (foodOrderID.equals("-1")) break;
                                            if (!foodOrderID.isEmpty() && categories1.containsKey(foodOrderID)) {
                                                stub.updateOrderStatus(foodOrderID, "Completed");
                                                System.out.println("Order status updated to Completed.");
                                            } else {
                                                System.out.println("Please enter a valid food order ID.");
                                            }
                                        }
                                    }
                                }else if (answer==4){
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect password. Please try again.");
                        }
                    }
                } else if (role == 2) {
                    while (true) {
                        System.out.println("Welcome to McGee Food Ordering System");
                        System.out.println("1. Food Menu");
                        System.out.println("2. Shopping Cart");
                        System.out.println("3. Food Order");
                        System.out.println("4. Check Balance");
                        System.out.println("5. Add Balance");
                        System.out.println("6. Exit");
                        System.out.println("Enter number of action to perform:");

                        int actionNum = 0;
                        try {
                            actionNum = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a number to choose an action.");
                            continue;
                        }

                        if (actionNum==6) {
                            break;
                        } else if (actionNum < 1 || actionNum > 6) {
                            System.out.println("Invalid action number. Please choose from 1 to 6.");
                            continue;
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
                                        System.out.println("No food items found.");
                                        System.out.println("*".repeat(40));
                                        break;
                                    } else {
                                        System.out.println("Food Menu:");
                                        for (Map.Entry<String, ?> entry : foodItemsMap.entrySet()) {
                                            FoodItems foodItem = (FoodItems) entry.getValue();
                                            FoodCategory foodCategory = (FoodCategory) foodCategoriesMap.get(foodItem.getFoodCategory());
                                            System.out.println(foodItem.getFoodID() + ": " + foodItem.getFoodName() + " (" + foodItem.getFoodPrice() + ") - " + foodItem.getFoodCategory());
                                        }
                                    }

                                    System.out.println("Enter food ID to add to cart or enter -1 to exit:");
                                    String foodID = scanner.nextLine();

                                    if (foodID.equals("-1")) {
                                        break;
                                    }

                                    FoodItems selectedItem = (FoodItems) foodItemsMap.get(foodID);
                                    if (selectedItem == null) {
                                        System.out.println("Invalid food ID. Please try again.");
                                        continue;
                                    }

                                    System.out.println("Enter quantity:");
                                    int foodQuantity;
                                    try {
                                        foodQuantity = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid quantity. Please enter a valid number.");
                                        continue;
                                    }

                                    price = selectedItem.getFoodPrice() * foodQuantity;
                                    totalPrice += price;
                                    selectedItems.add(new Cart(null,"John Doe", selectedItem, foodQuantity, price));
                                }
                                if(selectedItems.size()>0) {
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
                                            stub.createCart("John Doe", item.getFoodItem(), item.getQuantity(), item.getPrice());
                                        }
                                        System.out.println("Items added to cart successfully.");
                                    } else {
                                        System.out.println("Items not added to cart.");
                                    }
                                    System.out.println("*".repeat(40));
                                }
                                System.out.println("*".repeat(40));
                                break;
                            case 2:
                                System.out.println("This is items in your shopping cart.");
                                Map<String, ?> cartItems = stub.viewFoodData("ShoppingCart");
                                if (cartItems.isEmpty()) {
                                    System.out.println("No items found in the shopping cart.");
                                    System.out.println("*".repeat(40));
                                    break;
                                } else {
                                    System.out.println("Shopping Cart:");
                                    int i=0;
                                    for (Map.Entry<String, ?> entry : cartItems.entrySet()) {
                                        Cart cartItem = (Cart) entry.getValue();
                                        if (cartItem.getCustomerName().equals("John Doe")) {
                                            i++;
                                            System.out.println(i+ ": "  + cartItem.getFoodItem()+ " X "+ cartItem.getQuantity() + " Price:" +cartItem.getPrice());
                                        }
                                    }
                                }
                                while (true) {
                                    System.out.println("Food Ordering");
                                    System.out.print("1. Order \n2. Remove items from shopping cart \n3.Exit \n");
                                    int choice = 0;
                                    try {
                                        choice = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter number to choose");
                                    }
                                    if(choice==3) break;
                                    switch (choice){
                                        case 1:
                                            while (true) {
                                                if (cartItems.isEmpty()) {
                                                    System.out.println("No items found in the shopping cart.");
                                                    System.out.println("*".repeat(40));
                                                    break;
                                                } else {
                                                    System.out.println("Shopping Cart:");
                                                    int i = 0;
                                                    List<String> cartIDs = new ArrayList<>();
                                                    double totalOrderPrice = 0;
                                                    for (Map.Entry<String, ?> entry : cartItems.entrySet()) {
                                                        Cart cartItem = (Cart) entry.getValue();
                                                        if (cartItem.getCustomerName().equals("John Doe")) {
                                                            i++;
                                                            cartIDs.add(entry.getKey());
                                                            totalOrderPrice += cartItem.getPrice();
                                                            System.out.println(i + ": " + cartItem.getFoodItem() + " X " + cartItem.getQuantity() + " Price:" + cartItem.getPrice());
                                                        }
                                                    }

                                                    if (totalOrderPrice == 0) {
                                                        System.out.println("No items found in the shopping cart.");
                                                        System.out.println("*".repeat(40));
                                                        return;
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
                                                        System.out.println("Please enter a valid number to choose.");
                                                        continue;
                                                    }

                                                    System.out.println("Please select your order type. Enter 1 for dine in, 2 for pickup, or -1 to exit");
                                                    int orderType = 0;
                                                    try{
                                                        orderType = Integer.parseInt(scanner.nextLine());
                                                    }catch (NumberFormatException e){
                                                        System.out.println("Please enter number to choose");
                                                    }
                                                    if(orderType==-1) break;

                                                    String orderStatus=null;
                                                    if(orderType==1){
                                                        orderStatus="Dine In";
                                                    }else if(orderType==2){
                                                        orderStatus="Pick Up";
                                                    }else{
                                                        System.out.println("Please select 1 or 2 to order");
                                                        break;
                                                    }


                                                    if (cartNumber >= 1 && cartNumber <= cartIDs.size()) {
                                                        String selectedCartID = cartIDs.get(cartNumber - 1);
                                                        Cart selectedCartItem = (Cart) cartItems.get(selectedCartID);
                                                        if (selectedCartItem != null) {
                                                            double orderPrice = selectedCartItem.getPrice();
                                                            double currentBalance = stub.getBalance("John Doe");

                                                            if (currentBalance < orderPrice) {
                                                                System.out.println("Insufficient balance to place the order.");
                                                                break;
                                                            }

                                                            System.out.println("Selected Cart Item:");
                                                            System.out.println("Food Item: " + selectedCartItem.getFoodItem() + " X " + selectedCartItem.getQuantity() + " Price: " + selectedCartItem.getPrice());

                                                            System.out.println("Do you want to place the order for this food item? Enter y or n");
                                                            String answer1 = scanner.nextLine();
                                                            if (answer1.equalsIgnoreCase("y")) {
                                                                try {
                                                                    stub.createOrder("John Doe", selectedCartItem.getFoodItem().getFoodName(), selectedCartItem.getQuantity(), selectedCartItem.getPrice(), orderStatus, "Pending");
                                                                    stub.setBalance("John Doe", currentBalance - orderPrice);
                                                                    cartItems.remove(selectedCartID);
                                                                    stub.updateCartData(cartItems);
                                                                    System.out.println("Order placed successfully!");
                                                                    System.out.println("Updated Balance: " + (currentBalance - orderPrice));
                                                                } catch (RemoteException e) {
                                                                    System.err.println("Error creating order: " + e.getMessage());
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("Invalid cart ID selected.");
                                                        }
                                                    } else {
                                                        System.out.println("Invalid cart number selected.");
                                                    }
                                                }
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Enter cart ID to delete or enter -1 to exit:");
                                            int cartID = 0;
                                            try {
                                                cartID = Integer.parseInt(scanner.nextLine());
                                            }catch (NumberFormatException e){
                                                System.out.println("Please enter number to choose");
                                            }
                                            if(cartID==-1)break;
                                            break;
                                        default:
                                            System.out.println("Invalid choice");
                                    }
                                    System.out.println("*".repeat(40));
                                }
                                System.out.println("*".repeat(40));
                                break;
                            case 3:
                                System.out.println("This is order that you have made in McGee Restaurant");
                                Map<String, ? > categories1 = stub.viewFoodData("FoodOrder");
                                if (categories1.isEmpty()) {
                                    System.out.println("No food order found.");
                                    System.out.println("*".repeat(40));
                                    break;
                                } else {
                                    System.out.println("Food Orders:");
                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                        System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                    }
                                }
                                System.out.println("*".repeat(40));
                                break;
                            case 4:
                                double balance = stub.getBalance("John Doe");
                                System.out.println("Current Balance: " + balance);
                                break;
                            case 5:
                                System.out.println("Enter amount to add or -1 to exit:");
                                double amount;
                                try {
                                    amount = Double.parseDouble(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid amount. Please enter a valid number.");
                                    return;
                                }

                                double currentBalance = stub.getBalance("John Doe");
                                stub.setBalance("John Doe", currentBalance + amount);
                                System.out.println("Balance updated successfully. New balance: " + (currentBalance + amount));
                                break;
                            case 6:
                                break;
                            default:
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
