// File: app/GUI/ThongKeNhanVienPanel.java
package app.GUI;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

import org.knowm.xchart.*;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.Styler;

import app.DAO.HoaDonDAO;
import app.Entity.ThongKeNhanVien;
import app.Entity.DoanhThuTheoThang;

public class ThongKeTheoNhanVienPanel extends JPanel implements ActionListener {
    // Components
    private JLabel lblChonNam;
    private JLabel lblChonNhanVien;
    private JComboBox<Integer> cboNam;
    private JComboBox<String> cboNhanVien;
    
    private JLabel lblTongDoanhThu;
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JPanel pnlWestPanel;
    
    // Data
    private int namHienTai = 2025;
    private String maNVHienTai = "ALL"; // ALL = tất cả nhân viên
    private DecimalFormat df = new DecimalFormat("#,###.##' VNĐ'");
    private DecimalFormat dfPercent = new DecimalFormat("#,###.##'%'");
    private HoaDonDAO hdDAO = new HoaDonDAO();
    
    private ArrayList<ThongKeNhanVien> dsThongKe;
    
    public ThongKeTheoNhanVienPanel() {
        setLayout(new BorderLayout());
        
        // Load dữ liệu ban đầu
        dsThongKe = hdDAO.thongKeDoanhThuNhanVien(namHienTai);
        
        // Tạo chart ở phía Center
        chart = createChart();
        chartPanel = new XChartPanel<CategoryChart>(chart);
        Styler styler = chart.getStyler();
        styler.setChartBackgroundColor(new Color(245, 245, 255));
        styler.setPlotBackgroundColor(Color.WHITE);
        styler.setLegendBackgroundColor(new Color(230, 230, 255));
        
        // Font chữ
        styler.setChartTitleFont(new Font("Segoe UI", Font.BOLD, 20));
        styler.setLegendFont(new Font("Segoe UI", Font.PLAIN, 13));
        styler.setBaseFont(new Font("Segoe UI", Font.BOLD, 20));
        styler.setSeriesColors(new Color[]{

           Color.green
            
            
            
        });
        
        JPanel pnlNorthPanel = createNorthPanel();
        add(pnlNorthPanel, BorderLayout.NORTH);
        
        add(chartPanel, BorderLayout.CENTER);
        
        pnlWestPanel = createWestPanel();
        add(pnlWestPanel, BorderLayout.WEST);
        
        JPanel pnlSouthPanel = createSouthPanel();
        add(pnlSouthPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Tạo panel chọn năm và nhân viên
     */
    private JPanel createNorthPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        JLabel lblTieuDe = new JLabel("DOANH THU NHÂN VIÊN", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Chọn năm
        lblChonNam = new JLabel("Chọn năm:");
        lblChonNam.setFont(new Font("Arial", Font.BOLD, 16));
         
        cboNam = new JComboBox<>();
        for(int i = 2024; i <= 2025; i++) {
            cboNam.addItem(i);
        }
        cboNam.setSelectedItem(2025);
        cboNam.setFont(new Font("Arial", Font.PLAIN, 14));
        cboNam.addActionListener(this);
        
        // Chọn nhân viên
        lblChonNhanVien = new JLabel("Chọn nhân viên:");
        lblChonNhanVien.setFont(new Font("Arial", Font.BOLD, 16));
        
        cboNhanVien = new JComboBox<>();
        cboNhanVien.addItem("Tất cả nhân viên");
        for(ThongKeNhanVien tk : dsThongKe) {
            cboNhanVien.addItem(tk.getMaNV() + " - " + tk.getTenNV());
        }
        cboNhanVien.setFont(new Font("Arial", Font.PLAIN, 14));
        cboNhanVien.addActionListener(this);
        
        panel.add(lblChonNam);
        panel.add(cboNam);
        panel.add(lblChonNhanVien);
        panel.add(cboNhanVien);
        
        pnlMain.add(panel, BorderLayout.CENTER);
        pnlMain.add(lblTieuDe, BorderLayout.NORTH);
        return pnlMain;
    }
    
    /**
     * Tạo panel hiển thị tổng doanh thu
     */
    private JPanel createSouthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panel.setBackground(new Color(240, 248, 255)); // Alice Blue
        
        JLabel lblTitle = new JLabel("Tổng doanh thu năm " + namHienTai + ":");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        lblTongDoanhThu = new JLabel("");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 20));
        lblTongDoanhThu.setForeground(new Color(0, 128, 0));
        
