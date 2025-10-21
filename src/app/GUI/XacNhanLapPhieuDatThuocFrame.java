package app.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import app.DAO.KhachHangDAO;
import app.DAO.PhieuDatDAO;
import app.DAO.ChiTietPhieuDatDAO;
import app.Entity.KhachHang;
import app.Entity.PhieuDat;
import app.Entity.ChiTietPhieuDat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class XacNhanLapPhieuDatThuocFrame extends JFrame implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Object[]> dsChiTietData;
    private double tongTien;
    private String maPhieuDat;
    private String maNhanVien;
    private LapPhieuDatThuocPanel parentPanel;
    
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
    
    public XacNhanLapPhieuDatThuocFrame(ArrayList<Object[]> dsChiTietData, double tongTien,
                                        String maPhieuDat, String maNhanVien,
                                        LapPhieuDatThuocPanel parentPanel) {
        this.dsChiTietData = dsChiTietData;
        this.tongTien = tongTien;
        this.maPhieuDat = maPhieuDat;
        this.maNhanVien = maNhanVien;
        this.parentPanel = parentPanel;
        
        khachHangDAO = new KhachHangDAO();
        phieuDatDAO = new PhieuDatDAO();
        chiTietPhieuDatDAO = new ChiTietPhieuDatDAO();
        
        setTitle("Xác Nhận Lập Phiếu Đặt Thuốc - " + maPhieuDat);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createGUI();
        loadChiTietPhieuDat();
        
        setVisible(true);
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
        
        JLabel lblTitle = new JLabel("LẬP PHIẾU ĐẶT THUỐC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMaPhieuDat = new JLabel("Mã phiếu đặt: " + maPhieuDat);
        lblMaPhieuDat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
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
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblChiTiet.setRowHeight(30);
        tblChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
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
        
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
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
        panel.setPreferredSize(new Dimension(0, 80)); // Độ cao tương tự XacNhanLapHoaDonFrame

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
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại khách hàng!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this,
            "Số điện thoại không hợp lệ! (Phải có 10 số và bắt đầu bằng 0)",
                "Lỗi",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            KhachHang kh = khachHangDAO.findKhachHangByPhone(sdt);
            if (kh != null) {
                txtTenKhachHang.setText(kh.getTenKH());
                JOptionPane.showMessageDialog(this, "Đã tìm thấy khách hàng: " + kh.getTenKH(), 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                txtTenKhachHang.setText("");
                if (JOptionPane.showConfirmDialog(this,
                    "Không tìm thấy khách hàng với số điện thoại đã nhập.\n" +
                    "Bạn có muốn thêm khách hàng mới không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    // Mở frame thêm khách hàng mới
                    ThemKhachHangKhiLapHoaDon themKHFrame = new ThemKhachHangKhiLapHoaDon(sdt);
                    themKHFrame.setVisible(true);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm khách hàng: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xacNhanPhieuDat() {
        // Kiểm tra số điện thoại
        String sdt = txtSDTKhachHang.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại khách hàng!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this,
                "Số điện thoại không hợp lệ! (Phải có 10 số và bắt đầu bằng 0)",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            txtSDTKhachHang.requestFocus();
            return;
        }
        
        // Kiểm tra khách hàng đã được tìm kiếm
        String tenKH = txtTenKhachHang.getText().trim();
        if (tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng tìm kiếm thông tin khách hàng trước khi xác nhận!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            btnTim.requestFocus();
            return;
        }
        
        // Xác nhận lại khách hàng có tồn tại trong hệ thống
        try {
            KhachHang kh = khachHangDAO.findKhachHangByPhone(sdt);
            if (kh == null) {
                JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại trong hệ thống!\nVui lòng thêm khách hàng mới trước.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra thông tin khách hàng: " + ex.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Ngày giao hàng phải từ " + 
                new SimpleDateFormat("dd/MM/yyyy").format(calNgayMai.getTime()) + " trở đi!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Lấy thông tin khách hàng từ database
            KhachHang khachHang = khachHangDAO.findKhachHangByPhone(sdt);
            if (khachHang == null) {
                JOptionPane.showMessageDialog(this, "Khách hàng không tồn tại trong hệ thống!\nVui lòng thêm khách hàng mới trước.", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Lưu phiếu đặt vào database
            PhieuDat phieuDat = new PhieuDat(
                maPhieuDat,
                maNhanVien, 
                new Date(), // Ngày đặt
                khachHang.getMaKH(),
                true // isActive
            );
            
            boolean success = phieuDatDAO.addPhieuDat(phieuDat);
            
            if (success) {
                // Lưu chi tiết phiếu đặt
                success = saveChiTietPhieuDat();
                
                if (success) {
                    // Lưu chi tiết phiếu đặt thành công
                    
                    // Note: Đối với phiếu đặt, thông thường không cần cập nhật tồn kho ngay
                    // vì đây chỉ là đặt hàng chưa xuất kho. Tồn kho sẽ được cập nhật khi giao hàng.
                    // Nếu cần cập nhật tồn kho ngay (reserve inventory), có thể thêm logic ở đây.
                    
                    JOptionPane.showMessageDialog(this, 
                        "Lập phiếu đặt thành công!\n" +
                        "Mã phiếu đặt: " + maPhieuDat + "\n" +
                        "Khách hàng: " + tenKH + "\n" +
                        "SĐT: " + sdt + "\n" +
                        "Ngày giao: " + new SimpleDateFormat("dd/MM/yyyy").format(ngayGiao),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    
                    if (parentPanel != null) {
                        parentPanel.onPhieuDatSuccess(dsChiTietData, maPhieuDat);
                    }
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu chi tiết phiếu đặt!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu đặt!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu đặt: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean saveChiTietPhieuDat() {
        try {
            for (Object[] item : dsChiTietData) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1];
                int soLuong = (Integer) item[2];
                // Note: ChiTietPhieuDat không lưu donGia và thanhTien như ChiTietHoaDon
                
                ChiTietPhieuDat chiTiet = new ChiTietPhieuDat(
                    maPhieuDat,
                    maThuoc,
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