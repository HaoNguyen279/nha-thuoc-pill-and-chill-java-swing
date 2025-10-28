package app.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import app.DAO.HoaDonDAO;
import app.DAO.KhachHangDAO;
import app.Entity.DoanhThuHoaDon;



public class ThongKeDoanhThuTheoThangPanel extends JPanel implements ActionListener {
	private JLabel lblChonNam; 
	private JLabel lblChonThang;
	
	private JComboBox<Integer> cboNam; 
	private JComboBox<Integer> cboThang; 
	
	private JLabel lblTongDoanhThu;
	private CategoryChart chart;
	private XChartPanel<CategoryChart> chartPanel;
	private JPanel pnlWestPanel;
	
	private Integer[] days;
	private Double[] revenue;
	private int namDuocChon;
	private int thangDuocChon;
	
	private DecimalFormat df = new DecimalFormat("#,###.##' VND'");
    private HoaDonDAO hdDAO = new HoaDonDAO();
    private KhachHangDAO khDAO = new KhachHangDAO();
    
	public ThongKeDoanhThuTheoThangPanel() {
		setLayout(new BorderLayout());
		// Init giá trị
		thangDuocChon = 9;
		namDuocChon = 2025;
		
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
//        styler.setXAxisTitleColor(Color.RED);
//        styler.setYAxisTitleColor(Color.GREEN);
        styler.setSeriesColors(new Color[]{
                new Color(255, 127, 80),  // Cam
                new Color(160, 238, 160),  // Lục
                new Color(100, 149, 237), // Xanh 
        });

		JPanel pnlNorthPanel = createNorthPanel();
		add(pnlNorthPanel, BorderLayout.NORTH);
        
		add(chartPanel, BorderLayout.CENTER);
		
		pnlWestPanel = createWestPanel();
		add(pnlWestPanel, BorderLayout.WEST);
		
		JPanel pnlSouthPanel = createSouthPanel();
		add(pnlSouthPanel, BorderLayout.SOUTH);
		updateTongDoanhThu();
	}
	
