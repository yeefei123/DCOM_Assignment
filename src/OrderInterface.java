import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OrderInterface extends Remote {
    String placeOrder(String customerName, String item, int quantity, double price, String status) throws RemoteException;
    Order getOrderDetails(int orderId) throws RemoteException;
}
