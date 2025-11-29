package app.Main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import app.ConnectDB.ConnectDB;
import app.GUI.DangNhapFrame;
import app.GUI.MainFrame;

public class App {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ConnectDB.getInstance().connect();
                
                new DangNhapFrame(); 
            }catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Không thể kết nối đến cơ sở dữ liệu. Vui lòng kiểm tra lại.",
                    "Lỗi Kết Nối",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
                
            }
        });
    }
}