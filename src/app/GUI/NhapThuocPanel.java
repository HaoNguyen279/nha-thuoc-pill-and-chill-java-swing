package app.GUI;

// Temporarily disabled due to missing Apache POI dependencies
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class NhapThuocPanel extends JPanel {
    
    public NhapThuocPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JLabel lblMessage = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Chức năng nhập thuốc từ Excel</h2>" +
            "<p>Chức năng này tạm thời bị vô hiệu hóa do thiếu thư viện Apache POI.</p>" +
            "<p>Vui lòng thêm các dependencies sau vào thư mục lib:</p>" +
            "<ul>" +
            "<li>apache-poi-*.jar</li>" +
            "<li>apache-commons-math3-*.jar</li>" +
            "</ul>" +
            "</div></html>", 
            JLabel.CENTER
        );
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 14));
        lblMessage.setForeground(Color.DARK_GRAY);
        
        add(lblMessage, BorderLayout.CENTER);
    }
}