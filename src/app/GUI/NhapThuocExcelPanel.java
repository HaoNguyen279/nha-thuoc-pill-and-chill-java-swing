package app.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.text.DecimalFormat;

import app.ConnectDB.ConnectDB;
import app.DAO.ChiTietPhieuNhapDAO;
import app.DAO.PhieuNhapThuocDAO;
import app.DAO.ThuocDAO;
import app.Entity.ChiTietLoThuoc;
import app.Entity.ChiTietPhieuNhap;
import app.Entity.PhieuNhapThuoc;
import app.Entity.Thuoc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class NhapThuocExcelPanel extends JPanel implements ActionListener{ 
    private DefaultTableModel dtmTable;
    private ArrayList<Thuoc> dsThuoc;
    private ArrayList<ChiTietLoThuoc> dsCTLT;
    private JButton btnTim;
    private JTextField txtTim;
    private String tieuChi = "Mã thuốc";
    private JComboBox<String> cboTieuChi;
    private JLabel lblTongSoBanGhi = new JLabel("");
    private JLabel lblTongSoLuong = new JLabel("");
    private JLabel lblTongTien = new JLabel("");
    private JButton btnXoaThuoc;
    private JTable table;
    private JButton btnNhapThuoc;
    private JButton btnXuatPDF;
    private DecimalFormat df = new DecimalFormat("#,###");

    public NhapThuocExcelPanel(ArrayList<Thuoc> dsThuoc1, ArrayList<ChiTietLoThuoc> dsCTLT1) {
        // ✅ Sử dụng BorderLayout
        setLayout(new BorderLayout());
        
        ConnectDB.getInstance().connect();
        
        dsThuoc = dsThuoc1;
        dsCTLT = dsCTLT1;
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel centerPanel = taoCenterPanel();
        // ❌ XÓA: centerPanel.setPreferredSize(new Dimension(1400,700));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private JPanel taoCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("NHẬP THUỐC", SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel searchPanel = taoSearchPanel();
        titlePanel.add(searchPanel, BorderLayout.SOUTH);
        
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel bảng
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Bảng dữ liệu
        String[] cols = {"Mã thuốc", "Mã lô", "Tên thuốc", "Số lượng", "Giá nhập", 
                        "Đơn vị", "Mã NSX", "Ngày sản xuất", "Hạn sử dụng"};
        
        dtmTable = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loadData_Thuoc(dsThuoc);
        
        table = new JTable(dtmTable);
        
     // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(240, 250, 240));
        header.setForeground(new Color(51, 51, 51));
        header.setFont(new java.awt.Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        
        // Style table
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(240, 250, 240));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        JScrollPane scrollPane = new JScrollPane(table);
        // ❌ XÓA: scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // ✅ Panel thống kê ở dưới
        JPanel thongKePanel = taoThongKePanel();
        
        centerPanel.add(panel, BorderLayout.CENTER);
        centerPanel.add(thongKePanel, BorderLayout.SOUTH);
        return centerPanel;
    }
    
    // ✅ Tạo panel thống kê với 3 label
    private JPanel taoThongKePanel() {
        JPanel thongKePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        thongKePanel.setBackground(Color.WHITE);
        
        // Label tổng số bản ghi
        lblTongSoBanGhi = new JLabel("Tổng số bản ghi: " + dsThuoc.size());
        lblTongSoBanGhi.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblTongSoBanGhi.setForeground(new Color(52, 73, 94));
        
        // Label tổng số lượng thuốc
        int tongSoLuong = tinhTongSoLuong();
        lblTongSoLuong = new JLabel("Tổng số lượng: " + df.format(tongSoLuong));
        lblTongSoLuong.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblTongSoLuong.setForeground(new Color(41, 128, 185));
        
        // Label tổng tiền
        double tongTien = tinhTongTien();
        lblTongTien = new JLabel("Tổng tiền: " + df.format(tongTien) + " VNĐ");
        lblTongTien.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        lblTongTien.setForeground(new Color(231, 76, 60));
        
        thongKePanel.add(lblTongSoBanGhi);
        thongKePanel.add(lblTongSoLuong);
        thongKePanel.add(lblTongTien);
        
        return thongKePanel;
    }
    
    // ✅ Tính tổng số lượng
    private int tinhTongSoLuong() {
        int tong = 0;
        for (Thuoc thuoc : dsThuoc) {
            tong += thuoc.getSoLuongTon();
        }
        return tong;
    }
    
    // ✅ Tính tổng tiền
    private double tinhTongTien() {
        double tong = 0;
        for (Thuoc thuoc : dsThuoc) {
            tong += thuoc.getSoLuongTon() * thuoc.getGiaBan();
        }
        return tong;
    }
    
    // ✅ Cập nhật thống kê
    private void capNhatThongKe() {
        lblTongSoBanGhi.setText("Tổng số bản ghi: " + dsThuoc.size());
        lblTongSoLuong.setText("Tổng số lượng: " + df.format(tinhTongSoLuong()));
        lblTongTien.setText("Tổng tiền: " + df.format(tinhTongTien()) + " VNĐ");
    }
    
    private JPanel taoSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        searchPanel.setBackground(Color.WHITE);
        
        txtTim = new JTextField(25);
        txtTim.setText("");
        txtTim.setForeground(Color.GRAY);
        txtTim.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 13));
        txtTim.setBackground(new Color(245, 245, 245));
        txtTim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        btnTim = new JButton("Tìm");
        btnTim.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnTim.setPreferredSize(new Dimension(80, 30));
        btnTim.setBackground(Color.WHITE);
        btnTim.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btnTim.setFocusPainted(false);
        btnTim.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTim.addActionListener(this);
        
        String[] tieuChiTim = {"Mã thuốc", "Tên thuốc"};
        cboTieuChi = new JComboBox<String>(tieuChiTim);
        cboTieuChi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        cboTieuChi.setBackground(Color.WHITE);
        cboTieuChi.setForeground(Color.BLACK);
        cboTieuChi.setFocusable(false);
        cboTieuChi.setBorder(BorderFactory.createLineBorder(Color.gray));
        cboTieuChi.addActionListener(this);
        
        btnXoaThuoc = new JButton("Xóa thuốc");
        btnXoaThuoc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnXoaThuoc.setPreferredSize(new Dimension(100, 30));
        btnXoaThuoc.setBackground(new Color(231, 76, 60));
        btnXoaThuoc.setForeground(Color.WHITE);
        btnXoaThuoc.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43)));
        btnXoaThuoc.setFocusPainted(false);
        btnXoaThuoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaThuoc.addActionListener(this);
        
        btnNhapThuoc = new JButton("Lưu vào CSDL");
        btnNhapThuoc.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnNhapThuoc.setPreferredSize(new Dimension(120, 30));
        btnNhapThuoc.setBackground(new Color(46, 204, 113));
        btnNhapThuoc.setForeground(Color.WHITE);
        btnNhapThuoc.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96)));
        btnNhapThuoc.setFocusPainted(false);
        btnNhapThuoc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNhapThuoc.addActionListener(this);
        
        btnXuatPDF = new JButton("Xuất PDF");
        btnXuatPDF.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));
        btnXuatPDF.setPreferredSize(new Dimension(100, 30));
        btnXuatPDF.setBackground(new Color(52, 152, 219));
        btnXuatPDF.setForeground(Color.WHITE);
        btnXuatPDF.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)));
        btnXuatPDF.setFocusPainted(false);
        btnXuatPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuatPDF.addActionListener(e -> xuatPhieuNhapPDF());
        
        searchPanel.add(txtTim);
        searchPanel.add(btnTim);
        searchPanel.add(cboTieuChi);
        searchPanel.add(btnXoaThuoc);
        searchPanel.add(btnNhapThuoc);
        searchPanel.add(btnXuatPDF);
        
        return searchPanel;
    }
   
    public void loadData_Thuoc(ArrayList<Thuoc> dsThuoc) {
        dtmTable.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for(int i = 0; i < dsThuoc.size(); i++) {
            Object[] rowData = {
                dsThuoc.get(i).getMaThuoc(),
                dsCTLT.get(i).getMaLo(),
                dsThuoc.get(i).getTenThuoc(),
                dsThuoc.get(i).getSoLuongTon() == 0 ? "Hết hàng" : dsThuoc.get(i).getSoLuongTon(),
                df.format(dsThuoc.get(i).getGiaBan()),
                dsThuoc.get(i).getDonVi(),
                dsThuoc.get(i).getMaNSX(),
                sdf.format(dsCTLT.get(i).getNgaySanXuat()),
                sdf.format(dsCTLT.get(i).getHanSuDung()),
            };
            dtmTable.addRow(rowData);
        }
        // ✅ Cập nhật thống kê
        capNhatThongKe();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnTim) {
            ArrayList<Thuoc> ketQuaTim = new ArrayList<Thuoc>();
            ArrayList<ChiTietLoThuoc> ctltTim = new ArrayList<ChiTietLoThuoc>();
            String timString = txtTim.getText().toLowerCase().trim();
            
            if(timString.isBlank()) {
                loadData_Thuoc(dsThuoc);
            }
            else {
                if(tieuChi.equals("Mã thuốc")) {
                    for(int i = 0; i < dsThuoc.size(); i++) {
                        if(dsThuoc.get(i).getMaThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(dsThuoc.get(i));
                            ctltTim.add(dsCTLT.get(i));
                        }
                    }
                }
                else {
                    for(int i = 0; i < dsThuoc.size(); i++) {
                        if(dsThuoc.get(i).getTenThuoc().toLowerCase().matches("^" + timString + ".*")) {
                            ketQuaTim.add(dsThuoc.get(i));
                            ctltTim.add(dsCTLT.get(i));
                        }
                    }
                }
                
                if(ketQuaTim.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Không tìm thấy!!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData_Thuoc(dsThuoc);
                    txtTim.setText("");
                    txtTim.requestFocus();
                } else {
                    // Tạm thời hiển thị kết quả tìm kiếm
                    dtmTable.setRowCount(0);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for(int i = 0; i < ketQuaTim.size(); i++) {
                        Object[] rowData = {
                            ketQuaTim.get(i).getMaThuoc(),
                            ctltTim.get(i).getMaLo(),
                            ketQuaTim.get(i).getTenThuoc(),
                            ketQuaTim.get(i).getSoLuongTon() == 0 ? "Hết hàng" : ketQuaTim.get(i).getSoLuongTon(),
                            df.format(ketQuaTim.get(i).getGiaBan()),
                            ketQuaTim.get(i).getDonVi(),
                            ketQuaTim.get(i).getMaNSX(),
                            sdf.format(ctltTim.get(i).getNgaySanXuat()),
                            sdf.format(ctltTim.get(i).getHanSuDung()),
                        };
                        dtmTable.addRow(rowData);
                    }
                }
            }
            System.out.println(timString);
        }
        else if(o == cboTieuChi) {
            tieuChi = cboTieuChi.getSelectedItem().toString();
            System.out.println(tieuChi);
        }
        else if(o == btnXoaThuoc) {
            int row = table.getSelectedRow();
            if(row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn thuốc cần xóa!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa thuốc này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
            
            if(confirm == JOptionPane.YES_OPTION) {
                String maThuoc = table.getValueAt(row, 0).toString();
                System.out.println("Xóa: " + maThuoc);
                dtmTable.removeRow(row);
                dsThuoc.remove(row);
                dsCTLT.remove(row);
                // ✅ Cập nhật thống kê sau khi xóa
                capNhatThongKe();
            }
        }
        else if (o == btnNhapThuoc) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận nhập đơn hàng!!",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            
            if(result == JOptionPane.YES_OPTION) {
                if(nhapThuoc() && themPhieuNhap()) {
                    JOptionPane.showMessageDialog(this,
                        "Nhập thuốc vào CSDL thành công!!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dtmTable.setRowCount(0);
                    dsThuoc.clear();
                    dsCTLT.clear();
                    capNhatThongKe();
                }
                else {
                    JOptionPane.showMessageDialog(this,
                        "Hãy kiểm tra lại thông tin đơn hàng",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this,
                    "Đã Hủy!",
                    "Hủy", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean nhapThuoc() {
        ThuocDAO daoThuoc = new ThuocDAO();
        return daoThuoc.addDsThuoc(dsThuoc, dsCTLT);
    }
    
    private boolean themPhieuNhap() {
        PhieuNhapThuocDAO phieuDAO = new PhieuNhapThuocDAO();
        ChiTietPhieuNhapDAO ctpnDao = new ChiTietPhieuNhapDAO();
        PhieuNhapThuoc phieu = new PhieuNhapThuoc();
        phieu.setMaPhieuNhapThuoc(phieuDAO.taoMaTuDong());
        phieu.setMaNV("NV001");
        phieu.setNgayNhap(new Date());
        phieu.setIsActive(true);
        phieuDAO.addPhieuNhapThuoc(phieu);
        
        for (int i = 0; i < dsThuoc.size(); i++) {
            Thuoc thuoc = dsThuoc.get(i);
            ChiTietLoThuoc ctlt = dsCTLT.get(i);
            ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
            ctpn.setMaPhieuNhapThuoc(phieu.getMaPhieuNhapThuoc());
            ctpn.setMaLo(ctlt.getMaLo());
            ctpn.setMaThuoc(thuoc.getMaThuoc());
            ctpn.setSoLuong(ctlt.getSoLuong());
            ctpn.setDonGia((float)ctlt.getGiaNhap());
            ctpn.setIsActive(true);
            ctpnDao.addChiTietPhieuNhap(ctpn);
        }
        return true;
    }
    
    private void xuatPhieuNhapPDF() {
        if (dsThuoc == null || dsThuoc.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu để xuất PDF!",
                "Cảnh báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String defaultDir = "F:\\PTUD";
            File directory = new File(defaultDir);
            
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            PhieuNhapThuocDAO phieuDAO = new PhieuNhapThuocDAO();
            String maPhieuNhap = phieuDAO.taoMaTuDong();
            
            String fileName = "PhieuNhapThuoc_" + maPhieuNhap + ".pdf";
            String filePath = defaultDir + "\\" + fileName;
            
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", 
                BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 18, Font.BOLD);
            Font headerFont = new Font(baseFont, 12, Font.BOLD);
            Font normalFont = new Font(baseFont, 11, Font.NORMAL);
            Font totalFont = new Font(baseFont, 12, Font.BOLD);
            
            Paragraph title = new Paragraph("PHIẾU NHẬP THUỐC", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            Paragraph shopInfo = new Paragraph(
                "NHÀ THUỐC PILL & CHILL\n" +
                "Địa chỉ: 12 Nguyễn Văn Bảo, P.4, Q.Gò Vấp, TP.HCM\n" +
                "Hotline: 0987654321", 
                normalFont);
            shopInfo.setAlignment(Element.ALIGN_CENTER);
            document.add(shopInfo);
            
            document.add(new Paragraph("\n"));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDate = dateFormat.format(new Date());
            
            Paragraph phieuInfo = new Paragraph();
            phieuInfo.add(new Chunk("Mã phiếu nhập: " + maPhieuNhap + "\n", headerFont));
            phieuInfo.add(new Chunk("Ngày nhập: " + currentDate + "\n", normalFont));
            phieuInfo.add(new Chunk("Nhân viên nhập: NV001\n", normalFont));
            
            document.add(phieuInfo);
            document.add(new Paragraph("\n"));
            
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            float[] columnWidths = {1.2f, 2.5f, 1f, 1.5f, 1f, 1.2f, 1.3f, 1.3f};
            table.setWidths(columnWidths);
            
            addTableHeaderPhieuNhap(table, headerFont);
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            double tongTien = 0;
            
            for (int i = 0; i < dsThuoc.size(); i++) {
                Thuoc thuoc = dsThuoc.get(i);
                ChiTietLoThuoc ctlt = dsCTLT.get(i);
                
                double thanhTien = thuoc.getSoLuongTon() * thuoc.getGiaBan();
                tongTien += thanhTien;
                
                PdfPCell cellMaThuoc = new PdfPCell(new Phrase(thuoc.getMaThuoc(), normalFont));
                cellMaThuoc.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellMaThuoc);
                
                table.addCell(new PdfPCell(new Phrase(thuoc.getTenThuoc(), normalFont)));
                
                PdfPCell cellSoLuong = new PdfPCell(new Phrase(String.valueOf(thuoc.getSoLuongTon()), normalFont));
                cellSoLuong.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellSoLuong);
                
                PdfPCell cellDonGia = new PdfPCell(new Phrase(df.format(thuoc.getGiaBan()), normalFont));
                cellDonGia.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellDonGia);
                
                PdfPCell cellDonVi = new PdfPCell(new Phrase(thuoc.getDonVi(), normalFont));
                cellDonVi.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellDonVi);
                
                PdfPCell cellMaNSX = new PdfPCell(new Phrase(thuoc.getMaNSX(), normalFont));
                cellMaNSX.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellMaNSX);
                
                PdfPCell cellNSX = new PdfPCell(new Phrase(sdf.format(ctlt.getNgaySanXuat()), normalFont));
                cellNSX.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellNSX);
                
                PdfPCell cellHSD = new PdfPCell(new Phrase(sdf.format(ctlt.getHanSuDung()), normalFont));
                cellHSD.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellHSD);
            }
            
            document.add(table);
            
            Paragraph summary = new Paragraph();
            summary.add(new Chunk("Tổng số lượng thuốc: " + dsThuoc.size() + " loại\n", normalFont));
            int tongSoLuong = dsThuoc.stream().mapToInt(Thuoc::getSoLuongTon).sum();
            summary.add(new Chunk("Tổng số lượng nhập: " + tongSoLuong + " " + 
                (dsThuoc.size() > 0 ? dsThuoc.get(0).getDonVi() : "") + "\n", normalFont));
            summary.add(new Chunk("Tổng tiền: " + df.format(tongTien) + " VNĐ\n", totalFont));
            summary.setAlignment(Element.ALIGN_RIGHT);
            document.add(summary);
            
            document.add(new Paragraph("\n\n"));
            
            Paragraph signature = new Paragraph();
            signature.add(new Chunk(
                "Người lập phiếu                                    " +
                "                            Thủ kho\n\n\n\n", 
                normalFont));
            signature.setAlignment(Element.ALIGN_CENTER);
            document.add(signature);
            
            Paragraph note = new Paragraph(
                "\n\nLưu ý: \n" +
                "- Kiểm tra kỹ hạn sử dụng của thuốc trước khi nhập kho.\n" +
                "- Bảo quản thuốc theo đúng hướng dẫn của nhà sản xuất.", 
                normalFont);
            document.add(note);
            
            document.close();
            
            JOptionPane.showMessageDialog(this,
                "Đã xuất phiếu nhập PDF thành công!\nVị trí: " + filePath,
                "Xuất phiếu nhập",
                JOptionPane.INFORMATION_MESSAGE);
            
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
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
                "Lỗi khi xuất phiếu nhập PDF: " + e.getMessage() +
                "\nVui lòng kiểm tra đường dẫn: F:\\PTUD",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableHeaderPhieuNhap(PdfPTable table, Font headerFont) {
        String[] headers = {"Mã thuốc", "Tên thuốc", "SL", "Đơn giá", "ĐVT", "Mã NSX", "NSX", "HSD"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(200, 200, 200));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
}