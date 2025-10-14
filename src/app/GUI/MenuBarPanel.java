package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import app.Entity.PhieuDoiTra;
import app.Main.App;

public class MenuBarPanel extends JPanel implements ActionListener {
    private String tenNhanVien;
    private Menu parentFrame;
	private JFrame frmParent;
	// MenuItem cho hệ thống
	private JMenuItem mniHoTro;
	private JMenuItem mniDangXuat;
	private JMenuItem mniThoat;

	// MenuItem cho danh mục
	private JMenuItem mniHoaDon;
	private JMenuItem mniPhieuDoiTra;
	private JMenuItem mniPhieuDatThuoc;

	// MenuItem cho cập nhật
	private JMenuItem mniCapNhatThuoc;
	private JMenuItem mniCapNhatKhachHang;
	private JMenuItem mniCapNhatNhanVien;
	private JMenuItem mniCapNhatKhuyenMai;

	// MenuItem cho tìm kiếm
	private JMenuItem mniTimKiemThuoc;
	private JMenuItem mniTimKiemKhachHang;
	private JMenuItem mniTimKiemNhanVien;

	// MenuItem cho xử lí
	private JMenuItem mniLapHoaDon;
	private JMenuItem mniLapPhieuDatThuoc;
	private JMenuItem mniLapPhieuTraThuoc;

	// MenuItem cho thống kê
	private JMenuItem mniDoanhThu;
	private JMenuItem mniNhanVien;
	private JMenuItem mniKhachHang;
	private JMenuItem mniHanSuDung;
	private JMenuItem mniThue;
	private JMenuItem mniThuocDuocMuaNhieu;
	
    public MenuBarPanel(String tenNhanVien, Menu parentFrame) {
        this.tenNhanVien = tenNhanVien;
        this.parentFrame = parentFrame;
        initializeMenuBar();
    }

