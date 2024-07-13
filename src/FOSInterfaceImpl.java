import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class FOSInterfaceImpl extends UnicastRemoteObject implements FOSInterface {

    private Map<Integer, Order> orders;
    private Map<String, FoodCategory> foodCategories;
    private Map<String, FoodItems> foodItems;
    int foodCategoriesCount;
    private int orderIdCounter;
    private static final String FOOD_CATEGORIES_FILE = "food_categories.ser";
    private static final String FOOD_ITEM_FILE = "food_menu.ser";
    private static final String ORDERS_FILE = "orders.ser";

    protected FOSInterfaceImpl() throws RemoteException {
        super();
        orders = new HashMap<>();
        foodCategories = (Map<String, FoodCategory>) readFromFile(FOOD_CATEGORIES_FILE);
        foodItems = (Map<String, FoodItems>) readFromFile(FOOD_ITEM_FILE);
        orderIdCounter = 1;
    }

    private Map<?, ?> readFromFile(String filename) {
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
    public String placeOrder(String customerName, String item, int quantity, double price, String status) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, customerName, item, quantity, price, status);
        orders.put(orderId, order);
        saveOrders();
        return "Order placed successfully! " + order.toString();
    }

    private void saveOrders() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            out.writeObject(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // Add the new food item to the existing map
        foodItems.put(foodID1, newItem);

        // Write the updated map back to the file
        writeToFile(FOOD_ITEM_FILE, foodItems);
    }

    @Override
    public Map<String, ?> viewFoodData(String type) throws RemoteException {
        if ("FoodCategory".equalsIgnoreCase(type)) {
            return new HashMap<>(foodCategories);
        } else if ("FoodItems".equalsIgnoreCase(type)) {
            return new HashMap<>(foodItems);
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
    public void deleteFoodCategory(String categoryID) throws RemoteException {
        if (foodCategories.containsKey(categoryID)) {
            foodCategories.remove(categoryID);
            writeToFile(FOOD_CATEGORIES_FILE, foodCategories);
        } else {
            throw new RemoteException("Food category ID not found.");
        }
    }

    @Override
    public void deleteFoodItems(String foodID) throws RemoteException {
        if (foodItems.containsKey(foodID)) {
            foodItems.remove(foodID);
            writeToFile(FOOD_ITEM_FILE, foodItems);
            System.out.println("Food item deleted successfully.");
        } else {
            throw new RemoteException("Food item ID not found.");
        }
    }

}
