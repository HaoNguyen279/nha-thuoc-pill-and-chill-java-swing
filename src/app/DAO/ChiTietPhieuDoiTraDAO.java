package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietPhieuDoiTra;

/**
 * DAO (Data Access Object) for the ChiTietPhieuDoiTra entity.
 * Handles all database operations related to return/exchange note details.
 */
public class ChiTietPhieuDoiTraDAO {

    /**
     * Retrieves all detail lines for a specific return/exchange note.
     * @param maPhieuDoiTra The ID of the parent note.
     * @return An ArrayList of ChiTietPhieuDoiTra objects.
     */
    public ArrayList<ChiTietPhieuDoiTra> getChiTietByMaPhieuDoiTra(String maPhieuDoiTra) {
        ArrayList<ChiTietPhieuDoiTra> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuDoiTra WHERE maPhieuDoiTra = ? AND isActive = 1";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuDoiTra);

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
     * Retrieves a single return/exchange detail by its composite key.
     * @param maPhieuDoiTra The ID of the parent note.
     * @param maThuoc The ID of the medicine.
     * @return A ChiTietPhieuDoiTra object if found, otherwise null.
     */
    public ChiTietPhieuDoiTra getChiTietById(String maPhieuDoiTra, String maThuoc) {
        String sql = "SELECT * FROM ChiTietPhieuDoiTra WHERE maPhieuDoiTra = ? AND maThuoc = ?";
        ChiTietPhieuDoiTra ctpt = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDoiTra);
            stmt.setString(2, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ctpt = mapResultSetToChiTiet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctpt;
    }

    /**
     * Adds a new return/exchange detail to the database.
     * @param ctpt The ChiTietPhieuDoiTra object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addChiTietPhieuDoiTra(ChiTietPhieuDoiTra ctpt) {
        String sql = "INSERT INTO ChiTietPhieuDoiTra (maPhieuDoiTra, maThuoc, soLuong, donGia, maLo, lyDo, isActive) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, ctpt.getMaPhieuDoiTra());
            stmt.setString(2, ctpt.getMaThuoc());
            stmt.setInt(3, ctpt.getSoLuong());
            stmt.setFloat(4, ctpt.getDonGia());
            stmt.setString(5, ctpt.getMaLo());
            stmt.setString(6, ctpt.getLyDo());
            stmt.setBoolean(7, ctpt.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing return/exchange detail.
     * @param ctpt The ChiTietPhieuDoiTra object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateChiTietPhieuDoiTra(ChiTietPhieuDoiTra ctpt) {
        String sql = "UPDATE ChiTietPhieuDoiTra SET soLuong = ?, donGia = ?, maLo = ?, lyDo = ?, isActive = ? WHERE maPhieuDoiTra = ? AND maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, ctpt.getSoLuong());
            stmt.setFloat(2, ctpt.getDonGia());
            stmt.setString(3, ctpt.getMaLo());
            stmt.setString(4, ctpt.getLyDo());
            stmt.setBoolean(5, ctpt.isIsActive());
            stmt.setString(6, ctpt.getMaPhieuDoiTra());
            stmt.setString(7, ctpt.getMaThuoc());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a return/exchange detail by setting its isActive flag to false (soft delete).
     * @param maPhieuDoiTra The ID of the parent note.
     * @param maThuoc The ID of the medicine.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietPhieuDoiTra(String maPhieuDoiTra, String maThuoc) {
        String sql = "UPDATE ChiTietPhieuDoiTra SET isActive = 0 WHERE maPhieuDoiTra = ? AND maThuoc = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDoiTra);
            stmt.setString(2, maThuoc);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Helper method to map a ResultSet row to a ChiTietPhieuDoiTra object.
     */
    private ChiTietPhieuDoiTra mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        return new ChiTietPhieuDoiTra(
            rs.getString("maPhieuDoiTra"),
            rs.getString("maThuoc"),
            rs.getInt("soLuong"),
            rs.getFloat("donGia"),
            rs.getString("maLo"),
            rs.getString("lyDo"),
            rs.getBoolean("isActive")
        );
    }
}