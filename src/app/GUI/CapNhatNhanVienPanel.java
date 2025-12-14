package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.ChucVuDAO;
import app.DAO.NhanVienDAO;
import app.Entity.ChucVu;
import app.Entity.NhanVien;

public class CapNhatNhanVienPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaNv;
    private JLabel lblTenNv;
    private JLabel lblSoDienThoai;
    private JLabel lblChucVu;
public class CapNhatNhanVienPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaNv;
    private JLabel lblTenNv;
    private JLabel lblSoDienThoai;
    private JLabel lblChucVu;

    private JTextField txtMaNv;
    private JTextField txtTenNv;
    private JTextField txtSoDienThoai;
    private JTextField txtMaNv;
    private JTextField txtTenNv;
    private JTextField txtSoDienThoai;

    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnThem;
    private JButton btnXoaTrang;
    
    private JComboBox<String> cboChucVu;
    private DefaultTableModel dtm;
    private JTable tblNhanVien;
    
    private ArrayList<NhanVien> dsNhanVien;
    private ArrayList<ChucVu> dsChucVu;
    private Map<String,String> mapChucVu;
    private ChucVuDAO cvDAO;
    private String maNvHienTai;
    public CapNhatNhanVienPanel(String maNvHienTai) {

        ConnectDB.getInstance().connect();
        cvDAO = new ChucVuDAO();
        mapChucVu = new HashMap<>();
    	this.maNvHienTai = maNvHienTai;

    	
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initHeader();
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã nhân viên" , "Tên nhân viên" , "Số điện thoại", "Chức vụ"};
        dtm = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(BG_COLOR);
        pnlTop.add(lblTieuDe, BorderLayout.NORTH);
        pnlTop.add(createInputPanel(), BorderLayout.CENTER);
        pnlTop.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(pnlTop, BorderLayout.NORTH);
        JPanel pnlTop = new JPanel(new BorderLayout(0, 15));
        pnlTop.setBackground(BG_COLOR);
        pnlTop.add(lblTieuDe, BorderLayout.NORTH);
        pnlTop.add(createInputPanel(), BorderLayout.CENTER);
        pnlTop.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(pnlTop, BorderLayout.NORTH);
        add(createBotPanel(), BorderLayout.CENTER);
        
        loadNhanVienData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaNv = new JLabel("Mã NV:");
        lblMaNv.setFont(fontLabel);
        lblTenNv = new JLabel("Tên NV:");
        lblTenNv.setFont(fontLabel);
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setFont(fontLabel);
        lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(fontLabel);
        
        txtMaNv = new JTextField();
        txtMaNv.setFont(fontText);
        txtTenNv = new JTextField();
        txtTenNv.setFont(fontText);
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(fontText);
        
        cboChucVu = new JComboBox<>();
        cboChucVu.setFont(fontText);
        loadChucVuData();
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
        pnlForm.add(lblMaNv, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtMaNv, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenNv, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenNv, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblSoDienThoai, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtSoDienThoai, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblChucVu, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(cboChucVu, gbc);

        return pnlForm;
    }

    private void initButtons() {
        btnThem = createStyledButton("Thêm", BTN_ADD_COLOR);
        btnSua = createStyledButton("Sửa", BTN_EDIT_COLOR);
        btnXoa = createStyledButton("Xóa", BTN_DELETE_COLOR);
        btnXoaTrang = createStyledButton("Xóa trắng", BTN_CLEAR_COLOR);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlButtons.setBackground(BG_COLOR);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnXoaTrang);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblNhanVien = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setFillsViewportHeight(true);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setGridColor(new Color(224, 224, 224));
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaNv = new JLabel("Mã NV:");
        lblMaNv.setFont(fontLabel);
        lblTenNv = new JLabel("Tên NV:");
        lblTenNv.setFont(fontLabel);
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setFont(fontLabel);
        lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setFont(fontLabel);
        
        txtMaNv = new JTextField();
        txtMaNv.setFont(fontText);
        txtTenNv = new JTextField();
        txtTenNv.setFont(fontText);
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(fontText);
        
        cboChucVu = new JComboBox<>();
        cboChucVu.setFont(fontText);
        loadChucVuData();
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
        pnlForm.add(lblMaNv, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtMaNv, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenNv, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenNv, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblSoDienThoai, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtSoDienThoai, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblChucVu, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(cboChucVu, gbc);

        return pnlForm;
    }

    private void initButtons() {
        btnThem = createStyledButton("Thêm", BTN_ADD_COLOR);
        btnSua = createStyledButton("Sửa", BTN_EDIT_COLOR);
        btnXoa = createStyledButton("Xóa", BTN_DELETE_COLOR);
        btnXoaTrang = createStyledButton("Xóa trắng", BTN_CLEAR_COLOR);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createButtonPanel() {
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pnlButtons.setBackground(BG_COLOR);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnXoaTrang);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblNhanVien = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setFillsViewportHeight(true);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setGridColor(new Color(224, 224, 224));
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.setSelectionBackground(new Color(178, 223, 219));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        
        tblNhanVien.setSelectionBackground(new Color(178, 223, 219));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblNhanVien.addMouseListener(this);
        
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblNhanVien.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    

    
    public void loadNhanVienData() {
        NhanVienDAO nvDAO = new NhanVienDAO();
        dsNhanVien = nvDAO.getAllNhanVien();
        
        if (dsChucVu == null || dsChucVu.isEmpty()) {
             loadChucVuData();
        }
        
        Map<String, String> mapTenChucVu = new HashMap<>();
        for(ChucVu cv : dsChucVu) {
            mapTenChucVu.put(cv.getMaChucVu(), cv.getTenChucVu());
        }
        
        dtm.setRowCount(0);
        for(NhanVien nv : dsNhanVien) {
            String tenChucVu = mapTenChucVu.getOrDefault(nv.getmaChucVu(), "Không xác định");
            
            Object[] rowData = {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getSoDienThoai(),
                tenChucVu
            };
            dtm.addRow(rowData);
        }
    }
    
    public void loadChucVuData() {
        dsChucVu = cvDAO.getAllChucVu();
        cboChucVu.removeAllItems();
        for(ChucVu item : dsChucVu) {
            cboChucVu.addItem(item.getTenChucVu());
            mapChucVu.put(item.getMaChucVu(), item.getTenChucVu());
        }
    }
    
    public void xoaTrang() {
        txtMaNv.setText("");
        txtTenNv.setText("");
        txtSoDienThoai.setText("");
        if(cboChucVu.getItemCount() > 0) cboChucVu.setSelectedIndex(0);
        txtMaNv.setEnabled(true);
        loadNhanVienData();
        tblNhanVien.clearSelection();
    }
    }
    

    
    public void loadNhanVienData() {
        NhanVienDAO nvDAO = new NhanVienDAO();
        dsNhanVien = nvDAO.getAllNhanVien();
        
        if (dsChucVu == null || dsChucVu.isEmpty()) {
             loadChucVuData();
        }
        
        Map<String, String> mapTenChucVu = new HashMap<>();
        for(ChucVu cv : dsChucVu) {
            mapTenChucVu.put(cv.getMaChucVu(), cv.getTenChucVu());
        }
        
        dtm.setRowCount(0);
        for(NhanVien nv : dsNhanVien) {
            String tenChucVu = mapTenChucVu.getOrDefault(nv.getmaChucVu(), "Không xác định");
            
            Object[] rowData = {
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getSoDienThoai(),
                tenChucVu
            };
            dtm.addRow(rowData);
        }
    }
    
    public void loadChucVuData() {
        dsChucVu = cvDAO.getAllChucVu();
        cboChucVu.removeAllItems();
        for(ChucVu item : dsChucVu) {
            cboChucVu.addItem(item.getTenChucVu());
            mapChucVu.put(item.getMaChucVu(), item.getTenChucVu());
        }
    }
    
    public void xoaTrang() {
        txtMaNv.setText("");
        txtTenNv.setText("");
        txtSoDienThoai.setText("");
        if(cboChucVu.getItemCount() > 0) cboChucVu.setSelectedIndex(0);
        txtMaNv.setEnabled(true);
        loadNhanVienData();
        tblNhanVien.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnXoa) {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
                return;
            }
            String ma = tblNhanVien.getValueAt(selectedRow, 0).toString();
            if(ma.equalsIgnoreCase(maNvHienTai)) {
                JOptionPane.showMessageDialog(this, "Bạn không thể xóa bản thân!");
                return;
            }
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn xóa nhân viên " + ma + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                NhanVienDAO nvDAO = new NhanVienDAO();
                boolean result =  nvDAO.deleteNhanVien(ma);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                    loadNhanVienData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên không thành công!");
                }
            }
        }
        else if(o == btnThem) {
            if(validateInput(true)) {
                String maNV = txtMaNv.getText().trim();
                String tenNV = txtTenNv.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String chucVu = cboChucVu.getSelectedItem().toString();
                
                ChucVu cv = cvDAO.getByName(chucVu);
                NhanVienDAO nvDAO = new NhanVienDAO();
                
                String maCV = (cv != null) ? cv.getMaChucVu() : ""; 
                
                NhanVien nvNew = new NhanVien(maNV, tenNV, maCV, soDienThoai, true);
                boolean result = nvDAO.addNhanVien(nvNew);

                if(result) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    loadNhanVienData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên không thành công!");
                }
            }
        }
        else if(o == btnSua) {
            if(validateInput(false)) {
                String maNV = txtMaNv.getText().trim();
                String tenNV = txtTenNv.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String chucVu = cboChucVu.getSelectedItem().toString();
                String macv = "BHA";
                
                for (Map.Entry<String, String> item : mapChucVu.entrySet()) {
                    if(chucVu.equalsIgnoreCase(item.getValue())) {
                        macv = item.getKey();
                        break;
                    }
                }
                
                NhanVienDAO nvDAO = new NhanVienDAO();
                NhanVien nvNew = new NhanVien(maNV, tenNV, macv, soDienThoai, true);
                boolean result = nvDAO.updateNhanVien(nvNew);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                    loadNhanVienData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên không thành công!");
                }
            }
        }
        else if(o == btnXoaTrang) {
            xoaTrang();
        }
    }
    
    private boolean validateInput(boolean isAddingNew) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        dsNhanVien = nvDAO.getAllNhanVien();

        if (txtMaNv.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
            txtMaNv.requestFocus();
            return false;
        }
        if (txtTenNv.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            txtTenNv.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
            txtSoDienThoai.requestFocus();
            return false;
        }
                if(result) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    loadNhanVienData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên không thành công!");
                }
            }
        }
        else if(o == btnSua) {
            if(validateInput(false)) {
                String maNV = txtMaNv.getText().trim();
                String tenNV = txtTenNv.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String chucVu = cboChucVu.getSelectedItem().toString();
                String macv = "BHA";
                
                for (Map.Entry<String, String> item : mapChucVu.entrySet()) {
                    if(chucVu.equalsIgnoreCase(item.getValue())) {
                        macv = item.getKey();
                        break;
                    }
                }
                
                NhanVienDAO nvDAO = new NhanVienDAO();
                NhanVien nvNew = new NhanVien(maNV, tenNV, macv, soDienThoai, true);
                boolean result = nvDAO.updateNhanVien(nvNew);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
                    loadNhanVienData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên không thành công!");
                }
            }
        }
        else if(o == btnXoaTrang) {
            xoaTrang();
        }
    }
    
    private boolean validateInput(boolean isAddingNew) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        dsNhanVien = nvDAO.getAllNhanVien();

        if (txtMaNv.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
            txtMaNv.requestFocus();
            return false;
        }
        if (txtTenNv.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            txtTenNv.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!");
            txtSoDienThoai.requestFocus();
            return false;
        }

        String maNV = txtMaNv.getText().trim();
        if(isAddingNew) {
            for(NhanVien item : dsNhanVien) {
                if(item.getMaNV().equalsIgnoreCase(maNV)) {
                    JOptionPane.showMessageDialog(this, "Mã nhân viên không được trùng!");
                    txtMaNv.requestFocus();
                    return false;
                }
            }
        }
        if (!maNV.matches("NV\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải có định dạng NV kèm 3 ký số (Ví dụ: NV001)!");
            txtMaNv.requestFocus();
            return false;
        }
        
        String tenNV = txtTenNv.getText().trim();
        if (!tenNV.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được chứa số hoặc ký tự đặc biệt!");
            txtTenNv.requestFocus(); 
            return false;
        }
        
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có đúng 10 ký số!");
            txtSoDienThoai.requestFocus();
            return false;
        }
        return true;
    }
        String maNV = txtMaNv.getText().trim();
        if(isAddingNew) {
            for(NhanVien item : dsNhanVien) {
                if(item.getMaNV().equalsIgnoreCase(maNV)) {
                    JOptionPane.showMessageDialog(this, "Mã nhân viên không được trùng!");
                    txtMaNv.requestFocus();
                    return false;
                }
            }
        }
        if (!maNV.matches("NV\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải có định dạng NV kèm 3 ký số (Ví dụ: NV001)!");
            txtMaNv.requestFocus();
            return false;
        }
        
        String tenNV = txtTenNv.getText().trim();
        if (!tenNV.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được chứa số hoặc ký tự đặc biệt!");
            txtTenNv.requestFocus(); 
            return false;
        }
        
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có đúng 10 ký số!");
            txtSoDienThoai.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblNhanVien) {
            int row = tblNhanVien.getSelectedRow();
            if (row >= 0) {
                txtMaNv.setText(tblNhanVien.getValueAt(row, 0).toString());
                txtTenNv.setText(tblNhanVien.getValueAt(row, 1).toString());
                txtSoDienThoai.setText(tblNhanVien.getValueAt(row, 2).toString());
                String txt = tblNhanVien.getValueAt(row, 3).toString();
                cboChucVu.setSelectedItem(txt);
                
                txtMaNv.setEnabled(false);
            }
        }
    }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
    @Override
    public void mouseExited(MouseEvent e) {}
}