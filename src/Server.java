import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            FOSInterfaceImpl obj = new FOSInterfaceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("FOSInterface", obj);

            System.out.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
