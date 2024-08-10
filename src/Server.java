import javax.net.ssl.*;
import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyStore;

public class Server {
    public static void main(String[] args) {
        try {
            // Load keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream("./rmi-server.p12"), "password".toCharArray());

            // Set up key and trust managers
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, "password".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            // Set up SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Create SSL socket factories
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create RMI socket factories
            CustomRMIServerSocketFactory customServerSocketFactory = new CustomRMIServerSocketFactory(sslServerSocketFactory);
            CustomRMIClientSocketFactory customClientSocketFactory = new CustomRMIClientSocketFactory(sslSocketFactory);

            // Create registry
            Registry registry = LocateRegistry.createRegistry(1099, customClientSocketFactory, customServerSocketFactory);

            // Bind the remote object to the registry
            FOSInterfaceImpl obj = new FOSInterfaceImpl();
            registry.rebind("FOSInterface", obj);

            System.out.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
