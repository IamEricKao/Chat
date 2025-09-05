import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

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
                        var nickName = dis.readUTF();
                        Server.onLineSockets.put(socket, nickName);
                        // 更新所有在線使用者列表
                        UpdateClientOnLineUserList();
                        break;
                    case 2:
                        // 客戶端發來公開聊天訊息後，接著取得聊天內容，再轉發給所有在線的使用者
                        break;
                    default:
                }
            }

        }catch (Exception e){
            System.out.println("使用者下線: " + socket.getInetAddress().getHostAddress());
            // 使用者下線後，將該使用者從在線使用者列表中移除，並更新所有在線使用者的列表
            Server.onLineSockets.remove(socket);
            UpdateClientOnLineUserList();
        }
    }

    /// 更新所有在線使用者列表
    private void UpdateClientOnLineUserList() {
        // 取得目前所有在線使用者的暱稱
        Collection<String> onLineUsers = Server.onLineSockets.values();

        // 轉發給所有在線的使用者
        for(Socket socket : Server.onLineSockets.keySet()){
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(1); // 代表更新在線使用者列表的訊息類型
                dos.writeInt(onLineUsers.size()); // 先發送在線使用者的數量
                for(String user : onLineUsers) {
                    dos.writeUTF(user);
                }
                dos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
