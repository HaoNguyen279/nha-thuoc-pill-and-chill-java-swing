package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietPhieuDat;

/**
 * DAO (Data Access Object) for the ChiTietPhieuDat entity.
 * Handles all database operations related to order note details.
 */
public class ChiTietPhieuDatDAO {

    /**
     * Retrieves all detail lines for a specific order note.
     * @param maPhieuDat The ID of the parent order note.
     * @return An ArrayList of ChiTietPhieuDat objects.
     */
    public ArrayList<ChiTietPhieuDat> getChiTietByMaPhieuDat(String maPhieuDat) {
        ArrayList<ChiTietPhieuDat> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDat);

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
     * Retrieves a single order note detail by its composite key.
     * @param maPhieuDat The ID of the parent order note.
     * @param maThuoc The ID of the medicine.
     * @return A ChiTietPhieuDat object if found, otherwise null.
     */
    public ChiTietPhieuDat getChiTietById(String maPhieuDat, String maThuoc) {
        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND maThuoc = ?";
        ChiTietPhieuDat ctpd = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ctpd = mapResultSetToChiTiet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctpd;
    }

    /**
     * Adds a new order note detail to the database.
     * @param ctpd The ChiTietPhieuDat object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addChiTietPhieuDat(ChiTietPhieuDat ctpd) {
        String sql = "INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, tenThuoc, soLuong, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, ctpd.getMaPhieuDat());
            stmt.setString(2, ctpd.getMaThuoc());
            stmt.setString(3, ctpd.getTenThuoc());
            stmt.setInt(4, ctpd.getSoLuong());
            stmt.setBoolean(5, ctpd.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing order note detail.
     * @param ctpd The ChiTietPhieuDat object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateChiTietPhieuDat(ChiTietPhieuDat ctpd) {
        String sql = "UPDATE ChiTietPhieuDat SET tenThuoc = ?, soLuong = ?, isActive = ? WHERE maPhieuDat = ? AND maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, ctpd.getTenThuoc());
            stmt.setInt(2, ctpd.getSoLuong());
            stmt.setBoolean(3, ctpd.isIsActive());
            stmt.setString(4, ctpd.getMaPhieuDat());
            stmt.setString(5, ctpd.getMaThuoc());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates an order note detail by setting its isActive flag to false (soft delete).
     * @param maPhieuDat The ID of the parent order note.
     * @param maThuoc The ID of the medicine.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietPhieuDat(String maPhieuDat, String maThuoc) {
        String sql = "UPDATE ChiTietPhieuDat SET isActive = 0 WHERE maPhieuDat = ? AND maThuoc = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maThuoc);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Helper method to map a ResultSet row to a ChiTietPhieuDat object.
     */
    private ChiTietPhieuDat mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        return new ChiTietPhieuDat(
            rs.getString("maPhieuDat"),
            rs.getString("maThuoc"),
            rs.getString("tenThuoc"),
            rs.getInt("soLuong"),
            rs.getBoolean("isActive")
        );
    }
}
