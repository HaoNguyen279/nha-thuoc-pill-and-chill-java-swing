package app.GUI;

import java.awt. BorderLayout;
import java.awt.CardLayout;
import java.awt. Color;
import java.awt. Component;
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
import java. awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing. JButton;
import javax.swing. JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax. swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing. ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import app. ConnectDB. ConnectDB;
import app. DAO.KhuyenMaiDAO;
import app.Entity.KhuyenMai;

public class CapNhatKhuyenMaiSubPanel extends JPanel implements ActionListener, MouseListener {
    
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblMaKM;
    private JLabel lblMucGiam;
    private JLabel lblngayApDung;
    private JLabel lblNgayKetThuc;

    private JTextField txtMaKM;
    private JTextField txtMucGiam;
    private JDateChooser calNgayApDung;
    private JDateChooser calNgayKetThuc;

    private JButton btnKhoiPhuc;
    private JButton btnQuayLai;
    
    private DefaultTableModel dtm;
    private JTable tblKhuyenMai;
    
    private ArrayList<KhuyenMai> dsKhuyenMai;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private CapNhatKhuyenMaiPanel pnlCapNhatKhuyenMai;
    private KhuyenMaiDAO khuyenMaiDAO;
    
    Date today;
    
    public CapNhatKhuyenMaiSubPanel(CapNhatKhuyenMaiPanel pnlCapNhatKhuyenMai) {
        this.pnlCapNhatKhuyenMai = pnlCapNhatKhuyenMai;
        FlatLightLaf.setup();
        ConnectDB.getInstance().connect();
        khuyenMaiDAO = new KhuyenMaiDAO();
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
        initInputForm();
        initButtons();
        
        String[] cols = {"Mã khuyến mãi", "Mức giảm", "Ngày bắt đầu", "Ngày kết thúc"};
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
        loadKhuyenMaiData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ KHUYẾN MÃI ĐÃ XÓA", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaKM = new JLabel("Mã KM:");
        lblMaKM.setFont(fontLabel);
        lblMucGiam = new JLabel("Mức giảm:");
        lblMucGiam.setFont(fontLabel);
        lblngayApDung = new JLabel("Ngày bắt đầu:");
        lblngayApDung.setFont(fontLabel);
        lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc. setFont(fontLabel);
        
        txtMaKM = new JTextField();
        txtMaKM.setFont(fontText);
        txtMaKM.setPreferredSize(new Dimension(200, 35));
        
        txtMucGiam = new JTextField();
        txtMucGiam.setFont(fontText);
        txtMucGiam.setPreferredSize(new Dimension(200, 35));
        
        calNgayApDung = new JDateChooser();
        calNgayApDung.setDateFormatString("dd/MM/yyyy");
        calNgayApDung.setFont(fontText);
        calNgayApDung.setPreferredSize(new Dimension(200, 35));
        
        calNgayKetThuc = new JDateChooser();
        calNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        calNgayKetThuc.setFont(fontText);
        calNgayKetThuc.setPreferredSize(new Dimension(200, 35));
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

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm. add(lblMaKM, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm. add(txtMaKM, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblMucGiam, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtMucGiam, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblngayApDung, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc. weightx = 0.4;
        pnlForm.add(calNgayApDung, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblNgayKetThuc, gbc);
        gbc.gridx = 3; gbc. gridy = 1; gbc.weightx = 0.4;
        pnlForm. add(calNgayKetThuc, gbc);

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
        pnlButtons. setBackground(BG_COLOR);
        pnlButtons.add(btnKhoiPhuc);
        pnlButtons.add(btnQuayLai);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblKhuyenMai = new JTable(dtm) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
        
        tblKhuyenMai.setRowHeight(35);
        tblKhuyenMai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhuyenMai.setFillsViewportHeight(true);
        tblKhuyenMai.setShowGrid(true);
        tblKhuyenMai.setGridColor(new Color(224, 224, 224));
        tblKhuyenMai.setSelectionMode(ListSelectionModel. SINGLE_SELECTION);
        tblKhuyenMai.setSelectionBackground(new Color(178, 223, 219));
        tblKhuyenMai.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer. setHorizontalAlignment(JLabel.CENTER);

        // Center align cho tất cả các cột
        DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
        cellCenter.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblKhuyenMai.getColumnCount(); i++) {
            tblKhuyenMai. getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        tblKhuyenMai.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadKhuyenMaiData() {
        dsKhuyenMai = khuyenMaiDAO. getAllKhuyenMaiInactive();
        dtm.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for(KhuyenMai km : dsKhuyenMai) {
            Object[] rowData = {
                    km.getMaKM(),
                    km.getMucGiamGia(),
                    sdf. format(km.getNgayApDung()),
                    sdf.format(km.getNgayKetThuc())
            };
            dtm.addRow(rowData);
        }
    }
    
    public void xoaTrang() {
        txtMaKM.setText("");
        txtMucGiam.setText("");
        calNgayApDung.setDate(today);
        calNgayKetThuc.setDate(today);
        txtMaKM.setEnabled(true);
        tblKhuyenMai. clearSelection();
        loadKhuyenMaiData();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if(o == btnKhoiPhuc) {
            String maKM = txtMaKM.getText().trim();
            
            if(maKM.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần khôi phục!");
                return;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn khôi phục khuyến mãi " + maKM + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                boolean result = khuyenMaiDAO.reactiveKhuyenMai(maKM);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Khôi phục khuyến mãi thành công!");
                    loadKhuyenMaiData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Khôi phục khuyến mãi không thành công!");
                }
            }
        }
        else if(o == btnQuayLai) {
            pnlCapNhatKhuyenMai.quayLaiDanhSach();
        }
    }
    
    @Override   
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblKhuyenMai) {
            txtMaKM.setEnabled(false);
            int row = tblKhuyenMai.getSelectedRow();
            if (row >= 0) {
                txtMaKM.setText(tblKhuyenMai.getValueAt(row, 0).toString());
                txtMucGiam.setText(tblKhuyenMai.getValueAt(row, 1).toString());
                
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dateApDungString = tblKhuyenMai.getValueAt(row, 2).toString();
                    String dateKetThucString = tblKhuyenMai. getValueAt(row, 3).toString();
                    calNgayApDung.setDate(sdf.parse(dateApDungString));
                    calNgayKetThuc.setDate(sdf.parse(dateKetThucString));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
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