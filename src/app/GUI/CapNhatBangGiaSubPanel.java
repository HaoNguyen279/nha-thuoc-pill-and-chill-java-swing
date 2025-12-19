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
import com.toedter.calendar.JDateChooser;

import app.ConnectDB.ConnectDB;
import app.DAO.BangGiaDAO;
import app.Entity.BangGia;

public class CapNhatBangGiaSubPanel extends JPanel implements ActionListener, MouseListener{

	
	private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(51, 51, 51);
    
    private final Color BTN_ADD_COLOR = new Color(46, 204, 113);
    private final Color BTN_EDIT_COLOR = new Color(241, 196, 15);
    private final Color BTN_DELETE_COLOR = new Color(231, 76, 60);
    private final Color BTN_CLEAR_COLOR = new Color(149, 165, 166);

    private JLabel lblTieuDe;
    private JLabel lblngayApDung;
    private JLabel lblNgayKetThuc;
	private JLabel lblMaBangGia;
	private JLabel lblTenBangGia;
    private JLabel lblGhiChu;
	private JLabel lblDoUuTien;
	private JLabel lblTrangThai;
	private JLabel lblLoaiGia;
    private JDateChooser calNgayApDung;
    private JDateChooser calNgayKetThuc;
    
    private ArrayList<BangGia> dsBangGia;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    Date today;

	private JTextField txtMaBangGia;
	private JTextField txtTenBangGia;
	private JTextField txtTrangThai;
	private JTextField txtLoaiBangGia;
	private JTextField txtGhiChu;
	private JTextField txtDoUuTien;

	private DefaultTableModel dtm;
	private JTable tblBangGia;
	
	private JButton btnKhoiPhuc;
	private JButton btnQuayLai;
	private JButton btnXemChiTiet;
	
	private CapNhatBangGiaPanel pnlCapNhatBangGia;
    
