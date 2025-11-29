package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.TaiKhoan;

/**
 * DAO (Data Access Object) cho thực thể TaiKhoan.
 * Chịu trách nhiệm xử lý tất cả các thao tác cơ sở dữ liệu liên quan đến tài khoản người dùng.
 */
public class TaiKhoanDAO {

    /**
     * Kiểm tra thông tin đăng nhập và trạng thái hoạt động của tài khoản.
     * @param maNV Mã nhân viên (tên đăng nhập).
     * @param matKhau Mật khẩu.
     * @return Đối tượng TaiKhoan nếu đăng nhập thành công, ngược lại trả về null.
     */
    public TaiKhoan kiemTraDangNhap(String maNV, String matKhau) {
        String sql = "SELECT * FROM TaiKhoan WHERE maNV = ? AND matKhau = ? AND isActive = 1";
        TaiKhoan tk = null;

        // Make sure we have a fresh connection
        ConnectDB.connect();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            stmt.setString(2, matKhau);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                tk = new TaiKhoan(
                    rs.getString("maNV"),
                    rs.getString("matKhau"),
                    rs.getBoolean("isActive")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng ResultSet và Statement, KHÔNG đóng Connection
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tk;
    }

    /**
     * Lấy thông tin một tài khoản dựa vào mã nhân viên (ID).
     * @param maNV Mã của tài khoản cần tìm.
     * @return Một đối tượng TaiKhoan nếu tìm thấy, ngược lại trả về null.
     */
    public TaiKhoan getTaiKhoanById(String maNV) {
        String sql = "SELECT * FROM TaiKhoan WHERE maNV = ?";
        TaiKhoan tk = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maNV);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan(
                        rs.getString("maNV"),
                        rs.getString("matKhau"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }

    /**
     * Lấy danh sách tất cả các tài khoản đang hoạt động.
     * @return Một ArrayList chứa các đối tượng TaiKhoan.
     */
    public ArrayList<TaiKhoan> getAllTaiKhoan() {
        ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE isActive = 1";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan(
                    rs.getString("maNV"),
                    rs.getString("matKhau"),
                    rs.getBoolean("isActive")
                );
                dsTaiKhoan.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsTaiKhoan;
    }

    /**
     * Thêm một tài khoản mới vào cơ sở dữ liệu.
     * @param taiKhoan Đối tượng TaiKhoan cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TaiKhoan (maNV, matKhau, isActive) VALUES (?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, taiKhoan.getMaNV());
            stmt.setString(2, taiKhoan.getMatKhau());
            stmt.setBoolean(3, taiKhoan.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cập nhật thông tin của một tài khoản (thường là mật khẩu hoặc trạng thái).
     * @param taiKhoan Đối tượng TaiKhoan chứa thông tin đã được cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "UPDATE TaiKhoan SET matKhau = ?, isActive = ? WHERE maNV = ?";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, taiKhoan.getMatKhau());
            stmt.setBoolean(2, taiKhoan.isIsActive());
            stmt.setString(3, taiKhoan.getMaNV());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Vô hiệu hóa một tài khoản bằng cách cập nhật trạng thái isActive = 0 (xóa mềm).
     * @param maNV Mã của tài khoản cần vô hiệu hóa.
     * @return true nếu thành công, false nếu thất bại.
     */
    public boolean deleteTaiKhoan(String maNV) {
        String sql = "UPDATE TaiKhoan SET isActive = 0 WHERE maNV = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maNV);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Lấy tên nhân viên từ mã nhân viên.
     * @param maNV Mã nhân viên cần tìm.
     * @return Tên nhân viên nếu tìm thấy, ngược lại trả về null.
     */
    public String getTenNhanVienByMaNV(String maNV) {
        String sql = "SELECT tenNV FROM NhanVien WHERE maNV = ?";
        String tenNV = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maNV);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tenNV = rs.getString("tenNV");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenNV;
    }
}