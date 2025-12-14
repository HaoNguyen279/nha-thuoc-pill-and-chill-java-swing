package app.GUI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import app.ConnectDB.ConnectDB;
import app.DAO.HoaDonDAO;
import app.DAO.PhieuDatDAO;
import app.DAO.ThuocDAO;
import app.DTO.ThuocKemGiaDTO;
import app.Entity.Thuoc;

public class LapPhieuDatThuocPanel extends JPanel implements ActionListener, PhieuDatCallback {
    // Constants for styling
    private static final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private static final Color BTN_NEUTRAL_COLOR = new Color(149, 165, 166);

    private DefaultTableModel modelThuoc; // Model cho bảng thuốc
    private DefaultTableModel modelGioHang; // Model cho bảng giỏ hàng
    private JTable tblThuoc;
    private JTable tblGioHang;
    private ThuocDAO thuocDAO;
    private PhieuDatDAO phieuDatDAO;
    private ArrayList<ThuocKemGiaDTO> dsThuoc;
    
    // Components cần xử lý sự kiện
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnResetSearch; // Nút reset tìm kiếm
    private JSpinner spnQuantityTop;  // Spinner ở bảng trên
    private JSpinner spnQuantityBottom;  // Spinner ở bảng dưới
    private JButton btnAddToCart;
    private JButton btnRemove;
    private JButton btnReset;
    private JButton btnConfirm;
    private JTextField txtTongTien; // Textfield hiển thị tổng tiền (không chỉnh sửa được)
    
    // Placeholder cho search box
    private final String SEARCH_PLACEHOLDER = "Nhập từ khóa tìm kiếm...";
    private String maNhanVien; // Store employee ID
    public LapPhieuDatThuocPanel() {
        // TODO: Thêm implementation ở đây
    }
    public LapPhieuDatThuocPanel(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Khởi tạo kết nối database và DAO
        ConnectDB.connect();
        thuocDAO = new ThuocDAO();
        phieuDatDAO = new PhieuDatDAO();
        dsThuoc = new ArrayList<>();

        // Panel chính giữa chứa 2 bảng
        JPanel pnlCenter = createCenterPanel();
        add(pnlCenter, BorderLayout.CENTER);

        // Panel nút xác nhận phía dưới
        JPanel pnlBottom = createBottomPanel();
        add(pnlBottom, BorderLayout.SOUTH);
        
        // Load dữ liệu thuốc
        loadDataThuoc();
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BG_COLOR);
        JLabel lblTitle = new JLabel("LẬP PHIẾU ĐẶT THUỐC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(PRIMARY_COLOR);
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);

        centerPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel giữa chứa 2 bảng
        JPanel pnlTable = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlTable.setBackground(BG_COLOR);

        // Bảng trên - Danh sách thuốc để chọn
        JPanel topTablePanel = createTopTablePanel();
        pnlTable.add(topTablePanel);

        // Bảng dưới - Giỏ hàng
        JPanel bottomTablePanel = createBottomTablePanel();
        pnlTable.add(bottomTablePanel);

