package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class MenuBarPanel extends JPanel implements ActionListener {
	
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
	
    public MenuBarPanel(JFrame parent) {
    	this.frmParent = parent;
	}

	public JPanel get() {
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
        return pnlMenu;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if(o == mniLapHoaDon) {
			new LapHoaDon();
		}
		else if(o == mniCapNhatKhachHang) {
			new CapNhatKhachHangGUI();
		}
		else if(o == mniLapPhieuTraThuoc) {
			new LapPhieuDoiThuoc();
		}
		else if(o == mniTimKiemThuoc) {
			new TimKiemThuoc();
		}
		else if(o == mniDangXuat) { // Loi dispose
			CustomJOptionPane a = new CustomJOptionPane(this, "Bạn có chắc muốn đăng xuất?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) {
				Window window = SwingUtilities.getWindowAncestor(frmParent);
				window.dispose();
				new LoginGUI();
			}
		}
		else if(o == mniThoat) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Bạn có chắc muốn tắt ứng dụng?", true);
			int choice = a.show();
			if(choice == JOptionPane.YES_OPTION) System.exit(0);
		}
	}
}
