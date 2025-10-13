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
import app.DAO.KhuyenMaiDAO;
import app.Entity.KhuyenMai;

public class CapNhatKhuyenMaiPanel extends JPanel implements ActionListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaKM;
	private JLabel lblMucGiam;
	private JLabel lblngayApDung;
	private JLabel lblNgayKetThuc;

	private JTextField txtMaKM;
	private JTextField txtMucGiam;
	private JTextField txtngayApDung;
	private JTextField txtNgayKetThuc;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	
	private DefaultTableModel dtm;
	private JTable tblKhuyenMai;
	
	private ArrayList<KhuyenMai> dsKhuyenMai;
	
	public CapNhatKhuyenMaiPanel() {
        lblTieuDe = new JLabel("Cập nhật khuyến mãi", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        lblMaKM = new JLabel("Mã KM:");
        lblMucGiam = new JLabel("Mức giảm:");
        lblngayApDung = new JLabel("Ngày bắt đầu:");
        lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        
        txtMaKM = new JTextField(15);
        txtMucGiam = new JTextField(15);
        txtngayApDung = new JTextField(15);
        txtNgayKetThuc = new JTextField(15);

        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        
        // Table init
        String[] cols = {"Mã khuyến mãi" , "Mức giảm" , "Ngày bắt đầu", "Ngày kết thúc"};
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
        tblKhuyenMai = new JTable(dtm);        
        tblKhuyenMai.setBackground(new Color(240, 240, 245));
        tblKhuyenMai.setGridColor(Color.LIGHT_GRAY);
        tblKhuyenMai.setFont(new Font("Arial", Font.PLAIN, 15));
        tblKhuyenMai.setRowHeight(40);
        tblKhuyenMai.setGridColor(Color.LIGHT_GRAY);
        tblKhuyenMai.setSelectionBackground(new Color(100, 149, 237));
        JTableHeader header = tblKhuyenMai.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(192, 232, 246));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblKhuyenMai);
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
		pnlr1.add(lblMaKM, BorderLayout.WEST);
		pnlr1.add(txtMaKM);
		pnlr1.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

		JPanel pnlr2 = new JPanel(new BorderLayout());
		pnlr2.add(lblMucGiam, BorderLayout.WEST);
		pnlr2.add(txtMucGiam);
		pnlr2.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr3 = new JPanel(new BorderLayout());
		pnlr3.add(lblngayApDung, BorderLayout.WEST);
		pnlr3.add(txtngayApDung);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblNgayKetThuc, BorderLayout.WEST);
		pnlr4.add(txtNgayKetThuc);
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
        JLabel[] lblItems =  { lblMaKM, lblMucGiam,lblngayApDung, lblNgayKetThuc };
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems =  { txtMaKM, txtMucGiam,txtngayApDung, txtNgayKetThuc };
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
        KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
        dsKhuyenMai = kmDAO.getAllKhuyenMai();
		dtm.setRowCount(0);
		for(KhuyenMai km : dsKhuyenMai) {
			Object[] rowData = {
					km.getMaKM(),
					km.getMucGiamGia(),
					km.getNgayApDung(),
					km.getNgayKetThuc()
			};
		dtm.addRow(rowData);
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new CapNhatKhuyenMaiPanel();
	}

}
