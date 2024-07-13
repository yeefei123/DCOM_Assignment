import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class FOSInterfaceImpl extends UnicastRemoteObject implements FOSInterface {

    private Map<Integer, Order> orders;
    private int orderIdCounter;
    private static final String FOOD_CATEGORIES_FILE = "food_categories.ser";
    private static final String ORDERS_FILE = "orders.ser";
    private Map<String, FoodCategory> foodCategories;

    protected FOSInterfaceImpl() throws RemoteException {
        super();
        orders = new HashMap<>();
        foodCategories = readFoodCategories();
        orderIdCounter = 1;
    }

    private Map<String, FoodCategory> readFoodCategories() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FOOD_CATEGORIES_FILE))) {
            return (Map<String, FoodCategory>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void writeFoodCategories() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FOOD_CATEGORIES_FILE))) {
            out.writeObject(foodCategories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String placeOrder(String customerName, String item, int quantity, double price, String status) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, customerName, item, quantity, price, status);
        orders.put(orderId, order);
        saveOrders(); // Save orders to file
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
        writeFoodCategories();
    }

    @Override
    public Map<String, FoodCategory> viewFoodCategory() throws RemoteException {
        return new HashMap<>(foodCategories);
    }

    @Override
    public void updateFoodCategoryID(String oldCategoryID, String newCategoryID) throws RemoteException {
        if (foodCategories.containsKey(oldCategoryID)) {
            FoodCategory category = foodCategories.remove(oldCategoryID);
            category.setCategoryID(newCategoryID);
            foodCategories.put(newCategoryID, category);
            System.out.println("Food Category ID updates successfully.");
        }
    }

    @Override
    public void updateFoodCategoryName(String categoryID, String newCategoryName) throws RemoteException {
        if (foodCategories.containsKey(categoryID)) {
            FoodCategory category = foodCategories.get(categoryID);
            category.setCategoryName(newCategoryName);
            foodCategories.put(categoryID, category);
            System.out.println("Food category name updates successfully.");
        }
    }

    @Override
    public void readFoodItems() throws RemoteException{
        Map<String, FoodCategory> categories1=viewFoodCategory();
        if (categories1.isEmpty()) {
            System.out.println("No food categories found.");
        } else {
            System.out.println("Food Categories:");
            for (Map.Entry<String, FoodCategory> entry : categories1.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().toString());
            }
        }
    }

    @Override
    public void deleteFoodCategory(String categoryID) throws RemoteException {
        if (foodCategories.containsKey(categoryID)) {
            foodCategories.remove(categoryID);
            writeFoodCategories();
        } else {
            throw new RemoteException("Food category ID not found.");
        }
    }
}
