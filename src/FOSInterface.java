import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface FOSInterface extends Remote {
    String placeOrder(String customerName, String item, int quantity, double price, String status) throws RemoteException;
    Order getOrderDetails(int orderId) throws RemoteException;
    void createFoodCategory(String categoryID, String categoryName) throws RemoteException;
    void createFoodItems(String name, double price, String foodCategory) throws RemoteException;
    void createCart(String customerName, FoodItems foodItems, int quantity, double price) throws RemoteException;
    Map<String, ?> viewFoodData(String type) throws RemoteException;
    void updateFoodCategoryID(String oldCategoryID, String newCategoryID) throws RemoteException;
    void updateFoodCategoryName(String categoryID,  String newCategoryName) throws RemoteException;
    void deleteFoodCategory(String categoryID) throws RemoteException;
    void deleteFoodItems(String categoryID) throws RemoteException;
}
