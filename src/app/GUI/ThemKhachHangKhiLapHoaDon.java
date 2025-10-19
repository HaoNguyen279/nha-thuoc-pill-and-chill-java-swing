package app.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;

public class ThemKhachHangKhiLapHoaDon extends JFrame implements ActionListener {
    
    private JTextField txtTenKhachHang, txtSDTKhachHang;
    private JButton btnThem, btnHuy;
    private String sdt;
    private KhachHangDAO khachHangDAO;
    
    public ThemKhachHangKhiLapHoaDon(String sdt) {
        this.sdt = sdt;
        khachHangDAO = new KhachHangDAO();
        
        setTitle("Thêm Khách Hàng Mới");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Tạo giao diện trước
        createGUI();
        
        // Đặt kích thước frame rõ ràng
        setSize(500, 150);
        setMinimumSize(new Dimension(500, 150));
        setResizable(true);
        
        // Đặt vị trí của cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);
        
        // Hiển thị giao diện ngay lập tức
        setVisible(true);
        
        // Kiểm tra SĐT sau khi hiển thị UI để không block giao diện
        checkExistingPhone();
        
        // Đảm bảo focus vào trường tên
        txtTenKhachHang.requestFocusInWindow();
    }
    
    private void checkExistingPhone() {
        try {
            // Kiểm tra trước khi kết nối để tránh việc block UI
            SwingWorker<KhachHang, Void> worker = new SwingWorker<KhachHang, Void>() {
                @Override
                protected KhachHang doInBackground() throws Exception {
                    // Đảm bảo có kết nối database mới trước khi kiểm tra
                    app.ConnectDB.ConnectDB.connect();
                    
                    // Tìm khách hàng theo SDT
                    return khachHangDAO.findKhachHangByPhone(sdt);
                }
                
                @Override
                protected void done() {
                    try {
                        KhachHang existingKH = get();
                        if (existingKH != null) {
                            // Nếu tìm thấy khách hàng, hiển thị thông báo và đóng frame
                            JOptionPane.showMessageDialog(ThemKhachHangKhiLapHoaDon.this,
                                "Số điện thoại này đã được đăng ký!\n" +
                                "Khách hàng: " + existingKH.getTenKH() + "\n" +
                                "Mã KH: " + existingKH.getMaKH(),
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            worker.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createGUI() {
        // Thiết lập layout chính cho form
        setLayout(new BorderLayout());
        
        // Panel chính với khoảng cách xung quanh lớn hơn
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        
        // Panel tiêu đề ở trên cùng
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("THÊM KHÁCH HÀNG MỚI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(70, 70, 70));
        titlePanel.add(lblTitle);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Panel cho các trường nhập liệu ở giữa - sử dụng GridBagLayout
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Tạo các trường một cách đơn giản
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Số điện thoại
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fieldsPanel.add(lblSDT, gbc);
        
        txtSDTKhachHang = new JTextField(sdt);
        txtSDTKhachHang.setFont(fieldFont);
        txtSDTKhachHang.setEditable(false);
        txtSDTKhachHang.setPreferredSize(new Dimension(300, 30));
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0;
        fieldsPanel.add(txtSDTKhachHang, gbc);
        
        // Tên khách hàng  
        JLabel lblTen = new JLabel("Tên khách hàng:");
        lblTen.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        gbc.insets = new Insets(20, 8, 8, 8); // Khoảng cách lớn hơn từ trường trên
        fieldsPanel.add(lblTen, gbc);
        
        txtTenKhachHang = new JTextField();
        txtTenKhachHang.setFont(fieldFont);
        txtTenKhachHang.setPreferredSize(new Dimension(300, 30));
        txtTenKhachHang.setMinimumSize(new Dimension(300, 30));
        // Thêm border để debug
        txtTenKhachHang.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 8, 15, 8); // Thêm khoảng cách dưới
        fieldsPanel.add(txtTenKhachHang, gbc);
        
        // Panel cho các nút ở dưới
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        
        // Nút Thêm
        btnThem = new JButton("Thêm");
        btnThem.setPreferredSize(new Dimension(120, 40));
        btnThem.setBackground(new Color(76, 175, 80));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThem.setFocusPainted(false);
        btnThem.setBorder(BorderFactory.createLineBorder(new Color(56, 142, 60)));
        btnThem.addActionListener(this);
        
        // Nút Hủy
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(120, 40));
        btnHuy.setBackground(new Color(240, 240, 240));
        btnHuy.setForeground(Color.BLACK);
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnHuy.setFocusPainted(false);
        btnHuy.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnHuy.addActionListener(this);
        
        // Thêm các nút vào panel nút
        buttonsPanel.add(btnThem);
        buttonsPanel.add(btnHuy);
        
        // Ghép các panel vào panel chính
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        // Thêm panel chính vào frame
        add(mainPanel);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o == btnThem) {
            themKhachHang();
        } else if (o == btnHuy) {
            dispose();
        }
    }
    
    private void themKhachHang() {
        // Validate input
        String tenKH = txtTenKhachHang.getText().trim();
        String sdtKH = txtSDTKhachHang.getText().trim();

        // Kiểm tra nếu tên trống
        if (tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên khách hàng!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            txtTenKhachHang.requestFocus();
            return;
        }

        // Validate phone number format
        if (!sdtKH.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải có 10 chữ số!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Generate mã khách hàng tự động (KHXXX)
        String maKH = khachHangDAO.generateMaKhachHang();

        // Create new KhachHang object
        KhachHang kh = new KhachHang(maKH, tenKH, sdtKH, 0, true);

        // Save to database
        boolean success = khachHangDAO.addKhachHang(kh);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Thêm khách hàng thành công!\nMã khách hàng: " + maKH,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm khách hàng thất bại!\nVui lòng thử lại.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
