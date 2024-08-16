import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Objects;

public interface FOSInterface extends Remote {
    boolean checkDuplicate(String userName) throws RemoteException;
    void register(String username, String password, String firstName, String lastName, String icOrPassportNumber, String phoneNumber, String address) throws RemoteException;
    boolean login(String username, String password) throws RemoteException;
    Customer getCurrentLoginCustomer() throws RemoteException;
    void updateProfile(String userId, Customer customer) throws RemoteException;

    String placeOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException;
    Order getOrderDetails(int orderId) throws RemoteException;
    void createOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException;
    void updateOrderStatus(String orderID, String newStatus) throws RemoteException;

    void createFoodCategory(String categoryID, String categoryName) throws RemoteException;
    void updateFoodCategoryID(String oldCategoryID, String newCategoryID) throws RemoteException;
    void updateFoodCategoryName(String categoryID, String newCategoryName) throws RemoteException;

    void createFoodItems(String name, double price, String foodCategory) throws RemoteException;
    void createDrinkItems(String name, double price, String foodCategory) throws RemoteException;
    void updateFoodItemName(String foodID, String newFoodName) throws RemoteException;
    void updateFoodItemPrice(String foodID, double newFoodPrice) throws RemoteException;

    void createCart(String customerName, MenuItem foodItems, int quantity, double price) throws RemoteException;

    void updateCartData(Map<String, ?> updatedCartData) throws RemoteException;

    Map<String, ?> viewFoodData(String type) throws RemoteException;

    void delete(String ID, String type) throws RemoteException;

    void updateFoodItemName(String foodID, String newFoodName, String type) throws RemoteException;

    void updateFoodItemPrice(String foodID, double newFoodPrice, String type) throws RemoteException;

    void setBalance(String customerName, double balance) throws RemoteException;
    double getBalance(String customerName) throws RemoteException;
    void withdrawBalance(String customerName, double amount) throws RemoteException;
}
