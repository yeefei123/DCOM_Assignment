import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
                                System.out.println("Admin Edit Food Menu \n1. Food Category \n2. Food Items \n3. Exit");
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
                                                        stub.deleteFoodCategory(categoryIDToDelete);
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
                                                    System.out.println(entry.getKey() + ": " + entry.getValue());
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
                                                if (foodID=="-1") break;
                                                if (foodID.isEmpty()) {
                                                    System.out.println("Invalid input. Please enter food item ID from the list.");
                                                    continue;
                                                }
                                                if (categories1.containsKey(foodID)) {
                                                    stub.deleteFoodItems(foodID);
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
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect password. Please try again.");
                        }
                    }
                } else if (role == 2) {
                    String response = stub.placeOrder("John Doe", "Pizza", 2, 19.99, "Processing");
                    System.out.println(response);
                    Order order = stub.getOrderDetails(1);
                    System.out.println("Retrieved Order: " + order.toString());
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
