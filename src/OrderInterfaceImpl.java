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
    public Order placeOrder(String itemType, String itemId, double itemPrice, int quantity) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, itemType, itemId, itemPrice, quantity);
        orders.put(orderId, order);
        return order;
    }

    @Override
    public Order getOrderDetails(int orderId) throws RemoteException {
        return orders.get(orderId);
    }

    @Override
    public String reverseString(Order input) throws RemoteException {
        return new StringBuilder(input.toString()).reverse().toString();
    }
}
