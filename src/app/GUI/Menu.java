package app.GUI;

import java.awt.*;
import javax.swing.*;

public class Menu extends JFrame {
    private String tenNhanVien;
    private MenuBarPanel menuBarPanel;
    private JPanel contentPanel;
    private TimKiemThuocPanel timKiemThuocPanel;
    private LapHoaDonPanel lapHoaDonPanel;
    
    public Menu(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
        initializeFrame();
        initializePanels();
        showDefaultContent();
    }
    
    private void initializeFrame() {
        setTitle("Hệ thống quản lý nhà thuốc - Nhân viên: " + tenNhanVien);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Tạo menu bar panel
        menuBarPanel = new MenuBarPanel(tenNhanVien, this);
        add(menuBarPanel, BorderLayout.NORTH);
        
        // Tạo content panel chính
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private void initializePanels() {
        // Khởi tạo các panel chức năng (lazy loading)
        timKiemThuocPanel = null;
        lapHoaDonPanel = null;
    }
    
    private void showDefaultContent() {
        // Hiển thị trang chào mừng mặc định
        JPanel welcomePanel = createWelcomePanel();
        showPanel(welcomePanel);
    }
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Thêm hình ảnh nền
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/image/hinh-anh-nha-thuoc.jpg"));
            JLabel background = new JLabel(icon);
            background.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(background, BorderLayout.CENTER);
        } catch (Exception e) {
            // Nếu không tìm thấy hình ảnh, hiển thị text
            JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý nhà thuốc", SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
            welcomeLabel.setForeground(Color.BLUE);
            panel.add(welcomeLabel, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    public void showTimKiemThuocPanel() {
        if (timKiemThuocPanel == null) {
            timKiemThuocPanel = new TimKiemThuocPanel();
        }
        showPanel(timKiemThuocPanel);
    }
    
    public void showLapHoaDonPanel() {
        if (lapHoaDonPanel == null) {
            lapHoaDonPanel = new LapHoaDonPanel();
        }
        showPanel(lapHoaDonPanel);
    }
    
    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
