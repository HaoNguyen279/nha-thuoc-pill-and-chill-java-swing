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

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import app.Entity.DonVi;

public class CapNhatDonViSubPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaDonVi;
    private JLabel lblTenDonVi;
    
    private JTextField txtMaDonVi;
    private JTextField txtTenDonVi;

    private JButton btnKhoiPhuc;
    private JButton btnQuayLai;
    
    private DefaultTableModel dtm;
    private JTable tblDonVi;
    
    private ArrayList<DonVi> dsDonVi;
    private DonViDAO dvDao;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private CapNhatDonViPanel pnlCapNhatDonVi;
    
    public CapNhatDonViSubPanel(CapNhatDonViPanel pnlCapNhatDonVi) {
        this.pnlCapNhatDonVi = pnlCapNhatDonVi;
        FlatLightLaf.setup();
        ConnectDB.getInstance().connect();
        dvDao = new DonViDAO();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        setLayout(new BorderLayout());

        initHeader();
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã đơn vị", "Tên đơn vị"};
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
        
        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBackground(BG_COLOR);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  
        pnlMain.add(pnlTop, BorderLayout.NORTH);
        pnlMain.add(createBotPanel(), BorderLayout.CENTER);
        
        mainContainer.add(pnlMain, "DanhSach");
        cardLayout.show(mainContainer, "DanhSach");
        
        add(mainContainer, BorderLayout.CENTER);
        loadDonViData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ ĐƠN VỊ ĐÃ XÓA", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
        txtMaDonVi.setPreferredSize(new Dimension(200, 35));
        
        txtTenDonVi = new JTextField();
        txtTenDonVi.setFont(fontText);
        txtTenDonVi.setPreferredSize(new Dimension(200, 35));
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
        btnKhoiPhuc = createStyledButton("Khôi phục", BTN_ADD_COLOR);
        btnQuayLai = createStyledButton("Quay Lại", BTN_CLEAR_COLOR);
        
        btnKhoiPhuc.addActionListener(this);
        btnQuayLai.addActionListener(this);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 45));
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
        pnlButtons.add(btnKhoiPhuc);
        pnlButtons.add(btnQuayLai);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblDonVi = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblDonVi.setRowHeight(35);
        tblDonVi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDonVi.setFillsViewportHeight(true);
        tblDonVi.setShowGrid(true);
        tblDonVi.setGridColor(new Color(224, 224, 224));
        tblDonVi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDonVi.setSelectionBackground(new Color(178, 223, 219));
        tblDonVi.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblDonVi.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblDonVi.getColumnCount(); i++) {
            tblDonVi.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        tblDonVi.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblDonVi);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadDonViData() {
        dsDonVi = dvDao.getAllInactiveDonVi();
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
        tblDonVi.clearSelection();
        loadDonViData();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if(o == btnKhoiPhuc) {
            String maDV = txtMaDonVi.getText().trim();
            
            if(maDV.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn vị cần khôi phục!");
                return;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn khôi phục đơn vị " + maDV + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                boolean result = dvDao.reactivateDonVi(maDV);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Khôi phục đơn vị thành công!");
                    loadDonViData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Khôi phục đơn vị không thành công!");
                }
            }
        }
        else if(o == btnQuayLai) {
            pnlCapNhatDonVi.quayLaiDanhSach();
        }
    }
    
    @Override   
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblDonVi) {
            txtMaDonVi.setEnabled(false);
            int row = tblDonVi.getSelectedRow();
            if (row >= 0) {
                txtMaDonVi.setText(tblDonVi.getValueAt(row, 0).toString());
                txtTenDonVi.setText(tblDonVi.getValueAt(row, 1).toString());
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
