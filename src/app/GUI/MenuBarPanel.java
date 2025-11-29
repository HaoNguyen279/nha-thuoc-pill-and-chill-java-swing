package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.*;

import app.DAO.TaiKhoanDAO;
import app.Main.App;

public class MenuBarPanel extends JPanel implements ActionListener {
    private String maNV;
    private MainFrame parentFrame;
	// MenuItem cho hệ thống
	private JMenuItem mniHoTro;
	private JMenuItem mniDatLai;
	private JMenuItem mniTaiKhoan;
	private JMenuItem mniDangXuat;
	private JMenuItem mniThoat;

	// MenuItem cho cập nhật
	private JMenuItem mniCapNhatThuoc;
	private JMenuItem mniCapNhatKhachHang;
	private JMenuItem mniCapNhatNhanVien;
	private JMenuItem mniCapNhatKhuyenMai;
	private JMenuItem mniCapNhatChucVu;
	private JMenuItem mniXemPhieuNhap;

	// MenuItem cho tìm kiếm
	private JMenuItem mniTimKiemThuoc;
	private JMenuItem mniTimKiemKhachHang;
	private JMenuItem mniTimKiemNhanVien;
	private JMenuItem mniHoaDon;
	private JMenuItem mniPhieuDoiTra;
	private JMenuItem mniPhieuDatThuoc;
	
	// MenuItem cho xử lí
	private JMenuItem mniLapHoaDon;
	private JMenuItem mniLapPhieuDatThuoc;
	private JMenuItem mniLapPhieuTraThuoc;
	private JMenuItem mniNhapThuoc;

	// MenuItem cho thống kê
	private JMenu mnuDoanhThu; // Đổi thành JMenu để chứa submenu
	private JMenuItem mniDoanhThuTheoThang;
	private JMenuItem mniDoanhThuTheoNam;
	
	private JMenuItem mniNhanVien;
	private JMenuItem mniKhachHang;
	private JMenuItem mniHanSuDung;
//	private JMenuItem mniThue;
	private JMenuItem mniThuocDuocMuaNhieu;
	
    public MenuBarPanel(String maNV, MainFrame parentFrame) {
        this.maNV = maNV;
        this.parentFrame = parentFrame;
        initializeMenuBar();
    }

