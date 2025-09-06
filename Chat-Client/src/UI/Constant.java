package UI;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constant {
    public static final String SERVER_IP;
    public static final int SERVER_PORT = 6666;
    static {
        try {
            SERVER_IP = String.valueOf(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
