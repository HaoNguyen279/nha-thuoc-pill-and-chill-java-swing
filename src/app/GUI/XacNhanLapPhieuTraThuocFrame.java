package app.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import app.DAO.PhieuDoiTraDAO;
import app.DAO.ChiTietLoThuocDAO;
import app.DAO.ChiTietPhieuDoiTraDAO;
import app.DAO.ThuocDAO;
import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.DAO.NhanVienDAO;
import app.Entity.ChiTietHoaDon;
import app.Entity.ChiTietLoThuoc;
import app.Entity.ChiTietPhieuDoiTra;
import app.Entity.PhieuDoiTra;
import app.Entity.Thuoc;
import app.Entity.HoaDon;
import app.Entity.KhachHang;
import app.Entity.NhanVien;

// Import iTextPDF classes
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class XacNhanLapPhieuTraThuocFrame extends JFrame implements ActionListener {
    
    private String maHoaDon;
    private ArrayList<ChiTietHoaDon> danhSachChiTiet;
    private String maPhieuTraThuoc;
    private String maNhanVienDangNhap;
    
    // DAO objects
    private PhieuDoiTraDAO phieuDoiTraDAO;
    private ChiTietPhieuDoiTraDAO chiTietPhieuDoiTraDAO;
    private ThuocDAO thuocDAO;
    private HoaDonDAO hoaDonDAO;
    
    // GUI Components - Bảng chi tiết hóa đơn (trên)
    private JTable tableChiTietHoaDon;
    private DefaultTableModel modelChiTietHoaDon;
    
    // GUI Components - Bảng danh sách thuốc đổi trả (dưới)
    private JTable tableTraThuoc;
    private DefaultTableModel modelTraThuoc;
    
    // Controls
    private JSpinner spinnerSoLuong;
    private JButton btnThemVaoPhieu, btnXoaKhoiPhieu;
    private JLabel lblTongTienTra;
    private JButton btnXacNhan;
    
    // Data
    private ArrayList<Object[]> danhSachTraThuoc; // [maThuoc, tenThuoc, maLo, soLuong, donGia, thanhTien, lyDo]
    private double tongTienTra = 0;

    public XacNhanLapPhieuTraThuocFrame(String maHoaDon, ArrayList<ChiTietHoaDon> danhSachChiTiet) {
        this(maHoaDon, danhSachChiTiet, null);
    }
    
    public XacNhanLapPhieuTraThuocFrame(String maHoaDon, ArrayList<ChiTietHoaDon> danhSachChiTiet, String maNhanVien) {
        this.maHoaDon = maHoaDon;
        this.danhSachChiTiet = danhSachChiTiet;
        this.danhSachTraThuoc = new ArrayList<>();
        this.maNhanVienDangNhap = maNhanVien;
        
        initDAOs();
        generateMaPhieuTra();
        initGUI();
        loadChiTietHoaDon();
        
        setVisible(true);
    }
    
    private void initDAOs() {
        phieuDoiTraDAO = new PhieuDoiTraDAO();
        chiTietPhieuDoiTraDAO = new ChiTietPhieuDoiTraDAO();
        thuocDAO = new ThuocDAO();
        hoaDonDAO = new HoaDonDAO();
    }
    
    private void generateMaPhieuTra() {
        maPhieuTraThuoc = phieuDoiTraDAO.generateMaPhieuDoiTra();
    }
    
    private void initGUI() {
        setTitle("Lập Phiếu Trả Thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // Center panel
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Main title
        JLabel lblTitle = new JLabel("LẬP PHIẾU TRẢ THUỐC");
        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(lblTitle);
        
        // Subtitle with code
        JLabel lblMaPhieu = new JLabel("Mã phiếu trả: " + maPhieuTraThuoc);
        lblMaPhieu.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        lblMaPhieu.setForeground(Color.BLACK);
        lblMaPhieu.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(lblMaPhieu);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        
        // Main content with two tables
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Chi tiết hóa đơn panel (top)
        JPanel chiTietPanel = createChiTietHoaDonPanel();
        chiTietPanel.setPreferredSize(new Dimension(0, 300));
        
        // Danh sách thuốc đổi trả panel (bottom)
        JPanel traThuocPanel = createTraThuocPanel();
        
        mainPanel.add(chiTietPanel, BorderLayout.NORTH);
        mainPanel.add(traThuocPanel, BorderLayout.CENTER);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChiTietHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "CHI TIẾT HÓA ĐƠN - " + maHoaDon, 
            0, 0, new java.awt.Font("Arial", java.awt.Font.BOLD, 14)
        ));
        
        // Tạo bảng chi tiết hóa đơn
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá"};
        modelChiTietHoaDon = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableChiTietHoaDon = new JTable(modelChiTietHoaDon);
        tableChiTietHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableChiTietHoaDon.setRowHeight(25);
        tableChiTietHoaDon.getTableHeader().setReorderingAllowed(false);
        tableChiTietHoaDon.getTableHeader().setBackground(new Color(248, 248, 248));
        
        JScrollPane scrollPane = new JScrollPane(tableChiTietHoaDon);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.WHITE);
        controlPanel.add(new JLabel("Số lượng:"));
        
        spinnerSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerSoLuong.setPreferredSize(new Dimension(60, 25));
        controlPanel.add(spinnerSoLuong);
        
        btnThemVaoPhieu = new JButton("Thêm vào danh sách đổi trả");
        btnThemVaoPhieu.setPreferredSize(new Dimension(200, 35));
        btnThemVaoPhieu.setBackground(new Color(240, 250, 240));
        btnThemVaoPhieu.setForeground(Color.BLACK);
        btnThemVaoPhieu.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34)));
        btnThemVaoPhieu.setFocusPainted(false);
        btnThemVaoPhieu.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        btnThemVaoPhieu.addActionListener(this);
        controlPanel.add(btnThemVaoPhieu);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTraThuocPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "DANH SÁCH THUỐC ĐỔI TRẢ", 
            0, 0, new java.awt.Font("Arial", java.awt.Font.BOLD, 14)
        ));
        
        // Tạo bảng danh sách thuốc đổi trả
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá", "Thành tiền", "Lý do"};
        modelTraThuoc = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Chỉ cho phép edit cột "Lý do"
            }
        };
        
        tableTraThuoc = new JTable(modelTraThuoc);
        tableTraThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTraThuoc.setRowHeight(25);
        tableTraThuoc.getTableHeader().setReorderingAllowed(false);
        tableTraThuoc.getTableHeader().setBackground(new Color(248, 248, 248));
        
        // Custom cell editor cho cột "Lý do" với placeholder
        JTextField lyDoTextField = new JTextField();
        lyDoTextField.setForeground(Color.GRAY);
        lyDoTextField.setText("Nhập lý do");
        
        // Add focus listener để xử lý placeholder
        lyDoTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (lyDoTextField.getText().equals("Nhập lý do")) {
                    lyDoTextField.setText("");
                    lyDoTextField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (lyDoTextField.getText().isEmpty()) {
                    lyDoTextField.setForeground(Color.GRAY);
                    lyDoTextField.setText("Nhập lý do");
                }
            }
        });
        
        DefaultCellEditor lyDoEditor = new DefaultCellEditor(lyDoTextField);
        tableTraThuoc.getColumnModel().getColumn(6).setCellEditor(lyDoEditor);
        
        JScrollPane scrollPane = new JScrollPane(tableTraThuoc);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(Color.WHITE);
        
        // Left side - Remove button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        
        spinnerSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerSoLuong.setPreferredSize(new Dimension(60, 25));
        leftPanel.add(new JLabel("Số lượng:"));
        leftPanel.add(spinnerSoLuong);
        
        btnXoaKhoiPhieu = new JButton("Xóa khỏi danh sách đổi trả");
        btnXoaKhoiPhieu.setPreferredSize(new Dimension(200, 35));
        btnXoaKhoiPhieu.setBackground(new Color(255, 240, 240));
        btnXoaKhoiPhieu.setForeground(Color.BLACK);
        btnXoaKhoiPhieu.setBorder(BorderFactory.createLineBorder(Color.RED));
        btnXoaKhoiPhieu.setFocusPainted(false);
        btnXoaKhoiPhieu.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
        btnXoaKhoiPhieu.addActionListener(this);
        leftPanel.add(btnXoaKhoiPhieu);
        
        // Right side - Total amount
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel("Tổng tiền trả lại:"));
        
        lblTongTienTra = new JLabel("0 VNĐ");
        lblTongTienTra.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblTongTienTra.setForeground(Color.BLACK);
        rightPanel.add(lblTongTienTra);
        
        controlPanel.add(leftPanel, BorderLayout.WEST);
        controlPanel.add(rightPanel, BorderLayout.EAST);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
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
        modelChiTietHoaDon.setRowCount(0);
        
        for (ChiTietHoaDon cthd : danhSachChiTiet) {
            // Lấy tên thuốc
            String tenThuoc = "";
            if (cthd.getMaThuoc() != null) {
                Thuoc thuoc = thuocDAO.getThuocById(cthd.getMaThuoc());
                tenThuoc = thuoc != null ? thuoc.getTenThuoc() : cthd.getMaThuoc();
            }
            
            Object[] row = {
                cthd.getMaThuoc(),
                tenThuoc,
                cthd.getMaLo() != null ? cthd.getMaLo() : "",
                cthd.getSoLuong(),
                String.format("%.0f", cthd.getDonGia())
            };
            modelChiTietHoaDon.addRow(row);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThemVaoPhieu) {
            themVaoPhieuTra();
        } else if (e.getSource() == btnXoaKhoiPhieu) {
            xoaKhoiPhieuTra();
        } else if (e.getSource() == btnXacNhan) {
            xacNhanLapPhieu();
        }
    }
    
    private void themVaoPhieuTra() {
        int selectedRow = tableChiTietHoaDon.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một sản phẩm để thêm vào phiếu trả!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = (String) modelChiTietHoaDon.getValueAt(selectedRow, 0);
        String tenThuoc = (String) modelChiTietHoaDon.getValueAt(selectedRow, 1);
        String maLo = (String) modelChiTietHoaDon.getValueAt(selectedRow, 2);
        int soLuongHienTai = (Integer) modelChiTietHoaDon.getValueAt(selectedRow, 3);
        String donGiaStr = (String) modelChiTietHoaDon.getValueAt(selectedRow, 4);
        float donGia = Float.parseFloat(donGiaStr);
        
        int soLuongTra = (Integer) spinnerSoLuong.getValue();
        
        // Validate số lượng
        if (soLuongTra > soLuongHienTai) {
            JOptionPane.showMessageDialog(this, 
                "Số lượng trả không được vượt quá số lượng đã mua (" + soLuongHienTai + ")!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra xem đã có sản phẩm này trong danh sách chưa (cùng mã thuốc và mã lô)
        boolean daTonTai = false;
        int viTriTonTai = -1;
        int tongSoLuongDaThem = 0;
        
        for (int i = 0; i < modelTraThuoc.getRowCount(); i++) {
            String existingMaThuoc = (String) modelTraThuoc.getValueAt(i, 0);
            String existingMaLo = (String) modelTraThuoc.getValueAt(i, 2);
            
            if (existingMaThuoc.equals(maThuoc) && existingMaLo.equals(maLo)) {
                daTonTai = true;
                viTriTonTai = i;
                tongSoLuongDaThem = (Integer) modelTraThuoc.getValueAt(i, 3);
                break;
            }
        }
        
        // Kiểm tra tổng số lượng không vượt quá số lượng đã mua
        if (tongSoLuongDaThem + soLuongTra > soLuongHienTai) {
            JOptionPane.showMessageDialog(this, 
                "Tổng số lượng trả (" + (tongSoLuongDaThem + soLuongTra) + ") không được vượt quá số lượng đã mua (" + soLuongHienTai + ")!\n" +
                "Đã thêm: " + tongSoLuongDaThem + ", Sắp thêm: " + soLuongTra, 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (daTonTai) {
            // Cập nhật số lượng và thành tiền cho dòng đã có
            int soLuongMoi = tongSoLuongDaThem + soLuongTra;
            float thanhTienMoi = donGia * soLuongMoi;
            
            modelTraThuoc.setValueAt(soLuongMoi, viTriTonTai, 3);
            modelTraThuoc.setValueAt(String.format("%.0f", thanhTienMoi), viTriTonTai, 5);
            
            // Cập nhật trong danh sách
            danhSachTraThuoc.get(viTriTonTai)[3] = soLuongMoi;
            danhSachTraThuoc.get(viTriTonTai)[5] = thanhTienMoi;
        } else {
            // Thêm dòng mới
            float thanhTien = donGia * soLuongTra;
            
            Object[] row = {
                maThuoc, tenThuoc, maLo, soLuongTra, 
                String.format("%.0f", donGia), 
                String.format("%.0f", thanhTien), 
                "" // Lý do - để trống
            };
            modelTraThuoc.addRow(row);
            
            // Thêm vào danh sách
            Object[] data = {maThuoc, tenThuoc, maLo, soLuongTra, donGia, thanhTien, ""};
            danhSachTraThuoc.add(data);
        }
        
        // Cập nhật tổng tiền
        capNhatTongTien();
        
        // Reset spinner
        spinnerSoLuong.setValue(1);
    }
    
    private void xoaKhoiPhieuTra() {
        int selectedRow = tableTraThuoc.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một sản phẩm để xóa khỏi phiếu trả!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Xóa khỏi bảng và danh sách
        modelTraThuoc.removeRow(selectedRow);
        danhSachTraThuoc.remove(selectedRow);
        
        // Cập nhật tổng tiền
        capNhatTongTien();
    }
    
    private void capNhatTongTien() {
        tongTienTra = 0;
        for (Object[] item : danhSachTraThuoc) {
            tongTienTra += (Float) item[5]; // thanhTien
        }
        lblTongTienTra.setText(String.format("%.0f VNĐ", tongTienTra));
    }
    
    private void xacNhanLapPhieu() {
        if (danhSachTraThuoc.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng thêm ít nhất một sản phẩm vào phiếu trả!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Dừng việc edit bảng để lấy dữ liệu mới nhất
        if (tableTraThuoc.getCellEditor() != null) {
            tableTraThuoc.getCellEditor().stopCellEditing();
        }
        
        // Cập nhật lý do từ bảng
        for (int i = 0; i < modelTraThuoc.getRowCount(); i++) {
            String lyDo = (String) modelTraThuoc.getValueAt(i, 6);
            // Kiểm tra nếu lý do là placeholder thì thay bằng chuỗi rỗng
            if (lyDo != null && lyDo.equals("Nhập lý do")) {
                lyDo = "";
            }
            danhSachTraThuoc.get(i)[6] = lyDo != null ? lyDo : "";
        }
        
        // Hiển thị dialog xác nhận
        StringBuilder thongTinXacNhan = new StringBuilder();
        thongTinXacNhan.append("THÔNG TIN PHIẾU TRẢ THUỐC\n\n");
        thongTinXacNhan.append("Mã phiếu trả: ").append(maPhieuTraThuoc).append("\n");
        thongTinXacNhan.append("Hóa đơn gốc: ").append(maHoaDon).append("\n");
        thongTinXacNhan.append("Tổng tiền trả lại: ").append(String.format("%.0f VNĐ", tongTienTra)).append("\n\n");
        thongTinXacNhan.append("DANH SÁCH THUỐC TRẢ:\n");
        
        for (Object[] item : danhSachTraThuoc) {
            thongTinXacNhan.append("- ").append(item[1]).append(" (").append(item[0]).append(")")
                           .append(" - SL: ").append(item[3])
                           .append(" - Thành tiền: ").append(String.format("%.0f VNĐ", (Float)item[5]));
            if (item[6] != null && !item[6].toString().trim().isEmpty()) {
                thongTinXacNhan.append(" - Lý do: ").append(item[6]);
            }
            thongTinXacNhan.append("\n");
        }
        
        thongTinXacNhan.append("\nBạn có chắc chắn muốn lập phiếu trả thuốc này không?");
        
        int choice = JOptionPane.showConfirmDialog(this, 
            thongTinXacNhan.toString(), 
            "Xác nhận lập phiếu trả thuốc", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            // Lấy thông tin hóa đơn để có mã khách hàng
            HoaDon hoaDon = hoaDonDAO.getHoaDonById(maHoaDon);
            String maKhachHang = (hoaDon != null) ? hoaDon.getMaKH() : null;
            String maNhanVien = (maNhanVienDangNhap != null) ? maNhanVienDangNhap : "NV001"; // fallback
            
            // Tạo phiếu đổi trả
            PhieuDoiTra phieuDoiTra = new PhieuDoiTra(
                maPhieuTraThuoc, 
                new Date(), 
                maKhachHang, // mã khách hàng lấy từ hóa đơn
                maNhanVien,  // mã nhân viên đang đăng nhập
                true
            );
            
            // Lưu phiếu đổi trả
            if (phieuDoiTraDAO.addPhieuDoiTra(phieuDoiTra)) {
                // Lưu chi tiết phiếu đổi trả và cập nhật tồn kho
                boolean allSuccess = true;
                
                for (Object[] item : danhSachTraThuoc) {
                    String maThuoc = (String) item[0];
                    String maLo = (String) item[2];
                    int soLuongTra = (Integer) item[3];
                    float donGia = (Float) item[4];
                    String lyDo = (String) item[6];
                    
                    // Lưu chi tiết phiếu đổi trả
                    ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra(
                        maPhieuTraThuoc, maThuoc, soLuongTra, donGia, 
                        maLo, lyDo, true
                    );
                    
                    if (!chiTietPhieuDoiTraDAO.addChiTietPhieuDoiTra(chiTiet)) {
                        allSuccess = false;
                        JOptionPane.showMessageDialog(this, 
                            "Lỗi khi lưu chi tiết phiếu trả cho thuốc: " + maThuoc, 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    
                    // Cập nhật tồn kho
                	ChiTietLoThuocDAO ctloDAO = new ChiTietLoThuocDAO();
                    ChiTietLoThuoc ctlothuoc = ctloDAO.getChiTietLoThuocById(maLo, maThuoc);
                    System.out.println(maThuoc + maLo);
                    if (ctlothuoc != null) {
                        ctlothuoc.setSoLuong(ctlothuoc.getSoLuong() + soLuongTra);
                        if (!ctloDAO.update(ctlothuoc)) {
                            allSuccess = false;
                            JOptionPane.showMessageDialog(this, 
                                "Lỗi khi cập nhật tồn kho cho thuốc: " + maThuoc, 
                                "Lỗi", 
                                JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    } else {
                        allSuccess = false;
                        JOptionPane.showMessageDialog(this, 
                            "Không tìm thấy thông tin thuốc: " + maThuoc, 
                            "Lỗi", 
                            JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
                
                if (allSuccess) {
                    // Hỏi người dùng có muốn xuất phiếu trả PDF không
                    int pdfChoice = JOptionPane.showConfirmDialog(this,
                        "Lập phiếu trả thuốc thành công! Bạn có muốn xuất phiếu trả PDF không?",
                        "Xuất PDF",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (pdfChoice == JOptionPane.YES_OPTION) {
                        // Xuất phiếu trả PDF
                        xuatPhieuTraPDF(maPhieuTraThuoc, danhSachTraThuoc, tongTienTra, maKhachHang, maNhanVien);
                    }
                    
                    JOptionPane.showMessageDialog(this, 
                        "Lập phiếu trả thuốc thành công!\n\n" +
                        "Mã phiếu: " + maPhieuTraThuoc + "\n" +
                        "Tổng tiền trả: " + String.format("%.0f VNĐ", tongTienTra) + "\n" +
                        "Đã cập nhật tồn kho cho " + danhSachTraThuoc.size() + " sản phẩm", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dispose(); // Đóng frame
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi lưu phiếu đổi trả!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Xuất phiếu trả thuốc PDF
     * @param maPhieuTra Mã phiếu trả
     * @param danhSachTraThuoc Danh sách thuốc trả
     * @param tongTienTra Tổng tiền trả
     * @param maKhachHang Mã khách hàng (có thể null)
     * @param maNhanVien Mã nhân viên lập phiếu
     */
    private void xuatPhieuTraPDF(String maPhieuTra, ArrayList<Object[]> danhSachTraThuoc, 
                                double tongTienTra, String maKhachHang, String maNhanVien) {
        try {
            // Sử dụng đường dẫn mặc định để lưu file
            String defaultDir = "E:\\PTUD\\PDF\\PhieuTra";
            File directory = new File(defaultDir);
            
            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            String fileName = "PhieuTraThuoc_" + maPhieuTra + ".pdf";
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
            Paragraph title = new Paragraph("PHIẾU TRẢ THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Thông tin nhà thuốc
            Paragraph shopInfo = new Paragraph("NHÀ THUỐC PILL & CHILL\nĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\nHotline: 0987654321", normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            // Thông tin phiếu trả
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            
            Paragraph phieuInfo = new Paragraph();
            phieuInfo.add(new Chunk("Mã phiếu trả: " + maPhieuTra + "\n", headerFont));
            phieuInfo.add(new Chunk("Ngày lập: " + currentDate + "\n", normalFont));
            phieuInfo.add(new Chunk("Hóa đơn gốc: " + maHoaDon + "\n", normalFont));
            
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
            phieuInfo.add(new Chunk("Nhân viên lập: " + tenNhanVien + " (" + maNhanVien + ")\n", normalFont));
            
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
                phieuInfo.add(new Chunk("Khách hàng: " + tenKhachHang + "\n", normalFont));
                phieuInfo.add(new Chunk("Số điện thoại: " + sdtKhachHang + "\n", normalFont));
            } else {
                // Khách vãng lai
                phieuInfo.add(new Chunk("Khách hàng: Khách vãng lai\n", normalFont));
            }
            
            document.add(phieuInfo);
            document.add(new Paragraph("\n"));
            
            // Bảng chi tiết sản phẩm trả
            PdfPTable table = new PdfPTable(6); // 6 cột
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1f, 2.5f, 1f, 1f, 1.5f, 2f};
            table.setWidths(columnWidths);
            
            // Header của bảng
            addTableHeaderForPhieuTra(table, headerFont);
            
            // Nội dung bảng
            DecimalFormat df = new DecimalFormat("#,###");
            
            for (Object[] item : danhSachTraThuoc) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1];
                String maLo = (String) item[2];
                int soLuong = (Integer) item[3];
                float donGia = (Float) item[4];
                double thanhTien = (Float) item[5];
                
                table.addCell(new PdfPCell(new Phrase(maThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(tenThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(maLo, normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(soLuong), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(donGia), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(thanhTien), normalFont)));
            }
            
            document.add(table);
            
            // Thông tin tổng tiền trả
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng tiền trả lại: " + df.format(tongTienTra) + " VNĐ\n", totalFont));
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.add(new Paragraph("\n"));
            
            // Lý do trả hàng (gạch đầu dòng)
            Paragraph lyDoTitle = new Paragraph("LÝ DO TRẢ HÀNG:", headerFont);
            document.add(lyDoTitle);
            
            com.itextpdf.text.List lyDoList = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
            lyDoList.setIndentationLeft(20);
            
            for (Object[] item : danhSachTraThuoc) {
                String tenThuoc = (String) item[1];
                String lyDo = (String) item[6];
                
                if (lyDo != null && !lyDo.trim().isEmpty() && !lyDo.equals("Nhập lý do")) {
                    ListItem lyDoItem = new ListItem(tenThuoc + ": " + lyDo, normalFont);
                    lyDoList.add(lyDoItem);
                }
            }
            
            // Nếu không có lý do nào được nhập
            if (lyDoList.isEmpty()) {
                ListItem noReasonItem = new ListItem("Không có lý do cụ thể", normalFont);
                lyDoList.add(noReasonItem);
            }
            
            document.add(lyDoList);
            
            document.add(new Paragraph("\n"));
            
            // Chữ ký
            Paragraph signature = new Paragraph();
            signature.add(new Chunk("Xác nhận của khách hàng                                                             Người lập phiếu\n\n\n\n", normalFont));
            signature.add(new Chunk("                                                                                             " + tenNhanVien, normalFont));
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);
            
            // Lưu ý
            Paragraph note = new Paragraph("\n\nLưu ý: \n- Phiếu trả thuốc chỉ có thể lập trong ngày.\n- Thuốc trả phải còn nguyên vẹn bao bì.", normalFont);
            document.add(note);
            
            document.close();
            
            // Thông báo thành công
            JOptionPane.showMessageDialog(this, 
                "Đã xuất phiếu trả thuốc PDF thành công!\nVị trí: " + filePath, 
                "Xuất phiếu trả", 
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
                    "Đã xuất PDF thành công, nhưng không thể mở file tự động.\nVị trí: " + filePath,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra khi xuất PDF: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Thêm header cho bảng chi tiết phiếu trả
     */
    private void addTableHeaderForPhieuTra(PdfPTable table, com.itextpdf.text.Font font) {
        String[] headers = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá", "Thành tiền"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(new BaseColor(240, 240, 240));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
}