package app.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import app.ConnectDB.ConnectDB;
import app.DAO.PhieuDatDAO;
import app.DAO.ChiTietPhieuDatDAO;
import app.DAO.KhachHangDAO;
import app.DAO.NhanVienDAO;
import app.DAO.ThuocDAO;
import app.DTO.ThuocKemGiaDTO;
import app.DAO.HoaDonDAO;
import app.Entity.PhieuDat;
import app.Entity.ChiTietPhieuDat;
import app.Entity.KhachHang;
import app.Entity.NhanVien;
import app.Entity.Thuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LapHoaDonTuPhieuDatFrame extends JFrame implements ActionListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color TEXT_COLOR = new Color(51, 51, 51);

    // DAO instances
    private PhieuDatDAO phieuDatDAO;
    private ChiTietPhieuDatDAO chiTietPhieuDatDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private ThuocDAO thuocDAO;
    
    // Components
    private JButton btnQuayVe, btnTim, btnXacNhan;
    private JTextField txtSearch;
    private JTable tblPhieuDat, tblChiTietPhieuDat;
    private DefaultTableModel modelPhieuDat, modelChiTietPhieuDat;
    private JTextField txtTongTien;
    
    // Data
    private ArrayList<PhieuDat> dsPhieuDat;
    private ArrayList<ChiTietPhieuDat> dsChiTietPhieuDat;
    private PhieuDat phieuDatDuocChon;
    private double tongTien = 0.0;
    private String maNhanVien;
    
    public LapHoaDonTuPhieuDatFrame(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        
        // Khởi tạo DAO
        ConnectDB.connect();
        phieuDatDAO = new PhieuDatDAO();
        chiTietPhieuDatDAO = new ChiTietPhieuDatDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        thuocDAO = new ThuocDAO();
        
        dsPhieuDat = new ArrayList<>();
        dsChiTietPhieuDat = new ArrayList<>();
        
        setTitle("Lập Hóa Đơn Từ Phiếu Đặt");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createGUI();
        loadPhieuDatData();
        
        setVisible(true);
    }
    
    private void createGUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel: Header với nút quay về và tiêu đề
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel: Chứa 2 bảng 
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel: Tổng tiền và nút xác nhận
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Nút quay về
        btnQuayVe = new JButton("← Quay về");
        btnQuayVe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnQuayVe.setBackground(new Color(240, 240, 240));
        btnQuayVe.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnQuayVe.setFocusPainted(false);
        btnQuayVe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuayVe.addActionListener(this);
        
        // Tiêu đề
        JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN TỪ PHIẾU ĐẶT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        
        panel.add(btnQuayVe, BorderLayout.WEST);
        panel.add(lblTitle, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        panel.setBackground(Color.WHITE);
        
        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(300, 35));
        txtSearch.setBackground(new Color(245, 245, 245));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("Nhập từ khóa tìm kiếm");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Thêm placeholder functionality
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("Nhập từ khóa tìm kiếm")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("Nhập từ khóa tìm kiếm");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });
        
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTim.setBackground(new Color(76, 175, 80));
        btnTim.setForeground(Color.WHITE);
        btnTim.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        panel.add(txtSearch);
        panel.add(btnTim);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Bảng phiếu đặt (trên)
        JPanel phieuDatPanel = createPhieuDatTablePanel();
        
        // Bảng chi tiết phiếu đặt (dưới)
        JPanel chiTietPanel = createChiTietPhieuDatTablePanel();
        
        // Sử dụng GridLayout để chia đều 2 bảng
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        tablesPanel.setBackground(Color.WHITE);
        tablesPanel.add(phieuDatPanel);
        tablesPanel.add(chiTietPanel);
        
        panel.add(tablesPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPhieuDatTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Tiêu đề bảng
        JLabel lblTitle = new JLabel("DANH SÁCH PHIẾU ĐẶT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Tạo bảng phiếu đặt
        String[] columnNames = {"Mã phiếu đặt", "Nhân viên lập", "Khách hàng", "Số điện thoại", "Ngày đặt", "Ghi chú"};
        modelPhieuDat = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPhieuDat = new JTable(modelPhieuDat);
        tblPhieuDat.setRowHeight(35);
        tblPhieuDat.setBackground(Color.WHITE);
        tblPhieuDat.setGridColor(new Color(224, 224, 224));
        tblPhieuDat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhieuDat.setSelectionBackground(new Color(178, 223, 219));
        tblPhieuDat.setSelectionForeground(Color.BLACK);
        tblPhieuDat.setShowGrid(true);
        tblPhieuDat.setIntercellSpacing(new Dimension(1, 1));
        
        tblPhieuDat.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblPhieuDat.getTableHeader().setBackground(PRIMARY_COLOR);
        tblPhieuDat.getTableHeader().setForeground(Color.WHITE);
        tblPhieuDat.getTableHeader().setPreferredSize(new Dimension(tblPhieuDat.getTableHeader().getWidth(), 40));
        tblPhieuDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Căn giữa cho một số cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblPhieuDat.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã phiếu đặt
        tblPhieuDat.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Số điện thoại
        tblPhieuDat.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Ngày đặt
        
        // Thêm listener để xử lý khi chọn phiếu đặt
        tblPhieuDat.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    onPhieuDatSelected();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblPhieuDat);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChiTietPhieuDatTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Tiêu đề bảng
        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU ĐẶT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Tạo bảng chi tiết phiếu đặt
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTietPhieuDat = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblChiTietPhieuDat = new JTable(modelChiTietPhieuDat);
        tblChiTietPhieuDat.setRowHeight(35);
        tblChiTietPhieuDat.setBackground(Color.WHITE);
        tblChiTietPhieuDat.setGridColor(new Color(224, 224, 224));
        tblChiTietPhieuDat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChiTietPhieuDat.setSelectionBackground(new Color(178, 223, 219));
        tblChiTietPhieuDat.setSelectionForeground(Color.BLACK);
        tblChiTietPhieuDat.setShowGrid(true);
        tblChiTietPhieuDat.setIntercellSpacing(new Dimension(1, 1));
        
        tblChiTietPhieuDat.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblChiTietPhieuDat.getTableHeader().setBackground(PRIMARY_COLOR);
        tblChiTietPhieuDat.getTableHeader().setForeground(Color.WHITE);
        tblChiTietPhieuDat.getTableHeader().setPreferredSize(new Dimension(tblChiTietPhieuDat.getTableHeader().getWidth(), 40));
        tblChiTietPhieuDat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Căn phải cho các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblChiTietPhieuDat.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tblChiTietPhieuDat.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tblChiTietPhieuDat.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tblChiTietPhieuDat);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Panel phải chứa tổng tiền và nút xác nhận
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        rightPanel.setBackground(Color.WHITE);
        
        // Tổng tiền
        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        txtTongTien = new JTextField(15);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTien.setEditable(false);
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setBackground(new Color(245, 245, 245));
        txtTongTien.setText("0 VNĐ");
        txtTongTien.setPreferredSize(new Dimension(200, 40));
        
        // Nút xác nhận
        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new Font("Arial", Font.PLAIN, 14));
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        btnXacNhan.setBackground(new Color(240, 240, 240));
        btnXacNhan.setForeground(Color.BLACK);
        btnXacNhan.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setEnabled(false); // Vô hiệu hóa ban đầu
        btnXacNhan.addActionListener(this);
        
        rightPanel.add(lblTongTien);
        rightPanel.add(txtTongTien);
        rightPanel.add(btnXacNhan);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void loadPhieuDatData() {
        try {
            dsPhieuDat = phieuDatDAO.getAllPhieuDat();
            updatePhieuDatTable(dsPhieuDat);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu phiếu đặt: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updatePhieuDatTable(ArrayList<PhieuDat> dsPhieuDat) {
        modelPhieuDat.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (PhieuDat phieuDat : dsPhieuDat) {
            // Lấy tên nhân viên
            String tenNhanVien = "";
            try {
                NhanVien nv = nhanVienDAO.getNhanVienById(phieuDat.getMaNV());
                if (nv != null) {
                    tenNhanVien = nv.getTenNV();
                }
            } catch (Exception e) {
                tenNhanVien = phieuDat.getMaNV();
            }
            
            // Lấy tên khách hàng và số điện thoại
            String tenKhachHang = "";
            String soDienThoai = "";
            if (phieuDat.getMaKH() != null && !phieuDat.getMaKH().isEmpty()) {
                try {
                    KhachHang kh = khachHangDAO.getKhachHangById(phieuDat.getMaKH());
                    if (kh != null) {
                        tenKhachHang = kh.getTenKH();
                        soDienThoai = kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "";
                    } else {
                        tenKhachHang = "Khách vãng lai";
                        soDienThoai = "";
                    }
                } catch (Exception e) {
                    tenKhachHang = "Khách vãng lai";
                    soDienThoai = "";
                }
            } else {
                tenKhachHang = "Khách vãng lai";
                soDienThoai = "";
            }
            
            Object[] row = {
                phieuDat.getMaPhieuDat(),
                tenNhanVien,
                tenKhachHang,
                soDienThoai,
                dateFormat.format(phieuDat.getNgayDat()),
                phieuDat.getGhiChu() != null ? phieuDat.getGhiChu() : ""
            };
            
            modelPhieuDat.addRow(row);
        }
    }
    
    private void onPhieuDatSelected() {
        int selectedRow = tblPhieuDat.getSelectedRow();
        if (selectedRow >= 0) {
            String maPhieuDat = (String) modelPhieuDat.getValueAt(selectedRow, 0);
            phieuDatDuocChon = findPhieuDatByMa(maPhieuDat);
            
            if (phieuDatDuocChon != null) {
                loadChiTietPhieuDat(maPhieuDat);
                btnXacNhan.setEnabled(true);
            }
        } else {
            // Không có phiếu đặt nào được chọn
            modelChiTietPhieuDat.setRowCount(0);
            phieuDatDuocChon = null;
            tongTien = 0.0;
            txtTongTien.setText("0 VNĐ");
            btnXacNhan.setEnabled(false);
        }
    }
    
    private PhieuDat findPhieuDatByMa(String maPhieuDat) {
        for (PhieuDat phieuDat : dsPhieuDat) {
            if (phieuDat.getMaPhieuDat().equals(maPhieuDat)) {
                return phieuDat;
            }
        }
        return null;
    }
    
    private void loadChiTietPhieuDat(String maPhieuDat) {
        try {
            dsChiTietPhieuDat = chiTietPhieuDatDAO.getAllByPhieuDatId(maPhieuDat);
            updateChiTietPhieuDatTable();
            tinhTongTien();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải chi tiết phiếu đặt: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateChiTietPhieuDatTable() {
        modelChiTietPhieuDat.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for (ChiTietPhieuDat chiTiet : dsChiTietPhieuDat) {
            // Lấy thông tin thuốc để có đơn giá
            try {
                ThuocKemGiaDTO thuoc = thuocDAO.getThuocKemGiaById(chiTiet.getMaThuoc());
                if (thuoc != null) {
                    double donGia = thuoc.getGiaBan();
                    double thanhTien = chiTiet.getSoLuong() * donGia;
                    
                    Object[] row = {
                        chiTiet.getMaThuoc(),
                        chiTiet.getTenThuoc(),
                        chiTiet.getSoLuong(),
                        df.format(donGia) + " VNĐ",
                        df.format(thanhTien) + " VNĐ"
                    };
                    
                    modelChiTietPhieuDat.addRow(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void tinhTongTien() {
        tongTien = 0.0;
        DecimalFormat df = new DecimalFormat("#,###");
        
        for (ChiTietPhieuDat chiTiet : dsChiTietPhieuDat) {
            try {
                ThuocKemGiaDTO thuoc = thuocDAO.getThuocKemGiaById(chiTiet.getMaThuoc());
                if (thuoc != null) {
                    double thanhTien = chiTiet.getSoLuong() * thuoc.getGiaBan();
                    tongTien += thanhTien;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        txtTongTien.setText(df.format(tongTien) + " VNĐ");
    }
    
    private void timKiemPhieuDat() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        
        if (keyword.isEmpty() || keyword.equals("nhập từ khóa tìm kiếm")) {
            // Nếu không có từ khóa hoặc vẫn là placeholder, hiển thị tất cả
            updatePhieuDatTable(dsPhieuDat);
            return;
        }
        
        ArrayList<PhieuDat> ketQuaTimKiem = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (PhieuDat phieuDat : dsPhieuDat) {
            boolean khop = false;
            
            // Tìm theo mã phiếu đặt
            if (phieuDat.getMaPhieuDat().toLowerCase().contains(keyword)) {
                khop = true;
            }
            
            // Tìm theo tên nhân viên
            try {
                NhanVien nv = nhanVienDAO.getNhanVienById(phieuDat.getMaNV());
                if (nv != null && nv.getTenNV().toLowerCase().contains(keyword)) {
                    khop = true;
                }
            } catch (Exception e) {
                // Không làm gì
            }
            
            // Tìm theo tên khách hàng và số điện thoại
            if (phieuDat.getMaKH() != null && !phieuDat.getMaKH().isEmpty()) {
                try {
                    KhachHang kh = khachHangDAO.getKhachHangById(phieuDat.getMaKH());
                    if (kh != null) {
                        // Tìm theo tên khách hàng
                        if (kh.getTenKH().toLowerCase().contains(keyword)) {
                            khop = true;
                        }
                        // Tìm theo số điện thoại
                        if (kh.getSoDienThoai() != null && kh.getSoDienThoai().contains(keyword)) {
                            khop = true;
                        }
                    }
                } catch (Exception e) {
                    // Không làm gì
                }
            }
            
            // Tìm theo ngày đặt
            if (dateFormat.format(phieuDat.getNgayDat()).contains(keyword)) {
                khop = true;
            }
            
            // Tìm theo ghi chú
            if (phieuDat.getGhiChu() != null && phieuDat.getGhiChu().toLowerCase().contains(keyword)) {
                khop = true;
            }
            
            if (khop) {
                ketQuaTimKiem.add(phieuDat);
            }
        }
        
        updatePhieuDatTable(ketQuaTimKiem);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == btnQuayVe) {
            dispose();
        } else if (source == btnTim) {
            timKiemPhieuDat();
        } else if (source == btnXacNhan) {
            xacNhanLapHoaDon();
        }
    }
    
    private void xacNhanLapHoaDon() {
        if (phieuDatDuocChon == null || dsChiTietPhieuDat.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một phiếu đặt để lập hóa đơn!",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Chuyển đổi dữ liệu chi tiết phiếu đặt sang format để truyền cho XacNhanLapHoaDonFrame
            ArrayList<Object[]> dsChiTietHoaDon = new ArrayList<>();
            
            for (ChiTietPhieuDat chiTiet : dsChiTietPhieuDat) {
                ThuocKemGiaDTO thuoc = thuocDAO.getThuocKemGiaById(chiTiet.getMaThuoc());
                if (thuoc != null) {
                    Object[] item = {
                        chiTiet.getMaThuoc(),
                        chiTiet.getTenThuoc(),
                        chiTiet.getSoLuong(),
                        (float) thuoc.getGiaBan()
                    };
                    dsChiTietHoaDon.add(item);
                }
            }
            
            // Tạo mã hóa đơn tự động từ DAO
            HoaDonDAO hoaDonDAO = new HoaDonDAO();
            String maHoaDon = hoaDonDAO.generateMaHoaDon();
            
            // Lấy thông tin khách hàng từ phiếu đặt đã chọn
            String soDienThoaiKH = "";
            String tenKhachHang = "";
            if (phieuDatDuocChon.getMaKH() != null && !phieuDatDuocChon.getMaKH().isEmpty()) {
                try {
                    KhachHang kh = khachHangDAO.getKhachHangById(phieuDatDuocChon.getMaKH());
                    if (kh != null) {
                        soDienThoaiKH = kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "";
                        tenKhachHang = kh.getTenKH() != null ? kh.getTenKH() : "";
                    }
                } catch (Exception e) {
                    // Nếu lỗi, để trống thông tin khách hàng
                }
            }
            
            // Mở frame xác nhận lập hóa đơn với thông tin khách hàng và phiếu đặt
            new XacNhanLapHoaDonFrame(
                dsChiTietHoaDon, 
                tongTien, 
                maHoaDon, 
                maNhanVien,
                soDienThoaiKH,
                tenKhachHang,
                phieuDatDuocChon
            );
            
            // Đóng frame hiện tại
            this.dispose();
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi chuyển đến trang xác nhận: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}