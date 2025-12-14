package app.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.log.SysoCounter;

import com.formdev.flatlaf.FlatLightLaf;


import app.ConnectDB. ConnectDB;
import app. DAO.NhaSanXuatDAO;
import app.DAO.ThuocDAO;
import app. Entity.NhaSanXuat;
import app.Entity.Thuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java. awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java. util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimKiemThuocPanel extends JPanel implements ActionListener,MouseListener{ 
	
    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
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

	private JButton btnXoaTrang;
	private JButton btnTimKiem;
	
	private DefaultTableModel dtm;
	private JTable tblThuoc;
	
    private DefaultTableModel dtmTable;
    private ArrayList<Thuoc> dsThuoc;
    private JButton btnTim;
    private JTextField txtTim;
    private String tieuChi = "Mã thuốc";
    private JComboBox<String> cboTieuChi;
    private JTable table;
    private JLabel lblTongSoBanGhi = new JLabel("");
    private ThuocDAO thuocdao = new ThuocDAO();
	private ArrayList<NhaSanXuat> dsNhaSanXuat;
	private NhaSanXuatDAO nsxDAO = new NhaSanXuatDAO();
	private HashMap mapNhaSanXuat;
	
    public TimKiemThuocPanel() {
    	
    	FlatLightLaf.setup();
    	
    	FlatLightLaf.setup();
        setLayout(new BorderLayout());
        
        
     
        
        
        
        ConnectDB. getInstance().connect();
        
        nsxDAO = new NhaSanXuatDAO();
        mapNhaSanXuat = new HashMap<>();
        
        dsThuoc = thuocdao.getAllThuocCoTenNSX();
        JPanel topPanel = createTopPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = taoCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(topPanel,BorderLayout.NORTH);
        
        add(topPanel,BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("ENTER"), "clickLogin");
        getActionMap().put("clickLogin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTim. doClick();
            }
        });
        loadNhaSanXuatData();
        loadNhaSanXuatData();
        setVisible(true);
    }
    
	public JPanel createTopPanel() {
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlTopOfMain = new JPanel();
		JPanel pnlCenterOfMain = new JPanel(new GridLayout(3,2,10,5));
		JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		lblTieuDe = new JLabel("TÌM KIẾM THUỐC", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		pnlMain.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
		pnlTopOfMain.add(lblTieuDe);
        
        lblMaThuoc = new JLabel("Mã thuốc:");
        lblMaThuoc. setOpaque(true);
        lblTenThuoc = new JLabel("Tên thuốc:");
        lblSoLuongTon = new JLabel("Số lượng:");
       
    	lblDonVi = new JLabel("Đơn vị:");
    	lblNhaSanXuat = new JLabel("Nhà sản xuất:");
    	
        txtMaThuoc = new JTextField(15);
        txtTenThuoc = new JTextField(15);
        txtSoLuongTon = new JTextField(15);
        
        
        cboDonVi = new JComboBox<>();
    	cboNhaSanXuat = new JComboBox<>();
    	
    	// Thêm các đơn vị phổ biến
    	cboDonVi.addItem("");
    	cboDonVi.addItem("Viên");
    	cboDonVi.addItem("Hộp");
    	cboDonVi.addItem("Vỉ");
    	cboDonVi.addItem("Chai");
    	cboDonVi.addItem("Ống");
    	cboDonVi.addItem("Tuýp");
    	cboDonVi.addItem("Gói");
    	
        btnXoaTrang = new JButton("Xóa trắng");
        btnTimKiem = new JButton("Tìm kiếm");
		
		pnlMain.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
		
		JPanel pnlr1 = new JPanel(new BorderLayout());
		pnlr1.add(lblMaThuoc, BorderLayout. WEST);
		pnlr1.add(txtMaThuoc);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr3 = new JPanel(new BorderLayout());
		pnlr3.add(lblTenThuoc, BorderLayout.WEST);
		pnlr3.add(txtTenThuoc);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblSoLuongTon, BorderLayout.WEST);
		pnlr4.add(txtSoLuongTon);
		pnlr4.setBorder(BorderFactory. createEmptyBorder(0,10,0,10));
		
		

		JPanel pnlr6 = new JPanel(new BorderLayout());
		pnlr6.add(lblDonVi, BorderLayout. WEST);
		pnlr6.add(cboDonVi);
		pnlr6.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr8 = new JPanel(new BorderLayout());
		pnlr8.add(lblNhaSanXuat, BorderLayout.WEST);
		pnlr8.add(cboNhaSanXuat);
		pnlr8.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain. add(pnlr4);

		pnlCenterOfMain.add(pnlr6);
		pnlCenterOfMain.add(pnlr8);
		
		// JButton, JLabel, JTextField customization
		JButton[] btnList = {btnXoaTrang,btnTimKiem};
		for(JButton item : btnList) {
			item.setFont(new Font("Segoe UI", Font.BOLD, 16));
			item.setForeground(Color.WHITE);
			item.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color. LIGHT_GRAY),
				BorderFactory.createEmptyBorder(15,45,15,45)
			));
			item.setFocusPainted(false);
			item.setCursor(new Cursor(Cursor.HAND_CURSOR));
			item.addActionListener(this);
		}
		btnXoaTrang.setForeground(Color.BLACK);
		btnTimKiem.setForeground(Color.black);
		
		JLabel[] lblItems = { 
			    lblMaThuoc, lblTenThuoc, lblSoLuongTon,
			    lblDonVi, lblNhaSanXuat
			};
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Segoe UI", Font. PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        
        JTextField[] txtItems = {
        	    txtMaThuoc, txtTenThuoc, txtSoLuongTon,
        	    
        	};
        for(JTextField item : txtItems) {
        	item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        	item.setBackground(new Color(245, 245, 245));
        	item.setPreferredSize(new Dimension(200,30));
        	item.setBorder(BorderFactory.createCompoundBorder(
            	BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        		BorderFactory.createEmptyBorder(3,10,3,10)
        	));
        }
        
        cboNhaSanXuat.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        cboNhaSanXuat.setForeground(Color.BLUE);
        cboNhaSanXuat.setBackground(new Color(245, 245, 245));
        cboNhaSanXuat.setPreferredSize(new Dimension(200,30));
        cboNhaSanXuat.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        cboDonVi.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        cboDonVi. setForeground(Color. BLUE);
        cboDonVi.setBackground(new Color(245, 245, 245));
        cboDonVi. setPreferredSize(new Dimension(200,30));
        cboDonVi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
		txtMaThuoc.setDisabledTextColor(Color. GRAY);
		txtSoLuongTon.setDisabledTextColor(Color.GRAY);
		
		pnlBottomOfMain.add(btnXoaTrang);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnTimKiem);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		
		pnlMain.add(pnlTopOfMain,BorderLayout.NORTH);
		pnlCenterOfMain.setPreferredSize(new Dimension(0,110));
		pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
		pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		pnlMain.add(pnlBottomOfMain, BorderLayout. SOUTH);
		
		return pnlMain;
	}
    
	public void loadNhaSanXuatData() {
	    dsNhaSanXuat = nsxDAO.getAllNhaSanXuat();
	    cboNhaSanXuat.addItem("");
	    for(NhaSanXuat item : dsNhaSanXuat) {
	        cboNhaSanXuat.addItem(item.getTenNSX());
	        mapNhaSanXuat. put(item.getTenNSX(),item. getMaNSX());
	    }
	}

    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Table Panel
        JPanel panel = new JPanel(new BorderLayout());

        String[] cols = {"Mã thuốc", "Tên thuốc", "Số lượng", 
                         "Đơn vị", "NSX"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData_Thuoc(dsThuoc);
        
        table = new JTable(dtmTable) {
        	@Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };
        
        // Style table
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(224, 224, 224));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(178, 223, 219));
        table.setSelectionForeground(Color.BLACK);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.addMouseListener(this);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane. setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsThuoc.size());
        lblTongSoBanGhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTongSoBanGhi.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(lblTongSoBanGhi, BorderLayout. SOUTH);
        return centerPanel;
    }
    
    public void loadData_Thuoc(ArrayList<Thuoc> dsThuoc) {
        dtmTable.setRowCount(0);
        for(Thuoc thuoc : dsThuoc) {
        	System.out.println(thuoc);
        	System.out.println(thuoc);
            Object[] rowData = {
                thuoc.getMaThuoc(),
                thuoc.getTenThuoc(),
                thuoc. getSoLuongTon() == 0 ? "Hết hàng" : thuoc.getSoLuongTon(),
                thuoc.getDonVi(),
                thuoc.getMaNSX()
            };
            dtmTable.addRow(rowData);
        }
        lblTongSoBanGhi. setText("Tổng số bản ghi: " + dsThuoc.size());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnTim) {
            ArrayList<Thuoc> ketQuaTim = new ArrayList<Thuoc>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.equals("nhập từ khóa tìm kiếm... ") || timString.isBlank()) {
                loadData_Thuoc(dsThuoc);
            }
            else {
                if(tieuChi.equals("Mã thuốc")) {
                    for(Thuoc thuoc:  dsThuoc) {
                        if(thuoc.getMaThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim. add(thuoc);
                        }
                    }
                }
                else { // Tên thuốc
                    for(Thuoc thuoc: dsThuoc) {
                        if(thuoc. getTenThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(thuoc);
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả nào! ",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData_Thuoc(dsThuoc);
                    txtTim.setText("Nhập từ khóa tìm kiếm...");
                    txtTim.setForeground(Color.GRAY);
                    txtTim. setFont(new Font("Segoe UI", Font.ITALIC, 14));
                } else {
                    loadData_Thuoc(ketQuaTim);
                }
            }
        }
        else if(o == btnXoaTrang) {
        	txtMaThuoc.setText("");
            txtTenThuoc.setText("");
            txtSoLuongTon.setText("");
            
            cboDonVi.setSelectedIndex(0);
            cboNhaSanXuat.setSelectedIndex(0);
            loadData_Thuoc(dsThuoc);
        }
        else if (o == btnTimKiem) {
            String maThuocTim = txtMaThuoc. getText().trim().toLowerCase();
            String tenThuocTim = txtTenThuoc.getText().trim().toLowerCase();
            String soLuongStr = txtSoLuongTon.getText().trim();
            
            String donViTim = (String) cboDonVi.getSelectedItem();
            String nsxTim = (String) cboNhaSanXuat. getSelectedItem();

            Integer soLuongTim = null;
            Double giaBanTim = null;
        
            if (! soLuongStr.isEmpty()) {
                try {
                    soLuongTim = Integer.parseInt(soLuongStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Số lượng phải là số nguyên! ",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

           

            ArrayList<Thuoc> ketQua = new ArrayList<>();

            for (Thuoc t : dsThuoc) {
                boolean match = true;

                if (! maThuocTim.isEmpty() &&
                        !t.getMaThuoc().toLowerCase().contains(maThuocTim)) {
                    match = false;
                }

                if (match && !tenThuocTim.isEmpty() &&
                        !t.getTenThuoc().toLowerCase().contains(tenThuocTim)) {
                    match = false;
                }

                if (match && soLuongTim != null &&
                        t.getSoLuongTon() < soLuongTim) {
                    match = false;
                }

               

                if (match && donViTim != null && ! donViTim.isEmpty() &&
                        !t. getDonVi().equalsIgnoreCase(donViTim)) {
                    match = false;
                }

                if (match && nsxTim != null && !nsxTim. isEmpty()) {
                    String tenNSXThuoc = t.getMaNSX();
                    if (tenNSXThuoc == null ||
                            !tenNSXThuoc.equalsIgnoreCase(nsxTim)) {
                        match = false;
                    }
                }

                if (match) {
                    ketQua.add(t);
                }
            }

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy kết quả phù hợp!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadData_Thuoc(dsThuoc);
                return;
            }

            loadData_Thuoc(ketQua);
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
            System.out.println(tieuChi);
        }
    }

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}