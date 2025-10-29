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
import app.DAO.ChiTietPhieuDoiTraDAO;
import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.DAO.NhanVienDAO;
import app.DAO.PhieuDoiTraDAO;
import app.DAO.ThuocDAO;
import app.Entity.ChiTietHoaDon;
import app.Entity.ChiTietPhieuDoiTra;
import app.Entity.HoaDon;
import app.Entity.KhachHang;
import app.Entity.NhanVien;
import app.Entity.PhieuDoiTra;
import app.Entity.Thuoc;

public class DanhMucPhieuDoiTra extends JPanel implements ActionListener, MouseListener{
	private JButton btnXuatPhieuDoiTra;
	
	private DefaultTableModel dtmPhieuDoiTra;
	private JTable tblPhieuDoiTra;
	
	private ArrayList<PhieuDoiTra> dsPhieuDoiTra;
	
	private DefaultTableModel dtmChiTietPhieuDoiTra;
	private JTable tblChiTietPhieuDoiTra;

	private JButton searchButton;

	private JComboBox<String> cboTimKiem;

	private JComboBox<String> cboThang;

	private JComboBox<String> cboNam;

	private ArrayList<ChiTietPhieuDoiTra> dsChiTietPhieuDoiTra;

	private JTextField searchField;

	private JRadioButton radTimKiemThangNam;
	
	public DanhMucPhieuDoiTra() {
		
        // Table init
        String[] colsPhieuDoiTra = {"Mã phiếu đổi trả" , "Tên NV", "Tên KH", "Ngày đổi"};
        dtmPhieuDoiTra = new DefaultTableModel(colsPhieuDoiTra, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false, ngăn edit trên cell
               return false;
            }
        };
        
        String[] colsChiTietPhieuDoiTra = {"Mã thuốc", "Tên thuốc", "Mã lô", "Số lượng", "Đơn giá", "Lý do"};
        dtmChiTietPhieuDoiTra = new DefaultTableModel(colsChiTietPhieuDoiTra, 0) {
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
        
        loadPhieuDoiTraData();
        
        
        setBackground(new Color(248, 248, 248));
        setVisible(true);

        
	}
	
