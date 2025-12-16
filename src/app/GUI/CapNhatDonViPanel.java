package app.GUI;

import java.awt.BorderLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import app.ConnectDB.ConnectDB;
import app.DAO.ChucVuDAO;
import app.DAO.DonViDAO;
import app.Entity.ChucVu;
import app.Entity.DonVi;

public class CapNhatDonViPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaDonVi;
    private JLabel lblTenDonVi;
    
    private JTextField txtMaDonVi;
    private JTextField txtTenDonVi;

    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnThem;
    private JButton btnXoaTrang;
    
    private DefaultTableModel dtm;
    private JTable tblChucVu;
    
    private ArrayList<DonVi> dsDonVi;
    private DonViDAO dvDAO;
    
    public CapNhatDonViPanel() {
        ConnectDB.getInstance().connect();
        dvDAO = new DonViDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initHeader();
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã đơn vị" , "Tên đơn vị"};
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
        add(createBotPanel(), BorderLayout.CENTER);
        
        loadDonViData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ ĐƠN VỊ", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaDonVi = new JLabel("Mã đơn vị:");
        lblMaDonVi.setFont(fontLabel);
        
        lblTenDonVi = new JLabel("Tên đơn vị:");
        lblTenDonVi.setFont(fontLabel);
        
        txtMaDonVi = new JTextField();
        txtMaDonVi.setFont(fontText);
        
        txtTenDonVi = new JTextField();
        txtTenDonVi.setFont(fontText);
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
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblMaDonVi, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.9;
        pnlForm.add(txtMaDonVi, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblTenDonVi, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.9;
        pnlForm.add(txtTenDonVi, gbc);

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
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlButtons.setBackground(BG_COLOR);
        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);
        pnlButtons.add(btnXoaTrang);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblChucVu = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblChucVu.setRowHeight(35);
        tblChucVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChucVu.setFillsViewportHeight(true);
        tblChucVu.setShowGrid(true);
        tblChucVu.setGridColor(new Color(224, 224, 224));
        tblChucVu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChucVu.setSelectionBackground(new Color(178, 223, 219));
        tblChucVu.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblChucVu.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        // center note
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < tblChucVu. getColumnCount(); i++) {
            tblChucVu. getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }
        
        tblChucVu.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblChucVu);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadDonViData() {
        dsDonVi = dvDAO.getAllDonVi();
        dtm.setRowCount(0);
        for(DonVi dv : dsDonVi) {
            Object[] rowData = {
            		dv.getMaDonVi(),
            		dv.getTenDonVi(),
            };
            dtm.addRow(rowData);
        }
    }
    
    public void xoaTrang() {
        txtMaDonVi.setText("");
        txtTenDonVi.setText("");
        txtMaDonVi.setEnabled(true);
        tblChucVu.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnXoa) {
            int selectedRow = tblChucVu.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn vị cần xóa!");
                return;
            }
            String maCV = tblChucVu.getValueAt(selectedRow, 0).toString();

            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn xóa đơn vị " + maCV + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                boolean result = dvDAO.deleteDonVi(maCV);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Xóa đơn vị thành công!");
                    loadDonViData();
                    xoaTrang();
                }
                else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể xóa đơn vị này!\nHãy chắc chắn rằng đơn vị muốn xóa không có nhân viên.");
                }
            }
        }
        else if(o == btnThem) {
            if(validateInput(true)) {
                String maChucVu = txtMaDonVi.getText().trim();
                String tenChucVu = txtTenDonVi.getText().trim();
                
                DonVi cv = new DonVi(maChucVu, tenChucVu, true);
                boolean result = dvDAO.addDonVi(cv);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Thêm đơn vị thành công!");
                    loadDonViData();
                    xoaTrang();
                }
                else {
                    JOptionPane.showMessageDialog(this, "Thêm đơn vị không thành công!");
                }
            }
        }
        else if(o == btnSua) {
            if(validateInput(false)) {
                String maChucVu = txtMaDonVi.getText().trim();
                String tenChucVu = txtTenDonVi.getText().trim();
                
                DonVi cv = new DonVi(maChucVu, tenChucVu, true);
                boolean result = dvDAO.updateDonVi(cv);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Cập nhật đơn vị thành công!");
                    loadDonViData();
                    xoaTrang();
                }
                else {
                    JOptionPane.showMessageDialog(this, "Cập nhật đơn vị không thành công!");
                }
            }
        }
        else if(o == btnXoaTrang) {
            xoaTrang();
        }
    }
    
    private boolean validateInput(boolean isAddingNew) {
        if (txtMaDonVi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã đơn vị không được để trống!");
            txtMaDonVi.requestFocus();
            return false;
        }
        if (txtTenDonVi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đơn vị không được để trống!");
            txtTenDonVi.requestFocus();
            return false;
        }
        
        if (isAddingNew) {
            String maMoi = txtMaDonVi.getText().trim();
            for (DonVi cv : dsDonVi) {
                if (cv.getMaDonVi().equalsIgnoreCase(maMoi)) {
                    JOptionPane.showMessageDialog(this, "Mã đơn vị đã tồn tại!");
                    txtMaDonVi.requestFocus();
                    return false;
                }
            }
        }
        
        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblChucVu) {
            int row = tblChucVu.getSelectedRow();
            if (row >= 0) {
                txtMaDonVi.setText(tblChucVu.getValueAt(row, 0).toString());
                txtTenDonVi.setText(tblChucVu.getValueAt(row, 1).toString());
                txtMaDonVi.setEnabled(false);
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