package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import app.ConnectDB.ConnectDB;
import app.Entity.Thue;

/**
 * DAO (Data Access Object) cho thực thể Thue.
 * Chịu trách nhiệm xử lý tất cả các thao tác cơ sở dữ liệu liên quan đến thuế.
 */
public class ThueDAO {

    /**
     * Lấy danh sách tất cả các mức thuế đang hoạt động từ cơ sở dữ liệu.
     * @return Một ArrayList chứa các đối tượng Thue.
     */
    public ArrayList<Thue> getAllThue() {
        ArrayList<Thue> dsThue = new ArrayList<>();
        String sql = "SELECT * FROM Thue WHERE isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maThue = rs.getString("maThue");
                float mucThue = rs.getFloat("mucThue");
                Date ngayApDung = rs.getDate("ngayApDung");
                Date ngayKetThuc = rs.getDate("ngayKetThuc");
                boolean isActive = rs.getBoolean("isActive");

                Thue thue = new Thue(maThue, mucThue, ngayApDung, ngayKetThuc, isActive);
                dsThue.add(thue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsThue;
    }

    /**
     * Lấy thông tin một mức thuế dựa vào mã thuế (ID).
     * @param id Mã của mức thuế cần tìm.
     * @return Một đối tượng Thue nếu tìm thấy, ngược lại trả về null.
     */
    public Thue getThueById(String id) {
        String sql = "SELECT * FROM Thue WHERE maThue = ?";
        Thue thue = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maThue = rs.getString("maThue");
                    float mucThue = rs.getFloat("mucThue");
                    Date ngayApDung = rs.getDate("ngayApDung");
                    Date ngayKetThuc = rs.getDate("ngayKetThuc");
                    boolean isActive = rs.getBoolean("isActive");
                    
                    thue = new Thue(maThue, mucThue, ngayApDung, ngayKetThuc, isActive);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thue;
    }

    /**
     * Thêm một mức thuế mới vào cơ sở dữ liệu.
     * @param thue Đối tượng Thue cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addThue(Thue thue) {
        String sql = "INSERT INTO Thue (maThue, mucThue, ngayApDung, ngayKetThuc, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, thue.getMaThue());
            stmt.setFloat(2, thue.getMucThue());

            // Chuyển đổi java.util.Date sang java.sql.Date và xử lý null
            if (thue.getNgayApDung() != null) {
                stmt.setDate(3, new java.sql.Date(thue.getNgayApDung().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (thue.getNgayKetThuc() != null) {
                stmt.setDate(4, new java.sql.Date(thue.getNgayKetThuc().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setBoolean(5, thue.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cập nhật thông tin của một mức thuế đã có trong cơ sở dữ liệu.
     * @param thue Đối tượng Thue chứa thông tin đã được cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateThue(Thue thue) {
        String sql = "UPDATE Thue SET mucThue = ?, ngayApDung = ?, ngayKetThuc = ?, isActive = ? WHERE maThue = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setFloat(1, thue.getMucThue());

            if (thue.getNgayApDung() != null) {
                stmt.setDate(2, new java.sql.Date(thue.getNgayApDung().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            if (thue.getNgayKetThuc() != null) {
                stmt.setDate(3, new java.sql.Date(thue.getNgayKetThuc().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setBoolean(4, thue.isIsActive());
            stmt.setString(5, thue.getMaThue());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Xóa một mức thuế khỏi hệ thống bằng cách cập nhật trạng thái isActive = 0 (xóa mềm).
     * @param id Mã của mức thuế cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteThue(String id) {
        String sql = "UPDATE Thue SET isActive = 0 WHERE maThue = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}