import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface OrderInterface extends Remote {
    Order placeOrder(int foodId, int foodQuantity) throws RemoteException;
    Order getOrderDetails(int orderId) throws RemoteException;
    String reverseString(Order input) throws RemoteException;
}
