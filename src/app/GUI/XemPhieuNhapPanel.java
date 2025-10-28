package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.ChiTietPhieuNhapDAO;
import app.DAO.PhieuNhapThuocDAO;
import app.Entity.PhieuNhapThuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class XemPhieuNhapPanel extends JPanel implements ActionListener{ 
    private CardLayout cardLayout;
    private DefaultTableModel dtmTable;
    private ArrayList<PhieuNhapThuoc> dsPhieuNhap;
    private JButton btnTim;
    private JButton btnReset;
    private JButton btnXemChiTiet;
    private JTextField txtTim;
    private String tieuChi = "Mã phiếu nhập";
    private JComboBox<String> cboTieuChi;
    private JTable table;
    private JPanel mainContainer;
    private XemChiTietPhieuNhapSubPanel chiTietPanel;
    private PhieuNhapThuocDAO phieuNhapDao;
    private JLabel lblTongSoBanGhi;

    public XemPhieuNhapPanel() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        setLayout(new BorderLayout());
        
        ConnectDB.getInstance().connect();
        
        phieuNhapDao = new PhieuNhapThuocDAO();
        dsPhieuNhap = phieuNhapDao.getAllPhieuNhapThuoc();
        
        JPanel danhSachPanel = taoDanhSachPanel();
        mainContainer.add(danhSachPanel, "DanhSach");
        cardLayout.show(mainContainer, "DanhSach");
        
        add(mainContainer, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JPanel taoDanhSachPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel centerPanel = taoCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("LỊCH SỬ NHẬP THUỐC", SwingConstants.CENTER);
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

        String[] cols = {"Mã phiếu", "Mã nhân viên", "Ngày nhập"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadDataPhieuNhap(dsPhieuNhap);
        
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
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsPhieuNhap.size());
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
        
        // Button Tìm
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Arial", Font.BOLD, 14));
        btnTim.setPreferredSize(new Dimension(90, 40));
        btnTim.setBackground(new Color(144, 238, 144));
        btnTim.setForeground(new Color(51, 51, 51));
        btnTim.setBorder(BorderFactory.createLineBorder(new Color(100, 200, 100), 1));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        // Button Reset
        btnReset = new JButton("Reset");
        btnReset.setFont(new Font("Arial", Font.PLAIN, 14));
        btnReset.setPreferredSize(new Dimension(90, 40));
        btnReset.setBackground(Color.WHITE);
        btnReset.setForeground(new Color(51, 51, 51));
        btnReset.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(this);
        
        // Button Xem chi tiết
        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(new Font("Arial", Font.BOLD, 14));
        btnXemChiTiet.setPreferredSize(new Dimension(130, 40));
        btnXemChiTiet.setBackground(new Color(100, 149, 237));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setBorder(BorderFactory.createLineBorder(new Color(65, 105, 225), 1));
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.addActionListener(this);
        
        // ComboBox
        String[] tieuChiTim = {"Mã phiếu nhập", "Mã nhân viên"};
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
        searchPanel.add(btnXemChiTiet);
        searchPanel.add(cboTieuChi);
        
        return searchPanel;
    }
   
    public void loadDataPhieuNhap(ArrayList<PhieuNhapThuoc> dsPhieuNhap) {
    	dsPhieuNhap.sort(Comparator.comparing(PhieuNhapThuoc::getMaPhieuNhapThuoc));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dtmTable.setRowCount(0);
        for(PhieuNhapThuoc pn : dsPhieuNhap) {
            Object[] rowData = {
                pn.getMaPhieuNhapThuoc(),
                pn.getMaNV(),
                sdf.format(pn.getNgayNhap())
            };
            dtmTable.addRow(rowData);
        }
        if(lblTongSoBanGhi != null) {
            lblTongSoBanGhi.setText("Tổng số bản ghi: " + dsPhieuNhap.size());
        }
    }
    
    public void quayLaiDanhSach() {
        cardLayout.show(mainContainer, "DanhSach");
        loadDataPhieuNhap(dsPhieuNhap);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnTim) {
            ArrayList<PhieuNhapThuoc> ketQuaTim = new ArrayList<PhieuNhapThuoc>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm...") || timString.isBlank()) {
                loadDataPhieuNhap(dsPhieuNhap);
            }
            else {
                if(tieuChi.equals("Mã phiếu nhập")) {
                    for(PhieuNhapThuoc pn : dsPhieuNhap) {
                        if(pn.getMaPhieuNhapThuoc().toLowerCase().matches("^.*" + timString + ".*")) {
                            ketQuaTim.add(pn);
                        }
                    }
                }
                else { // Mã nhân viên
                    for(PhieuNhapThuoc pn : dsPhieuNhap) {
                        if(pn.getMaNV().toLowerCase().matches("^.*" + timString + ".*")) {
                            ketQuaTim.add(pn);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadDataPhieuNhap(dsPhieuNhap);
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
                } else {
                    loadDataPhieuNhap(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim.setText("Nhập từ khóa tìm kiếm...");
            txtTim.setForeground(Color.GRAY);
            txtTim.setFont(new Font("Arial", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Mã phiếu nhập";
            dsPhieuNhap = phieuNhapDao.getAllPhieuNhapThuoc();
            loadDataPhieuNhap(dsPhieuNhap);
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
        }
        else if(o == btnXemChiTiet) {
            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một phiếu nhập để xem chi tiết!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String maPhieuNhap = table.getValueAt(row, 0).toString();
            
            chiTietPanel = new XemChiTietPhieuNhapSubPanel(maPhieuNhap, this);
            
            try {
                mainContainer.remove(mainContainer.getComponent(1));
            } catch (Exception ex) {
                // Không có panel chi tiết cũ
            }
            
            mainContainer.add(chiTietPanel, "ChiTiet");
            cardLayout.show(mainContainer, "ChiTiet");
        }
    }
}