    private void initializeMenuBar() {
        JMenuBar mnuMenuBar = new JMenuBar();
        TaiKhoanDAO tkDAO = new TaiKhoanDAO();
        String tenNhanVien =  tkDAO.getTenNhanVienByMaNV(maNV);
        JLabel lblTenNhanVien = new JLabel("Chào mừng:" + tenNhanVien);
        lblTenNhanVien.setFont(new Font("Arial", Font.PLAIN, 15));
        
        // Tạo các menu với icon
        JMenu mnuHeThong = new JMenu("Hệ thống");
        mnuHeThong.setIcon(loadIcon("/icon/system_icon.png"));  
        
        JMenu mnuCapNhat = new JMenu("Danh mục");
        mnuCapNhat.setIcon(loadIcon("/icon/task_icon.png"));  
        
        JMenu mnuTimKiem = new JMenu("Tìm kiếm");
        mnuTimKiem.setIcon(loadIcon("/icon/magnifier_icon.png"));  
        
        JMenu mnuXuLy = new JMenu("Xử lý");
        mnuXuLy.setIcon(loadIcon("/icon/update_icon.png"));  
        
        JMenu mnuThongKe = new JMenu("Thống kê");
        mnuThongKe.setIcon(loadIcon("/icon/chart_icon.png"));  
        
        Font fntMenu = new Font("Arial", Font.PLAIN, 15);
        
        for (JMenu item : new JMenu[]{
        		mnuHeThong , mnuCapNhat,
        		mnuTimKiem, mnuXuLy, mnuThongKe
    	    }) {
    	        item.setFont(fntMenu);
    	    }

        this.setPreferredSize(new Dimension(0,50));
        mnuMenuBar.setBackground(new Color(240,250,240));

	     // Trong method initializeMenuBar(), sau khi tạo JMenuItem
	
	     // HỆ THỐNG
	     mniHoTro = new JMenuItem("Hỗ trợ");
	     mniHoTro.setIcon(loadIcon("/icon/support_icon.png"));
	     mniHoTro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)); // F1
	
	     mniDatLai = new JMenuItem("Đặt lại");
	     mniDatLai.setIcon(loadIcon("/icon/reset_icon.png"));
	     mniDatLai.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)); // F5
	
	     mniTaiKhoan = new JMenuItem("Đổi mật khẩu");
	     mniTaiKhoan.setIcon(loadIcon("/icon/password_icon.png"));
	     mniTaiKhoan.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK)); // Ctrl+P
	
	     mniDangXuat = new JMenuItem("Đăng xuất");
	     mniDangXuat.setIcon(loadIcon("/icon/logout_icon.png"));
	     mniDangXuat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK)); // Ctrl+L
	
	     mniThoat = new JMenuItem("Thoát");
	     mniThoat.setIcon(loadIcon("/icon/poweroff_icon.png"));
	     mniThoat.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK)); // Ctrl+Q
	
	     // CẬP NHẬT
	     mniCapNhatThuoc = new JMenuItem("Thuốc");
	     mniCapNhatThuoc.setIcon(loadIcon("/icon/drug_icon.png"));
	     mniCapNhatThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK)); // Ctrl+T
	
	     mniCapNhatKhachHang = new JMenuItem("Khách hàng");
	     mniCapNhatKhachHang.setIcon(loadIcon("/icon/customer_icon.png"));
	     mniCapNhatKhachHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK)); // Ctrl+K
	
	     mniCapNhatNhanVien = new JMenuItem("Nhân viên");
	     mniCapNhatNhanVien.setIcon(loadIcon("/icon/employee_icon.png"));
	     mniCapNhatNhanVien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)); // Ctrl+N
	
	     mniCapNhatKhuyenMai = new JMenuItem("Khuyến mãi");
	     mniCapNhatKhuyenMai.setIcon(loadIcon("/icon/sale_icon.png"));
	     mniCapNhatKhuyenMai.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK)); // Ctrl+M
	
	     mniCapNhatChucVu = new JMenuItem("Chức vụ");
	     mniCapNhatChucVu.setIcon(loadIcon("/icon/position_icon.png"));
	     mniCapNhatChucVu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK)); // Ctrl+V
	
	     mniXemPhieuNhap = new JMenuItem("Phiếu nhập");
	     mniXemPhieuNhap.setIcon(loadIcon("/icon/import_icon.png"));
	     mniXemPhieuNhap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK)); // Ctrl+I
	
	     // TÌM KIẾM
	     mniTimKiemThuoc = new JMenuItem("Thuốc");
	     mniTimKiemThuoc.setIcon(loadIcon("/icon/drug_icon.png"));
	     mniTimKiemThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK)); // Ctrl+F
	     
	     mniTimKiemKhachHang = new JMenuItem("Khách hàng");
	     mniTimKiemKhachHang.setIcon(loadIcon("/icon/customer_icon.png"));
	     mniTimKiemKhachHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK)); // Ctrl+Shift+F
	     
	     mniTimKiemNhanVien = new JMenuItem("Nhân viên");
	     mniTimKiemNhanVien.setIcon(loadIcon("/icon/employee_icon.png"));
	     mniTimKiemNhanVien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK)); // Alt+F
	     
        //lala
        mniHoaDon = new JMenuItem("Hóa đơn");
        mniHoaDon.setIcon(loadIcon("/icon/bill_icon.png"));  
        mniHoaDon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK)); // Alt+N
	    
        mniPhieuDatThuoc = new JMenuItem("Phiếu đặt thuốc");
        mniPhieuDatThuoc.setIcon(loadIcon("/icon/order_icon.png"));  
        mniPhieuDatThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK)); // Alt+D
	    
        mniPhieuDoiTra = new JMenuItem("Phiếu đổi trả");
        mniPhieuDoiTra.setIcon(loadIcon("/icon/cash_back_icon.png"));  
        mniPhieuDoiTra.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK)); // Alt+T
		    
	     
	     // XỬ LÝ
	     mniLapHoaDon = new JMenuItem("Lập hóa đơn");
	     mniLapHoaDon.setIcon(loadIcon("/icon/bill_icon.png"));
	     mniLapHoaDon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK)); // Ctrl+H
	
	     mniLapPhieuDatThuoc = new JMenuItem("Lập phiếu đặt thuốc");
	     mniLapPhieuDatThuoc.setIcon(loadIcon("/icon/order_icon.png"));
	     mniLapPhieuDatThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK)); // Ctrl+D
	
	     mniLapPhieuTraThuoc = new JMenuItem("Lập phiếu trả thuốc");
	     mniLapPhieuTraThuoc.setIcon(loadIcon("/icon/refund_icon.png"));
	     mniLapPhieuTraThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK)); // Ctrl+R
	
	     mniNhapThuoc = new JMenuItem("Nhập thuốc");
	     mniNhapThuoc.setIcon(loadIcon("/icon/import_icon.png"));
	     mniNhapThuoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK)); // Ctrl+Shift+I
	
	     // THỐNG KÊ
        // MenuItem cho thống kê với icon
        mnuDoanhThu = new JMenu("Doanh thu nhà thuốc"); // Đổi thành JMenu
        mnuDoanhThu.setIcon(loadIcon("/icon/revenue_icon.png"));
	     
	     
	     
        // Tạo các submenu cho Doanh thu
        mniDoanhThuTheoThang = new JMenuItem("Theo tháng");
        mniDoanhThuTheoThang.setIcon(loadIcon("/icon/month_icon.png"));
        mniDoanhThuTheoThang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK)); // Ctrl+1
        
        mniDoanhThuTheoNam = new JMenuItem("Theo năm");
        mniDoanhThuTheoNam.setIcon(loadIcon("/icon/year_icon.png"));
        mniDoanhThuTheoNam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK)); // Ctrl+2
        
	     mniNhanVien = new JMenuItem("Doanh thu của NV");
	     mniNhanVien.setIcon(loadIcon("/icon/employee_revenue_icon.png"));
	     mniNhanVien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK)); // Ctrl+2
	
	     mniKhachHang = new JMenuItem("Doanh thu từ KH");
	     mniKhachHang.setIcon(loadIcon("/icon/customer_revenue_icon.png"));
	     mniKhachHang.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK)); // Ctrl+3
	
	     mniHanSuDung = new JMenuItem("Hạn sử dụng");
	     mniHanSuDung.setIcon(loadIcon("/icon/expiration_date_icon.png"));  
	     mniHanSuDung.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK)); // Ctrl+4
	
	
	
	     mniThuocDuocMuaNhieu = new JMenuItem("Thuốc mua nhiều");
	     mniThuocDuocMuaNhieu.setIcon(loadIcon("/icon/top_icon.png"));  
	     mniThuocDuocMuaNhieu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.CTRL_MASK)); // Ctrl+6
            
        Font itemFont = new Font("Arial", Font.PLAIN, 14);

        for (JMenuItem item1 : new JMenuItem[]{
            mniHoTro, mniDangXuat, mniThoat,mniDatLai,mniTaiKhoan,mniCapNhatChucVu,mniXemPhieuNhap,
            mniCapNhatThuoc, mniCapNhatKhachHang, mniCapNhatNhanVien, mniCapNhatKhuyenMai,
            mniTimKiemThuoc, mniTimKiemKhachHang, mniTimKiemNhanVien, mniHoaDon,
            mniDoanhThuTheoThang, mniDoanhThuTheoNam,
        }) {
            item1.setFont(itemFont);
            item1.setBackground(getBackground());
            item1.setPreferredSize(new Dimension(180,40));
            item1.addActionListener(this);
        }
        for (JMenuItem item1 : new JMenuItem[]{
                mniLapHoaDon, mniLapPhieuDatThuoc, mniLapPhieuTraThuoc,mniNhapThuoc, mniPhieuDatThuoc, mniPhieuDoiTra, 
                mniNhanVien, mniKhachHang, mniHanSuDung, mniThuocDuocMuaNhieu
            }) {
                item1.setFont(itemFont);
                item1.setBackground(getBackground());
                item1.setPreferredSize(new Dimension(230,40));
                item1.addActionListener(this);
            }
        
        // Set bù cho cái Jmenu
        mnuDoanhThu.setFont(itemFont);
        mnuDoanhThu.setBackground(getBackground());
        mnuDoanhThu.setPreferredSize(new Dimension(180,40));
        
        // Hệ thống
        mnuHeThong.add(mniHoTro);
        mnuHeThong.add(mniDatLai);
        mnuHeThong.add(mniTaiKhoan);
        mnuHeThong.add(mniDangXuat);
        mnuHeThong.add(mniThoat);

        // Cập nhật
        mnuCapNhat.add(mniCapNhatThuoc);
        mnuCapNhat.add(mniCapNhatKhachHang);
        mnuCapNhat.add(mniCapNhatNhanVien);
        mnuCapNhat.add(mniCapNhatKhuyenMai);
        mnuCapNhat.add(mniCapNhatChucVu);


        // Tìm kiếm
        mnuTimKiem.add(mniTimKiemThuoc);
        mnuTimKiem.add(mniTimKiemKhachHang);
        mnuTimKiem.add(mniTimKiemNhanVien);
        mnuTimKiem.add(mniHoaDon);
        mnuTimKiem.add(mniPhieuDatThuoc);
        mnuTimKiem.add(mniPhieuDoiTra);
        mnuTimKiem.add(mniXemPhieuNhap);

        // Xử lý
        mnuXuLy.add(mniLapHoaDon);
        mnuXuLy.add(mniLapPhieuDatThuoc);
        mnuXuLy.add(mniLapPhieuTraThuoc);
        mnuXuLy.add(mniNhapThuoc);
        
        // Thống kê
        mnuDoanhThu.add(mniDoanhThuTheoThang);
        mnuDoanhThu.add(mniDoanhThuTheoNam);
        
        mnuThongKe.add(mnuDoanhThu);
        mnuThongKe.add(mniNhanVien);
        mnuThongKe.add(mniKhachHang);
