package app.GUI;

import java.awt.*;
import javax.swing.*;

import app.DAO.LoThuocDAO;
import app.DAO.NhanVienDAO;


public class MainFrame extends JFrame {
    private String maNhanVien;
    private MenuBarPanel menuBarPanel;
    private JPanel contentPanel;
    private TimKiemThuocPanel timKiemThuocPanel;
    private LapHoaDonPanel lapHoaDonPanel;
    private CapNhatKhachHangPanel capNhatKhachHangPanel;
    private CapNhatNhanVienPanel capNhatNhanVienPanel;
    private CapNhatThuocPanel capNhatThuocPanel;
    private CapNhatKhuyenMaiPanel capNhatKhuyenMaiPanel;
    private LapPhieuDatThuocPanel lapPhieuDatThuocPanel;
    private NhapThuocPanel nhapThuocPanel;
    private LapPhieuTraThuocPanel lapPhieuTraThuocPanel;
    private ThongKeTheoDoanhThuPanel thongKeTheoDoanhThuPanel;
    private ThongKeDoanhThuTheoThangPanel thongKeTheoDoanhThuTheoThangPanel;
    private ThongKeTheoNhanVienPanel thongKeTheoNhanVienPanel;
    private ThongKeTheoKhachHangPanel thongKeTheoKhachHangPanel;
    private ThongKeTheoHSDPanel thongKeTheoHSDPanel;
    private ThongKeTheoThuocPanel thongKeTheoThuocPanel;
    private ThongKeTheoThuePanel thongKeTheoThuePanel;
    private TimKiemKhachHangPanel timKiemKhachHangPanel;
    private TimKiemNhanVienPanel timKiemNhanVienPanel;
    private TaiKhoanPanel taiKhoanPanel;
    private CapNhatChucVuPanel capNhatChucVuPanel;
    private XemPhieuNhapPanel xemPhieuNhapPanel;
    
    private DanhMucHoaDon danhMucHoaDonPanel;
    private DanhMucPhieuDoiTra danhMucPhieuDoiTra;
    private DanhMucPhieuDat danhMucPhieuDat;
    
    private boolean isQuanLy = false;
    
    public MainFrame(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        initializeFrame();
        initializePanels();
        showDefaultContent();
        if(true) {
        	NhanVienDAO nvDAO = new NhanVienDAO();
        	isQuanLy=  nvDAO.isQuanLy(maNhanVien);
        }
    }
    
    private void initializeFrame() {
        setTitle("Hệ thống quản lý nhà thuốc - Nhân viên: " + maNhanVien);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Tạo menu bar panel
        menuBarPanel = new MenuBarPanel(maNhanVien, this);
        add(menuBarPanel, BorderLayout.NORTH);
        
        // Tạo content panel chính
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel, BorderLayout.CENTER);
        LoThuocDAO loDAO = new LoThuocDAO();
        loDAO.capNhatThuocHetHan();
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
        nhapThuocPanel = null;
        lapPhieuTraThuocPanel = null;
        thongKeTheoDoanhThuPanel = null;
        thongKeTheoDoanhThuTheoThangPanel = null;
        thongKeTheoNhanVienPanel = null;
        thongKeTheoKhachHangPanel = null;
        thongKeTheoHSDPanel = null;
        thongKeTheoThuocPanel = null;
        thongKeTheoThuePanel = null;
        timKiemKhachHangPanel = null;
        timKiemNhanVienPanel = null;
        taiKhoanPanel = null;
        capNhatChucVuPanel = null;
        xemPhieuNhapPanel = null;
        
