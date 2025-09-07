package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientChatFrame extends JFrame {

    private JTextArea chatArea;
    private JTextArea inputArea;
    private JButton sendButton;
    private JList<String> userList;
    private Socket socket;

    public ClientChatFrame() {
        setTitle("聊天室");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        this.setVisible(true);
    }

    public ClientChatFrame(String nickname, Socket socket) {
        this();
        this.setTitle(nickname + " 的聊天視窗");
        this.socket = socket;
        // 連接伺服器後，啟動讀取訊息的執行緒
        new ClientReaderThread(socket, this).start();
    }

    private void initComponents() {
        // 左側：線上使用者列表
        userList = new JList<>();
        userList.setBorder(BorderFactory.createTitledBorder("線上使用者"));

        JScrollPane userListScroll = new JScrollPane(userList);
        userListScroll.setPreferredSize(new Dimension(120, 0));

        // 中央：聊天訊息區域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        // 下方：訊息輸入區
        inputArea = new JTextArea(3, 20);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setCaretPosition(0); // 游標在第一列
        JScrollPane inputScroll = new JScrollPane(inputArea);

        sendButton = new JButton("送出");
        sendButton.setPreferredSize(new Dimension(70, 60));
        sendButton.addActionListener(e -> sendMessageToServer() );

        inputArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !evt.isShiftDown()) {
                    evt.consume(); // 防止換行
                    sendMessageToServer();
                }
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // 主要佈局
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScroll, userListScroll);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0);
        splitPane.setEnabled(false);

        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessageToServer() {
        String text = inputArea.getText().trim();
        if (!text.isEmpty()) {
            inputArea.setText("");
            // 連線伺服器並發送消息
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(2); // 訊息類型 : 2 = 公開聊天訊息
                dos.writeUTF(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientChatFrame::new);
    }

    public void UpdateOnLineUserList(String[] users) {
        userList.setListData(users);
    }

    public void UpdateChatArea(String msg) {
        // 將新訊息加入聊天區域
        chatArea.append(msg + "\n");
        // 自動捲動到最底部
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}