package app.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import app.Entity.ChiTietLoThuoc;
import app.Entity.Thuoc;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

public class NhapThuocPanel extends JPanel {
    private ArrayList<Thuoc> dsThuoc;
    private JButton btnChonFile;
    private ArrayList<ChiTietLoThuoc> dsCTLT;
    private NhapThuocExcelPanel nhapThuocPanel;
    private JPanel centerPanel; // Panel chứa nội dung chính
    
    public NhapThuocPanel() {
        // ✅ Sử dụng BorderLayout cho panel chính
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ✅ Panel phía trên chứa nút chọn file
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnChonFile = new JButton("Chọn file Excel");
        btnChonFile.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        btnChonFile.setPreferredSize(new Dimension(180, 40));
        btnChonFile.setBackground(new Color(52, 152, 219));
        btnChonFile.setForeground(Color.WHITE);
        btnChonFile.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185)));
        btnChonFile.setFocusPainted(false);
        btnChonFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChonFile.addActionListener(e -> chonFileExcel());
        
        topPanel.add(btnChonFile);

        // ✅ Panel trung tâm chứa NhapThuocExcelPanel
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // ✅ Thêm các panel vào layout chính
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void chonFileExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel chứa danh sách thuốc");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx, *.xls)", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Đọc dữ liệu từ Excel
            Pair<ArrayList<Thuoc>, ArrayList<ChiTietLoThuoc>> data = docThuocTuExcel(file.getAbsolutePath());
            dsThuoc = data.getKey();
            dsCTLT = data.getValue();

            // Kiểm tra dữ liệu
            if (dsThuoc == null || dsThuoc.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "File Excel không có dữ liệu hoặc định dạng không đúng!",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Log dữ liệu
            System.out.println("=== Đọc được " + dsThuoc.size() + " thuốc ===");
            dsThuoc.forEach(System.out::println);
            dsCTLT.forEach(System.out::println);

            // ✅ Xóa panel cũ nếu có
            if (nhapThuocPanel != null) {
                centerPanel.remove(nhapThuocPanel);
            }

            // ✅ Tạo panel mới và thêm vào centerPanel
            nhapThuocPanel = new NhapThuocExcelPanel(dsThuoc, dsCTLT);
            centerPanel.add(nhapThuocPanel, BorderLayout.CENTER);

            // ✅ Cập nhật giao diện
            centerPanel.revalidate();
            centerPanel.repaint();

            // Thông báo thành công
            JOptionPane.showMessageDialog(this,
                    "Đọc thành công " + dsThuoc.size() + " thuốc từ file Excel!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Pair<ArrayList<Thuoc>, ArrayList<ChiTietLoThuoc>> docThuocTuExcel(String filePath) {
        ArrayList<Thuoc> list1 = new ArrayList<>();
        ArrayList<ChiTietLoThuoc> list2 = new ArrayList<>();
        ArrayList<String> errors = new ArrayList<>(); // Lưu các lỗi để hiển thị
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Bỏ dòng tiêu đề (bắt đầu từ dòng 1)
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String maThuoc = getStringCell(row.getCell(0));
                    String maLo = getStringCell(row.getCell(1));
                    String tenThuoc = getStringCell(row.getCell(2));
                    int soLuongTon = (int) getNumericCell(row.getCell(3));
                    double giaBan = getNumericCell(row.getCell(4));
                    String donVi = getStringCell(row.getCell(5));
                    int soLuongToiThieu = (int) getNumericCell(row.getCell(6));
                    String maNSX = getStringCell(row.getCell(7));
                    Date ngaySX = getDateCell(row.getCell(8));
                    Date hanSD = getDateCell(row.getCell(9));

                    // ✅ Kiểm tra dữ liệu bắt buộc
                    if (maThuoc.isEmpty() || tenThuoc.isEmpty()) {
                        String error = "Dòng " + (i + 1) + ": Thiếu mã hoặc tên thuốc";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra định dạng mã thuốc: TXXX (T + 3 chữ số)
                    if (!maThuoc.matches("^T\\d{3}$")) {
                        String error = "Dòng " + (i + 1) + ": Mã thuốc '" + maThuoc + 
                                       "' không đúng định dạng (phải là TXXX với XXX là 3 chữ số)";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra định dạng mã lô: LOXXX (LO + 3 chữ số)
                    if (!maLo.matches("^LO\\d{3}$")) {
                        String error = "Dòng " + (i + 1) + ": Mã lô '" + maLo + 
                                       "' không đúng định dạng (phải là LOXXX với XXX là 3 chữ số)";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra số lượng phải > 0
                    if (soLuongTon <= 0) {
                        String error = "Dòng " + (i + 1) + ": Số lượng tồn phải lớn hơn 0";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra giá bán phải > 0
                    if (giaBan <= 0) {
                        String error = "Dòng " + (i + 1) + ": Giá bán phải lớn hơn 0";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra ngày sản xuất và hạn sử dụng
                    if (ngaySX == null || hanSD == null) {
                        String error = "Dòng " + (i + 1) + ": Thiếu ngày sản xuất hoặc hạn sử dụng";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra hạn sử dụng phải sau ngày sản xuất
                    if (hanSD.before(ngaySX)) {
                        String error = "Dòng " + (i + 1) + ": Hạn sử dụng phải sau ngày sản xuất";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // ✅ Kiểm tra hạn sử dụng không được trong quá khứ
                    if (hanSD.before(new Date())) {
                        String error = "Dòng " + (i + 1) + ": Thuốc đã hết hạn sử dụng";
                        System.out.println(error);
                        errors.add(error);
                        continue;
                    }

                    // Tạo đối tượng Thuoc
                    Thuoc t = new Thuoc(
                            maThuoc, 
                            tenThuoc,
                            soLuongTon,
                            giaBan,
                            donVi,
                            soLuongToiThieu,
                            maNSX,
                            true
                    );
                    
                    // Tạo đối tượng ChiTietLoThuoc
                    ChiTietLoThuoc ctl = new ChiTietLoThuoc();
                    ctl.setMaLo(maLo);
                    ctl.setMaThuoc(maThuoc);
                    ctl.setNgaySanXuat(ngaySX);
                    ctl.setHanSuDung(hanSD);
                    ctl.setIsActive(true);
                    ctl.setGiaNhap(giaBan);
                    ctl.setSoLuong(soLuongTon);
                    
                    list1.add(t);
                    list2.add(ctl);
                    
                } catch (Exception e) {
                    String error = "Dòng " + (i + 1) + ": Lỗi không xác định - " + e.getMessage();
                    System.out.println(error);
                    errors.add(error);
                }
            }

            // ✅ Hiển thị thông báo lỗi nếu có
            if (!errors.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Có ").append(errors.size()).append(" lỗi khi đọc file:\n\n");
                
                // Chỉ hiển thị tối đa 10 lỗi đầu tiên
                int displayCount = Math.min(errors.size(), 10);
                for (int i = 0; i < displayCount; i++) {
                    errorMessage.append(errors.get(i)).append("\n");
                }
                
                if (errors.size() > 10) {
                    errorMessage.append("\n... và ").append(errors.size() - 10).append(" lỗi khác");
                }
                
                errorMessage.append("\n\nĐã bỏ qua các dòng lỗi.");
                errorMessage.append("\nSố dòng hợp lệ: ").append(list1.size());
                
                JOptionPane.showMessageDialog(this, 
                        errorMessage.toString(),
                        "Cảnh báo", 
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi đọc file: " + e.getMessage(),
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return new Pair<>(list1, list2);
    }

    private String getStringCell(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim().toUpperCase(); // ✅ Chuyển về chữ hoa để đồng nhất
    }

    private double getNumericCell(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return cell.getNumericCellValue();
    }

    private Date getDateCell(Cell cell) {
        if (cell == null) return null;
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else {
            return null;
        }
    }
}