package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.*;
import app.Main.App;

public class MenuBarPanel extends JPanel implements ActionListener {
    private String tenNhanVien;
    private MainFrame parentFrame;
	// MenuItem cho hệ thống
	private JMenuItem mniHoTro;
	private JMenuItem mniDangXuat;
	private JMenuItem mniThoat;

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
	
    public MenuBarPanel(String tenNhanVien, MainFrame parentFrame) {
        this.tenNhanVien = tenNhanVien;
        this.parentFrame = parentFrame;
        initializeMenuBar();
    }

    private void initializeMenuBar() {
        JMenuBar mnuMenuBar = new JMenuBar();
        
        JLabel lblTenNhanVien = new JLabel("Chào mừng:" + tenNhanVien);
        lblTenNhanVien.setFont(new Font("Arial", Font.PLAIN, 15));
        
        // Tạo các menu với icon
        JMenu mnuHeThong = new JMenu("Hệ thống");
        mnuHeThong.setIcon(loadIcon("/resources/icon/system_icon.png"));  
        
        JMenu mnuCapNhat = new JMenu("Danh mục");
        mnuCapNhat.setIcon(loadIcon("/resources/icon/task_icon.png"));  
        
        JMenu mnuTimKiem = new JMenu("Tìm kiếm");
        mnuTimKiem.setIcon(loadIcon("/resources/icon/magnifier_icon.png"));  
        
        JMenu mnuXuLy = new JMenu("Xử lý");
        mnuXuLy.setIcon(loadIcon("/resources/icon/update_icon.png"));  
        
        JMenu mnuThongKe = new JMenu("Thống kê");
        mnuThongKe.setIcon(loadIcon("/resources/icon/chart_icon.png"));  
        
        Font fntMenu = new Font("Arial", Font.PLAIN, 15);
        
        for (JMenu item : new JMenu[]{
        		mnuHeThong , mnuCapNhat,
        		mnuTimKiem, mnuXuLy, mnuThongKe
    	    }) {
    	        item.setFont(fntMenu);
    	    }

        this.setPreferredSize(new Dimension(0,50));
        mnuMenuBar.setBackground(new Color(240,250,240));

        // MenuItem cho hệ thống với icon
        mniHoTro = new JMenuItem("Hỗ trợ");
        mniHoTro.setIcon(loadIcon("/resources/icon/support_icon.png"));  
        
        mniDangXuat = new JMenuItem("Đăng xuất");
        mniDangXuat.setIcon(loadIcon("/resources/icon/logout_icon.png"));  
        
        mniThoat = new JMenuItem("Thoát");
        mniThoat.setIcon(loadIcon("/resources/icon/poweroff_icon.png"));  

        // MenuItem cho cập nhật với icon
        mniCapNhatThuoc = new JMenuItem("Thuốc");
        mniCapNhatThuoc.setIcon(loadIcon("/resources/icon/drug_icon.png"));  
        
        mniCapNhatKhachHang = new JMenuItem("Khách hàng");
        mniCapNhatKhachHang.setIcon(loadIcon("/resources/icon/customer_icon.png"));  
        
        mniCapNhatNhanVien = new JMenuItem("Nhân viên");
        mniCapNhatNhanVien.setIcon(loadIcon("/resources/icon/employee_icon.png"));  
        
        mniCapNhatKhuyenMai = new JMenuItem("Khuyến mãi");
        mniCapNhatKhuyenMai.setIcon(loadIcon("/resources/icon/sale_icon.png")); 

        // MenuItem cho tìm kiếm với icon
        mniTimKiemThuoc = new JMenuItem("Thuốc");
        mniTimKiemThuoc.setIcon(loadIcon("/resources/icon/drug_icon.png")); 
        
        mniTimKiemKhachHang = new JMenuItem("Khách hàng");
        mniTimKiemKhachHang.setIcon(loadIcon("/resources/icon/customer_icon.png"));  
        
        mniTimKiemNhanVien = new JMenuItem("Nhân viên");
        mniTimKiemNhanVien.setIcon(loadIcon("/resources/icon/employee_icon.png"));  

        // MenuItem cho xử lí với icon
        mniLapHoaDon = new JMenuItem("Lập hóa đơn");
        mniLapHoaDon.setIcon(loadIcon("/resources/icon/bill_icon.png"));
        
        mniLapPhieuDatThuoc = new JMenuItem("Lập phiếu đặt thuốc");
        mniLapPhieuDatThuoc.setIcon(loadIcon("/resources/icon/order_icon.png"));  
        
        mniLapPhieuTraThuoc = new JMenuItem("Lập phiếu trả thuốc");
        mniLapPhieuTraThuoc.setIcon(loadIcon("/resources/icon/refund_icon.png"));  
        
        // MenuItem cho thống kê với icon
        mniDoanhThu = new JMenuItem("Doanh thu nhà thuốc");
        mniDoanhThu.setIcon(loadIcon("/resources/icon/revenue_icon.png"));  
        
        mniNhanVien = new JMenuItem("Doanh thu của NV");
        mniNhanVien.setIcon(loadIcon("/resources/icon/employee_revenue_icon.png"));  
        
        mniKhachHang = new JMenuItem("Doanh thu của KH");
        mniKhachHang.setIcon(loadIcon("/resources/icon/customer_revenue_icon.png"));  
        
        mniHanSuDung = new JMenuItem("Hạn sử dụng");
        mniHanSuDung.setIcon(loadIcon("/resources/icon/refund_icon.png"));  
        
        mniThue = new JMenuItem("Thuế");
        mniThue.setIcon(loadIcon("/resources/icon/refund_icon.png"));  
        
        mniThuocDuocMuaNhieu = new JMenuItem("Thuốc mua nhiều");
        mniThuocDuocMuaNhieu.setIcon(loadIcon("/resources/icon/refund_icon.png"));  
            
        Font itemFont = new Font("Arial", Font.PLAIN, 14);

        for (JMenuItem item1 : new JMenuItem[]{
            mniHoTro, mniDangXuat, mniThoat,
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
        mnuMenuBar.add(mnuCapNhat);
        mnuMenuBar.add(mnuTimKiem);
        mnuMenuBar.add(mnuXuLy);
        mnuMenuBar.add(mnuThongKe);
        //Tạo khoảng cách giữa mnubar và tên nhân viên
        mnuMenuBar.add(Box.createHorizontalGlue());
        
        JMenuItem[] menuItems = {
        	    mniHoTro, mniDangXuat, mniThoat,
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
		
		// Tìm kiếm
		else if (o == mniTimKiemThuoc) parentFrame.showTimKiemThuocPanel();

		else if (o == mniTimKiemKhachHang) parentFrame.showTimKiemKhachHangPanel();

		else if (o == mniTimKiemNhanVien) parentFrame.showTimKiemNhanVienPanel();
		
		// Xử lý
		else if (o == mniLapHoaDon) parentFrame.showLapHoaDonPanel();

		else if (o == mniLapPhieuDatThuoc) parentFrame.showLapPhieuDatThuocPanel();

		else if (o == mniLapPhieuTraThuoc) parentFrame.showLapPhieuDoiThuocPanel();
		
		// Thống kê
		else if (o == mniDoanhThu) parentFrame.showThongKeTheoDoanhThuPanel();

		else if (o == mniNhanVien) parentFrame.showThongKeTheoNhanVienPanel();

		else if (o == mniKhachHang) parentFrame.showThongKeTheoKhachHangPanel();

		else if (o == mniHanSuDung) parentFrame.showThongKeTheoHSDPanel();

		else if (o == mniThuocDuocMuaNhieu) parentFrame.showThongKeTheoThuocPanel();

		else if (o == mniThue) parentFrame.showThongKeTheoThuePanel();
		
		// Hệ thống
		else if(o == mniDangXuat) {
			CustomJOptionPane a = new CustomJOptionPane(parentFrame, "Bạn có chắc muốn đăng xuất?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) {
				parentFrame.dangXuatHandle();
			}
		}
		else if(o == mniThoat) {
			CustomJOptionPane a = new CustomJOptionPane(parentFrame, "Bạn có chắc muốn tắt ứng dụng?", true);
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