    private void initializeMenuBar() {
    	JPanel pnlMenu = new JPanel(new BorderLayout());
        JMenuBar mnuMenuBar = new JMenuBar();
        
        JLabel lblTenNhanVien = new JLabel("Tên nhân viên: Huynh Gia Man");
        lblTenNhanVien.setFont(new Font("Arial", Font.PLAIN, 15));
        
        JMenu mnuHeThong = new JMenu("Hệ thống");
        JMenu mnuDanhMuc = new JMenu("Danh mục");
        JMenu mnuCapNhat = new JMenu("Cập nhật");
        JMenu mnuTimKiem = new JMenu("Tìm kiếm");
        JMenu mnuXuLy = new JMenu("Xử lý");
        JMenu mnuThongKe = new JMenu("Thống kê");
        
        Font fntMenu = new Font("Arial", Font.PLAIN, 15);
        
        for (JMenu item : new JMenu[]{
        		mnuHeThong, mnuDanhMuc, mnuCapNhat,
        		mnuTimKiem, mnuXuLy, mnuThongKe
    	    }) {
    	        item.setFont(fntMenu);
    	    }

        mnuMenuBar.setPreferredSize(new Dimension(1900,50));
        mnuMenuBar.setBackground(new Color(240,250,240));

        // MenuItem cho hệ thống
        mniHoTro = new JMenuItem("Hỗ trợ");
        mniDangXuat = new JMenuItem("Đăng xuất");
        mniThoat = new JMenuItem("Thoát");

        // MenuItem cho danh mục
        mniHoaDon = new JMenuItem("Hóa đơn");
        mniPhieuDoiTra = new JMenuItem("Phiếu đổi trả");
        mniPhieuDatThuoc = new JMenuItem("Phiếu đặt thuốc");

        // MenuItem cho cập nhật
        mniCapNhatThuoc = new JMenuItem("Thuốc");
        mniCapNhatKhachHang = new JMenuItem("Khách hàng");
        mniCapNhatNhanVien = new JMenuItem("Nhân viên");
        mniCapNhatKhuyenMai = new JMenuItem("Khuyến mãi");

        // MenuItem cho tìm kiếm
        mniTimKiemThuoc = new JMenuItem("Thuốc");
        mniTimKiemKhachHang = new JMenuItem("Khách hàng");
        mniTimKiemNhanVien = new JMenuItem("Nhân viên");

        // MenuItem cho xử lí
        mniLapHoaDon = new JMenuItem("Lập hóa đơn");
        mniLapPhieuDatThuoc = new JMenuItem("Lập phiếu đặt thuốc");
        mniLapPhieuTraThuoc = new JMenuItem("Lập phiếu trả thuốc");
        
        // MenuItem cho thống kê
        mniDoanhThu = new JMenuItem("Doanh thu");
        mniNhanVien = new JMenuItem("Nhân viên");
        mniKhachHang = new JMenuItem("Khách hàng");
        mniHanSuDung = new JMenuItem("Hạn sử dụng");
        mniThue = new JMenuItem("Thuế");
        mniThuocDuocMuaNhieu = new JMenuItem("Thuốc mua nhiều");
            
        Font itemFont = new Font("Arial", Font.PLAIN, 14);

        for (JMenuItem item1 : new JMenuItem[]{
            mniHoTro, mniDangXuat, mniThoat,
            mniHoaDon, mniPhieuDoiTra, mniPhieuDatThuoc,
            mniCapNhatThuoc, mniCapNhatKhachHang, mniCapNhatNhanVien, mniCapNhatKhuyenMai,
            mniTimKiemThuoc, mniTimKiemKhachHang, mniTimKiemNhanVien,
            mniLapHoaDon, mniLapPhieuDatThuoc, mniLapPhieuTraThuoc,
            mniDoanhThu, mniNhanVien, mniKhachHang, mniHanSuDung, mniThue, mniThuocDuocMuaNhieu
        }) {
            item1.setFont(itemFont);
            item1.setBackground(getBackground());
            item1.setPreferredSize(new Dimension(180,40));
        }
        
        // Hệ thống
        mnuHeThong.add(mniHoTro);
        mnuHeThong.add(mniDangXuat);
        mnuHeThong.add(mniThoat);

        // Danh mục
        mnuDanhMuc.add(mniHoaDon);
        mnuDanhMuc.add(mniPhieuDoiTra);
        mnuDanhMuc.add(mniPhieuDatThuoc);

        // Cập nhật
        mnuCapNhat.add(mniCapNhatThuoc);
        mnuCapNhat.add(mniCapNhatKhachHang);
        mnuCapNhat.add(mniCapNhatNhanVien);
        mnuCapNhat.add(mniCapNhatKhuyenMai);

        // Tìm kiếm
        mnuTimKiem.add(mniTimKiemThuoc);
        mnuTimKiem.add(mniTimKiemKhachHang);
        mnuTimKiem.add(mniTimKiemNhanVien);

        // Xử lý
        mnuXuLy.add(mniLapHoaDon);
        mnuXuLy.add(mniLapPhieuDatThuoc);
        mnuXuLy.add(mniLapPhieuTraThuoc);
        
        // Thống kê
        mnuThongKe.add(mniDoanhThu);
        mnuThongKe.add(mniNhanVien);
        mnuThongKe.add(mniKhachHang);
        mnuThongKe.add(mniThue);
        mnuThongKe.add(mniHanSuDung);
        mnuThongKe.add(mniThuocDuocMuaNhieu);

        // Thêm vào Menubar chính
        mnuMenuBar.add(mnuHeThong);
        mnuMenuBar.add(mnuDanhMuc);
        mnuMenuBar.add(mnuCapNhat);
        mnuMenuBar.add(mnuTimKiem);
        mnuMenuBar.add(mnuXuLy);
        mnuMenuBar.add(mnuThongKe);
        //Tạo khoảng cách giữa mnubar và tên nhân viên
        mnuMenuBar.add(Box.createHorizontalGlue());
        
        JMenuItem[] menuItems = {
        	    mniHoTro, mniDangXuat, mniThoat,
        	    mniHoaDon, mniPhieuDoiTra, mniPhieuDatThuoc,
        	    mniCapNhatThuoc, mniCapNhatKhachHang, mniCapNhatNhanVien, mniCapNhatKhuyenMai,
        	    mniTimKiemThuoc, mniTimKiemKhachHang, mniTimKiemNhanVien,
        	    mniLapHoaDon, mniLapPhieuDatThuoc, mniLapPhieuTraThuoc,
        	    mniDoanhThu, mniNhanVien, mniKhachHang, mniHanSuDung, mniThue, mniThuocDuocMuaNhieu
        	};
    	for (JMenuItem item : menuItems) {
    	    item.addActionListener(this);
    	}

        mnuMenuBar.add(lblTenNhanVien);
        mnuMenuBar.add(Box.createHorizontalStrut(10));
        
        pnlMenu.add(mnuMenuBar);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if(o == mniLapHoaDon) {
			new LapHoaDonPanel();
		} else if (o == mniCapNhatKhachHang) {
		    parentFrame.showCapNhatKhachHangPanel();
		} else if (o == mniCapNhatNhanVien) {
		    parentFrame.showCapNhatNhanVienPanel();
		} else if (o == mniCapNhatThuoc) {
		    parentFrame.showCapNhatThuocPanel();
		} else if (o == mniCapNhatKhuyenMai) {
		    parentFrame.showCapNhatKhuyenMaiPanel();
		} else if (o == mniTimKiemThuoc) {
		    parentFrame.showTimKiemThuocPanel();
		} else if (o == mniTimKiemKhachHang) {
		    parentFrame.showTimKiemKhachHangPanel();
		} else if (o == mniTimKiemNhanVien) {
		    parentFrame.showTimKiemNhanVienPanel();
		} else if (o == mniLapHoaDon) {
		    parentFrame.showLapHoaDonPanel();
		} else if (o == mniLapPhieuDatThuoc) {
		    parentFrame.showLapPhieuDatThuocPanel();
		} else if (o == mniLapPhieuTraThuoc) {
		    parentFrame.showLapPhieuDoiThuocPanel();
		} else if (o == mniDoanhThu) {
		    parentFrame.showThongKeTheoDoanhThuPanel();
		} else if (o == mniNhanVien) {
		    parentFrame.showThongKeTheoNhanVienPanel();
		} else if (o == mniKhachHang) {
		    parentFrame.showThongKeTheoKhachHangPanel();
		} else if (o == mniHanSuDung) {
		    parentFrame.showThongKeTheoHSDPanel();
		} else if (o == mniThuocDuocMuaNhieu) {
		    parentFrame.showThongKeTheoThuocPanel();
		} else if (o == mniThue) {
		    parentFrame.showThongKeTheoThuePanel();
		}

		else if(o == mniDangXuat) { // Loi dispose
			CustomJOptionPane a = new CustomJOptionPane(this, "Bạn có chắc muốn đăng xuất?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) {
				Window window = SwingUtilities.getWindowAncestor(frmParent);
				window.dispose();
				new DangNhap();
			}
		}
		else if(o == mniThoat) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Bạn có chắc muốn tắt ứng dụng?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) System.exit(0);
		}
		else if(o == mniHoTro) {
			try {
				URL fileURL = App.class.getResource("/resources/pdf/HuongDanSuDung.pdf");
				if(fileURL != null) {
					File file = new File(fileURL.toURI());
					if(file.exists()) {
						if(Desktop.isDesktopSupported()) {
							Desktop.getDesktop().open(file);
						}
					}
					else {
						CustomJOptionPane a =  new CustomJOptionPane(this, "File PDF không tồn tại!", false);
						a.show();
					}
				}else {
					CustomJOptionPane a =  new CustomJOptionPane(this, "File PDF không tồn tại!", false);
					a.show();
				}
			} catch (Exception e1) {
				new CustomJOptionPane(this, "Gặp lỗi khi mở file PDF, mã lỗi:!" + e1.getMessage(), false);
			}
		}
		else if(o == mniDoanhThu) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Test jcustom", false);
			int choice = a.show();
			System.out.println(choice);
//			if(choice == JOptionPane.YES_OPTION) System.exit(0);
		}
	}
}
