package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietHoaDon;

/**
 * DAO (Data Access Object) for the ChiTietHoaDon entity.
 * Handles all database operations related to invoice details.
 */
public class ChiTietHoaDonDAO {

    /**
     * Retrieves all active detail lines for a specific invoice.
     * @param maHoaDon The ID of the parent invoice.
     * @return An ArrayList of ChiTietHoaDon objects.
     */
    public ArrayList<ChiTietHoaDon> getChiTietByMaHoaDon(String maHoaDon) {
        ArrayList<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsChiTiet.add(mapResultSetToChiTiet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    /**
     * Retrieves a single invoice detail by its composite key.
     * @param maHoaDon The ID of the parent invoice.
     * @param maThuoc The ID of the medicine.
     * @param maLo The ID of the batch/lot.  
     * @return A ChiTietHoaDon object if found, otherwise null.
     */
    public ChiTietHoaDon getChiTietById(String maHoaDon, String maThuoc, String maLo) {
        // Cập nhật SQL và tham số
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND maThuoc = ? AND maLo = ?";
        ChiTietHoaDon cthd = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maThuoc);
            stmt.setString(3, maLo);  
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cthd = mapResultSetToChiTiet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cthd;
    }

    /**
     * Adds a new invoice detail to the database.
     * @param cthd The ChiTietHoaDon object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addChiTietHoaDon(ChiTietHoaDon cthd) {
        // Cập nhật SQL và tham số
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, maLo, soLuong, donGia, isActive) VALUES (?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, cthd.getMaHoaDon());
            stmt.setString(2, cthd.getMaThuoc());
            stmt.setString(3, cthd.getMaLo());  
            stmt.setInt(4, cthd.getSoLuong());
            stmt.setFloat(5, cthd.getDonGia());
            stmt.setBoolean(6, cthd.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing invoice detail.
     * @param cthd The ChiTietHoaDon object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateChiTietHoaDon(ChiTietHoaDon cthd) {
        // Cập nhật SQL (WHERE clause) và tham số
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, donGia = ?, isActive = ? WHERE maHoaDon = ? AND maThuoc = ? AND maLo = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, cthd.getSoLuong());
            stmt.setFloat(2, cthd.getDonGia());
            stmt.setBoolean(3, cthd.isIsActive());
            stmt.setString(4, cthd.getMaHoaDon());
            stmt.setString(5, cthd.getMaThuoc());
            stmt.setString(6, cthd.getMaLo());  
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates an invoice detail by setting its isActive flag to false (soft delete).
     * @param maHoaDon The ID of the parent invoice.
     * @param maThuoc The ID of the medicine.
     * @param maLo The ID of the batch/lot.  
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietHoaDon(String maHoaDon, String maThuoc, String maLo) {
        // Cập nhật SQL (WHERE clause) và tham số
        String sql = "UPDATE ChiTietHoaDon SET isActive = 0 WHERE maHoaDon = ? AND maThuoc = ? AND maLo = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maThuoc);
            stmt.setString(3, maLo);  
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Helper method to map a ResultSet row to a ChiTietHoaDon object.
     */
    private ChiTietHoaDon mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        // Cập nhật để đọc cả maLo
        return new ChiTietHoaDon(
            rs.getString("maHoaDon"),
            rs.getString("maThuoc"),
            rs.getString("maLo"), 
            rs.getInt("soLuong"),
            rs.getFloat("donGia"),
            rs.getBoolean("isActive")
        );
    }
    
    
    //lala
    public ArrayList<ChiTietHoaDon> getChiTietByMaHoaDon5Field(String maHoaDon) {
        ArrayList<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        String sql = "SELECT ct.maThuoc, t.tenThuoc, ct.maLo, ct.soLuong, ct.donGia, ct.isActive \n"
        		+ "FROM ChiTietHoaDon ct JOIN Thuoc t ON ct.maThuoc = t.maThuoc \n"
        		+ " WHERE ct.maHoaDon = ? AND ct.isActive = 1"
        		+ "ORDER BY maThuoc, tenThuoc";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsChiTiet.add(mapResultSetToChiTiet5Field(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }
    
    //lala
    private ChiTietHoaDon mapResultSetToChiTiet5Field(ResultSet rs) throws SQLException {
        // Cập nhật để đọc cả maLo
        return new ChiTietHoaDon(
        	rs.getString("tenThuoc"),
            rs.getString("maThuoc"),
            rs.getString("maLo"), 
            rs.getInt("soLuong"),
            rs.getFloat("donGia"),
            rs.getBoolean("isActive")
        );
    }
}