package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LapHoaDon extends JFrame {
    
    public LapHoaDon() {
        setTitle("Lập Hóa Đơn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Main panel với BorderLayout
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        
        // Panel menu phía trên (NORTH)
        MenuBarPanel menu = new MenuBarPanel();
        JPanel pnlMenu = menu.get();
        pnlMain.add(pnlMenu, BorderLayout.NORTH);
        
        // Panel chính giữa chứa 2 bảng(CENTER)
        JPanel pnlCenter = createCenterPanel();
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        
        // Panel nút xác nhận phía dưới (SOUTH)
        JPanel pnlBottom = createBottomPanel();
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);
        
        add(pnlMain);
        setVisible(true);
    }
    
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);
        
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel giữa chứa 2 bảng
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        tablesPanel.setBackground(Color.WHITE);
        
        // Bảng trên - Lập hóa đơn
        JPanel topTablePanel = createTablePanel("", "Thêm vào giỏ hàng");
        tablesPanel.add(topTablePanel);
        
        // Bảng dưới - Giỏ hàng
        JPanel bottomTablePanel = createTablePanel("GIỎ HÀNG", "Xóa khỏi giỏ hàng");
        tablesPanel.add(bottomTablePanel);
        
        centerPanel.add(tablesPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(25);
//        searchField.setText("Nhập từ khóa tìm kiếm");
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.ITALIC, 13));
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton searchButton = new JButton("Tìm");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 13));
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }
    
    private JPanel createTablePanel(String title, String buttonText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Tiêu đề bảng (nếu có)
        if (!title.isEmpty()) {
            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
            panel.add(titleLabel, BorderLayout.NORTH);
        }
        
        // Bảng dữ liệu
        JTable table = new JTable(5, 5);
        table.setRowHeight(30);
        table.setBackground(new Color(240, 250, 240));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel điều khiển dưới bảng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(Color.WHITE);
        
        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(60, 25));
        
        JButton actionButton = new JButton(buttonText);
        actionButton.setFont(new Font("Arial", Font.PLAIN, 13));
        actionButton.setPreferredSize(new Dimension(160, 35));
        actionButton.setBackground(Color.WHITE);
        actionButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        actionButton.setFocusPainted(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        controlPanel.add(quantityLabel);
        controlPanel.add(quantitySpinner);
        controlPanel.add(actionButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.setBackground(Color.WHITE);
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        bottomPanel.add(confirmButton);
        
        return bottomPanel;
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LapHoaDon());
    }
}