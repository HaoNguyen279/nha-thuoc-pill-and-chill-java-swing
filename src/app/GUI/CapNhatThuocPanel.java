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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB.ConnectDB;
import app.DAO.DonViDAO;
import app.DAO.HoaDonDAO;
import app.DAO.NhaSanXuatDAO;
import app.DAO.ThuocDAO;
import app.DTO.ThuocKemGiaDTO;
import app.Entity.DonVi;
import app.Entity.NhaSanXuat;
import app.Entity.Thuoc;

public class CapNhatThuocPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);
    private final Color BTN_REFRESH_COLOR = new Color(57, 155, 226);
    
    private JLabel lblTieuDe;
    private JLabel lblMaThuoc;
    private JLabel lblTenThuoc;
    private JLabel lblSoLuongTon;
    private JLabel lblDonVi;
    private JLabel lblNhaSanXuat;

    private JTextField txtMaThuoc;
    private JTextField txtTenThuoc;
    private JTextField txtSoLuongTon;

    private JComboBox<String> cboDonVi;
    private JComboBox<String> cboNhaSanXuat;

    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnThem;
    private JButton btnXoaTrang;
    private JButton btnLamMoi;
    private JButton btnSuaTrangThai;
    
    private DefaultTableModel dtm;
    private JTable tblThuoc;
    
    private ArrayList<ThuocKemGiaDTO> dsThuoc;
    private ArrayList<DonVi> dsDonVi;
    private Map<String,String> mapDonVi;
    private ArrayList<NhaSanXuat> dsNhaSanXuat;
    private NhaSanXuatDAO nsxDAO;
    private Map<String, String> mapNhaSanXuat;
	private CardLayout cardLayout;
	private JPanel mainContainer;

    
    public CapNhatThuocPanel() {
    	FlatLightLaf.setup();
        ConnectDB.getInstance().connect();
        nsxDAO = new NhaSanXuatDAO();
        mapNhaSanXuat = new HashMap<>();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        setLayout(new BorderLayout());

        initHeader();
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã thuốc" , "Tên thuốc", "Số lượng","Giá hiện tại", "Đơn vị", "Nhà SX"};
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
        JPanel pnlMain = new JPanel(new BorderLayout(10,10));
        pnlMain.setBackground(BG_COLOR);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(createBotPanel(), BorderLayout.CENTER);
        
        mainContainer.add(pnlMain, "DanhSach");
        cardLayout.show(mainContainer, "DanhSach");
        
        add(mainContainer,BorderLayout.CENTER);
        loadThuocData();
        loadNhaSanXuatData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ THUỐC", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaThuoc = new JLabel("Mã thuốc:");
        lblMaThuoc.setFont(fontLabel);
        lblTenThuoc = new JLabel("Tên thuốc:");
        lblTenThuoc.setFont(fontLabel);
        lblSoLuongTon = new JLabel("Số lượng:");
        lblSoLuongTon.setFont(fontLabel);
        
        lblDonVi = new JLabel("Đơn vị:");
        lblDonVi.setFont(fontLabel);
        lblNhaSanXuat = new JLabel("Nhà sản xuất:");
        lblNhaSanXuat.setFont(fontLabel);
        
        txtMaThuoc = new JTextField();
        txtMaThuoc.setFont(fontText);
        txtTenThuoc = new JTextField();
        txtTenThuoc.setFont(fontText);
        txtSoLuongTon = new JTextField();
        txtSoLuongTon.setFont(fontText);

        
        cboDonVi = new JComboBox<>();
        cboDonVi.setFont(fontText);
        loadDonViData();
        
        cboNhaSanXuat = new JComboBox<>();
        cboNhaSanXuat.setFont(fontText);
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

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblMaThuoc, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtMaThuoc, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenThuoc, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenThuoc, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblSoLuongTon, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtSoLuongTon, gbc);
        
      

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        pnlForm.add(lblDonVi, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.4;
        pnlForm.add(cboDonVi, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblNhaSanXuat, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(cboNhaSanXuat, gbc);

        return pnlForm;
    }

    private void initButtons() {
        btnThem = createStyledButton("Thêm", BTN_ADD_COLOR);
        btnSua = createStyledButton("Sửa", BTN_EDIT_COLOR);
        btnXoa = createStyledButton("Xóa", BTN_DELETE_COLOR);
        btnXoaTrang = createStyledButton("Xóa trắng", BTN_CLEAR_COLOR);
        btnLamMoi = createStyledButton("Làm mới", BTN_REFRESH_COLOR);
        btnSuaTrangThai = createStyledButton("Thuốc đã xóa", new Color(153,102,204));
        btnSuaTrangThai.setPreferredSize(new Dimension(150,40));
        
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnSuaTrangThai.addActionListener(this);
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
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(BG_COLOR);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnXoaTrang);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnSuaTrangThai);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblThuoc = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblThuoc.setRowHeight(35);
        tblThuoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblThuoc.setFillsViewportHeight(true);
        tblThuoc.setShowGrid(true);
        tblThuoc.setGridColor(new Color(224, 224, 224));
        tblThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblThuoc.setSelectionBackground(new Color(178, 223, 219));
        tblThuoc.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblThuoc.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblThuoc.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblThuoc.getColumnModel().getColumn(4).setPreferredWidth(50);
        tblThuoc.getColumnModel().getColumn(5).setPreferredWidth(150);

        tblThuoc.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadThuocData() {
        ThuocDAO thuocDAO = new ThuocDAO();
        dsThuoc = thuocDAO.getAllThuocCoTenNSXvaGia();
        dtm.setRowCount(0);
        for(ThuocKemGiaDTO thuoc : dsThuoc) {
            Object[] rowData = {
                    thuoc.getMaThuoc(),
                    thuoc.getTenThuoc(),
                    thuoc.getSoLuongTon(),
                    thuoc.getGiaBan(),
                    thuoc.getDonVi(),
                    thuoc.getMaNSX()
            };
            dtm.addRow(rowData);
            System.out.println(thuoc.toString());
        }
    }
    
    public void loadDonViData() {
        DonViDAO donViDAO = new DonViDAO();
        dsDonVi = donViDAO.getAllDonVi();
        mapDonVi = new HashMap<String, String>();
        cboDonVi.removeAllItems();
        for(DonVi dv : dsDonVi) {
            cboDonVi.addItem(dv.getTenDonVi());
            mapDonVi.put(dv.getTenDonVi(), dv.getMaDonVi());
        }
    }
    
    public void loadNhaSanXuatData() {
        dsNhaSanXuat = nsxDAO.getAllNhaSanXuat();
        cboNhaSanXuat.removeAllItems();
        for(NhaSanXuat item : dsNhaSanXuat) {
            cboNhaSanXuat.addItem(item.getTenNSX());
            mapNhaSanXuat.put(item.getMaNSX(), item.getTenNSX());
        }
    }
    
    public void xoaTrang() {
        txtMaThuoc.setText("");
        txtTenThuoc.setText("");
      
        txtSoLuongTon.setText("0");
        cboDonVi.setSelectedIndex(0);
        cboNhaSanXuat.setSelectedIndex(0);
        txtMaThuoc.setEnabled(true);
        tblThuoc.clearSelection();
        loadThuocData();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnXoa) {
            int selectedRow = tblThuoc.getSelectedRow();
            if(selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thuốc cần xóa!");
                return;
            }
            String maThuoc = tblThuoc.getValueAt(selectedRow, 0).toString();
            String maLo = tblThuoc.getValueAt(selectedRow, 1).toString();
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn xóa thuốc " + maThuoc + " - Lô " + maLo + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                ThuocDAO thuocDAO = new ThuocDAO();
                boolean result = thuocDAO.deleteThuoc(maThuoc);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Xóa thuốc thành công!");
                    loadThuocData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thuốc không thành công!");
                }
            }
        }
        else if(o == btnThem) {
            if(validateInput(true)) {
                String maThuoc = txtMaThuoc.getText().trim();
                String tenThuoc = txtTenThuoc.getText().trim();
                int soLuongTon = 0;
                
                String tenDonVi = cboDonVi.getSelectedItem().toString();
                String maDonVi = mapDonVi.get(tenDonVi);
                
                int soLuongToiThieu = 0;
                
                String tenNSX = cboNhaSanXuat.getSelectedItem().toString();
                String maNSX = "";
                for (Map.Entry<String, String> item : mapNhaSanXuat.entrySet()) {
                    if(tenNSX.equalsIgnoreCase(item.getValue())) {
                        maNSX = item.getKey();
                        break;
                    }
                }
                
                ThuocDAO thuocDAO = new ThuocDAO();
                Thuoc thuocNew = new Thuoc(maThuoc, tenThuoc, soLuongTon, 
                                           maDonVi, soLuongToiThieu, maNSX, true);
                boolean result = thuocDAO.addThuoc(thuocNew);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Thêm thuốc thành công!");
                    loadThuocData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thuốc không thành công!");
                }
            }
        }
        else if(o == btnSua) {
            if(validateInput(false)) {
                String maThuoc = txtMaThuoc.getText().trim();
                String tenThuoc = txtTenThuoc.getText().trim();
                int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
               
                String donVi = cboDonVi.getSelectedItem().toString();
                int soLuongToiThieu = 0;
                
                String tenNSX = cboNhaSanXuat.getSelectedItem().toString();
                String maNSX = "";
                for (Map.Entry<String, String> item : mapNhaSanXuat.entrySet()) {
                    if(tenNSX.equals(item.getValue())) {
                        maNSX = item.getKey();
                        break;
                    }
                }
                
                ThuocDAO thuocDAO = new ThuocDAO();
                Thuoc thuocUpdate = new Thuoc(maThuoc, tenThuoc, soLuongTon, 
                                              donVi, soLuongToiThieu, maNSX, true);
                boolean result = thuocDAO.updateThuoc(thuocUpdate);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thuốc thành công!");
                    loadThuocData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thuốc không thành công!");
                }
            }
        }
        else if(o == btnXoaTrang) {
            xoaTrang();
        }
        else if(o == btnLamMoi) {
        	xoaTrang();
            loadThuocData();
            loadDonViData();
            loadNhaSanXuatData();
        }
        else if(o == btnSuaTrangThai) {
        	System.out.println("cc");
        	
            
            JPanel pnlThuocDaXoa = new CapNhatThuocSubPanel(this);
            
            try {
                mainContainer.remove(mainContainer.getComponent(1));
            } catch (Exception ex) {
                // Không có panel chi tiết cũ
            }
            
            mainContainer.add(pnlThuocDaXoa, "ChiTiet");
            cardLayout.show(mainContainer, "ChiTiet");
        }
    }
    
    public void quayLaiDanhSach() {
        cardLayout.show(mainContainer, "DanhSach");
        loadThuocData();
    }
    
    private boolean validateInput(boolean isAddingNew) {
        if (txtMaThuoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã thuốc không được để trống!");
            txtMaThuoc.requestFocus();
            return false;
        }
        if (txtTenThuoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thuốc không được để trống!");
            txtTenThuoc.requestFocus();
            return false;
        }
        
        
        String maThuoc = txtMaThuoc.getText().trim();
        if(isAddingNew) {
            for(ThuocKemGiaDTO item : dsThuoc) {
                if(item.getMaThuoc().equalsIgnoreCase(maThuoc)) {
                    JOptionPane.showMessageDialog(this, "Mã thuốc không được trùng!");
                    txtMaThuoc.requestFocus();
                    return false;
                }
            }
        }
        if (!maThuoc.matches("T\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Mã thuốc phải có định dạng T kèm 3 ký số (Ví dụ: T001)!");
            txtMaThuoc.requestFocus();
            return false;
        }
    
       
        
        return true;
    }
    
    @Override   
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblThuoc) {
            txtMaThuoc.setEnabled(false);
            txtSoLuongTon.setEnabled(false);
            int row = tblThuoc.getSelectedRow();
            if (row >= 0) {
                txtMaThuoc.setText(tblThuoc.getValueAt(row, 0).toString());
                txtTenThuoc.setText(tblThuoc.getValueAt(row, 1).toString());
                txtSoLuongTon.setText(tblThuoc.getValueAt(row, 2).toString());
             
                cboDonVi.setSelectedItem(tblThuoc.getValueAt(row, 4).toString());
                cboNhaSanXuat.setSelectedItem(tblThuoc.getValueAt(row, 5).toString());
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