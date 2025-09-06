package UI;

import java.io.DataInputStream;
import java.net.Socket;


public class ClientReaderThread extends Thread {
    private Socket socket;
    private ClientChatFrame chatFrame;
    public ClientReaderThread(Socket socket, ClientChatFrame chatFrame) {
        this.socket = socket;
        this.chatFrame = chatFrame;
    }
    //接收訊息
    @Override
    public void run() {
        //接收的訊息類型: 1.更新在線使用者列表 2.公開聊天的訊息
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while(true){
                // 先從socket中接收客戶端傳來的訊息類型的編號
                var type = dis.readInt();
                switch(type){
                    case 1:
                        // 接收服務端發來的使用者暱稱，並加入在線使用者列表中
                        UpdateChatFrameOnLineUserList(dis);
                        break;
                    case 2:

                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void UpdateChatFrameOnLineUserList(DataInputStream dis) throws Exception {
        int count =  dis.readInt();  // 先讀取使用者數量
        String[] users = new String[count];
        for(int i = 0; i < count; i++){
            String nickname = dis.readUTF(); // 讀取每個使用者的暱稱
            users[i] = nickname;
        }
        chatFrame.UpdateOnLineUserList(users);
    }

}