	public JPanel createBotPanel() {
		JPanel pnlBot = new JPanel();
		pnlBot.setLayout(new BorderLayout());
		
		tblChiTietPhieuDoiTra = new JTable(dtmChiTietPhieuDoiTra);        
		tblChiTietPhieuDoiTra.setBackground(new Color(240, 240, 245));
		tblChiTietPhieuDoiTra.setGridColor(Color.LIGHT_GRAY);
		tblChiTietPhieuDoiTra.setFont(new Font("Arial", Font.PLAIN, 15));
		tblChiTietPhieuDoiTra.setRowHeight(40);
		tblChiTietPhieuDoiTra.setGridColor(Color.LIGHT_GRAY);
        tblChiTietPhieuDoiTra.setSelectionBackground(new Color(220, 255, 220));
        JTableHeader header = tblChiTietPhieuDoiTra.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(144, 238, 144));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblChiTietPhieuDoiTra);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(00,50,5,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        JLabel lblPhieuDoiTra = new JLabel("CHI TIẾT PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        lblPhieuDoiTra.setFont(new Font("Arial", Font.BOLD, 24));
        lblPhieuDoiTra.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        pnlBot.add(lblPhieuDoiTra, BorderLayout.NORTH);
        pnlBot.add(scrollPane, BorderLayout.CENTER);
        
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        btnXuatPhieuDoiTra = new JButton("Xuất phiếu đổi trả");
        btnXuatPhieuDoiTra.setBackground(new Color(224, 248, 228));
        btnXuatPhieuDoiTra.setPreferredSize(new Dimension(200, 50));
        btnXuatPhieuDoiTra.setFocusPainted(false);
        
        pnlButton.add(btnXuatPhieuDoiTra);
        pnlButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 50));
        pnlBot.add(pnlButton, BorderLayout.SOUTH);
        
        btnXuatPhieuDoiTra.addActionListener(this);
        
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
        String[] kieuTimKiem = {"Theo mã phiếu đổi trả", "Theo tên nhân viên", "Theo tên khách hàng"};
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
        int namHienTai = Year.now().getValue();
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

        radTimKiemThangNam.addActionListener(this);
        searchButton.addActionListener(this);
        cboThang.addActionListener(this);
        cboNam.addActionListener(this);
        
        return searchPanel;
    }
	
	public JPanel createTopPanel() {
		
		JPanel pnlTop = new JPanel();
		pnlTop.setLayout(new BorderLayout());
		
		tblPhieuDoiTra = new JTable(dtmPhieuDoiTra);        
        tblPhieuDoiTra.setBackground(new Color(240, 240, 245));
        tblPhieuDoiTra.setGridColor(Color.LIGHT_GRAY);
        tblPhieuDoiTra.setFont(new Font("Arial", Font.PLAIN, 15));
        tblPhieuDoiTra.setRowHeight(40);
        tblPhieuDoiTra.setGridColor(Color.LIGHT_GRAY);
        tblPhieuDoiTra.setSelectionBackground(new Color(220, 255, 220));
        JTableHeader header = tblPhieuDoiTra.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        header.setBackground(new Color(144, 238, 144));
        header.setFont(new Font("Arial", Font.BOLD, 15));
        JScrollPane scrollPane = new JScrollPane(tblPhieuDoiTra);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
    		BorderFactory.createEmptyBorder(0,50,5,50),
    		BorderFactory.createLineBorder(Color.GRAY, 2)
        ));
        
        JLabel lblPhieuDoiTra = new JLabel("PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        lblPhieuDoiTra.setFont(new Font("Arial", Font.BOLD, 24));
        lblPhieuDoiTra.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        
        JPanel pnlTieuDe = new JPanel(new GridLayout(2,1,0,0));
        pnlTieuDe.add(lblPhieuDoiTra);
        pnlTieuDe.add(createSearchPanel());
        
        pnlTop.add(pnlTieuDe, BorderLayout.NORTH);
        pnlTop.add(scrollPane, BorderLayout.CENTER);
        
        tblPhieuDoiTra.addMouseListener(this);
        
        return pnlTop;

	}
	
	public void loadPhieuDoiTraData() {
		ConnectDB.getInstance().connect();
		PhieuDoiTraDAO pdtDAO = new PhieuDoiTraDAO();
		dsPhieuDoiTra = pdtDAO.getAllHoaDon5Field();
		dtmPhieuDoiTra.setRowCount(0);
		dtmChiTietPhieuDoiTra.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDoiTra pdt : dsPhieuDoiTra) {
			Object[] rowData = {
					pdt.getMaPhieuDoiTra(),
					pdt.getMaNV(),
					pdt.getMaKH(),
					sdf.format(pdt.getNgayDoiTra())
			};
			dtmPhieuDoiTra.addRow(rowData);
		}
	}
	
	public void loadChiTietPhieuDoiTraData(String mapdt) {
		ConnectDB.getInstance().connect();
		ChiTietPhieuDoiTraDAO ctDAO = new ChiTietPhieuDoiTraDAO();
		dsChiTietPhieuDoiTra = ctDAO.getChiTietByMaPhieuDoiTra(mapdt);
		dtmChiTietPhieuDoiTra.setRowCount(0);
		for(ChiTietPhieuDoiTra ct : dsChiTietPhieuDoiTra) {
			Object[] rowData = {
					ct.getMaThuoc(),
					ct.getMaPhieuDoiTra(),
					ct.getMaLo(),
					ct.getSoLuong(),
					ct.getDonGia()
			};
			dtmChiTietPhieuDoiTra.addRow(rowData);
		}
	}
	
	public void timKiemPhieuDoiTraTheoThangNam() {
		ConnectDB.getInstance().connect();
        PhieuDoiTraDAO pdtDAO = new PhieuDoiTraDAO();
        String txtNam = (String) cboNam.getSelectedItem();
		int nam = Integer.parseInt(txtNam);
        dsPhieuDoiTra = pdtDAO.findPhieuDoiTraByThangNam(cboThang.getSelectedIndex() + 1, nam);
        dtmPhieuDoiTra.setRowCount(0);
        dtmChiTietPhieuDoiTra.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDoiTra pdt : dsPhieuDoiTra) {
			Object[] rowData = {
					pdt.getMaPhieuDoiTra(),
					pdt.getMaNV(),
					pdt.getMaKH(),
					sdf.format(pdt.getNgayDoiTra())
			};
			dtmPhieuDoiTra.addRow(rowData);
		}
	}
	
	public void timKiemPhieuDoiTraTheoMaPhieuDoiTra() {
		String maPhieuDoiTra = searchField.getText();
		dtmPhieuDoiTra.setRowCount(0);
		dtmChiTietPhieuDoiTra.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDoiTra pdt : dsPhieuDoiTra) {
			if(pdt.getMaPhieuDoiTra().equals(maPhieuDoiTra)) {
				Object[] rowData = {
						pdt.getMaPhieuDoiTra(),
						pdt.getMaNV(),
						pdt.getMaKH(),
						sdf.format(pdt.getNgayDoiTra())
				};
				dtmPhieuDoiTra.addRow(rowData);
			}
		}
	}
	
	public void timKiemPhieuDoiTraTheoTenNhanVien() {
		String tenNhanVien = searchField.getText().toLowerCase().trim();
		dtmPhieuDoiTra.setRowCount(0);
		dtmChiTietPhieuDoiTra.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDoiTra pdt : dsPhieuDoiTra) {
			if(tenNhanVien != null && pdt.getMaNV().toLowerCase().matches(".*"+tenNhanVien+".*")) {
				Object[] rowData = {
						pdt.getMaPhieuDoiTra(),
						pdt.getMaNV(),
						pdt.getMaKH(),
						sdf.format(pdt.getNgayDoiTra())
				};
				dtmPhieuDoiTra.addRow(rowData);
			}
		}
	}
	
	public void timKiemPhieuDoiTraTheoTenKhachHang() {
		String tenKhachHang = searchField.getText().toLowerCase().trim();
		dtmPhieuDoiTra.setRowCount(0);
		dtmChiTietPhieuDoiTra.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for(PhieuDoiTra pdt : dsPhieuDoiTra) {
			if(tenKhachHang != null && pdt.getMaKH().toLowerCase().matches(".*"+tenKhachHang+".*")) {
				Object[] rowData = {
						pdt.getMaPhieuDoiTra(),
						pdt.getMaNV(),
						pdt.getMaKH(),
						sdf.format(pdt.getNgayDoiTra())
				};
				dtmPhieuDoiTra.addRow(rowData);
			}
		}
	}
	private void xuatPhieuDoiTraPDF() {
		try {
            // Sử dụng đường dẫn mặc định để lưu file
            String defaultDir = "D:\\PTUD\\PDF\\PhieuDoiTra";
            File directory = new File(defaultDir);
            
            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            int selectedRow = tblPhieuDoiTra.getSelectedRow();
            String maPhieuDoiTra= tblPhieuDoiTra.getValueAt(selectedRow, 0).toString();
            
            //Lấy dữ liệu hóa đơn đang chọn
            PhieuDoiTraDAO pdtDAO = new PhieuDoiTraDAO();
            PhieuDoiTra pdt = pdtDAO.getPhieuDoiTraById(maPhieuDoiTra);
            
            String fileName = "PhieuDoiTra" + maPhieuDoiTra + ".pdf";
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
            Paragraph title = new Paragraph("PHIẾU ĐỔI TRẢ THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Thông tin nhà thuốc
            Paragraph shopInfo = new Paragraph("NHÀ THUỐC PILL & CHILL\nĐịa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\nHotline: 0987654321", normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            // Thông tin hóa đơn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = dateFormat.format(new Date());
            
            Paragraph invoiceInfo = new Paragraph();
            invoiceInfo.add(new Chunk("Mã hóa đơn: " + maPhieuDoiTra + "\n", headerFont));
            invoiceInfo.add(new Chunk("Ngày lập: " + currentDate + "\n", normalFont));
            
            // Lấy thông tin nhân viên
            String tenNhanVien = "Nhân viên";
            try {
                NhanVienDAO nvDAO = new NhanVienDAO();
                NhanVien nv = nvDAO.getNhanVienById(pdt.getMaNV());
                if (nv != null) {
                    tenNhanVien = nv.getTenNV();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            invoiceInfo.add(new Chunk("Nhân viên lập: " + tenNhanVien + " (" + pdt.getMaNV() + ")\n", normalFont));
            
            // Lấy thông tin khách hàng nếu có
            if (pdt.getMaKH() != null && !pdt.getMaKH().isEmpty()) {
                String tenKhachHang = "Khách hàng";
                String sdtKhachHang = "";
                try {
                    KhachHangDAO khDAO = new KhachHangDAO();
                    KhachHang kh = khDAO.getKhachHangById(pdt.getMaKH());
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
            PdfPTable table = new PdfPTable(5); // 5 cột
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1f, 3f, 1f, 1.5f, 2f};
            table.setWidths(columnWidths);
            
            // Header của bảng
            addTableHeader(table, headerFont);
            
            // Nội dung bảng
            DecimalFormat df = new DecimalFormat("#,###");
            double tongTien = 0;
            
            for (ChiTietPhieuDoiTra item : dsChiTietPhieuDoiTra) {
            	ThuocDAO thuocDAO = new ThuocDAO();
            	Thuoc thuoc = thuocDAO.getThuocById(item.getMaThuoc()); 
            	
                String maThuoc = item.getMaThuoc();
                String tenThuoc = thuoc.getTenThuoc();
                int soLuong = item.getSoLuong();
                float donGia = item.getDonGia();
                String lyDo = item.getLyDo();
                
                table.addCell(new PdfPCell(new Phrase(maThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(tenThuoc, normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(soLuong), normalFont)));
                table.addCell(new PdfPCell(new Phrase(df.format(donGia), normalFont)));
                table.addCell(new PdfPCell(new Phrase(lyDo, normalFont)));
            }
            
            document.add(table);
            
            // Thông tin tổng tiền
            // Sử dụng mức thuế mặc định là 10%
            
            
            double tyLeThue = 0.10;
            String tenThue = "VAT (10%)";
            double tienThue = tongTien * tyLeThue;
            double tongThanhToan = tongTien + tienThue;
            
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng tiền hàng: " + df.format(tongTien) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Thuế " + tenThue + ": " + df.format(tienThue) + " VNĐ\n", normalFont));
            summary.add(new Chunk("Tổng thanh toán: " + df.format(tongThanhToan) + " VNĐ\n", totalFont));
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.add(new Paragraph("\n"));
            
            // Chữ ký
            Paragraph signature = new Paragraph();
            signature.add(new Chunk("Xác nhận của khách hàng                                                             Người lập phiếu đổi trả\n\n\n\n", normalFont));
            signature.add(new Chunk("                                                                                             " + tenNhanVien, normalFont));
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);
            
            // Thêm ghi chú từ form nếu có
            String ghiChuText = "";
            if (!ghiChuText.isEmpty()) {
                document.add(new Paragraph("\n"));
                Paragraph ghiChuParagraph = new Paragraph("Ghi chú: " + ghiChuText, normalFont);
                ghiChuParagraph.setAlignment(Element.ALIGN_LEFT);
                document.add(ghiChuParagraph);
            }
            
            // Lưu ý
            Paragraph note = new Paragraph("\n\nLưu ý: \n- Phiếu đổi trả chỉ có thể xuất trong ngày.", normalFont);
            document.add(note);
            
            document.close();
            
            // Thông báo thành công
            JOptionPane.showMessageDialog(this, 
                "Đã xuất phiếu đổi PDF thành công!\nVị trí: " + filePath, 
                "Xuất phiếu đổi trả", 
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
                "Lỗi khi xuất phiếu đổi trả PDF: " + e.getMessage() + 
                "\nVui lòng kiểm tra đường dẫn: E:\\PTUD\\PDF\\PhieuDoiTra", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
	}

	/**
	 * Thêm header cho bảng PDF
	 */
	private void addTableHeader(PdfPTable table, com.itextpdf.text.Font font) {
		String[] headers = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Lý do"};
		
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBackgroundColor(new BaseColor(240, 240, 240));
			cell.setPadding(5);
			table.addCell(cell);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		
		if (o == searchButton) {
			System.out.println("OK");
			if (cboTimKiem.getSelectedIndex() == 0) {
				timKiemPhieuDoiTraTheoMaPhieuDoiTra();
			} else if (cboTimKiem.getSelectedIndex() == 1) {
				timKiemPhieuDoiTraTheoTenNhanVien();
			} else if (cboTimKiem.getSelectedIndex() == 2) {
				timKiemPhieuDoiTraTheoTenKhachHang();
			}
			
		} else if (o == radTimKiemThangNam) {
			if (radTimKiemThangNam.isSelected()) {
				timKiemPhieuDoiTraTheoThangNam();
			} else if (!radTimKiemThangNam.isSelected()) {
				loadPhieuDoiTraData();
			} 
		} else if (o == cboThang || o == cboNam) {
			if (radTimKiemThangNam.isSelected()) {
				timKiemPhieuDoiTraTheoThangNam();
			}
		} else if (o == btnXuatPhieuDoiTra) {
			xuatPhieuDoiTraPDF();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblPhieuDoiTra.getSelectedRow();
        if (row != -1) {
            String maPhieuDoiTra = tblPhieuDoiTra.getValueAt(row, 0).toString();
            System.out.println(maPhieuDoiTra);
            loadChiTietPhieuDoiTraData(maPhieuDoiTra);
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
