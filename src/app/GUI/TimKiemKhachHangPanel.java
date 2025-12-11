package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax. swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB. ConnectDB;
import app. DAO.KhachHangDAO;
import app.Entity.KhachHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event. ActionListener;
import java.util.ArrayList;

public class TimKiemKhachHangPanel extends JPanel implements ActionListener {

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);

    // Label
    private JLabel lblTieuDe;
    private JLabel lblMaKH;
    private JLabel lblTenKH;
    private JLabel lblSoDienThoai;
    private JLabel lblDiemTichLuy;

    // TextField
    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JTextField txtSoDienThoai;
    private JTextField txtDiemTichLuy;

    // Button
    private JButton btnXoaTrang;
    private JButton btnTimKiem;

    // Table
    private DefaultTableModel dtmTable;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");

    // Data
    private ArrayList<KhachHang> dsKhachHang;
    private KhachHangDAO khachHangDao = new KhachHangDAO();

    public TimKiemKhachHangPanel() {
    	FlatLightLaf. setup();
        setLayout(new BorderLayout());

        ConnectDB.getInstance().connect();
        dsKhachHang = khachHangDao.getAllKhachHang();

        JPanel topPanel = createTopPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = taoCenterPanel();

        mainPanel. add(centerPanel, BorderLayout. CENTER);

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
        JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout. RIGHT));

        // Tiêu đề
        lblTieuDe = new JLabel("TÌM KIẾM KHÁCH HÀNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        pnlTopOfMain. add(lblTieuDe);

        // Label
        lblMaKH = new JLabel("Mã khách hàng:");
        lblTenKH = new JLabel("Tên khách hàng:");
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblDiemTichLuy = new JLabel("Điểm tích lũy tối thiểu:");

        // TextField
        txtMaKH = new JTextField();
        txtTenKH = new JTextField();
        txtSoDienThoai = new JTextField();
        txtDiemTichLuy = new JTextField();

        // Button
        btnXoaTrang = new JButton("Xóa trắng");
        btnTimKiem = new JButton("Tìm kiếm");

        // Panel dòng nhập
        JPanel pnlr1 = new JPanel(new BorderLayout());
        pnlr1.add(lblMaKH, BorderLayout. WEST);
        pnlr1.add(txtMaKH);
        pnlr1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr2 = new JPanel(new BorderLayout());
        pnlr2.add(lblTenKH, BorderLayout.WEST);
        pnlr2.add(txtTenKH);
        pnlr2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr3 = new JPanel(new BorderLayout());
        pnlr3.add(lblSoDienThoai, BorderLayout. WEST);
        pnlr3.add(txtSoDienThoai);
        pnlr3.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel pnlr4 = new JPanel(new BorderLayout());
        pnlr4.add(lblDiemTichLuy, BorderLayout.WEST);
        pnlr4.add(txtDiemTichLuy);
        pnlr4.setBorder(BorderFactory. createEmptyBorder(0, 10, 0, 10));

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
                lblMaKH, lblTenKH, lblSoDienThoai, lblDiemTichLuy
        };
        for (JLabel item : lblItems) {
        	 item.setFont(new Font("Segoe UI", Font. PLAIN, 15));
            item.setPreferredSize(new Dimension(130, 0));
        }

        // Style textfield
        JTextField[] txtItems = {
                txtMaKH, txtTenKH, txtSoDienThoai, txtDiemTichLuy
        };
        for (JTextField item : txtItems) {
            item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            item.setBackground(new Color(245, 245, 245));
            item.setPreferredSize(new Dimension(200, 30));
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(3, 10, 3, 10)
            ));
        }

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

        String[] cols = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy"};

        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        load_dataKhachHang(dsKhachHang);

        table = new JTable(dtmTable) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (! isRowSelected(row)) {
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
        header.setPreferredSize(new Dimension(header. getWidth(), 40));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cell content
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color. WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsKhachHang.size());
        lblTongSoBanGhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongSoBanGhi. setBorder(new EmptyBorder(15, 0, 0, 0));

        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout. SOUTH);
        return centerPanel;
    }

    public void load_dataKhachHang(ArrayList<KhachHang> dsKhachHang) {
        dtmTable.setRowCount(0);
        for (KhachHang khach : dsKhachHang) {
            Object[] rowData = {
                    khach.getMaKH(),
                    khach.getTenKH(),
                    khach.getSoDienThoai(),
                    khach.getDiemTichLuy()
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi. setText("Tổng số bản ghi: " + dsKhachHang.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnXoaTrang) {
            txtMaKH.setText("");
            txtTenKH.setText("");
            txtSoDienThoai.setText("");
            txtDiemTichLuy. setText("");
            load_dataKhachHang(dsKhachHang);
        } else if (o == btnTimKiem) {

            String maKHTim = txtMaKH. getText().trim().toLowerCase();
            String tenKHTim = txtTenKH.getText().trim().toLowerCase();
            String sdtTim = txtSoDienThoai.getText().trim().toLowerCase();
            String diemStr = txtDiemTichLuy.getText().trim();

            Integer diemTim = null;

            if (! diemStr.isEmpty()) {
                try {
                    diemTim = Integer.parseInt(diemStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Điểm tích lũy phải là số nguyên! ",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            ArrayList<KhachHang> ketQua = new ArrayList<>();

            for (KhachHang kh : dsKhachHang) {
                boolean match = true;

                if (! maKHTim.isEmpty() &&
                        !kh.getMaKH().toLowerCase().contains(maKHTim)) {
                    match = false;
                }

                if (match && !tenKHTim.isEmpty() &&
                        !kh. getTenKH().toLowerCase().contains(tenKHTim)) {
                    match = false;
                }

                if (match && ! sdtTim.isEmpty() &&
                        (kh.getSoDienThoai() == null ||
                                !kh.getSoDienThoai().toLowerCase().contains(sdtTim))) {
                    match = false;
                }

                // Tìm khách có điểm tích lũy >= giá trị nhập
                if (match && diemTim != null &&
                        (kh.getDiemTichLuy() == 0 || kh.getDiemTichLuy() < diemTim)) {
                    match = false;
                }

                if (match) {
                    ketQua.add(kh);
                }
            }

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả phù hợp!",
                        "Thông báo", JOptionPane. INFORMATION_MESSAGE);
                load_dataKhachHang(dsKhachHang);
                return;
            }

            load_dataKhachHang(ketQua);
        }
    }
}