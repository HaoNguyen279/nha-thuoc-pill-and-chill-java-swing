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
import app.DAO.ChucVuDAO;
import app.DAO.KhachHangDAO;
import app.DAO.KhachHangDAO;
import app.Entity.ChucVu;
import app.Entity.KhachHang;
import app.Entity.KhachHang;

public class CapNhatChucVuPanel extends JPanel implements ActionListener,MouseListener{
	
	private JLabel lblTieuDe;


	private JTextField txtMaKh;
	private JTextField txtTenKh;


	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoaTrang;
	
	private DefaultTableModel dtm;
	private JTable tblChucVu;
	

	private JLabel lblMaChucVu;
	private JLabel lblTenChucVu;
	private JTextField txtMaChucVu;
	private JTextField txtTenChucVu;
	private ArrayList<ChucVu> dsChucVu;
	private ChucVuDAO cvDao;
	
	public CapNhatChucVuPanel() {
		
		
        lblTieuDe = new JLabel("Cập nhật chức vụ", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        lblMaChucVu = new JLabel("Mã Chức Vụ:");
        lblTenChucVu = new JLabel("Tên Chức Vụ:");
        
        
        txtMaChucVu= new JTextField(15);
        txtTenChucVu = new JTextField(15);
  
        
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        btnXoaTrang = new JButton("Xóa trắng");
        
        // Table init
        String[] cols = {"Mã chức vụ" , "Tên chức vụ"};
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
        
        loadChucVuData();
        setBackground(new Color(248, 248, 248));
        setVisible(true);
        
	}
	
	public JScrollPane createBotPanel() {
        tblChucVu = new JTable(dtm);        
        tblChucVu.setBackground(new Color(240, 240, 245));
        tblChucVu.setGridColor(Color.LIGHT_GRAY);
        tblChucVu.setFont(new Font("Arial", Font.PLAIN, 15));
        tblChucVu.setRowHeight(40);
        tblChucVu.setSelectionBackground(new Color(190, 226, 252));
        tblChucVu.addMouseListener(this);
        tblChucVu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = tblChucVu.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(0, 117, 196));
        header.setForeground(Color.white);
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblChucVu);
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
		
		pnlMain.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
		pnlTopOfMain.add(lblTieuDe);
		
		JPanel pnlr1 = new JPanel(new BorderLayout());
		pnlr1.add(lblMaChucVu, BorderLayout.WEST);
		pnlr1.add(txtMaChucVu);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr2 = new JPanel(new BorderLayout());
		pnlr2.add(lblTenChucVu, BorderLayout.WEST);
		pnlr2.add(txtTenChucVu);
		pnlr2.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		
		
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr2);
	
		
		btnThem.setBackground(new Color(46, 204, 113));
		btnXoa.setBackground(new Color(231, 76, 60));
		btnSua.setBackground(new Color(52, 152, 219));
		btnXoaTrang.setBackground(Color.WHITE);
		
		// JButton, JLabel, JTextField customization
		JButton[] btnList = {btnXoa, btnThem, btnSua, btnXoaTrang};
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
        JLabel[] lblItems =  { lblMaChucVu, lblTenChucVu };
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems =  { txtMaChucVu, txtTenChucVu };
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
        
        txtMaChucVu.setDisabledTextColor(Color.BLACK);
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
	
	public void loadChucVuData() {
		ConnectDB.getInstance().connect();
        
        cvDao = new ChucVuDAO();
        dsChucVu = cvDao.getAllChucVu();
		dtm.setRowCount(0);
		for(ChucVu cv : dsChucVu) {
			Object[] rowData = {
				cv.getMaChucVu(),
				cv.getTenChucVu(),
			};
		dtm.addRow(rowData);
		}
	}
	public void xoaTrang() {
		loadChucVuData();
	    txtMaChucVu.setText("");
	    txtTenChucVu.setText("");
	    txtMaChucVu.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnXoa) {
			int selectedRow = tblChucVu.getSelectedRow();
			String maCV = tblChucVu.getValueAt(selectedRow, 0).toString();

			CustomJOptionPane a = new CustomJOptionPane(this, "Có chắc muốn xóa chức vụ " + maCV, true);
			int option = a.show();
			if(option == JOptionPane.YES_OPTION) {
				
				boolean result =  cvDao.delete(maCV);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa chức vụ thành công!", false);
					a1.show();
					loadChucVuData();
					xoaTrang();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Không thể xóa chức vụ này!\n"+"Hãy chắc chắn rằng chức vụ muốn xóa không có nhân viên", false);
					a1.show();
				}
			}
		}
		else if(o == btnThem) {
			if(validateInput(true)) {
				String maChucVu = txtMaChucVu.getText();
				String tenChucVu = txtTenChucVu.getText();
				
				
				ChucVu cv = new ChucVu(maChucVu,tenChucVu,true);
				boolean result = cvDao.insert(cv);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm chức vụ thành công!", false);
					a1.show();
					loadChucVuData();
					xoaTrang();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm chức vụ không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnSua) {
			if(validateInput(false)) {
				String maChucVu = txtMaChucVu.getText();
				String tenChucVu = txtTenChucVu.getText();
				ChucVu cv = new ChucVu(maChucVu,tenChucVu,true);
				boolean result = cvDao.update(cv);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật chức vụ thành công!", false);
					a1.show();
					loadChucVuData();
					xoaTrang();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật chức vụ không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnXoaTrang) {
			xoaTrang();
		}
		
	}
	private boolean validateInput(boolean isAddingNew) {
	    loadChucVuData();
	    // Kiểm tra các trường không được rỗng
	    if (txtMaChucVu.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã chức vụ không được để trống!", false);
	        a1.show();
	        txtMaKh.requestFocus();
	        return false;
	    }
	    if (txtTenChucVu.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Tên chức vụ không được để trống!", false);
	        a1.show();
	        txtTenKh.requestFocus();
	        return false;
	    }
	    return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblChucVu) {
            int row = tblChucVu.getSelectedRow();
            if (row >= 0) {
                txtMaChucVu.setText(tblChucVu.getValueAt(row, 0).toString());
                txtTenChucVu.setText(tblChucVu.getValueAt(row, 1).toString());
  
                txtMaChucVu.setEnabled(false);
                
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

