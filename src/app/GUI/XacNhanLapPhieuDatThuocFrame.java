package app.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import app.DAO.KhachHangDAO;
import app.DAO.PhieuDatDAO;
import app.DAO.ChiTietPhieuDatDAO;
import app.DAO.NhanVienDAO;
import app.DAO.TonKhoDAO;
import app.Entity.KhachHang;
import app.Entity.PhieuDat;
import app.Entity.ChiTietPhieuDat;
import app.Entity.NhanVien;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;

// Import all iTextPDF classes
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class XacNhanLapPhieuDatThuocFrame extends JFrame implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Object[]> dsChiTietData;
    private double tongTien;
    private String maPhieuDat;
    private String maNhanVien;
    private LapPhieuDatThuocPanel parentPanel;
    
    // Callback khi lập phiếu đặt thành công
    private PhieuDatCallback phieuDatCallback;
    
    // Components theo style của XacNhanLapHoaDonFrame
    private JButton btnQuayVe, btnTim, btnXacNhan;
    private JTextField txtSDTKhachHang, txtTenKhachHang, txtNhanVienLap, txtNgayLap, txtTongTien;
    private JTextArea txtGhiChu;
    private JSpinner spinnerNgayGiao;
    
    private JTable tblChiTiet;
    private DefaultTableModel modelChiTiet;
    
    private KhachHangDAO khachHangDAO;
    private PhieuDatDAO phieuDatDAO;
    private ChiTietPhieuDatDAO chiTietPhieuDatDAO;
    private NhanVienDAO nhanVienDAO;
    
    public XacNhanLapPhieuDatThuocFrame(ArrayList<Object[]> dsChiTietData, double tongTien,
                                        String maPhieuDat, String maNhanVien,
                                        LapPhieuDatThuocPanel parentPanel) {
        this.dsChiTietData = dsChiTietData;
        this.tongTien = tongTien;
        this.maPhieuDat = maPhieuDat;
        this.maNhanVien = maNhanVien;
        this.parentPanel = parentPanel;
        // Đặt callback từ parentPanel vì LapPhieuDatThuocPanel implement PhieuDatCallback
        this.phieuDatCallback = parentPanel;
        
        khachHangDAO = new KhachHangDAO();
        phieuDatDAO = new PhieuDatDAO();
        chiTietPhieuDatDAO = new ChiTietPhieuDatDAO();
        nhanVienDAO = new NhanVienDAO();
        
        setTitle("Xác Nhận Lập Phiếu Đặt Thuốc - " + maPhieuDat);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createGUI();
        loadChiTietPhieuDat();
        
        setVisible(true);
    }
    
    // Constructor với callback
    public XacNhanLapPhieuDatThuocFrame(ArrayList<Object[]> dsChiTietData, double tongTien,
                                        String maPhieuDat, String maNhanVien, 
                                        PhieuDatCallback callback) {
        this(dsChiTietData, tongTien, maPhieuDat, maNhanVien, (LapPhieuDatThuocPanel) null);
        this.phieuDatCallback = callback;
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
        btnQuayVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        btnQuayVe.setBackground(new Color(240, 240, 240));
        btnQuayVe.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnQuayVe.setFocusPainted(false);
        btnQuayVe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuayVe.addActionListener(this);
        
        // Tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel("LẬP PHIẾU ĐẶT THUỐC");
        lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMaPhieuDat = new JLabel("Mã phiếu đặt: " + maPhieuDat);
        lblMaPhieuDat.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));
        lblMaPhieuDat.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblMaPhieuDat);
        
        headerPanel.add(btnQuayVe, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // Table panel: Bảng chi tiết phiếu đặt
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Tạo bảng chi tiết phiếu đặt
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblChiTiet = new JTable(modelChiTiet);
        tblChiTiet.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblChiTiet.setRowHeight(25);
        tblChiTiet.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        tblChiTiet.getTableHeader().setBackground(new Color(240, 250, 240));
        tblChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Căn phải cho các cột số
        tblChiTiet.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.RIGHT); }
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
        
        // Left panel: Thông tin khách hàng và phiếu đặt
        JPanel leftPanel = createLeftInfoPanel();
        
        // Right panel: Thông tin ngày giao và tổng tiền
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
        
        java.awt.Font labelFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font fieldFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        
        // SDT khách hàng (với nút Tìm)
        panel.add(createInfoRow("SĐT khách hàng:", labelFont));
        JPanel sdtPanel = new JPanel(new BorderLayout(5, 0));
        sdtPanel.setBackground(Color.WHITE);
        sdtPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtSDTKhachHang = createTextField(fieldFont);
        
        // Thêm DocumentFilter để chỉ cho phép nhập số và giới hạn 10 ký tự
        ((javax.swing.text.AbstractDocument) txtSDTKhachHang.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                if (string.matches("\\d*") && (fb.getDocument().getLength() + string.length()) <= 10) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                if (text.matches("\\d*") && (fb.getDocument().getLength() - length + text.length()) <= 10) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        btnTim = new JButton("Tìm");
        btnTim.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnTim.setBackground(new Color(76, 175, 80));
        btnTim.setForeground(Color.WHITE);
        btnTim.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        sdtPanel.add(txtSDTKhachHang, BorderLayout.CENTER);
        sdtPanel.add(btnTim, BorderLayout.EAST);
        panel.add(sdtPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Tên khách hàng
        panel.add(createInfoRow("Tên khách hàng:", labelFont));
        txtTenKhachHang = createTextField(fieldFont);
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(new Color(220, 220, 220));
        txtTenKhachHang.setFocusable(false);
        panel.add(txtTenKhachHang);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Nhân viên lập
        panel.add(createInfoRow("Nhân viên lập:", labelFont));
        txtNhanVienLap = createTextField(fieldFont);
        txtNhanVienLap.setEditable(false);
        if (maNhanVien != null && !maNhanVien.isEmpty()) {
            txtNhanVienLap.setText(maNhanVien);
        }
        panel.add(txtNhanVienLap);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Ngày lập
        panel.add(createInfoRow("Ngày lập:", labelFont));
        txtNgayLap = createTextField(fieldFont);
        txtNgayLap.setEditable(false);
        txtNgayLap.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        panel.add(txtNgayLap);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
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
        
        java.awt.Font labelFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        java.awt.Font fieldFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
        
        DecimalFormat df = new DecimalFormat("#,###");
        
        // Tổng tiền
        panel.add(createInfoRow("Tổng tiền:", labelFont));
        txtTongTien = createTextField(fieldFont);
        txtTongTien.setEditable(false);
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setText(df.format(tongTien) + " VNĐ");
        panel.add(txtTongTien);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Ngày giao hàng
        panel.add(createInfoRow("Ngày giao hàng:", labelFont));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date minDate = cal.getTime();
        
        SpinnerDateModel dateModel = new SpinnerDateModel(minDate, minDate, null, Calendar.DAY_OF_MONTH);
        spinnerNgayGiao = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerNgayGiao, "dd/MM/yyyy");
        spinnerNgayGiao.setEditor(dateEditor);
        spinnerNgayGiao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        spinnerNgayGiao.setFont(fieldFont);
        panel.add(spinnerNgayGiao);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createInfoRow(String text, java.awt.Font font) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel label = new JLabel(text);
        label.setFont(font);
        rowPanel.add(label);
        
        return rowPanel;
    }
    
    private JTextField createTextField(java.awt.Font font) {
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
        panel.setPreferredSize(new Dimension(0, 80)); // Độ cao tương tự XacNhanLapHoaDonFrame

        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
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
    
    private void setupFrame() {
        setTitle("Xac nhan lap phieu dat thuoc");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
    }
    
    private void loadChiTietPhieuDat() {
        modelChiTiet.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for (int i = 0; i < dsChiTietData.size(); i++) {
            Object[] row = dsChiTietData.get(i);
            Object[] displayRow = {
                row[0], // Mã thuốc
                row[1], // Tên thuốc
                row[2], // Số lượng
                df.format(row[3]) + " VND", // Đơn giá
                df.format(row[4]) + " VND"  // Thành tiền
            };
            modelChiTiet.addRow(displayRow);
        }
    }
    
    private void timKhachHang() {
        String sdt = txtSDTKhachHang.getText().trim();
        if (sdt.isEmpty()) {
            CustomJOptionPane warningPane = new CustomJOptionPane(this, "Vui lòng nhập số điện thoại khách hàng!", false);
            warningPane.show();
            return;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Số điện thoại không hợp lệ! (Phải có 10 số và bắt đầu bằng 0)", false);
            errorPane.show();
            return;
        }
        try {
            KhachHang kh = khachHangDAO.findKhachHangByPhone(sdt);
            if (kh != null) {
                txtTenKhachHang.setText(kh.getTenKH());
            } else {
                txtTenKhachHang.setText("");
                CustomJOptionPane confirmPane = new CustomJOptionPane(this, 
                    "Không tìm thấy khách hàng với số điện thoại đã nhập.\nBạn có muốn thêm khách hàng mới không?", 
                    true);
                int confirm = confirmPane.show();
                
                if (confirm == JOptionPane.YES_OPTION) {

                    // Mở frame thêm khách hàng mới
                    ThemKhachHangKhiLapHoaDon themKHFrame = new ThemKhachHangKhiLapHoaDon(sdt);
                    themKHFrame.setVisible(true);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi tìm kiếm khách hàng: " + e.getMessage(), false);
            errorPane.show();
        }
    }
    
    private void xacNhanPhieuDat() {
        // Kiểm tra số điện thoại
        String sdt = txtSDTKhachHang.getText().trim();
        if (sdt.isEmpty()) {
            CustomJOptionPane warningPane = new CustomJOptionPane(this, "Vui lòng nhập số điện thoại khách hàng!", false);
            warningPane.show();
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!sdt.matches("^0\\d{9}$")) {
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Số điện thoại không hợp lệ! (Phải có 10 số và bắt đầu bằng 0)", false);
            errorPane.show();
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra khách hàng đã được tìm kiếm
        String tenKH = txtTenKhachHang.getText().trim();
        if (tenKH.isEmpty()) {
            CustomJOptionPane warningPane = new CustomJOptionPane(this, "Vui lòng tìm kiếm thông tin khách hàng trước khi xác nhận!", false);
            warningPane.show();
            btnTim.requestFocus();
            return;
        }
        
        // Xác nhận lại khách hàng có tồn tại trong hệ thống
        try {
            KhachHang kh = khachHangDAO.findKhachHangByPhone(sdt);
            if (kh == null) {
                CustomJOptionPane errorPane = new CustomJOptionPane(this, "Khách hàng không tồn tại trong hệ thống!\nVui lòng thêm khách hàng mới trước.", false);
                errorPane.show();
                return;
            }
        } catch (Exception ex) {
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi kiểm tra thông tin khách hàng: " + ex.getMessage(), false);
            errorPane.show();
            return;
        }
        
        // Kiểm tra ngày giao hàng
        Date ngayGiao = (Date) spinnerNgayGiao.getValue();
        
        // Lấy ngày hiện tại (chỉ lấy ngày, bỏ qua giờ)
        Calendar calHienTai = Calendar.getInstance();
        calHienTai.set(Calendar.HOUR_OF_DAY, 0);
        calHienTai.set(Calendar.MINUTE, 0);
        calHienTai.set(Calendar.SECOND, 0);
        calHienTai.set(Calendar.MILLISECOND, 0);
        Date ngayHienTai = calHienTai.getTime();
        
        // Lấy ngày giao (chỉ lấy ngày, bỏ qua giờ)
        Calendar calNgayGiao = Calendar.getInstance();
        calNgayGiao.setTime(ngayGiao);
        calNgayGiao.set(Calendar.HOUR_OF_DAY, 0);
        calNgayGiao.set(Calendar.MINUTE, 0);
        calNgayGiao.set(Calendar.SECOND, 0);
        calNgayGiao.set(Calendar.MILLISECOND, 0);
        Date ngayGiaoClean = calNgayGiao.getTime();
        
        // Kiểm tra: ngày giao phải từ ngày mai trở đi
        if (ngayGiaoClean.before(ngayHienTai) || ngayGiaoClean.equals(ngayHienTai)) {
            Calendar calNgayMai = Calendar.getInstance();
            calNgayMai.add(Calendar.DAY_OF_MONTH, 1);
            CustomJOptionPane warningPane = new CustomJOptionPane(this, "Ngày giao hàng phải từ " + 
                new SimpleDateFormat("dd/MM/yyyy").format(calNgayMai.getTime()) + " trở đi!", false);
            warningPane.show();
            return;
        }
        
        try {
            // Lấy thông tin khách hàng từ database
            KhachHang khachHang = khachHangDAO.findKhachHangByPhone(sdt);
            if (khachHang == null) {
                CustomJOptionPane errorPane = new CustomJOptionPane(this, "Khách hàng không tồn tại trong hệ thống!\nVui lòng thêm khách hàng mới trước.", false);
                errorPane.show();
                return;
            }
            
            // Lấy ghi chú từ form
            String ghiChu = txtGhiChu.getText().trim();
            
            // Lưu phiếu đặt vào database
            PhieuDat phieuDat = new PhieuDat(
                maPhieuDat,
                maNhanVien, 
                new Date(), // Ngày đặt
                khachHang.getMaKH(),
                ghiChu,     // Ghi chú
                true        // isActive
            );
            
            boolean success = phieuDatDAO.addPhieuDat(phieuDat);
            
            if (success) {
                // Lưu chi tiết phiếu đặt
                success = saveChiTietPhieuDat();
                
                if (success) {
                    // Lưu chi tiết phiếu đặt thành công
                    
                    // Lấy ghi chú để hiển thị trong thông báo
                    String ghiChuDisplay = txtGhiChu.getText().trim();
                    String ghiChuText = ghiChuDisplay.isEmpty() ? "(Không có ghi chú)" : ghiChuDisplay;
                    
                    // Hỏi người dùng có muốn xuất phiếu đặt PDF không
                    CustomJOptionPane confirmPane = new CustomJOptionPane(this,
                        "Lập phiếu đặt thành công!\nMã phiếu đặt: " + maPhieuDat + "\nKhách hàng: " + tenKH + "\nSĐT: " + sdt + "\nNgày giao: " + new SimpleDateFormat("dd/MM/yyyy").format(ngayGiao) + "\nGhi chú: " + ghiChuText + "\n\nBạn có muốn xuất phiếu đặt PDF không?",
                        true);
                    int printOption = confirmPane.show();
                        
                    if (printOption == JOptionPane.YES_OPTION) {
                        // Xuất phiếu đặt PDF
                        xuatPhieuDatPDF(maPhieuDat, dsChiTietData, tongTien, khachHang.getMaKH(), maNhanVien, ngayGiao);
                    }
                    
                    // Gọi callback khi lập phiếu đặt thành công
                    if (phieuDatCallback != null) {
                        phieuDatCallback.onPhieuDatSuccess(dsChiTietData, maPhieuDat);
                    }
                    
                    dispose();
                } else {
                    CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi lưu chi tiết phiếu đặt!", false);
                    errorPane.show();
                }
            } else {
                CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi lưu phiếu đặt!", false);
                errorPane.show();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi lưu phiếu đặt: " + e.getMessage(), false);
            errorPane.show();
        }
    }
    
    private boolean saveChiTietPhieuDat() {
        try {
            for (Object[] item : dsChiTietData) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1];
                int soLuong = (Integer) item[2];
                // item[3] và item[4] là donGia và thanhTien (không dùng cho PhieuDat)
                String maLo = item.length > 5 ? (String) item[5] : "N/A"; // Lấy maLo nếu có
                
                ChiTietPhieuDat chiTiet = new ChiTietPhieuDat(
                    maPhieuDat,
                    maThuoc,
                    maLo,      // Thêm maLo
                    tenThuoc,
                    soLuong,
                    true // isActive
                );
                
                boolean success = chiTietPhieuDatDAO.create(chiTiet);
                if (!success) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xuất phiếu đặt PDF
     * @param maPhieuDat Mã phiếu đặt
     * @param dsChiTietData Danh sách chi tiết sản phẩm
     * @param tongTien Tổng tiền phiếu đặt
     * @param maKhachHang Mã khách hàng
     * @param maNhanVien Mã nhân viên lập phiếu đặt
     * @param ngayGiao Ngày giao hàng
     */
    private void xuatPhieuDatPDF(String maPhieuDat, ArrayList<Object[]> dsChiTietData, 
                                double tongTien, String maKhachHang, String maNhanVien, Date ngayGiao) {
        try {
            // Sử dụng đường dẫn mặc định để lưu file
            String defaultDir = "D:\\PTUD\\PDF\\PhieuDat";
            File directory = new File(defaultDir);
            
            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = "PhieuDat_" + maPhieuDat + ".pdf";
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
            Paragraph title = new Paragraph("PHIẾU ĐẶT THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Thông tin nhà thuốc
            Paragraph shopInfo = new Paragraph("NHÀ THUỐC PILL & CHILL\nĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\nHotline: 0987654321", normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            // Thông tin phiếu đặt
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());
            String deliveryDate = dateFormat.format(ngayGiao);
            
            Paragraph orderInfo = new Paragraph();
            orderInfo.add(new Chunk("Mã phiếu đặt: " + maPhieuDat + "\n", headerFont));
            orderInfo.add(new Chunk("Ngày đặt: " + currentDate + "\n", normalFont));
            orderInfo.add(new Chunk("Ngày giao hàng: " + deliveryDate + "\n", normalFont));
            
            // Lấy thông tin nhân viên
            String tenNhanVien = "Nhân viên";
            try {
                NhanVien nv = nhanVienDAO.getNhanVienById(maNhanVien);
                if (nv != null) {
                    tenNhanVien = nv.getTenNV();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            orderInfo.add(new Chunk("Nhân viên lập: " + tenNhanVien + " (" + maNhanVien + ")\n", normalFont));
            
            // Lấy thông tin khách hàng
            if (maKhachHang != null && !maKhachHang.isEmpty()) {
                try {
                    KhachHang kh = khachHangDAO.getKhachHangById(maKhachHang);
                    if (kh != null) {
                        orderInfo.add(new Chunk("Khách hàng: " + kh.getTenKH() + "\n", normalFont));
                        orderInfo.add(new Chunk("Số điện thoại: " + kh.getSoDienThoai() + "\n", normalFont));
                        // Note: KhachHang entity doesn't have address field currently
                        orderInfo.add(new Chunk("Điểm tích lũy: " + kh.getDiemTichLuy() + "\n", normalFont));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            document.add(orderInfo);
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
                
                // Xử lý donGia an toàn - có thể là Float hoặc Double
                double donGia;
                if (item[3] instanceof Float) {
                    donGia = ((Float) item[3]).doubleValue();
                } else if (item[3] instanceof Double) {
                    donGia = (Double) item[3];
                } else {
                    donGia = Double.parseDouble(item[3].toString());
                }
                
                // Xử lý thanhTien an toàn - có thể là Float hoặc Double
                double thanhTien;
                if (item[4] instanceof Float) {
                    thanhTien = ((Float) item[4]).doubleValue();
                } else if (item[4] instanceof Double) {
                    thanhTien = (Double) item[4];
                } else {
                    thanhTien = Double.parseDouble(item[4].toString());
                }
                
                table.addCell(new PdfPCell(new Phrase(maThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(tenThuoc, normalFont)));
                
                PdfPCell cellSoLuong = new PdfPCell(new Phrase(String.valueOf(soLuong), normalFont));
                cellSoLuong.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellSoLuong);
                
                PdfPCell cellDonGia = new PdfPCell(new Phrase(df.format(donGia) + " VNĐ", normalFont));
                cellDonGia.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellDonGia);
                
                PdfPCell cellThanhTien = new PdfPCell(new Phrase(df.format(thanhTien) + " VNĐ", normalFont));
                cellThanhTien.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellThanhTien);
            }
            
            document.add(table);
            
            // Thông tin tổng tiền
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng tiền: " + df.format(tongTien) + " VNĐ\n", totalFont));
            
            // Thêm ghi chú nếu có
            String ghiChu = txtGhiChu.getText().trim();
            if (!ghiChu.isEmpty()) {
                summary.add(new Chunk("Ghi chú: " + ghiChu + "\n", normalFont));
            }
            
            summary.add(new Chunk("\nLưu ý: Đây là phiếu đặt thuốc.\nGiá chưa bao gồm thuế VAT. Khách hàng vui lòng thanh toán khi nhận hàng.\n", normalFont));
            summary.add(new Chunk("Ngày giao hàng: " + deliveryDate + "\n", normalFont));
            
            // Chữ ký
            summary.add(new Chunk("\n\n"));
            summary.add(new Chunk("Người lập phiếu\n", normalFont));
            summary.add(new Chunk("(Ký và ghi rõ họ tên)\n\n\n", normalFont));
            summary.add(new Chunk(tenNhanVien, normalFont));
            
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.close();
            
            // Hiển thị thông báo thành công và hỏi có muốn mở file không
            CustomJOptionPane confirmPane = new CustomJOptionPane(this,
                "Xuất phiếu đặt PDF thành công!\nFile được lưu tại: " + filePath + "\n\nBạn có muốn mở file không?",
                true);
            int openOption = confirmPane.show();
                
            if (openOption == JOptionPane.YES_OPTION) {
                // Mở file PDF
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filePath));
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            CustomJOptionPane errorPane = new CustomJOptionPane(this, "Lỗi khi xuất phiếu đặt PDF: " + e.getMessage(), false);
            errorPane.show();
        }
    }
    
    /**
     * Thêm header cho bảng PDF
     */
    private void addTableHeader(PdfPTable table, com.itextpdf.text.Font font) {
        String[] headers = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, font));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(8);
            table.addCell(headerCell);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == btnQuayVe) {
            dispose();
        } else if (source == btnTim) {
            timKhachHang();
        } else if (source == btnXacNhan) {
            xacNhanPhieuDat();
        }
    }
}