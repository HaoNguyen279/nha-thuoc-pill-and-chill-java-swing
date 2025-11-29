package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import app.DAO.LoThuocDAO;
import app.Entity.LoThuocHetHan;


public class ThongKeTheoHSDPanel extends JPanel implements ActionListener, MouseListener{
	
	private JButton btnThongKe, btnLamMoi, btnXoaLoThuoc;
	private JLabel lblTieuDe, lblLoaiThongKe, lblSoNgay;
	private JComboBox<String> cboLoaiThongKe;
	private JTextField txtSoNgay;
	private DefaultTableModel dtm;
	private JTable tblLoThuoc;
	private ArrayList<LoThuocHetHan> dsLoThuocHetHan;
	private LoThuocDAO loThuocDAO;
	private SimpleDateFormat dateFormat;
	
	public ThongKeTheoHSDPanel() {
		ConnectDB.getInstance().connect();
		loThuocDAO = new LoThuocDAO();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
        lblTieuDe = new JLabel("THỐNG KÊ THUỐC HẾT HẠN SỬ DỤNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        lblLoaiThongKe = new JLabel("Loại thống kê:");
        lblLoaiThongKe.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblSoNgay = new JLabel("Số ngày kiểm tra:");
        lblSoNgay.setFont(new Font("Arial", Font.BOLD, 14));
        
        cboLoaiThongKe = new JComboBox<String>();
        cboLoaiThongKe.addItem("Thuốc đã hết hạn");
        cboLoaiThongKe.addItem("Thuốc sắp hết hạn");
        cboLoaiThongKe.setFont(new Font("Arial", Font.PLAIN, 14));
        cboLoaiThongKe.setBackground(Color.WHITE);
        cboLoaiThongKe.setPreferredSize(new Dimension(200, 35));
        cboLoaiThongKe.addActionListener(this);
        
        txtSoNgay = new JTextField("30");
        txtSoNgay.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSoNgay.setPreferredSize(new Dimension(200, 35));
        txtSoNgay.setEnabled(false);
        
        btnThongKe = new JButton("Thống kê");
        btnLamMoi = new JButton("Làm mới");
        btnXoaLoThuoc = new JButton("Xóa lô thuốc");
        
        String[] cols = {"Mã lô", "Mã thuốc", "Tên thuốc", "Ngày sản xuất", "Hạn sử dụng", "Số lượng tồn", "Trạng thái"};
        dtm = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        
        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(createBotPanel(), BorderLayout.CENTER);
        
        loadLoThuocHetHan();
        setBackground(new Color(248, 248, 248));
	}
	
	public JScrollPane createBotPanel() {
        tblLoThuoc = new JTable(dtm);
        tblLoThuoc.setBackground(new Color(240, 240, 245));
        tblLoThuoc.setGridColor(Color.LIGHT_GRAY);
        tblLoThuoc.setFont(new Font("Arial", Font.PLAIN, 14));
        tblLoThuoc.setRowHeight(40);
        tblLoThuoc.setSelectionBackground(new Color(190, 226, 252));
        tblLoThuoc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLoThuoc.addMouseListener(this);
        
        JTableHeader header = tblLoThuoc.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(0, 117, 196));
        header.setForeground(Color.white);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(tblLoThuoc);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(0, 50, 50, 50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        return scrollPane;
	}
	
	public JPanel createTopPanel() {
		JPanel pnlMain = new JPanel(new BorderLayout());
		JPanel pnlTopOfMain = new JPanel();
		
		pnlTopOfMain.add(lblTieuDe, SwingConstants.CENTER);
		
		JPanel pnlCenterOfMain = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
		pnlCenterOfMain.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		pnlCenterOfMain.setBackground(Color.WHITE);
		
		pnlCenterOfMain.add(lblLoaiThongKe);
		pnlCenterOfMain.add(cboLoaiThongKe);
		pnlCenterOfMain.add(Box.createHorizontalStrut(30));
		pnlCenterOfMain.add(lblSoNgay);
		pnlCenterOfMain.add(txtSoNgay);
		
		JPanel pnlBottomOfMain = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		
		btnThongKe.setBackground(new Color(52, 152, 219));
		btnLamMoi.setBackground(new Color(149, 165, 166));
		btnXoaLoThuoc.setBackground(new Color(231, 76, 60));
		
		JButton[] btnList = {btnThongKe, btnLamMoi, btnXoaLoThuoc};
		for(JButton item : btnList) {
			item.setFont(new Font("Arial", Font.BOLD, 14));
			item.setForeground(Color.WHITE);
			item.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(10, 30, 10, 30)
			));
			item.setFocusPainted(false);
			item.setCursor(new Cursor(Cursor.HAND_CURSOR));
			item.addActionListener(this);
		}

