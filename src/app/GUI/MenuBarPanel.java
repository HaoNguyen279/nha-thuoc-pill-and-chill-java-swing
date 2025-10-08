package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuBarPanel extends JPanel {
	
    public MenuBarPanel() {
		super();
	}

	public JPanel get() {
    	JPanel pnlMenu = new JPanel(new BorderLayout());
        JMenuBar mnuMenuBar = new JMenuBar();
        
        JLabel ten = new JLabel("Tên nhân viên: Huynh Gia Man");
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
//    	        item.setMargin(new Insets(50, 20, 20, 20)); // trên, trái, dưới, phải
//    	        item.setPreferredSize(new Dimension(180,40));
    	    }

        mnuMenuBar.setPreferredSize(new Dimension(1900,50));
        mnuMenuBar.setBackground(new Color(240,250,240));


        
        
        // MenuItem cho hệ thống
        JMenuItem mniHoTro = new JMenuItem("Hỗ trợ");
        JMenuItem mniDangXuat = new JMenuItem("Đăng xuất");
        JMenuItem mniThoat = new JMenuItem("Thoát");
        
        // MenuItem cho danh mục
        JMenuItem mniHoaDon = new JMenuItem("Hóa đơn");
        JMenuItem mniPhieuDoiTra = new JMenuItem("Phiếu đổi trả");
        JMenuItem mniPhieuDatThuoc = new JMenuItem("Phiếu đặt thuốc");
        
        // MenuItem cho cập nhật
        JMenuItem mniCapNhatThuoc = new JMenuItem("Thuốc");
        JMenuItem mniCapNhatKhachHang = new JMenuItem("Khách hàng");
        JMenuItem mniCapNhatNhanVien = new JMenuItem("Nhân viên");
        JMenuItem mniCapNhatKhuyenMai = new JMenuItem("Khuyến mãi");

        // MenuItem cho tìm kiếm
        JMenuItem mniTimKiemThuoc = new JMenuItem("Thuốc");
        JMenuItem mniTimKiemKhachHang = new JMenuItem("Khách hàng");
        JMenuItem mniTimKiemNhanVien = new JMenuItem("Nhân viên");

        // MenuItem cho xử lí	
        JMenuItem mniLapHoaDon = new JMenuItem("Lập hóa đơn");
        JMenuItem mniLapPhieuDatThuoc = new JMenuItem("Lập phiếu đặt thuốc");
        JMenuItem mniLapPhieuTraThuoc = new JMenuItem("Lập phiếu trả thuốc");
        
        // MenuItem cho thống kê
        JMenuItem mniDoanhThu = new JMenuItem("Doanh thu");
        JMenuItem mniNhanVien = new JMenuItem("Nhân viên");
        JMenuItem mniKhachHang = new JMenuItem("Khách hàng");
        JMenuItem mniHanSuDung = new JMenuItem("Khách hàng");
        JMenuItem mniThue = new JMenuItem("Khách hàng");
        JMenuItem mniThuocDuocMuaNhieu = new JMenuItem("Thuốc mua nhiều");
            
       
       
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
//            item.setMargin(new Insets(50, 20, 20, 20)); // trên, trái, dưới, phải
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
        
        
        mnuMenuBar.add(ten);
        mnuMenuBar.add(Box.createHorizontalStrut(10));
        
        pnlMenu.add(mnuMenuBar);
        return pnlMenu;
    }

}
