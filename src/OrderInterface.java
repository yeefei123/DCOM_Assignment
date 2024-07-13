import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OrderInterface extends Remote {
    Order placeOrder(String itemType, String itemId, double itemPrice, int quantity) throws RemoteException;

    Order getOrderDetails(int orderId) throws RemoteException;

    String reverseString(Order input) throws RemoteException;
}
