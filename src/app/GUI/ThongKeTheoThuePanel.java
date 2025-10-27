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

import javax.swing.*;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import app.DAO.HoaDonDAO;
import app.Entity.DoanhThuTheoThang;
import app.Entity.HoaDon;

public class ThongKeTheoThuePanel extends JPanel implements ActionListener {
	
	private JLabel lblChonNam; 
	private JComboBox<Integer> cboNam; 
	private JLabel lblTongDoanhThu;
	private CategoryChart chart;
	private XChartPanel<CategoryChart> chartPanel;
	
	private String[] months;
	private Double[] revenue;
	private int namHienTai = 2025; // Năm mặc định
	private DecimalFormat df = new DecimalFormat("#,###.##");
    
	public ThongKeTheoThuePanel() {
		
		setLayout(new BorderLayout());
		
		// Tạo panel chọn năm ở phía North
		JPanel northPanel = createNorthPanel();
		add(northPanel, BorderLayout.NORTH);
		
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
		add(chartPanel, BorderLayout.CENTER);
		
		// Tạo panel tổng doanh thu ở phía South
		JPanel southPanel = createSouthPanel();
		add(southPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Tạo panel chọn năm
	 */
	private JPanel createNorthPanel() {
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
		
		return panel;
	}
	
	/**
	 * Tạo panel hiển thị tổng doanh thu
	 */
	private JPanel createSouthPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
		panel.setBackground(new Color(240, 248, 255)); // Alice Blue
		
		JLabel lblTitle = new JLabel("Tổng doanh thu năm " + namHienTai + ":");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
		
		double tongDoanhThu = tinhTongDoanhThu();
		lblTongDoanhThu = new JLabel(df.format(tongDoanhThu) + " VNĐ");
		lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 20));
		lblTongDoanhThu.setForeground(new Color(0, 128, 0)); // Màu xanh lá
		
		panel.add(lblTitle);
		panel.add(lblTongDoanhThu);
		
		return panel;
	}
	
	/**
	 * Tính tổng doanh thu từ mảng revenue
	 */
	private double tinhTongDoanhThu() {
		double tong = 0;
		if(revenue != null) {
			for(Double dt : revenue) {
				tong += dt;
			}
		}
		return tong * 1000000;
	}
	
	private CategoryChart createChart() {
		CategoryChart chart = new CategoryChartBuilder()
			.width(600)
			.height(400)
			.title("Doanh thu nhà thuốc năm " + namHienTai)
			.xAxisTitle("Tháng")
			.yAxisTitle("Doanh thu (triệu)")
			.build();
		
		loadDoanhThuTheoNamData(namHienTai);
		
        chart.addSeries("Doanh thu " + namHienTai, Arrays.asList(months), Arrays.asList(revenue));
        
		return chart;
	}
	
	
	private void loadDoanhThuTheoNamData(int nam) {
		ArrayList<DoanhThuTheoThang> dsDT = new ArrayList<>();
		HoaDonDAO hdDAO = new HoaDonDAO();
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
	}
	
	/**
	 * Cập nhật hiển thị tổng doanh thu
	 */
	private void updateTongDoanhThu() {
		double tongDoanhThu = tinhTongDoanhThu();
		lblTongDoanhThu.setText(df.format(tongDoanhThu) + " VNĐ");
		
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
		}
	}

}
