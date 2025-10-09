import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

class Book {
    String title, author, seller;
    double price;
    boolean isSold;
    ImageIcon cover;

    public Book(String title, String author, double price, String seller, ImageIcon cover) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.seller = seller;
        this.cover = cover;
        this.isSold = false;
    }
}

public class SecondHandBookGUI extends JFrame {
    private final ArrayList<Book> books = new ArrayList<>();
    private JTextField titleField, authorField, priceField, sellerField;
    private JLabel imagePreview;
    private File selectedImageFile;
    private JPanel bookListPanel; // 書籍展示區（販賣頁）
    private CardLayout cardLayout;
    private JPanel mainCardPanel;

    public SecondHandBookGUI() {
        super("📚 二手書平台");

        // 使用系統外觀
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setSize(920, 660);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 功能欄（上方） ---
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        menuBar.setBackground(new Color(245, 245, 247));

        Font menuFont = new Font("微軟正黑體", Font.PLAIN, 15);

        // 改成 JMenu + JMenuItem 結構，點擊才切換
        JMenu menuSell = new JMenu("販賣");
        JMenu menuChat = new JMenu("聊聊");
        JMenu menuFind = new JMenu("找書");

        menuSell.setFont(menuFont);
        menuChat.setFont(menuFont);
        menuFind.setFont(menuFont);

        JMenuItem sellItem = new JMenuItem("進入販賣頁");
        JMenuItem chatItem = new JMenuItem("進入聊聊頁");
        JMenuItem findItem = new JMenuItem("進入找書頁");

        sellItem.addActionListener(e -> cardLayout.show(mainCardPanel, "販賣"));
        chatItem.addActionListener(e -> cardLayout.show(mainCardPanel, "聊聊"));
        findItem.addActionListener(e -> cardLayout.show(mainCardPanel, "找書"));

        menuSell.add(sellItem);
        menuChat.add(chatItem);
        menuFind.add(findItem);

        menuBar.add(menuSell);
        menuBar.add(Box.createHorizontalStrut(12));
        menuBar.add(menuChat);
        menuBar.add(Box.createHorizontalStrut(12));
        menuBar.add(menuFind);
        setJMenuBar(menuBar);

        // --- 販賣頁面 ---
        JPanel sellPanel = new JPanel(new BorderLayout());
        sellPanel.setBackground(new Color(250, 250, 250));
        sellPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("新增書籍"),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        inputPanel.setBackground(new Color(250, 250, 250));

        titleField = new JTextField();
        authorField = new JTextField();
        priceField = new JTextField();
        sellerField = new JTextField();
        imagePreview = new JLabel("尚未選擇圖片");
        imagePreview.setFont(new Font("微軟正黑體", Font.PLAIN, 12));

        JButton uploadButton = new JButton("上傳封面");
        uploadButton.addActionListener(e -> chooseImage());

        JButton addButton = new JButton("新增書籍");
        addButton.addActionListener(e -> addBook());

        inputPanel.add(new JLabel("書名：")); inputPanel.add(titleField);
        inputPanel.add(new JLabel("作者：")); inputPanel.add(authorField);
        inputPanel.add(new JLabel("價格：")); inputPanel.add(priceField);
        inputPanel.add(new JLabel("售賣者姓名：")); inputPanel.add(sellerField);
        inputPanel.add(uploadButton); inputPanel.add(imagePreview);
        inputPanel.add(new JLabel()); inputPanel.add(addButton);

        sellPanel.add(inputPanel, BorderLayout.NORTH);

        bookListPanel = new JPanel();
        bookListPanel.setLayout(new GridLayout(0, 4, 14, 14)); // 一行四本
        bookListPanel.setBackground(new Color(250, 250, 250));
        bookListPanel.setBorder(BorderFactory.createEmptyBorder(10, 6, 10, 6));

        JScrollPane scrollPane = new JScrollPane(bookListPanel);
        scrollPane.getViewport().setBackground(new Color(250, 250, 250));
        scrollPane.setBorder(null);
        sellPanel.add(scrollPane, BorderLayout.CENTER);

        // --- 聊聊、找書頁面 ---
        JPanel chatPanel = createPlaceholderPanel("💬 聊聊功能尚未開放");
        JPanel findPanel = createPlaceholderPanel("🔎 找書功能尚未開放");

        // --- 主畫面切換區 ---
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.add(sellPanel, "販賣");
        mainCardPanel.add(chatPanel, "聊聊");
        mainCardPanel.add(findPanel, "找書");

        add(mainCardPanel, BorderLayout.CENTER);

        // 預設顯示販賣頁
        cardLayout.show(mainCardPanel, "販賣");

        // 全域字型美化
        setGlobalFont(new Font("微軟正黑體", Font.PLAIN, 13));

        setVisible(true);
    }

    private void chooseImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imagePreview.setText(selectedImageFile.getName());
        }
    }

    // 提高圖片清晰度
    private Image getHighQualityScaledImage(Image srcImg, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resized;
    }

    private void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String seller = sellerField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());

            ImageIcon cover = null;
            if (selectedImageFile != null) {
                ImageIcon original = new ImageIcon(selectedImageFile.getAbsolutePath());
                Image scaled = getHighQualityScaledImage(original.getImage(), 160, 220);
                cover = new ImageIcon(scaled);
            }

            Book book = new Book(title, author, price, seller, cover);
            books.add(book);
            displayBook(book);
            clearInput();
            JOptionPane.showMessageDialog(this, "書籍新增成功！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "請確認輸入資料是否完整與正確！");
        }
    }

    private void displayBook(Book book) {
        JPanel bookPanel = new JPanel(new BorderLayout());
        bookPanel.setBackground(Color.WHITE);
        bookPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        bookPanel.setPreferredSize(new Dimension(200, 320));

        JLabel coverLabel = new JLabel();
        coverLabel.setHorizontalAlignment(JLabel.CENTER);
        if (book.cover != null) {
            coverLabel.setIcon(book.cover);
        } else {
            coverLabel.setText("無封面");
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        bookPanel.add(coverLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 2, 2));

        Font infoFont = new Font("微軟正黑體", Font.PLAIN, 13);
        JLabel titleLabel = new JLabel("書名：" + book.title);
        JLabel authorLabel = new JLabel("作者：" + book.author);
        JLabel priceLabel = new JLabel(String.format("價格：$%.2f", book.price));
        JLabel sellerLabel = new JLabel("售賣者：" + book.seller);

        titleLabel.setFont(infoFont);
        authorLabel.setFont(infoFont);
        priceLabel.setFont(infoFont);
        sellerLabel.setFont(infoFont);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        infoPanel.add(sellerLabel);

        bookPanel.add(infoPanel, BorderLayout.SOUTH);
        bookListPanel.add(bookPanel);
        bookListPanel.revalidate();
        bookListPanel.repaint();
    }

    private void clearInput() {
        titleField.setText("");
        authorField.setText("");
        priceField.setText("");
        sellerField.setText("");
        selectedImageFile = null;
        imagePreview.setText("尚未選擇圖片");
    }

    private JPanel createPlaceholderPanel(String msg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(250, 250, 250));
        JLabel label = new JLabel(msg);
        label.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        label.setForeground(new Color(120, 120, 120));
        p.add(label);
        return p;
    }

    private void setGlobalFont(Font f) {
        UIManager.put("Label.font", f);
        UIManager.put("Button.font", f);
        UIManager.put("TextField.font", f);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecondHandBookGUI::new);
    }
}