        panel.add(lblTitle);
        panel.add(lblTongDoanhThu);
        
        return panel;
    }
    
    /**
     * Tạo biểu đồ
     */
    private CategoryChart createChart() {
        CategoryChart chart = new CategoryChartBuilder()
            .width(600)
            .height(400)
            .title(getChartTitle())
            .xAxisTitle(maNVHienTai.equals("ALL") ? "Nhân viên" : "Tháng")
            .yAxisTitle("Doanh thu (triệu)")
            .build();
        
        chart.getStyler().setOverlapped(false);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Bar);

        if(maNVHienTai.equals("ALL")) {
            // Hiển thị top 5 nhân viên
            ArrayList<ThongKeNhanVien> topNV = hdDAO.getTopNhanVien(namHienTai, 5);
            
            ArrayList<String> tenNV = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(ThongKeNhanVien tk : topNV) {
                tenNV.add(tk.getTenNV());
                doanhThu.add(tk.getDoanhThu() / 1000000);
            }
            
            chart.addSeries("Doanh thu " + namHienTai, tenNV, doanhThu);
        } else {
            // Hiển thị doanh thu theo tháng của 1 nhân viên
            ArrayList<DoanhThuTheoThang> dsThang = 
                hdDAO.thongKeDoanhThuNhanVienTheoThang(maNVHienTai, namHienTai);
            
            ArrayList<String> thang = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(DoanhThuTheoThang dt : dsThang) {
                thang.add(dt.getTenThang());
                doanhThu.add(dt.getDoanhThu() / 1000000);
            }
            
            String tenNV = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .map(ThongKeNhanVien::getTenNV)
                .orElse(maNVHienTai);
            
            chart.addSeries(tenNV, thang, doanhThu);
        }

        return chart;
    }
    
    /**
     * Lấy tiêu đề biểu đồ
     */
    private String getChartTitle() {
        if(maNVHienTai.equals("ALL")) {
            return "Top 5 nhân viên có doanh thu cao nhất năm " + namHienTai;
        } else {
            String tenNV = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .map(ThongKeNhanVien::getTenNV)
                .orElse(maNVHienTai);
            return "Doanh thu của " + tenNV + " năm " + namHienTai;
        }
    }
    
    /**
     * Cập nhật biểu đồ khi chọn năm hoặc nhân viên mới
     */
    private void updateChart(int nam) {
        namHienTai = nam;
        dsThongKe = hdDAO.thongKeDoanhThuNhanVien(namHienTai);
        
        // Cập nhật combobox nhân viên
        String selectedNV = (String) cboNhanVien.getSelectedItem();
        cboNhanVien.removeAllItems();
        cboNhanVien.addItem("Tất cả nhân viên");
        for(ThongKeNhanVien tk : dsThongKe) {
            cboNhanVien.addItem(tk.getMaNV() + " - " + tk.getTenNV());
        }
        
        // Giữ lựa chọn nhân viên nếu có thể
        if(selectedNV != null && !selectedNV.equals("Tất cả nhân viên")) {
            for(int i = 0; i < cboNhanVien.getItemCount(); i++) {
                if(cboNhanVien.getItemAt(i).startsWith(maNVHienTai)) {
                    cboNhanVien.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        // Xóa series cũ và thêm series mới
        chart.getSeriesMap().clear();
        
        if(maNVHienTai.equals("ALL")) {
            ArrayList<ThongKeNhanVien> topNV = hdDAO.getTopNhanVien(namHienTai, 5);
            
            ArrayList<String> tenNV = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(ThongKeNhanVien tk : topNV) {
                tenNV.add(tk.getTenNV());
                doanhThu.add(tk.getDoanhThu() / 1000000);
            }
            
            chart.addSeries("Doanh thu " + namHienTai, tenNV, doanhThu);
            chart.setXAxisTitle("Nhân viên");
        } else {
            ArrayList<DoanhThuTheoThang> dsThang = 
                hdDAO.thongKeDoanhThuNhanVienTheoThang(maNVHienTai, namHienTai);
            
            ArrayList<String> thang = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(DoanhThuTheoThang dt : dsThang) {
                thang.add(dt.getTenThang());
                doanhThu.add(dt.getDoanhThu() / 1000000);
            }
            
            String tenNV = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .map(ThongKeNhanVien::getTenNV)
                .orElse(maNVHienTai);
            
            chart.addSeries(tenNV, thang, doanhThu);
            chart.setXAxisTitle("Tháng");
        }
        
        chart.setTitle(getChartTitle());
        
        // Cập nhật tổng doanh thu
        updateTongDoanhThu();
        
        // Cập nhật lại chartPanel
        chartPanel.repaint();
        refreshWestPanel();
    }
    
    /**
     * Cập nhật khi chọn nhân viên khác
     */
    private void updateChartForEmployee() {
        // Xóa series cũ và thêm series mới
        chart.getSeriesMap().clear();
        
        if(maNVHienTai.equals("ALL")) {
            ArrayList<ThongKeNhanVien> topNV = hdDAO.getTopNhanVien(namHienTai, 5);
            
            ArrayList<String> tenNV = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(ThongKeNhanVien tk : topNV) {
                tenNV.add(tk.getTenNV());
                doanhThu.add(tk.getDoanhThu() / 1000000);
            }
            
            chart.addSeries("Doanh thu " + namHienTai, tenNV, doanhThu);
            chart.setXAxisTitle("Nhân viên");
        } else {
            ArrayList<DoanhThuTheoThang> dsThang = 
                hdDAO.thongKeDoanhThuNhanVienTheoThang(maNVHienTai, namHienTai);
            
            ArrayList<String> thang = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(DoanhThuTheoThang dt : dsThang) {
                thang.add(dt.getTenThang());
                doanhThu.add(dt.getDoanhThu() / 1000000);
            }
            
            String tenNV = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .map(ThongKeNhanVien::getTenNV)
                .orElse(maNVHienTai);
            
            chart.addSeries(tenNV, thang, doanhThu);
            chart.setXAxisTitle("Tháng");
        }
        
        chart.setTitle(getChartTitle());
        
        // Cập nhật lại chartPanel
        chartPanel.repaint();
        refreshWestPanel();
    }
    
    /**
     * Cập nhật hiển thị tổng doanh thu
     */
    private void updateTongDoanhThu() {
        double tongDoanhThu;
        
        if(maNVHienTai.equals("ALL")) {
            tongDoanhThu = dsThongKe.stream()
                .mapToDouble(ThongKeNhanVien::getDoanhThu)
                .sum();
        } else {
            tongDoanhThu = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .mapToDouble(ThongKeNhanVien::getDoanhThu)
                .sum();
        }
        
        lblTongDoanhThu.setText(df.format(tongDoanhThu));
        
        // Cập nhật label title với năm mới
        JPanel southPanel = (JPanel) lblTongDoanhThu.getParent();
        if(southPanel != null) {
            for(int i = 0; i < southPanel.getComponentCount(); i++) {
                if(southPanel.getComponent(i) instanceof JLabel) {
                    JLabel lbl = (JLabel) southPanel.getComponent(i);
                    if(lbl != lblTongDoanhThu) {
                        lbl.setText("Tổng doanh thu năm " + namHienTai + ":");
                        break;
                    }
                }
            }
        }
    }
    
    public void refresh() {
        int namDuocChon = (Integer) cboNam.getSelectedItem();
        updateChart(namDuocChon);
        updateTongDoanhThu();
        chartPanel.repaint();
        chartPanel.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cboNam) {
            int namDuocChon = (Integer) cboNam.getSelectedItem();
            updateChart(namDuocChon);
            namHienTai = namDuocChon;
        }
        else if(e.getSource() == cboNhanVien) {
            String selected = (String) cboNhanVien.getSelectedItem();
            if(selected.equals("Tất cả nhân viên")) {
                maNVHienTai = "ALL";
            } else {
                maNVHienTai = selected.split(" - ")[0];
            }
            updateChartForEmployee();
            updateTongDoanhThu();
        }
    }
    
    /**
     * Tạo West Panel với thống kê
     */
    public JPanel createWestPanel() {
        // Tạo panel chính với GridLayout
        JPanel panel = new JPanel(new GridLayout(10, 1, 0, 0));
        panel.setBackground(Color.WHITE);
        
        // Font cho labels
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Arial", Font.BOLD, 14);

        if(maNVHienTai.equals("ALL")) {
            // Thống kê tổng quan
            double tongDoanhThu = dsThongKe.stream()
                .mapToDouble(ThongKeNhanVien::getDoanhThu)
                .sum();
            int tongDonHang = dsThongKe.stream()
                .mapToInt(ThongKeNhanVien::getSoLuongDonHang)
                .sum();
            int tongKhachHang = dsThongKe.stream()
                .mapToInt(ThongKeNhanVien::getSoLuongKhachHang)
                .sum();
            double giaTriTrungBinh = tongDoanhThu / dsThongKe.size();
            
            addRow(panel, "Tổng doanh thu:", df.format(tongDoanhThu), labelFont, valueFont, 1);
            addRow(panel, "Tổng số nhân viên:", String.valueOf(dsThongKe.size()), labelFont, valueFont, 2);
            addRow(panel, "Tổng số đơn hàng:", String.valueOf(tongDonHang), labelFont, valueFont, 3);
            addRow(panel, "Tổng số khách hàng:", String.valueOf(tongKhachHang), labelFont, valueFont, 4);
            addRow(panel, "DT trung bình/nhân viên:", df.format(giaTriTrungBinh), labelFont, valueFont, 5);
        } else {
            // Thống kê chi tiết 1 nhân viên
            ThongKeNhanVien nv = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .orElse(null);
            
            if(nv != null) {
                int thuHang = dsThongKe.indexOf(nv) + 1;
                
                addRow(panel, "Mã nhân viên:", nv.getMaNV(), labelFont, valueFont, 1);
                addRow(panel, "Tên nhân viên:", nv.getTenNV(), labelFont, valueFont, 2);
                addRow(panel, "Doanh thu:", df.format(nv.getDoanhThu()), labelFont, valueFont, 3);
                addRow(panel, "Số đơn hàng:", String.valueOf(nv.getSoLuongDonHang()), labelFont, valueFont, 4);
                addRow(panel, "Số khách hàng:", String.valueOf(nv.getSoLuongKhachHang()), labelFont, valueFont, 5);
                addRow(panel, "GT TB đơn hàng:", df.format(nv.getGiaTriTrungBinhDonHang()), labelFont, valueFont, 6);
                addRow(panel, "Tỷ lệ đóng góp:", dfPercent.format(nv.getTyLeDongGop()), labelFont, valueFont, 7);
                addRow(panel, "Thứ hạng:", "#" + thuHang + " / " + dsThongKe.size(), labelFont, valueFont, 8);
            }
        }
        
        // Thêm các panel trống để đủ 10 dòng
        int rowsUsed = maNVHienTai.equals("ALL") ? 5 : 8;
        for(int i = 0; i < (10 - rowsUsed); i++) {
            panel.add(new JPanel());
        }
        
        return panel;
    }
    
    private static void addRow(JPanel panel, String label, String value, 
                               Font labelFont, Font valueFont, int row) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(labelFont);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(valueFont);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel pnlRow = new JPanel(new BorderLayout());
        pnlRow.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        if(row % 2 == 0) pnlRow.setBackground(new Color(230, 230, 230));
        
        pnlRow.add(lblLabel, BorderLayout.WEST);
        pnlRow.add(lblValue, BorderLayout.EAST);
        panel.add(pnlRow);
    }
    
    public void refreshWestPanel() {
        this.remove(pnlWestPanel); // xóa panel cũ
        pnlWestPanel = createWestPanel(); // tạo panel mới với dữ liệu mới
        this.add(pnlWestPanel, BorderLayout.WEST); // add lại vào frame đúng vị trí
        this.revalidate(); // cập nhật layout
        this.repaint(); // vẽ lại frame
    }
}