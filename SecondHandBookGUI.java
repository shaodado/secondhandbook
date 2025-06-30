import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

class Book {
    String title, author;
    double price;
    boolean isSold;
    ImageIcon cover;

    public Book(String title, String author, double price, ImageIcon cover) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.cover = cover;
        this.isSold = false;
    }

    @Override
    public String toString() {
        return String.format("《%s》 by %s - $%.2f [%s]", title, author, price, isSold ? "已售出" : "可購買");
    }
}

public class SecondHandBookGUI extends JFrame {
    private final ArrayList<Book> books = new ArrayList<>();
    private JTextArea displayArea;
    private JTextField titleField, authorField, priceField;
    private JLabel imagePreview;
    private File selectedImageFile;

    public SecondHandBookGUI() {
        setTitle("📚 二手書平台");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 選單列
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("檔案");
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // 新增
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("新增書籍"));
        titleField = new JTextField();
        authorField = new JTextField();
        priceField = new JTextField();
        imagePreview = new JLabel("尚未選擇圖片");

        JButton uploadButton = new JButton("上傳封面");
        uploadButton.addActionListener(e -> chooseImage());

        JButton addButton = new JButton("新增書籍");
        addButton.addActionListener(e -> addBook());

        inputPanel.add(new JLabel("書名：")); inputPanel.add(titleField);
        inputPanel.add(new JLabel("作者：")); inputPanel.add(authorField);
        inputPanel.add(new JLabel("價格：")); inputPanel.add(priceField);
        inputPanel.add(uploadButton); inputPanel.add(imagePreview);
        inputPanel.add(new JLabel()); inputPanel.add(addButton);

        // 顯示
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

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

    private void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());

            ImageIcon cover = selectedImageFile != null ? new ImageIcon(selectedImageFile.getAbsolutePath()) : null;

            Book book = new Book(title, author, price, cover);
            books.add(book);

            displayArea.append(book + "\n");
            clearInput();

            JOptionPane.showMessageDialog(this, "書籍新增成功！");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "請填寫正確資料！");
        }
    }

    private void clearInput() {
        titleField.setText("");
        authorField.setText("");
        priceField.setText("");
        selectedImageFile = null;
        imagePreview.setText("尚未選擇圖片");
    }
}
