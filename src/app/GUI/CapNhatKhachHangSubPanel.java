package app.GUI;

import java.awt. BorderLayout;
import java.awt. CardLayout;
import java.awt. Color;
import java.awt.Component;
import java.awt. Cursor;
import java.awt. Dimension;
import java.awt. FlowLayout;
import java. awt.Font;
import java. awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt. Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java. util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing. JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing. ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;

import app.ConnectDB.ConnectDB;
import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;

public class CapNhatKhachHangSubPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaKh;
    private JLabel lblTenKh;
    private JLabel lblSoDienThoai;
    private JLabel lblDiemTichLuy;

    private JTextField txtMaKh;
    private JTextField txtTenKh;
    private JTextField txtSoDienThoai;
    private JTextField txtDiemTichLuy;

    private JButton btnKhoiPhuc;
    private JButton btnQuayLai;
    
    private DefaultTableModel dtm;
    private JTable tblKhachHang;
    
    private ArrayList<KhachHang> dsKhachHang;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private CapNhatKhachHangPanel pnlCapNhatKhachHang;
    private KhachHangDAO khachHangDAO;
    
    public CapNhatKhachHangSubPanel(CapNhatKhachHangPanel pnlCapNhatKhachHang) {
        this.pnlCapNhatKhachHang = pnlCapNhatKhachHang;
        FlatLightLaf.setup();
        ConnectDB.getInstance().connect();
        khachHangDAO = new KhachHangDAO();
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        setLayout(new BorderLayout());

        initHeader();
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy"};
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
        loadKhachHangData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ KHÁCH HÀNG ĐÃ XÓA", SwingConstants. CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaKh = new JLabel("Mã KH:");
        lblMaKh.setFont(fontLabel);
        lblTenKh = new JLabel("Tên KH:");
        lblTenKh.setFont(fontLabel);
        lblSoDienThoai = new JLabel("Số ĐT:");
        lblSoDienThoai.setFont(fontLabel);
        lblDiemTichLuy = new JLabel("Điểm tích lũy:");
        lblDiemTichLuy. setFont(fontLabel);
        
        txtMaKh = new JTextField();
        txtMaKh.setFont(fontText);
        txtMaKh.setPreferredSize(new Dimension(200, 35));
        
        txtTenKh = new JTextField();
        txtTenKh.setFont(fontText);
        txtTenKh.setPreferredSize(new Dimension(200, 35));
        
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(fontText);
        txtSoDienThoai.setPreferredSize(new Dimension(200, 35));
        
        txtDiemTichLuy = new JTextField();
        txtDiemTichLuy.setFont(fontText);
        txtDiemTichLuy.setPreferredSize(new Dimension(200, 35));
    }

    private JPanel createInputPanel() {
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(ACCENT_COLOR);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 50, 20, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints. HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 20);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm. add(lblMaKh, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm. add(txtMaKh, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenKh, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenKh, gbc);

        // Row 1
        gbc. gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblSoDienThoai, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtSoDienThoai, gbc);
        
        gbc. gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblDiemTichLuy, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(txtDiemTichLuy, gbc);

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
        tblKhachHang = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (! isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblKhachHang.setRowHeight(35);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhachHang.setFillsViewportHeight(true);
        tblKhachHang.setShowGrid(true);
        tblKhachHang.setGridColor(new Color(224, 224, 224));
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhachHang.setSelectionBackground(new Color(178, 223, 219));
        tblKhachHang.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblKhachHang.getTableHeader();
        header.setFont(new Font("Segoe UI", Font. BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblKhachHang.getColumnCount(); i++) {
            tblKhachHang.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        tblKhachHang.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadKhachHangData() {
        dsKhachHang = khachHangDAO.getAllKhachHangInActive();
        
        dtm.setRowCount(0);
        for(KhachHang kh :  dsKhachHang) {
            Object[] rowData = {
                kh.getMaKH(),
                kh.getTenKH(),
                kh. getSoDienThoai(),
                kh.getDiemTichLuy()
            };
            dtm.addRow(rowData);
        }
    }
    
    public void xoaTrang() {
        txtMaKh.setText("");
        txtTenKh.setText("");
        txtSoDienThoai.setText("");
        txtDiemTichLuy. setText("");
        txtMaKh.setEnabled(true);
        tblKhachHang.clearSelection();
        loadKhachHangData();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if(o == btnKhoiPhuc) {
            String maKH = txtMaKh.getText().trim();
            
            if(maKH.isEmpty()) {
                JOptionPane. showMessageDialog(this, "Vui lòng chọn khách hàng cần khôi phục!");
                return;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn khôi phục khách hàng " + maKH + "? ", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                boolean result = khachHangDAO.reactivateKhachHang(maKH);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Khôi phục khách hàng thành công!");
                    loadKhachHangData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Khôi phục khách hàng không thành công!");
                }
            }
        }
        else if(o == btnQuayLai) {
            pnlCapNhatKhachHang.quayLaiDanhSach();
        }
    }
    
    @Override   
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblKhachHang) {
            txtMaKh.setEnabled(false);
            int row = tblKhachHang.getSelectedRow();
            if (row >= 0) {
                txtMaKh.setText(tblKhachHang.getValueAt(row, 0).toString());
                txtTenKh.setText(tblKhachHang. getValueAt(row, 1).toString());
                txtSoDienThoai.setText(tblKhachHang.getValueAt(row, 2).toString());
                txtDiemTichLuy.setText(tblKhachHang.getValueAt(row, 3).toString());
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