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
import app.DAO.NhanVienDAO;
import app.Entity.NhanVien;

public class CapNhatNhanVienPanel extends JPanel implements ActionListener, MouseListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaNv;
	private JLabel lblTenNv;
	private JLabel lblSoDienThoai;
	private JLabel lblChucVu;

	private JTextField txtMaNv;
	private JTextField txtTenNv;
	private JTextField txtSoDienThoai;
	private JTextField txtChucVu;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoaTrang;
	
	private JComboBox<String> cboChucVu;
	private DefaultTableModel dtm;
	private JTable tblNhanVien;
	
	private ArrayList<NhanVien> dsNhanVien;

	public CapNhatNhanVienPanel() {
        lblTieuDe = new JLabel("Cập nhật nhân viên", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        lblMaNv = new JLabel("Mã NV:");
        lblTenNv = new JLabel("Tên NV:");
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblChucVu = new JLabel("Chức vụ:");
        
        txtMaNv = new JTextField(15);
        txtTenNv = new JTextField(15);
        txtSoDienThoai = new JTextField(15);
        txtChucVu = new JTextField(15);

        cboChucVu = new JComboBox<String>();
        cboChucVu.addItem("Nhân viên quản lý");
        cboChucVu.addItem("Nhân viên bán hàng");
        
        
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        btnXoaTrang = new JButton("Xóa trắng");
        
        // Table init
        String[] cols = {"Mã nhân viên" , "Tên nhân viên" , "Số điện thoại", "Chức vụ"};
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
        
        loadNhanVienData();
        setBackground(new Color(248, 248, 248));
        setVisible(true);
        
	}
	
	public JScrollPane createBotPanel() {
        tblNhanVien = new JTable(dtm);        
        tblNhanVien.setBackground(new Color(240, 240, 245));
        tblNhanVien.setGridColor(Color.LIGHT_GRAY);
        tblNhanVien.setFont(new Font("Arial", Font.PLAIN, 15));
        tblNhanVien.setRowHeight(40);
        tblNhanVien.setGridColor(Color.LIGHT_GRAY);
        tblNhanVien.setSelectionBackground(new Color(100, 149, 237));
        tblNhanVien.addMouseListener(this);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = tblNhanVien.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(192, 232, 246));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(00,50,50,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        return scrollPane;
	}
	
	
	public JPanel createTopPanel() {
		
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlTopOfMain = new JPanel();
		JPanel pnlCenterOfMain = new JPanel(new GridLayout(2,2,10,10));
		JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		pnlTopOfMain.add(lblTieuDe);
		
		JPanel pnlr1 = new JPanel(new BorderLayout());
		pnlr1.add(lblMaNv, BorderLayout.WEST);
		pnlr1.add(txtMaNv);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr2 = new JPanel(new BorderLayout());
		pnlr2.add(lblTenNv, BorderLayout.WEST);
		pnlr2.add(txtTenNv);
		pnlr2.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr3 = new JPanel(new BorderLayout());
		pnlr3.add(lblSoDienThoai, BorderLayout.WEST);
		pnlr3.add(txtSoDienThoai);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblChucVu, BorderLayout.WEST);
		pnlr4.add(cboChucVu);
		pnlr4.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr2);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain.add(pnlr4);
		
		btnThem.setBackground(new Color(224, 248, 228));
		btnXoa.setBackground(new Color(255, 121, 121));
		btnSua.setBackground(new Color(223, 249, 251));
		btnXoaTrang.setBackground(Color.WHITE);
		
		// JButton, JLabel, JTextField customization
		JButton[] btnList = {btnXoa, btnThem, btnSua,btnXoaTrang};
		for(JButton item : btnList) {
			item.setFont(new Font("Arial", Font.PLAIN, 15));
			item.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(15,45,15,45)
			));
			item.setFocusPainted(false);
			item.setCursor(new Cursor(Cursor.HAND_CURSOR));
			item.addActionListener(this);
		}
        JLabel[] lblItems =  { lblMaNv, lblTenNv,lblSoDienThoai, lblChucVu };
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems =  { txtMaNv, txtTenNv,txtSoDienThoai, txtChucVu };
        for(JTextField item : txtItems) {
        	item.setFont(new Font("Arial", Font.ITALIC, 16));
        	item.setForeground(Color.BLUE); // Màu font 
        	item.setBackground(new Color(245, 245, 245));
        	item.setPreferredSize(new Dimension(200,20));
        	item.setBorder(BorderFactory.createCompoundBorder(
            	BorderFactory.createLineBorder(Color.LIGHT_GRAY),
        		BorderFactory.createEmptyBorder(5,10,5,10)
        	));
        }
        
        txtMaNv.setDisabledTextColor(Color.BLACK);
        // Cbo chức vụ
    	cboChucVu.setFont(new Font("Arial", Font.ITALIC, 16));
    	cboChucVu.setForeground(Color.BLUE); // Màu font 
    	cboChucVu.setBackground(new Color(245, 245, 245));
    	cboChucVu.setPreferredSize(new Dimension(220,30));
    	cboChucVu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        
		
		pnlBottomOfMain.add(btnXoaTrang);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnXoa);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnSua);
		pnlBottomOfMain.add(Box.createHorizontalStrut(10));
		pnlBottomOfMain.add(btnThem);
		pnlBottomOfMain.add(Box.createHorizontalStrut(50));
		
		pnlMain.add(pnlTopOfMain,BorderLayout.NORTH);
		pnlCenterOfMain.setPreferredSize(new Dimension(0,90));
		pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
		pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
		pnlMain.add(pnlBottomOfMain, BorderLayout.SOUTH);
		
		return pnlMain;
	}
	
	public void loadNhanVienData() {
		ConnectDB.getInstance().connect();
        NhanVienDAO nvDAO = new NhanVienDAO();
        dsNhanVien = nvDAO.getAllNhanVien();
		dtm.setRowCount(0);
		for(NhanVien nv : dsNhanVien) {
			Object[] rowData = {
					nv.getMaNV(),
					nv.getTenNV(),
					nv.getSoDienThoai(),
					nv.getChucVu()
			};
		dtm.addRow(rowData);
		}
	}
	public void xoaTrang() {
	    txtMaNv.setText("");
	    txtTenNv.setText("");
	    txtSoDienThoai.setText("");
	    txtChucVu.setText("");
	    txtMaNv.setEnabled(true);
	    tblNhanVien.clearSelection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnXoa) {
			int selectedRow = tblNhanVien.getSelectedRow();
			String ma = tblNhanVien.getValueAt(selectedRow, 0).toString();

			CustomJOptionPane a = new CustomJOptionPane(this, "Có chắc muốn xóa nhân viên " + ma, true);
			int option = a.show();
			if(option == JOptionPane.YES_OPTION) {
				NhanVienDAO nvDAO = new NhanVienDAO();
				boolean result =  nvDAO.deleteNhanVien(ma);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa nhân viên thành công!", false);
					a1.show();
					loadNhanVienData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa nhân viên không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnThem) {
			if(validateInput(true)) {
				String maNV = txtMaNv.getText();
				String tenNV = txtTenNv.getText();
				String soDienThoai = txtSoDienThoai.getText();
				String chucVu = cboChucVu.getSelectedItem().toString();
				NhanVienDAO nvDAO = new NhanVienDAO();
				NhanVien nvNew = new NhanVien(maNV, tenNV,chucVu, soDienThoai , true);
				boolean result = nvDAO.addNhanVien(nvNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm nhân viên thành công!", false);
					a1.show();
					loadNhanVienData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm nhân viên không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnSua) {
			if(validateInput(false)) {
				String maNV = txtMaNv.getText();
				String tenNV = txtTenNv.getText();
				String soDienThoai = txtSoDienThoai.getText();
				String chucVu = cboChucVu.getSelectedItem().toString();
				NhanVienDAO nvDAO = new NhanVienDAO();
				NhanVien nvNew = new NhanVien(maNV, tenNV,chucVu, soDienThoai , true);
				boolean result = nvDAO.updateNhanVien(nvNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật nhân viên thành công!", false);
					a1.show();
					loadNhanVienData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật nhân viên không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnXoaTrang) {
			xoaTrang();
		}
	}
	
	private boolean validateInput(boolean isAddingNew) {
		loadNhanVienData();
	    // Kiểm tra các trường không được rỗng
	    if (txtMaNv.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã nhân viên không được để trống!", false);
	        a1.show();
	        txtMaNv.requestFocus();
	        return false;
	    }
	    if (txtTenNv.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Tên nhân viên không được để trống!", false);
	        a1.show();
	        txtTenNv.requestFocus();
	        return false;
	    }
	    if (txtSoDienThoai.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Số điện thoại không được để trống!", false);
	        a1.show();
	        txtSoDienThoai.requestFocus();
	        return false;
	    }


	    // Kiểm tra định dạng mã nhân viên
	    String maNV = txtMaNv.getText().trim();
	    if(isAddingNew) {
	    	for(NhanVien item : dsNhanVien) {
		    	if(item.getMaNV().equalsIgnoreCase(maNV)) {
			        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã nhân viên không được trùng!", false);
			        a1.show();
			        txtMaNv.requestFocus();
			        return false;
		    	}
		    }
	    }
	    if (!maNV.matches("NV\\d{3}")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã nhân viên phải có định dạng NV kèm 3 ký số (Ví dụ: NV001)!", false);
	        a1.show();
	        txtMaNv.requestFocus();
	        return false;
	    }
	    String tenNV = txtTenNv.getText().trim();
	    if (!tenNV.matches("^[\\p{L}\\s]+$")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Tên nhân viên không được chứa số hoặc ký tự đặc biệt (Ví dụ: Nguyễn Văn A)!", false);
	        a1.show();
	        txtTenNv.requestFocus(); 
	        return false;
	    }
	    // Kiểm tra số điện thoại
	    String soDienThoai = txtSoDienThoai.getText().trim();
	    if (!soDienThoai.matches("\\d{10}")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Số điện thoại phải có đúng 10 ký số!", false);
	        a1.show();
	        txtSoDienThoai.requestFocus();
	        return false;
	    }
	    return true;
	}

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
