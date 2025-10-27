package app.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import app.DAO.ChiTietHoaDonDAO;
import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.DAO.NhanVienDAO;
import app.DAO.ThuocDAO;
import app.Entity.ChiTietHoaDon;
import app.Entity.HoaDon;
import app.Entity.KhachHang;
import app.Entity.NhanVien;
import app.Entity.Thuoc;

public class LapPhieuTraThuocPanel extends JPanel implements ActionListener {

    // DAO objects
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private ThuocDAO thuocDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;

    // GUI Components - Bảng hóa đơn
    private JTable tableHoaDon;
    private DefaultTableModel modelHoaDon;
    private JScrollPane scrollHoaDon;

    // GUI Components - Bảng chi tiết hóa đơn
    private JTable tableChiTiet;
    private DefaultTableModel modelChiTiet;
    private JScrollPane scrollChiTiet;

    // Button
    private JButton btnConfirm;

    // Data
    private ArrayList<HoaDon> danhSachHoaDon;
    private String maHoaDonSelected = null;
    private String maNhanVienDangNhap; // Mã nhân viên đang đăng nhập

    public LapPhieuTraThuocPanel() {
        this(null); // Constructor mặc định
    }

    public LapPhieuTraThuocPanel(String maNhanVien) {
        this.maNhanVienDangNhap = maNhanVien;
        initDAOs();
        createLapPhieuDoiThuoc();
        loadDataHoaDon();
    }

    private void initDAOs() {
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        thuocDAO = new ThuocDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
    }

    private void createLapPhieuDoiThuoc() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblTitle = new JLabel("LẬP PHIẾU TRẢ THUỐC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.DARK_GRAY);
        titlePanel.add(lblTitle);

        return titlePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Panel hóa đơn (trên)
        JPanel hoaDonPanel = createHoaDonPanel();
        
        // Panel chi tiết hóa đơn (dưới)
        JPanel chiTietPanel = createChiTietPanel();

        // Thêm vào main panel với tỉ lệ 1:1
        mainPanel.add(hoaDonPanel, BorderLayout.NORTH);
        mainPanel.add(chiTietPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createHoaDonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "HÓA ĐƠN", 
            0, 0, new Font("Arial", Font.BOLD, 14)
        ));
        panel.setPreferredSize(new Dimension(0, 300));

        // Tạo bảng hóa đơn
        String[] columnNames = {"Mã hóa đơn", "Ngày lập", "Nhân viên", "Khách hàng", "Ghi chú"};
        modelHoaDon = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableHoaDon = new JTable(modelHoaDon);
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.setRowHeight(25);
        tableHoaDon.getTableHeader().setReorderingAllowed(false);

        // Listener để hiển thị chi tiết khi chọn hóa đơn
        tableHoaDon.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableHoaDon.getSelectedRow();
                    if (selectedRow >= 0) {
                        maHoaDonSelected = (String) modelHoaDon.getValueAt(selectedRow, 0);
                        loadChiTietHoaDon(maHoaDonSelected);
                    }
                }
            }
        });

        scrollHoaDon = new JScrollPane(tableHoaDon);
        panel.add(scrollHoaDon, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChiTietPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            "CHI TIẾT HÓA ĐƠN", 
            0, 0, new Font("Arial", Font.BOLD, 14)
        ));

        // Tạo bảng chi tiết hóa đơn
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá"};
        modelChiTiet = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(modelChiTiet);
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableChiTiet.setRowHeight(25);
        tableChiTiet.getTableHeader().setReorderingAllowed(false);

        scrollChiTiet = new JScrollPane(tableChiTiet);
        panel.add(scrollChiTiet, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 14));
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(new Color(240, 240, 240)); // Màu xám nhạt
        btnConfirm.setForeground(Color.BLACK); // Chữ màu đen
        btnConfirm.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền màu xám
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(this);

        panel.add(btnConfirm);
        return panel;
    }

    private void loadDataHoaDon() {
        danhSachHoaDon = hoaDonDAO.getAllHoaDon();
        modelHoaDon.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (HoaDon hd : danhSachHoaDon) {
            String ngayLap = hd.getNgayBan() != null ? sdf.format(hd.getNgayBan()) : "";
            
            // Lấy tên nhân viên
            String tenNV = "";
            if (hd.getMaNV() != null) {
                NhanVien nv = nhanVienDAO.getNhanVienById(hd.getMaNV());
                tenNV = nv != null ? nv.getTenNV() : hd.getMaNV();
            }

            // Lấy tên khách hàng
            String tenKH = "";
            if (hd.getMaKH() != null) {
                KhachHang kh = khachHangDAO.getKhachHangById(hd.getMaKH());
                tenKH = kh != null ? kh.getTenKH() : "Khách vãng lai";
            } else {
                tenKH = "Khách vãng lai";
            }

            Object[] row = {
                hd.getMaHoaDon(),
                ngayLap,
                tenNV,
                tenKH,
                hd.getGhiChu() != null ? hd.getGhiChu() : ""
            };
            modelHoaDon.addRow(row);
        }
    }

    private void loadChiTietHoaDon(String maHoaDon) {
        modelChiTiet.setRowCount(0);
        
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            return;
        }

        ArrayList<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.getChiTietByMaHoaDon(maHoaDon);

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
            modelChiTiet.addRow(row);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirm) {
            xacNhanTraThuoc();
        }
    }

    private void xacNhanTraThuoc() {
        if (maHoaDonSelected == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một hóa đơn để tạo phiếu trả thuốc!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mở frame XacNhanLapPhieuTraThuocFrame với chi tiết hóa đơn đã chọn
        ArrayList<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.getChiTietByMaHoaDon(maHoaDonSelected);
        
        if (danhSachChiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Hóa đơn không có sản phẩm nào để trả!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mở frame xác nhận lập phiếu trà thuốc với thông tin nhân viên
        new XacNhanLapPhieuTraThuocFrame(maHoaDonSelected, danhSachChiTiet, maNhanVienDangNhap);
    }
}








