package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB. ConnectDB;
import app. DAO.NhanVienDAO;
import app.DAO. ChucVuDAO;
import app.Entity.NhanVien;
import app.Entity.ChucVu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt. event.ActionListener;
import java.util.ArrayList;

public class TimKiemNhanVienPanel extends JPanel implements ActionListener {

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);

    // Label
    private JLabel lblTieuDe;
    private JLabel lblMaNV;
    private JLabel lblTenNV;
    private JLabel lblSoDienThoai;
    private JLabel lblChucVu;

    // TextField
    private JTextField txtMaNV;
    private JTextField txtTenNV;
    private JTextField txtSoDienThoai;

    // ComboBox chức vụ (tìm theo combobox)
    private JComboBox<String> cboChucVu;
    private ArrayList<ChucVu> dsChucVu;
    private ChucVuDAO chucVuDAO = new ChucVuDAO();

    // Button
    private JButton btnXoaTrang;
    private JButton btnTimKiem;

    // Table
    private DefaultTableModel dtmTable;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");

    // Data
    private ArrayList<NhanVien> dsNhanVien;
    private NhanVienDAO nhanVienDao = new NhanVienDAO();

    public TimKiemNhanVienPanel() {
    	FlatLightLaf.setup();
        setLayout(new BorderLayout());

        ConnectDB. getInstance().connect();
        dsNhanVien = nhanVienDao.getAllNhanVien();

        JPanel topPanel = createTopPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = taoCenterPanel();

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Enter = click tìm kiếm
        getInputMap(JComponent. WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "clickSearch");
        getActionMap().put("clickSearch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTimKiem.doClick();
            }
        });

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        JPanel pnlTopOfMain = new JPanel();
        JPanel pnlCenterOfMain = new JPanel(new GridLayout(2, 2, 10, 5));
        JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Tiêu đề
        lblTieuDe = new JLabel("TÌM KIẾM NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe. setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        pnlTopOfMain. add(lblTieuDe);

        // Label
        lblMaNV = new JLabel("Mã nhân viên:");
        lblTenNV = new JLabel("Tên nhân viên:");
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblChucVu = new JLabel("Chức vụ:");

        // TextField
        txtMaNV = new JTextField(15);
        txtTenNV = new JTextField(15);
        txtSoDienThoai = new JTextField(15);
        
        // ComboBox chức vụ
        dsChucVu = chucVuDAO.getAllChucVu();
        cboChucVu = new JComboBox<>();
        cboChucVu.addItem(""); // Không chọn
        for (ChucVu cv : dsChucVu) {
            cboChucVu.addItem(cv.getTenChucVu());
            System.out.println(cv);
        }

        // Button
        btnXoaTrang = new JButton("Xóa trắng");
        btnTimKiem = new JButton("Tìm kiếm");

        // Panel từng dòng
        JPanel pnlr1 = new JPanel(new BorderLayout());
        pnlr1.add(lblMaNV, BorderLayout. WEST);
        pnlr1.add(txtMaNV);
        pnlr1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr2 = new JPanel(new BorderLayout());
        pnlr2.add(lblTenNV, BorderLayout.WEST);
        pnlr2.add(txtTenNV);
        pnlr2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr3 = new JPanel(new BorderLayout());
        pnlr3.add(lblSoDienThoai, BorderLayout.WEST);
        pnlr3.add(txtSoDienThoai);
        pnlr3.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr4 = new JPanel(new BorderLayout());
        pnlr4.add(lblChucVu, BorderLayout. WEST);
        pnlr4.add(cboChucVu);
        pnlr4.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        pnlCenterOfMain.add(pnlr1);
        pnlCenterOfMain.add(pnlr2);
        pnlCenterOfMain. add(pnlr3);
        pnlCenterOfMain.add(pnlr4);

        // Style button
        JButton[] btnList = {btnXoaTrang, btnTimKiem};
        for (JButton item : btnList) {
            item.setFont(new Font("Segoe UI", Font.BOLD, 16));
            item.setForeground(Color.BLACK);
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(15, 45, 15, 45)
            ));
            item.setFocusPainted(false);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));
            item.addActionListener(this);
        }

        // Style label
        JLabel[] lblItems = {
                lblMaNV, lblTenNV, lblSoDienThoai, lblChucVu
        };
        for (JLabel item : lblItems) {
            item.setFont(new Font("Segoe UI", Font. PLAIN, 15));
            item.setPreferredSize(new Dimension(130, 0));
        }

        // Style textfield
        JTextField[] txtItems = {
                txtMaNV, txtTenNV, txtSoDienThoai
        };
        for (JTextField item : txtItems) {
            item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            item.setBackground(new Color(245, 245, 245));
            item.setPreferredSize(new Dimension(200, 30));
            item.setBorder(BorderFactory. createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(3, 10, 3, 10)
            ));
        }

        // Style comboBox chức vụ
        cboChucVu.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        cboChucVu.setForeground(Color.BLUE);
        cboChucVu. setBackground(new Color(245, 245, 245));
        cboChucVu.setPreferredSize(new Dimension(200, 30));
        cboChucVu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Bottom buttons
        pnlBottomOfMain.add(btnXoaTrang);
        pnlBottomOfMain.add(Box.createHorizontalStrut(10));
        pnlBottomOfMain.add(btnTimKiem);
        pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add vào main
        pnlMain.add(pnlTopOfMain, BorderLayout.NORTH);
        pnlCenterOfMain.setPreferredSize(new Dimension(0, 80));
        pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
        pnlMain.add(pnlBottomOfMain, BorderLayout.SOUTH);

        return pnlMain;
    }

    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Table Panel
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"Mã nhân viên", "Tên nhân viên", "Chức vụ", "Số điện thoại"};

        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData_NhanVien(dsNhanVien);

        table = new JTable(dtmTable) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };

        // Style table
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerCenter = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerCenter.setHorizontalAlignment(JLabel.CENTER);

        // Center align cell content
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsNhanVien.size());
        lblTongSoBanGhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongSoBanGhi. setBorder(new EmptyBorder(15, 0, 0, 0));

        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout.SOUTH);
        return centerPanel;
    }

    public void loadData_NhanVien(ArrayList<NhanVien> dsNhanVien) {
        dtmTable.setRowCount(0);
        for (NhanVien nhanvien : dsNhanVien) {
            Object[] rowData = {
                    nhanvien.getMaNV(),
                    nhanvien.getTenNV(),
                    nhanvien.getmaChucVu(),
                    nhanvien.getSoDienThoai()
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi. setText("Tổng số bản ghi: " + dsNhanVien.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnXoaTrang) {
            txtMaNV.setText("");
            txtTenNV.setText("");
            txtSoDienThoai.setText("");
            cboChucVu.setSelectedIndex(0);
            loadData_NhanVien(dsNhanVien);
        } else if (o == btnTimKiem) {

            String maNVTim = txtMaNV.getText().trim().toLowerCase();
            String tenNVTim = txtTenNV. getText().trim().toLowerCase();
            String sdtTim = txtSoDienThoai.getText().trim().toLowerCase();
            String chucVuChon = (String) cboChucVu.getSelectedItem(); // tên chức vụ

            // Lấy mã chức vụ từ DAO nếu người dùng có chọn
            String maChucVuTim = null;
            if (chucVuChon != null && !chucVuChon.isEmpty()) {
                ChucVu cv = chucVuDAO.getByName(chucVuChon);
                if (cv != null) {
                    maChucVuTim = cv.getMaChucVu();
                }
            }

            ArrayList<NhanVien> ketQua = new ArrayList<>();

            for (NhanVien nv : dsNhanVien) {
                boolean match = true;

                if (! maNVTim.isEmpty() &&
                        !nv.getMaNV().toLowerCase().contains(maNVTim)) {
                    match = false;
                }

                if (match && !tenNVTim.isEmpty() &&
                        ! nv.getTenNV().toLowerCase().contains(tenNVTim)) {
                    match = false;
                }

                if (match && !sdtTim.isEmpty()) {
                    String sdt = nv.getSoDienThoai() == null ? "" : nv.getSoDienThoai();
                    if (!sdt.toLowerCase().contains(sdtTim)) {
                        match = false;
                    }
                }

                // Lọc theo chức vụ (so sánh mã chức vụ trong nhân viên với mã chức vụ lấy từ DAO)
                if (match && maChucVuTim != null) {
                    String maCV_NV = nv.getmaChucVu() == null ? "" : nv. getmaChucVu();
                    if (!maCV_NV.equalsIgnoreCase(maChucVuTim)) {
                        match = false;
                    }
                }

                if (match) {
                    ketQua.add(nv);
                }
            }

            if (ketQua.isEmpty()) {
                JOptionPane. showMessageDialog(this,
                        "Không tìm thấy kết quả phù hợp! ",
                        "Thông báo", JOptionPane. INFORMATION_MESSAGE);
                loadData_NhanVien(dsNhanVien);
                return;
            }

            loadData_NhanVien(ketQua);
        }
    }
}