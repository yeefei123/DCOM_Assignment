import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            OrderInterface orderService = new OrderInterfaceImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/OrderService", orderService);
            System.out.println("Order Service is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
