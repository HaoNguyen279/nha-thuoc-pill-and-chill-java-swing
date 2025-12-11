package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

    private final Color PRIMARY_COLOR = new Color(0, 150, 136);
    private final Color BG_COLOR = new Color(245, 245, 245);
    private final Color ACCENT_COLOR = new Color(255, 255, 255);
    private final Color TEXT_COLOR = new Color(51, 51, 51);

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
        

        setBackground(BG_COLOR);
        setVisible(true);

        
	}
    

	private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        searchPanel.setBackground(BG_COLOR);

        searchField = new JTextField(25);
        searchField.setText("Nhập từ khóa...");
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        //PlaceHolder
        searchField.addFocusListener(new FocusListener() {
			
        	@Override
			public void focusGained(FocusEvent e) {
				if(searchField.getText().equals("Nhập từ khóa...")) {
					searchField.setText("");
                    searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    searchField.setForeground(TEXT_COLOR);
				}
			}
        	
			@Override
			public void focusLost(FocusEvent e) {
				if(searchField.getText().equals("")) {
					searchField.setText("Nhập từ khóa...");
                    searchField.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                    searchField.setForeground(Color.GRAY);
				}
			}
			
		});

        searchButton = new JButton("Tìm");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchButton.setPreferredSize(new Dimension(100, 40));
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //ComboBox
        String[] kieuTimKiem = {"Theo mã khách hàng", "Theo tên khách hàng", "Theo SĐT khách hàng"};
        cboTimKiem = new JComboBox<>(kieuTimKiem);
        cboTimKiem.setSelectedIndex(0);
        cboTimKiem.setPreferredSize(new Dimension(180, 40));
        cboTimKiem.setBackground(Color.WHITE);
        cboTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboTimKiem.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        //ComboBox Date
        JLabel lblThang = new JLabel("Tháng: ");
        lblThang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] thang = "1 2 3 4 5 6 7 8 9 10 11 12".split(" ");
        cboThang = new JComboBox<>(thang);
        LocalDate today = LocalDate.now();
        cboThang.setSelectedIndex(today.getMonthValue() - 1);
        cboThang.setPreferredSize(new Dimension(70, 40));
        cboThang.setBackground(Color.WHITE);
        cboThang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboThang.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel lblNam = new JLabel("Năm: ");
        lblNam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] nam = "2024 2025".split(" ");
        cboNam = new JComboBox<>(nam);
        cboNam.setSelectedIndex(1);
        cboNam.setPreferredSize(new Dimension(80, 40));
        cboNam.setBackground(Color.WHITE);
        cboNam.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboNam.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        radTimKiemThangNam = new JRadioButton("Lọc theo Tháng/Năm");
        radTimKiemThangNam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        radTimKiemThangNam.setBackground(BG_COLOR);
        radTimKiemThangNam.setFocusPainted(false);
        
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
        pnlTop.setBackground(BG_COLOR);
		
		tblThongKe = new JTable(dtmThongKe) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                return c;
            }
        };        
		tblThongKe.setBackground(Color.WHITE);
		tblThongKe.setGridColor(new Color(224, 224, 224));
		tblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tblThongKe.setRowHeight(35);
		tblThongKe.setSelectionBackground(new Color(178, 223, 219));
        tblThongKe.setSelectionForeground(Color.BLACK);
        tblThongKe.setShowGrid(true);
        tblThongKe.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = tblThongKe.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tblThongKe);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(0,50,5,50),
    		BorderFactory.createLineBorder(new Color(220, 220, 220))
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JLabel lblThongKe = new JLabel("THỐNG KÊ DOANH THU THEO KHÁCH HÀNG", SwingConstants.CENTER);
        lblThongKe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblThongKe.setForeground(PRIMARY_COLOR);
        lblThongKe.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        JPanel pnlTieuDe = new JPanel(new GridLayout(2,1,0,0));
        pnlTieuDe.setBackground(BG_COLOR);
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
