import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.rmi.server.RMIClientSocketFactory;

public class CustomRMIClientSocketFactory implements RMIClientSocketFactory {
    private final SSLSocketFactory sslSocketFactory;

    public CustomRMIClientSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    @Override
    public SSLSocket createSocket(String host, int port) throws IOException {
        return (SSLSocket) sslSocketFactory.createSocket(host, port);
    }
}
