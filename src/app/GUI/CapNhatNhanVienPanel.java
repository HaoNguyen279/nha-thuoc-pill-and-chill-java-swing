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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.NhanVienDAO;
import app.Entity.NhanVien;

public class CapNhatNhanVienPanel extends JPanel implements ActionListener{
	
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

        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        
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
		pnlr4.add(txtChucVu);
		pnlr4.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr2);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain.add(pnlr4);
		
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
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new CapNhatNhanVienPanel();
	}

}
