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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB.ConnectDB;
import app.DAO.BangGiaDAO;
import app.DAO.ChiTietBangGiaDAO;
import app.DAO.ThuocDAO;
import app.DTO.ThuocDTO;
import app.DTO.ThuocKemGiaChuanVaGiaHienTaiDTO;
import app.DTO.ThuocKemGiaDTO;
import app.Entity.BangGia;
import app.Entity.ChiTietBangGia;
import app.Entity.Thuoc;

public class CapNhatChiTietBangGiaPanel extends JPanel implements ActionListener, MouseListener{

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
    private JButton btnLuu;
	private JComboBox<String> cboTieuChi;
	private String tieuChi = "Mã thuốc";
	private ThuocDAO thuocDao = new ThuocDAO();
	private JLabel lblMaThuoc;
	private JLabel lblTenThuoc;
	private JLabel lblGiaBan;
	private JLabel lblDonVi;
	private JTextField txtMaThuoc;
	private JTextField txtTenThuoc;
	private JTextField txtGiaBan;
	private JTextField txtDonVi;
	private JButton btnThem;
	private JButton btnXoa;
	
	// Danh sách thuốc đã chọn từ SubPanel
	private ArrayList<ThuocKemGiaChuanVaGiaHienTaiDTO> dsThuocDaChon = new ArrayList<>();
    
