package app.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import app.ConnectDB.ConnectDB;
import app.DAO.BangGiaDAO;
import app.DAO.ChiTietBangGiaDAO;
import app.DAO.ChiTietLoThuocDAO;
import app.DAO.ChiTietPhieuNhapDAO;
import app.DAO.ChucVuDAO;
import app.DAO.KhuyenMaiDAO;
import app.DAO.ThuocDAO;
import app.Entity.BangGia;
import app.Entity.ChiTietBangGia;
import app.Entity.ChiTietLoThuoc;
import app.Entity.ChiTietPhieuNhap;
import app.Entity.ChucVu;
import app.Entity.KhuyenMai;
import app.Entity.Thuoc;

public class XemChiTietBangGiaPanel extends JPanel implements ActionListener{
	
	private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);
    private final Color BTN_BACK_COLOR = new Color(70, 70, 70);

    private JLabel lblTieuDe;
    private ArrayList<ChiTietBangGia> dsChiTietBangGia;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    Date today;
	private DefaultTableModel dtmBangGia;
	private JTable tblBangGia;
	private String maBangGia;
	private CapNhatBangGiaPanel parentPanel;
	
	private BangGia bangGia;
	private JTable tblChiTietBangGia;
	private DefaultTableModel dtmChiTietBangGia;
	private JButton btnQuayLai;
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnReset;
	private JComboBox<String> cboTieuChi;
	private String tieuChi = "Mã thuốc";
	private ThuocDAO thuocDao = new ThuocDAO();
    
    public XemChiTietBangGiaPanel(String maBangGia, CapNhatBangGiaPanel parentPanel) {
    	FlatLightLaf.setup();
    	this.maBangGia = maBangGia;
    	this.parentPanel = parentPanel;
    	
        ConnectDB.getInstance().connect();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar. SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = cal.getTime();
        
        setLayout(new BorderLayout());
        
        initHeader();
        taoSearchPanel();
        
        String[] colsBangGia = {"Mã bảng giá", "Tên bảng giá", "Loại bảng giá", "Độ Ưu Tiên", "Ngày áp dụng", "Ngày kết thúc", "Trạng thái", "Ghi chú"};
        dtmBangGia = new DefaultTableModel(colsBangGia, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        String[] colsChiTietBangGia = {"Mã bảng giá", "Mã thuốc", "Giá bán", "Đơn vị"};
        dtmChiTietBangGia = new DefaultTableModel(colsChiTietBangGia, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(BG_COLOR);
        pnlTop.add(lblTieuDe, BorderLayout.NORTH);
        pnlTop.add(taoSearchPanel());
        
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(BG_COLOR);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(createBotPanel(), BorderLayout.CENTER);
        
        mainContainer.add(pnlMain, "DanhSach");
        cardLayout.show(mainContainer, "DanhSach");
        
        add(mainContainer, BorderLayout.CENTER);
        loadBangGiaData();
        loadChiTietBangGiaData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ CHI TIẾT BẢNG GIÁ", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
        String[] tieuChiTim = {"Mã thuốc", "Mã đơn vị", "Tên thuốc"};
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
    
    public JPanel createBotPanel() {
    	JPanel panel = new JPanel(new BorderLayout(0, 15));
    	
        tblBangGia = new JTable(dtmBangGia) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        }; 
        
        tblChiTietBangGia = new JTable(dtmChiTietBangGia) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };
        
        tblBangGia.setRowHeight(35);
        tblBangGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblBangGia.setFillsViewportHeight(true);
        tblBangGia.setShowGrid(true);
        tblBangGia.setGridColor(new Color(224, 224, 224));
        tblBangGia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBangGia.setSelectionBackground(new Color(178, 223, 219));
        tblBangGia.setSelectionForeground(Color.BLACK);
        
        tblChiTietBangGia.setRowHeight(35);
        tblChiTietBangGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChiTietBangGia.setFillsViewportHeight(true);
        tblChiTietBangGia.setShowGrid(true);
        tblChiTietBangGia.setGridColor(new Color(224, 224, 224));
        tblChiTietBangGia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChiTietBangGia.setSelectionBackground(new Color(178, 223, 219));
        tblChiTietBangGia.setSelectionForeground(Color.BLACK);
        
        JTableHeader headerBangGia = tblBangGia.getTableHeader();
        headerBangGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerBangGia.setBackground(PRIMARY_COLOR);
        headerBangGia.setForeground(Color.WHITE);
        headerBangGia.setPreferredSize(new Dimension(headerBangGia.getWidth(), 40));
        headerBangGia.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) headerBangGia.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        JTableHeader headerChiTietBangGia = tblChiTietBangGia.getTableHeader();
        headerChiTietBangGia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerChiTietBangGia.setBackground(PRIMARY_COLOR);
        headerChiTietBangGia.setForeground(Color.WHITE);
        headerChiTietBangGia.setPreferredSize(new Dimension(headerChiTietBangGia.getWidth(), 40));
        headerChiTietBangGia.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRendererChiTietBangGia = (DefaultTableCellRenderer) headerChiTietBangGia.getDefaultRenderer();
        centerRendererChiTietBangGia.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenterBangGia = new DefaultTableCellRenderer();
        cellCenterBangGia.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblBangGia.getColumnCount(); i++) {
        	tblBangGia.getColumnModel().getColumn(i).setCellRenderer(cellCenterBangGia);
        }
        
        DefaultTableCellRenderer cellCenterChiTietBangGia = new DefaultTableCellRenderer();
        cellCenterChiTietBangGia.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblChiTietBangGia.getColumnCount(); i++) {
        	tblChiTietBangGia.getColumnModel().getColumn(i).setCellRenderer(cellCenterChiTietBangGia);
        }
        
        JScrollPane scrollPaneBangGia = new JScrollPane(tblBangGia);
        scrollPaneBangGia.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPaneBangGia.getViewport().setBackground(Color.WHITE);
        
     // ===== SET CHIỀU CAO VỪA ĐỦ CHO tblBangGia =====
        int rowHeight = tblBangGia.getRowHeight();
        int rowCount = Math.max(tblBangGia.getRowCount(), 1);
        int headerHeight = tblBangGia.getTableHeader()
                                     .getPreferredSize()
                                     .height;


        // BorderLayout.NORTH chỉ ăn đúng preferred height
        scrollPaneBangGia.setPreferredSize(new Dimension(0, rowHeight + rowCount + headerHeight +1));

        
        JScrollPane scrollPaneChiTietBangGia = new JScrollPane(tblChiTietBangGia);
        scrollPaneChiTietBangGia.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPaneChiTietBangGia.getViewport().setBackground(Color.WHITE);
        //return scrollPane;
        
        panel.add(scrollPaneBangGia, BorderLayout.NORTH);
        panel.add(scrollPaneChiTietBangGia, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void loadBangGiaData() {
        BangGiaDAO bgDAO = new BangGiaDAO();
        bangGia = bgDAO.getBangGiaTheoMa(maBangGia);
        dtmBangGia.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        Object[] rowData = {
            	bangGia.getMaBangGia(),
            	bangGia.getTenBangGia(),
            	bangGia.getLoaiGia(),
            	bangGia.getDoUuTien(),
                sdf.format(bangGia.getNgayApDung()),
                sdf.format(bangGia.getNgayKetThuc()),
                bangGia.getTrangThai(),
                bangGia.getGhiChu()
        };
        dtmBangGia.addRow(rowData);
    }
    
    public void loadChiTietBangGiaData() {
        ChiTietBangGiaDAO ctbgDAO = new ChiTietBangGiaDAO();
        dsChiTietBangGia = ctbgDAO.getChiTietBangGiaTheoMa(maBangGia);
        dtmChiTietBangGia.setRowCount(0);
        
        for(ChiTietBangGia ctbg :  dsChiTietBangGia) {
            Object[] rowData = {
                    ctbg.getMaBangGia(),
                    ctbg.getMaThuoc(),
                    ctbg.getDonGia(),
                    ctbg.getDonVi(),
            };
            dtmChiTietBangGia.addRow(rowData);
        }
    }
    
    public void loadKetQuaTim(ArrayList<ChiTietBangGia> dsChiTietBangGia) {
        
        dtmChiTietBangGia.setRowCount(0);
        for(ChiTietBangGia ct : dsChiTietBangGia) {
            Object[] rowData = {
                ct.getMaBangGia(),
                ct.getMaThuoc(),
                ct.getDonGia(),
                ct.getDonVi(),
            };
            dtmChiTietBangGia.addRow(rowData);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	Object o = e.getSource();
        
        if(o == btnQuayLai) {
            parentPanel.quayLaiDanhSach();
        }
        else if(o == btnTim) {
            ArrayList<ChiTietBangGia> ketQuaTim = new ArrayList<ChiTietBangGia>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm...") || timString.isBlank()) {
                loadChiTietBangGiaData();
            }
            else {
                if(tieuChi.equals("Mã thuốc")) {
                    for(ChiTietBangGia ct : dsChiTietBangGia) {
                        if(ct.getMaThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                else if(tieuChi.equals("Mã đơn vị")) {
                    for(ChiTietBangGia ct : dsChiTietBangGia) {
                        if(ct.getDonVi().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                else { // Tên thuốc
                    for(ChiTietBangGia ct :  dsChiTietBangGia) {
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
                    loadChiTietBangGiaData();
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim. setFont(new Font("Segoe UI", Font.ITALIC, 14));
                } else {
                	loadKetQuaTim(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim. setText("Nhập từ khóa tìm kiếm...");
            txtTim. setForeground(Color. GRAY);
            txtTim. setFont(new Font("Segoe UI", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Mã thuốc";
            loadChiTietBangGiaData();
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
        }
    }
	
}