		pnlBottomOfMain.add(btnThongKe);
		pnlBottomOfMain.add(btnLamMoi);
		pnlBottomOfMain.add(btnXoaLoThuoc);
		
		pnlMain.add(pnlTopOfMain, BorderLayout.NORTH);
		pnlMain.add(pnlCenterOfMain, BorderLayout.CENTER);
		pnlBottomOfMain.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 50));
		pnlMain.add(pnlBottomOfMain, BorderLayout.SOUTH);
		
		return pnlMain;
	}
	
	private void loadLoThuocHetHan() {
		dsLoThuocHetHan = loThuocDAO.getCacLoThuocHetHan();
		hienThiDuLieuLenBang();
	}
	
	private void loadLoThuocSapHetHan(int soNgay) {
		dsLoThuocHetHan = loThuocDAO.getCacLoThuocSapHetHan(soNgay);
		hienThiDuLieuLenBang();
	}
	
	private void hienThiDuLieuLenBang() {
		dtm.setRowCount(0);
		String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
		
		for(LoThuocHetHan thuoc : dsLoThuocHetHan) {
			String trangThai = "";
			if(loaiThongKe.equals("Thuốc đã hết hạn")) {
				int soNgayQuaHan = thuoc.getSoNgayDaHetHan();
				trangThai = "Quá hạn " + soNgayQuaHan + " ngày";
			} else {
				int soNgayConLai = thuoc.getSoNgayDaHetHan();
				trangThai = "Còn " + soNgayConLai + " ngày";
			}
			
			Object[] rowData = {
				thuoc.getMaLo(),
				thuoc.getMaThuoc(),
				thuoc.getTenThuoc(),
				dateFormat.format(thuoc.getNgaySanXuat()),
				dateFormat.format(thuoc.getHanSuDung()),
				thuoc.getSoLuongTon(),
				trangThai
			};
			dtm.addRow(rowData);
		}
	}
	
	private void thongKe() {
		String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
		
		if(loaiThongKe.equals("Thuốc đã hết hạn")) {
			loadLoThuocHetHan();
		} else {
			try {
				int soNgay = Integer.parseInt(txtSoNgay.getText().trim());
				if(soNgay <= 0) {
					CustomJOptionPane errorPane = new CustomJOptionPane(this, "Số ngày phải lớn hơn 0!", false);
					errorPane.show();
					txtSoNgay.requestFocus();
					return;
				}
				loadLoThuocSapHetHan(soNgay);
			} catch(NumberFormatException ex) {
				CustomJOptionPane errorPane = new CustomJOptionPane(this, "Vui lòng nhập số ngày hợp lệ!", false);
				errorPane.show();
				txtSoNgay.requestFocus();
			}
		}
	}
	
	private void lamMoi() {
		cboLoaiThongKe.setSelectedIndex(0);
		txtSoNgay.setText("30");
		txtSoNgay.setEnabled(false);
		loadLoThuocHetHan();
	}
	
	private void xoaLoThuoc() {
		int selectedRow = tblLoThuoc.getSelectedRow();
		
		if(selectedRow == -1) {
			CustomJOptionPane warningPane = new CustomJOptionPane(this, "Vui lòng chọn lô thuốc cần xóa!", false);
			warningPane.show();
			return;
		}
		
		String maLo = tblLoThuoc.getValueAt(selectedRow, 0).toString();
		String maThuoc = tblLoThuoc.getValueAt(selectedRow, 1).toString();
		String tenThuoc = tblLoThuoc.getValueAt(selectedRow, 2).toString();
		String ngaySanXuatStr = tblLoThuoc.getValueAt(selectedRow, 3).toString();
		String hanSuDungStr = tblLoThuoc.getValueAt(selectedRow, 4).toString();
		String soLuongTon = tblLoThuoc.getValueAt(selectedRow, 5).toString();
		
		// Kiểm tra hạn sử dụng
		String trangThaiHSD = "";
		try {
			java.util.Date hanSuDung = dateFormat.parse(hanSuDungStr);
			java.util.Date ngayHienTai = new java.util.Date();
			
			if(hanSuDung.before(ngayHienTai)) {
				long soNgayQuaHan = (ngayHienTai.getTime() - hanSuDung.getTime()) / (1000 * 60 * 60 * 24);
				trangThaiHSD = "Lô thuốc này ĐÃ HẾT HẠN " + soNgayQuaHan + " ngày";
			} else {
				long soNgayConLai = (hanSuDung.getTime() - ngayHienTai.getTime()) / (1000 * 60 * 60 * 24);
				trangThaiHSD = "Lô thuốc này CÒN HẠN SỬ DỤNG (" + soNgayConLai + " ngày nữa)";
			}
		} catch(Exception ex) {
			trangThaiHSD = "Không xác định được hạn sử dụng";
		}
		
		CustomJOptionPane confirmPane = new CustomJOptionPane(this, 
			"Bạn có chắc chắn muốn XÓA VĨNH VIỄN chi tiết lô thuốc này?\n\n" +
			"Mã lô: " + maLo + "\n" +
			"Mã thuốc: " + maThuoc + "\n" +
			"Tên thuốc: " + tenThuoc + "\n" +
			"Ngày sản xuất: " + ngaySanXuatStr + "\n" +
			"Hạn sử dụng: " + hanSuDungStr + "\n" +
			"Số lượng tồn: " + soLuongTon + "\n\n" +
			trangThaiHSD + "\n\n" ,
			true);
		int confirm = confirmPane.show();
		
		if(confirm == javax.swing.JOptionPane.YES_OPTION) {
			boolean success = loThuocDAO.xoaChiTietLoThuoc(maLo, maThuoc);
			
			if(success) {
				CustomJOptionPane successPane = new CustomJOptionPane(this, 
					"Đã xóa vĩnh viễn chi tiết lô thuốc thành công!\n\n" +
					"Mã lô: " + maLo + "\n" +
					"Mã thuốc: " + maThuoc, 
					false);
				successPane.show();
				
				// Refresh lại dữ liệu
				String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
				if(loaiThongKe.equals("Thuốc đã hết hạn")) {
					loadLoThuocHetHan();
				} else {
					try {
						int soNgay = Integer.parseInt(txtSoNgay.getText().trim());
						loadLoThuocSapHetHan(soNgay);
					} catch(NumberFormatException ex) {
						loadLoThuocHetHan();
					}
				}
			} else {
				CustomJOptionPane errorPane = new CustomJOptionPane(this, 
					"Xóa chi tiết lô thuốc thất bại!\n\n" , 
					false);
				errorPane.show();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o == cboLoaiThongKe) {
			String loaiThongKe = (String) cboLoaiThongKe.getSelectedItem();
			if(loaiThongKe.equals("Thuốc sắp hết hạn")) {
				txtSoNgay.setEnabled(true);
				lblSoNgay.setEnabled(true);
			} else {
				txtSoNgay.setEnabled(false);
				lblSoNgay.setEnabled(true);
			}
		} else if(o == btnThongKe) {
			thongKe();
		} else if(o == btnLamMoi) {
			lamMoi();
		} else if(o == btnXoaLoThuoc) {
			xoaLoThuoc();
		}
	}
	

	@Override	
	public void mouseClicked(MouseEvent e) {
		Object o = e.getSource();

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
