package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatEntryFrame extends JFrame {

    private JTextField nicknameField;
    private JButton loginButton;
    private JButton cancelButton;
    private Socket socket;

    public ChatEntryFrame() {
        setTitle("聊天室登入");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 180);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel nicknameLabel = new JLabel("暱稱：");
        nicknameLabel.setBounds(40, 30, 50, 25);
        panel.add(nicknameLabel);

        nicknameField = new JTextField();
        nicknameField.setBounds(95, 30, 200, 25);
        panel.add(nicknameField);

        loginButton = new JButton("登入");
        loginButton.setBounds(70, 80, 80, 35);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setFocusPainted(false);

        cancelButton = new JButton("取消");
        cancelButton.setBounds(190, 80, 80, 35);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setFocusPainted(false);

        panel.add(loginButton);
        panel.add(cancelButton);

        // 按鈕事件
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText().trim();
                if (nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(ChatEntryFrame.this, "請輸入暱稱！", "錯誤", JOptionPane.ERROR_MESSAGE);
                } else {
                    // 可在此處進行登入邏輯
                    try {
                        Login(nickname);
                        dispose();
                        // 開啟聊天視窗
                        new ClientChatFrame(nickname, socket);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nicknameField.setText("");
            }
        });

        setContentPane(panel);
    }

    /// 登入方法
    private void Login(String nickname) throws Exception {
        // 連接伺服器
        socket = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(1); // 訊息類型 : 1 = 登入
        dos.writeUTF(nickname); // 傳送暱稱
        dos.flush();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatEntryFrame::new);
    }
}