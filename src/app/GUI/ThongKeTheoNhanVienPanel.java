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

import com.formdev.flatlaf.FlatClientProperties;

import app.DAO.HoaDonDAO;
import app.Entity.ThongKeNhanVien;
import app.Entity.DoanhThuTheoThang;

public class ThongKeTheoNhanVienPanel extends JPanel implements ActionListener {
    // Components
    private JComboBox<Integer> cboNam;
    private JComboBox<String> cboNhanVien;
    
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JPanel pnlStats;
    
    // Data
    private int namHienTai = 2025;
    private String maNVHienTai = "ALL"; // ALL = tất cả nhân viên
    private DecimalFormat df = new DecimalFormat("#,###.##' VNĐ'");
    private DecimalFormat dfPercent = new DecimalFormat("#,###.##'%'");
    private HoaDonDAO hdDAO = new HoaDonDAO();
    
    private ArrayList<ThongKeNhanVien> dsThongKe;
    
    public ThongKeTheoNhanVienPanel() {
        initData();
        initComponents();
    }

    private void initData() {
        dsThongKe = hdDAO.thongKeDoanhThuNhanVien(namHienTai);
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        add(createHeaderPanel(), BorderLayout.NORTH);
        
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 0));
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

        JLabel lblTitle = new JLabel("Thống Kê Doanh Thu Nhân Viên");
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControls.setBackground(Color.WHITE);

        JLabel lblChonNam = new JLabel("Năm:");
        JLabel lblChonNhanVien = new JLabel("Nhân viên:");
        
        cboNam = new JComboBox<>();
        cboNam.setPreferredSize(new Dimension(100, 30));
        for(int i = 2024; i <= 2025; i++) {
            cboNam.addItem(i);
        }
        cboNam.setSelectedItem(namHienTai);
        cboNam.addActionListener(this);

        cboNhanVien = new JComboBox<>();
        cboNhanVien.setPreferredSize(new Dimension(200, 30));
        cboNhanVien.addItem("Tất cả nhân viên");
        for(ThongKeNhanVien tk : dsThongKe) {
            cboNhanVien.addItem(tk.getMaNV() + " - " + tk.getTenNV());
        }
        cboNhanVien.addActionListener(this);

        pnlControls.add(lblChonNam);
        pnlControls.add(cboNam);
        pnlControls.add(lblChonNhanVien);
        pnlControls.add(cboNhanVien);

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

        String titleText = maNVHienTai.equals("ALL") ? "Tổng Quan Năm " + namHienTai : "Chi Tiết Nhân Viên";
        JLabel lblSummaryTitle = new JLabel(titleText);
        lblSummaryTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBackground(new Color(248, 250, 252));
        pnlTitle.add(lblSummaryTitle, BorderLayout.WEST);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(pnlTitle);
        panel.add(Box.createVerticalStrut(15));

        if(maNVHienTai.equals("ALL")) {
            double tongDoanhThu = dsThongKe.stream().mapToDouble(ThongKeNhanVien::getDoanhThu).sum();
            int tongDonHang = dsThongKe.stream().mapToInt(ThongKeNhanVien::getSoLuongDonHang).sum();
            int tongKhachHang = dsThongKe.stream().mapToInt(ThongKeNhanVien::getSoLuongKhachHang).sum();
            double giaTriTrungBinh = dsThongKe.isEmpty() ? 0 : tongDoanhThu / dsThongKe.size();

            panel.add(createStatCard("Tổng doanh thu", df.format(tongDoanhThu), new Color(13, 148, 136)));
            panel.add(Box.createVerticalStrut(10));
            panel.add(createStatCard("Tổng số nhân viên", String.valueOf(dsThongKe.size()), new Color(234, 88, 12)));
            panel.add(Box.createVerticalStrut(10));
            panel.add(createStatCard("Tổng số đơn hàng", String.valueOf(tongDonHang), new Color(79, 70, 229)));
            panel.add(Box.createVerticalStrut(10));
            panel.add(createStatCard("Tổng số khách hàng", String.valueOf(tongKhachHang), new Color(219, 39, 119)));
            panel.add(Box.createVerticalStrut(10));
            panel.add(createStatCard("TB doanh thu/NV", df.format(giaTriTrungBinh), new Color(8, 145, 178)));
        } else {
            ThongKeNhanVien nv = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .orElse(null);
            
            if(nv != null) {
                int thuHang = dsThongKe.indexOf(nv) + 1;
                
                panel.add(createStatCard("Nhân viên", nv.getTenNV(), new Color(13, 148, 136)));
                panel.add(Box.createVerticalStrut(10));
                panel.add(createStatCard("Doanh thu", df.format(nv.getDoanhThu()), new Color(234, 88, 12)));
                panel.add(Box.createVerticalStrut(10));
                panel.add(createStatCard("Số đơn hàng", String.valueOf(nv.getSoLuongDonHang()), new Color(79, 70, 229)));
                panel.add(Box.createVerticalStrut(10));
                panel.add(createStatCard("TB đơn hàng", df.format(nv.getGiaTriTrungBinhDonHang()), new Color(219, 39, 119)));
                panel.add(Box.createVerticalStrut(10));
                panel.add(createStatCard("Tỷ lệ đóng góp", dfPercent.format(nv.getTyLeDongGop()), new Color(8, 145, 178)));
                panel.add(Box.createVerticalStrut(10));
                panel.add(createStatCard("Thứ hạng", "#" + thuHang + " / " + dsThongKe.size(), new Color(100, 116, 139)));
            }
        }
        
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
            .xAxisTitle(maNVHienTai.equals("ALL") ? "Nhân viên" : "Tháng")
            .yAxisTitle("Doanh thu (triệu)")
            .build();
        
        Styler styler = chart.getStyler();
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBackgroundColor(Color.WHITE);
        styler.setLegendBackgroundColor(Color.WHITE);
        styler.setLegendBorderColor(Color.WHITE);
        styler.setChartTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        styler.setSeriesColors(new Color[]{new Color(59, 130, 246)});
        styler.setLegendVisible(false);

        updateChartSeries(chart);

        return chart;
    }
    
    private void updateChartSeries(CategoryChart chart) {
        chart.getSeriesMap().clear();
        
        if(maNVHienTai.equals("ALL")) {
            ArrayList<ThongKeNhanVien> topNV = hdDAO.getTopNhanVien(namHienTai, 5);
            ArrayList<String> tenNV = new ArrayList<>();
            ArrayList<Double> doanhThu = new ArrayList<>();
            
            for(ThongKeNhanVien tk : topNV) {
                tenNV.add(tk.getTenNV());
                doanhThu.add(tk.getDoanhThu() / 1000000);
            }
            
            if (!tenNV.isEmpty()) {
                chart.addSeries("Doanh thu " + namHienTai, tenNV, doanhThu);
            }
        } else {
            ArrayList<DoanhThuTheoThang> dsThang = hdDAO.thongKeDoanhThuNhanVienTheoThang(maNVHienTai, namHienTai);
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
            
            if (!thang.isEmpty()) {
                chart.addSeries(tenNV, thang, doanhThu);
            }
        }
    }
    
    private String getChartTitle() {
        if(maNVHienTai.equals("ALL")) {
            return "Top 5 Nhân Viên Doanh Thu Cao Nhất Năm " + namHienTai;
        } else {
            String tenNV = dsThongKe.stream()
                .filter(tk -> tk.getMaNV().equals(maNVHienTai))
                .findFirst()
                .map(ThongKeNhanVien::getTenNV)
                .orElse(maNVHienTai);
            return "Doanh Thu Của " + tenNV + " Năm " + namHienTai;
        }
    }
    
    private void updateChart(int nam) {
        namHienTai = nam;
        dsThongKe = hdDAO.thongKeDoanhThuNhanVien(namHienTai);
        
        // Update combobox
        String selectedNV = (String) cboNhanVien.getSelectedItem();
        cboNhanVien.removeAllItems();
        cboNhanVien.addItem("Tất cả nhân viên");
        for(ThongKeNhanVien tk : dsThongKe) {
            cboNhanVien.addItem(tk.getMaNV() + " - " + tk.getTenNV());
        }
        
        // Restore selection
        if(selectedNV != null && !selectedNV.equals("Tất cả nhân viên")) {
            for(int i = 0; i < cboNhanVien.getItemCount(); i++) {
                if(cboNhanVien.getItemAt(i).startsWith(maNVHienTai)) {
                    cboNhanVien.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        updateChartSeries(chart);
        chart.setTitle(getChartTitle());
        chart.setXAxisTitle(maNVHienTai.equals("ALL") ? "Nhân viên" : "Tháng");
        
        chartPanel.repaint();
        chartPanel.revalidate();
        refreshStatsPanel();
    }
    
    private void updateChartForEmployee() {
        updateChartSeries(chart);
        chart.setTitle(getChartTitle());
        chart.setXAxisTitle(maNVHienTai.equals("ALL") ? "Nhân viên" : "Tháng");
        
        chartPanel.repaint();
        chartPanel.revalidate();
        refreshStatsPanel();
    }
    
    public void refreshStatsPanel() {
        JPanel parent = (JPanel) pnlStats.getParent();
        if (parent != null) {
            parent.remove(pnlStats);
            pnlStats = createStatsPanel();
            parent.add(pnlStats, BorderLayout.WEST);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    public void refresh() {
        int namDuocChon = (Integer) cboNam.getSelectedItem();
        updateChart(namDuocChon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cboNam) {
            int namDuocChon = (Integer) cboNam.getSelectedItem();
            updateChart(namDuocChon);
        }
        else if(e.getSource() == cboNhanVien) {
            String selected = (String) cboNhanVien.getSelectedItem();
            if (selected != null) {
                if(selected.equals("Tất cả nhân viên")) {
                    maNVHienTai = "ALL";
                } else {
                    maNVHienTai = selected.split(" - ")[0];
                }
                updateChartForEmployee();
            }
        }
    }
}            
