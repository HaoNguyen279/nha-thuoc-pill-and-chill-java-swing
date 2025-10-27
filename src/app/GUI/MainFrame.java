package app.GUI;

import java.awt.*;
import javax.swing.*;


public class MainFrame extends JFrame {
    private String tenNhanVien;
    private MenuBarPanel menuBarPanel;
    private JPanel contentPanel;
    private TimKiemThuocPanel timKiemThuocPanel;
    private LapHoaDonPanel lapHoaDonPanel;
    private CapNhatKhachHangPanel capNhatKhachHangPanel;
    private CapNhatNhanVienPanel capNhatNhanVienPanel;
    private CapNhatThuocPanel capNhatThuocPanel;
    private CapNhatKhuyenMaiPanel capNhatKhuyenMaiPanel;
    private LapPhieuDatThuocPanel lapPhieuDatThuocPanel;
    private LapPhieuDoiThuocPanel lapPhieuDoiThuocPanel;
    private NhapThuocPanel nhapThuocPanel;
    private ThongKeTheoDoanhThuPanel thongKeTheoDoanhThuPanel;
    private ThongKeDoanhThuTheoThangPanel thongKeTheoDoanhThuTheoThangPanel;
    private ThongKeTheoNhanVienPanel thongKeTheoNhanVienPanel;
    private ThongKeTheoKhachHangPanel thongKeTheoKhachHangPanel;
    private ThongKeTheoHSDPanel thongKeTheoHSDPanel;
    private ThongKeTheoThuocPanel thongKeTheoThuocPanel;
    private ThongKeTheoThuePanel thongKeTheoThuePanel;
    private TimKiemKhachHangPanel timKiemKhachHangPanel;
    private TimKiemNhanVienPanel timKiemNhanVienPanel;
    
    public MainFrame(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
        initializeFrame();
        initializePanels();
        showDefaultContent();
    }
    
