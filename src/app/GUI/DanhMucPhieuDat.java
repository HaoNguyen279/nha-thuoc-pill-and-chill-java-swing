package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import app.ConnectDB.ConnectDB;
import app.DAO.ChiTietHoaDonDAO;
import app.DAO.ChiTietPhieuDatDAO;
import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.DAO.NhanVienDAO;
import app.DAO.PhieuDatDAO;
import app.DAO.ThuocDAO;
import app.Entity.ChiTietHoaDon;
import app.Entity.ChiTietPhieuDat;
import app.Entity.HoaDon;
import app.Entity.KhachHang;
import app.Entity.NhanVien;
import app.Entity.PhieuDat;
import app.Entity.Thuoc;

public class DanhMucPhieuDat extends JPanel implements ActionListener, MouseListener{
	private JButton btnXuatPhieuDat;
	
	private DefaultTableModel dtmPhieuDat;
	private JTable tblPhieuDat;
	
	private ArrayList<PhieuDat> dsPhieuDat;

	private DefaultTableModel dtmChiTietPhieuDat;
	private JTable tblChiTietPhieuDat;
	
	private JButton searchButton;

	private JComboBox<String> cboTimKiem;

	private JComboBox<String> cboThang;

	private JComboBox<String> cboNam;

	private JRadioButton radTimKiemThangNam;

	private ArrayList<ChiTietPhieuDat> dsChiTietPhieuDat;

	private JTextField searchField;
	
	public DanhMucPhieuDat() {
		
        // Table init
        String[] colsPhieuDat = {"Mã phiếu đặt", "Tên NV", "Tên KH", "Ngày lập", "Đã nhận", "Ghi chú"};
        dtmPhieuDat = new DefaultTableModel(colsPhieuDat, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false, ngăn edit trên cell
               return false;
            }
        };
        
