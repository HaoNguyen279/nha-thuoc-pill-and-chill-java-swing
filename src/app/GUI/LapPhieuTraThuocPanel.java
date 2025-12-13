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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import com.formdev.flatlaf.FlatLightLaf;

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

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);

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
        FlatLightLaf.setup();
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
        setBackground(BG_COLOR);

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
        titlePanel.setBackground(BG_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblTitle = new JLabel("LẬP PHIẾU TRẢ THUỐC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(PRIMARY_COLOR);
        titlePanel.add(lblTitle);

        return titlePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_COLOR);
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
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)), 
            "HÓA ĐƠN", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), TEXT_COLOR
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

        tableHoaDon = new JTable(modelHoaDon) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };
        
        tableHoaDon.setRowHeight(35);
        tableHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHoaDon.setFillsViewportHeight(true);
        tableHoaDon.setShowGrid(true);
        tableHoaDon.setGridColor(new Color(224, 224, 224));
        tableHoaDon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableHoaDon.setSelectionBackground(new Color(178, 223, 219));
        tableHoaDon.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tableHoaDon.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableHoaDon.getColumnCount(); i++) {
            tableHoaDon.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

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
        scrollHoaDon.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollHoaDon.getViewport().setBackground(Color.WHITE);
        panel.add(scrollHoaDon, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createChiTietPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)), 
            "CHI TIẾT HÓA ĐƠN", 
            0, 0, new Font("Segoe UI", Font.BOLD, 14), TEXT_COLOR
        ));

        // Tạo bảng chi tiết hóa đơn
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá"};
        modelChiTiet = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableChiTiet = new JTable(modelChiTiet) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };
        
        tableChiTiet.setRowHeight(35);
        tableChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableChiTiet.setFillsViewportHeight(true);
        tableChiTiet.setShowGrid(true);
        tableChiTiet.setGridColor(new Color(224, 224, 224));
        tableChiTiet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableChiTiet.setSelectionBackground(new Color(178, 223, 219));
        tableChiTiet.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tableChiTiet.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableChiTiet.getColumnCount(); i++) {
            tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        scrollChiTiet = new JScrollPane(tableChiTiet);
        scrollChiTiet.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollChiTiet.getViewport().setBackground(Color.WHITE);
        panel.add(scrollChiTiet, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setPreferredSize(new Dimension(150, 45));
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setBackground(PRIMARY_COLOR);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setBorderPainted(false);
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

    /**
     * Phương thức reload dữ liệu từ database
     * Gọi lại khi cần cập nhật danh sách hóa đơn (ví dụ sau khi xuất hóa đơn mới)
     */
    public void reloadDataFromDatabase() {
        try {
            // Load lại dữ liệu hóa đơn từ database
            loadDataHoaDon();
            
            // Reset selection và clear chi tiết
            tableHoaDon.clearSelection();
            maHoaDonSelected = null;
            modelChiTiet.setRowCount(0);
            
            // Refresh UI
            tableHoaDon.revalidate();
            tableHoaDon.repaint();
            tableChiTiet.revalidate();
            tableChiTiet.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải lại dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}








