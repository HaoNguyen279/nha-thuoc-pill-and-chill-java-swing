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
     * Retrieves all detail lines for a specific invoice.
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
     * @return A ChiTietHoaDon object if found, otherwise null.
     */
    public ChiTietHoaDon getChiTietById(String maHoaDon, String maThuoc) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ? AND maThuoc = ?";
        ChiTietHoaDon cthd = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maThuoc);
            
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
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, soLuong, donGia, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, cthd.getMaHoaDon());
            stmt.setString(2, cthd.getMaThuoc());
            stmt.setInt(3, cthd.getSoLuong());
            stmt.setFloat(4, cthd.getDonGia());
            stmt.setBoolean(5, cthd.isIsActive());
            
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
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, donGia = ?, isActive = ? WHERE maHoaDon = ? AND maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, cthd.getSoLuong());
            stmt.setFloat(2, cthd.getDonGia());
            stmt.setBoolean(3, cthd.isIsActive());
            stmt.setString(4, cthd.getMaHoaDon());
            stmt.setString(5, cthd.getMaThuoc());
            
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
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietHoaDon(String maHoaDon, String maThuoc) {
        String sql = "UPDATE ChiTietHoaDon SET isActive = 0 WHERE maHoaDon = ? AND maThuoc = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maThuoc);
            
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
        return new ChiTietHoaDon(
            rs.getString("maHoaDon"),
            rs.getString("maThuoc"),
            rs.getInt("soLuong"),
            rs.getFloat("donGia"),
            rs.getBoolean("isActive")
        );
    }
}
