package app.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuBarPanel extends JPanel implements ActionListener {
    private String tenNhanVien;
    private Menu parentFrame;
    private JMenuItem timKiemThuocItem;
    private JMenuItem timKiemKhachHangItem;
    private JMenuItem timNhanVienItem;
    private JMenuItem lapHoaDonItem;
    private JMenuItem lapPhieuDatThuocItem;
    private JMenuItem lapPhieuDoiThuocItem;
    private JMenuItem capNhatKhachHangItem;
    private JMenuItem capNhatNhanVienItem;
    private JMenuItem capNhatThuocItem;
    private JMenuItem capNhatKhuyenMaiItem;
    private JMenuItem thongKeTheoDoanhThu;
    private JMenuItem thongKeTheoNhanVien;
    private JMenuItem thongKeTheoKhachHang;
    private JMenuItem thongKeTheoHanSuDung;
    private JMenuItem thongKeTheoThuoc;
    private JMenuItem thongKeTheoThue;
    public MenuBarPanel(String tenNhanVien, Menu parentFrame) {
        this.tenNhanVien = tenNhanVien;
        this.parentFrame = parentFrame;
        initializeMenuBar();
    }
    
    private void initializeMenuBar() {
        setLayout(new BorderLayout());
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(0, 40));
        
        // Tạo các menu chính
        JMenu menuHeThong = new JMenu("Hệ thống");
        JMenu menuDanhMuc = new JMenu("Danh mục");
        JMenu menuCapNhat = new JMenu("Cập nhật");
        JMenu menuTimKiem = new JMenu("Tìm kiếm");
        JMenu menuXuLi = new JMenu("Xử lý");
        JMenu menuThongKe = new JMenu("Thống kê");
        
        // Menu Hệ thống
        JMenuItem dangXuatItem = new JMenuItem("Đăng xuất");
        JMenuItem thoatItem = new JMenuItem("Thoát");
        dangXuatItem.addActionListener(this);
        thoatItem.addActionListener(this);
        menuHeThong.add(dangXuatItem);
        menuHeThong.addSeparator();
        menuHeThong.add(thoatItem);
        // Menu Danh mục
        // Menu Cập nhật
        capNhatKhachHangItem = new JMenuItem("Khách hàng");
        capNhatKhachHangItem.addActionListener(this);
        capNhatNhanVienItem = new JMenuItem("Nhân viên");
        capNhatNhanVienItem.addActionListener(this);
        capNhatThuocItem = new JMenuItem("Thuốc");
        capNhatThuocItem.addActionListener(this);
        capNhatKhuyenMaiItem = new JMenuItem("Khuyến mãi");
        capNhatKhuyenMaiItem.addActionListener(this);
        menuCapNhat.add(capNhatKhachHangItem);
        menuCapNhat.add(capNhatNhanVienItem);
        menuCapNhat.add(capNhatThuocItem);
        menuCapNhat.add(capNhatKhuyenMaiItem);
        // Menu Tìm kiếm
        timKiemThuocItem = new JMenuItem("Thuốc");
        timKiemThuocItem.addActionListener(this);
        timKiemKhachHangItem = new JMenuItem("Khách hàng");
        timKiemKhachHangItem.addActionListener(this);
        timNhanVienItem = new JMenuItem("Nhân viên");
        timNhanVienItem.addActionListener(this);
        menuTimKiem.add(timKiemThuocItem);
        menuTimKiem.add(timKiemKhachHangItem);
        menuTimKiem.add(timNhanVienItem);

        // Menu Xử lý
        lapHoaDonItem = new JMenuItem("Lập hóa đơn");
        lapHoaDonItem.addActionListener(this);
        lapPhieuDatThuocItem = new JMenuItem("Lập phiếu đặt thuốc");
        lapPhieuDatThuocItem.addActionListener(this);
        lapPhieuDoiThuocItem = new JMenuItem("Lập phiếu đổi thuốc");
        lapPhieuDoiThuocItem.addActionListener(this);
        menuXuLi.add(lapHoaDonItem);
        menuXuLi.add(lapPhieuDatThuocItem);
        menuXuLi.add(lapPhieuDoiThuocItem);
        // Menu Thống kê
        thongKeTheoDoanhThu = new JMenuItem("Thống kê theo doanh thu");
        thongKeTheoDoanhThu.addActionListener(this);
        thongKeTheoNhanVien = new JMenuItem("Thống kê theo nhân viên");
        thongKeTheoNhanVien.addActionListener(this);
        thongKeTheoKhachHang = new JMenuItem("Thống kê theo khách hàng");
        thongKeTheoKhachHang.addActionListener(this);
        thongKeTheoHanSuDung = new JMenuItem("Thống kê theo hạn sử dụng");
        thongKeTheoHanSuDung.addActionListener(this);
        thongKeTheoThuoc = new JMenuItem("Thống kê theo thuốc");
        thongKeTheoThuoc.addActionListener(this);
        thongKeTheoThue = new JMenuItem("Thống kê theo thuế");
        thongKeTheoThue.addActionListener(this);
        menuThongKe.add(thongKeTheoDoanhThu);
        menuThongKe.add(thongKeTheoNhanVien);
        menuThongKe.add(thongKeTheoKhachHang);
        menuThongKe.add(thongKeTheoHanSuDung);
        menuThongKe.add(thongKeTheoThuoc);
        menuThongKe.add(thongKeTheoThue);
        // Placeholder items cho các menu khác
        JMenuItem placeholderItem1 = new JMenuItem("Đang phát triển...");
        
        placeholderItem1.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));

        menuDanhMuc.add(placeholderItem1);
        
        // Thêm các menu vào menu bar
        menuBar.add(menuHeThong);
        menuBar.add(menuDanhMuc);
        menuBar.add(menuCapNhat);
        menuBar.add(menuTimKiem);
        menuBar.add(menuXuLi);
        menuBar.add(menuThongKe);
        
        // Thêm khoảng trống và tên nhân viên
        menuBar.add(Box.createHorizontalGlue());
        JLabel tenNVLabel = new JLabel("Nhân viên: " + tenNhanVien);
        tenNVLabel.setFont(new Font("Arial", Font.BOLD, 12));
        tenNVLabel.setForeground(Color.BLUE);
        menuBar.add(tenNVLabel);
        menuBar.add(Box.createHorizontalStrut(10));
        
        add(menuBar, BorderLayout.CENTER);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == timKiemThuocItem) {
            parentFrame.showTimKiemThuocPanel();
        } else if (source == lapHoaDonItem) {
            parentFrame.showLapHoaDonPanel();
        } else if (source.equals(getMenuItemByText("Đăng xuất"))) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?", 
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                parentFrame.dispose();
                new DangNhap();
            }
        } else if (source.equals(getMenuItemByText("Thoát"))) {
            int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thoát ứng dụng?", 
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if(source == capNhatKhachHangItem) {
            parentFrame.showCapNhatKhachHangPanel();
        } else if(source == capNhatNhanVienItem) {
            parentFrame.showCapNhatNhanVienPanel();
        } else if(source == capNhatThuocItem) {
            parentFrame.showCapNhatThuocPanel();
        } else if(source == capNhatKhuyenMaiItem) {
            parentFrame.showCapNhatKhuyenMaiPanel();
        } else if(source == timKiemKhachHangItem) {
            parentFrame.showTimKiemKhachHangPanel();
        } else if (source == timNhanVienItem) {
            parentFrame.showTimKiemNhanVienPanel();
        } else if (source == lapPhieuDatThuocItem) {
            parentFrame.showLapPhieuDatThuocPanel();
        } else if (source == lapPhieuDoiThuocItem) {
            parentFrame.showLapPhieuDoiThuocPanel();
        } else if(source == thongKeTheoDoanhThu) {
            parentFrame.showThongKeTheoDoanhThuPanel();
        } else if (source == thongKeTheoNhanVien) {
            parentFrame.showThongKeTheoNhanVienPanel();
        } else if (source == thongKeTheoKhachHang) {
            parentFrame.showThongKeTheoKhachHangPanel();
        } else if (source == thongKeTheoHanSuDung) {
            parentFrame.showThongKeTheoHSDPanel();
        } else if (source == thongKeTheoThuoc) {
            parentFrame.showThongKeTheoThuocPanel();
        } else if (source == thongKeTheoThue) {
            parentFrame.showThongKeTheoThuePanel();
        }
            
    }
    
    private JMenuItem getMenuItemByText(String text) {
        Component[] components = ((JMenuBar) getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null && text.equals(item.getText())) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}