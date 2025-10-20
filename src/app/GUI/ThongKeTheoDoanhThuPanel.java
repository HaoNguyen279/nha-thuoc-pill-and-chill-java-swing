package app.GUI;
import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.*;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;

public class ThongKeTheoDoanhThuPanel extends JPanel {
	
	CategoryChart chart;
	XChartPanel<CategoryChart> chartPanel;
	
	public ThongKeTheoDoanhThuPanel() {
		setLayout(new BorderLayout());
		chart = createChart();
		chartPanel = new XChartPanel<CategoryChart>(chart);
		add(chartPanel);
	}
	
	
	
	private CategoryChart createChart() {
		CategoryChart chart = new CategoryChartBuilder()
			.width(600)
			.height(400)
			.title("Doanh thu nhà thuốc")
			.xAxisTitle("Tháng")
			.yAxisTitle("Doanh thu (triệu)")
			.build();
		
        String[] months = new String[] { "Tháng 1", "Tháng 2", "Tháng 3", 
                "Tháng 4", "Tháng 5", "Tháng 6" };
        Integer[] sales = new Integer[] { 100, 150, 120, 180, 200, 170 };
		
        chart.addSeries("Doanh thu 2025", Arrays.asList(months), Arrays.asList(sales));
		return chart;
	}

}