        centerPanel.add(pnlTable, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        searchPanel.setBackground(BG_COLOR);

        txtSearch = new JTextField(25);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        txtSearch.setBackground(Color.WHITE);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.setPreferredSize(new Dimension(300, 40));
        
        // Gán placeholder lúc đầu
        txtSearch.setText(SEARCH_PLACEHOLDER);
        
        // Lắng nghe khi focus
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals(SEARCH_PLACEHOLDER)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                    txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(SEARCH_PLACEHOLDER);
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                }
            }
        });

        btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(100, 40));
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setBorderPainted(false);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(this);
        
        btnResetSearch = new JButton("Làm mới");
        btnResetSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnResetSearch.setPreferredSize(new Dimension(100, 40));
        btnResetSearch.setBackground(BTN_NEUTRAL_COLOR);
        btnResetSearch.setForeground(Color.WHITE);
        btnResetSearch.setBorderPainted(false);
        btnResetSearch.setFocusPainted(false);
        btnResetSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetSearch.addActionListener(this);

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnResetSearch);

        return searchPanel;
    }

    // Bảng trên - Danh sách thuốc để chọn (có cột Số lượng tồn)
    private JPanel createTopTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Bảng dữ liệu với cột "Số lượng tồn"
        String[] columns = {"Mã thuốc", "Tên thuốc", "Số lượng tồn", "Đơn giá", "Đơn vị"};
        
        modelThuoc = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        tblThuoc = new JTable(modelThuoc) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(220, 240, 255));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tblThuoc.setRowHeight(35);
        tblThuoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblThuoc.setGridColor(new Color(230, 230, 230));
        tblThuoc.setSelectionBackground(new Color(220, 240, 255));
        tblThuoc.setSelectionForeground(Color.BLACK);
        
        // Chỉ tô màu header
        tblThuoc.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblThuoc.getTableHeader().setBackground(PRIMARY_COLOR);
        tblThuoc.getTableHeader().setForeground(Color.WHITE);
        tblThuoc.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel điều khiển dưới bảng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(BG_COLOR);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        spnQuantityTop = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnQuantityTop.setPreferredSize(new Dimension(80, 30));
        spnQuantityTop.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnAddToCart = new JButton("Thêm vào giỏ hàng");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddToCart.setPreferredSize(new Dimension(180, 40));
        btnAddToCart.setBackground(PRIMARY_COLOR);
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setBorderPainted(false);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(this);

        controlPanel.add(quantityLabel);
        controlPanel.add(spnQuantityTop);
        controlPanel.add(btnAddToCart);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Bảng dưới - Giỏ hàng (có cột Số lượng thay vì Số lượng tồn)
    private JPanel createBottomTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Tiêu đề "GIỎ HÀNG"
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        titlePanel.setBackground(BG_COLOR);
        JLabel lblCartTitle = new JLabel("GIỎ HÀNG");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCartTitle.setForeground(PRIMARY_COLOR);
        titlePanel.add(lblCartTitle);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Bảng giỏ hàng với cột "Số lượng"
        String[] columns = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        
        modelGioHang = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        tblGioHang = new JTable(modelGioHang) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(220, 240, 255));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tblGioHang.setRowHeight(35);
        tblGioHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblGioHang.setGridColor(new Color(230, 230, 230));
        tblGioHang.setSelectionBackground(new Color(220, 240, 255));
        tblGioHang.setSelectionForeground(Color.BLACK);
        
        // Chỉ tô màu header
        tblGioHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblGioHang.getTableHeader().setBackground(PRIMARY_COLOR);
        tblGioHang.getTableHeader().setForeground(Color.WHITE);
        tblGioHang.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(tblGioHang);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel điều khiển dưới bảng giỏ hàng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(BG_COLOR);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        spnQuantityBottom = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnQuantityBottom.setPreferredSize(new Dimension(80, 30));
        spnQuantityBottom.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnRemove = new JButton("Xóa khỏi giỏ hàng");
        btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRemove.setPreferredSize(new Dimension(180, 40));
        btnRemove.setBackground(BTN_DELETE_COLOR);
        btnRemove.setForeground(Color.WHITE);
        btnRemove.setBorderPainted(false);
        btnRemove.setFocusPainted(false);
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemove.addActionListener(this);

        btnReset = new JButton("Làm mới giỏ hàng");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.setPreferredSize(new Dimension(180, 40));
        btnReset.setBackground(BTN_NEUTRAL_COLOR);
        btnReset.setForeground(Color.WHITE);
        btnReset.setBorderPainted(false);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(this);

        controlPanel.add(quantityLabel);
        controlPanel.add(spnQuantityBottom);
        controlPanel.add(btnRemove);
        controlPanel.add(btnReset);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 15));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        // Label và textfield tổng tiền
        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
        lblTongTienLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        txtTongTien = new JTextField("0 VNĐ");
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTien.setPreferredSize(new Dimension(200, 40));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(Color.WHITE);
        txtTongTien.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        txtTongTien.setForeground(new Color(0, 128, 0)); // Màu xanh cho số tiền

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(PRIMARY_COLOR);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setBorderPainted(false);
        btnConfirm.setFocusPainted(false);
        btnConfirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirm.addActionListener(this);
        
        bottomPanel.add(lblTongTienLabel);
        bottomPanel.add(txtTongTien);

        bottomPanel.add(btnConfirm);

        return bottomPanel;
    }

    /**
     * Cập nhật tổng tiền hiển thị dựa trên các sản phẩm trong giỏ hàng
     * Được khai báo là public để có thể gọi từ XacNhanLapHoaDonFrame
     */
    public void updateTongTien() {
        double tongTien = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            Object thanhTienObj = modelGioHang.getValueAt(i, 4); // Cột "Thành tiền"
            if (thanhTienObj != null) {
                // Loại bỏ dấu phẩy phân cách và "VNĐ" rồi parse
                String thanhTienStr = thanhTienObj.toString().replace(",", "").replace(" VNĐ", "").trim();
                try {
                    tongTien += Double.parseDouble(thanhTienStr);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi parse thành tiền: " + thanhTienStr);
                }
            }
        }
        
        // Format và hiển thị tổng tiền
        DecimalFormat df = new DecimalFormat("#,###");
        txtTongTien.setText(df.format(tongTien) + " VNĐ");
    }

    /**
     * Tải dữ liệu thuốc từ database và hiển thị trên bảng
     * Đảm bảo luôn lấy dữ liệu mới nhất từ CSDL
     */
    private void loadDataThuoc() {
        try {
            // Đảm bảo có kết nối database
            ConnectDB.connect();
            
            // Tạo instance mới của DAO để đảm bảo lấy dữ liệu mới nhất
            thuocDAO = new ThuocDAO();
            
            // Lấy danh sách thuốc mới nhất từ database
            dsThuoc = thuocDAO.getAllThuocKemGia();
            
            // Xóa dữ liệu cũ trong bảng
            modelThuoc.setRowCount(0);
            
            // Thêm dữ liệu mới vào bảng
            for (ThuocKemGiaDTO thuoc : dsThuoc) {
                Object[] row = {
                        thuoc.getMaThuoc(),
                        thuoc.getTenThuoc(),
                        thuoc.getSoLuongTon() == 0 ? "Hết hàng" : thuoc.getSoLuongTon(),
                        String.format("%,.0f VNĐ", thuoc.getGiaBan()),
                        thuoc.getDonVi()
                };
                modelThuoc.addRow(row);
            }
            
            // Buộc bảng cập nhật lại giao diện
            modelThuoc.fireTableDataChanged();
            tblThuoc.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tải lại dữ liệu từ database, làm mới bảng thuốc
     * Gọi phương thức này khi cần cập nhật giao diện với dữ liệu mới nhất
     */
    public void reloadDataFromDatabase() {
        loadDataThuoc();
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object o = e.getSource();
        
        if (o == btnSearch) {
            timKiemThuoc();
        } else if (o == btnResetSearch) {
            resetTimKiem();
        } else if (o == btnAddToCart) {
            themVaoGioHang();
        } else if (o == btnRemove) {
            xoaKhoiGioHang();
        } else if (o == btnReset) {
            resetGioHangWithConfirm();
        } else if (o == btnConfirm) {
            xacNhanHoaDon();
        }
    }
    
    private void timKiemThuoc() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        
        if (keyword.isEmpty() || keyword.equals(SEARCH_PLACEHOLDER.toLowerCase())) {
            loadDataThuoc();
            return;
        }
        
        modelThuoc.setRowCount(0);
        int count = 0;
        
        for (ThuocKemGiaDTO thuoc : dsThuoc) {
            if (thuoc.getMaThuoc().toLowerCase().contains(keyword) || 
                thuoc.getTenThuoc().toLowerCase().contains(keyword)) {
                Object[] row = {
                    thuoc.getMaThuoc(),
                    thuoc.getTenThuoc(),
                    thuoc.getSoLuongTon() == 0 ? "Hết hàng" : thuoc.getSoLuongTon(),
                    String.format("%,.0f VNĐ", thuoc.getGiaBan()),
                    thuoc.getDonVi()
                };
                modelThuoc.addRow(row);
                count++;
            }
        }
        
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void resetTimKiem() {
        txtSearch.setText(SEARCH_PLACEHOLDER);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font("Arial", Font.ITALIC, 13));
        loadDataThuoc();
    }
    
    private void themVaoGioHang() {
        int selectedRow = tblThuoc.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc cần thêm vào giỏ hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = modelThuoc.getValueAt(selectedRow, 0).toString();
        String tenThuoc = modelThuoc.getValueAt(selectedRow, 1).toString();
        Object slTonObj = modelThuoc.getValueAt(selectedRow, 2);
        String donGiaStr = modelThuoc.getValueAt(selectedRow, 3).toString();
        
        // Kiểm tra hết hàng
        if (slTonObj.toString().equals("Hết hàng")) {
            JOptionPane.showMessageDialog(this, "Thuốc này đã hết hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int soLuongTon = Integer.parseInt(slTonObj.toString());
        int soLuongMua = (int) spnQuantityTop.getValue();
        
        // Kiểm tra số lượng
        if (soLuongMua > soLuongTon) {
            JOptionPane.showMessageDialog(this, "Số lượng mua vượt quá số lượng tồn kho!\nSố lượng tồn: " + soLuongTon, "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy đơn giá (bỏ VNĐ và dấu phẩy)
        double donGia = Double.parseDouble(donGiaStr.replaceAll("[^0-9]", ""));
        double thanhTien = donGia * soLuongMua;
        
        // Kiểm tra xem thuốc đã có trong giỏ hàng chưa
        boolean daTonTai = false;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            if (modelGioHang.getValueAt(i, 0).equals(maThuoc)) {
                // Cập nhật số lượng
                int soLuongCu = Integer.parseInt(modelGioHang.getValueAt(i, 2).toString());
                int soLuongMoi = soLuongCu + soLuongMua;
                
                if (soLuongMoi > soLuongTon) {
                    JOptionPane.showMessageDialog(this, "Tổng số lượng vượt quá số lượng tồn kho!\nSố lượng tồn: " + soLuongTon, "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                modelGioHang.setValueAt(soLuongMoi, i, 2);
                modelGioHang.setValueAt(String.format("%,.0f VNĐ", donGia * soLuongMoi), i, 4);
                daTonTai = true;
                break;
            }
        }
        
        // Nếu chưa có, thêm mới
        if (!daTonTai) {
            Object[] row = {
                maThuoc,
                tenThuoc,
                soLuongMua,
                String.format("%,.0f VNĐ", donGia),
                String.format("%,.0f VNĐ", thanhTien)
            };
            modelGioHang.addRow(row);
        }
        
        // Cập nhật tổng tiền
        updateTongTien();
        spnQuantityTop.setValue(1); // Reset về 1
    }
    
    // Xóa thuốc khỏi giỏ hàng
    private void xoaKhoiGioHang() {
        int selectedRow = tblGioHang.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc cần xóa khỏi giỏ hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy số lượng thuốc hiện tại trong giỏ hàng
        int soLuongHienTai = Integer.parseInt(modelGioHang.getValueAt(selectedRow, 2).toString());
        int soLuongXoa = (int) spnQuantityBottom.getValue();
        
        if (soLuongXoa >= soLuongHienTai) {
            // Xóa hẳn dòng
            modelGioHang.removeRow(selectedRow);
          } else {
            // Giảm số lượng
            int soLuongMoi = soLuongHienTai - soLuongXoa;
            String donGiaStr = modelGioHang.getValueAt(selectedRow, 3).toString();
            double donGia = Double.parseDouble(donGiaStr.replaceAll("[^0-9]", ""));
            
            modelGioHang.setValueAt(soLuongMoi, selectedRow, 2);
            modelGioHang.setValueAt(String.format("%,.0f VNĐ", donGia * soLuongMoi), selectedRow, 4);
         }
        
        // Cập nhật tổng tiền
        updateTongTien();
        spnQuantityBottom.setValue(1); // Reset về 1
    }
    
    // Reset giỏ hàng - làm public để có thể gọi từ XacNhanLapHoaDonFrame
    public void resetGioHang() {
        if (modelGioHang.getRowCount() == 0) {
            return; // Nếu giỏ hàng đã trống, không hiển thị thông báo và thoát
        }
        
        modelGioHang.setRowCount(0);
        // Cập nhật tổng tiền về 0
        updateTongTien();
    }
    
    // Phương thức gọi từ UI với xác nhận người dùng
    private void resetGioHangWithConfirm() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa toàn bộ giỏ hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            resetGioHang();
            JOptionPane.showMessageDialog(this, "Đã xóa toàn bộ giỏ hàng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Xác nhận hóa đơn (tạm thời chỉ thông báo)
    private void xacNhanHoaDon() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống! Vui lòng thêm thuốc vào giỏ hàng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Tính tổng tiền
        double tongTien = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            String thanhTienStr = modelGioHang.getValueAt(i, 4).toString();
            double thanhTien = Double.parseDouble(thanhTienStr.replaceAll("[^0-9]", ""));
            tongTien += thanhTien;
        }
        
        String message = String.format(
            "Xác nhận tạo hóa đơn?\n\n" +
            "Số mặt hàng: %d\n" +
            "Tổng tiền: %,.0f VNĐ", 
            modelGioHang.getRowCount(), 
            tongTien
        );
        
        int confirm = JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Tạo mã phiếu đặt tự động theo chuỗi từ database
            String maPhieuDat = phieuDatDAO.generateNextMaPhieuDat();
            
            // Thu thập dữ liệu từ giỏ hàng với maLo
            ArrayList<Object[]> chiTietData = new ArrayList<>();
            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                String maThuoc = modelGioHang.getValueAt(i, 0).toString();
                String tenThuoc = modelGioHang.getValueAt(i, 1).toString();
                int soLuong = Integer.parseInt(modelGioHang.getValueAt(i, 2).toString());
                String donGiaStr = modelGioHang.getValueAt(i, 3).toString();
                float donGia = Float.parseFloat(donGiaStr.replaceAll("[^0-9]", ""));
                
                // Lấy maLo đầu tiên có sẵn cho thuốc này từ database
                String maLo = getMaLoForThuoc(maThuoc);
                
                Object[] data = {maThuoc, tenThuoc, soLuong, donGia, soLuong * donGia, maLo};
                chiTietData.add(data);
            }
            
            // Mở frame xác nhận lập phiếu đặt với callback
            XacNhanLapPhieuDatThuocFrame frame = new XacNhanLapPhieuDatThuocFrame(chiTietData, tongTien, maPhieuDat, maNhanVien, this);
            frame.setVisible(true);
        }
    }
    
    /**
     * Phương thức xử lý khi nhận callback từ XacNhanLapPhieuDatFrame
     * Được gọi khi phiếu đặt thuốc đã được lập thành công
     * LƯU Ý: Không cập nhật tồn kho ở đây vì phiếu đặt không ảnh hưởng đến tồn kho
     * Tồn kho chỉ được trừ khi lập hóa đơn từ phiếu đặt
     */
    public void onPhieuDatSuccess(ArrayList<Object[]> dsChiTiet, String maPhieuDat) {
        try {
            // 1. Reset giỏ hàng và tổng tiền sau khi lập phiếu đặt thành công
            resetGioHang();
            updateTongTien();

            // 2. Lưu vị trí đang chọn để khôi phục sau khi load lại
            int selectedRow = tblThuoc.getSelectedRow();

            // 3. Load lại dữ liệu từ database để làm mới giao diện
            // (Tồn kho không thay đổi, chỉ làm mới để đảm bảo hiển thị dữ liệu mới nhất)
            SwingUtilities.invokeLater(() -> {
                try {
                    // Tải lại dữ liệu từ database
                    loadDataThuoc();

                    // Khôi phục vị trí đã chọn (nếu có)
                    if (selectedRow >= 0 && selectedRow < tblThuoc.getRowCount()) {
                        tblThuoc.setRowSelectionInterval(selectedRow, selectedRow);
                    }

                    // Hiển thị thông báo kết quả cho người dùng
                    JOptionPane.showMessageDialog(LapPhieuDatThuocPanel.this, 
                            "Đã lập phiếu đặt thuốc thành công!\nMã phiếu đặt: " + maPhieuDat + "\nSố mặt hàng: " + dsChiTiet.size() + "\nTồn kho không thay đổi.", 
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LapPhieuDatThuocPanel.this, "Đã xảy ra lỗi khi tải lại dữ liệu sau khi lập phiếu đặt.\nVui lòng nhấn làm mới để tải lại dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho người dùng
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi sau khi lập phiếu đặt thuốc.\nPhiếu đặt đã được lưu thành công.\nLỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Lấy mã lô đầu tiên có sẵn cho thuốc được chỉ định
     * @param maThuoc mã thuốc cần lấy maLo
     * @return mã lô đầu tiên có sẵn, hoặc "N/A" nếu không tìm thấy
     */
    private String getMaLoForThuoc(String maThuoc) {
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT TOP 1 maLo FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1 AND soLuong > 0 ORDER BY ngaySanXuat";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String maLo = rs.getString("maLo");
                rs.close();
                stmt.close();
                return maLo;
            }
            
            rs.close();
            stmt.close();
            return "N/A"; // Không tìm thấy lô nào có sẵn
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    public void refreshData() {
        try {
            // Reset giỏ hàng
            resetGioHang();
            updateTongTien();
            
            // Tải lại dữ liệu thuốc
            loadDataThuoc();
            
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được cập nhật!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lại dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
