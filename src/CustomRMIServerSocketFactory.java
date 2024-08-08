import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.rmi.server.RMIServerSocketFactory;

public class CustomRMIServerSocketFactory implements RMIServerSocketFactory {
    private final SSLServerSocketFactory sslServerSocketFactory;

    public CustomRMIServerSocketFactory(SSLServerSocketFactory sslServerSocketFactory) {
        this.sslServerSocketFactory = sslServerSocketFactory;
    }

    @Override
    public SSLServerSocket createServerSocket(int port) throws IOException {
        return (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
    }
}
