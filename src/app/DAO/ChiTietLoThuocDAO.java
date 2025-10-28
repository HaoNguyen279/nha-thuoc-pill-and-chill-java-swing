package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietLoThuoc;

/**
 * DAO (Data Access Object) cho thực thể ChiTietLoThuoc.
 * Chịu trách nhiệm cho tất cả các hoạt động cơ sở dữ liệu liên quan đến chi tiết lô thuốc.
 */
public class ChiTietLoThuocDAO {

    /**
     * Lấy danh sách tất cả các chi tiết lô thuốc đang hoạt động (isActive = 1).
     * @return ArrayList chứa các đối tượng ChiTietLoThuoc.
     */
    public ArrayList<ChiTietLoThuoc> getAllActiveChiTietLoThuoc() {
        ArrayList<ChiTietLoThuoc> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ChiTietLoThuoc ct = new ChiTietLoThuoc(
                    rs.getString("maLo"),
                    rs.getString("maThuoc"),
                    rs.getDate("ngaySanXuat"),
                    rs.getDate("hanSuDung"),
                    rs.getBoolean("isActive")
                );
                dsChiTiet.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }
    
    /**
     * Lấy một chi tiết lô thuốc cụ thể dựa trên khóa chính tổng hợp (mã lô và mã thuốc).
     * @param maLo Mã lô thuốc.
     * @param maThuoc Mã thuốc.
     * @return Đối tượng ChiTietLoThuoc nếu tìm thấy, ngược lại trả về null.
     */
    public ChiTietLoThuoc getChiTietLoThuocById(String maLo, String maThuoc) {
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE maLo = ? AND maThuoc = ?";
        ChiTietLoThuoc chiTiet = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLo);
            stmt.setString(2, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    chiTiet = new ChiTietLoThuoc(
                        rs.getString("maLo"),
                        rs.getString("maThuoc"),
                        rs.getDate("ngaySanXuat"),
                        rs.getDate("hanSuDung"),
                        rs.getInt("soLuong"),
                        rs.getInt("giaNhap"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTiet;
    }

    /**
     * Thêm một chi tiết lô thuốc mới vào cơ sở dữ liệu.
     * @param chiTiet Đối tượng ChiTietLoThuoc cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean create(ChiTietLoThuoc chiTiet) {
        String sql = "INSERT INTO ChiTietLoThuoc (maLo, maThuoc, ngaySanXuat, hanSuDung,soLuong,giaNhap, isActive) VALUES (?, ?, ?, ?, ?,?,?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, chiTiet.getMaLo());
            stmt.setString(2, chiTiet.getMaThuoc());
            stmt.setDate(3, new java.sql.Date(chiTiet.getNgaySanXuat().getTime()));
            stmt.setDate(4, new java.sql.Date(chiTiet.getHanSuDung().getTime()));
            stmt.setInt(5, chiTiet.getSoLuong());
            stmt.setFloat(6,(float) chiTiet.getGiaNhap());
            stmt.setBoolean(7, chiTiet.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cập nhật thông tin của một chi tiết lô thuốc.
     * @param chiTiet Đối tượng ChiTietLoThuoc chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean update(ChiTietLoThuoc chiTiet) {
        String sql = "UPDATE ChiTietLoThuoc SET ngaySanXuat = ?, hanSuDung = ?, soLuong = ?, giaNhap = ?, isActive = ? WHERE maLo = ? AND maThuoc = ?";
        
        Connection con = ConnectDB.getConnection(); // Dùng singleton connection
        PreparedStatement stmt = null;
        int n = 0;
        
        try {
            stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(chiTiet.getNgaySanXuat().getTime()));
            stmt.setDate(2, new java.sql.Date(chiTiet.getHanSuDung().getTime()));
            stmt.setInt(3, chiTiet.getSoLuong());
            stmt.setDouble(4, chiTiet.getGiaNhap());
            stmt.setBoolean(5, chiTiet.isIsActive());
            stmt.setString(6, chiTiet.getMaLo());
            stmt.setString(7, chiTiet.getMaThuoc());
            
            n = stmt.executeUpdate();
            System.out.println("   → Updated " + n + " rows in ChiTietLoThuoc");
            
        } catch (SQLException e) {
            System.err.println("   ❌ Error updating CTLT: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                // ⚠️ KHÔNG đóng Connection!
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    /**
     * Xóa mềm một chi tiết lô thuốc bằng cách đặt isActive = 0.
     * @param maLo Mã lô của chi tiết cần xóa.
     * @param maThuoc Mã thuốc của chi tiết cần xóa.
     * @return true nếu thành công, false nếu thất bại.
     */
    public boolean softDelete( String maThuoc) {
        String sql = "UPDATE ChiTietLoThuoc SET soLuong = 0,isActive = 0 WHERE maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
           
            stmt.setString(1, maThuoc);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<ChiTietLoThuoc> getAllInactiveChiTietLoThuoc() {
        ArrayList<ChiTietLoThuoc> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE isActive = 0";
        Connection con = ConnectDB.getConnection();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ChiTietLoThuoc ct = new ChiTietLoThuoc(
                    rs.getString("maLo"),
                    rs.getString("maThuoc"),
                    rs.getDate("ngaySanXuat"),
                    rs.getDate("hanSuDung"),
                    rs.getBoolean("isActive")
                );
                ct.setSoLuong(rs.getInt("soLuong"));
                ct.setGiaNhap(rs.getDouble("giaNhap"));
                dsChiTiet.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    /**
     * Lấy CTLT theo ID (bao gồm cả inactive)
     */
    public ChiTietLoThuoc getChiTietLoThuocByIdIncludeInactive(String maLo, String maThuoc) {
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE maLo = ? AND maThuoc = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLo);
            stmt.setString(2, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ChiTietLoThuoc ct = new ChiTietLoThuoc(
                        rs.getString("maLo"),
                        rs.getString("maThuoc"),
                        rs.getDate("ngaySanXuat"),
                        rs.getDate("hanSuDung"),
                        rs.getBoolean("isActive")
                    );
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setGiaNhap(rs.getDouble("giaNhap"));
                    return ct;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
