package app. DAO;

import app.ConnectDB. ConnectDB;
import app.Entity.ChucVu;
import java.sql.*;
import java.util.ArrayList;

public class ChucVuDAO {
    public ChucVuDAO() {

    }
   
    /**
     * Lấy danh sách các chức vụ
     */
    public ArrayList<ChucVu> getAllChucVu() {
        ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE isActive = 1";

        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt. executeQuery(sql)
        ) {
            while (rs. next()) {
                ChucVu chucVu = new ChucVu();
                chucVu.setMaChucVu(rs.getString("maChucVu"));
                chucVu.setTenChucVu(rs.getString("tenChucVu"));
                chucVu.setIsActive(rs.getBoolean("isActive"));
                danhSachChucVu. add(chucVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChucVu;
    }
    
    /**
     * Tìm chức vụ theo mã
     */
    public ChucVu getById(String maChucVu) {
        String sql = "SELECT * FROM ChucVu WHERE maChucVu = ?";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, maChucVu);
            
            try (ResultSet rs = stmt. executeQuery()) {
                if (rs. next()) {
                    ChucVu chucVu = new ChucVu();
                    chucVu.setMaChucVu(rs.getString("maChucVu"));
                    chucVu.setTenChucVu(rs.getString("tenChucVu"));
                    chucVu.setIsActive(rs. getBoolean("isActive"));
                    return chucVu;
                }
            }
        } catch (SQLException e) {
            e. printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm chức vụ theo tên (chính xác)
     */
    public ChucVu getByName(String chucVU) {
        String sql = "SELECT * FROM ChucVu WHERE tenChucVu = ?  AND isActive = 1";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt. setString(1, chucVU);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ChucVu chucVu = new ChucVu();
                    chucVu.setMaChucVu(rs.getString("maChucVu"));
                    chucVu.setTenChucVu(rs.getString("tenChucVu"));
                    chucVu.setIsActive(rs.getBoolean("isActive"));
                    return chucVu;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm kiếm chức vụ theo tên
     */
    public ArrayList<ChucVu> findByName(String tenChucVu) {
        ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE tenChucVu LIKE ?";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "%" + tenChucVu + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChucVu chucVu = new ChucVu();
                    chucVu.setMaChucVu(rs.getString("maChucVu"));
                    chucVu.setTenChucVu(rs.getString("tenChucVu"));
                    chucVu.setIsActive(rs.getBoolean("isActive"));
                    danhSachChucVu.add(chucVu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChucVu;
    }
    
    /**
     * Thêm chức vụ mới
     */
    public boolean insert(ChucVu chucVu) {
        String sql = "INSERT INTO ChucVu (maChucVu, tenChucVu, isActive) VALUES (?, ?, ?)";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, chucVu.getMaChucVu());
            stmt.setString(2, chucVu.getTenChucVu());
            stmt.setBoolean(3, chucVu.isIsActive());
            
            return stmt. executeUpdate() > 0;
        } catch (SQLException e) {
            e. printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin chức vụ
     */
    public boolean update(ChucVu chucVu) {
        String sql = "UPDATE ChucVu SET tenChucVu = ?, isActive = ? WHERE maChucVu = ?";
        
        try (
            Connection conn = ConnectDB. getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, chucVu.getTenChucVu());
            stmt.setBoolean(2, chucVu. isIsActive());
            stmt.setString(3, chucVu.getMaChucVu());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa chức vụ (soft delete)
     */
    public boolean delete(String maChucVu) {
        String checkSql = "SELECT COUNT(*) FROM NhanVien WHERE maChucVu = ?  AND isActive = 1";
        String deleteSql = "UPDATE ChucVu SET isActive = 0 WHERE maChucVu = ? ";
        
        try (Connection conn = ConnectDB.getInstance().getConnection()) {
            // Kiểm tra
            try (PreparedStatement checkStmt = conn. prepareStatement(checkSql)) {
                checkStmt. setString(1, maChucVu);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            // Xóa
            try (PreparedStatement deleteStmt = conn. prepareStatement(deleteSql)) {
                deleteStmt. setString(1, maChucVu);
                return deleteStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ChucVu> getAllInactiveChucVu() {
        ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE isActive = 0";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                ChucVu chucVu = new ChucVu();
                chucVu.setMaChucVu(rs.getString("maChucVu"));
                chucVu.setTenChucVu(rs.getString("tenChucVu"));
                chucVu.setIsActive(rs. getBoolean("isActive"));
                danhSachChucVu.add(chucVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChucVu;
    }

    public boolean reactivateChucVu(String maCV) {
        String sql = "UPDATE ChucVu SET isActive = 1 WHERE maChucVu = ?";
        
        try (
            Connection conn = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, maCV);
            return stmt. executeUpdate() > 0;
        } catch (SQLException e) {
            e. printStackTrace();
            return false;
        }
    }
}