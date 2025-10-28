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

import com.toedter.calendar.JDateChooser;

import app.DAO.ThuocDAO;
import app.DAO.HoaDonDAO;
import app.Entity.ThongKeThuoc;

public class ThongKeTheoThuocPanel extends JPanel implements ActionListener {
    // Components
    private JLabel lblChonLoaiThongKe;
    private JLabel lblThoiGian;
    private JLabel lblTop;
    private JComboBox<String> cboLoai;
    private JDateChooser calThoiGian = new JDateChooser();
    private JComboBox<String> cboTop;
    
    private JButton btnHomNay;
    private JButton btnHomQua;
    private JButton btnThangNay;
    
    private JLabel lblTongDoanhThu = new JLabel("");
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JPanel pnlWestPanel;
    
    // Data
    private String loaiThongKe = "Năm"; // Năm, Tháng, Ngày
    private int topN = 10; // 0 = Tất cả
    private DecimalFormat df = new DecimalFormat("#,###.##' VNĐ'");
    private ThuocDAO thuocDAO = new ThuocDAO();
    private HoaDonDAO hdDAO = new HoaDonDAO();
    
    private ArrayList<ThongKeThuoc> dsThongKe;
	private String thoiGian = new String(" năm 2025");
	private JLabel lblTitle;
    
    public ThongKeTheoThuocPanel() {
        setLayout(new BorderLayout());
        
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
            new Color(46, 204, 113),  // Xanh lá
//            new Color(52, 152, 219),  // Xanh dương
//            new Color(155, 89, 182),  // Tím
//            new Color(241, 196, 15),  // Vàng
//            new Color(230, 126, 34),  // Cam
//            new Color(231, 76, 60),   // Đỏnv00
//            new Color(149, 165, 166), // Xám
//            new Color(26, 188, 156),  // Xanh ngọc
//            new Color(52, 73, 94),    // Xanh đậm
//            new Color(192, 57, 43)    // Đỏ đậm
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
     * Tạo panel chọn lọc ở phía Bắc
     */
    private JPanel createNorthPanel() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        
        // Tiêu đề
        JLabel lblTieuDe = new JLabel("THỐNG KÊ THUỐC BÁN CHẠY", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        lblTieuDe.setForeground(new Color(0, 102, 204));
        
        // Panel chọn lọc - Dòng 1
        JPanel pnlFilter1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlFilter1.setBackground(new Color(240, 248, 255));
        
        // Chọn loại thống kê
        lblChonLoaiThongKe = new JLabel("Loại thống kê:");
        lblChonLoaiThongKe.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] loaiOptions = {"Năm", "Tháng", "Ngày"};
        cboLoai = new JComboBox<>(loaiOptions);
        cboLoai.setSelectedItem("Năm");
        cboLoai.setFont(new Font("Arial", Font.PLAIN, 14));
        cboLoai.setPreferredSize(new Dimension(120, 30));
        cboLoai.addActionListener(this);
        
        // Chọn thời gian
        lblThoiGian = new JLabel("Thời gian:");
        lblThoiGian.setFont(new Font("Arial", Font.BOLD, 16));
        
        calThoiGian = new JDateChooser();
        calThoiGian.setDateFormatString("yyyy");
        calThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        calThoiGian.setPreferredSize(new Dimension(150, 30));
        calThoiGian.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        calThoiGian.getDateEditor().getUiComponent().setBackground(new Color(218, 250, 251));
        calThoiGian.getCalendarButton().setBackground(new Color(230, 230, 250));
        calThoiGian.setDate(new Date()); // Set ngày hiện tại
        calThoiGian.addPropertyChangeListener("date", evt -> {
            if (evt.getNewValue() != null) {
                updateChart();
            }
        });
        
        // Chọn Top
        lblTop = new JLabel("Top:");
        lblTop.setFont(new Font("Arial", Font.BOLD, 16));
        
        String[] topOptions = {"Top 10", "Top 5"};
        cboTop = new JComboBox<>(topOptions);
        cboTop.setSelectedItem("Top 10");
        cboTop.setFont(new Font("Arial", Font.PLAIN, 14));
        cboTop.setPreferredSize(new Dimension(120, 30));
        cboTop.setSelectedItem("Top 10");
        cboTop.addActionListener(this);
        
        pnlFilter1.add(lblChonLoaiThongKe);
        pnlFilter1.add(cboLoai);
        pnlFilter1.add(lblThoiGian);
        pnlFilter1.add(calThoiGian);
        pnlFilter1.add(lblTop);
        pnlFilter1.add(cboTop);
        
        // Panel quick buttons - Dòng 2
        JPanel pnlFilter2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlFilter2.setBackground(new Color(240, 248, 255));
        
        btnHomNay = createQuickButton("Hôm nay");
        btnHomQua = createQuickButton("Hôm qua");
        btnThangNay = createQuickButton("Tháng này");
        
        pnlFilter2.add(btnHomNay);
        pnlFilter2.add(btnHomQua);
        pnlFilter2.add(btnThangNay);
        
        // Kết hợp cả 2 panel filter
        JPanel pnlFilters = new JPanel(new BorderLayout());
        pnlFilters.add(pnlFilter1, BorderLayout.NORTH);
        pnlFilters.add(pnlFilter2, BorderLayout.SOUTH);
        
        pnlMain.add(lblTieuDe, BorderLayout.NORTH);
        pnlMain.add(pnlFilters, BorderLayout.CENTER);
        
        return pnlMain;
    }
    