        String[] colsChiTietPhieuDat = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng"};
        dtmChiTietPhieuDat = new DefaultTableModel(colsChiTietPhieuDat, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false, ngăn edit trên cell
               return false;
            }
        };
        
        // Frame
        setLayout(new GridLayout(2,1,10,10));
        
        add(createTopPanel());
        
        add(createBotPanel());
        
        loadPhieuDatData();
        
        

        setBackground(new Color(248, 248, 248));
        setVisible(true);

        
	}
	
	public JPanel createBotPanel() {
		JPanel pnlBot = new JPanel();
		pnlBot.setLayout(new BorderLayout());
		
		tblChiTietPhieuDat = new JTable(dtmChiTietPhieuDat);        
		tblChiTietPhieuDat.setBackground(new Color(240, 240, 245));
		tblChiTietPhieuDat.setGridColor(Color.LIGHT_GRAY);
		tblChiTietPhieuDat.setFont(new Font("Arial", Font.PLAIN, 15));
		tblChiTietPhieuDat.setRowHeight(40);
		tblChiTietPhieuDat.setGridColor(Color.LIGHT_GRAY);
        tblChiTietPhieuDat.setSelectionBackground(new Color(220, 255, 220));
        JTableHeader header = tblChiTietPhieuDat.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(144, 238, 144));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblChiTietPhieuDat);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(00,50,5,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        JLabel lblPhieuDoiTra = new JLabel("CHI TIẾT PHIẾU ĐẶT", SwingConstants.CENTER);
        lblPhieuDoiTra.setFont(new Font("Arial", Font.BOLD, 24));
        lblPhieuDoiTra.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        pnlBot.add(lblPhieuDoiTra, BorderLayout.NORTH);
        pnlBot.add(scrollPane, BorderLayout.CENTER);
        
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        btnXuatPhieuDat = new JButton("Xuất phiếu đặt");
        btnXuatPhieuDat.setBackground(new Color(224, 248, 228));
        btnXuatPhieuDat.setPreferredSize(new Dimension(200, 50));
        btnXuatPhieuDat.setFocusPainted(false);
        
        pnlButton.add(btnXuatPhieuDat);
        pnlButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 50));
        pnlBot.add(pnlButton, BorderLayout.SOUTH);
        
        btnXuatPhieuDat.addActionListener(this);
        
        return pnlBot;
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
        String[] kieuTimKiem = {"Theo mã phiếu đặt", "Theo tên nhân viên", "Theo tên khách hàng"};
        cboTimKiem = new JComboBox<>(kieuTimKiem);
        cboTimKiem.setSelectedIndex(0);
        cboTimKiem.setPreferredSize(new Dimension(160, 30));
        cboTimKiem.setBackground(Color.WHITE);
        cboTimKiem.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //ComboBox Date
        JLabel lblThang = new JLabel("Tháng: ");
        String[] thang = "1 2 3 4 5 6 7 8 9 10 11 12".split(" ");
        cboThang = new JComboBox<>(thang);
        LocalDate today = LocalDate.now();
        cboThang.setSelectedIndex(today.getMonthValue() - 1);
        cboThang.setPreferredSize(new Dimension(60, 30));
        cboThang.setBackground(Color.WHITE);
        cboThang.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel lblNam = new JLabel("Năm: ");
        String nam[] = "2024 2025".split(" ");
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
        
        return searchPanel;
    }
	
	public JPanel createTopPanel() {
		
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		
		tblPhieuDat = new JTable(dtmPhieuDat);        
        tblPhieuDat.setBackground(new Color(240, 240, 245));
        tblPhieuDat.setGridColor(Color.LIGHT_GRAY);
        tblPhieuDat.setFont(new Font("Arial", Font.PLAIN, 15));
        tblPhieuDat.setRowHeight(40);
        tblPhieuDat.setGridColor(Color.LIGHT_GRAY);
        tblPhieuDat.setSelectionBackground(new Color(220, 255, 220));
        JTableHeader header = tblPhieuDat.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(144, 238, 144));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblPhieuDat);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(0,50,5,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        JLabel lblPhieuDat = new JLabel("PHIẾU ĐẶT", SwingConstants.CENTER);
        lblPhieuDat.setFont(new Font("Arial", Font.BOLD, 24));
        lblPhieuDat.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        JPanel pnlTieuDe = new JPanel(new GridLayout(2,1,0,0));
        pnlTieuDe.add(lblPhieuDat);
        pnlTieuDe.add(createSearchPanel());
        
        
        pnlTop.add(pnlTieuDe, BorderLayout.NORTH);
        pnlTop.add(scrollPane, BorderLayout.CENTER);
        
        tblPhieuDat.addMouseListener(this);
        cboNam.addActionListener(this);
        cboThang.addActionListener(this);
        searchButton.addActionListener(this);
        radTimKiemThangNam.addActionListener(this);
        
        return pnlTop;

	}
	
	public void loadPhieuDatData() {
		ConnectDB.connect();
		PhieuDatDAO pdDAO = new PhieuDatDAO();
		dsPhieuDat = pdDAO.getAllPhieuDat();
		dtmPhieuDat.setRowCount(0);
		dtmChiTietPhieuDat.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDat pd : dsPhieuDat) {
			String trangThaiNhan = pd.isReceived() ? "Đã nhận" : "Chưa nhận";
			Object[] rowData = {
					pd.getMaPhieuDat(),
					pd.getMaNV(),
					pd.getMaKH(),
					sdf.format(pd.getNgayDat()),
					trangThaiNhan,
					pd.getGhiChu()
			};
			dtmPhieuDat.addRow(rowData);
		}
	}
	
	public void loadChiTietPhieuDatData(String mapd) {
		ConnectDB.connect();
		ChiTietPhieuDatDAO ctDAO = new ChiTietPhieuDatDAO();
		dsChiTietPhieuDat = ctDAO.getAllByPhieuDatId(mapd);
		dtmChiTietPhieuDat.setRowCount(0);
		for(ChiTietPhieuDat ct : dsChiTietPhieuDat) {
			Object[] rowData = {
					ct.getMaThuoc(),
					ct.getTenThuoc(),
					ct.getMaLo(),
					ct.getSoLuong(),
			};
			dtmChiTietPhieuDat.addRow(rowData);
		}
	}
	
	public void timKiemPhieuDatTheoThangNam() {
		ConnectDB.connect();
        PhieuDatDAO hdDAO = new PhieuDatDAO();
        String txtNam = (String) cboNam.getSelectedItem();
		int nam = Integer.parseInt(txtNam);
        dsPhieuDat = hdDAO.findPhieuDatByThangNam(cboThang.getSelectedIndex() + 1, nam);
        dtmPhieuDat.setRowCount(0);
        dtmChiTietPhieuDat.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDat pd : dsPhieuDat) {
			String trangThaiNhan = pd.isReceived() ? "Đã nhận" : "Chưa nhận";
			Object[] rowData = {
					pd.getMaPhieuDat(),
					pd.getMaNV(),
					pd.getMaKH(),
					sdf.format(pd.getNgayDat()),
					trangThaiNhan,
					pd.getGhiChu()
			};
			dtmPhieuDat.addRow(rowData);
		}
	}
	
	public void timKiemPhieuDatTheoMaPhieuDat() {
		String maPhieuDat = searchField.getText();
		dtmPhieuDat.setRowCount(0);
		dtmChiTietPhieuDat.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDat pd : dsPhieuDat) {
			if(pd.getMaPhieuDat().equals(maPhieuDat)) {
				String trangThaiNhan = pd.isReceived() ? "Đã nhận" : "Chưa nhận";
				Object[] rowData = {
						pd.getMaPhieuDat(),
						pd.getMaNV(),
						pd.getMaKH(),
						sdf.format(pd.getNgayDat()),
						trangThaiNhan,
						pd.getGhiChu()
				};
				dtmPhieuDat.addRow(rowData);
			}
		}
	}
	
	public void timKiemPhieuDatTheoTenNhanVien() {
		String tenNhanVien = searchField.getText().toLowerCase().trim();
		dtmPhieuDat.setRowCount(0);
		dtmChiTietPhieuDat.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDat pd : dsPhieuDat) {
			if(tenNhanVien != null && pd.getMaNV().toLowerCase().matches(".*"+tenNhanVien+".*")) {
				String trangThaiNhan = pd.isReceived() ? "Đã nhận" : "Chưa nhận";
				Object[] rowData = {
						pd.getMaPhieuDat(),
						pd.getMaNV(),
						pd.getMaKH(),
						sdf.format(pd.getNgayDat()),
						trangThaiNhan,
						pd.getGhiChu()
				};
				dtmPhieuDat.addRow(rowData);
			}
		}
	}
	
	public void timKiemPhieuDatTheoTenKhachHang() {
		String tenKhachHang = searchField.getText().toLowerCase().trim();
		dtmPhieuDat.setRowCount(0);
		dtmChiTietPhieuDat.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDat pd : dsPhieuDat) {
			if(tenKhachHang != null && pd.getMaKH().toLowerCase().matches(".*"+tenKhachHang+".*")) {
				String trangThaiNhan = pd.isReceived() ? "Đã nhận" : "Chưa nhận";
				Object[] rowData = {
						pd.getMaPhieuDat(),
						pd.getMaNV(),
						pd.getMaKH(),
						sdf.format(pd.getNgayDat()),
						trangThaiNhan,
						pd.getGhiChu()
				};
				dtmPhieuDat.addRow(rowData);
			}
		}
	}
	
	private void xuatPhieuDatPDF() {
		try {
            // Sử dụng đường dẫn mặc định để lưu file
            String defaultDir = "D:\\PTUD\\PDF\\PhieuDat";
            File directory = new File(defaultDir);
            
            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            int selectedRow = tblPhieuDat.getSelectedRow();
            String maPhieuDat= tblPhieuDat.getValueAt(selectedRow, 0).toString();
            
            //Lấy dữ liệu hóa đơn đang chọn
            PhieuDatDAO hdDAO = new PhieuDatDAO();
            PhieuDat hd = hdDAO.getPhieuDatById(maPhieuDat);
            
            String fileName = "PhieuDat_" + maPhieuDat + ".pdf";
            String filePath = defaultDir + "\\" + fileName;
            
            // Tạo document và writer
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // Font chữ
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(baseFont, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(baseFont, 11, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font totalFont = new com.itextpdf.text.Font(baseFont, 12, com.itextpdf.text.Font.BOLD);
            
            // Tiêu đề
            Paragraph title = new Paragraph("PHIẾU ĐẶT THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Thông tin nhà thuốc
            Paragraph shopInfo = new Paragraph("NHÀ THUỐC PILL & CHILL\nĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\nHotline: 0987654321", normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            // Thông tin hóa đơn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            
            Paragraph invoiceInfo = new Paragraph();
            invoiceInfo.add(new Chunk("Mã phiếu đặt: " + maPhieuDat + "\n", headerFont));
            invoiceInfo.add(new Chunk("Ngày đặt: " + currentDate + "\n", normalFont));
            
            // Lấy thông tin nhân viên
            String tenNhanVien = "Nhân viên";
            try {
                NhanVienDAO nvDAO = new NhanVienDAO();
                NhanVien nv = nvDAO.getNhanVienById(hd.getMaNV());
                if (nv != null) {
                    tenNhanVien = nv.getTenNV();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            invoiceInfo.add(new Chunk("Nhân viên lập: " + tenNhanVien + " (" + hd.getMaNV() + ")\n", normalFont));
            
            // Lấy thông tin khách hàng nếu có
            if (hd.getMaKH() != null && !hd.getMaKH().isEmpty()) {
                String tenKhachHang = "Khách hàng";
                String sdtKhachHang = "";
                try {
                    KhachHangDAO khDAO = new KhachHangDAO();
                    KhachHang kh = khDAO.getKhachHangById(hd.getMaKH());
                    if (kh != null) {
                        tenKhachHang = kh.getTenKH();
                        sdtKhachHang = kh.getSoDienThoai();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                invoiceInfo.add(new Chunk("Khách hàng: " + tenKhachHang + "\n", normalFont));
                invoiceInfo.add(new Chunk("Số điện thoại: " + sdtKhachHang + "\n", normalFont));
            }
            
            document.add(invoiceInfo);
            document.add(new Paragraph("\n"));
            
            // Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(3); // 5 cột
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1f, 3f, 1f};
            table.setWidths(columnWidths);
            
            // Header của bảng
            addTableHeader(table, headerFont);
            
            // Nội dung bảng
            DecimalFormat df = new DecimalFormat("#,###");
            double tongTien = 0;
            
            for (ChiTietPhieuDat item : dsChiTietPhieuDat) {
            	ThuocDAO thuocDAO = new ThuocDAO();
            	Thuoc thuoc = thuocDAO.getThuocById(item.getMaThuoc()); 
            	
                String maThuoc = item.getMaThuoc();
                String tenThuoc = thuoc.getTenThuoc();
                int soLuong = item.getSoLuong();
                
                table.addCell(new PdfPCell(new Phrase(maThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(tenThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(soLuong), normalFont)));
            }
            
            document.add(table);
            
            // Thông tin tổng tiền
            // Sử dụng mức thuế mặc định là 10%
            
            
            double tyLeThue = 0.10;
            String tenThue = "VAT (10%)";
            double tienThue = 0;
            double tongThanhToan = 0;
            
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng tiền hàng: " + df.format(tongTien) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Thuế " + tenThue + ": " + df.format(tienThue) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Tổng thanh toán: " + df.format(tongThanhToan) + " VNĐ\n", totalFont));
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.add(new Paragraph("\n"));
            
            // Chữ ký
            Paragraph signature = new Paragraph();
            signature.add(new Chunk("Xác nhận của khách hàng                                                             Người lập phiếu đặt\n\n\n\n", normalFont));
            signature.add(new Chunk("                                                                                             " + tenNhanVien, normalFont));
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);
            
            // Thêm ghi chú từ form nếu có
            String ghiChuText;
            if(hd.getGhiChu() != null) {
            	ghiChuText = hd.getGhiChu();
            }else {
                ghiChuText = "";
            }


            if (!ghiChuText.isEmpty()) {
                document.add(new Paragraph("\n"));
                Paragraph ghiChuParagraph = new Paragraph("Ghi chú: " + ghiChuText, normalFont);
                ghiChuParagraph.setAlignment(Element.ALIGN_LEFT);
                document.add(ghiChuParagraph);
            }
            
            // Lưu ý
            Paragraph note = new Paragraph("\n\nLưu ý: \n- Phiếu đặt chỉ có thể xuất trong ngày.", normalFont);
            document.add(note);
            
            document.close();
            
            // Thông báo thành công
            JOptionPane.showMessageDialog(this, 
                "Đã xuất phiếu đặt PDF thành công!\nVị trí: " + filePath, 
                "Xuất phiếu đặt", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Tự động mở file PDF sau khi xuất
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    // Sử dụng Desktop API để mở file với ứng dụng mặc định
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        // Nếu Desktop API không được hỗ trợ, dùng ProcessBuilder để mở
                        new ProcessBuilder("cmd", "/c", "start", "", filePath).start();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Không thể tự động mở file PDF: " + ex.getMessage(), 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi xuất phiếu đặt PDF: " + e.getMessage() + 
                "\nVui lòng kiểm tra đường dẫn: E:\\PTUD\\PDF\\PHIEUDAT", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
		
	}
	
	private void addTableHeader(PdfPTable table, com.itextpdf.text.Font font) {
		String[] headers = {"Mã thuốc", "Tên thuốc", "Số lượng"};
		
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(new BaseColor(240, 240, 240));
			cell.setPadding(3);
			table.addCell(cell);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		
		if (o == searchButton) {
			System.out.println("OK");
			if (cboTimKiem.getSelectedIndex() == 0) {
				timKiemPhieuDatTheoMaPhieuDat();
			} else if (cboTimKiem.getSelectedIndex() == 1) {
				timKiemPhieuDatTheoTenNhanVien();
			} else if (cboTimKiem.getSelectedIndex() == 2) {
				timKiemPhieuDatTheoTenKhachHang();
			}
			
		} else if (o == radTimKiemThangNam) {
			if (radTimKiemThangNam.isSelected()) {
				timKiemPhieuDatTheoThangNam();
			} else if (!radTimKiemThangNam.isSelected()) {
				loadPhieuDatData();
			} 
		} else if (o == cboThang || o == cboNam) {
			if (radTimKiemThangNam.isSelected()) {
				timKiemPhieuDatTheoThangNam();
			}
		} else if (o == btnXuatPhieuDat) {
			xuatPhieuDatPDF();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblPhieuDat.getSelectedRow();
        if (row != -1) {
            String maPhieuDat = tblPhieuDat.getValueAt(row, 0).toString();
            System.out.println(maPhieuDat);
            loadChiTietPhieuDatData(maPhieuDat);
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
