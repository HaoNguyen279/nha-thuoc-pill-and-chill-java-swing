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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        tblKhuyenMai.addMouseListener(this);
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
		Object source = e.getSource();
		if (source.equals(btnThem)) {
			String maKM = txtMaKM.getText().trim();
			String mucGiamStr = txtMucGiam.getText().trim();
			String ngayBatDauStr = txtngayApDung.getText().trim();
			String ngayKetThucStr = txtNgayKetThuc.getText().trim();
			
			if(maKM.isEmpty() || mucGiamStr.isEmpty() || ngayBatDauStr.isEmpty() || ngayKetThucStr.isEmpty()) {
				javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
				return;
			}
			
			try {
				float mucGiam = Float.parseFloat(mucGiamStr);
				java.sql.Date ngayBatDau = java.sql.Date.valueOf(ngayBatDauStr); // Format: yyyy-MM-dd
				java.sql.Date ngayKetThuc = java.sql.Date.valueOf(ngayKetThucStr);
				
				KhuyenMai km = new KhuyenMai(maKM, mucGiam, ngayBatDau, ngayKetThuc, true);
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				boolean success = kmDAO.addKhuyenMai(km);
				if(success) {
					javax.swing.JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công.");
					loadNhanVienData();
					clearFields();
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại.");
				}
			} catch (NumberFormatException ex) {
				javax.swing.JOptionPane.showMessageDialog(this, "Mức giảm phải là số thực.");
			} catch (IllegalArgumentException ex) {
				javax.swing.JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo format: yyyy-MM-dd (ví dụ: 2024-01-15)");
			}
			
		} else if (source.equals(btnSua)) {
			int selectedRow = tblKhuyenMai.getSelectedRow();
			if(selectedRow == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để sửa.");
				return;
			}
			
			String maKM = txtMaKM.getText().trim();
			String mucGiamStr = txtMucGiam.getText().trim();
			String ngayBatDauStr = txtngayApDung.getText().trim();
			String ngayKetThucStr = txtNgayKetThuc.getText().trim();
			
			if(maKM.isEmpty() || mucGiamStr.isEmpty() || ngayBatDauStr.isEmpty() || ngayKetThucStr.isEmpty()) {
				javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
				return;
			}
			
			try {
				float mucGiam = Float.parseFloat(mucGiamStr);
				java.sql.Date ngayBatDau = java.sql.Date.valueOf(ngayBatDauStr);
				java.sql.Date ngayKetThuc = java.sql.Date.valueOf(ngayKetThucStr);
				
				KhuyenMai km = new KhuyenMai(maKM, mucGiam, ngayBatDau, ngayKetThuc, true);
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				boolean success = kmDAO.updateKhuyenMai(km);
				if(success) {
					javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công.");
					loadNhanVienData();
					clearFields();
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thất bại.");
				}
			} catch (NumberFormatException ex) {
				javax.swing.JOptionPane.showMessageDialog(this, "Mức giảm phải là số thực.");
			} catch (IllegalArgumentException ex) {
				javax.swing.JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo format: yyyy-MM-dd (ví dụ: 2024-01-15)");
			}
			
		} else if (source.equals(btnXoa)) {
			int selectedRow = tblKhuyenMai.getSelectedRow();
			if(selectedRow == -1) {
				javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để xóa.");
				return;
			}
			
			int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
				"Bạn có chắc chắn muốn xóa khuyến mãi này?", 
				"Xác nhận xóa", 
				javax.swing.JOptionPane.YES_NO_OPTION);
			
			if(confirm == javax.swing.JOptionPane.YES_OPTION) {
				String maKM = (String) dtm.getValueAt(selectedRow, 0);
				KhuyenMaiDAO kmDAO = new KhuyenMaiDAO();
				boolean success = kmDAO.deleteKhuyenMai(maKM);
				if(success) {
					javax.swing.JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công.");
					loadNhanVienData();
					clearFields();
				} else {
					javax.swing.JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thất bại.");
				}
			}
		}
		
	}
	
	private void clearFields() {
		txtMaKM.setText("");
		txtMucGiam.setText("");
		txtngayApDung.setText("");
		txtNgayKetThuc.setText("");
		tblKhuyenMai.clearSelection();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedRow = tblKhuyenMai.getSelectedRow();
		if(selectedRow != -1) {
			String maKM = (String) dtm.getValueAt(selectedRow, 0);
			float mucGiam = (float) dtm.getValueAt(selectedRow, 1);
			java.sql.Date ngayBatDau = (java.sql.Date) dtm.getValueAt(selectedRow, 2);
			java.sql.Date ngayKetThuc = (java.sql.Date) dtm.getValueAt(selectedRow, 3);
			
			txtMaKM.setText(maKM);
			txtMucGiam.setText(String.valueOf(mucGiam));
			txtngayApDung.setText(ngayBatDau.toString());
			txtNgayKetThuc.setText(ngayKetThuc.toString());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Không cần xử lý
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Không cần xử lý
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Không cần xử lý
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Không cần xử lý
	}


}
