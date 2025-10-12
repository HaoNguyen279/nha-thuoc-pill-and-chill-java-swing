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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.KhachHangDAO;
import app.Entity.KhachHang;

public class CapNhatKhachHangGUI extends JFrame implements ActionListener{
	
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
	
	private DefaultTableModel dtm;
	private JTable tblKhachHang;
	
	private ArrayList<KhachHang> dsKhachHang;
	
	public CapNhatKhachHangGUI() {
		
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
        setTitle("Cập nhật khách hàng");
        setBackground(new Color(248, 248, 248));
        setVisible(true);
        setSize(new Dimension(1200,700));
        setLocationRelativeTo(null);
        
	}
	
	public JScrollPane createBotPanel() {
        tblKhachHang = new JTable(dtm);        
        tblKhachHang.setBackground(new Color(240, 240, 245));
        tblKhachHang.setGridColor(Color.LIGHT_GRAY);
        tblKhachHang.setFont(new Font("Arial", Font.PLAIN, 15));
        tblKhachHang.setRowHeight(40);
        tblKhachHang.setGridColor(Color.LIGHT_GRAY);
        tblKhachHang.setSelectionBackground(new Color(100, 149, 237));
        JTableHeader header = tblKhachHang.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
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
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new CapNhatKhachHangGUI();
	}

}
