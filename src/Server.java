import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            OrderInterface orderService = new OrderInterfaceImpl();
            Registry registry = LocateRegistry.createRegistry(1099); // default port is 1099
            registry.rebind("OrderInterface", orderService);
            System.out.println("Order Service is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