    private void initializeFrame() {
        setTitle("Hệ thống quản lý nhà thuốc - Nhân viên: " + tenNhanVien);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        capNhatKhachHangPanel = null;
        capNhatNhanVienPanel = null;
        capNhatThuocPanel = null;
        capNhatKhuyenMaiPanel = null;
        lapPhieuDatThuocPanel = null;
        lapPhieuDoiThuocPanel = null;
        nhapThuocPanel = null;
        thongKeTheoDoanhThuPanel = null;
        thongKeTheoDoanhThuTheoThangPanel = null;
        thongKeTheoNhanVienPanel = null;
        thongKeTheoKhachHangPanel = null;
        thongKeTheoHSDPanel = null;
        thongKeTheoThuocPanel = null;
        thongKeTheoThuePanel = null;
        timKiemKhachHangPanel = null;
        timKiemNhanVienPanel = null;
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
//            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/image/pharmacy.jpg"));
//            JLabel background = new JLabel(icon);
//            background.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(createImageBackgroundPanel("/resources/image/pharmacy.jpg"), BorderLayout.CENTER);
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
            lapHoaDonPanel = new LapHoaDonPanel(tenNhanVien);
        }
        // Luôn làm mới dữ liệu khi hiển thị panel này, bất kể là mới tạo hay đã tồn tại
        lapHoaDonPanel.reloadDataFromDatabase();
        showPanel(lapHoaDonPanel);
    }
    
    /**
     * Trả về panel LapHoaDon hiện tại
     * Thêm phương thức này để có thể gọi các phương thức của LapHoaDonPanel từ bên ngoài
     * @return LapHoaDonPanel đang được sử dụng
     */
    public LapHoaDonPanel getLapHoaDonPanel() {
        if (lapHoaDonPanel == null) {
            lapHoaDonPanel = new LapHoaDonPanel(tenNhanVien);
        }
        return lapHoaDonPanel;
    }
    
    public void showCapNhatKhachHangPanel() {
        if (capNhatKhachHangPanel == null) {
            capNhatKhachHangPanel = new CapNhatKhachHangPanel();
        }
        showPanel(capNhatKhachHangPanel);
    }
    
    public void showCapNhatNhanVienPanel() {
        if (capNhatNhanVienPanel == null) {
            capNhatNhanVienPanel = new CapNhatNhanVienPanel();
        }
        showPanel(capNhatNhanVienPanel);
    }
    
    public void showCapNhatThuocPanel() {
        if (capNhatThuocPanel == null) {
            capNhatThuocPanel = new CapNhatThuocPanel();
        }
        showPanel(capNhatThuocPanel);
    }
    
    public void showCapNhatKhuyenMaiPanel() {
        if (capNhatKhuyenMaiPanel == null) {
            capNhatKhuyenMaiPanel = new CapNhatKhuyenMaiPanel();
        }
        showPanel(capNhatKhuyenMaiPanel);
    }
    
    // TODO: Thêm các method này khi có TimKiemKhachHangPanel và TimKiemNhanVienPanel
    public void showTimKiemKhachHangPanel() {
        if (timKiemKhachHangPanel == null) {
            timKiemKhachHangPanel = new TimKiemKhachHangPanel();
        }
        showPanel(timKiemKhachHangPanel);
    }
    
    public void showTimKiemNhanVienPanel() {
        if (timKiemNhanVienPanel == null) {
            timKiemNhanVienPanel = new TimKiemNhanVienPanel();
        }
        showPanel(timKiemNhanVienPanel);
    }
    
    public void showLapPhieuDatThuocPanel() {
        if (lapPhieuDatThuocPanel == null) {
            lapPhieuDatThuocPanel = new LapPhieuDatThuocPanel(tenNhanVien);
        }
        // Luôn làm mới dữ liệu khi hiển thị panel này, bất kể là mới tạo hay đã tồn tại
        lapPhieuDatThuocPanel.reloadDataFromDatabase();
        showPanel(lapPhieuDatThuocPanel);
    }
    
    public void showLapPhieuDoiThuocPanel() {
        if (lapPhieuDoiThuocPanel == null) {
            lapPhieuDoiThuocPanel = new LapPhieuDoiThuocPanel();
        }
        showPanel(lapPhieuDoiThuocPanel);
    }
    public void showNhapThuocPanel() {
    	if (nhapThuocPanel == null) {
    		nhapThuocPanel = new NhapThuocPanel();
    	}
    	showPanel(nhapThuocPanel);
    }
    public void showThongKeTheoNhanVienPanel() {
        if (thongKeTheoNhanVienPanel == null) {
            thongKeTheoNhanVienPanel = new ThongKeTheoNhanVienPanel();
        }
        showPanel(thongKeTheoNhanVienPanel);
    }
    
    public void showThongKeTheoKhachHangPanel() {
        if (thongKeTheoKhachHangPanel == null) {
            thongKeTheoKhachHangPanel = new ThongKeTheoKhachHangPanel();
        }
        showPanel(thongKeTheoKhachHangPanel);
    }
    
    public void showThongKeTheoHSDPanel() {
        if (thongKeTheoHSDPanel == null) {
            thongKeTheoHSDPanel = new ThongKeTheoHSDPanel();
        }
        showPanel(thongKeTheoHSDPanel);
    }
    
    public void showThongKeTheoThuocPanel() {
        if (thongKeTheoThuocPanel == null) {
            thongKeTheoThuocPanel = new ThongKeTheoThuocPanel();
        }
        showPanel(thongKeTheoThuocPanel);
    }
    public void showThongKeTheoDoanhThuPanelTheoThang() {
    	if (thongKeTheoDoanhThuTheoThangPanel == null) {
        	thongKeTheoDoanhThuTheoThangPanel = new ThongKeDoanhThuTheoThangPanel();
        }
        thongKeTheoDoanhThuTheoThangPanel.refresh();
        showPanel(thongKeTheoDoanhThuTheoThangPanel);
    }
    public void showThongKeTheoDoanhThuPanelTheoNam() {
        if (thongKeTheoDoanhThuPanel == null) {
        	thongKeTheoDoanhThuPanel = new ThongKeTheoDoanhThuPanel();
        }
        thongKeTheoDoanhThuPanel.refresh();
        showPanel(thongKeTheoDoanhThuPanel);
    }
    public void showThongKeTheoThuePanel() {
        if (thongKeTheoThuePanel == null) {
            thongKeTheoThuePanel = new ThongKeTheoThuePanel();
        }
        showPanel(thongKeTheoThuePanel);
    }
    
    
    // Show panel - đổi panel khi chọn tab khác
    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public void dangXuatHandle() {
    	this.dispose();
    	new DangNhapFrame();
   }
    public static JPanel createImageBackgroundPanel(String imagePath) {
        Image backgroundImage = new ImageIcon(imagePath).getImage();

        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, this);
            }
        };
    }

}
