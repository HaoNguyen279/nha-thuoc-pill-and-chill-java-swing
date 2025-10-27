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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

import com.toedter.calendar.JDateChooser;

import app.ConnectDB.ConnectDB;
import app.DAO.KhuyenMaiDAO;
import app.Entity.KhuyenMai;


public class CapNhatKhuyenMaiPanel extends JPanel implements ActionListener, MouseListener{
	
	private JLabel lblTieuDe;
	private JLabel lblMaKM;
	private JLabel lblMucGiam;
	private JLabel lblngayApDung;
	private JLabel lblNgayKetThuc;

	private JTextField txtMaKM;
	private JTextField txtMucGiam;
	private JDateChooser calNgayApDung;
	private JDateChooser calNgayKetThuc;

	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnThem;
	private JButton btnXoaTrang;
	
	private DefaultTableModel dtm;
	private JTable tblKhuyenMai;
	
	private ArrayList<KhuyenMai> dsKhuyenMai;
	
	Date today;
	
	public CapNhatKhuyenMaiPanel() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		today = cal.getTime();
		
        lblTieuDe = new JLabel("Cập nhật khuyến mãi", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        lblMaKM = new JLabel("Mã KM:");
        lblMucGiam = new JLabel("Mức giảm:");
        lblngayApDung = new JLabel("Ngày bắt đầu:");
        lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        
        txtMaKM = new JTextField(15);
        txtMucGiam = new JTextField(15);
        calNgayApDung = new JDateChooser();
        calNgayKetThuc = new JDateChooser();
        
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnThem = new JButton("Thêm");
        btnXoaTrang = new JButton("Xóa trắng");
        
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
        
        loadKhuyenMaiData();
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
        tblKhuyenMai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhuyenMai.addMouseListener(this);
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
		pnlMain.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
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
		pnlr3.add(calNgayApDung);
		pnlr3.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		JPanel pnlr4 = new JPanel(new BorderLayout());
		pnlr4.add(lblNgayKetThuc, BorderLayout.WEST);
		pnlr4.add(calNgayKetThuc);
		pnlr4.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		pnlCenterOfMain.add(pnlr1);
		pnlCenterOfMain.add(pnlr2);
		pnlCenterOfMain.add(pnlr3);
		pnlCenterOfMain.add(pnlr4);
		
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
		
        JLabel[] lblItems =  { lblMaKM, lblMucGiam,lblngayApDung, lblNgayKetThuc };
        for(JLabel item : lblItems) {
        	item.setFont(new Font("Arial", Font.PLAIN, 15));
        	item.setPreferredSize(new Dimension(100,0));
        }
        JTextField[] txtItems =  { txtMaKM, txtMucGiam,};
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
        JDateChooser[] calItems = {calNgayApDung, calNgayKetThuc};
        for(JDateChooser item : calItems) {
        	item.setDateFormatString("dd/MM/yyyy");
        	item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        	item.setPreferredSize(new Dimension(150, 30));
        	item.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        	item.getDateEditor().getUiComponent().setBackground(new Color(218, 250, 251)); // vàng nhạt
        	item.getCalendarButton().setBackground(new Color(230, 230, 250)); // nút chọn ngày
        	item.getCalendarButton().setPreferredSize(new Dimension(70,0)); 
        }
        
        txtMaKM.setDisabledTextColor(Color.BLACK);
        
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
	
	public void loadKhuyenMaiData() {
		ConnectDB.getInstance().connect();
        KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
        dsKhuyenMai = kmDAO.getAllKhuyenMai();
		dtm.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		for(KhuyenMai km : dsKhuyenMai) {
			Object[] rowData = {
					km.getMaKM(),
					km.getMucGiamGia(),
					sdf.format(km.getNgayApDung()),
					sdf.format(km.getNgayKetThuc())
			};
		dtm.addRow(rowData);
		}
	}
	public void xoaTrang() {
	    txtMaKM.setText("");
	    txtMucGiam.setText("");
	    calNgayApDung.setDate(today);
	    calNgayKetThuc.setDate(today);
	    txtMaKM.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == btnXoa) {
			int selectedRow = tblKhuyenMai.getSelectedRow();
			String ma = tblKhuyenMai.getValueAt(selectedRow, 0).toString();

			CustomJOptionPane a = new CustomJOptionPane(this, "Có chắc muốn xóa khuyến mãi " + ma, true);
			int option = a.show();
			if(option == JOptionPane.YES_OPTION) {
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				boolean result =  kmDAO.deleteKhuyenMai(ma);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa khuyến mãi thành công!", false);
					a1.show();
					loadKhuyenMaiData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Xóa khuyến mãi không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnThem) {
			if(validateInput(true)) {
				String maNV = txtMaKM.getText();
				float mucGiam = Float.parseFloat(txtMucGiam.getText().toString());
			    Date apDung = calNgayApDung.getDate();
			    Date ketThuc = calNgayKetThuc.getDate();
			    
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				KhuyenMai kmNew = new KhuyenMai(maNV, mucGiam,apDung, ketThuc , true);
				boolean result = kmDAO.addKhuyenMai(kmNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm khuyến mãi thành công!", false);
					a1.show();
					loadKhuyenMaiData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Thêm khuyến mãi không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnSua) {
			if(validateInput(false)) {
				String maNV = txtMaKM.getText();
				float mucGiam = Float.parseFloat(txtMucGiam.getText().toString());
			    Date apDung = calNgayApDung.getDate();
			    Date ketThuc = calNgayKetThuc.getDate();
			    
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				KhuyenMai kmNew = new KhuyenMai(maNV, mucGiam,apDung, ketThuc , true);
				boolean result = kmDAO.updateKhuyenMai(kmNew);
				if(result) {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật khuyến mãi thành công!", false);
					a1.show();
					loadKhuyenMaiData();
				}
				else {
					CustomJOptionPane a1 = new CustomJOptionPane(this, "Cập nhật khuyến mãi không thành công!", false);
					a1.show();
				}
			}
		}
		else if(o == btnXoaTrang) {
			xoaTrang();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
        Object o = e.getSource();
        if(o == tblKhuyenMai) {
            int row = tblKhuyenMai.getSelectedRow();
            if (row >= 0) {
                txtMaKM.setText(tblKhuyenMai.getValueAt(row, 0).toString());
                txtMucGiam.setText(tblKhuyenMai.getValueAt(row, 1).toString());
                calNgayApDung.setDate(null);
                
                try {
                	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dateApDungString = tblKhuyenMai.getValueAt(row, 2).toString();
                    String dateKetThucString = tblKhuyenMai.getValueAt(row, 3).toString();
                    calNgayApDung.setDate(sdf.parse(dateApDungString));
                    calNgayKetThuc.setDate(sdf.parse(dateKetThucString));
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
                txtMaKM.setEnabled(false);
            }
        }
	}
	private boolean validateInput(boolean isAddingNew) {
	    loadKhuyenMaiData();
	    // Kiểm tra các trường không được rỗng
	    if (txtMaKM.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khuyến mãi không được để trống!", false);
	        a1.show();
	        txtMaKM.requestFocus();
	        return false;
	    }
	    if (txtMucGiam.getText().trim().isEmpty()) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mức giảm không được để trống!", false);
	        a1.show();
	        txtMucGiam.requestFocus();
	        return false;
	    }

	    // Kiểm tra định dạng mã khuyến mãi
	    String maKM = txtMaKM.getText().trim();
	    if(isAddingNew) {
	        for(KhuyenMai item : dsKhuyenMai) {
	            if(item.getMaKM().equalsIgnoreCase(maKM) && item.isVisible()) {
	                CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khuyến mãi không được trùng!", false);
	                a1.show();
	                txtMaKM.requestFocus();
	                return false;
	            }
	        }
	    }
	    if (!maKM.matches("KM\\d{3}")) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mã khuyến mãi phải có định dạng KM kèm 3 ký số (Ví dụ: KM001)!", false);
	        a1.show();
	        txtMaKM.requestFocus();
	        return false;
	    }
	    
	    // Kiểm tra mức giảm là số thực không âm
	    try {
	        float mucGiam = Float.parseFloat(txtMucGiam.getText().trim());
	        if (mucGiam < 0 || mucGiam > 1) {
	            CustomJOptionPane a1 = new CustomJOptionPane(this, "Mức giảm phải là số thực không âm! Và thuộc [0,1]", false);
	            a1.show();
	            txtMucGiam.requestFocus();
	            return false;
	        }
	    } catch (NumberFormatException e) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Mức giảm phải là số thực!", false);
	        a1.show();
	        txtMucGiam.requestFocus();
	        return false;
	    }
	    // Kiểm tra ngày áp dụng
	    Date apDung = calNgayApDung.getDate();
	    Date ketThuc = calNgayKetThuc.getDate();
	    
	    if(apDung == null) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Vui lòng chọn ngày áp dụng !", false);
	        a1.show();
	        calNgayApDung.requestFocus();
	        return false;
	    }
	    if(ketThuc == null) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Vui lòng chọn ngày kết thúc !", false);
	        a1.show();
	        calNgayKetThuc.requestFocus();
	        return false;
	    }
	    
	    
	    try {
			JTextField txtDateApDung = (JTextField) calNgayApDung.getDateEditor().getUiComponent(); 
			JTextField txtDateKetThuc = (JTextField) calNgayKetThuc.getDateEditor().getUiComponent(); 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
			String date1 = txtDateApDung.getText().trim();
			String date2 = txtDateKetThuc.getText().trim();
			Date dateTest1 = sdf.parse(date1);
			Date dateTest2 = sdf.parse(date2);
		} catch (Exception e) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Sai định dạng ngày!", false);
	        a1.show();
	        return false;
		}
	    if (apDung.before(today)) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Ngày áp dụng phải là hôm ngay hoặc sau hôm nay!", false);
	        a1.show();
	        calNgayApDung.requestFocus();
	        return false;
	    }
	    if (ketThuc.before(today)) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Ngày kết thúc phải là hôm ngay hoặc sau hôm nay!", false);
	        a1.show();
	        calNgayKetThuc.requestFocus();
	        return false;
	    }
	    if (ketThuc.before(today)) {
	        CustomJOptionPane a1 = new CustomJOptionPane(this, "Ngày kết thúc phải bằng hoặc sau ngày áp dụng!", false);
	        a1.show();
	        calNgayKetThuc.requestFocus();
	        return false;
	    }
	    return true;
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
