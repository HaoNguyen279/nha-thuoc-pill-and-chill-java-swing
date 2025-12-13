package app.GUI;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;

import org.knowm.xchart.*;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.style.Styler;

import com.formdev.flatlaf.FlatClientProperties;
import com.toedter.calendar.JDateChooser;

import app.DAO.ThuocDAO;
import app.DAO.HoaDonDAO;
import app.Entity.ThongKeThuoc;

public class ThongKeTheoThuocPanel extends JPanel implements ActionListener {
    // Components
    private JComboBox<String> cboLoai;
    private JDateChooser calThoiGian;
    private JComboBox<String> cboTop;
    
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JPanel pnlStats;
    private JPanel pnlCenter;
    
    // Data
    private String loaiThongKe = "Năm"; // Năm, Tháng, Ngày
    private int topN = 10; // 0 = Tất cả
    private DecimalFormat df = new DecimalFormat("#,###.##' VNĐ'");
    private ThuocDAO thuocDAO = new ThuocDAO();
    
    private ArrayList<ThongKeThuoc> dsThongKe;
    
    public ThongKeTheoThuocPanel() {
        initData();
        initComponents();
    }
    
    private void initData() {
        calThoiGian = new JDateChooser();
        calThoiGian.setDateFormatString("yyyy");
        calThoiGian.setDate(new Date());
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        add(createHeaderPanel(), BorderLayout.NORTH);
        
        pnlCenter = new JPanel(new BorderLayout(20, 0));
        pnlCenter.setBackground(Color.WHITE);
        
        pnlStats = createStatsPanel();
        pnlCenter.add(pnlStats, BorderLayout.WEST);

        chart = createChart();
        chartPanel = new XChartPanel<>(chart);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        pnlCenter.add(chartPanel, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thống Kê Thuốc Bán Chạy");
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControls.setBackground(Color.WHITE);

        // Loại thống kê
        JLabel lblLoai = new JLabel("Loại:");
        String[] loaiOptions = {"Năm", "Tháng", "Ngày"};
        cboLoai = new JComboBox<>(loaiOptions);
        cboLoai.setSelectedItem("Năm");
        cboLoai.setPreferredSize(new Dimension(100, 30));
        cboLoai.addActionListener(this);
        
        // Thời gian
        JLabel lblThoiGian = new JLabel("Thời gian:");
        calThoiGian.setPreferredSize(new Dimension(150, 30));
        calThoiGian.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                updateChart();
            }
        });
        
        // Top
        JLabel lblTop = new JLabel("Top:");
        String[] topOptions = {"Top 10", "Top 5"};
        cboTop = new JComboBox<>(topOptions);
        cboTop.setSelectedItem("Top 10");
        cboTop.setPreferredSize(new Dimension(100, 30));
        cboTop.addActionListener(this);

        pnlControls.add(lblLoai);
        pnlControls.add(cboLoai);
        pnlControls.add(Box.createHorizontalStrut(10));
        pnlControls.add(lblThoiGian);
        pnlControls.add(calThoiGian);
        pnlControls.add(Box.createHorizontalStrut(10));
        pnlControls.add(lblTop);
        pnlControls.add(cboTop);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlControls, BorderLayout.EAST);

        return pnlHeader;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        panel.setPreferredSize(new Dimension(320, 0));

        JLabel lblSummaryTitle = new JLabel("Tổng Quan");
        lblSummaryTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBackground(new Color(248, 250, 252));
        pnlTitle.add(lblSummaryTitle, BorderLayout.WEST);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(pnlTitle);
        panel.add(Box.createVerticalStrut(15));

        // Calculate stats
        int tongSoLuong = 0;
        double tongDoanhThu = 0;
        int soLoaiThuoc = 0;
        int soHoaDon = 0;
        
        if (dsThongKe != null) {
            try {
                tongSoLuong = dsThongKe.stream().mapToInt(ThongKeThuoc::getSoLuongBan).sum();
                tongDoanhThu = dsThongKe.stream().mapToDouble(ThongKeThuoc::getDoanhThu).sum();
                soLoaiThuoc = dsThongKe.size();
                if (!dsThongKe.isEmpty() && dsThongKe.get(0) != null) {
                    soHoaDon = dsThongKe.get(0).getTongSoHoaDon();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        double slTrungBinh = soHoaDon > 0 ? (double) tongSoLuong / soHoaDon : 0;

        panel.add(createStatCard("Tổng doanh thu", df.format(tongDoanhThu), new Color(13, 148, 136)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Tổng số lượng bán", String.valueOf(tongSoLuong), new Color(234, 88, 12)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Số loại thuốc", String.valueOf(soLoaiThuoc), new Color(79, 70, 229)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Số hóa đơn", String.valueOf(soHoaDon), new Color(219, 39, 119)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("TB/Hóa đơn", String.format("%.1f đơn vị", slTrungBinh), new Color(8, 145, 178)));
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.GRAY);
        
        JLabel lblValue = new JLabel(value);
        lblValue.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
    private CategoryChart createChart() {
        CategoryChart chart = new CategoryChartBuilder()
            .width(800)
            .height(600)
            .title(getChartTitle())
            .xAxisTitle("Thuốc")
            .yAxisTitle("Số lượng bán")
            .build();
        
        Styler styler = chart.getStyler();
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBackgroundColor(Color.WHITE);
        styler.setLegendBackgroundColor(Color.WHITE);
        styler.setLegendBorderColor(Color.WHITE);
        
        styler.setChartTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        styler.setSeriesColors(new Color[]{new Color(59, 130, 246)});
        styler.setLegendVisible(false);
       
        
        // Load dữ liệu ban đầu
        // loadData(); // Already loaded in initData
        
        // Kiểm tra dữ liệu trước khi thêm vào chart
        if (dsThongKe != null && !dsThongKe.isEmpty()) {
            ArrayList<String> tenThuoc = new ArrayList<>();
            ArrayList<Integer> soLuongBan = new ArrayList<>();
            for (ThongKeThuoc tk : dsThongKe) {
                tenThuoc.add(tk.getTenThuoc());
                soLuongBan.add(tk.getSoLuongBan());
            }
            chart.addSeries("Số lượng bán", tenThuoc, soLuongBan);
        } else {
            chart.addSeries(
                "Không có dữ liệu",
                java.util.Arrays.asList("Không có dữ liệu"),
                java.util.Arrays.asList(0)
            );
        }
        
        return chart;
    }
    private String getChartTitle() {
        String title = "Top " + (topN == 5 ? topN : 10) + " Thuốc Bán Chạy ";
        
        if (calThoiGian.getDate() != null) {
            SimpleDateFormat sdf;
            switch (loaiThongKe) {
                case "Ngày":
                    sdf = new SimpleDateFormat("dd/MM/yyyy");
                    title += "Ngày " + sdf.format(calThoiGian.getDate());
                    break;
                case "Tháng":
                    sdf = new SimpleDateFormat("MM/yyyy");
                    title += "Tháng " + sdf.format(calThoiGian.getDate());
                    break;
                case "Năm":
                    sdf = new SimpleDateFormat("yyyy");
                    title += "Năm " + sdf.format(calThoiGian.getDate());
                    break;
            }
        }
        
        return title;
    }
    
    /**
     * Load dữ liệu từ DAO
     */
    private void loadData() {
        // Khởi tạo dsThongKe rỗng trước
        dsThongKe = new ArrayList<>();
        
        // Kiểm tra calThoiGian
        if (calThoiGian == null || calThoiGian.getDate() == null) {
            calThoiGian.setDate(new Date());
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(calThoiGian.getDate());
        
        try {
            switch (loaiThongKe) {
                case "Ngày":
                    java.sql.Date sqlDate = new java.sql.Date(calThoiGian.getDate().getTime());
                    dsThongKe = thuocDAO.thongKeThuocTheoNgay(sqlDate, topN);
                    break;
                    
                case "Tháng":
                    int thang = cal.get(Calendar.MONTH) + 1;
                    int nam = cal.get(Calendar.YEAR);
                    dsThongKe = thuocDAO.thongKeThuocTheoThang(thang, nam, topN);
                    break;
                    
                case "Năm":
                    int namOnly = cal.get(Calendar.YEAR);
                    dsThongKe = thuocDAO.thongKeThuocTheoNam(namOnly, topN);
                    break;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
            dsThongKe = new ArrayList<>();
        }
        
        // ✅ Đảm bảo dsThongKe không null
        if (dsThongKe == null) {
            dsThongKe = new ArrayList<>();
        }
    }
    
    /**
     * Cập nhật biểu đồ và thống kê
     */
    private void updateChart() {
        // Load lại dữ liệu
        loadData();
        
        // 1. Cập nhật Chart
        chart.getSeriesMap().clear();
        chart.setTitle(getChartTitle());
        
        if (dsThongKe != null && !dsThongKe.isEmpty()) {
            ArrayList<String> tenThuoc = new ArrayList<>();
            ArrayList<Integer> soLuongBan = new ArrayList<>();
            for (ThongKeThuoc tk : dsThongKe) {
                tenThuoc.add(tk.getTenThuoc());
                soLuongBan.add(tk.getSoLuongBan());
            }
            chart.addSeries("Số lượng bán", tenThuoc, soLuongBan);
        } else {
            chart.addSeries(
                "Không có dữ liệu",
                java.util.Arrays.asList("Không có dữ liệu"),
                java.util.Arrays.asList(0)
            );
        }
        chartPanel.repaint();
        
        // 2. Cập nhật Stats Panel
        if (pnlCenter != null) {
            pnlCenter.remove(pnlStats);
            pnlStats = createStatsPanel(); // Tạo panel mới với dữ liệu mới
            pnlCenter.add(pnlStats, BorderLayout.WEST);
            pnlCenter.revalidate();
            pnlCenter.repaint();
        }
    }
    
    /**
     * Refresh toàn bộ panel
     */
    public void refresh() {
        updateChart();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        // Thay đổi loại thống kê
        if (o == cboLoai) {
            loaiThongKe = (String) cboLoai.getSelectedItem();
            
            switch (loaiThongKe) {
                case "Năm":
                    calThoiGian.setDateFormatString("yyyy");
                    break;
                case "Tháng":
                    calThoiGian.setDateFormatString("MM/yyyy");
                    break;
                case "Ngày":
                    calThoiGian.setDateFormatString("dd/MM/yyyy");
                    break;
            }
            
            updateChart();
        }
        // Thay đổi Top
        else if (o == cboTop) {
            String topStr = (String) cboTop.getSelectedItem();
            
            switch (topStr) {
                case "Top 10":
                    topN = 10;
                    break;
                case "Top 5":
                    topN = 5;
                    break;
            }
            
            updateChart();
        }
    }
}