    /**
     * Tạo button nhanh
     */
    private JButton createQuickButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }
        });
        
        return btn;
    }
    
    /**
     * Tạo panel hiển thị tổng kết ở phía Nam
     */
    private JPanel createSouthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(new Color(240, 248, 255));
        
        lblTitle = new JLabel("Tổng doanh thu ");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
       
        lblTongDoanhThu = new JLabel("");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 20));
        lblTongDoanhThu.setForeground(new Color(0, 128, 0));
        updateTongDoanhThu();
        panel.add(lblTitle);
        panel.add(lblTongDoanhThu);
        
        return panel;
    }
    
    /**
     * Tạo biểu đồ
     */
    private CategoryChart createChart() {
        CategoryChart chart = new CategoryChartBuilder()
            .width(800)
            .height(500)
            .title(getChartTitle())
            .xAxisTitle("Thuốc")
            .yAxisTitle("Số lượng bán")
            .build();
        
        chart.getStyler().setOverlapped(false);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Bar);
        
        // Load dữ liệu ban đầu
        loadData();
        
        // ✅ Kiểm tra dữ liệu trước khi thêm vào chart
        if (dsThongKe != null && !dsThongKe.isEmpty()) {
        	ArrayList<String> tenThuoc = new ArrayList<>();
            ArrayList<Integer> soLuongBan = new ArrayList<>();
            for (ThongKeThuoc tk : dsThongKe) {
                tenThuoc.add(tk.getTenThuoc());
                soLuongBan.add(tk.getSoLuongBan());
            }
            chart.addSeries("kk", tenThuoc, soLuongBan);
        } else {
            // ✅ Thêm dữ liệu giả để chart không bị lỗi
            chart.addSeries(
                "Không có dữ liệu",
                java.util.Arrays.asList("Không có dữ liệu"),
                java.util.Arrays.asList(0.0)
            );
        }
        
        return chart;
    }
    
    /**
     * Lấy tiêu đề biểu đồ
     */
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
        // ✅ Khởi tạo dsThongKe rỗng trước
        dsThongKe = new ArrayList<>();
        
        // ✅ Kiểm tra calThoiGian
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
                    System.out.println("✅ Load được " + dsThongKe.size() + " thuốc theo ngày");
                    break;
                    
                case "Tháng":
                    int thang = cal.get(Calendar.MONTH) + 1;
                    int nam = cal.get(Calendar.YEAR);
                    dsThongKe = thuocDAO.thongKeThuocTheoThang(thang, nam, topN);
                    System.out.println("✅ Load được " + dsThongKe.size() + " thuốc theo tháng");
                    break;
                    
                case "Năm":
                    int namOnly = cal.get(Calendar.YEAR);
                    dsThongKe = thuocDAO.thongKeThuocTheoNam(namOnly, topN);
                    System.out.println("✅ Load được " + dsThongKe.size() + " thuốc theo năm");
                    break;
                    
                default:
                    System.out.println("⚠️ Loại thống kê không hợp lệ: " + loaiThongKe);
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
     * Cập nhật biểu đồ
     */
    private void updateChart() {
        // Load lại dữ liệu
        loadData();
        
        // Xóa series cũ
        chart.getSeriesMap().clear();
        
        // ✅ Kiểm tra dữ liệu trước khi thêm vào chart
        if (dsThongKe != null && !dsThongKe.isEmpty()) {
        	ArrayList<String> tenThuoc = new ArrayList<>();
            ArrayList<Integer> soLuongBan = new ArrayList<>();
            for (ThongKeThuoc tk : dsThongKe) {
                tenThuoc.add(tk.getTenThuoc());
                soLuongBan.add(tk.getSoLuongBan());
            }
            chart.addSeries("kk", tenThuoc, soLuongBan);
        } else {
            // ✅ Thêm dữ liệu giả để chart không bị lỗi
            chart.addSeries(
                "Không có dữ liệu",
                java.util.Arrays.asList("Không có dữ liệu"),
                java.util.Arrays.asList(0.0)
            );
        }
        
        
        // Cập nhật tiêu đề
        chart.setTitle(getChartTitle());
        
        // Cập nhật tổng doanh thu
        updateTongDoanhThu();
        
        // Cập nhật West Panel
        refreshWestPanel();
        
        // Refresh chart
        chartPanel.repaint();
        chartPanel.revalidate();
    }
    
    /**
     * Cập nhật tổng doanh thu
     */
    private void updateTongDoanhThu() {
        if (calThoiGian.getDate() == null) {
            lblTongDoanhThu.setText("0 VNĐ");
            return;
        }
        
        double tongDoanhThu = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(calThoiGian.getDate());
        
        switch (loaiThongKe) {
            case "Ngày":
                java.sql.Date sqlDate = new java.sql.Date(calThoiGian.getDate().getTime());
                tongDoanhThu = thuocDAO.getTongDoanhThuThuocTheoNgay(sqlDate);
                break;
                
            case "Tháng":
                int thang = cal.get(Calendar.MONTH) + 1;
                int nam = cal.get(Calendar.YEAR);
                tongDoanhThu = thuocDAO.getTongDoanhThuThuocTheoThang(thang, nam);
                break;
                
            case "Năm":
                nam = cal.get(Calendar.YEAR);
                tongDoanhThu = thuocDAO.getTongDoanhThuThuocTheoNam(nam);
                break;
        }
        lblTitle.setText("Tổng doanh thu"+thoiGian);
        lblTongDoanhThu.setText(df.format(tongDoanhThu));
    }
    
    /**
     * Tạo West Panel với thống kê chi tiết
     */
    private JPanel createWestPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 1, 0, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(350, 0));
        
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Arial", Font.BOLD, 14);
        
        // ✅ Kiểm tra dữ liệu
        if (dsThongKe == null || dsThongKe.isEmpty()) {
            JLabel lblNoData = new JLabel("Chưa có dữ liệu", SwingConstants.CENTER);
            lblNoData.setFont(new Font("Arial", Font.ITALIC, 16));
            lblNoData.setForeground(Color.GRAY);
            panel.add(lblNoData);
            
            // Thêm panel trống
            for (int i = 1; i < 10; i++) {
                panel.add(new JPanel());
            }
            
            return panel;
        }
        
        // Tính toán các chỉ số (có try-catch)
        int tongSoLuong = 0;
        double tongDoanhThu = 0;
        int soLoaiThuoc = 0;
        int soHoaDon = 0;
        
        try {
            tongSoLuong = dsThongKe.stream()
                .mapToInt(ThongKeThuoc::getSoLuongBan)
                .sum();
            
            tongDoanhThu = dsThongKe.stream()
                .mapToDouble(ThongKeThuoc::getDoanhThu)
                .sum();
            
            soLoaiThuoc = dsThongKe.size();
            
            // ✅ Kiểm tra trước khi lấy tongSoHoaDon
            if (!dsThongKe.isEmpty() && dsThongKe.get(0) != null) {
                soHoaDon = dsThongKe.get(0).getTongSoHoaDon();
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính toán chỉ số: " + e.getMessage());
            e.printStackTrace();
        }
        
        double slTrungBinh = soHoaDon > 0 ? (double) tongSoLuong / soHoaDon : 0;
        
        // Thêm các dòng thống kê
        addRow(panel, "Tổng số lượng bán:", tongSoLuong + " đơn vị", labelFont, valueFont, 1);
        addRow(panel, "Số loại thuốc:", String.valueOf(soLoaiThuoc), labelFont, valueFont, 2);
        addRow(panel, "Số hóa đơn:", String.valueOf(soHoaDon), labelFont, valueFont, 3);
        addRow(panel, "Doanh thu:", df.format(tongDoanhThu), labelFont, valueFont, 4);
        addRow(panel, "SL TB/hóa đơn:", String.format("%.1f đơn vị", slTrungBinh), labelFont, valueFont, 5);
        
        // Thêm Top 3
        if (dsThongKe.size() >= 1) {
            JLabel lblTop3 = new JLabel("TOP 3 THUỐC", SwingConstants.CENTER);
            lblTop3.setFont(new Font("Arial", Font.BOLD, 15));
            panel.add(lblTop3);
            
            for (int i = 0; i < Math.min(3, dsThongKe.size()); i++) {
                try {
                    ThongKeThuoc tk = dsThongKe.get(i);
                    String medal = i == 0 ? "" : i == 1 ? "" : "";
                    String text = medal + " " + tk.getTenThuoc();
                    String value = tk.getSoLuongBan() + "  đơn vị";
                    addRow(panel, text, value, labelFont, valueFont, 6 + i);
                } catch (Exception e) {
                    System.err.println("Lỗi khi hiển thị thuốc #" + (i+1) + ": " + e.getMessage());
                }
            }
        }
        
        // Thêm panel trống
        for (int i = panel.getComponentCount(); i < 10; i++) {
            panel.add(new JPanel());
        }
        
        return panel;
    }
    
    /**
     * Thêm dòng thống kê
     */
    private void addRow(JPanel panel, String label, String value, 
                        Font labelFont, Font valueFont, int row) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(labelFont);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(valueFont);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        lblValue.setForeground(new Color(0, 102, 204));
        
        JPanel pnlRow = new JPanel(new BorderLayout());
        pnlRow.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        if (row % 2 == 0) {
            pnlRow.setBackground(new Color(240, 248, 255));
        } else {
            pnlRow.setBackground(Color.WHITE);
        }
        
        pnlRow.add(lblLabel, BorderLayout.WEST);
        pnlRow.add(lblValue, BorderLayout.EAST);
        panel.add(pnlRow);
    }
    
    /**
     * Refresh West Panel
     */
    private void refreshWestPanel() {
        this.remove(pnlWestPanel);
        pnlWestPanel = createWestPanel();
        this.add(pnlWestPanel, BorderLayout.WEST);
        this.revalidate();
        this.repaint();
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
            SimpleDateFormat sdf;
            
            switch (loaiThongKe) {
                case "Năm":
                	sdf = new SimpleDateFormat("yyyy");
                    calThoiGian.setDateFormatString("yyyy");
                    thoiGian = new String( " năm " + sdf.format(calThoiGian.getDate()));
                    break;
                case "Tháng":
                	sdf = new SimpleDateFormat("MM/yyyy");
                    calThoiGian.setDateFormatString("MM/yyyy");
                    thoiGian = new String( " tháng " + sdf.format(calThoiGian.getDate()));
                    break;
                case "Ngày":
                	sdf = new SimpleDateFormat("dd/MM/yyyy");
                    calThoiGian.setDateFormatString("dd/MM/yyyy");
                    thoiGian = new String( " ngày " + sdf.format(calThoiGian.getDate()));
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
        // Quick buttons
        else if (o == btnHomNay) {
            cboLoai.setSelectedItem("Ngày");
            calThoiGian.setDate(new Date());
            updateChart();
        }
        else if (o == btnHomQua) {
            cboLoai.setSelectedItem("Ngày");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            calThoiGian.setDate(cal.getTime());
            updateChart();
        }
        else if (o == btnThangNay) {
            cboLoai.setSelectedItem("Tháng");
            calThoiGian.setDate(new Date());
            updateChart();
        }
    }
}