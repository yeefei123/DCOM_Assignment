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

    public Order placeOrder(int foodId, int foodQuantity) throws RemoteException {
        int orderId = orderIdCounter++;
        Order order = new Order(orderId, foodId, foodQuantity);
        orders.put(orderId, order);
        return order;
    }

    @Override
    public Order getOrderDetails(int orderId) throws RemoteException {
        return orders.get(orderId);
    }

    @Override

    public String reverseString(Order input) throws RemoteException {

        return new StringBuilder(String.valueOf(input)).reverse().toString();

    }
}
