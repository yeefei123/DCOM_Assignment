import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class OrderInterfaceImpl extends UnicastRemoteObject implements OrderInterface {

    private Map<Integer, Order> orders;
    private int orderIdCounter;

    protected OrderInterfaceImpl() throws RemoteException {
        super();
        orders = new HashMap<>();
        orderIdCounter = 1;
    }

    @Override
    public String placeOrder(String customerName, String item, int quantity, double price, String status) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, customerName, item, quantity, price, status);
        orders.put(orderId, order);
        return "Order placed successfully! " + order.toString();
    }

    @Override
    public Order getOrderDetails(int orderId) throws RemoteException {
        return orders.get(orderId);
    }
}
