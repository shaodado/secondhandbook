
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("登入");
        setSize(300, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // 背景
        WatermarkPanel panel = new WatermarkPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 帳號
        JLabel userLabel = new JLabel("帳號：");
        JTextField userField = new JTextField(15);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(Box.createVerticalStrut(10));

        // 密碼
        JLabel passLabel = new JLabel("密碼：");
        JPasswordField passField = new JPasswordField(15);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createVerticalStrut(20));

        // 登入按鈕
        JButton loginButton = new JButton("確定");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (user.equals("admin") && pass.equals("1234")) {
                JOptionPane.showMessageDialog(this, "登入成功！");
                dispose();
                new SecondHandBookGUI();
            } else {
                JOptionPane.showMessageDialog(this, "帳號或密碼錯誤！");
            }
        });
        panel.add(loginButton);

        add(panel);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    
    class WatermarkPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            String watermark = "天才邵立翔製作";
            g2d.setFont(new Font("微軟正黑體", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();

            int textWidth = fm.stringWidth(watermark);
            int textHeight = fm.getHeight();

            int x = getWidth() - textWidth - 10; 
            int y = getHeight() - 10;            

            g2d.setColor(new Color(0, 0, 0, 180)); 
            g2d.drawString(watermark, x, y);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