//        mnuThongKe.add(mniThue);
        mnuThongKe.add(mniHanSuDung);
        mnuThongKe.add(mniThuocDuocMuaNhieu);

        // Thêm vào Menubar chính
        mnuMenuBar.add(mnuHeThong);
        mnuMenuBar.add(mnuCapNhat);
        mnuMenuBar.add(mnuTimKiem);
        mnuMenuBar.add(mnuXuLy);
        mnuMenuBar.add(mnuThongKe);
        //Tạo khoảng cách giữa mnubar và tên nhân viên
        mnuMenuBar.add(Box.createHorizontalGlue());
        
        

        mnuMenuBar.add(lblTenNhanVien);
        mnuMenuBar.add(Box.createHorizontalStrut(10));
        this.setLayout(new BorderLayout());
        this.add(mnuMenuBar, BorderLayout.CENTER);
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		
		// Cập nhật
		if (o == mniCapNhatKhachHang) parentFrame.showCapNhatKhachHangPanel();

		else if (o == mniCapNhatNhanVien) parentFrame.showCapNhatNhanVienPanel();

		else if (o == mniCapNhatThuoc) parentFrame.showCapNhatThuocPanel();

		else if (o == mniCapNhatKhuyenMai) parentFrame.showCapNhatKhuyenMaiPanel();
		
		else if(o == mniCapNhatChucVu) parentFrame.showCapNhatChucVuPanel();
		
		else if (o == mniXemPhieuNhap) parentFrame.showPhieuNhap();
		
		// Tìm kiếm
		else if (o == mniTimKiemThuoc) parentFrame.showTimKiemThuocPanel();

		else if (o == mniTimKiemKhachHang) parentFrame.showTimKiemKhachHangPanel();

		else if (o == mniTimKiemNhanVien) parentFrame.showTimKiemNhanVienPanel();
		
		else if (o == mniHoaDon) parentFrame.showDanhMucHoaDonPanel();
		
		else if (o == mniPhieuDatThuoc) parentFrame.showDanhMucPhieuDatThuocPanel();
		
		else if (o == mniPhieuDoiTra) parentFrame.showDanhMucPhieuDoiTraPanel();
		
		// Xử lý
		else if (o == mniLapHoaDon) parentFrame.showLapHoaDonPanel();

		else if (o == mniLapPhieuDatThuoc) parentFrame.showLapPhieuDatThuocPanel();

		else if (o == mniLapPhieuTraThuoc) parentFrame.showLapPhieuDoiThuocPanel();
		
		else if (o == mniNhapThuoc) parentFrame.showNhapThuocPanel();
		
		// Thống kê
		else if (o == mniDoanhThuTheoThang) {
			// TODO: Implement theo tháng
			parentFrame.showThongKeTheoDoanhThuPanelTheoThang();
		}
		else if (o == mniDoanhThuTheoNam) {
			// TODO: Implement theo năm
			parentFrame.showThongKeTheoDoanhThuPanelTheoNam();
		}

		else if (o == mniNhanVien) parentFrame.showThongKeTheoNhanVienPanel();

		else if (o == mniKhachHang) parentFrame.showThongKeTheoKhachHangPanel();

		else if (o == mniHanSuDung) parentFrame.showThongKeTheoHSDPanel();

		else if (o == mniThuocDuocMuaNhieu) parentFrame.showThongKeTheoThuocPanel();

