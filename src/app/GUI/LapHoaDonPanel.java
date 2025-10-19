package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.DAO.ThuocDAO;
import app.DAO.HoaDonDAO;
import app.Entity.Thuoc;

public class LapHoaDonPanel extends JPanel implements ActionListener, HoaDonCallback {
    
    private DefaultTableModel modelThuoc; // Model cho bảng danh sách thuốc
    private DefaultTableModel modelGioHang; // Model cho bảng giỏ hàng
    private JTable tblThuoc;
    private JTable tblGioHang;
    private ThuocDAO thuocDAO;
    private ArrayList<Thuoc> dsThuoc;
    
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

    public LapHoaDonPanel(String maNhanVien) {
        this.maNhanVien = maNhanVien;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Khởi tạo kết nối database và DAO
        ConnectDB.connect();
        thuocDAO = new ThuocDAO();
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
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("LẬP HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);

        centerPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel giữa chứa 2 bảng
        JPanel pnlTable = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlTable.setBackground(Color.WHITE);

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
        searchPanel.setBackground(Color.WHITE);

        txtSearch = new JTextField(25);
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setFont(new Font("Arial", Font.ITALIC, 13));
        txtSearch.setBackground(new Color(245, 245, 245));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Gán placeholder lúc đầu
        txtSearch.setText(SEARCH_PLACEHOLDER);
        
        // Lắng nghe khi focus
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals(SEARCH_PLACEHOLDER)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                    txtSearch.setFont(new Font("Arial", Font.PLAIN, 13));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(SEARCH_PLACEHOLDER);
                    txtSearch.setForeground(Color.GRAY);
                    txtSearch.setFont(new Font("Arial", Font.ITALIC, 13));
                }
            }
        });

        btnSearch = new JButton("Tìm");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSearch.setPreferredSize(new Dimension(80, 30));
        btnSearch.setBackground(new Color(240, 250, 240)); // Màu xanh nhạt
        btnSearch.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34))); // Viền xanh đậm
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(this);
        
        btnResetSearch = new JButton("Reset");
        btnResetSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        btnResetSearch.setPreferredSize(new Dimension(80, 30));
        btnResetSearch.setBackground(Color.WHITE);
        btnResetSearch.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        panel.setBackground(Color.WHITE);

        // Bảng dữ liệu với cột "Số lượng tồn"
        String[] columns = {"Mã thuốc", "Tên thuốc", "Số lượng tồn", "Đơn giá", "Đơn vị"};
        
        modelThuoc = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp
            }
        };

        tblThuoc = new JTable(modelThuoc);
        tblThuoc.setRowHeight(30);
        tblThuoc.setBackground(Color.WHITE); // Nền trắng cho các dòng
        tblThuoc.setGridColor(Color.LIGHT_GRAY);
        tblThuoc.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Chỉ tô màu header
        tblThuoc.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblThuoc.getTableHeader().setBackground(new Color(240, 250, 240)); // Màu xanh nhạt cho header
        tblThuoc.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel điều khiển dưới bảng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(Color.WHITE);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        spnQuantityTop = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnQuantityTop.setPreferredSize(new Dimension(60, 25));

        btnAddToCart = new JButton("Thêm vào giỏ hàng");
        btnAddToCart.setFont(new Font("Arial", Font.PLAIN, 13));
        btnAddToCart.setPreferredSize(new Dimension(160, 35));
        btnAddToCart.setBackground(new Color(240, 250, 240));
        btnAddToCart.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34))); // Viền xanh đậm
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
        panel.setBackground(Color.WHITE);

        // Tiêu đề "GIỎ HÀNG"
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        titlePanel.setBackground(Color.WHITE);
        JLabel lblCartTitle = new JLabel("GIỎ HÀNG");
        lblCartTitle.setFont(new Font("Arial", Font.BOLD, 16));
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

        tblGioHang = new JTable(modelGioHang);
        tblGioHang.setRowHeight(30);
        tblGioHang.setBackground(Color.WHITE); // Nền trắng cho các dòng
        tblGioHang.setGridColor(Color.LIGHT_GRAY);
        tblGioHang.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Chỉ tô màu header
        tblGioHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tblGioHang.getTableHeader().setBackground(new Color(240, 250, 240)); // Màu xanh nhạt cho header
        tblGioHang.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tblGioHang);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel điều khiển dưới bảng giỏ hàng
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(Color.WHITE);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        spnQuantityBottom = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnQuantityBottom.setPreferredSize(new Dimension(60, 25));

        btnRemove = new JButton("Xóa khỏi giỏ hàng");
        btnRemove.setFont(new Font("Arial", Font.PLAIN, 13));
        btnRemove.setPreferredSize(new Dimension(160, 35));
        btnRemove.setBackground(new Color(255, 240, 240));
        btnRemove.setBorder(BorderFactory.createLineBorder(Color.RED));
        btnRemove.setFocusPainted(false);
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemove.addActionListener(this);

        btnReset = new JButton("Reset giỏ hàng");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 13));
        btnReset.setPreferredSize(new Dimension(140, 35));
        btnReset.setBackground(Color.WHITE);
        btnReset.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        // Label và textfield tổng tiền
        JLabel lblTongTienLabel = new JLabel("Tổng tiền:");
        lblTongTienLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        txtTongTien = new JTextField("0 VNĐ");
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        txtTongTien.setPreferredSize(new Dimension(200, 40));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(Color.WHITE);
        txtTongTien.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtTongTien.setForeground(new Color(0, 128, 0)); // Màu xanh cho số tiền

        btnConfirm = new JButton("Xác nhận");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 14));
        btnConfirm.setPreferredSize(new Dimension(150, 40));
        btnConfirm.setBackground(new Color(240, 240, 240)); // Màu xám nhạt
        btnConfirm.setForeground(Color.BLACK); // Chữ màu đen
        btnConfirm.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Viền màu xám
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
            dsThuoc = thuocDAO.getAllThuoc();
            
            // Xóa dữ liệu cũ trong bảng
            modelThuoc.setRowCount(0);
            
            // Thêm dữ liệu mới vào bảng
            for (Thuoc thuoc : dsThuoc) {
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
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi load dữ liệu thuốc: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
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
        
        for (Thuoc thuoc : dsThuoc) {
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
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy thuốc nào!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc cần thêm vào giỏ hàng!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maThuoc = modelThuoc.getValueAt(selectedRow, 0).toString();
        String tenThuoc = modelThuoc.getValueAt(selectedRow, 1).toString();
        Object slTonObj = modelThuoc.getValueAt(selectedRow, 2);
        String donGiaStr = modelThuoc.getValueAt(selectedRow, 3).toString();
        
        // Kiểm tra hết hàng
        if (slTonObj.toString().equals("Hết hàng")) {
            JOptionPane.showMessageDialog(this, 
                "Thuốc này đã hết hàng!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int soLuongTon = Integer.parseInt(slTonObj.toString());
        int soLuongMua = (int) spnQuantityTop.getValue();
        
        // Kiểm tra số lượng
        if (soLuongMua > soLuongTon) {
            JOptionPane.showMessageDialog(this, 
                "Số lượng mua vượt quá số lượng tồn kho!\nSố lượng tồn: " + soLuongTon, 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, 
                        "Tổng số lượng vượt quá số lượng tồn kho!\nSố lượng tồn: " + soLuongTon, 
                        "Thông báo", 
                        JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc cần xóa khỏi giỏ hàng!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
                "Giỏ hàng đang trống!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa toàn bộ giỏ hàng?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            resetGioHang();
            JOptionPane.showMessageDialog(this, 
                "Đã xóa toàn bộ giỏ hàng!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Xác nhận hóa đơn (tạm thời chỉ thông báo)
    private void xacNhanHoaDon() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Giỏ hàng đang trống! Vui lòng thêm thuốc vào giỏ hàng.", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
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
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            message, 
            "Xác nhận hóa đơn", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Tạo mã hóa đơn tự động
            HoaDonDAO hoaDonDAO = new HoaDonDAO();
            String maHoaDon = hoaDonDAO.generateMaHoaDon();
            
            // Chuẩn bị dữ liệu chi tiết hóa đơn từ giỏ hàng
            ArrayList<Object[]> chiTietData = new ArrayList<>();
            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                String maThuoc = modelGioHang.getValueAt(i, 0).toString();
                String tenThuoc = modelGioHang.getValueAt(i, 1).toString();
                int soLuong = Integer.parseInt(modelGioHang.getValueAt(i, 2).toString());
                String donGiaStr = modelGioHang.getValueAt(i, 3).toString();
                float donGia = Float.parseFloat(donGiaStr.replaceAll("[^0-9]", ""));
                
                Object[] data = {maThuoc, tenThuoc, soLuong, donGia};
                chiTietData.add(data);
            }
            
            // Mở frame xác nhận lập hóa đơn với callback
            XacNhanLapHoaDonFrame frame = new XacNhanLapHoaDonFrame(chiTietData, tongTien, maHoaDon, maNhanVien, this);
            frame.setVisible(true);
        }
    }
    
    /**
     * Phương thức xử lý khi nhận callback từ XacNhanLapHoaDonFrame
     * Được gọi khi hóa đơn đã được lập thành công
     * Cập nhật lại số lượng tồn kho ngay lập tức mà không cần load lại từ database
     */
    @Override
    public void onHoaDonSuccess(ArrayList<Object[]> dsChiTiet, String maHoaDon) {
        try {
            // 1. Reset giỏ hàng và tổng tiền trước
            resetGioHang();
            updateTongTien();
            
            // 2. Tải lại dữ liệu từ database để đảm bảo hiển thị số lượng tồn chính xác nhất
            // Lưu vị trí đang chọn và scroll position hiện tại để khôi phục sau khi load lại
            int selectedRow = tblThuoc.getSelectedRow();
            
            // Force load lại dữ liệu từ database ngay lập tức
            SwingUtilities.invokeLater(() -> {
                try {
                    // Tải lại dữ liệu từ database
                    loadDataThuoc();
                    
                    // Khôi phục vị trí đã chọn (nếu có)
                    if (selectedRow >= 0 && selectedRow < tblThuoc.getRowCount()) {
                        tblThuoc.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                    
                    // Hiển thị thông báo kết quả cho người dùng
                    JOptionPane.showMessageDialog(this,
                        "Đã lập hóa đơn thành công!\n" + 
                        "Mã hóa đơn: " + maHoaDon + "\n" +
                        "Số mặt hàng: " + dsChiTiet.size(),
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                        
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "Đã xảy ra lỗi khi cập nhật dữ liệu sau khi lập hóa đơn.\n" +
                        "Vui lòng nhấn Reset để tải lại dữ liệu.",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho người dùng
            JOptionPane.showMessageDialog(this,
                "Đã xảy ra lỗi khi cập nhật dữ liệu sau khi lập hóa đơn.\n" +
                "Vui lòng nhấn Reset để tải lại dữ liệu.",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}
