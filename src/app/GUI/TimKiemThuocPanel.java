package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.ThuocDAO;
import app.Entity.Thuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimKiemThuocPanel extends JPanel implements ActionListener{ 
    private DefaultTableModel dtmTable;
    private ArrayList<Thuoc> dsThuoc;
    private JButton btnTim;
    private JButton btnReset;
    private JTextField txtTim;
    private String tieuChi = "Mã thuốc";
    private JComboBox<String> cboTieuChi;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");
    private ThuocDAO thuocdao = new ThuocDAO();
    
    public TimKiemThuocPanel() {
        setLayout(new BorderLayout());
        
        ConnectDB.getInstance().connect();
        
        dsThuoc = thuocdao.getAllThuocCoTenNSX();
        
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
        JLabel titleLabel = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
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

        String[] cols = {"Mã thuốc", "Tên thuốc", "Số lượng tồn", 
                        "Giá bán", "Đơn vị", "Mã NSX"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData_Thuoc(dsThuoc);
        
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
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsThuoc.size());
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
        String[] tieuChiTim = {"Mã thuốc", "Mã lô", "Tên thuốc"};
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
   
    public void loadData_Thuoc(ArrayList<Thuoc> dsThuoc) {
        dtmTable.setRowCount(0);
        for(Thuoc thuoc : dsThuoc) {
            Object[] rowData = {
                thuoc.getMaThuoc(),
                thuoc.getTenThuoc(),
                thuoc.getSoLuongTon() == 0 ? "Hết hàng" : thuoc.getSoLuongTon(),
                thuoc.getGiaBan(),
                thuoc.getDonVi(),
               
                thuoc.getMaNSX()
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi.setText("Tổng số bản ghi: " + dsThuoc.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnTim) {
            ArrayList<Thuoc> ketQuaTim = new ArrayList<Thuoc>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm...") || timString.isBlank()) {
                loadData_Thuoc(dsThuoc);
            }
            else {
                if(tieuChi.equals("Mã thuốc")) {
                    for(Thuoc thuoc: dsThuoc) {
                        if(thuoc.getMaThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(thuoc);
                        }
                    }
                }
                
                else { // Tên thuốc
                    for(Thuoc thuoc: dsThuoc) {
                        if(thuoc.getTenThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(thuoc);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData_Thuoc(dsThuoc);
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
                } else {
                    loadData_Thuoc(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim.setText("Nhập từ khóa tìm kiếm...");
            txtTim.setForeground(Color.GRAY);
            txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Mã thuốc";
            dsThuoc = thuocdao.getAllThuoc();
            loadData_Thuoc(dsThuoc);
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
            System.out.println(tieuChi);
        }
    }
}