//		else if (o == mniThue) parentFrame.showThongKeTheoThuePanel();
		
		// Hệ thống
		else if(o == mniDangXuat) {
			CustomJOptionPane a = new CustomJOptionPane(parentFrame, "Bạn có chắc muốn đăng xuất?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) {
				parentFrame.dangXuatHandle();
			}
		}
		else if(o == mniTaiKhoan) {
			parentFrame.showTaiKhoanPanel();
		}
		else if(o == mniDatLai) {
			CustomJOptionPane a = new CustomJOptionPane(parentFrame, "Bạn có chắc muốn đặt lại ứng dụng?", true);
		    int choice = a.show();
		    if(choice == JOptionPane.YES_OPTION) {
		        parentFrame.resetApplication();
		        CustomJOptionPane b = new CustomJOptionPane(parentFrame, "Đặt lại ứng dụng thành công!!", false);
		        b.show();
		    }
		}
		else if(o == mniThoat) {
			CustomJOptionPane a = new CustomJOptionPane(parentFrame, "Bạn có chắc muốn tắt ứng dụng?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) System.exit(0);
		}
		else if(o == mniHoTro) {
			try {
				URL fileURL = App.class.getResource("/pdf/HuongDanSuDungPhanMem.pdf");
				if(fileURL != null) {
					File file = new File(fileURL.toURI());
					if(file.exists()) {
						if(Desktop.isDesktopSupported()) {
							Desktop.getDesktop().open(file);
						}
					}
					else {
						CustomJOptionPane a =  new CustomJOptionPane(parentFrame, "File PDF không tồn tại!", false);
						a.show();
					}
				}else {
					CustomJOptionPane a =  new CustomJOptionPane(parentFrame, "File PDF không tồn tại!", false);
					a.show();
				}
			} catch (Exception e1) {
				new CustomJOptionPane(this, "Gặp lỗi khi mở file PDF, mã lỗi:!" + e1.getMessage(), false);
			}
		}
	}
	
    private ImageIcon loadIcon(String path) {
        try {
            URL iconURL = getClass().getResource(path);
            if (iconURL != null) {
                return new ImageIcon(iconURL);
            } else {
                iconURL = getClass().getClassLoader().getResource(path.substring(1)); // bỏ dấu / đầu
                if (iconURL != null) {
                    return new ImageIcon(iconURL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}