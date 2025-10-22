package app.DAO;

import app.ConnectDB.ConnectDB;
import app.Entity.ChucVu;
import java.sql.*;
import java.util.ArrayList;

public class ChucVuDAO {
    private Connection conn;
    
    public ChucVuDAO() {
    	this.conn = ConnectDB.getInstance().getConnection();
    }
   
    
    /**
     * Lấy danh sách các chức vụ
     */
    public ArrayList<ChucVu> getAllChucVu() {
        ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE isActive = 1";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ChucVu chucVu = new ChucVu();
                chucVu.setMaChucVu(rs.getString("maChucVu"));
                chucVu.setTenChucVu(rs.getString("tenChucVu"));
                chucVu.setIsActive(rs.getBoolean("isActive"));
                danhSachChucVu.add(chucVu);
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
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maChucVu);
            
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
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chucVu.getMaChucVu());
            stmt.setString(2, chucVu.getTenChucVu());
            stmt.setBoolean(3, chucVu.isIsActive());
            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin chức vụ
     */
    public boolean update(ChucVu chucVu) {
        String sql = "UPDATE ChucVu SET tenChucVu = ?, isActive = ? WHERE maChucVu = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chucVu.getTenChucVu());
            stmt.setBoolean(2, chucVu.isIsActive());
            stmt.setString(3, chucVu.getMaChucVu());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa chức vụ (cập nhật trạng thái isActive thành false)
     */
    public boolean delete(String maChucVu) {
        String sql = "UPDATE ChucVu SET isActive = 0 WHERE maChucVu = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maChucVu);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}