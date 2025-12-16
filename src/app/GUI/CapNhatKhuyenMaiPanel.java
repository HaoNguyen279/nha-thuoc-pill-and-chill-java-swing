package app.GUI;

import java.awt.BorderLayout;
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
import java. awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import com.toedter.calendar.JDateChooser;

import app.ConnectDB. ConnectDB;
import app. DAO.KhuyenMaiDAO;
import app.Entity.KhuyenMai;

public class CapNhatKhuyenMaiPanel extends JPanel implements ActionListener, MouseListener {
    
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
    private JLabel lblMaKM;
    private JLabel lblMucGiam;
    private JLabel lblngayApDung;
    private JLabel lblNgayKetThuc;

    private JTextField txtMaKM;
    private JTextField txtMucGiam;
    private JDateChooser calNgayApDung;
    private JDateChooser calNgayKetThuc;

    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnThem;
    private JButton btnXoaTrang;
    private JButton btnLamMoi;
    private JButton btnKhuyenMaiDaXoa;
    
    private DefaultTableModel dtm;
    private JTable tblKhuyenMai;
    
    private ArrayList<KhuyenMai> dsKhuyenMai;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    Date today;
    
    public CapNhatKhuyenMaiPanel() {
        FlatLightLaf.setup();
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
        lblTieuDe = new JLabel("QUẢN LÝ KHUYẾN MÃI", SwingConstants.CENTER);
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
        pnlForm.add(lblMaKM, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc. weightx = 0.4;
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
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(calNgayKetThuc, gbc);

        return pnlForm;
    }

    private void initButtons() {
        btnThem = createStyledButton("Thêm", BTN_ADD_COLOR);
        btnSua = createStyledButton("Sửa", BTN_EDIT_COLOR);
        btnXoa = createStyledButton("Xóa", BTN_DELETE_COLOR);
        btnXoaTrang = createStyledButton("Xóa trắng", BTN_CLEAR_COLOR);
        btnLamMoi = createStyledButton("Làm mới", BTN_REFRESH_COLOR);
        btnKhuyenMaiDaXoa = createStyledButton("Khuyến mãi đã xóa", new Color(153,102,204));
        btnKhuyenMaiDaXoa.setPreferredSize(new Dimension(180, 45));

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnKhuyenMaiDaXoa.addActionListener(this);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color. WHITE);
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
        pnlButtons.add(btnKhuyenMaiDaXoa);
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
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhuyenMai.setSelectionBackground(new Color(178, 223, 219));
        tblKhuyenMai.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = tblKhuyenMai.getTableHeader();
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
        for (int i = 0; i < tblKhuyenMai.getColumnCount(); i++) {
            tblKhuyenMai.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        tblKhuyenMai.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadKhuyenMaiData() {
        KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
        dsKhuyenMai = kmDAO.getAllKhuyenMai();
        dtm.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for(KhuyenMai km :  dsKhuyenMai) {
            Object[] rowData = {
                    km.getMaKM(),
                    km.getMucGiamGia(),
                    sdf.format(km.getNgayApDung()),
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
    
    public void quayLaiDanhSach() {
        cardLayout.show(mainContainer, "DanhSach");
        loadKhuyenMaiData();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnXoa) {
            int selectedRow = tblKhuyenMai.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa!");
                return;
            }
            String ma = tblKhuyenMai. getValueAt(selectedRow, 0).toString();

            int option = JOptionPane. showConfirmDialog(this, 
                    "Có chắc muốn xóa khuyến mãi " + ma + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
                KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
                boolean result = kmDAO.deleteKhuyenMai(ma);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
                    loadKhuyenMaiData();
                    xoaTrang();
                } else {
                    JOptionPane. showMessageDialog(this, "Xóa khuyến mãi không thành công!");
                }
            }
        }
        else if(o == btnThem) {
            if(validateInput(true)) {
                String maNV = txtMaKM. getText();
                float mucGiam = Float.parseFloat(txtMucGiam.getText());
                Date apDung = calNgayApDung.getDate();
                Date ketThuc = calNgayKetThuc.getDate();
                
                KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
                KhuyenMai kmNew = new KhuyenMai(maNV, mucGiam, apDung, ketThuc, true);
                boolean result = kmDAO.addKhuyenMai(kmNew);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
                    loadKhuyenMaiData();
                    xoaTrang();
                } else {
                    JOptionPane. showMessageDialog(this, "Thêm khuyến mãi không thành công!");
                }
            }
        }
        else if(o == btnSua) {
            if(validateInput(false)) {
                String maNV = txtMaKM.getText();
                float mucGiam = Float.parseFloat(txtMucGiam.getText());
                Date apDung = calNgayApDung.getDate();
                Date ketThuc = calNgayKetThuc.getDate();
                
                KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
                KhuyenMai kmNew = new KhuyenMai(maNV, mucGiam, apDung, ketThuc, true);
                boolean result = kmDAO.updateKhuyenMai(kmNew);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!");
                    loadKhuyenMaiData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi không thành công!");
                }
            }
        }
        else if(o == btnXoaTrang) {
            xoaTrang();
        }
        else if(o == btnLamMoi) {
        	xoaTrang();
        	loadKhuyenMaiData();
        }
        else if(o == btnKhuyenMaiDaXoa) {
            System.out.println("Chuyển sang panel khuyến mãi đã xóa");
            
            JPanel pnlKhuyenMaiDaXoa = new CapNhatKhuyenMaiSubPanel(this);
            
            try {
                mainContainer.remove(mainContainer.getComponent(1));
            } catch (Exception ex) {
                // Không có panel chi tiết cũ
            }
            
            mainContainer.add(pnlKhuyenMaiDaXoa, "ChiTiet");
            cardLayout.show(mainContainer, "ChiTiet");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblKhuyenMai) {
            int row = tblKhuyenMai.getSelectedRow();
            if (row >= 0) {
                txtMaKM.setText(tblKhuyenMai. getValueAt(row, 0).toString());
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
                txtMaKM.setEnabled(false);
            }
        }
    }
    