        danhMucHoaDonPanel = null;
        danhMucPhieuDoiTra = null;
        danhMucPhieuDat = null;
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
        	ImageIcon img = new ImageIcon(getClass().getResource("/resources/image/pharmacy.jpg"));
        	JLabel lblImg = new JLabel();
        	lblImg.setIcon(img);
            panel.add(lblImg, BorderLayout.CENTER);
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
            lapHoaDonPanel = new LapHoaDonPanel(maNhanVien);
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
            lapHoaDonPanel = new LapHoaDonPanel(maNhanVien);
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
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
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
            lapPhieuDatThuocPanel = new LapPhieuDatThuocPanel(maNhanVien);
        }
        lapPhieuDatThuocPanel.reloadDataFromDatabase();
        showPanel(lapPhieuDatThuocPanel);
    }
    
    public void showLapPhieuDoiThuocPanel() {
        if (lapPhieuTraThuocPanel == null) {
            lapPhieuTraThuocPanel = new LapPhieuTraThuocPanel(maNhanVien);
        }
        showPanel(lapPhieuTraThuocPanel);
    }
    public void showNhapThuocPanel() {
    	if (nhapThuocPanel == null) {
    		nhapThuocPanel = new NhapThuocPanel();
    	}
    	showPanel(nhapThuocPanel);
    }
    public void showThongKeTheoNhanVienPanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoNhanVienPanel == null) {
            thongKeTheoNhanVienPanel = new ThongKeTheoNhanVienPanel();
        }
        showPanel(thongKeTheoNhanVienPanel);
    }
    
    public void showThongKeTheoKhachHangPanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoKhachHangPanel == null) {
            thongKeTheoKhachHangPanel = new ThongKeTheoKhachHangPanel();
        }
        showPanel(thongKeTheoKhachHangPanel);
    }
    
    public void showThongKeTheoHSDPanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoHSDPanel == null) {
            thongKeTheoHSDPanel = new ThongKeTheoHSDPanel();
        }
        showPanel(thongKeTheoHSDPanel);
    }
    
    public void showThongKeTheoThuocPanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoThuocPanel == null) {
            thongKeTheoThuocPanel = new ThongKeTheoThuocPanel();
        }
        showPanel(thongKeTheoThuocPanel);
    }
    public void showThongKeTheoDoanhThuPanelTheoThang() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
    	if (thongKeTheoDoanhThuTheoThangPanel == null) {
        	thongKeTheoDoanhThuTheoThangPanel = new ThongKeDoanhThuTheoThangPanel();
        }
        thongKeTheoDoanhThuTheoThangPanel.refresh();
        showPanel(thongKeTheoDoanhThuTheoThangPanel);
    }
    public void showThongKeTheoDoanhThuPanelTheoNam() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoDoanhThuPanel == null) {
        	thongKeTheoDoanhThuPanel = new ThongKeTheoDoanhThuPanel();
        }
        thongKeTheoDoanhThuPanel.refresh();
        showPanel(thongKeTheoDoanhThuPanel);
    }
    public void showThongKeTheoThuePanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
        if (thongKeTheoThuePanel == null) {
            thongKeTheoThuePanel = new ThongKeTheoThuePanel();
        }
        showPanel(thongKeTheoThuePanel);
    }
    public void showCapNhatChucVuPanel() {
    	if(!isQuanLy) {
    		CustomJOptionPane a = new CustomJOptionPane(this, "Chỉ có Nhân viên Quản lý mới có thể dùng chức năng này!", false);
    		a.show();
    		return;
    	}
		if(capNhatChucVuPanel == null) {
			capNhatChucVuPanel = new CapNhatChucVuPanel();
		}
		showPanel(capNhatChucVuPanel);
		
	}
    public void showPhieuNhap() {
		if(xemPhieuNhapPanel == null) {
			xemPhieuNhapPanel = new XemPhieuNhapPanel();
		}
		showPanel(xemPhieuNhapPanel);
	}
	public void showTaiKhoanPanel() {
		
		NhanVienDAO nvDao = new NhanVienDAO();
		if(taiKhoanPanel == null) {
			taiKhoanPanel = new TaiKhoanPanel(nvDao.getNhanVienById(maNhanVien));
		}
		showPanel(taiKhoanPanel);
	}
	// lala
	public void showDanhMucHoaDonPanel() {
		if(danhMucHoaDonPanel == null) {
			danhMucHoaDonPanel = new DanhMucHoaDon();
		}
		showPanel(danhMucHoaDonPanel);
	} 
	public void showDanhMucPhieuDatThuocPanel() {
		if(danhMucPhieuDat == null) {
			danhMucPhieuDat = new DanhMucPhieuDat();
		}
		showPanel(danhMucPhieuDat);
	}
	public void showDanhMucPhieuDoiTraPanel() {
		if(danhMucPhieuDoiTra == null) {
			danhMucPhieuDoiTra = new DanhMucPhieuDoiTra();
		}
		showPanel(danhMucPhieuDoiTra);
	}
	
	
    public void resetApplication() {
        // Xóa nội dung hiện tại trên màn hình
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();
        
        // Dispose các panels cũ nếu cần (giải phóng resources)
        disposeAllPanels();
        
        // Set tất cả panels về null
        initializePanels();
        
        // Hiển thị trang chào mừng
        showDefaultContent();
    }
    
    private void disposeAllPanels() {
        
        JPanel[] allPanels = {
            timKiemThuocPanel, lapHoaDonPanel, capNhatKhachHangPanel,
            capNhatNhanVienPanel, capNhatThuocPanel, capNhatKhuyenMaiPanel,
            capNhatChucVuPanel, lapPhieuDatThuocPanel, lapPhieuTraThuocPanel,
            nhapThuocPanel, thongKeTheoDoanhThuPanel, thongKeTheoNhanVienPanel,
            thongKeTheoKhachHangPanel, thongKeTheoHSDPanel, thongKeTheoThuocPanel,
            thongKeTheoThuePanel, timKiemKhachHangPanel, timKiemNhanVienPanel,
            xemPhieuNhapPanel
        };
        
        
        for (JPanel panel : allPanels) {
            if (panel != null) {
                panel.removeAll();
            }
        }
        
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
//    public static JPanel createImageBackgroundPanel(String imagePath) {
//        Image backgroundImage = new ImageIcon(imagePath).getImage();
//
//        return new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(backgroundImage, 0, 0, this);
//            }
//        };
//    }

}
