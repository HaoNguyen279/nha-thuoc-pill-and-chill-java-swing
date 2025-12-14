package app.GUI;

import java.awt. BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt. Cursor;
import java.awt. Dimension;
import java.awt. FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java. awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing. JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax. swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table. DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB. ConnectDB;
import app. DAO.ChiTietLoThuocDAO;
import app.DAO.ChiTietPhieuNhapDAO;
import app.DAO.ThuocDAO;
import app. Entity.ChiTietLoThuoc;
import app.Entity. ChiTietPhieuNhap;
import app.Entity.Thuoc;

public class XemChiTietPhieuNhapSubPanel extends JPanel implements ActionListener {
    private DefaultTableModel dtmTable;
    private ArrayList<ChiTietPhieuNhap> dsCTPN;
    private ArrayList<ChiTietLoThuoc> dsCTLT;
    private JButton btnTim;
    private JButton btnReset;
    private JButton btnQuayLai;
    private JTextField txtTim;
    private String tieuChi = "Mã thuốc";
    private JComboBox<String> cboTieuChi;
    private JTable table;
    private ChiTietLoThuocDAO ctltDao = new ChiTietLoThuocDAO();
    private ChiTietPhieuNhapDAO ctpnDao = new ChiTietPhieuNhapDAO();
    private ThuocDAO thuocDao = new ThuocDAO();
    private String maPhieuNhap;
    private XemPhieuNhapPanel parentPanel;
    private JLabel lblTongSoBanGhi;

