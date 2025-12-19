package app.DAO;

import app.ConnectDB.ConnectDB;
import app.Entity.ChucVu;
import java.sql.*;
import java.util.ArrayList;

public class ChucVuDAO {
    private Connection conn;
    
    public ChucVuDAO() {
//    	this.conn = ConnectDB.getInstance().getConnection();
    }
   
    
    /**
     * Lấy danh sách các chức vụ
     */
    public ArrayList<ChucVu> getAllChucVu() {
        ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE isActive = 1";
        Connection con = ConnectDB.getInstance().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try 
            {
        	stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
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
    
    public ChucVu getByName(String chucVU) {
        String sql = "SELECT * FROM ChucVu WHERE tenChucVu = ? and isActive = 1";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, chucVU);
            
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
        Connection con = ConnectDB.getInstance().getConnection();
        String checkSql = """
            SELECT COUNT(*) 
            FROM NhanVien 
            WHERE maChucVu = ? AND isActive = 1
        """;
        String deleteSql = """
            UPDATE ChucVu 
            SET isActive = 0 
            WHERE maChucVu = ?
        """;
        try (
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            PreparedStatement deleteStmt = con.prepareStatement(deleteSql)
        ){
            checkStmt.setString(1, maChucVu);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Có nhân viên → không cho xóa
                return false;
            }
            deleteStmt.setString(1, maChucVu);
            return deleteStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



	public ArrayList<ChucVu> getAllInactiveChucVu() {
		ArrayList<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE isActive = 0";
        Connection con = ConnectDB.getInstance().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try 
            {
        	stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
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


	public boolean reactivateChucVu(String maCV) {
		String sql = "UPDATE ChucVu SET isActive = 1 WHERE maChucVu = ?";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1,maCV);
            int n = stmt.executeUpdate();
            System.out.println("   → Reactivated Thuoc: " + maCV + " (rows: " + n + ")");
            return n > 0;
        } catch (SQLException e) {
            System.err.println("   ❌ Error reactivating Thuoc: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
	}

}