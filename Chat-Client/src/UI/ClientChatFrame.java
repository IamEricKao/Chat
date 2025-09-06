package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class ClientChatFrame extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
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
        this.setTitle(nickname);
        this.socket = socket;
    }

    private void initComponents() {
        // 左側：線上使用者列表
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createTitledBorder("線上使用者"));
        userListModel.addElement("小明");
        userListModel.addElement("小華");
        userListModel.addElement("小美");

        JScrollPane userListScroll = new JScrollPane(userList);
        userListScroll.setPreferredSize(new Dimension(120, 0));

        // 中央：聊天訊息區域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        // 下方：訊息輸入區
        inputField = new JTextField();
        sendButton = new JButton("送出");
        sendButton.setPreferredSize(new Dimension(70, 60));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(inputField, BorderLayout.CENTER);
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

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            chatArea.append("我: " + text + "\n");
            inputField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientChatFrame::new);
    }
}