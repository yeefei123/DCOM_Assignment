import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FOSInterfaceImpl extends UnicastRemoteObject implements FOSInterface {
    private Map<String,Customer> customers;
    private Map<String, Order> orders;
    private Map<String, FoodCategory> foodCategories;
    private Map<String, FoodItems> foodItems;
    private Map<String, DrinkItems> drinkItems;
    private Map<String, Cart> shoppingCart;
    private Map<String, Double> balances;

    private Customer currentCustomer;

    int foodCategoriesCount;
    private int orderIdCounter;
    private static final String CUSTOMER_FILE = "customer.ser";
    private static final String FOOD_CATEGORIES_FILE = "food_categories.ser";
    private static final String FOOD_ITEM_FILE = "food_menu.ser";
    private static final String DRINK_ITEM_FILE = "drink_menu.ser";
    private static final String CART_FILE = "food_cart.ser";
    private static final String ORDERS_FILE = "orders.ser";
    private static final String BALANCES_FILE = "balances.ser";

    protected FOSInterfaceImpl() throws RemoteException {
        super();
        customers = (Map<String,Customer>) readFromFile(CUSTOMER_FILE);
        orders = (Map<String, Order>) readFromFile(ORDERS_FILE);
        foodCategories = (Map<String, FoodCategory>) readFromFile(FOOD_CATEGORIES_FILE);
        drinkItems = (Map<String, DrinkItems>) readFromFile(DRINK_ITEM_FILE);
        foodItems = (Map<String, FoodItems>) readFromFile(FOOD_ITEM_FILE);
        shoppingCart = (Map<String, Cart>) readFromFile(CART_FILE);
        balances = (Map<String, Double>) readFromFile(BALANCES_FILE);
        orderIdCounter = 1;
    }

    private Map<?, ?> readFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("Created new file: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<?, ?>) in.readObject();
        } catch (EOFException e) {
            System.out.println("The file " + filename + " is empty or corrupted.");
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void writeToFile(String filename, Map<?, ?> map) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(map);
            System.out.println("Data written successfully to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkDuplicate(String userName) throws RemoteException {
        customers = (Map<String, Customer>) readFromFile(CUSTOMER_FILE);
        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
            String customerId = entry.getKey();
            Customer customer = entry.getValue();

            if(Objects.equals(userName.toLowerCase(), customer.getUsername().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(String username, String password, String firstName, String lastName, String icOrPassportNumber, String phoneNumber, String address) throws RemoteException {
        customers = (Map<String, Customer>) readFromFile(CUSTOMER_FILE);
        int customerId = customers.size() + 1;
        String customerIdString = String.valueOf(customerId);
        Customer newCustomer = new Customer(customerIdString, username, password, firstName, lastName, icOrPassportNumber, phoneNumber, address);
        customers.put(customerIdString, newCustomer);
        writeToFile(CUSTOMER_FILE, customers);
        System.out.println(newCustomer.getUsername() + " register successfully");
    }

    @Override
    public boolean login(String userName, String password) throws RemoteException {
        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
            String customerId = entry.getKey();
            Customer customer = entry.getValue();

            if(Objects.equals(userName.toLowerCase(), customer.getUsername().toLowerCase()) && Objects.equals(password,customer.getPassword())){
                System.out.println(customer.getUsername() + " login successfully");
                currentCustomer = customer;
                return true;
            }
        }

        return false;
    }

    @Override
    public Customer getCurrentLoginCustomer() throws RemoteException {
        return currentCustomer;
    }

    @Override
    public void updateProfile(String customerId, Customer newCustomer) throws RemoteException {
        if(customers.containsKey(customerId)) {
            customers.put(customerId, newCustomer);
            System.out.println("Customer profile updated successfully.");
            writeToFile(CUSTOMER_FILE, customers);
            currentCustomer = newCustomer;
        } else {
            System.out.println("Customer ID not found. Please try again.");
        }
    }

    @Override
    public void updateFoodItemName(String foodID, String newFoodName) throws RemoteException {
        if (foodItems.containsKey(foodID)) {
            FoodItems foodItem = foodItems.get(foodID);
            foodItem.setFoodName(newFoodName);
            foodItems.put(foodID, foodItem);
            System.out.println("Food item name updated successfully.");
            writeToFile(FOOD_ITEM_FILE, foodItems);
        } else {
            System.out.println("Food item ID not found. Please try again.");
        }
    }

    @Override

    public void updateFoodItemPrice(String foodID, double newFoodPrice) throws RemoteException {
        if (foodItems.containsKey(foodID)) {
            FoodItems foodItem = foodItems.get(foodID);
            foodItem.setFoodPrice(newFoodPrice);
            foodItems.put(foodID, foodItem);
            System.out.println("Food item price updated successfully.");
            writeToFile(FOOD_ITEM_FILE, foodItems);
        } else {
            System.out.println("Food item ID not found. Please try again.");
        }
    }

    @Override
    public String placeOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException {
        withdrawBalance(customerName, price * quantity); // Deduct balance here
        int orderId = orderIdCounter++;
        LocalDateTime orderTime = LocalDateTime.now();
        Order order = new Order(orderId, customerName, item, quantity, price, orderType, status, orderTime);
        orders.put(String.valueOf(orderId), order);
        saveOrders();
        return "Order placed successfully! " + order.toString();
    }


    private void saveOrders() {
        writeToFile(ORDERS_FILE, orders);
    }

    @Override
    public Order getOrderDetails(int orderId) throws RemoteException {
        return orders.get(orderId);
    }

    @Override
    public void createFoodCategory(String categoryID, String categoryName) throws RemoteException {
        FoodCategory newCategory = new FoodCategory(categoryID, categoryName);
        foodCategories.put(categoryID, newCategory);
        writeToFile(FOOD_CATEGORIES_FILE, foodCategories);
    }

    @Override
    public void createFoodItems(String name, double price, String foodCategory) throws RemoteException {
        // Read the existing food items from the file
        foodItems = (Map<String, FoodItems>) readFromFile(FOOD_ITEM_FILE);
        int foodID = foodItems.size() + 1;
        String foodID1 = String.valueOf(foodID);
        FoodItems newItem = new FoodItems(foodID1, name, price, foodCategory);
        foodItems.put(foodID1, newItem);
        writeToFile(FOOD_ITEM_FILE, foodItems);
    }

    @Override
    public void createDrinkItems(String name, double price, String foodCategory) throws RemoteException {
        drinkItems = (Map<String, DrinkItems>) readFromFile(DRINK_ITEM_FILE);
        int foodID = drinkItems.size() + 1;
        String foodID1 = String.valueOf(foodID);
        DrinkItems newItem = new DrinkItems(foodID1, name, price, foodCategory);
        drinkItems.put(foodID1, newItem);
        writeToFile(DRINK_ITEM_FILE, drinkItems);
    }

    @Override
    public void createCart(String customerName, MenuItem item, int quantity, double price) throws RemoteException {
        shoppingCart = (Map<String, Cart>) readFromFile(CART_FILE);

        if (shoppingCart == null) {
            shoppingCart = new HashMap<>();
        }
        int maxID = 0;
        for (String id : shoppingCart.keySet()) {
            try {
                int currentID = Integer.parseInt(id);
                if (currentID > maxID) {
                    maxID = currentID;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        int newID = maxID + 1;
        String cartIDString = String.valueOf(newID);

        Cart newItem = new Cart(cartIDString, customerName, item, quantity, price);
        shoppingCart.put(cartIDString, newItem);

        System.out.println("Adding to cart: " + newItem);

        writeToFile(CART_FILE, shoppingCart);
    }



    @Override
    public void createOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException {
        orders = (Map<String, Order>) readFromFile(ORDERS_FILE);
        if (orders == null) {
            orders = new HashMap<>();
        }

        int orderID = orders.size() + 1;
        LocalDateTime now = LocalDateTime.now();  // Current date and time
        Order newOrder = new Order(orderID, customerName, item, quantity, price, orderType, status, now);
        orders.put(String.valueOf(orderID), newOrder);
        writeToFile(ORDERS_FILE, orders);
    }

    @Override
    public Map<String, ?> viewFoodData(String type) throws RemoteException {
        if ("FoodCategory".equalsIgnoreCase(type)) {
            return new HashMap<>(foodCategories);
        } else if ("DrinkItems".equalsIgnoreCase(type)) {
            return new HashMap<>(drinkItems);
        }else if ("FoodItems".equalsIgnoreCase(type)) {
            return new HashMap<>(foodItems);
        } else if ("ShoppingCart".equalsIgnoreCase(type)) {
            return new HashMap<>(shoppingCart);
        } else if ("FoodOrder".equals(type)) {
            return new HashMap<>(orders);
        } else {
            throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    @Override
    public void updateFoodCategoryID(String oldCategoryID, String newCategoryID) throws RemoteException {
        if (foodCategories.containsKey(oldCategoryID)) {
            FoodCategory category = foodCategories.remove(oldCategoryID);
            category.setCategoryID(newCategoryID);
            foodCategories.put(newCategoryID, category);
            System.out.println("Food Category ID updated successfully.");
            writeToFile(FOOD_CATEGORIES_FILE, foodCategories);
        }
    }

    @Override
    public void updateFoodCategoryName(String categoryID, String newCategoryName) throws RemoteException {
        if (foodCategories.containsKey(categoryID)) {
            FoodCategory category = foodCategories.get(categoryID);
            category.setCategoryName(newCategoryName);
            foodCategories.put(categoryID, category);
            System.out.println("Food category name updated successfully.");
            writeToFile(FOOD_CATEGORIES_FILE, foodCategories);
        }
    }

    @Override
    public void delete(String ID, String type) throws RemoteException {
        switch (type) {
            case "category":
                if (foodCategories.containsKey(ID)) {
                    foodCategories.remove(ID);
                    writeToFile(FOOD_CATEGORIES_FILE, foodCategories);
                } else {
                    throw new RemoteException("Food category ID not found.");
                }
                break;
            case "item":
                if (foodItems.containsKey(ID)) {
                    foodItems.remove(ID);
                    writeToFile(FOOD_ITEM_FILE, foodItems);
                    System.out.println("Food item deleted successfully.");
                } else {
                    throw new RemoteException("Food item ID not found.");
                }
                break;
            case "drink":
                if (drinkItems.containsKey(ID)) {
                    drinkItems.remove(ID);
                    writeToFile(DRINK_ITEM_FILE, drinkItems);
                    System.out.println("Drink item deleted successfully.");
                } else {
                    throw new RemoteException("Food item ID not found.");
                }
                break;
            case "cart":
                if (shoppingCart.containsKey(ID)) {
                    shoppingCart.remove(ID);
                    writeToFile(CART_FILE, shoppingCart);
                    System.out.println("Cart item deleted successfully.");
                } else {
                    throw new RemoteException("Cart item ID not found.");
                }
                break;
            default:
                throw new RemoteException("Invalid delete type.");
        }
    }

    @Override
    public void updateCartData(Map<String, ?> updatedCartData) throws RemoteException {
        shoppingCart = (Map<String, Cart>) updatedCartData;
        writeToFile(CART_FILE, shoppingCart);
        System.out.println("Shopping cart updated successfully.");
    }

    @Override
    public void updateOrderStatus(String orderID, String newStatus) throws RemoteException {
        if (orders.containsKey(orderID)) {
            Order order = orders.get(orderID);
            order.setStatus(newStatus);
            orders.put(orderID, order);
            writeToFile(ORDERS_FILE, orders);
            System.out.println("Order status updated successfully.");
        } else {
            throw new RemoteException("Order ID not found.");
        }
    }

    @Override
    public void updateFoodItemName(String foodID, String newFoodName, String type) throws RemoteException {
        if(type.equals("FoodItems")) {
            if (foodItems.containsKey(foodID)) {
                FoodItems foodItem = foodItems.get(foodID);
                foodItem.setFoodName(newFoodName);
                foodItems.put(foodID, foodItem);
                System.out.println("Food item name updated successfully.");
                writeToFile(FOOD_ITEM_FILE, foodItems);
            } else {
                System.out.println("Food item ID not found. Please try again.");
            }
        }
        if(type.equals("DrinkItems")) {
            if (drinkItems.containsKey(foodID)) {
                DrinkItems foodItem = drinkItems.get(foodID);
                foodItem.setFoodName(newFoodName);
                drinkItems.put(foodID, foodItem);
                System.out.println("Food item name updated successfully.");
                writeToFile(DRINK_ITEM_FILE, drinkItems);
            } else {
                System.out.println("Food item ID not found. Please try again.");
            }
        }
    }

    @Override
    public void updateFoodItemPrice(String foodID, double newFoodPrice, String type) throws RemoteException {
        if(type.equals("FoodItems")) {
            if (foodItems.containsKey(foodID)) {
                FoodItems foodItem = foodItems.get(foodID);
                foodItem.setFoodPrice(newFoodPrice);
                foodItems.put(foodID, foodItem);
                System.out.println("Food item price updated successfully.");
                writeToFile(FOOD_ITEM_FILE, foodItems);
            } else {
                System.out.println("Food item ID not found. Please try again.");
            }
        }
        if(type.equals("DrinkItems")) {
            if (drinkItems.containsKey(foodID)) {
                DrinkItems foodItem = drinkItems.get(foodID);
                foodItem.setFoodPrice(newFoodPrice);
                drinkItems.put(foodID, foodItem);
                System.out.println("Food item price updated successfully.");
                writeToFile(DRINK_ITEM_FILE, drinkItems);
            } else {
                System.out.println("Food item ID not found. Please try again.");
            }
        }
    }

    @Override
    public void setBalance(String customerName, double balance) throws RemoteException {
        balances.put(customerName, balance);
        writeToFile(BALANCES_FILE, balances);
        System.out.println("Balance set for " + customerName + ": " + balance);
    }

    @Override
    public double getBalance(String customerName) throws RemoteException {
        return balances.getOrDefault(customerName, 0.0);
    }

    @Override
    public void withdrawBalance(String customerName, double amount) throws RemoteException {
        double currentBalance = getBalance(customerName);
        if (currentBalance >= amount) {
            balances.put(customerName, currentBalance - amount);
            writeToFile(BALANCES_FILE, balances);
            System.out.println("Balance updated for " + customerName + ": " + (currentBalance - amount));
        } else {
            System.out.println("Insufficient balance.");
        }
    }
}
