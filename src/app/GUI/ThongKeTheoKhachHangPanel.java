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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import app.ConnectDB.ConnectDB;
import app.DAO.ChiTietHoaDonDAO;
import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.Entity.ChiTietHoaDon;
import app.Entity.HoaDon;
import app.Entity.KhachHang;

public class ThongKeTheoKhachHangPanel extends JPanel implements ActionListener{

	private DefaultTableModel dtmThongKe;
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox cboTimKiem;
	private JComboBox cboThang;
	private JComboBox cboNam;
	private JRadioButton radTimKiemThangNam;
	private JTable tblThongKe;
	private ArrayList<KhachHang> dsKhachHang;
	private ArrayList<ChiTietHoaDon> dsChiTietHoaDon;
	private ArrayList<HoaDon> dsHoaDon;

	public ThongKeTheoKhachHangPanel() {
		// Table init
        String[] colsThongKe = {"Mã khách hàng", "Tên khách hàng", "SĐT khách hàng", "Tổng tiền"};
        dtmThongKe = new DefaultTableModel(colsThongKe, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false, ngăn edit trên cell
               return false;
            }
        };
		
        // Frame
        setLayout(new GridLayout(1,1,10,10));
        add(createTopPanel());
        
        loadKhachHangData();
        

        setBackground(new Color(248, 248, 248));
        setVisible(true);

        
	}
    

	private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));

        searchField = new JTextField(25);
        searchField.setText("Nhập từ khóa...");
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.ITALIC, 13));
        searchField.setBackground(new Color(245, 245, 245));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        //PlaceHolder
        searchField.addFocusListener(new FocusListener() {
			
        	@Override
			public void focusGained(FocusEvent e) {
				if(searchField.getText().equals("Nhập từ khóa...")) {
					searchField.setText("");
				}
			}
        	
			@Override
			public void focusLost(FocusEvent e) {
				if(searchField.getText().equals("")) {
					searchField.setText("Nhập từ khóa...");
				}
			}
			
		});

        searchButton = new JButton("Tìm");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 13));
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //ComboBox
        String[] kieuTimKiem = {"Theo mã khách hàng", "Theo tên khách hàng", "Theo SĐT khách hàng"};
        cboTimKiem = new JComboBox<>(kieuTimKiem);
        cboTimKiem.setSelectedIndex(0);
        cboTimKiem.setPreferredSize(new Dimension(160, 30));
        cboTimKiem.setBackground(Color.WHITE);
        cboTimKiem.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //ComboBox Date
        JLabel lblThang = new JLabel("Tháng: ");
        lblThang.setFont(new Font("Arial", Font.BOLD, 16));
        String[] thang = "1 2 3 4 5 6 7 8 9 10 11 12".split(" ");
        cboThang = new JComboBox<>(thang);
        LocalDate today = LocalDate.now();
        cboThang.setSelectedIndex(today.getMonthValue() - 1);
        cboThang.setPreferredSize(new Dimension(60, 30));
        cboThang.setBackground(Color.WHITE);
        cboThang.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel lblNam = new JLabel("Năm: ");
        lblNam.setFont(new Font("Arial", Font.BOLD, 16));
        String[] nam = "2024 2025".split(" ");
        cboNam = new JComboBox<>(nam);
        cboNam.setSelectedIndex(1);
        cboNam.setPreferredSize(new Dimension(60, 30));
        cboNam.setBackground(Color.WHITE);
        cboNam.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        radTimKiemThangNam = new JRadioButton("Lọc theo Tháng/Năm");
        radTimKiemThangNam.setFont(new Font("Arial", Font.BOLD, 16));
        
        searchPanel.add(Box.createHorizontalStrut(15));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(cboTimKiem);
        searchPanel.add(radTimKiemThangNam);
        searchPanel.add(lblThang);
        searchPanel.add(cboThang);
        searchPanel.add(lblNam);
        searchPanel.add(cboNam);
        
        searchButton.addActionListener(this);
        cboThang.addActionListener(this);
        cboNam.addActionListener(this);
        radTimKiemThangNam.addActionListener(this);
        
        return searchPanel;
    }
	
	public JPanel createTopPanel() {
		
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		
		tblThongKe = new JTable(dtmThongKe);        
		tblThongKe.setBackground(new Color(240, 240, 245));
		tblThongKe.setGridColor(Color.LIGHT_GRAY);
		tblThongKe.setFont(new Font("Arial", Font.PLAIN, 15));
		tblThongKe.setRowHeight(40);
		tblThongKe.setGridColor(Color.LIGHT_GRAY);
		tblThongKe.setSelectionBackground(new Color(100, 149, 237));
        JTableHeader header = tblThongKe.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(192, 232, 246));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblThongKe);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(0,50,5,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        JLabel lblThongKe = new JLabel("Thống kê doanh thu theo Khách hàng", SwingConstants.CENTER);
        lblThongKe.setFont(new Font("Arial", Font.BOLD, 24));
        lblThongKe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        JPanel pnlTieuDe = new JPanel(new GridLayout(2,1,0,0));
        pnlTieuDe.add(lblThongKe);
        pnlTieuDe.add(createSearchPanel());
        
        pnlTop.add(pnlTieuDe, BorderLayout.NORTH);
        pnlTop.add(scrollPane, BorderLayout.CENTER);
        
        return pnlTop;

	}
	
	public void loadKhachHangData() {
	    ConnectDB.getInstance().connect();
	    KhachHangDAO khDAO = new KhachHangDAO();
	    dsKhachHang = khDAO.getAllKhachHang();
	    Map<String, Double> mapDoanhThu = khDAO.thongKeTongDoanhThuTatCaKhachHang();
	    dtmThongKe.setRowCount(0);
	    DecimalFormat df = new DecimalFormat("#,###");
	    for (KhachHang kh : dsKhachHang) {
	    	double tongTien = mapDoanhThu.getOrDefault(kh.getMaKH(), 0.0);

	    	String tongTienFormatted = df.format(tongTien) + "VNĐ";
	    	
	        Object[] rowData = {
	            kh.getMaKH(),
	            kh.getTenKH(),
	            kh.getSoDienThoai(),
	            tongTienFormatted
	        };

	        dtmThongKe.addRow(rowData);
	    }
	}

	public void loadKhachHangDataTheoThangNam() {
	    int thang = cboThang.getSelectedIndex() + 1;
	    String txtNam = (String) cboNam.getSelectedItem();
	    int nam = Integer.parseInt(txtNam);

	    ConnectDB.getInstance().connect();
	    KhachHangDAO khDAO = new KhachHangDAO();
	    dsKhachHang = khDAO.getAllKhachHang();

	    Map<String, Double> mapDoanhThu = khDAO.thongKeTongDoanhThuKhachHangTheoThangNam(thang, nam);

	    DecimalFormat df = new DecimalFormat("#,###");
	    dtmThongKe.setRowCount(0);

	    int dem = 0;
	    for (KhachHang kh : dsKhachHang) {
	        double tongTien = mapDoanhThu.getOrDefault(kh.getMaKH(), 0.0);
	        if (tongTien > 0) {
	            String tongTienFormatted = df.format(tongTien) + " VNĐ";

	            Object[] rowData = {
	                kh.getMaKH(),
	                kh.getTenKH(),
	                kh.getSoDienThoai(),
	                tongTienFormatted
	            };

	            dtmThongKe.addRow(rowData);
	            dem++;
	        }
	    }
	    if (dem == 0) {
	        JOptionPane.showMessageDialog(this, 
	            "Không có khách hàng nào có hóa đơn trong tháng " + thang + "/" + nam);
	    }
	}


	public void loadKhachHangDataTheoMaKH() {
		String maKH = searchField.getText();
	    ConnectDB.getInstance().connect();
	    KhachHangDAO khDAO = new KhachHangDAO();
	    KhachHang kh = khDAO.getKhachHangById(maKH);
	    dtmThongKe.setRowCount(0);

	    if (kh != null) {
	        double tongTien = khDAO.timKiemDoanhThuKhachHang(maKH);
	        DecimalFormat df = new DecimalFormat("#,###");
	        String tongTienFormatted = df.format(tongTien) + " VNĐ";
	        Object[] rowData = {
	            kh.getMaKH(),
	            kh.getTenKH(),
	            kh.getSoDienThoai(),
	            tongTienFormatted
	        };
	        dtmThongKe.addRow(rowData);
	    } else {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã: " + maKH);
	    }
	}
	
	public void loadKhachHangDataTheoTen() {
		String tenKH = searchField.getText();
	    ConnectDB.getInstance().connect();
	    KhachHangDAO khDAO = new KhachHangDAO();
	    ArrayList<KhachHang> dsKhachHang = khDAO.findKhachHang(tenKH);
	    Map<String, Double> mapDoanhThu = khDAO.thongKeTongDoanhThuTatCaKhachHang();
	    dtmThongKe.setRowCount(0);

	    if (dsKhachHang.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có tên: " + tenKH);
	        return;
	    }

	    DecimalFormat df = new DecimalFormat("#,###");
	    for (KhachHang kh : dsKhachHang) {
	        double tongTien = mapDoanhThu.getOrDefault(kh.getMaKH(), 0.0);
	        String tongTienFormatted = df.format(tongTien) + " VNĐ";

	        Object[] rowData = {
	            kh.getMaKH(),
	            kh.getTenKH(),
	            kh.getSoDienThoai(),
	            tongTienFormatted
	        };
	        dtmThongKe.addRow(rowData);
	    }
	}

	public void loadKhachHangDataTheoSoDienThoai() {
		String soDienThoai = searchField.getText();
	    ConnectDB.getInstance().connect();
	    KhachHangDAO khDAO = new KhachHangDAO();
	    KhachHang kh = khDAO.findKhachHangByPhone(soDienThoai);
	    dtmThongKe.setRowCount(0);

	    if (kh == null) {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có số điện thoại: " + soDienThoai);
	        return;
	    }
	    double tongTien = khDAO.timKiemDoanhThuKhachHang(kh.getMaKH());
	    DecimalFormat df = new DecimalFormat("#,###");
	    String tongTienFormatted = df.format(tongTien) + " VNĐ";

	    Object[] rowData = {
	        kh.getMaKH(),
	        kh.getTenKH(),
	        kh.getSoDienThoai(),
	        tongTienFormatted
	    };

	    dtmThongKe.addRow(rowData);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == searchButton) {
			System.out.println("OK");
			if (cboTimKiem.getSelectedIndex() == 0) {
				loadKhachHangDataTheoMaKH();
			} else if (cboTimKiem.getSelectedIndex() == 1) {
				loadKhachHangDataTheoTen();
			} else if (cboTimKiem.getSelectedIndex() == 2) {
				loadKhachHangDataTheoSoDienThoai();
			}
			
		} else if (o == radTimKiemThangNam) {
			if (radTimKiemThangNam.isSelected()) {
				loadKhachHangDataTheoThangNam();
			} else if (!radTimKiemThangNam.isSelected()) {
				loadKhachHangData();
			} 
		} else if (o == cboThang || o == cboNam) {
			if (radTimKiemThangNam.isSelected()) {
				loadKhachHangDataTheoThangNam();
			}
		} 
	}
	
}
