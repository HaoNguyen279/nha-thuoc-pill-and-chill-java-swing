package app.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuBarPanel extends JPanel implements ActionListener {
    private String tenNhanVien;
    private Menu parentFrame;
    private JMenuItem timKiemThuocItem;
    private JMenuItem lapHoaDonItem;
    
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
        
        // Menu Tìm kiếm
        timKiemThuocItem = new JMenuItem("Thuốc");
        timKiemThuocItem.addActionListener(this);
        menuTimKiem.add(timKiemThuocItem);
        
        // Menu Xử lý
        lapHoaDonItem = new JMenuItem("Lập hóa đơn");
        lapHoaDonItem.addActionListener(this);
        menuXuLi.add(lapHoaDonItem);
        
        // Placeholder items cho các menu khác
        JMenuItem placeholderItem1 = new JMenuItem("Đang phát triển...");
        JMenuItem placeholderItem2 = new JMenuItem("Đang phát triển...");
        JMenuItem placeholderItem3 = new JMenuItem("Đang phát triển...");
        
        placeholderItem1.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
        placeholderItem2.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
        placeholderItem3.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Chức năng đang được phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE));
            
        menuDanhMuc.add(placeholderItem1);
        menuCapNhat.add(placeholderItem2);
        menuThongKe.add(placeholderItem3);
        
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
