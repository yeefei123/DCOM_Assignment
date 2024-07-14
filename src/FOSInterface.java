import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface FOSInterface extends Remote {
    String placeOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException;
    Order getOrderDetails(int orderId) throws RemoteException;
    void createFoodCategory(String categoryID, String categoryName) throws RemoteException;
    void createFoodItems(String name, double price, String foodCategory) throws RemoteException;
    void createCart(String customerName, FoodItems foodItems, int quantity, double price) throws RemoteException;
    void createOrder(String customerName, String item, int quantity, double price, String orderType, String status) throws RemoteException;
    Map<String, ?> viewFoodData(String type) throws RemoteException;
    void updateFoodCategoryID(String oldCategoryID, String newCategoryID) throws RemoteException;
    void updateFoodCategoryName(String categoryID,  String newCategoryName) throws RemoteException;
    void delete(String ID, String type) throws RemoteException;
    void updateOrderStatus(String orderID, String newStatus) throws RemoteException;
    void updateCartData(Map<String, ?> updatedCartData) throws RemoteException;
}