    public CapNhatChiTietBangGiaPanel(String maBangGia, CapNhatBangGiaPanel parentPanel) {
    	FlatLightLaf.setup();
    	this.maBangGia = maBangGia;
    	this.parentPanel = parentPanel;
    	
        ConnectDB.getInstance().connect();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = cal.getTime();
        
        setLayout(new BorderLayout());
        
        initHeader();
        taoSearchPanel();
        initInputForm();
        initButtons();
        
        String[] colsBangGia = {"Mã bảng giá", "Tên bảng giá", "Loại bảng giá", "Độ Ưu Tiên", "Ngày áp dụng", "Ngày kết thúc", "Trạng thái", "Ghi chú"};
        dtmBangGia = new DefaultTableModel(colsBangGia, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        String[] colsChiTietBangGia = {"Mã bảng giá", "Mã thuốc", "Tên thuốc", "Giá bán", "Đơn vị"};
        dtmChiTietBangGia = new DefaultTableModel(colsChiTietBangGia, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return column == 3;
            }
            
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 3) {
                    try {
                        double val = Double.parseDouble(aValue.toString());
                        if (val < 0) {
                            return; // Revert to old value
                        }
                        super.setValueAt(aValue, row, column);
                    } catch (NumberFormatException e) {
                        super.setValueAt("0", row, column); // Set to 0 if not a number
                    }
                } else {
                    super.setValueAt(aValue, row, column);
                }
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
        
        // Text field
        txtTim = new JTextField(25);
        txtTim.setText("Nhập từ khóa tìm kiếm...");
        txtTim.setForeground(Color.GRAY);
        txtTim.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        txtTim.setBackground(Color.WHITE);
        txtTim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Add focus listener for placeholder text
        txtTim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtTim.getText().equals("Nhập từ khóa tìm kiếm...")) {
                    txtTim.setText("");
                    txtTim.setForeground(Color.BLACK);
                    txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTim.getText().isEmpty()) {
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                }
            }
        });
        
        // Button Tìm
        btnTim = new JButton("Tìm");
        btnTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnTim.setPreferredSize(new Dimension(90, 40));
        btnTim.setBackground(PRIMARY_COLOR);
        btnTim.setForeground(Color.WHITE);
        btnTim.setBorderPainted(false);
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        // Button Reset
        btnReset = new JButton("Làm mới");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.setPreferredSize(new Dimension(100, 40));
        btnReset.setBackground(BTN_CLEAR_COLOR);
        btnReset.setForeground(Color.WHITE);
        btnReset.setBorderPainted(false);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(this);
        
        // ComboBox
        String[] tieuChiTim = {"Mã thuốc", "Mã đơn vị", "Tên thuốc"};
        cboTieuChi = new JComboBox<String>(tieuChiTim);
        cboTieuChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTieuChi.setBackground(Color.WHITE);
        cboTieuChi.setForeground(TEXT_COLOR);
        cboTieuChi.setFocusable(false);
        cboTieuChi.setPreferredSize(new Dimension(150, 40));
        cboTieuChi.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        cboTieuChi.addActionListener(this);
        
        searchPanel.add(btnQuayLai);
        searchPanel.add(txtTim);
        searchPanel.add(btnTim);
        searchPanel.add(btnReset);
        searchPanel.add(cboTieuChi);
        
        return searchPanel;
    }
    
    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaThuoc = new JLabel("Mã thuốc:");
        lblMaThuoc.setFont(fontLabel);
        lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setFont(fontLabel);
        lblGiaBan = new JLabel("Giá bán:");
        lblGiaBan.setFont(fontLabel);
        lblDonVi = new JLabel("Đơn vị:");
        lblDonVi.setFont(fontLabel);
        
        txtMaThuoc = new JTextField();
        txtMaThuoc.setFont(fontText);
        txtMaThuoc.setPreferredSize(new Dimension(200, 35));
        txtMaThuoc.setEditable(false);
        txtMaThuoc.setBackground(new Color(240, 240, 240));
        
        txtTenThuoc = new JTextField();
        txtTenThuoc.setFont(fontText);
        txtTenThuoc.setPreferredSize(new Dimension(200, 35));
        txtTenThuoc.setEditable(false);
        txtTenThuoc.setBackground(new Color(240, 240, 240));
        
        txtGiaBan = new JTextField();
        txtGiaBan.setFont(fontText);
        txtGiaBan.setPreferredSize(new Dimension(200, 35));
        
        
        txtDonVi = new JTextField();
        txtDonVi.setFont(fontText);
        txtDonVi.setPreferredSize(new Dimension(200, 35));
    }
    
    private JPanel createInputPanel() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(ACCENT_COLOR);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 50, 20, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 20);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblMaThuoc, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtMaThuoc, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenThuoc, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenThuoc, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblGiaBan, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtGiaBan, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblDonVi, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtDonVi, gbc);
        
        return pnlForm;
    }
    
    private void initButtons() {
        btnThem = createStyledButton("Chọn thuốc cần thêm", BTN_ADD_COLOR);
        btnXoa = createStyledButton("Xóa", BTN_DELETE_COLOR);
        btnLuu = createStyledButton("Lưu", PRIMARY_COLOR);

        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLuu.addActionListener(this);
   
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(BG_COLOR);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnLuu);
        return pnlButtons;
    }
    
    private JPanel createBangGiaInfoPanel() {
    	BangGiaDAO bgDAO = new BangGiaDAO();
        bangGia = bgDAO.getBangGiaTheoMa(maBangGia);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        TitledBorder border = BorderFactory.createTitledBorder("Thông tin bảng giá");
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.setBorder(border);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        row = addRow(panel, gbc, row, "Mã bảng giá:", bangGia.getMaBangGia());
        row = addRow(panel, gbc, row, "Tên bảng giá:", bangGia.getTenBangGia());
        row = addRow(panel, gbc, row, "Loại bảng giá:", bangGia.getLoaiGia());
        row = addRow(panel, gbc, row, "Độ ưu tiên:", String.valueOf(bangGia.getDoUuTien()));
        row = addRow(panel, gbc, row, "Ngày áp dụng:", sdf.format(bangGia.getNgayApDung()));
        row = addRow(panel, gbc, row, "Ngày kết thúc:", sdf.format(bangGia.getNgayKetThuc()));
        row = addRow(panel, gbc, row, "Trạng thái:", bangGia.getTrangThai());
        row = addRow(panel, gbc, row, "Ghi chú:", bangGia.getGhiChu());

        return panel;
    }
    
    private int addRow(JPanel panel, GridBagConstraints gbc, int row,
            String labelText, String valueText) {
    	
    	JLabel lbl = new JLabel(labelText);
    	lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

    	gbc.gridx = 0;
    	gbc.gridy = row;
    	gbc.anchor = GridBagConstraints.NORTHWEST;
    	panel.add(lbl, gbc);

    	gbc.gridx = 1;
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.weightx = 1.0;

    	JTextArea ta = new JTextArea(valueText);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setOpaque(false);
        ta.setBorder(null);
        ta.setRows(1);

        panel.add(ta, gbc);

    	gbc.fill = GridBagConstraints.NONE;
    	gbc.weightx = 0;

    	return row + 1;
    }

    public JPanel createBotPanel() {
    	JPanel panel = new JPanel(new BorderLayout(5, 15));
    	panel.setBackground(BG_COLOR);
    	
    	JPanel panelSubBot = new JPanel(new BorderLayout(5, 15));
    	panelSubBot.setBackground(BG_COLOR);
    	
    	JPanel panelAddButton = new JPanel(new BorderLayout(5, 15));
    	panelAddButton.setBackground(BG_COLOR);
    	
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
        
        // Thêm mouse listener cho bảng chi tiết
        tblChiTietBangGia.addMouseListener(this);
        
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
        
        int rowHeight = tblBangGia.getRowHeight();
        int rowCount = Math.max(tblBangGia.getRowCount(), 1);
        int headerHeight = tblBangGia.getTableHeader().getPreferredSize().height;

        scrollPaneBangGia.setPreferredSize(new Dimension(0, rowHeight * rowCount + headerHeight + 2));

        JScrollPane scrollPaneChiTietBangGia = new JScrollPane(tblChiTietBangGia);
        scrollPaneChiTietBangGia.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPaneChiTietBangGia.getViewport().setBackground(Color.WHITE);
        
        panelAddButton.add(createInputPanel(), BorderLayout.CENTER);
        panelAddButton.add(createButtonPanel(), BorderLayout.SOUTH);
        
        panelSubBot.add(panelAddButton, BorderLayout.NORTH);
        panelSubBot.add(scrollPaneChiTietBangGia, BorderLayout.CENTER);
        
        panel.add(createBangGiaInfoPanel(), BorderLayout.WEST);
        panel.add(panelSubBot, BorderLayout.CENTER);
        
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
        
        for(ChiTietBangGia ctbg : dsChiTietBangGia) {
            Thuoc thuoc = thuocDao.getThuocById(ctbg.getMaThuoc());
            String tenThuoc = (thuoc != null) ? thuoc.getTenThuoc() : "";
            
            Object[] rowData = {
                    ctbg.getMaBangGia(),
                    ctbg.getMaThuoc(),
                    tenThuoc,
                    ctbg.getDonGia(),
                    ctbg.getDonVi(),
            };
            dtmChiTietBangGia.addRow(rowData);
        }
    }
    
    public void loadChiTietBangGiaTuList() {
        for(ChiTietBangGia ctbg : dsChiTietBangGia) {
            Thuoc thuoc = thuocDao.getThuocById(ctbg.getMaThuoc());
            String tenThuoc = (thuoc != null) ? thuoc.getTenThuoc() : "";
            
            Object[] rowData = {
                    ctbg.getMaBangGia(),
                    ctbg.getMaThuoc(),
                    tenThuoc,
                    ctbg.getDonGia(),
                    ctbg.getDonVi(),
            };
            dtmChiTietBangGia.addRow(rowData);
        }
    }
    
    public void loadKetQuaTim(ArrayList<ChiTietBangGia> dsChiTietBangGia) {
        
        dtmChiTietBangGia.setRowCount(0);
        for(ChiTietBangGia ct : dsChiTietBangGia) {
            Thuoc thuoc = thuocDao.getThuocById(ct.getMaThuoc());
            String tenThuoc = (thuoc != null) ? thuoc.getTenThuoc() : "";
            
            Object[] rowData = {
                ct.getMaBangGia(),
                ct.getMaThuoc(),
                tenThuoc,
                ct.getDonGia(),
                ct.getDonVi(),
            };
            dtmChiTietBangGia.addRow(rowData);
        }
    }
    
    public void xoaTrang() {
        txtMaThuoc.setText("");
        txtTenThuoc.setText("");
        txtDonVi.setText("");
        txtGiaBan.setText("");
        tblChiTietBangGia.clearSelection();
        dsThuocDaChon.clear();
        loadChiTietBangGiaData();
    }
    
    /**
     * Phương thức để nhận danh sách thuốc đã chọn từ SubPanel
     */
    public void nhanDanhSachThuocDaChon(ArrayList<ThuocKemGiaChuanVaGiaHienTaiDTO> listThuoc) {
        if (listThuoc == null || listThuoc.isEmpty()) {
            return;
        }

        // Copy danh sách để tránh lỗi tham chiếu
        ArrayList<ThuocKemGiaChuanVaGiaHienTaiDTO> dsThuoc = new ArrayList<>(listThuoc);
        
        ChiTietBangGiaDAO ctbgDAO = new ChiTietBangGiaDAO();
        app.DAO.DonViDAO donViDAO = new app.DAO.DonViDAO();
        int soThuocThemThanhCong = 0;
        int soThuocDaTonTai = 0;
        
        // Đảm bảo danh sách hiện tại đã được load để kiểm tra trùng
        if (dsChiTietBangGia == null) {
            loadChiTietBangGiaData();
        }
        
   
        
        for (ThuocKemGiaChuanVaGiaHienTaiDTO thuoc : dsThuoc) {
            // Kiểm tra trùng
            boolean daTonTai = false;
            if (dsChiTietBangGia != null) {
                for (ChiTietBangGia ct : dsChiTietBangGia) {
                    if (ct.getMaThuoc().equals(thuoc.getMaThuoc())) {
                        daTonTai = true;
                        soThuocDaTonTai++;
                        break;
                    }
                }
            }
            
            if (!daTonTai) {
                String donViValue = thuoc.getDonVi();
                String maDonVi = donViValue;
                
                // Kiểm tra xem có phải là mã đơn vị không
                app.Entity.DonVi dvCheck = donViDAO.getDonViByMa(donViValue);
                if (dvCheck != null) {
                    maDonVi = dvCheck.getMaDonVi();
                } else {
                    // Nếu không phải mã, tìm theo tên
                    java.util.List<app.Entity.DonVi> listDonVi = donViDAO.getDonViByTen(donViValue);
                    for (app.Entity.DonVi dv : listDonVi) {
                        if (dv.getTenDonVi().equalsIgnoreCase(donViValue)) {
                            maDonVi = dv.getMaDonVi();
                            break;
                        }
                    }
                }

                ChiTietBangGia ctbgNew = new ChiTietBangGia(
                    maBangGia,
                    thuoc.getMaThuoc(),
                    thuoc.getGiaHienTai(),
                    maDonVi,
                    true
                );
                
                if (ctbgDAO.themChiTietBangGia(ctbgNew)) {
                    soThuocThemThanhCong++;
                }
            }
        }
        
        // Hiển thị thông báo
        String message = "";
        if (soThuocThemThanhCong > 0) {
            message += "Đã thêm thành công " + soThuocThemThanhCong + " thuốc vào bảng giá!\n";
        }
        if (soThuocDaTonTai > 0) {
            message += soThuocDaTonTai + " thuốc đã tồn tại trong bảng giá.\n";
        }
        if (soThuocThemThanhCong == 0 && soThuocDaTonTai == 0) {
            message = "Không có thuốc nào được thêm vào!";
        }
        
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        
        // Load lại dữ liệu
        loadChiTietBangGiaData();
        xoaTrang();
    }
    
    // Phương thức để quay lại danh sách chính
    public void quayLaiDanhSach() {
        try {
            mainContainer.remove(mainContainer.getComponent(1));
        } catch (Exception ex) {
            // Không có panel chi tiết
        }
        cardLayout.show(mainContainer, "DanhSach");
        loadChiTietBangGiaData();
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
                    for(ChiTietBangGia ct : dsChiTietBangGia) {
                        Thuoc thuoc = thuocDao.getThuocById(ct.getMaThuoc());
                        if(thuoc != null && thuoc.getTenThuoc().toLowerCase().contains(timString)) {
                            ketQuaTim.add(ct);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadChiTietBangGiaData();
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                } else {
                	loadKetQuaTim(ketQuaTim);
                }
            }
        }
        else if(o == btnReset) {
            txtTim.setText("Nhập từ khóa tìm kiếm...");
            txtTim.setForeground(Color.GRAY);
            txtTim.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            cboTieuChi.setSelectedIndex(0);
            tieuChi = "Mã thuốc";
            loadChiTietBangGiaData();
            txtTim.requestFocus();
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
        }
        else if(o == btnXoa) {
            int selectedRow = tblChiTietBangGia.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết bảng giá cần xóa!");
                return;
            }
            
            String maThuoc = tblChiTietBangGia.getValueAt(selectedRow, 1).toString();
            String tenThuoc = tblChiTietBangGia.getValueAt(selectedRow, 2).toString();

            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn xóa thuốc \"" + tenThuoc + "\" khỏi bảng giá?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                ChiTietBangGiaDAO ctbgDAO = new ChiTietBangGiaDAO();
                boolean result = ctbgDAO.xoaChiTietBangGia(maBangGia, maThuoc);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Xóa chi tiết bảng giá thành công!");
                    loadChiTietBangGiaData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa chi tiết bảng giá không thành công!");
                }
            }
        }
        else if(o == btnThem) {
        	JPanel pnlChonThuoc = new CapNhatChiTietBangGiaSubPanel(this);
        	
        	try {
                mainContainer.remove(mainContainer.getComponent(1));
            } catch (Exception ex) {
                // Không có panel chi tiết cũ
            }
            
            mainContainer.add(pnlChonThuoc, "ChiTiet");
            cardLayout.show(mainContainer, "ChiTiet");
        }
        else if(o == btnLuu) {
            ChiTietBangGiaDAO ctbgDAO = new ChiTietBangGiaDAO();
            boolean allSuccess = true;
            
            for (int i = 0; i < tblChiTietBangGia.getRowCount(); i++) {
                String maThuoc = tblChiTietBangGia.getValueAt(i, 1).toString();
                // Column 3 is price.
                double giaBan = 0;
                try {
                    giaBan = Double.parseDouble(tblChiTietBangGia.getValueAt(i, 3).toString());
                } catch (NumberFormatException ex) {
                    giaBan = 0;
                }
                String donVi = tblChiTietBangGia.getValueAt(i, 4).toString();
                
                ChiTietBangGia ctbgNew = new ChiTietBangGia(maBangGia, maThuoc, giaBan, donVi, true);
                if (!ctbgDAO.capNhatChiTietBangGia(ctbgNew)) {
                    allSuccess = false;
                }
            }
            
            if (allSuccess) {
                JOptionPane.showMessageDialog(this, "Lưu thay đổi thành công!");
                parentPanel.quayLaiDanhSach();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lưu một số chi tiết!");
            }
        }
    }
    
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == tblChiTietBangGia) {
            int selectedRow = tblChiTietBangGia.getSelectedRow();
            if(selectedRow != -1) {
                String maThuoc = tblChiTietBangGia.getValueAt(selectedRow, 1).toString();
                String tenThuoc = tblChiTietBangGia.getValueAt(selectedRow, 2).toString();
                String giaBan = tblChiTietBangGia.getValueAt(selectedRow, 3).toString();
                String donVi = tblChiTietBangGia.getValueAt(selectedRow, 4).toString();
                
                txtMaThuoc.setText(maThuoc);
                txtTenThuoc.setText(tenThuoc);
                txtGiaBan.setText(giaBan);
                txtDonVi.setText(donVi);
            }
        }
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}
