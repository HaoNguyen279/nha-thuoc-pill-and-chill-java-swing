package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.LoThuoc;
import app.Entity.LoThuocHetHan;

/**
 * DAO (Data Access Object) for the LoThuoc entity.
 * Handles all database operations related to medicine batches.
 */
public class LoThuocDAO {

    /**
     * Retrieves a list of all active medicine batches.
     * @return An ArrayList of LoThuoc objects.
     */
    public ArrayList<LoThuoc> getAllLoThuoc() {
        ArrayList<LoThuoc> dsLoThuoc = new ArrayList<>();
        String sql = "SELECT * FROM LoThuoc WHERE isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LoThuoc lt = new LoThuoc(
                    rs.getString("maLo"),
                    rs.getString("maNSX"),
                    rs.getBoolean("isActive")
                );
                dsLoThuoc.add(lt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLoThuoc;
    }

    /**
     * Retrieves a single medicine batch by its ID.
     * @param id The ID of the medicine batch to find.
     * @return A LoThuoc object if found, otherwise null.
     */
    public LoThuoc getLoThuocById(String id) {
        String sql = "SELECT * FROM LoThuoc WHERE maLo = ?";
        LoThuoc lt = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lt = new LoThuoc(
                        rs.getString("maLo"),
                        rs.getString("maNSX"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lt;
    }

    /**
     * Adds a new medicine batch to the database.
     * @param loThuoc The LoThuoc object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addLoThuoc(LoThuoc loThuoc) {
        String sql = "INSERT INTO LoThuoc (maLo, maNSX, isActive) VALUES (?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, loThuoc.getMaLo());
            stmt.setString(2, loThuoc.getMaNSX());
            stmt.setBoolean(3, loThuoc.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing medicine batch's information.
     * @param loThuoc The LoThuoc object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateLoThuoc(LoThuoc loThuoc) {
        String sql = "UPDATE LoThuoc SET maNSX = ?, isActive = ? WHERE maLo = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, loThuoc.getMaNSX());
            stmt.setBoolean(2, loThuoc.isIsActive());
            stmt.setString(3, loThuoc.getMaLo());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a medicine batch by setting its isActive flag to false (soft delete).
     * @param id The ID of the medicine batch to deactivate.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteLoThuoc(String id) {
        String sql = "UPDATE LoThuoc SET isActive = 0 WHERE maLo = ?";
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
    
    public ArrayList<LoThuoc> getAllInactiveLoThuoc() {
        ArrayList<LoThuoc> dsLo = new ArrayList<>();
        String sql = "SELECT * FROM LoThuoc WHERE isActive = 0";
        Connection con = ConnectDB.getConnection();
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LoThuoc lo = new LoThuoc(
                    rs.getString("maLo"),
                    rs.getString("maNSX"),
                    rs.getBoolean("isActive")
                );
                dsLo.add(lo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLo;
    }

    /**
     * Kích hoạt lại lô đã bị xóa mềm
     */
    public boolean reactivateLoThuoc(String maLo) {
        String sql = "UPDATE LoThuoc SET isActive = 1 WHERE maLo = ?";
        Connection con = ConnectDB.getConnection();
       
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLo);
            int n = stmt.executeUpdate();
            System.out.println("   → Reactivated LoThuoc: " + maLo + " (rows: " + n + ")");
            return n > 0;
        } catch (SQLException e) {
            System.err.println("   ❌ Error reactivating LoThuoc: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get các lô thuốc đã hết hạn 
    public ArrayList<LoThuocHetHan> getCacLoThuocHetHan() {
        ArrayList<LoThuocHetHan> dsLo = new ArrayList<>();
        String sql =  "{CALL sp_GetLoThuocDaHetHan}";
        Connection con = ConnectDB.getConnection();
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LoThuocHetHan lo = new LoThuocHetHan(
                    rs.getString("maLo"),
                    rs.getString("maThuoc"),
                    rs.getString("tenThuoc"),
                    rs.getDate("ngaySanXuat"),
                    rs.getDate("hanSuDung"),
                    rs.getInt("soLuongTon"),
                    rs.getInt("soNgayDaHetHan")
                );
                dsLo.add(lo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLo;
    }
    

 // Lấy các lô thuốc sắp hết hạn
    public ArrayList<LoThuocHetHan> getCacLoThuocSapHetHan(int soNgay) {
        ArrayList<LoThuocHetHan> dsLo = new ArrayList<>();
        String sql = "{CALL sp_GetLoThuocSapHetHan(?)}";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, soNgay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LoThuocHetHan lo = new LoThuocHetHan(
                        rs.getString("maLo"),
                        rs.getString("maThuoc"),
                        rs.getString("tenThuoc"),
                        rs.getDate("ngaySanXuat"),
                        rs.getDate("hanSuDung"),
                        rs.getInt("soLuongTon"),
                        rs.getInt("soNgayConLai") 
                    );
                    dsLo.add(lo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsLo;
    }
    
    /**
     * Xóa cứng chi tiết lô thuốc khỏi database (HARD DELETE)
     * @param maLo Mã lô thuốc cần xóa
     * @param maThuoc Mã thuốc cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean xoaChiTietLoThuoc(String maLo, String maThuoc) {
        String sql = "DELETE FROM ChiTietLoThuoc WHERE maLo = ? AND maThuoc = ?";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maLo);
            stmt.setString(2, maThuoc);
            
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}