    // Màu sắc giống XemPhieuNhapPanel
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);
    private final Color BTN_BACK_COLOR = new Color(70, 70, 70);

    public XemChiTietPhieuNhapSubPanel(String maPhieuNhap, XemPhieuNhapPanel parentPanel) {
        FlatLightLaf.setup();
        this.maPhieuNhap = maPhieuNhap;
        this.parentPanel = parentPanel;
        
        setLayout(new BorderLayout());
        
        ConnectDB.getInstance().connect();
        
        dsCTPN = ctpnDao. getChiTietByMaPhieuNhap(maPhieuNhap);
        dsCTLT = ctltDao.getAllActiveChiTietLoThuoc();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        
        JPanel centerPanel = taoCenterPanel();
        mainPanel. add(centerPanel, BorderLayout. CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("CHI TIẾT PHIẾU NHẬP " + maPhieuNhap, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel searchPanel = taoSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);
        
        centerPanel. add(titlePanel, BorderLayout. NORTH);
        
        // Table Panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] cols = {"Mã thuốc", "Mã lô", "Tên thuốc", "Số lượng", "Giá nhập", 
                        "Đơn vị", "Mã NSX", "Ngày sản xuất", "Hạn sử dụng"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadDataChiTiet(dsCTPN);
        
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
        
        // Style table header - giống XemPhieuNhapPanel
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // Style table - giống XemPhieuNhapPanel
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(224, 224, 224));
        table.setFont(new Font("Segoe UI", Font. PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(178, 223, 219));
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
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsCTPN.size());
        lblTongSoBanGhi.setFont(new Font("Segoe UI", Font. PLAIN, 14));
        lblTongSoBanGhi.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout. SOUTH);
        return centerPanel;
    }
    
    private JPanel taoSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        searchPanel.setBackground(BG_COLOR);
        
        // Nút quay lại
        btnQuayLai = new JButton("← Quay lại");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnQuayLai.setPreferredSize(new Dimension(120, 40));
        btnQuayLai.setBackground(BTN_BACK_COLOR);
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setBorderPainted(false);
        btnQuayLai.setFocusPainted(false);
        btnQuayLai.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuayLai.addActionListener(this);
        
        // Text field - giống XemPhieuNhapPanel
        txtTim = new JTextField(25);
        txtTim.setText("Nhập từ khóa tìm kiếm.. .");
        txtTim.setForeground(Color.GRAY);
        txtTim.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        txtTim.setBackground(Color.WHITE);
        txtTim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Add focus listener for placeholder text
        txtTim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event. FocusEvent evt) {
                if (txtTim.getText().equals("Nhập từ khóa tìm kiếm...")) {
                    txtTim.setText("");
                    txtTim.setForeground(Color.BLACK);
                    txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTim.getText().isEmpty()) {
                    txtTim. setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color. GRAY);
                    txtTim.setFont(new Font("Segoe UI", Font. ITALIC, 14));
                }
            }
        });
        
        // Button Tìm - giống XemPhieuNhapPanel
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Segoe UI", Font. BOLD, 14));
        btnTim.setPreferredSize(new Dimension(90, 40));
        btnTim.setBackground(PRIMARY_COLOR);
        btnTim.setForeground(Color. WHITE);
        btnTim.setBorderPainted(false);
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        // Button Reset - giống XemPhieuNhapPanel
        btnReset = new JButton("Làm mới");
        btnReset.setFont(new Font("Segoe UI", Font. BOLD, 14));
        btnReset.setPreferredSize(new Dimension(100, 40));
        btnReset.setBackground(BTN_CLEAR_COLOR);
        btnReset.setForeground(Color.WHITE);
        btnReset.setBorderPainted(false);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor. HAND_CURSOR));
        btnReset.addActionListener(this);
        
        // ComboBox - giống XemPhieuNhapPanel
        String[] tieuChiTim = {"Mã thuốc", "Mã lô", "Tên thuốc"};
        cboTieuChi = new JComboBox<String>(tieuChiTim);
        cboTieuChi.setFont(new Font("Segoe UI", Font. PLAIN, 14));
        cboTieuChi.setBackground(Color.WHITE);
        cboTieuChi. setForeground(TEXT_COLOR);
        cboTieuChi.setFocusable(false);
        cboTieuChi. setPreferredSize(new Dimension(150, 40));
        cboTieuChi.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        cboTieuChi.addActionListener(this);
        
        searchPanel.add(btnQuayLai);
        searchPanel.add(txtTim);
        searchPanel.add(btnTim);
        searchPanel. add(btnReset);
        searchPanel. add(cboTieuChi);
        
        return searchPanel;
    }
    
    public void loadDataChiTiet(ArrayList<ChiTietPhieuNhap> dsCTPN) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        dtmTable.setRowCount(0);
        for(ChiTietPhieuNhap ct : dsCTPN) {
            ChiTietLoThuoc lt = new ChiTietLoThuoc();
            for (ChiTietLoThuoc ctlt : dsCTLT) {
                if(ctlt.getMaLo().equalsIgnoreCase(ct.getMaLo()) && 
                   ctlt. getMaThuoc().equalsIgnoreCase(ct.getMaThuoc())) {
                    lt = ctlt;
                }
            }
            Thuoc thuoc = thuocDao.getThuocById(ct.getMaThuoc());
            Object[] rowData = {
                ct.getMaThuoc(),
                ct.getMaLo(),
                thuoc.getTenThuoc(),
                ct. getSoLuong(),
                ct.getDonGia(),
                thuoc.getDonVi(),
                thuoc.getMaNSX(),
                sdf.format(lt.getNgaySanXuat()),
                sdf.format(lt.getHanSuDung())
            };
            dtmTable.addRow(rowData);
        }
        if(lblTongSoBanGhi != null) {
            lblTongSoBanGhi.setText("Tổng số bản ghi: " + dsCTPN.size());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if(o == btnQuayLai) {
            parentPanel.quayLaiDanhSach();
        }
        else if(o == btnTim) {
            ArrayList<ChiTietPhieuNhap> ketQuaTim = new ArrayList<ChiTietPhieuNhap>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm...") || timString.isBlank()) {
                loadDataChiTiet(dsCTPN);
            }
            else {
                if(tieuChi.equals("Mã thuốc")) {
                    for(ChiTietPhieuNhap ct : dsCTPN) {
                        if(ct.getMaThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                else if(tieuChi.equals("Mã lô")) {
                    for(ChiTietPhieuNhap ct : dsCTPN) {
                        if(ct.getMaLo().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                else { // Tên thuốc
                    for(ChiTietPhieuNhap ct :  dsCTPN) {
                        Thuoc thuoc = thuocDao.getThuocById(ct. getMaThuoc());
                        if(thuoc. getTenThuoc().toLowerCase().contains(timString)) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào! ",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadDataChiTiet(dsCTPN);
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim. setFont(new Font("Segoe UI", Font.ITALIC, 14));
                } else {
                    loadDataChiTiet(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim. setText("Nhập từ khóa tìm kiếm...");
            txtTim. setForeground(Color. GRAY);
            txtTim. setFont(new Font("Segoe UI", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Mã thuốc";
            loadDataChiTiet(dsCTPN);
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
        }
    }
}