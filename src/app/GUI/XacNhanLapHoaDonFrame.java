package app.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import app.DAO.KhachHangDAO;
import app.DAO.HoaDonDAO;
import app.DAO.NhanVienDAO;
import app.DAO.TonKhoDAO;
import app.Entity.KhachHang;
import app.Entity.NhanVien;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Import all iTextPDF classes
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;



public class XacNhanLapHoaDonFrame extends JFrame implements ActionListener {
    
    private ArrayList<Object[]> dsChiTietData; // [maThuoc, tenThuoc, soLuong, donGia]
    private double tongTien;
    private String maHoaDon;
    private double tongThanhToan;
    
    // Callback khi lập hóa đơn thành công
    private HoaDonCallback hoaDonCallback;
    
    // Components
    private JButton btnQuayVe, btnTim, btnXacNhan;
    private JTextField txtSDTKhachHang, txtTenKhachHang, txtMaKhuyenMai;
    private JTextField txtNhanVienLap, txtNgayMua;
    private JTextArea txtGhiChu;
    private JTextField txtTongTien, txtThue, txtTienGiam, txtTongCong, txtTienNhan, txtTienThua;
    
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;
    
    private String maNhanVienLap; // Store employee ID for later use
    
    public XacNhanLapHoaDonFrame(ArrayList<Object[]> dsChiTietData, double tongTien, String maHoaDon, String maNhanVien) {
        this.dsChiTietData = dsChiTietData;
        this.tongTien = tongTien;
        this.maHoaDon = maHoaDon;
        this.maNhanVienLap = maNhanVien; // Store employee ID
        
        setTitle("Xác Nhận Lập Hóa Đơn - " + maHoaDon);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createGUI();
        loadChiTietHoaDon();
        
        setVisible(true);
    }
    
    // Constructor với callback
    public XacNhanLapHoaDonFrame(ArrayList<Object[]> dsChiTietData, double tongTien, String maHoaDon, 
                                String maNhanVien, HoaDonCallback callback) {
        this(dsChiTietData, tongTien, maHoaDon, maNhanVien);
        this.hoaDonCallback = callback;
    }
    
    // Constructor với LapHoaDonPanel - giữ lại để tương thích với code cũ
    public XacNhanLapHoaDonFrame(ArrayList<Object[]> dsChiTietData, double tongTien, String maHoaDon, 
                                String maNhanVien, LapHoaDonPanel parentPanel) {
        this(dsChiTietData, tongTien, maHoaDon, maNhanVien);
        // Đặt callback từ parentPanel vì LapHoaDonPanel implement HoaDonCallback
        this.hoaDonCallback = parentPanel;
    }
    
    private void createGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel: Tiêu đề, nút quay về và bảng chi tiết
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel: Form fields (2 columns)
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel: Nút xác nhận
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel: Nút quay về và tiêu đề
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Nút quay về
        btnQuayVe = new JButton("← Quay về");
        btnQuayVe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnQuayVe.setBackground(new Color(240, 240, 240));
        btnQuayVe.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnQuayVe.setFocusPainted(false);
        btnQuayVe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuayVe.addActionListener(this);
        
        // Tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMaHoaDon = new JLabel("Mã hóa đơn: " + maHoaDon);
        lblMaHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMaHoaDon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblMaHoaDon);
        
        headerPanel.add(btnQuayVe, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Table panel: Bảng chi tiết hóa đơn
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Tạo bảng chi tiết hóa đơn
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChiTiet.setRowHeight(30);
        tblChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblChiTiet.getTableHeader().setBackground(new Color(240, 250, 240));
        tblChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Căn phải cho các cột số
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.CENTER); }
        });
        tblChiTiet.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
        });
        tblChiTiet.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblChiTiet);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 40, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Left panel: Thông tin khách hàng và hóa đơn
        JPanel leftPanel = createLeftInfoPanel();
        
        // Right panel: Thông tin thanh toán
        JPanel rightPanel = createRightInfoPanel();
        
        panel.add(leftPanel);
        panel.add(rightPanel);
        
        return panel;
    }
    
    private JPanel createLeftInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // SDT khách hàng (với nút Tìm)
        panel.add(createInfoRow("SDT khách hàng:", labelFont));
        JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
        sdtPanel.setBackground(Color.WHITE);
        sdtPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtSDTKhachHang = createTextField(fieldFont);
        
        // Thêm DocumentFilter để chỉ cho phép nhập số và giới hạn 10 ký tự
        ((javax.swing.text.AbstractDocument) txtSDTKhachHang.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string != null && string.matches("[0-9]*") && (fb.getDocument().getLength() + string.length()) <= 10) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text != null && text.matches("[0-9]*") && (fb.getDocument().getLength() - length + text.length()) <= 10) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTim.setBackground(new Color(76, 175, 80));
        btnTim.setForeground(Color.WHITE);
        btnTim.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        sdtPanel.add(txtSDTKhachHang, BorderLayout.CENTER);
        sdtPanel.add(btnTim, BorderLayout.EAST);
        panel.add(sdtPanel);
         
        
        // Tên khách hàng
        panel.add(createInfoRow("Tên khách hàng:", labelFont));
        txtTenKhachHang = createTextField(fieldFont);
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(new Color(220, 220, 220)); // Màu xám nhạt để thể hiện không editable
        txtTenKhachHang.setFocusable(false); // Không thể focus vào
        panel.add(txtTenKhachHang);
         
        
        // Mã khuyến mãi
        panel.add(createInfoRow("Mã khuyến mãi:", labelFont));
        txtMaKhuyenMai = createTextField(fieldFont);
        panel.add(txtMaKhuyenMai);
         
        
        // Nhân viên lập
        panel.add(createInfoRow("Nhân viên lập:", labelFont));
        txtNhanVienLap = createTextField(fieldFont);
        txtNhanVienLap.setEditable(false);
        // Set the employee ID from the stored value
        if (maNhanVienLap != null && !maNhanVienLap.isEmpty()) {
            txtNhanVienLap.setText(maNhanVienLap);
        }
        panel.add(txtNhanVienLap);
         
        
        // Ngày mua
        panel.add(createInfoRow("Ngày mua:", labelFont));
        txtNgayMua = createTextField(fieldFont);
        txtNgayMua.setEditable(false);
        txtNgayMua.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
        panel.add(txtNgayMua);
         
        
        // Ghi chú
        panel.add(createInfoRow("Ghi chú:", labelFont));
        txtGhiChu = new JTextArea(3, 20);
        txtGhiChu.setFont(fieldFont);
        txtGhiChu.setBackground(new Color(245, 245, 245));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setWrapStyleWord(true);
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(scrollGhiChu);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createRightInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        DecimalFormat df = new DecimalFormat("#,###");
        
        // Tổng tiền
        panel.add(createInfoRow("Tổng tiền:", labelFont));
        txtTongTien = createTextField(fieldFont);
        txtTongTien.setEditable(false);
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setText(df.format(tongTien) + " VNĐ");
        panel.add(txtTongTien);
        
        // Sử dụng mức thuế mặc định là 10%
        double tyLeThue = 0.10;
        String tenThue = "Thuế (10%)";
        double tienThue = tongTien * tyLeThue;
        
        panel.add(createInfoRow(tenThue + ":", labelFont));
        txtThue = createTextField(fieldFont);
        txtThue.setEditable(false);
        txtThue.setHorizontalAlignment(JTextField.RIGHT);
        txtThue.setText(df.format(tienThue) + " VNĐ");
        panel.add(txtThue);
         
        
        // Tiền giảm
        panel.add(createInfoRow("Tiền giảm:", labelFont));
        txtTienGiam = createTextField(fieldFont);
        txtTienGiam.setEditable(false);
        txtTienGiam.setHorizontalAlignment(JTextField.RIGHT);
        txtTienGiam.setText("0 VNĐ");
        panel.add(txtTienGiam);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Tính tổng thanh toán
        tongThanhToan = tongTien + tienThue;
        
        // Tổng cộng (Tổng tiền + Thuế - Tiền giảm)
        panel.add(createInfoRow("Tổng cộng:", labelFont));
        txtTongCong = createTextField(fieldFont);
        txtTongCong.setEditable(false);
        txtTongCong.setHorizontalAlignment(JTextField.RIGHT);
        txtTongCong.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Font đậm để nổi bật
        txtTongCong.setText(df.format(tongThanhToan) + " VNĐ");
        panel.add(txtTongCong);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Tiền nhận
        panel.add(createInfoRow("Tiền nhận:", labelFont));
        txtTienNhan = createTextField(fieldFont);
        txtTienNhan.setHorizontalAlignment(JTextField.RIGHT);
        // Thêm KeyListener để tính tiền thừa tự động
        txtTienNhan.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                XacNhanLapHoaDonFrame.this.tinhTienThua();
            }
        });
        panel.add(txtTienNhan);
         
        
        // Tiền thừa
        panel.add(createInfoRow("Tiền thừa:", labelFont));
        txtTienThua = createTextField(fieldFont);
        txtTienThua.setEditable(false);
        txtTienThua.setHorizontalAlignment(JTextField.RIGHT);
        txtTienThua.setText("0 VNĐ");
        panel.add(txtTienThua);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createInfoRow(String text, Font font) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel label = new JLabel(text);
        label.setFont(font);
        rowPanel.add(label);
        
        return rowPanel;
    }
    
    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setPreferredSize(new Dimension(0, 35));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        textField.setBackground(new Color(245, 245, 245));
        return textField;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        panel.setPreferredSize(new Dimension(0, 80)); // Độ cao tương tự LapHoaDonPanel

        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new Font("Arial", Font.PLAIN, 14));
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        btnXacNhan.setBackground(new Color(240, 240, 240));
        btnXacNhan.setForeground(Color.BLACK);
        btnXacNhan.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.addActionListener(this);
        
        panel.add(btnXacNhan);
        
        return panel;
    }
    
    private void loadChiTietHoaDon() {
        modelChiTiet.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for (Object[] data : dsChiTietData) {
            String maThuoc = (String) data[0];
            String tenThuoc = (String) data[1];
            int soLuong = (Integer) data[2];
            float donGia = (Float) data[3];
            
            Object[] row = {
                maThuoc,
                tenThuoc,
                soLuong,
                df.format(donGia) + " VNĐ",
                df.format(donGia * soLuong) + " VNĐ"
            };
            modelChiTiet.addRow(row);
        }
    }
    
    // Tính tiền thừa khi nhập tiền nhận
    private void tinhTienThua() {
        try {
            String tienNhanStr = txtTienNhan.getText().trim();
            if (tienNhanStr.isEmpty()) {
                txtTienThua.setText("0 VNĐ");
                return;
            }
            
            // Loại bỏ dấu phẩy và "VNĐ" nếu có
            tienNhanStr = tienNhanStr.replace(",", "").replace(" VNĐ", "").replace("VNĐ", "").trim();
            double tienNhan = Double.parseDouble(tienNhanStr);
            
            // Tính tiền thừa = tiền nhận - tổng thanh toán
            double tienThua = tienNhan - tongThanhToan;
            
            DecimalFormat df = new DecimalFormat("#,###");
            if (tienThua < 0) {
                txtTienThua.setText("0 VNĐ");
            } else {
                txtTienThua.setText(df.format(tienThua) + " VNĐ");
            }
        } catch (NumberFormatException ex) {
            txtTienThua.setText("0 VNĐ");
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if (o == btnQuayVe) {
            if(JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn quay về? Mọi thay đổi chưa lưu sẽ bị mất.",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    dispose(); // Đóng frame
                }
        } else if (o == btnTim) {
            timKhachHang();
        } else if (o == btnXacNhan) {
            xacNhanHoaDon();
        }
    }
    
    private void timKhachHang() {
        String sdt = txtSDTKhachHang.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập số điện thoại!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra format số điện thoại: phải có đúng 10 số và bắt đầu bằng 0
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this,
                "Số điện thoại không hợp lệ!\n" +
                "Yêu cầu: Phải có đúng 10 chữ số và bắt đầu bằng số 0\n" +
                "Ví dụ: 0123456789",
                "Lỗi định dạng",
                JOptionPane.ERROR_MESSAGE);
            txtSDTKhachHang.requestFocus();
            txtSDTKhachHang.selectAll(); // Chọn toàn bộ text để người dùng sửa dễ hơn
            return;
        }

       
        
        try {
            // Tạo kết nối mới để đảm bảo không bị lỗi connection closed
            app.ConnectDB.ConnectDB.connect();
            
            // Tạo DAO mới và tìm khách hàng
            KhachHangDAO khDAO = new KhachHangDAO();
            KhachHang kh = khDAO.findKhachHangByPhone(sdt);
            
            if (kh != null) {
                txtTenKhachHang.setText(kh.getTenKH());
            } else {
                txtTenKhachHang.setText("");
                if (JOptionPane.showConfirmDialog(this,
                    "Không tìm thấy khách hàng với số điện thoại đã nhập. Bạn có muốn thêm khách hàng mới?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    // Mở frame thêm khách hàng mới
                    ThemKhachHangKhiLapHoaDon themKHFrame = new ThemKhachHangKhiLapHoaDon(sdt);
                    themKHFrame.setVisible(true);
                    
                    // Đợi frame đóng và kiểm tra lại

                    // Vì đã chuyển từ Dialog sang Frame nên cần cách khác để theo dõi
                    
                    // Một lựa chọn là thử kiểm tra lại sau 1-2 giây
                    Thread.sleep(2000); // Đợi 2 giây
                    
                    // Tạo kết nối mới và kiểm tra lại
                    app.ConnectDB.ConnectDB.connect();
                    KhachHang newKH = khDAO.findKhachHangByPhone(sdt);
                    if (newKH != null) {
                        txtTenKhachHang.setText(newKH.getTenKH());
                    }
                }
            }
        } catch (Exception e) {
           
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Đã xảy ra lỗi khi tìm kiếm khách hàng: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xacNhanHoaDon() {
        // Kiểm tra thông tin bắt buộc - nhập tiền khách thanh toán
        if (txtTienNhan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập số tiền khách hàng thanh toán!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            txtTienNhan.requestFocus();
            return;
        }
        
        // Đánh dấu trường tiền nhận bằng màu đỏ nếu chưa nhập
        txtTienNhan.setBorder(txtTienNhan.getText().trim().isEmpty() 
                ? BorderFactory.createLineBorder(Color.RED, 2)
                : BorderFactory.createLineBorder(Color.GRAY));
        
        // Tính tiền thừa để kiểm tra xem khách đã trả đủ tiền chưa
        try {
            String tienNhanStr = txtTienNhan.getText().trim().replace(",", "").replace(" VNĐ", "").replace("VNĐ", "").trim();
            double tienNhan = Double.parseDouble(tienNhanStr);
            
            if (tienNhan < tongThanhToan) {
                JOptionPane.showMessageDialog(this,
                    "Số tiền khách thanh toán chưa đủ!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
                txtTienNhan.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Số tiền không hợp lệ!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtTienNhan.requestFocus();
            return;
        }
        if(txtTenKhachHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập tìm kiếm khách hàng!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Xác nhận lưu hóa đơn
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận lưu hóa đơn?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Lấy thông tin khách hàng (nếu có)
            String sdt = txtSDTKhachHang.getText().trim();
            String maKhachHang = null;
            
            // Lấy mã khách hàng từ SDT nếu đã nhập
            if (!sdt.isEmpty()) {
                try {
                    KhachHangDAO khDAO = new KhachHangDAO();
                    KhachHang kh = khDAO.findKhachHangByPhone(sdt);
                    if (kh != null) {
                        maKhachHang = kh.getMaKH();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // Lấy thông tin khuyến mãi và ghi chú
            String maKhuyenMai = txtMaKhuyenMai.getText().trim();
            if (maKhuyenMai.isEmpty()) {
                maKhuyenMai = null;
            }
            
            String ghiChu = txtGhiChu.getText().trim();
            if (ghiChu.isEmpty()) {
                ghiChu = null;
            }
            
            // Chuẩn bị lưu hóa đơn
            boolean success = false;
            
            try {
                // Trước khi lưu, kiểm tra xem có đủ số lượng tồn kho không
                TonKhoDAO tonKhoDAO = new TonKhoDAO();
                if (!tonKhoDAO.kiemTraDuSoLuong(dsChiTietData)) {
                    JOptionPane.showMessageDialog(this,
                        "Không đủ số lượng thuốc trong kho để hoàn tất giao dịch. Vui lòng kiểm tra lại.",
                        "Cảnh báo tồn kho",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Sử dụng class DAO để lưu hóa đơn và chi tiết
                HoaDonDAO hoaDonDAO = new HoaDonDAO();
                success = hoaDonDAO.saveHoaDon(dsChiTietData, tongTien, maHoaDon, maNhanVienLap,
                                                     maKhachHang, maKhuyenMai, ghiChu);
                
                if (success) {
                    // Sau khi lưu hóa đơn thành công, cập nhật số lượng tồn kho
                    updateInventory(dsChiTietData);
                    
                    // Hỏi người dùng có muốn xuất hóa đơn PDF không
                    int printOption = JOptionPane.showConfirmDialog(this,
                        "Lưu hóa đơn thành công! Bạn có muốn xuất hóa đơn PDF không?",
                        "Xuất hóa đơn",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                        
                    if (printOption == JOptionPane.YES_OPTION) {
                        // Xuất hóa đơn PDF
                        xuatHoaDonPDF(maHoaDon, dsChiTietData, tongTien, maKhachHang, maNhanVienLap);
                    }
                    
                    // Gọi callback để thông báo cho LapHoaDonPanel load lại dữ liệu
                    // notifyCartClear() sẽ tự động đóng frame
                    notifyCartClear();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Lưu hóa đơn không thành công!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi lưu hóa đơn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Cập nhật số lượng trong kho sau khi bán
     * @param dsChiTietData danh sách chi tiết sản phẩm đã bán
     */
    private void updateInventory(ArrayList<Object[]> dsChiTietData) {
        try {
            // Sử dụng TonKhoDAO để cập nhật số lượng tồn kho
            TonKhoDAO tonKhoDAO = new TonKhoDAO();
            boolean success = tonKhoDAO.capNhatTonKhoSauKhiBan(dsChiTietData);
            
            if (success) {
                // Đồng bộ lại tổng thể để đảm bảo tính nhất quán dữ liệu
                boolean syncSuccess = tonKhoDAO.dongBoSoLuongTon(null);
                
                // Nếu đồng bộ tổng thể thất bại, thử đồng bộ từng thuốc cụ thể
                if (!syncSuccess) {
                    for (Object[] item : dsChiTietData) {
                        String maThuoc = (String) item[0];
                        tonKhoDAO.dongBoSoLuongTon(maThuoc);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không thể cập nhật số lượng tồn kho. Hóa đơn đã được lưu nhưng tồn kho chưa được cập nhật.",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi cập nhật số lượng tồn kho: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xuất hóa đơn PDF
     * @param maHoaDon Mã hóa đơn
     * @param dsChiTietData Danh sách chi tiết sản phẩm
     * @param tongTien Tổng tiền hóa đơn
     * @param maKhachHang Mã khách hàng (có thể null)
     * @param maNhanVien Mã nhân viên lập hóa đơn
     */
    private void xuatHoaDonPDF(String maHoaDon, ArrayList<Object[]> dsChiTietData, 
                              double tongTien, String maKhachHang, String maNhanVien) {
        try {
            // Sử dụng đường dẫn mặc định để lưu file
            String defaultDir = "E:\\PTUD\\PDF\\HoaDon";
            File directory = new File(defaultDir);
            
            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = "HoaDon_" + maHoaDon + ".pdf";
            String filePath = defaultDir + "\\" + fileName;
            
            // Tạo document và writer
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Font chữ
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(baseFont, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(baseFont, 11, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font totalFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            
            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Thông tin nhà thuốc
            Paragraph shopInfo = new Paragraph("NHÀ THUỐC PILL & CHILL\nĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\nHotline: 0987654321", normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            // Thông tin hóa đơn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            
            Paragraph invoiceInfo = new Paragraph();
            invoiceInfo.add(new Chunk("Mã hóa đơn: " + maHoaDon + "\n", headerFont));
            invoiceInfo.add(new Chunk("Ngày lập: " + currentDate + "\n", normalFont));
            
            // Lấy thông tin nhân viên
            String tenNhanVien = "Nhân viên";
            try {
                NhanVienDAO nvDAO = new NhanVienDAO();
                NhanVien nv = nvDAO.getNhanVienById(maNhanVien);
                if (nv != null) {
                    tenNhanVien = nv.getTenNV();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            invoiceInfo.add(new Chunk("Nhân viên lập: " + tenNhanVien + " (" + maNhanVien + ")\n", normalFont));
            
            // Lấy thông tin khách hàng nếu có
            if (maKhachHang != null && !maKhachHang.isEmpty()) {
                String tenKhachHang = "Khách hàng";
                String sdtKhachHang = "";
                try {
                    KhachHangDAO khDAO = new KhachHangDAO();
                    KhachHang kh = khDAO.getKhachHangById(maKhachHang);
                    if (kh != null) {
                        tenKhachHang = kh.getTenKH();
                        sdtKhachHang = kh.getSoDienThoai();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                invoiceInfo.add(new Chunk("Khách hàng: " + tenKhachHang + "\n", normalFont));
                invoiceInfo.add(new Chunk("Số điện thoại: " + sdtKhachHang + "\n", normalFont));
            }
            
            document.add(invoiceInfo);
            document.add(new Paragraph("\n"));
            
            // Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(5); // 5 cột
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1f, 3f, 1f, 1.5f, 2f};
            table.setWidths(columnWidths);
            
            // Header của bảng
            addTableHeader(table, headerFont);
            
            // Nội dung bảng
            DecimalFormat df = new DecimalFormat("#,###");
            
            for (Object[] item : dsChiTietData) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1];
                int soLuong = (Integer) item[2];
                float donGia = (Float) item[3];
                double thanhTien = soLuong * donGia;
                
                table.addCell(new PdfPCell(new Phrase(maThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(tenThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(soLuong), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(donGia), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(thanhTien), normalFont)));
            }
            
            document.add(table);
            
            // Thông tin tổng tiền
            // Sử dụng mức thuế mặc định là 10%
            double tyLeThue = 0.10;
            String tenThue = "VAT (10%)";
            double tienThue = tongTien * tyLeThue;
            double tongThanhToan = tongTien + tienThue;
            
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng tiền hàng: " + df.format(tongTien) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Thuế " + tenThue + ": " + df.format(tienThue) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Tổng thanh toán: " + df.format(tongThanhToan) + " VNĐ\n", totalFont));
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.add(new Paragraph("\n"));
            
            // Chữ ký
            Paragraph signature = new Paragraph();
            signature.add(new Chunk("Xác nhận của khách hàng                                                             Người lập hóa đơn\n\n\n\n", normalFont));
            signature.add(new Chunk("                                                                                             " + tenNhanVien, normalFont));
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);
            
            // Thêm ghi chú từ form nếu có
            String ghiChuText = txtGhiChu.getText().trim();
            if (!ghiChuText.isEmpty()) {
                document.add(new Paragraph("\n"));
                Paragraph ghiChuParagraph = new Paragraph("Ghi chú: " + ghiChuText, normalFont);
                ghiChuParagraph.setAlignment(Element.ALIGN_LEFT);
                document.add(ghiChuParagraph);
            }
            
            // Lưu ý
            Paragraph note = new Paragraph("\n\nLưu ý: \n- Hóa đơn chỉ có thể xuất trong ngày.", normalFont);
            document.add(note);
            
            document.close();
            
            // Thông báo thành công
            JOptionPane.showMessageDialog(this, 
                "Đã xuất hóa đơn PDF thành công!\nVị trí: " + filePath, 
                "Xuất hóa đơn", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Tự động mở file PDF sau khi xuất
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    // Sử dụng Desktop API để mở file với ứng dụng mặc định
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        // Nếu Desktop API không được hỗ trợ, dùng ProcessBuilder để mở
                        new ProcessBuilder("cmd", "/c", "start", "", filePath).start();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Không thể tự động mở file PDF: " + ex.getMessage(), 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi xuất hóa đơn PDF: " + e.getMessage() + 
                "\nVui lòng kiểm tra đường dẫn: E:\\PTUD\\PDF\\HoaDon", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Thêm header cho bảng PDF
     */
    private void addTableHeader(PdfPTable table, com.itextpdf.text.Font font) {
        String[] headers = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(new BaseColor(240, 240, 240));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
    
    /**
     * Làm mới toàn bộ dữ liệu sau khi lập hóa đơn thành công
     * - Xóa giỏ hàng
     * - Cập nhật lại bảng hiển thị thuốc với số lượng tồn mới
     * - Đảm bảo hiển thị dữ liệu mới nhất trên giao diện
     */
    private void notifyCartClear() {
        try {
            // Sử dụng callback là phương pháp chính và ưu tiên
            if (hoaDonCallback != null) {
                // Gọi callback trước để cập nhật dữ liệu
                hoaDonCallback.onHoaDonSuccess(dsChiTietData, maHoaDon);
                
                // Đảm bảo callback được xử lý trước khi đóng frame
                SwingUtilities.invokeLater(() -> {
                    // Đóng frame sau khi callback đã được xử lý
                    dispose();
                });
                return;
            }
            
            // Phương pháp cũ (fallback) nếu không có callback
            // Tìm tất cả các frame đang mở
            Frame[] frames = Frame.getFrames();
            JFrame mainFrame = null;
            
            // Tìm MainFrame trong số các frame đang mở
            for (Frame frame : frames) {
                if (frame instanceof JFrame && frame.getClass().getName().contains("MainFrame")) {
                    mainFrame = (JFrame) frame;
                    break;
                }
            }
            
            // Hiển thị thông báo thành công trước khi đóng
            JOptionPane.showMessageDialog(this,
                "Hóa đơn đã được lưu và tồn kho đã được cập nhật thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Nếu tìm thấy MainFrame, cập nhật dữ liệu trước khi đóng
            if (mainFrame != null) {
                // Thử truy cập trực tiếp nếu MainFrame có interface đã biết
                if (mainFrame instanceof app.GUI.MainFrame) {
                    final JFrame finalMainFrame = mainFrame;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Cập nhật giao diện qua MainFrame
                                ((app.GUI.MainFrame) finalMainFrame).showLapHoaDonPanel();
                            } catch (Exception ex) {
                                // Không làm gì
                            }
                        }
                    });
                } else {
                    // Fallback đến Reflection nếu không cast được
                    final JFrame finalMainFrame = mainFrame;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Cập nhật giao diện qua reflection
                                Method showLapHoaDonPanel = finalMainFrame.getClass().getMethod("showLapHoaDonPanel");
                                showLapHoaDonPanel.invoke(finalMainFrame);
                            } catch (Exception ex) {
                                // Không làm gì
                            }
                        }
                    });
                }
            }
            
            // Đóng frame hiện tại
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            this.dispose();
        }
    }
}
