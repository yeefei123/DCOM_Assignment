import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static List<MenuItem> foodMenu = new ArrayList<>();
    private static List<MenuItem> drinksMenu = new ArrayList<>();

    private static final String FOOD_MENU_FILE = "foodMenu.ser";
    private static final String DRINKS_MENU_FILE = "drinksMenu.ser";

    static class MenuItem implements Serializable {
        private static final long serialVersionUID = 1L;

        String id;
        String name;
        double price;

        MenuItem(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return id + ". " + name + " - RM " + price;
        }
    }

    private static void writeMenuToFile(String fileName, List<MenuItem> menu) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(menu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<MenuItem> readMenuFromFile(String fileName) {
        List<MenuItem> menu = new ArrayList<>();
        File file = new File(fileName);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
                menu = (List<MenuItem>) in.readObject();
            } catch (EOFException e) {
                System.out.println("Reached end of file while reading " + fileName);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    private static void writeOrderToFile(String fileName, Order order) {
        boolean append = new File(fileName).exists();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName, append))) {
            out.writeObject(order);
            System.out.println("Order Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMenus() {
        foodMenu = readMenuFromFile(FOOD_MENU_FILE);
        drinksMenu = readMenuFromFile(DRINKS_MENU_FILE);
    }

    private static void saveMenus() {
        writeMenuToFile(FOOD_MENU_FILE, foodMenu);
        writeMenuToFile(DRINKS_MENU_FILE, drinksMenu);
    }

    private static void displayMenu(List<MenuItem> menu, String title) {
        System.out.println(title);
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }

    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("Admin Menu:\n 1. Create Menu Item\n 2. View Menu\n 3. Update Menu Item\n 4. Delete Menu Item\n 5. Exit");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 5) {
                    break;
                }

                switch (choice) {
                    case 1:
                        int menuType;
                        do {
                            System.out.println("Enter 1 for Food or 2 for Drinks");
                            try {
                                menuType = Integer.parseInt(scanner.nextLine());
                                if (menuType != 1 && menuType != 2) {
                                    System.out.println("Invalid input. Please enter 1 for Food or 2 for Drinks.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number (1 or 2) for Food or Drinks.");
                                menuType = 0; // Reset menuType to force loop reiteration
                            }
                        } while (menuType != 1 && menuType != 2);

                        System.out.println("Enter item name:");
                        String name = scanner.nextLine();
                        System.out.println("Enter item price:");
                        double price = Double.parseDouble(scanner.nextLine());

                        List<MenuItem> menu = (menuType == 1) ? foodMenu : drinksMenu;
                        String id = (menuType == 1 ? "F" : "D") + (menu.size() + 1);
                        menu.add(new MenuItem(id, name, price));
                        saveMenus();
                        System.out.println("Item added to menu.");
                        break;

                    case 2:
                        displayMenu(foodMenu, "Food Menu:");
                        displayMenu(drinksMenu, "Drinks Menu:");
                        break;

                    case 3:
                        updateMenuItem(scanner);
                        break;

                    case 4:
                        deleteMenuItem(scanner);
                        break;

                    default:
                        System.out.println("Invalid choice.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1-5) for menu selection.");
            }
        }
    }

    private static void updateMenuItem(Scanner scanner) {
        System.out.println("Enter 1 for Food or 2 for Drinks");
        try {
            int menuType = Integer.parseInt(scanner.nextLine());
            if (menuType != 1 && menuType != 2) {
                System.out.println("Invalid input. Please enter 1 for Food or 2 for Drinks.");
                return;
            }

            String itemTypeToUpdate = (menuType == 1) ? "Food" : "Drinks";
            List<MenuItem> menu = (menuType == 1) ? foodMenu : drinksMenu;
            displayMenu(menu, itemTypeToUpdate + " Menu:");
            System.out.println("Enter item ID to update:");
            String idToUpdate = scanner.nextLine();

            // Validate item ID format
            if (!idToUpdate.matches((menuType == 1 ? "F" : "D") + "\\d+")) {
                System.out.println("Invalid item ID format. Expected format: " + (menuType == 1 ? "F1, F2, ..." : "D1, D2, ..."));
                return;
            }

            System.out.println("Enter new name:");
            String newName = scanner.nextLine();
            double newPrice;

            // Validate new price input
            while (true) {
                try {
                    System.out.println("Enter new price:");
                    newPrice = Double.parseDouble(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for price. Please enter a valid number.");
                }
            }

            MenuItem itemToUpdate = menu.stream().filter(i -> i.id.equals(idToUpdate)).findFirst().orElse(null);
            if (itemToUpdate != null) {
                itemToUpdate.name = newName;
                itemToUpdate.price = newPrice;
                saveMenus();
                System.out.println("Item updated.");
            } else {
                System.out.println("Item not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1 or 2) for Food or Drinks.");
        }
    }


    private static void deleteMenuItem(Scanner scanner) {
        System.out.println("Enter 1 for Food or 2 for Drinks");
        try {
            int menuType = Integer.parseInt(scanner.nextLine());
            if (menuType != 1 && menuType != 2) {
                System.out.println("Invalid input. Please enter 1 for Food or 2 for Drinks.");
                return;
            }

            String itemTypeToDelete = (menuType == 1) ? "Food" : "Drinks";
            List<MenuItem> menu = (menuType == 1) ? foodMenu : drinksMenu;
            displayMenu(menu, itemTypeToDelete + " Menu:");
            System.out.println("Enter item ID to delete:");
            String idToDelete = scanner.nextLine();

            // Validate item ID format
            if (!idToDelete.matches("[FD]\\d+")) {
                System.out.println("Invalid item ID format. Expected format: F1, D1, etc.");
                return;
            }

            boolean itemDeleted = menu.removeIf(i -> i.id.equals(idToDelete));
            if (itemDeleted) {
                for (int i = 0; i < menu.size(); i++) {
                    menu.get(i).id = (menuType == 1 ? "F" : "D") + (i + 1);
                }
                saveMenus();
                System.out.println("Item deleted.");
            } else {
                System.out.println("Item not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1 or 2) for Food or Drinks.");
        }
    }

    private static void customerMenu(Scanner scanner) {
        displayMenu(foodMenu, "Food Menu:");
        displayMenu(drinksMenu, "Drinks Menu:");

        double total = 0.0;
        List<Order> orders = new ArrayList<>();
        int orderId = 1;

        while (true) {
            System.out.println("Enter foodId or drinkId to order (or -1 to exit):");
            String menuIdStr = scanner.nextLine().trim();

            if (menuIdStr.equals("-1")) {
                break;
            }

            // Validate input
            if (!isValidMenuItem(menuIdStr)) {
                System.out.println("Invalid item ID. Please enter a valid item ID.");
                continue;
            }

            System.out.println("Enter quantity to order:");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for quantity. Please enter a valid number.");
                continue;
            }

            // Determine item type (food or drink) and item ID
            String itemType = menuIdStr.substring(0, 1);
            String itemId = menuIdStr.substring(1);

            // Find item in the respective menu list
            MenuItem orderedItem = null;
            List<MenuItem> menu = itemType.equals("F") ? foodMenu : drinksMenu;
            for (MenuItem item : menu) {
                if (item.id.equals(menuIdStr)) {
                    orderedItem = item;
                    break;
                }
            }

            if (orderedItem == null) {
                System.out.println("Item not found. Please try again.");
                continue;
            }

            // Calculate subtotal for the order
            double subtotal = orderedItem.price * quantity;

            // Create Order object and add to orders list
            Order order = new Order(orderId++, itemType, itemId, orderedItem.price, quantity);
            orders.add(order);

            // Calculate total price
            total += subtotal;

            // Write order to file (optional)
            writeOrderToFile("foodOrder.ser", order);
        }

        // Display order summary
        System.out.println("Thank you for ordering!");
        System.out.println("==========================================");
        System.out.println("                 Your Bill                ");
        System.out.println("==========================================");
        for (Order order : orders) {
            String itemName = order.getItemType().equals("F") ? getMenuItemName(foodMenu, order.getItemId()) : getMenuItemName(drinksMenu, order.getItemId());
            System.out.println("Item: " + itemName + " - RM " + order.getQuantity() + " x " + order.getItemPrice() + " = RM " + (order.getItemPrice() * order.getQuantity()));
        }
        System.out.println("==========================================");
        System.out.println("Total: RM " + total);
        System.out.println("==========================================");
    }

    private static boolean isValidMenuItem(String menuIdStr) {
        if (menuIdStr.length() < 2) {
            return false;
        }
        String itemType = menuIdStr.substring(0, 1);
        String itemId = menuIdStr.substring(1);
        if (!(itemType.equals("F") || itemType.equals("D"))) {
            return false;
        }
        List<MenuItem> menu = itemType.equals("F") ? foodMenu : drinksMenu;
        for (MenuItem item : menu) {
            if (item.id.equals(menuIdStr)) {
                return true;
            }
        }
        return false;
    }

    private static String getMenuItemName(List<MenuItem> menu, String itemId) {
        for (MenuItem item : menu) {
            if (item.id.equals(itemId)) {
                return item.name;
            }
        }
        return "";
    }

    public static void main(String[] args) {
        loadMenus(); // Load the menus from files

        try {
            // Specify the host and port for the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1098);

            // Look up the remote object in the RMI registry
            OrderInterface stub = (OrderInterface) registry.lookup("OrderInterface");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Select your role:\n 1. Admin\n 2. Customer");

                int role;
                try {
                    role = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number (1 or 2) for role selection.");
                    continue;
                }

                if (role == 1) {
                    System.out.println("Enter admin password:");
                    String password = scanner.nextLine();
                    if (password.equals("1234")) {
                        adminMenu(scanner);
                    } else {
                        System.out.println("Incorrect password.");
                    }
                } else if (role == 2) {
                    customerMenu(scanner);
                    break; // Exit after customer menu is done
                } else {
                    System.out.println("Invalid role. Please enter 1 for Admin or 2 for Customer.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
