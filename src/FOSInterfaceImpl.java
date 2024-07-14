import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class FOSInterfaceImpl extends UnicastRemoteObject implements FOSInterface {

    private Map<String, Order> orders;
    private Map<String, FoodCategory> foodCategories;
    private Map<String, FoodItems> foodItems;
    private Map<String, Cart> shoppingCart;

    int foodCategoriesCount;
    private int orderIdCounter;
    private static final String FOOD_CATEGORIES_FILE = "food_categories.ser";
    private static final String FOOD_ITEM_FILE = "food_menu.ser";
    private static final String CART_FILE = "food_cart.ser";
    private static final String ORDERS_FILE = "orders.ser";

    protected FOSInterfaceImpl() throws RemoteException {
        super();
        orders = (Map<String, Order>) readFromFile(ORDERS_FILE);
        foodCategories = (Map<String, FoodCategory>) readFromFile(FOOD_CATEGORIES_FILE);
        foodItems = (Map<String, FoodItems>) readFromFile(FOOD_ITEM_FILE);
        shoppingCart = (Map<String, Cart>) readFromFile(CART_FILE);
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
    public String placeOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, customerName, item, quantity, price, orderType, status);
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

        // Create the new food item
        int foodID = foodItems.size() + 1;
        String foodID1 = String.valueOf(foodID);
        FoodItems newItem = new FoodItems(foodID1, name, price, foodCategory);
        foodItems.put(foodID1, newItem);

        // Write the updated map back to the file
        writeToFile(FOOD_ITEM_FILE, foodItems);
    }

    @Override
    public void createCart(String customerName, FoodItems foodItem, int quantity, double price) throws RemoteException {
        shoppingCart = (Map<String, Cart>) readFromFile(CART_FILE);

        // Check if the shoppingCart is null, initialize it if it is
        if (shoppingCart == null) {
            shoppingCart = new HashMap<>();
        }

        int cartID = shoppingCart.size() + 1;
        Cart newItem = new Cart(String.valueOf(cartID), customerName, foodItem, quantity, price);
        shoppingCart.put(String.valueOf(cartID), newItem);

        // Write the updated shoppingCart back to the file
        writeToFile(CART_FILE, shoppingCart);
    }

    @Override
    public void createOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException {
        orders = (Map<String, Order>) readFromFile(ORDERS_FILE);
        if (orders == null) {
            orders = new HashMap<>();
        }

        int orderID = orders.size() + 1;
        System.out.println(orderID);
        Order newItem = new Order(orderID, customerName, item, quantity, price, orderType, status);
        orders.put(String.valueOf(orderID), newItem);
        writeToFile(ORDERS_FILE, orders);
    }

    @Override
    public Map<String, ?> viewFoodData(String type) throws RemoteException {
        if ("FoodCategory".equalsIgnoreCase(type)) {
            return new HashMap<>(foodCategories);
        } else if ("FoodItems".equalsIgnoreCase(type)) {
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
            case "cart":
                if (orders.containsKey(ID)) {
                    orders.remove(ID);
                    writeToFile(ORDERS_FILE, orders);
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
}
