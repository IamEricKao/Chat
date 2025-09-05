import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    // 儲存所有登入的使用者管道,以便後續轉發訊息給所有人
    public static final Map<Socket, String> onLineSockets = new HashMap<>();
    public static void main(String[] args) {
        System.out.println("聊天伺服器已啟動...");
        try {
            ServerSocket serverSocket = new ServerSocket(Constant.PORT);
            while (true) {
                System.out.println("等待客戶端連接...");
                var socket = serverSocket.accept();
                System.out.println("客戶端連接進來了");
                new ServerReaderThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("伺服器啟動失敗");
        }
    }
}
