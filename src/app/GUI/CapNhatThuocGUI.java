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
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import app.DAO.NhanVienDAO;
import app.DAO.ThuocDAO;
import app.Entity.KhachHang;
import app.Entity.NhanVien;
import app.Entity.Thuoc;

public class CapNhatThuocGUI extends JFrame implements ActionListener, MouseListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaThuoc;
	private JLabel lblMaLo;
	private JLabel lblTenThuoc;
	private JLabel lblSoLuongTon;
	private JLabel lblGiaBan;
	private JLabel lblDonVi;
	private JLabel lblSoLuongToiThieu;
	private JLabel lblNhaSanXuat;

	private JTextField txtMaThuoc;
	private JTextField txtMaLo;
	private JTextField txtTenThuoc;
	private JTextField txtSoLuongTon;
	private JTextField txtGiaBan;
	private JTextField txtDonVi;
	private JTextField txtSoLuongToiThieu;
	private JTextField txtNhaSanXuat;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	
	private DefaultTableModel dtm;
	private JTable tblThuoc;
	
	private ArrayList<Thuoc> dsThuoc;
	
	public CapNhatThuocGUI() {
        lblTieuDe = new JLabel("Cập nhật thuốc", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        lblMaThuoc = new JLabel("Mã thuốc:");
        lblMaLo = new JLabel("Mã lô:");
        lblTenThuoc = new JLabel("Tên thuốc:");
        lblSoLuongTon = new JLabel("SL tồn:");
        lblGiaBan = new JLabel("Giá bán:");
    	lblDonVi = new JLabel("Đơn vị:");
    	lblSoLuongToiThieu = new JLabel("SL tối thiểu:");
    	lblNhaSanXuat = new JLabel("Nhà sản xuất:");
    	
        txtMaThuoc = new JTextField(15);
        txtMaLo = new JTextField(15);
        txtTenThuoc = new JTextField(15);
        txtSoLuongTon = new JTextField(15);
        txtGiaBan = new JTextField(15);
    	txtDonVi = new JTextField(15);
    	txtSoLuongToiThieu = new JTextField(15);
    	txtNhaSanXuat = new JTextField(15);
    	
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        
        // Table init
        String[] cols = {"Mã thuốc" , "Mã lô" , "Tên thuốc", "Số lượng tồn", "Giá bán","Đơn vị","Số lượng tối thiểu","Mã nsx"};
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
        setTitle("Cập nhật thuốc");
        setBackground(new Color(248, 248, 248));
        setVisible(true);
        setSize(new Dimension(1200,700));
        setLocationRelativeTo(null);
        
	}
	
	public JScrollPane createBotPanel() {
        tblThuoc = new JTable(dtm);
        tblThuoc.setBackground(new Color(240, 240, 245));
        tblThuoc.setGridColor(Color.LIGHT_GRAY);
        tblThuoc.setFont(new Font("Arial", Font.PLAIN, 15));
        tblThuoc.setRowHeight(40);
        tblThuoc.setGridColor(Color.LIGHT_GRAY);
        tblThuoc.setSelectionBackground(new Color(100, 149, 237));
        tblThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblThuoc.addMouseListener(this);
        JTableHeader header = tblThuoc.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(192, 232, 246));
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
		JPanel pnlCenterOfMain = new JPanel(new GridLayout(3,3,10,10));
		JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		pnlTopOfMain.add(lblTieuDe);
		
		JPanel pnlr1 = new JPanel(new BorderLayout());
		pnlr1.add(lblMaThuoc, BorderLayout.WEST);
		pnlr1.add(txtMaThuoc);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr2 = new JPanel(new BorderLayout());
		pnlr2.add(lblMaLo, BorderLayout.WEST);
		pnlr2.add(txtMaLo);
		pnlr2.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
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
		pnlr6.add(txtDonVi);
		pnlr6.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr7 = new JPanel(new BorderLayout());
		pnlr7.add(lblSoLuongToiThieu, BorderLayout.WEST);
		pnlr7.add(txtSoLuongToiThieu);
		pnlr7.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr8 = new JPanel(new BorderLayout());
		pnlr8.add(lblNhaSanXuat, BorderLayout.WEST);
		pnlr8.add(txtNhaSanXuat);
		pnlr8.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr2);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain.add(pnlr4);
		pnlCenterOfMain.add(pnlr5);
		pnlCenterOfMain.add(pnlr6);
		pnlCenterOfMain.add(pnlr7);
		pnlCenterOfMain.add(pnlr8);
		
		btnThem.setBackground(new Color(224, 248, 228));
		btnXoa.setBackground(new Color(255, 121, 121));
		btnSua.setBackground(new Color(223, 249, 251));
		
		// JButton, JLabel, JTextField customization
		JButton[] btnList = {btnXoa, btnThem, btnSua};
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
		JLabel[] lblItems = { 
			    lblMaThuoc, lblMaLo, lblTenThuoc, lblSoLuongTon,
			    lblGiaBan, lblDonVi, lblSoLuongToiThieu, lblNhaSanXuat
			};
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems = { 
        	    txtMaThuoc, txtMaLo, txtTenThuoc, txtSoLuongTon,
        	    txtGiaBan, txtDonVi, txtSoLuongToiThieu, txtNhaSanXuat
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
        
		txtMaThuoc.setDisabledTextColor(Color.GRAY);
		txtMaLo.setDisabledTextColor(Color.GRAY);
		
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
	
	public void loadNhanVienData() {
		ConnectDB.getInstance().connect();
        ThuocDAO thuocDAO = new ThuocDAO();
        dsThuoc = thuocDAO.getListThuoc();
		dtm.setRowCount(0);
		for(Thuoc thuoc : dsThuoc) {
			Object[] rowData = {
					thuoc.getMaThuoc(),
					thuoc.getMaLo(),
					thuoc.getTenThuoc(),
					thuoc.getSoLuongTon(),
					thuoc.getGiaBan(),
					thuoc.getDonVi(),
					thuoc.getSoLuongToiThieu(),
					thuoc.getMaNSX()
			};
		dtm.addRow(rowData);
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new CapNhatThuocGUI();
	}

	@Override	
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();
		if(o == tblThuoc) {
			txtMaThuoc.setEnabled(false);
			txtMaLo.setEnabled(false);
			int row = tblThuoc.getSelectedRow();
	        if (row >= 0) {
	            txtMaThuoc.setText(tblThuoc.getValueAt(row, 0).toString());
	            txtMaLo.setText(tblThuoc.getValueAt(row, 1).toString());
	            txtTenThuoc.setText(tblThuoc.getValueAt(row, 2).toString());
	            txtSoLuongTon.setText(tblThuoc.getValueAt(row, 3).toString());
	            txtGiaBan.setText(tblThuoc.getValueAt(row, 4).toString());
	            txtDonVi.setText(tblThuoc.getValueAt(row, 5).toString());
	            txtSoLuongToiThieu.setText(tblThuoc.getValueAt(row, 6).toString());
	            txtNhaSanXuat.setText(tblThuoc.getValueAt(row, 7).toString());
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
