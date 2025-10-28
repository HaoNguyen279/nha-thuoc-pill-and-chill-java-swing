package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimKiemKhachHangPanel extends JPanel implements ActionListener{ 
    private DefaultTableModel dtmTable;
    private ArrayList<KhachHang> dsKhachHang;
    private JButton btnTim;
    private JButton btnReset;
    private JTextField txtTim;
    private String tieuChi = "Số điện thoại";
    private JComboBox<String> cboTieuChi;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");
    
    public TimKiemKhachHangPanel(){
        setLayout(new BorderLayout());
        
        ConnectDB.getInstance().connect();
        KhachHangDAO khachHangDao = new KhachHangDAO();
        dsKhachHang = khachHangDao.getAllKhachHang();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel centerPanel = taoCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("ENTER"), "clickLogin");
        getActionMap().put("clickLogin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTim.doClick();
            }
        });
        setVisible(true);
    }
    
    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("TÌM KIẾM KHÁCH HÀNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel searchPanel = taoSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);
        
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] cols = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        load_dataKhachHang(dsKhachHang);
        
        table = new JTable(dtmTable);
        
        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(144, 238, 144));
        header.setForeground(new Color(51, 51, 51));
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        
        // Style table
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(220, 255, 220));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsKhachHang.size());
        lblTongSoBanGhi.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTongSoBanGhi.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout.SOUTH);
        return centerPanel;
    }
    
    private JPanel taoSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        searchPanel.setBackground(Color.WHITE);
        
        // Text field
        txtTim = new JTextField(25);
        txtTim.setText("Nhập từ khóa tìm kiếm...");
        txtTim.setForeground(Color.GRAY);
        txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
        txtTim.setBackground(Color.WHITE);
        txtTim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Add focus listener for placeholder text
        txtTim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtTim.getText().equals("Nhập từ khóa tìm kiếm...")) {
                    txtTim.setText("");
                    txtTim.setForeground(Color.BLACK);
                    txtTim.setFont(new Font("Arial", Font.PLAIN, 14));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTim.getText().isEmpty()) {
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
                }
            }
        });
        
        // Button Tìm - Green style
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Arial", Font.BOLD, 14));
        btnTim.setPreferredSize(new Dimension(90, 40));
        btnTim.setBackground(new Color(144, 238, 144));
        btnTim.setForeground(new Color(51, 51, 51));
        btnTim.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 100), 1));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        // Button Reset - White style
        btnReset = new JButton("Làm mới");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReset.setPreferredSize(new Dimension(90, 40));
        btnReset.setBackground(Color.WHITE);
        btnReset.setForeground(new Color(51, 51, 51));
        btnReset.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(this);
        
        // ComboBox
        String[] tieuChiTim = {"Số điện thoại", "Mã khách hàng", "Tên khách hàng"};
        cboTieuChi = new JComboBox<String>(tieuChiTim);
        cboTieuChi.setFont(new Font("Arial", Font.PLAIN, 14));
        cboTieuChi.setBackground(Color.WHITE);
        cboTieuChi.setForeground(new Color(51, 51, 51));
        cboTieuChi.setFocusable(false);
        cboTieuChi.setPreferredSize(new Dimension(150, 40));
        cboTieuChi.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        cboTieuChi.addActionListener(this);
        
        searchPanel.add(txtTim);
        searchPanel.add(btnTim);
        searchPanel.add(btnReset);
        searchPanel.add(cboTieuChi);
        
        return searchPanel;
    }
   
    public void load_dataKhachHang(ArrayList<KhachHang> dsKhachHang) {
        dtmTable.setRowCount(0);
        for(KhachHang khach : dsKhachHang) {
            Object[] rowData = {
                khach.getMaKH(),
                khach.getTenKH(),
                khach.getSoDienThoai(),
                khach.getDiemTichLuy()
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi.setText("Tổng số bản ghi: " + dsKhachHang.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnTim) {
            ArrayList<KhachHang> ketQuaTim = new ArrayList<KhachHang>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm...") || timString.isBlank()) {
                load_dataKhachHang(dsKhachHang);
            }
            else {
                if(tieuChi.equals("Mã khách hàng")) {
                    for(KhachHang khachhang : dsKhachHang) {
                        if(khachhang.getMaKH().toLowerCase().matches("^.*" + timString + ".*")) {
                            ketQuaTim.add(khachhang);
                        }
                    }
                }
                else if(tieuChi.equals("Tên khách hàng")) {
                    for(KhachHang khachhang : dsKhachHang) {
                        if(khachhang.getTenKH().toLowerCase().matches("^.*" + timString + ".*")) {
                            ketQuaTim.add(khachhang);
                        }
                    }
                }
                else { // Số điện thoại
                    for(KhachHang khachhang : dsKhachHang) {
                        if(khachhang.getSoDienThoai().toLowerCase().matches("^.*" + timString + ".*")) {
                            ketQuaTim.add(khachhang);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    load_dataKhachHang(dsKhachHang);
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
                } else {
                    load_dataKhachHang(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim.setText("Nhập từ khóa tìm kiếm...");
            txtTim.setForeground(Color.GRAY);
            txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Số điện thoại";
            load_dataKhachHang(dsKhachHang);
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
            System.out.println(tieuChi);
        }
    }
}