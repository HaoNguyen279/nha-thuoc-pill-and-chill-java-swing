package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import com.formdev.flatlaf.FlatClientProperties;

import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.Entity.DoanhThuTheoThang;

public class ThongKeTheoDoanhThuPanel extends JPanel implements ActionListener {

    private JComboBox<Integer> cboNam;
    private CategoryChart chart;
    private XChartPanel<CategoryChart> chartPanel;
    private JPanel pnlStats;

    private String[] months;
    private Double[] revenue;
    private int namDuocChon;

    private DecimalFormat df = new DecimalFormat("#,###.##' VND'");
    private HoaDonDAO hdDAO = new HoaDonDAO();
    private KhachHangDAO khDAO = new KhachHangDAO();
    
    private JButton btnDetail;
    private XemChiTietDoanhThuTheoThangFrame xemChiTietDoanhThuTheoNamFrame;
    public ThongKeTheoDoanhThuPanel() {
        initData();
        initComponents();
    }

    private void initData() {
        namDuocChon = 2025;
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

        JLabel lblTitle = new JLabel("Thống Kê Doanh Thu Theo Năm");
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControls.setBackground(Color.WHITE);

        JLabel lblChonNam = new JLabel("Năm:");
        
        cboNam = new JComboBox<>();
        cboNam.setPreferredSize(new Dimension(100, 30));
        
        updateYearCombobox();
        // check later - done
        cboNam.setSelectedItem(namDuocChon);
        cboNam.addActionListener(this);

        pnlControls.add(lblChonNam);
        pnlControls.add(cboNam);

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

        JLabel lblSummaryTitle = new JLabel("Tổng Quan Năm " + namDuocChon);
        lblSummaryTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.setBackground(new Color(248, 250, 252));
        pnlTitle.add(lblSummaryTitle, BorderLayout.WEST);
        pnlTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(pnlTitle);
        panel.add(Box.createVerticalStrut(15));

        double tongDoanhThuNam = hdDAO.getDoanhThuCuaNam(namDuocChon);
        int soHoaDonNam = hdDAO.getSoHoaDonTheoNam(namDuocChon);
        int soKhachHangNam = khDAO.getSoKhachHangCuaNam(namDuocChon);
        
        double giaTriTrungBinh = soHoaDonNam > 0 ? tongDoanhThuNam / soHoaDonNam : 0;
        double doanhThuTrungBinhThang = hdDAO.getDoanhThuTrungBinhTheoThang(namDuocChon);

        panel.add(createStatCard("Tổng doanh thu", df.format(tongDoanhThuNam), new Color(13, 148, 136)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Tổng số hóa đơn", String.valueOf(soHoaDonNam), new Color(234, 88, 12)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Khách hàng mua", String.valueOf(soKhachHangNam), new Color(79, 70, 229)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Trung bình/Hóa đơn", df.format(giaTriTrungBinh), new Color(219, 39, 119)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createStatCard("Trung bình/Tháng", df.format(doanhThuTrungBinhThang), new Color(8, 145, 178)));
        
        JPanel pnlDetailButton = new JPanel();
        btnDetail = new JButton("Xem chi tiết");
        btnDetail.addActionListener(this);
        pnlDetailButton.setBackground(new Color(248, 250, 252));
        pnlDetailButton.add(btnDetail);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(pnlDetailButton);
        
        
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
                .title("Biểu Đồ Doanh Thu Năm " + namDuocChon)
                .xAxisTitle("Tháng")
                .yAxisTitle("Doanh Thu (Triệu VNĐ)")
                .build();

        loadDoanhThuTheoNamData(namDuocChon);
        
        Styler styler = chart.getStyler();
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBackgroundColor(Color.WHITE);
        styler.setLegendBackgroundColor(Color.WHITE);
        styler.setLegendBorderColor(Color.WHITE);
        
        styler.setChartTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        styler.setSeriesColors(new Color[]{new Color(59, 130, 246)});
        styler.setLegendVisible(false);

        chart.addSeries("Doanh thu", Arrays.asList(months), Arrays.asList(revenue));

        return chart;
    }

    private void loadDoanhThuTheoNamData(int nam) {
        ArrayList<DoanhThuTheoThang> dsDT = hdDAO.thongKeDoanhThuTheoThang(nam);
        months = new String[dsDT.size()];
        revenue = new Double[dsDT.size()];
        for(int i = 0; i < dsDT.size(); i++) {
            months[i] = dsDT.get(i).getTenThang();
            revenue[i] = dsDT.get(i).getDoanhThu()/1000000.0;
        }
    }

    private void updateChart(int nam) {
        loadDoanhThuTheoNamData(nam);

        chart.getSeriesMap().clear();
        chart.addSeries("Doanh thu", Arrays.asList(months), Arrays.asList(revenue));
        chart.setTitle("Biểu Đồ Doanh Thu Năm " + nam);

        chartPanel.repaint();
        chartPanel.revalidate();
        
        refreshStatsPanel();
    }

    public void refresh() {
        loadDoanhThuTheoNamData(namDuocChon);
        chart.getSeriesMap().clear();
        chart.addSeries("Doanh thu", Arrays.asList(months), Arrays.asList(revenue));
        updateChart(namDuocChon);
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
    public void updateYearCombobox() {
    	HoaDonDAO hdDAO = new HoaDonDAO();
    	ArrayList<Integer> listNam = hdDAO.getNamCoHoaDon();
    	for(int i : listNam) {
    		cboNam.addItem(i);
    	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cboNam) {
            namDuocChon = (Integer) cboNam.getSelectedItem();
            updateChart(namDuocChon);
        } else if(e.getSource() == btnDetail) {
        	if(xemChiTietDoanhThuTheoNamFrame!= null) {

        		xemChiTietDoanhThuTheoNamFrame.dispose();
        		xemChiTietDoanhThuTheoNamFrame = null;
        	}
        	xemChiTietDoanhThuTheoNamFrame = new XemChiTietDoanhThuTheoThangFrame(namDuocChon);
        }
    }
}