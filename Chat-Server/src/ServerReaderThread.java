import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerReaderThread extends Thread {
    Socket socket;
    public ServerReaderThread(Socket socket) {
        this.socket = socket;
    }
    //接收訊息
    @Override
    public void run() {
        //接收的訊息類型: 1.登入 2.公開聊天
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while(true){
                // 先從socekt中接收客戶端傳來的訊息類型的編號
                var type = dis.readInt();
                switch(type){
                    case 1:
                        // 客戶端發來登入訊息後，接著取得暱稱，再更新客戶端的在線使用者列表
                        break;
                    case 2:
                        // 客戶端發來公開聊天訊息後，接著取得聊天內容，再轉發給所有在線的使用者
                        break;
                    default:
                }
            }

        }catch (Exception e){
            System.out.println("使用者下線: " + socket.getInetAddress().getHostAddress());
        }
    }
}