    public CapNhatBangGiaSubPanel(CapNhatBangGiaPanel pnlCapNhatBangGia) {
    	this.pnlCapNhatBangGia = pnlCapNhatBangGia;
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
        
        String[] colsBangGia = {"Mã bảng giá", "Tên bảng giá", "Loại bảng giá", "Độ Ưu Tiên", "Ngày áp dụng", "Ngày kết thúc", "Trạng thái", "Ghi chú"};
        dtm = new DefaultTableModel(colsBangGia, 0) {
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
        loadBangGiaData();
    }

    private void initHeader() {
        lblTieuDe = new JLabel("QUẢN LÝ BẢNG GIÁ ĐÃ XÓA", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTieuDe.setForeground(PRIMARY_COLOR);
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void initInputForm() {
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        lblMaBangGia = new JLabel("Mã bảng giá:");
        lblMaBangGia.setFont(fontLabel);
        lblTenBangGia = new JLabel("Tên bảng giá:");
        lblTenBangGia.setFont(fontLabel);
        lblGhiChu = new JLabel("Ghi chú:");
        lblGhiChu.setFont(fontLabel);
        lblDoUuTien = new JLabel("Độ Ưu Tiên");
        lblDoUuTien.setFont(fontLabel);
        lblngayApDung = new JLabel("Ngày bắt đầu:");
        lblngayApDung.setFont(fontLabel);
        lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc. setFont(fontLabel);
        lblLoaiGia = new JLabel("Loại bảng giá:");
        lblLoaiGia.setFont(fontLabel);
        lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(fontLabel);
        
        txtMaBangGia = new JTextField();
        txtMaBangGia.setFont(fontText);
        txtMaBangGia.setPreferredSize(new Dimension(200, 35));
        
        txtTenBangGia = new JTextField();
        txtTenBangGia.setFont(fontText);
        txtTenBangGia.setPreferredSize(new Dimension(200, 35));
        
        txtGhiChu = new JTextField();
        txtGhiChu.setFont(fontText);
        txtGhiChu.setPreferredSize(new Dimension(200, 35));
        
        txtDoUuTien = new JTextField("0");
        txtDoUuTien.setFont(fontText);
        txtDoUuTien.setPreferredSize(new Dimension(200, 35));
        
        txtTrangThai = new JTextField();
        txtTrangThai.setFont(fontText);
        txtTrangThai.setPreferredSize(new Dimension(200, 35));
        
        txtLoaiBangGia = new JTextField();
        txtLoaiBangGia.setFont(fontText);
        txtLoaiBangGia.setPreferredSize(new Dimension(200, 35));
        
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
        pnlForm.add(lblMaBangGia, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc. weightx = 0.4;
        pnlForm. add(txtMaBangGia, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlForm.add(lblTenBangGia, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        pnlForm.add(txtTenBangGia, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblngayApDung, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc. weightx = 0.4;
        pnlForm.add(calNgayApDung, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlForm.add(lblNgayKetThuc, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        pnlForm.add(calNgayKetThuc, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        pnlForm.add(lblDoUuTien, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc. weightx = 0.4;
        pnlForm. add(txtDoUuTien, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.1;
        pnlForm.add(lblGhiChu, gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.4;
        pnlForm.add(txtGhiChu, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.1;
        pnlForm.add(lblLoaiGia, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.4;
        pnlForm.add(txtLoaiBangGia, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.1;
        pnlForm.add(lblTrangThai, gbc);
        gbc.gridx = 3; gbc.gridy = 3; gbc.weightx = 0.4;
        pnlForm.add(txtTrangThai,gbc);
        
        return pnlForm;
    }

    private void initButtons() {
    	btnKhoiPhuc = createStyledButton("Khôi phục", BTN_ADD_COLOR);
        btnQuayLai = createStyledButton("Quay lại", BTN_CLEAR_COLOR);
        btnXemChiTiet = createStyledButton("Xem chi tiết", BTN_ADD_COLOR);
        
        btnKhoiPhuc.addActionListener(this);
        btnQuayLai.addActionListener(this);
        btnXemChiTiet.addActionListener(this);

        
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
        pnlButtons.add(btnKhoiPhuc);
        pnlButtons.add(btnQuayLai);
        pnlButtons.add(btnXemChiTiet);
        return pnlButtons;
    }
    
    public JScrollPane createBotPanel() {
        tblBangGia = new JTable(dtm) {
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
        
        JTableHeader header = tblBangGia.getTableHeader();
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
        for (int i = 0; i < tblBangGia.getColumnCount(); i++) {
        	tblBangGia.getColumnModel().getColumn(i).setCellRenderer(cellCenter);
        }

        tblBangGia.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(tblBangGia);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    public void loadBangGiaData() {
        BangGiaDAO bgDAO = new BangGiaDAO();
        dsBangGia = bgDAO.getAllBangGiaInactive();
        dtm.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for(BangGia bg :  dsBangGia) {
            Object[] rowData = {
                    bg.getMaBangGia(),
                    bg.getTenBangGia(),
                    bg.getLoaiGia(),
                    bg.getDoUuTien(),
                    sdf.format(bg.getNgayApDung()),
                    sdf.format(bg.getNgayKetThuc()),
                    bg.getTrangThai(),
                    bg.getGhiChu()
            };
            dtm.addRow(rowData);
        }
    }
    
    public void xoaTrang() {
        txtMaBangGia.setText("");
        txtTenBangGia.setText("");
        calNgayApDung.setDate(today);
        calNgayKetThuc.setDate(today);
        txtDoUuTien.setText("0");
        txtGhiChu.setText("");
        txtLoaiBangGia.setText("");
        txtTrangThai.setText("");
        txtMaBangGia.setEnabled(true);
        tblBangGia. clearSelection();
        loadBangGiaData();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        if(o == btnKhoiPhuc) {
            String maBangGia = txtMaBangGia.getText().trim();
            
            if(maBangGia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bảng giá cần khôi phục!");
                return;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Có chắc muốn khôi phục khuyến mãi " + maBangGia + "?", 
                    "Xác nhận", 
                    JOptionPane.YES_NO_OPTION);
            
            if(option == JOptionPane.YES_OPTION) {
            	BangGiaDAO bgDAO = new BangGiaDAO();
                boolean result = bgDAO.reactiveBangGia(maBangGia);
                if(result) {
                    JOptionPane.showMessageDialog(this, "Khôi phục bảng giá thành công!");
                    loadBangGiaData();
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Khôi phục bảng giá không thành công!");
                }
            }
        }
        else if(o == btnQuayLai) {
            pnlCapNhatBangGia.quayLaiDanhSach();
        }
        else if(o == btnXemChiTiet) {
        	System.out.println("Chuyển sang panel xem chi tiết bảng giá");
        	
        	try {
                mainContainer.remove(mainContainer.getComponent(1));
            } catch (Exception ex) {
                // Không có panel chi tiết cũ
            }
            
            //mainContainer.add(pnlKhuyenMaiDaXoa, "ChiTiet");
            cardLayout.show(mainContainer, "ChiTiet");
        	
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblBangGia) {
            int row = tblBangGia.getSelectedRow();
            if (row >= 0) {
                txtMaBangGia.setText(tblBangGia. getValueAt(row, 0).toString());
                txtTenBangGia.setText(tblBangGia.getValueAt(row, 1).toString());
                txtLoaiBangGia.setText(tblBangGia. getValueAt(row, 2).toString());
                txtDoUuTien.setText(tblBangGia. getValueAt(row, 3).toString());
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dateApDungString = tblBangGia.getValueAt(row, 4).toString();
                    String dateKetThucString = tblBangGia. getValueAt(row, 5).toString();
                    calNgayApDung.setDate(sdf.parse(dateApDungString));
                    calNgayKetThuc.setDate(sdf.parse(dateKetThucString));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                
                txtTrangThai.setText(tblBangGia. getValueAt(row, 6).toString());
                txtGhiChu.setText(tblBangGia. getValueAt(row, 7).toString());
                
                txtMaBangGia.setEnabled(false);
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
