package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.NhaSanXuatDAO;
import app.DAO.ThuocDAO;
import app.Entity.NhaSanXuat;
import app.Entity.Thuoc;

public class CapNhatThuocPanel extends JPanel implements ActionListener, MouseListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaThuoc;
	private JLabel lblTenThuoc;
	private JLabel lblSoLuongTon;
	private JLabel lblGiaBan;
	private JLabel lblDonVi;
	private JLabel lblNhaSanXuat;

	private JTextField txtMaThuoc;
	private JTextField txtTenThuoc;
	private JTextField txtSoLuongTon;
	private JTextField txtGiaBan;
	private JComboBox<String> cboDonVi;
//	private JTextField txtSoLuongToiThieu;
	private JComboBox<String> cboNhaSanXuat;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoaTrang;
	
	private DefaultTableModel dtm;
	private JTable tblThuoc;
	
	private ArrayList<Thuoc> dsThuoc;
	private ArrayList<NhaSanXuat> dsNhaSanXuat;
	private NhaSanXuatDAO nsxDAO;
	private Map<String, String> mapNhaSanXuat;
	
	public CapNhatThuocPanel() {
		ConnectDB.getInstance().connect();
		nsxDAO = new NhaSanXuatDAO();
		mapNhaSanXuat = new HashMap<>();
        lblTieuDe = new JLabel("Cập nhật thuốc", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        lblMaThuoc = new JLabel("Mã thuốc:");
        lblTenThuoc = new JLabel("Tên thuốc:");
        lblSoLuongTon = new JLabel("Số lượng:");
        lblGiaBan = new JLabel("Giá bán:");
    	lblDonVi = new JLabel("Đơn vị:");
    	lblNhaSanXuat = new JLabel("Nhà sản xuất:");
    	
        txtMaThuoc = new JTextField(15);
        txtTenThuoc = new JTextField(15);
        txtSoLuongTon = new JTextField(15);
        txtGiaBan = new JTextField(15);
        
    	cboDonVi = new JComboBox<>();
    	cboNhaSanXuat = new JComboBox<>();
    	
    	// Thêm các đơn vị phổ biến
    	cboDonVi.addItem("Viên");
    	cboDonVi.addItem("Hộp");
    	cboDonVi.addItem("Vỉ");
    	cboDonVi.addItem("Chai");
    	cboDonVi.addItem("Ống");
    	cboDonVi.addItem("Tuýp");
    	cboDonVi.addItem("Gói");
    	
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        btnXoaTrang = new JButton("Xóa trắng");
        
        // Table init
        String[] cols = {"Mã thuốc" , "Tên thuốc", "Số lượng", "Giá bán","Đơn vị","Nhà SX"};
        dtm = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false, ngăn edit trên cell
               return false;
            }
        };
        
        // Frame
        setLayout(new BorderLayout());
        add(createTopPanel(),BorderLayout.NORTH);
        add(createBotPanel(), BorderLayout.CENTER);
        
        loadThuocData();
        loadNhaSanXuatData();
        setBackground(new Color(248, 248, 248));

        
	}
	
	public JScrollPane createBotPanel() {
        tblThuoc = new JTable(dtm);
        tblThuoc.setBackground(new Color(240, 240, 245));
        tblThuoc.setGridColor(Color.LIGHT_GRAY);
        tblThuoc.setFont(new Font("Arial", Font.PLAIN, 15));
        tblThuoc.setRowHeight(40);
        tblThuoc.setSelectionBackground(new Color(190, 226, 252));
        tblThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblThuoc.getColumnModel().getColumn(5).setPreferredWidth(170);
        tblThuoc.addMouseListener(this);
        JTableHeader header = tblThuoc.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(0, 117, 196));
        header.setForeground(Color.white);
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblThuoc);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(00,50,50,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        

        return scrollPane;
	}
	
	
	public JPanel createTopPanel() {
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlTopOfMain = new JPanel();
		JPanel pnlCenterOfMain = new JPanel(new GridLayout(3,2,10,10));
		JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		pnlMain.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
		pnlTopOfMain.add(lblTieuDe);
		
		JPanel pnlr1 = new JPanel(new BorderLayout());
		pnlr1.add(lblMaThuoc, BorderLayout.WEST);
		pnlr1.add(txtMaThuoc);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		
		JPanel pnlr3 = new JPanel(new BorderLayout());
		pnlr3.add(lblTenThuoc, BorderLayout.WEST);
		pnlr3.add(txtTenThuoc);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblSoLuongTon, BorderLayout.WEST);
		pnlr4.add(txtSoLuongTon);
		pnlr4.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr5 = new JPanel(new BorderLayout());
		pnlr5.add(lblGiaBan, BorderLayout.WEST);
		pnlr5.add(txtGiaBan);
		pnlr5.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr6 = new JPanel(new BorderLayout());
		pnlr6.add(lblDonVi, BorderLayout.WEST);
		pnlr6.add(cboDonVi);
		pnlr6.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr8 = new JPanel(new BorderLayout());
		pnlr8.add(lblNhaSanXuat, BorderLayout.WEST);
		pnlr8.add(cboNhaSanXuat);
		pnlr8.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain.add(pnlr4);
		pnlCenterOfMain.add(pnlr5);
		pnlCenterOfMain.add(pnlr6);
		pnlCenterOfMain.add(pnlr8);
		
		btnThem.setBackground(new Color(46, 204, 113));
		btnXoa.setBackground(new Color(231, 76, 60));
		btnSua.setBackground(new Color(52, 152, 219));
		btnXoaTrang.setBackground(Color.WHITE);
		
		// JButton, JLabel, JTextField customization
		JButton[] btnList = {btnXoa, btnThem, btnSua,btnXoaTrang};
		for(JButton item : btnList) {
			item.setFont(new Font("Arial", Font.BOLD, 16));
			item.setForeground(Color.WHITE);
			item.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(15,45,15,45)
			));
			item.setFocusPainted(false);
			item.setCursor(new Cursor(Cursor.HAND_CURSOR));
			item.addActionListener(this);
			
		}
		btnXoaTrang.setForeground(Color.BLACK);
		JLabel[] lblItems = { 
			    lblMaThuoc, lblTenThuoc, lblSoLuongTon,
			    lblGiaBan, lblDonVi, lblNhaSanXuat
			};
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems = {
        	    txtMaThuoc, txtTenThuoc, txtSoLuongTon,
        	    txtGiaBan
        	};
        for(JTextField item : txtItems) {
        	item.setFont(new Font("Arial", Font.ITALIC, 16));
        	item.setForeground(Color.BLUE);
        	item.setBackground(new Color(245, 245, 245));
        	item.setPreferredSize(new Dimension(200,20));
        	item.setBorder(BorderFactory.createCompoundBorder(
            	BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        		BorderFactory.createEmptyBorder(5,10,5,10)
        	));
        }
        cboNhaSanXuat.setFont(new Font("Arial", Font.ITALIC, 16));
        cboNhaSanXuat.setForeground(Color.BLUE);
        cboNhaSanXuat.setBackground(new Color(245, 245, 245));
        cboNhaSanXuat.setPreferredSize(new Dimension(200,20));
        
        cboDonVi.setFont(new Font("Arial", Font.ITALIC, 16));
        cboDonVi.setForeground(Color.BLUE);
        cboDonVi.setBackground(new Color(245, 245, 245));
        cboDonVi.setPreferredSize(new Dimension(200,20));
        cboDonVi.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
		txtMaThuoc.setDisabledTextColor(Color.GRAY);
		
		pnlBottomOfMain.add(btnXoaTrang);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnXoa);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnSua);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnThem);
		pnlBottomOfMain.add(Box.createHorizontalStrut(50));
		
		pnlMain.add(pnlTopOfMain,BorderLayout.NORTH);
		pnlCenterOfMain.setPreferredSize(new Dimension(0,120)); //important
		pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
		pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		pnlMain.add(pnlBottomOfMain, BorderLayout.SOUTH);
		
		return pnlMain;
	}
	
	public void loadThuocData() {
        ThuocDAO thuocDAO = new ThuocDAO();
        dsThuoc = thuocDAO.getAllThuocCoTenNSX();
		dtm.setRowCount(0);
		for(Thuoc thuoc : dsThuoc) {
			Object[] rowData = {
					thuoc.getMaThuoc(),
					thuoc.getTenThuoc(),
					thuoc.getSoLuongTon(),
					thuoc.getGiaBan(),
					thuoc.getDonVi(),
					thuoc.getMaNSX()
			};
		dtm.addRow(rowData);
		}
	}
	public void loadNhaSanXuatData() {
		dsNhaSanXuat = nsxDAO.getAllNhaSanXuat();
		for(NhaSanXuat item : dsNhaSanXuat) {
			cboNhaSanXuat.addItem(item.getTenNSX());
			mapNhaSanXuat.put(item.getMaNSX(), item.getTenNSX());
		}
	}
	public void xoaTrang() {
	    txtMaThuoc.setText("");
	    txtTenThuoc.setText("");
	    txtGiaBan.setText("");
	    txtSoLuongTon.setText("");
	    cboDonVi.setSelectedIndex(0);
	    cboNhaSanXuat.setSelectedIndex(0);
	    txtMaThuoc.setEnabled(true);
	    tblThuoc.clearSelection();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnXoa) {
			int selectedRow = tblThuoc.getSelectedRow();
			if(selectedRow == -1) {
				CustomJOptionPane a = new CustomJOptionPane(this, "Vui lòng chọn thuốc cần xóa!", false);
				a.show();
				return;
			}
			String maThuoc = tblThuoc.getValueAt(selectedRow, 0).toString();
			String maLo = tblThuoc.getValueAt(selectedRow, 1).toString();
			
			CustomJOptionPane a = new CustomJOptionPane(this, "Có chắc muốn xóa thuốc " + maThuoc + " - Lô " + maLo + "?", true);
			int option = a.show();
			if(option == JOptionPane.YES_OPTION) {
				ThuocDAO thuocDAO = new ThuocDAO();
				boolean result = thuocDAO.deleteThuoc(maThuoc);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa thuốc thành công!", false);
					a1.show();
					loadThuocData();
					xoaTrang();
				} else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa thuốc không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnThem) {
			if(validateInput(true)) {
				String maThuoc = txtMaThuoc.getText().trim();
				String tenThuoc = txtTenThuoc.getText().trim();
				int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
				double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
				String donVi = cboDonVi.getSelectedItem().toString();
				int soLuongToiThieu = 0;
				
				// Lấy mã NSX từ tên NSX được chọn
				String tenNSX = cboNhaSanXuat.getSelectedItem().toString();
				String maNSX = "";
				for (Map.Entry<String, String> item : mapNhaSanXuat.entrySet()) {
					if(tenNSX.equalsIgnoreCase(item.getValue())) {
						maNSX = item.getKey();
						break;
					}
				}
				
				ThuocDAO thuocDAO = new ThuocDAO();
				Thuoc thuocNew = new Thuoc(maThuoc, tenThuoc, soLuongTon, giaBan, 
										   donVi, soLuongToiThieu, maNSX, true);
				boolean result = thuocDAO.addThuoc(thuocNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm thuốc thành công!", false);
					a1.show();
					loadThuocData();
					xoaTrang();
				} else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm thuốc không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnSua) {
			if(validateInput(false)) {
				String maThuoc = txtMaThuoc.getText().trim();
				String tenThuoc = txtTenThuoc.getText().trim();
				int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
				double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
				String donVi = cboDonVi.getSelectedItem().toString();
				int soLuongToiThieu = 0;
				
				// Lấy mã NSX từ tên NSX được chọn
				String tenNSX = cboNhaSanXuat.getSelectedItem().toString();
				String maNSX = "";
				for (Map.Entry<String, String> item : mapNhaSanXuat.entrySet()) {
					if(tenNSX.equals(item.getValue())) {
						maNSX = item.getKey();
						break;
					}
				}
				
				ThuocDAO thuocDAO = new ThuocDAO();
				Thuoc thuocUpdate = new Thuoc(maThuoc, tenThuoc, soLuongTon, giaBan, 
											  donVi, soLuongToiThieu, maNSX, true);
				boolean result = thuocDAO.updateThuoc(thuocUpdate);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật thuốc thành công!", false);
					a1.show();
					loadThuocData();
					xoaTrang();
				} else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật thuốc không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnXoaTrang) {
			xoaTrang();
		}
	}
	
	private boolean validateInput(boolean isAddingNew) {
		// Kiểm tra các trường không được rỗng
		if (txtMaThuoc.getText().trim().isEmpty()) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Mã thuốc không được để trống!", false);
			a.show();
			txtMaThuoc.requestFocus();
			return false;
		}
		if (txtTenThuoc.getText().trim().isEmpty()) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Tên thuốc không được để trống!", false);
			a.show();
			txtTenThuoc.requestFocus();
			return false;
		}
		if (txtSoLuongTon.getText().trim().isEmpty()) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Số lượng không được để trống!", false);
			a.show();
			txtGiaBan.requestFocus();
			return false;
		}
		if (txtGiaBan.getText().trim().isEmpty()) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Giá bán không được để trống!", false);
			a.show();
			txtGiaBan.requestFocus();
			return false;
		}
		
		// Kiểm tra định dạng mã thuốc
		String maThuoc = txtMaThuoc.getText().trim();
		if(isAddingNew) {
			for(Thuoc item : dsThuoc) {
				if(item.getMaThuoc().equalsIgnoreCase(maThuoc)) {
					CustomJOptionPane a = new CustomJOptionPane(this, "Mã thuốc không được trùng!", false);
					a.show();
					txtMaThuoc.requestFocus();
					return false;
				}
			}
		}
		if (!maThuoc.matches("T\\d{3}")) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Mã thuốc phải có định dạng T kèm 3 ký số (Ví dụ: T001)!", false);
			a.show();
			txtMaThuoc.requestFocus();
			return false;
		}
	
		
		// Kiểm tra giá bán
		try {
			double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
			if(giaBan <= 0) {
				CustomJOptionPane a = new CustomJOptionPane(this, "Giá bán phải lớn hơn 0!", false);
				a.show();
				txtGiaBan.requestFocus();
				return false;
			}
		} catch (NumberFormatException ex) {
			CustomJOptionPane a = new CustomJOptionPane(this, "Giá bán phải là số!", false);
			a.show();
			txtGiaBan.requestFocus();
			return false;
		}
		
		// Kiểm tra số lượng tồn (chỉ khi sửa)
		if(!txtSoLuongTon.getText().trim().isEmpty()) {
			try {
				int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());
				if(soLuongTon < 0) {
					CustomJOptionPane a = new CustomJOptionPane(this, "Số lượng tồn không được âm!", false);
					a.show();
					txtSoLuongTon.requestFocus();
					return false;
				}
			} catch (NumberFormatException ex) {
				CustomJOptionPane a = new CustomJOptionPane(this, "Số lượng tồn phải là số nguyên!", false);
				a.show();
				txtSoLuongTon.requestFocus();
				return false;
			}
		}
		
		
		return true;
	}
	

	@Override	
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if(o == tblThuoc) {
			txtMaThuoc.setEnabled(false);
			int row = tblThuoc.getSelectedRow();
	        if (row >= 0) {
	            txtMaThuoc.setText(tblThuoc.getValueAt(row, 0).toString());
	            txtTenThuoc.setText(tblThuoc.getValueAt(row, 1).toString());
	            txtSoLuongTon.setText(tblThuoc.getValueAt(row, 2).toString());
	            txtGiaBan.setText(tblThuoc.getValueAt(row, 3).toString());
	            cboDonVi.setSelectedItem(tblThuoc.getValueAt(row, 4).toString());
	            cboNhaSanXuat.setSelectedItem(tblThuoc.getValueAt(row, 5).toString());
	        }
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
