package app.Main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import app.ConnectDB.ConnectDB;
import app.GUI.DangNhap; // hoặc LoginGUI nếu bạn đã đổi tên lớp này

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Kết nối CSDL
                ConnectDB.getInstance().connect();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra lại.",
                    "Lỗi Kết Nối",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Mở giao diện đăng nhập
            new DangNhap(); // hoặc new LoginGUI(); nếu bạn dùng tên khác
        });
    }
}