	/**
	 * Tạo panel chọn năm
	 */
	private JPanel createNorthPanel() {
		JPanel pnlMain = new JPanel(new BorderLayout());
        JLabel lblTieuDe = new JLabel("DOANH THU THÁNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		lblChonNam = new JLabel("Chọn năm:");
		lblChonNam.setFont(new Font("Arial", Font.BOLD, 16));
		lblChonThang = new JLabel("Chọn tháng:");
		lblChonThang.setFont(new Font("Arial", Font.BOLD, 16));
		
		cboNam = new JComboBox<>();
		cboThang = new JComboBox<>();
		// Thêm các năm từ 2020 đến 2025
		for(int i = 2024; i <= 2025; i++) cboNam.addItem(i);
		// Thêm các Tháng từ 1 đến 12
		for(int i = 1; i <= 12; i++) cboThang.addItem(i);
		
		cboNam.setSelectedItem(namDuocChon); // Chọn mặc định năm 2025
		cboNam.setFont(new Font("Arial", Font.PLAIN, 14));
		cboNam.addActionListener(this);
		
		cboThang.setSelectedItem(thangDuocChon); // Chọn mặc định năm 2025
		cboThang.setFont(new Font("Arial", Font.PLAIN, 14));
		cboThang.addActionListener(this);
		
		panel.add(lblChonNam);
		panel.add(cboNam);
		
		panel.add(lblChonThang);
		panel.add(cboThang);
		
		pnlMain.add(panel, BorderLayout.CENTER);
		pnlMain.add(lblTieuDe, BorderLayout.NORTH);
		return pnlMain;

		
	}
	
//	/**
//	 * Tạo panel hiển thị tổng doanh thu
//	 */
	private JPanel createSouthPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
		panel.setBackground(new Color(240, 248, 255)); // Alice Blue
		
		JLabel lblTitle = new JLabel("Tổng doanh thu tháng " + thangDuocChon + ":");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

		lblTongDoanhThu = new JLabel("");
		lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 20));
		lblTongDoanhThu.setForeground(new Color(0, 128, 0)); // Màu xanh lá
		
		panel.add(lblTitle);
		panel.add(lblTongDoanhThu);
		
		return panel;
	}
	
	private CategoryChart createChart() {
		CategoryChart chart = new CategoryChartBuilder()
			.width(600)
			.height(400)
			.title("Doanh thu nhà thuốc theo tháng của năm " + namDuocChon)
			.xAxisTitle("Ngày")
			.yAxisTitle("Doanh thu (triệu)")
			.build();

		loadDoanhThuTheoNamData(thangDuocChon, namDuocChon);
		chart.getStyler().setOverlapped(false);
	    chart.getStyler().setLegendVisible(true);
	    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Bar);

        chart.addSeries("Doanh thu tháng " + thangDuocChon, Arrays.asList(days), Arrays.asList(revenue));

		return chart;
	}
	
	
	private void loadDoanhThuTheoNamData(int thang, int nam) {
		ArrayList<DoanhThuHoaDon> dsDT = new ArrayList<>();
		YearMonth ym = YearMonth.of(nam, thang);
		int daysInMonth = ym.lengthOfMonth();
		
		dsDT = hdDAO.getDoanhThuTheoNgay(thang, nam);
		days = new Integer[daysInMonth];
		revenue = new Double[daysInMonth];
		
        for(int i = 0; i < daysInMonth; i++) {
            days[i] = i + 1;
            revenue[i] = 0.0;
        }

        for(DoanhThuHoaDon item : dsDT) {
            int ngay = item.getNgay();
            if(ngay >= 1 && ngay <= daysInMonth) {
                days[ngay - 1] = ngay;
                revenue[ngay - 1] = item.getDoanhThu() / 1000000.0;
            }
        }
	}
	
	/**
	 * Cập nhật biểu đồ khi chọn năm mới
	 */
    private void updateChart(int thang, int nam) {
        loadDoanhThuTheoNamData(thang, nam);
        
        // Xóa series cũ và thêm series mới
        chart.getSeriesMap().clear();
        chart.addSeries("Doanh thu tháng " + thang + "/" + nam, 
                        Arrays.asList(days), 
                        Arrays.asList(revenue));
        chart.setTitle("Doanh thu nhà thuốc tháng " + thang + "/" + nam);
        
        // Cập nhật lại chartPanel
        chartPanel.repaint();
        chartPanel.revalidate();
        refreshWestPanel();
    }
	
	/**
	 * Cập nhật hiển thị tổng doanh thu
	 */
	private void updateTongDoanhThu() {
        double tongDoanhThuThang = hdDAO.getDoanhThuCuaThang(thangDuocChon, namDuocChon);
		lblTongDoanhThu.setText(df.format(tongDoanhThuThang));

		// Cập nhật label title với năm mới
		JPanel southPanel = (JPanel) lblTongDoanhThu.getParent();
		if(southPanel != null) {
			for(int i = 0; i < southPanel.getComponentCount(); i++) {
				if(southPanel.getComponent(i) instanceof JLabel) {
					JLabel lbl = (JLabel) southPanel.getComponent(i);
					if(lbl != lblTongDoanhThu) {
						lbl.setText("Tổng doanh thu tháng " + thangDuocChon + ":");
						break;
					}
				}
			}
		}
	}
	
	public void refresh() {
		loadDoanhThuTheoNamData(thangDuocChon, namDuocChon);
		chart.getSeriesMap().clear();
        chart.addSeries("Doanh thu " + namDuocChon, Arrays.asList(days), Arrays.asList(revenue));
		updateChart(thangDuocChon, namDuocChon);
        chartPanel.repaint();
        chartPanel.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cboNam) {
			int namChon = (Integer) cboNam.getSelectedItem();
			namDuocChon = namChon;
			updateChart(thangDuocChon, namDuocChon);
			updateTongDoanhThu();
		}
		else if(e.getSource() == cboThang) {
			int thangChon = (Integer) cboThang.getSelectedItem();
			thangDuocChon = thangChon;
			updateChart(thangDuocChon, namDuocChon);
			updateTongDoanhThu();
		}
	}
	
	 public  JPanel createWestPanel() {
        // Tạo panel chính với GridLayout
        JPanel panel = new JPanel(new GridLayout(10,1,0,0));
        panel.setBackground(Color.WHITE);
        
        // Font cho labels
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Arial", Font.BOLD, 14);

        // Thêm các dòng dữ liệu
        double tongDoanhThuThang = hdDAO.getDoanhThuCuaThang(thangDuocChon, namDuocChon);
        int soHoaDonThang = hdDAO.getSoHoaDonTheoThang(thangDuocChon,namDuocChon);
        double giaTriTrungBinh = tongDoanhThuThang/soHoaDonThang;
        double doanhThuTrungBinhNgay = hdDAO.getDoanhThuTrungBinhTheoNgay(thangDuocChon,namDuocChon);
        int soKhachHangCuaThang = khDAO.getSoKhachHangCuaThang(thangDuocChon, namDuocChon);
        
        addRow(panel, "Tổng doanh thu tháng:", df.format(tongDoanhThuThang), labelFont, valueFont,1);
        addRow(panel, "Tổng số hóa đơn:", String.valueOf(soHoaDonThang), labelFont, valueFont,2);
        addRow(panel, "Tổng sổ khách hàng (có tài khoản) đã mua:",String.valueOf(soKhachHangCuaThang), labelFont, valueFont,3);
        addRow(panel, "Giá trị trung bình của 1 hóa đơn:", df.format(giaTriTrungBinh), labelFont, valueFont,4);
        addRow(panel, "Doanh thu trung bình mỗi ngày:", df.format(doanhThuTrungBinhNgay), labelFont, valueFont,5);
        
        for(int i = 0;i <5; i++) panel.add(new JPanel());
        
        return panel;
    }
	    
    private static void addRow(JPanel panel, String label, String value, 
                               Font labelFont, Font valueFont,int row) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(labelFont);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(valueFont);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        JPanel pnlRow = new JPanel(new BorderLayout());
        pnlRow.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        if(row%2 == 0) pnlRow.setBackground(new Color(230, 230, 230));
        
        pnlRow.add(lblLabel,BorderLayout.WEST);
        pnlRow.add(lblValue,BorderLayout.EAST);
        panel.add(pnlRow);
    }
	    
    public void refreshWestPanel() {
        this.remove(pnlWestPanel); // xóa panel cũ
        pnlWestPanel = createWestPanel(); // tạo panel mới với dữ liệu namHienTai mới
        this.add(pnlWestPanel, BorderLayout.WEST); // add lại vào frame đúng vị trí
        this.revalidate(); // cập nhật layout
        this.repaint(); // vẽ lại frame
    }


}

