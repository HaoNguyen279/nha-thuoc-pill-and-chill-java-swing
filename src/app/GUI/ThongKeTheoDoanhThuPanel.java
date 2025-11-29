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
import app.Entity.DoanhThuTheoThang;


public class ThongKeTheoDoanhThuPanel extends JPanel implements ActionListener {
	private JLabel lblChonNam; 
	private JLabel lblChonThang;
	
	private JComboBox<Integer> cboNam; 
	private JComboBox<Integer> cboThang; 
	
	private JLabel lblTongDoanhThu;
	private CategoryChart chart;
	private XChartPanel<CategoryChart> chartPanel;
	private JPanel pnlWestPanel;
	
	private String[] months;
	private Double[] revenue;
	private int namHienTai = 2025; // Năm mặc định
	private DecimalFormat df = new DecimalFormat("#,###.##' VND'");
    private HoaDonDAO hdDAO = new HoaDonDAO();
    private KhachHangDAO khDAO = new KhachHangDAO();
    
	public ThongKeTheoDoanhThuPanel() {
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
//        styler.setXAxisTitleColor(Color.RED);
//        styler.setYAxisTitleColor(Color.GREEN);
        styler.setSeriesColors(new Color[]{
                new Color(100, 149, 237), // Xanh 
                new Color(255, 127, 80),  // Cam
                new Color(144, 238, 144)  // Lục
        });
        
		JPanel pnlNorthPanel = createNorthPanel();
		add(pnlNorthPanel, BorderLayout.NORTH);
        
		add(chartPanel, BorderLayout.CENTER);
		
		pnlWestPanel = createWestPanel(1);
		add(pnlWestPanel, BorderLayout.WEST);
		
		JPanel pnlSouthPanel = createSouthPanel();
		add(pnlSouthPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Tạo panel chọn năm
	 */
	private JPanel createNorthPanel() {
		JPanel pnlMain = new JPanel(new BorderLayout());
        JLabel lblTieuDe = new JLabel("DOANH THU NĂM", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		lblChonNam = new JLabel("Chọn năm:");
		lblChonNam.setFont(new Font("Arial", Font.BOLD, 16));
		
		cboNam = new JComboBox<>();
		// Thêm các năm từ 2020 đến 2025
		for(int i = 2024; i <= 2025; i++) {
			cboNam.addItem(i);
		}
		cboNam.setSelectedItem(2025); // Chọn mặc định năm 2025
		cboNam.setFont(new Font("Arial", Font.PLAIN, 14));
		cboNam.addActionListener(this);
		
		panel.add(lblChonNam);
		panel.add(cboNam);
		
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
		lblTongDoanhThu.setForeground(new Color(0, 128, 0)); // Màu xanh lá
		
		panel.add(lblTitle);
		panel.add(lblTongDoanhThu);
		
		return panel;
	}
	
	private CategoryChart createChart() {
		CategoryChart chart = new CategoryChartBuilder()
			.width(600)
			.height(400)
			.title("Doanh thu nhà thuốc theo tháng của năm " + namHienTai)
			.xAxisTitle("Tháng")
			.yAxisTitle("Doanh thu (triệu)")
			.build();
		
		loadDoanhThuTheoNamData(namHienTai);
		chart.getStyler().setOverlapped(false);
	    chart.getStyler().setLegendVisible(true);
	    chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Bar);

        chart.addSeries("Doanh thu " + namHienTai, Arrays.asList(months), Arrays.asList(revenue));

		return chart;
	}
	
	
	private void loadDoanhThuTheoNamData(int nam) {
		ArrayList<DoanhThuTheoThang> dsDT = new ArrayList<>();

		dsDT = hdDAO.thongKeDoanhThuTheoThang(nam);
		int i = 0;
		months = new String[dsDT.size()];
		revenue = new Double[dsDT.size()];
		for(DoanhThuTheoThang item : dsDT) {
			months[i] = item.getTenThang();
			revenue[i] = item.getDoanhThu()/1000000;
			i++;
		}
	}
	
	/**
	 * Cập nhật biểu đồ khi chọn năm mới
	 */
	private void updateChart(int nam) {
		namHienTai = nam;
		loadDoanhThuTheoNamData(nam);
		
		// Xóa series cũ và thêm series mới
		chart.getSeriesMap().clear();
		chart.addSeries("Doanh thu " + nam, Arrays.asList(months), Arrays.asList(revenue));
		chart.setTitle("Doanh thu nhà thuốc năm " + nam);
		
		// Cập nhật tổng doanh thu
		updateTongDoanhThu();
		
		// Cập nhật lại chartPanel
		chartPanel.repaint();
		refreshWestPanel();
	}
	
	/**
	 * Cập nhật hiển thị tổng doanh thu
	 */
	private void updateTongDoanhThu() {
		lblTongDoanhThu.setText(df.format(hdDAO.getDoanhThuCuaNam(namHienTai)));
		
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
		loadDoanhThuTheoNamData(namDuocChon);
		chart.getSeriesMap().clear();
        chart.addSeries("Doanh thu " + namDuocChon, Arrays.asList(months), Arrays.asList(revenue));
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
	}
	
	 public  JPanel createWestPanel(int option) {
	        // Tạo panel chính với GridLayout
	        JPanel panel = new JPanel(new GridLayout(10,1,0,0));
	        panel.setBackground(Color.WHITE);
	        
	        // Font cho labels
	        Font labelFont = new Font("Arial", Font.PLAIN, 14);
	        Font valueFont = new Font("Arial", Font.BOLD, 14);

	        // Thêm các dòng dữ liệu
	        double tongDoanhThu = hdDAO.getDoanhThuCuaNam(namHienTai);
	        int soHoaDon = hdDAO.getSoHoaDonTheoNam(namHienTai);
	        double giaTriTrungBinh = tongDoanhThu/soHoaDon;
	        double doanhThuTrungBinhThang = hdDAO.getDoanhThuTrungBinhTheoThang(namHienTai);
	        
	        addRow(panel, "Tổng doanh thu:", df.format(tongDoanhThu), labelFont, valueFont,1);
	        addRow(panel, "Tổng số hóa đơn:", String.valueOf(soHoaDon), labelFont, valueFont,2);
	        addRow(panel, "Tổng sổ khách hàng (có tài khoản) đã mua:",String.valueOf(khDAO.getSoKhachHangCuaNam(namHienTai)) , labelFont, valueFont,3);
	        addRow(panel, "Giá trị trung bình của 1 hóa đơn:", df.format(giaTriTrungBinh), labelFont, valueFont,4);
	        addRow(panel, "Doanh thu trung bình mỗi tháng:", df.format(doanhThuTrungBinhThang), labelFont, valueFont,5);
	        
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
	        pnlWestPanel = createWestPanel(1); // tạo panel mới với dữ liệu namHienTai mới
	        this.add(pnlWestPanel, BorderLayout.WEST); // add lại vào frame đúng vị trí
	        this.revalidate(); // cập nhật layout
	        this.repaint(); // vẽ lại frame
	    }


}
