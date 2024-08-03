import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
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
                                                    System.out.println("*".repeat(40));
                                                }
                                                break;
                                            case 2:
                                                System.out.println("Create Food Category for McGee\nIf you want to exit, please enter -1");
                                                System.out.println("Enter food category ID to add:");
                                                String categoryID = scanner.nextLine();
                                                if (categoryID.equals("-1")) break;
                                                System.out.println("Enter food category name:");
                                                String categoryName = scanner.nextLine();
                                                if (categoryID.equals("-1")) break;
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
                                                        System.out.println("*".repeat(40));
                                                        System.out.println("No food categories found.");
                                                        System.out.println("*".repeat(40));
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
                                                        System.out.println("Food Category Deleted successfully.");
                                                        System.out.println("*".repeat(40));
                                                    }else{
                                                        System.out.println("Food 1 ID not found. Please try again.");
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
                                    System.out.println("Admin Edit Food Items:\n1. View Menu Items\n2. Create Menu Items\n3. Update Menu Items \n4. Delete Menu Items \n5. Exit");
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
                                                System.out.println("*".repeat(40));
                                                System.out.println("No food items found.");
                                                System.out.println("*".repeat(40));
                                            } else {
                                                System.out.println("Food Items:");
                                                for (Map.Entry<String, ?> entry : categories.entrySet()) {
                                                    System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                }
                                                System.out.println("*".repeat(40));
                                            }

                                            Map<String, ?> categories2 = stub.viewFoodData("DrinkItems");
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
                                           break;
                                        case 2:
                                            while (true) {
                                                System.out.println("Create Food Item for McGee\nIf you want to exit, please enter -1");
                                                System.out.println("1. Food Item \n2. Drink Item");
                                                int choice1;
                                                try {
                                                    choice1 = Integer.parseInt(scanner.nextLine());
                                                } catch (NumberFormatException e) {
                                                    System.out.println("Invalid input. Please enter a number.");
                                                    continue;
                                                }
                                                if (choice1==-1) break;
                                                switch (choice1) {
                                                    case 1:
                                                        System.out.println("Enter food item name to add:");
                                                        String foodName = scanner.nextLine().trim();
                                                        if (foodName.equals("-1")) break;
                                                        System.out.println("Enter food price:");
                                                        double foodPrice = Double.parseDouble(scanner.nextLine());
                                                        if (foodPrice==-1) break;
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
                                                        System.out.println("Enter food category ID for the new food item:");
                                                        String foodCategoryID = scanner.nextLine().trim();
                                                        if (foodPrice == -1) break;

                                                        if (categories1.containsKey(foodCategoryID)) {
                                                            FoodCategory foodCategory = (FoodCategory) categories1.get(foodCategoryID);
                                                            String categoryName = foodCategory.getCategoryName();
                                                            if (!foodCategoryID.isEmpty() && !foodName.isEmpty() && foodPrice > 0) {
                                                                stub.createFoodItems(foodName, foodPrice, categoryName);
                                                                System.out.println("Food Item created successfully.");
                                                                System.out.println("*".repeat(40));
                                                            }else {
                                                                System.out.println("Please select food category ID from the list.");
                                                                System.out.println("*".repeat(40));
                                                            }
                                                        } else {
                                                            System.out.println("Please select food category ID from the list.");
                                                            System.out.println("*".repeat(40));
                                                        }
                                                        break;
                                                    case 2:
                                                        System.out.println("Enter drink item name to add:");
                                                        String drinkName = scanner.nextLine().trim();
                                                        if (drinkName.equals("-1")) break;
                                                        System.out.println("Enter drink price:");
                                                        double drinkPrice = Double.parseDouble(scanner.nextLine());
                                                        if (drinkPrice==-1) break;
                                                        if (drinkPrice > 0) {
                                                            stub.createDrinkItems(drinkName, drinkPrice);
                                                            System.out.println("Drink Item created successfully.");
                                                            System.out.println("*".repeat(40));
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                            break;
                                        case 3:
                                            System.out.println("Which category of food do you want to update?");
                                            System.out.println("1. Food Items \n2. Drink Items");
                                            int choice1;
                                            try {
                                                choice1 = Integer.parseInt(scanner.nextLine());
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid input. Please enter a number.");
                                                continue;
                                            }
                                            if (choice1==-1) break;
                                            switch (choice1){
                                                case 1:
                                            while (true) {
                                                Map<String, ?> categories1 = stub.viewFoodData("FoodItems");
                                                if (categories1.isEmpty()) {
                                                    System.out.println("*".repeat(40));
                                                    System.out.println("No food items found.");
                                                    System.out.println("*".repeat(40));
                                                } else {
                                                    System.out.println("Food Items:");
                                                    for (Map.Entry<String, ?> entry : categories1.entrySet()) {
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
                                                            stub.updateFoodItemName(foodID, newFoodName, "FoodItems");
                                                            System.out.println("Food item name updated successfully.");
                                                            System.out.println("*".repeat(40));
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
                                                            System.out.println("*".repeat(40));
                                                            continue;
                                                        }
                                                        try {
                                                            stub.updateFoodItemPrice(foodID, newFoodPrice, "FoodItems");
                                                            System.out.println("Food item price updated successfully.");
                                                            System.out.println("*".repeat(40));
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
                                                case 2:
                                                    while (true) {
                                                        Map<String, ?> categories1 = stub.viewFoodData("DrinkItems");
                                                        if (categories1.isEmpty()) {
                                                            System.out.println("No drink items found.");
                                                        } else {
                                                            System.out.println("Drink Items:");
                                                            for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                                System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                            }
                                                        }
                                                        System.out.println("Enter drink item ID to modify or enter -1 to exit:");
                                                        String foodID = scanner.nextLine().trim();
                                                        if (foodID.equals("-1")) break;
                                                        if (categories1.containsKey(foodID)) {
                                                            System.out.println("Do you want to change drink item name or drink item price? \n1. Food Item Name \n2. Food Item Price \n3. Back");
                                                            int answer2;
                                                            try {
                                                                answer2 = Integer.parseInt(scanner.nextLine());
                                                            } catch (NumberFormatException e) {
                                                                System.out.println("Invalid input. Please enter a number.");
                                                                continue;
                                                            }

                                                            if (answer2 == 1) {
                                                                System.out.println("Enter new drink item name:");
                                                                String newFoodName = scanner.nextLine().trim();
                                                                try {
                                                                    stub.updateFoodItemName(foodID, newFoodName, "DrinkItems");
                                                                    System.out.println("Drink item name updated successfully.");
                                                                    System.out.println("*".repeat(40));
                                                                } catch (RemoteException e) {
                                                                    System.out.println("Error updating drink item name: " + e.getMessage());
                                                                }
                                                            } else if (answer2 == 2) {
                                                                System.out.println("Enter new drink item price:");
                                                                double newFoodPrice;
                                                                try {
                                                                    newFoodPrice = Double.parseDouble(scanner.nextLine());
                                                                } catch (NumberFormatException e) {
                                                                    System.out.println("Invalid input. Please enter a valid price.");
                                                                    System.out.println("*".repeat(40));
                                                                    continue;
                                                                }
                                                                try {
                                                                    stub.updateFoodItemPrice(foodID, newFoodPrice, "DrinkItems");
                                                                    System.out.println("Drink item price updated successfully.");
                                                                    System.out.println("*".repeat(40));
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
                                                default:
                                                    break;
                                            }
                                            break;
                                        case 4:
                                            System.out.println("Do you want to delete food items or drink items?");
                                            System.out.println("1. Food Items \n2. Drink Items");
                                            int choice2;
                                            try {
                                                choice2 = Integer.parseInt(scanner.nextLine());
                                            } catch (NumberFormatException e) {
                                                System.out.println("Invalid input. Please enter a number.");
                                                continue;
                                            }
                                            if (choice2==-1) break;
                                            switch (choice2){
                                                case 1:
                                                    while (true) {
                                                        Map<String, ?> categories1 = stub.viewFoodData("FoodItems");
                                                        if (categories1.isEmpty()) {
                                                            System.out.println("*".repeat(40));
                                                            System.out.println("No food item found.");
                                                            System.out.println("*".repeat(40));
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
                                                    break;
                                                case 2:
                                                    while (true) {
                                                        Map<String, ?> categories1 = stub.viewFoodData("DrinkItems");
                                                        if (categories1.isEmpty()) {
                                                            System.out.println("No drink item found.");
                                                        } else {
                                                            System.out.println("Drink Items:");
                                                            for (Map.Entry<String, ?> entry : categories1.entrySet()) {
                                                                System.out.println(entry.getKey() + ": " + entry.getValue().toString());
                                                            }
                                                        }
                                                        System.out.println("Admin delete drink items \nEnter drink item ID to delete or enter -1 to exit:");
                                                        String foodID = scanner.nextLine().trim();
                                                        if (foodID.equals("-1")) break;
                                                        if (foodID.isEmpty()) {
                                                            System.out.println("Invalid input. Please enter food item ID from the list.");
                                                            continue;
                                                        }
                                                        if (categories1.containsKey(foodID)) {
                                                            stub.delete(foodID, "drink");
                                                        } else {
                                                            System.out.println("Drink item ID not found.");
                                                        }
                                                    }
                                                    break;
                                                default:
                                                    break;
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
                                        System.out.println("*".repeat(40));
                                        System.out.println("No food order found.");
                                        System.out.println("*".repeat(40));
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
                                                System.out.println("*".repeat(40));
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
                        System.out.println("4. Exit");
                        System.out.println("Enter number of action to perform:");

                        int actionNum = 0;
                        try {
                            actionNum = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a number to choose an action.");
                            continue;
                        }

                        if (actionNum==4) {
                            break;
                        } else if (actionNum < 1 || actionNum > 4) {
                            System.out.println("Invalid action number. Please choose from 1 to 4.");
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
                                        System.out.println("*".repeat(40));
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
                                    System.out.println("Food items selected successfully..");
                                    System.out.println("*".repeat(40));
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
                                    System.out.println("*".repeat(40));
                                    System.out.println("1. Order \n2.Exit");
                                    int choice = 0;
                                    try {
                                        choice = Integer.parseInt(scanner.nextLine());
                                    } catch (NumberFormatException e) {
                                        System.out.println("Please enter number to choose");
                                    }
                                    if(choice==2) break;
                                    if(choice==1 || choice ==2) {
                                        while (true) {
                                            if (cartItems.isEmpty()) {
                                                System.out.println("No items found in the shopping cart.");
                                                System.out.println("*".repeat(40));
                                                break;
                                            } else {
                                                System.out.println("Shopping Cart:");
                                                int i = 0;
                                                List<String> cartIDs = new ArrayList<>();
                                                for (Map.Entry<String, ?> entry : cartItems.entrySet()) {
                                                    Cart cartItem = (Cart) entry.getValue();
                                                    if (cartItem.getCustomerName().equals("John Doe")) {
                                                        i++;
                                                        cartIDs.add(entry.getKey());
                                                        System.out.println(i + ": " + cartItem.getFoodItem() + " X " + cartItem.getQuantity() + " Price:" + cartItem.getPrice());
                                                    }
                                                }

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
                                                if (cartNumber >= 1 && cartNumber <= cartIDs.size()) {
                                                    System.out.println("Please select your order type or -1 to exit. \n1 for dine in \n2 for pickup ");
                                                    int orderType = 0;
                                                    try {
                                                        orderType = Integer.parseInt(scanner.nextLine());
                                                    } catch (NumberFormatException e) {
                                                        System.out.println("Please enter number to choose");
                                                    }
                                                    if (orderType == -1) break;

                                                    String orderStatus = null;
                                                    if (orderType == 1) {
                                                        orderStatus = "Dine In";
                                                    } else if (orderType == 2) {
                                                        orderStatus = "Pick Up";
                                                    } else {
                                                        System.out.println("Please select 1 or 2 to order");
                                                        break;
                                                    }

                                                    String selectedCartID = cartIDs.get(cartNumber - 1);
                                                    Cart selectedCartItem = (Cart) cartItems.get(selectedCartID);
                                                    if (selectedCartItem != null) {
                                                        System.out.println("Selected Cart Item:");
                                                        System.out.println("Food Item: " + selectedCartItem.getFoodItem() + " X " + selectedCartItem.getQuantity() + " Price: " + selectedCartItem.getPrice());

                                                        System.out.println("Do you want to make order for this food item? Enter y or n");
                                                        String answer1 = scanner.nextLine();
                                                        if (answer1.equals("y")) {
                                                            try {
                                                                stub.createOrder("John Doe", selectedCartItem.getFoodItem().getFoodName(), selectedCartItem.getQuantity(), selectedCartItem.getPrice(), orderStatus, "Pending");
                                                                cartItems.remove(selectedCartID);
                                                                stub.updateCartData(cartItems);
                                                                System.out.println("Order placed successfully!");
                                                            } catch (RemoteException e) {
                                                                System.err.println("Error creating order: " + e.getMessage());
                                                            }
                                                        }else{
                                                            System.out.println("Invalid choice. PLease try again.");
                                                        }
                                                    } else {
                                                        System.out.println("Invalid cart ID selected.");
                                                    }
                                                } else {
                                                    System.out.println("Invalid cart number selected.");
                                                }
                                            }
                                        }
                                    }else{
                                        System.out.println(("Please enter 1 or 2 to select."));
                                    }
                                    System.out.println("*".repeat(40));
                                }
                                System.out.println("*".repeat(40));
                                break;
                            case 3:
                                System.out.println("This is order that you have made in McGee Restaurant");
                                Map<String, ? > categories1 = stub.viewFoodData("FoodOrder");
                                if (categories1.isEmpty()) {
                                    System.out.println("*".repeat(40));
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