    private boolean validateInput(boolean isAddingNew) {
        KhuyenMaiDAO dao = new KhuyenMaiDAO();
        dsKhuyenMai = dao.getAllKhuyenMai();

        if (txtMaKM.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khuyến mãi không được để trống!");
            txtMaKM.requestFocus();
            return false;
        }
        if (txtMucGiam.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mức giảm không được để trống!");
            txtMucGiam.requestFocus();
            return false;
        }

        String maKM = txtMaKM.getText().trim();
        if(isAddingNew) {
            for(KhuyenMai item : dsKhuyenMai) {
                if(item. getMaKM().equalsIgnoreCase(maKM) && item.isVisible()) {
                    JOptionPane.showMessageDialog(this, "Mã khuyến mãi không được trùng!");
                    txtMaKM.requestFocus();
                    return false;
                }
            }
        }
        if (! maKM.matches("KM\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Mã khuyến mãi phải có định dạng KM kèm 3 ký số (Ví dụ: KM001)!");
            txtMaKM.requestFocus();
            return false;
        }
        
        try {
            float mucGiam = Float.parseFloat(txtMucGiam.getText().trim());
            if (mucGiam < 0 || mucGiam > 1) {
                JOptionPane.showMessageDialog(this, "Mức giảm phải là số thực không âm và thuộc [0,1]!");
                txtMucGiam.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Mức giảm phải là số thực!");
            txtMucGiam.requestFocus();
            return false;
        }
        
        Date apDung = calNgayApDung.getDate();
        Date ketThuc = calNgayKetThuc.getDate();
        
        if(apDung == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày áp dụng!");
            calNgayApDung.requestFocus();
            return false;
        }
        if(ketThuc == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày kết thúc!");
            calNgayKetThuc.requestFocus();
            return false;
        }
        
        if (apDung.before(today)) {
            JOptionPane.showMessageDialog(this, "Ngày áp dụng phải là hôm nay hoặc sau hôm nay!");
            calNgayApDung.requestFocus();
            return false;
        }
        if (ketThuc.before(today)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải là hôm nay hoặc sau hôm nay!");
            calNgayKetThuc.requestFocus();
            return false;
        }
        if (ketThuc.before(apDung)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải bằng hoặc sau ngày áp dụng!");
            calNgayKetThuc.requestFocus();
            return false;
        }
        return true;
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