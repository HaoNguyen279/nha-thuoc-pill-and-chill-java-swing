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

public class NhapThuocPanel extends JPanel {
	private ArrayList<Thuoc> dsThuoc;
    private JButton btnChonFile;
    private ArrayList<ChiTietLoThuoc> dsCTLT;
	private NhapThuocExcelPanel nhapThuocPanel;
    public NhapThuocPanel() {
      
//        setSize(400, 200);
        
        setLayout(new FlowLayout());

        btnChonFile = new JButton("📂 Chọn file Excel");
        add(btnChonFile);

        btnChonFile.addActionListener(e -> chonFileExcel());
        
       

         // căn giữa cửa sổ
    }

    private void chonFileExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file Excel chứa danh sách thuốc");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Excel Files (*.xlsx, *.xls)", "xlsx", "xls"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
           dsThuoc = docThuocTuExcel(file.getAbsolutePath()).getKey();
           dsCTLT = docThuocTuExcel(file.getAbsolutePath()).getValue();
            // Hiển thị thông báo
//            JOptionPane.showMessageDialog(this,
//                    "Đọc thành công " + dsThuoc.size() + " thuốc!",
//                    "Thông báo",
//                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("📋 Danh sách thuốc:");
            dsThuoc.forEach(System.out::println);
            dsCTLT.forEach(System.out::println);
            if(nhapThuocPanel == null) {
            	nhapThuocPanel = new NhapThuocExcelPanel(dsThuoc, dsCTLT);
                add(nhapThuocPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
            else {
            	remove(nhapThuocPanel);
            	nhapThuocPanel = new NhapThuocExcelPanel(dsThuoc, dsCTLT);
                add(nhapThuocPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
            
            
        }
        
    }

    private Pair<ArrayList<Thuoc>, ArrayList<ChiTietLoThuoc>> docThuocTuExcel(String filePath) {
    	ArrayList<Thuoc> list1 = new ArrayList<>();
    	ArrayList<ChiTietLoThuoc> list2 = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Bỏ dòng tiêu đề
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

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
                Thuoc t = new Thuoc(
                        maThuoc,
                        maLo,
                        tenThuoc,
                        soLuongTon,
                        giaBan,
                        donVi,
                        soLuongToiThieu,
                        maNSX,
                        true
                );
                ChiTietLoThuoc ctl = new ChiTietLoThuoc();
                ctl.setMaLo(maLo);
                ctl.setMaThuoc(maThuoc);
                ctl.setNgaySanXuat(ngaySX);
                ctl.setHanSuDung(hanSD);
                ctl.setIsActive(true);;
                ctl.setGiaNhap(giaBan);
                ctl.setSoLuong(soLuongTon);
                list1.add(t);
                list2.add(ctl);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc file: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return new Pair<ArrayList<Thuoc>, ArrayList<ChiTietLoThuoc>>(list1,list2);
    }
    
    

    private String getStringCell(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new DocThuocExcelUI().setVisible(true));
//    }
}
