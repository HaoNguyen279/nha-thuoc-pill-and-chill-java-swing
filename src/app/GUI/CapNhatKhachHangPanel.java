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
import app.DAO.KhachHangDAO;
import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;
import app.Entity.KhachHang;

public class CapNhatKhachHangPanel extends JPanel implements ActionListener,MouseListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaKh;
	private JLabel lblTenKh;
	private JLabel lblSoDienThoai;
	private JLabel lblDiemTichLuy;

	private JTextField txtMaKh;
	private JTextField txtTenKh;
	private JTextField txtSoDienThoai;
	private JTextField txtDiemTichLuy;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoaTrang;
	
	private DefaultTableModel dtm;
	private JTable tblKhachHang;
	
	private ArrayList<KhachHang> dsKhachHang;
	
	public CapNhatKhachHangPanel() {
		
        lblTieuDe = new JLabel("Cập nhật khách hàng", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        lblMaKh = new JLabel("Mã KH:");
        lblTenKh = new JLabel("Tên KH:");
        lblSoDienThoai = new JLabel("Số điện thoại:");
        lblDiemTichLuy = new JLabel("Điểm tích lũy:");
        
        txtMaKh = new JTextField(15);
        txtTenKh = new JTextField(15);
        txtSoDienThoai = new JTextField(15);
        txtDiemTichLuy = new JTextField(15);
        
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        btnXoaTrang = new JButton("Xóa trắng");
        
        // Table init
        String[] cols = {"Mã khách hàng" , "Tên khách hàng" , "Số điện thoại", "Điểm tích lũy"};
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
        
        loadKhachHangData();
        setBackground(new Color(248, 248, 248));
        setVisible(true);
        
	}
	
	public JScrollPane createBotPanel() {
        tblKhachHang = new JTable(dtm);        
        tblKhachHang.setBackground(new Color(240, 240, 245));
        tblKhachHang.setGridColor(Color.LIGHT_GRAY);
        tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 15));
        tblKhachHang.setRowHeight(40);
        tblKhachHang.setGridColor(Color.LIGHT_GRAY);
        tblKhachHang.setSelectionBackground(new Color(100, 149, 237));
        tblKhachHang.addMouseListener(this);
        tblKhachHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = tblKhachHang.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(192, 232, 246));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
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
		pnlr1.add(lblMaKh, BorderLayout.WEST);
		pnlr1.add(txtMaKh);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr2 = new JPanel(new BorderLayout());
		pnlr2.add(lblTenKh, BorderLayout.WEST);
		pnlr2.add(txtTenKh);
		pnlr2.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr3 = new JPanel(new BorderLayout());
		pnlr3.add(lblSoDienThoai, BorderLayout.WEST);
		pnlr3.add(txtSoDienThoai);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblDiemTichLuy, BorderLayout.WEST);
		pnlr4.add(txtDiemTichLuy);
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
		JButton[] btnList = {btnXoa, btnThem, btnSua, btnXoaTrang};
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
        JLabel[] lblItems =  { lblMaKh, lblTenKh,lblSoDienThoai, lblDiemTichLuy };
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems =  { txtMaKh, txtTenKh,txtSoDienThoai, txtDiemTichLuy };
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
        
        txtMaKh.setDisabledTextColor(Color.BLACK);
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
	
	public void loadKhachHangData() {
		ConnectDB.getInstance().connect();
        KhachHangDAO khDAO = new KhachHangDAO();
        dsKhachHang = khDAO.getAllKhachHang();
		dtm.setRowCount(0);
		for(KhachHang kh : dsKhachHang) {
			Object[] rowData = {
				kh.getMaKH(),
				kh.getTenKH(),
				kh.getSoDienThoai(),
				kh.getDiemTichLuy()	
			};
		dtm.addRow(rowData);
		}
	}
	public void xoaTrang() {
	    txtMaKh.setText("");
	    txtTenKh.setText("");
	    txtSoDienThoai.setText("");
	    txtDiemTichLuy.setText("");
	    txtMaKh.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnXoa) {
			int selectedRow = tblKhachHang.getSelectedRow();
			String ma = tblKhachHang.getValueAt(selectedRow, 0).toString();

			CustomJOptionPane a = new CustomJOptionPane(this, "Có chắc muốn xóa khách hàng " + ma, true);
			int option = a.show();
			if(option == JOptionPane.YES_OPTION) {
				KhachHangDAO nvDAO = new KhachHangDAO();
				boolean result =  nvDAO.deleteKhachHang(ma);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa khách hàng thành công!", false);
					a1.show();
					loadKhachHangData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa khách hàng không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnThem) {
			if(validateInput(true)) {
				String maKH = txtMaKh.getText();
				String tenKH = txtTenKh.getText();
				String soDienThoai = txtSoDienThoai.getText();
				int diemTichLuy = Integer.parseInt(txtDiemTichLuy.getText());
				KhachHangDAO khDAO = new KhachHangDAO();
				KhachHang khNew = new KhachHang(maKH, tenKH, soDienThoai , diemTichLuy, true);
				boolean result = khDAO.addKhachHang(khNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm khách hàng thành công!", false);
					a1.show();
					loadKhachHangData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm khách hàng không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnSua) {
			if(validateInput(false)) {
				String maKH = txtMaKh.getText();
				String tenKH = txtTenKh.getText();
				String soDienThoai = txtSoDienThoai.getText();
				int diemTichLuy = Integer.parseInt(txtDiemTichLuy.getText());
				KhachHangDAO khDAO = new KhachHangDAO();
				KhachHang khNew = new KhachHang(maKH, tenKH, soDienThoai , diemTichLuy, true);
				boolean result = khDAO.updateKhachHang(khNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật viên thành công!", false);
					a1.show();
					loadKhachHangData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật khách hàng không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnXoaTrang) {
			xoaTrang();
		}
		
	}
	private boolean validateInput(boolean isAddingNew) {
	    loadKhachHangData();
	    // Kiểm tra các trường không được rỗng
	    if (txtMaKh.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khách hàng không được để trống!", false);
	        a1.show();
	        txtMaKh.requestFocus();
	        return false;
	    }
	    if (txtTenKh.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Tên khách hàng không được để trống!", false);
	        a1.show();
	        txtTenKh.requestFocus();
	        return false;
	    }
	    if (txtSoDienThoai.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Số điện thoại không được để trống!", false);
	        a1.show();
	        txtSoDienThoai.requestFocus();
	        return false;
	    }
	    if (txtDiemTichLuy.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Điểm tích lũy không được để trống!", false);
	        a1.show();
	        txtDiemTichLuy.requestFocus();
	        return false;
	    }

	    // Kiểm tra định dạng mã khách hàng
	    String maKH = txtMaKh.getText().trim();
	    if(isAddingNew) {
	        for(KhachHang item : dsKhachHang) {
	            if(item.getMaKH().equalsIgnoreCase(maKH) && item.isVisible()) {
	                CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khách hàng không được trùng!", false);
	                a1.show();
	                txtMaKh.requestFocus();
	                return false;
	            }
	        }
	    }
	    if (!maKH.matches("KH\\d{3}")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khách hàng phải có định dạng KH kèm 3 ký số (Ví dụ: KH001)!", false);
	        a1.show();
	        txtMaKh.requestFocus();
	        return false;
	    }
	    
	    // Kiểm tra tên khách hàng
	    String tenKH = txtTenKh.getText().trim();
	    if (!tenKH.matches("^[\\p{L}\\s]+$")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Tên khách hàng không được chứa số hoặc ký tự đặc biệt (Ví dụ: Nguyễn Văn A)!", false);
	        a1.show();
	        txtTenKh.requestFocus(); 
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
	    
	    // Kiểm tra điểm tích lũy là số nguyên không âm
	    try {
	        int diemTichLuy = Integer.parseInt(txtDiemTichLuy.getText().trim());
	        if (diemTichLuy < 0) {
	            CustomJOptionPane a1 = new CustomJOptionPane(this, "Điểm tích lũy phải là số nguyên không âm!", false);
	            a1.show();
	            txtDiemTichLuy.requestFocus();
	            return false;
	        }
	    } catch (NumberFormatException e) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Điểm tích lũy phải là số nguyên!", false);
	        a1.show();
	        txtDiemTichLuy.requestFocus();
	        return false;
	    }
	    
	    return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblKhachHang) {
            int row = tblKhachHang.getSelectedRow();
            if (row >= 0) {
                txtMaKh.setText(tblKhachHang.getValueAt(row, 0).toString());
                txtTenKh.setText(tblKhachHang.getValueAt(row, 1).toString());
                txtSoDienThoai.setText(tblKhachHang.getValueAt(row, 2).toString());
                txtDiemTichLuy.setText(tblKhachHang.getValueAt(row, 3).toString());
                
                txtMaKh.setEnabled(false);
                
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
