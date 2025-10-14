package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietPhieuNhap;

/**
 * DAO (Data Access Object) for the ChiTietPhieuNhap entity.
 * Handles all database operations related to import note details.
 */
public class ChiTietPhieuNhapDAO {

    /**
     * Retrieves all detail lines for a specific import note.
     * @param maPhieuNhap The ID of the parent import note.
     * @return An ArrayList of ChiTietPhieuNhap objects.
     */
    public ArrayList<ChiTietPhieuNhap> getChiTietByMaPhieuNhap(String maPhieuNhap) {
        ArrayList<ChiTietPhieuNhap> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhapThuoc = ? AND isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maPhieuNhap);

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
     * Retrieves a single import note detail by its composite key.
     * @param maPhieuNhap The ID of the parent import note.
     * @param maLo The ID of the medicine batch.
     * @return A ChiTietPhieuNhap object if found, otherwise null.
     */
    public ChiTietPhieuNhap getChiTietById(String maPhieuNhap, String maLo) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhapThuoc = ? AND maLo = ?";
        ChiTietPhieuNhap ctpn = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuNhap);
            stmt.setString(2, maLo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ctpn = mapResultSetToChiTiet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctpn;
    }

    /**
     * Adds a new import note detail to the database.
     * @param ctpn The ChiTietPhieuNhap object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addChiTietPhieuNhap(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap (maPhieuNhapThuoc, maLo, soLuong, donGia, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, ctpn.getMaPhieuNhapThuoc());
            stmt.setString(2, ctpn.getMaLo());
            stmt.setInt(3, ctpn.getSoLuong());
            stmt.setFloat(4, ctpn.getDonGia());
            stmt.setBoolean(5, ctpn.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing import note detail.
     * @param ctpn The ChiTietPhieuNhap object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateChiTietPhieuNhap(ChiTietPhieuNhap ctpn) {
        String sql = "UPDATE ChiTietPhieuNhap SET soLuong = ?, donGia = ?, isActive = ? WHERE maPhieuNhapThuoc = ? AND maLo = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, ctpn.getSoLuong());
            stmt.setFloat(2, ctpn.getDonGia());
            stmt.setBoolean(3, ctpn.isIsActive());
            stmt.setString(4, ctpn.getMaPhieuNhapThuoc());
            stmt.setString(5, ctpn.getMaLo());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates an import note detail by setting its isActive flag to false (soft delete).
     * @param maPhieuNhap The ID of the parent import note.
     * @param maLo The ID of the medicine batch.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietPhieuNhap(String maPhieuNhap, String maLo) {
        String sql = "UPDATE ChiTietPhieuNhap SET isActive = 0 WHERE maPhieuNhapThuoc = ? AND maLo = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuNhap);
            stmt.setString(2, maLo);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Helper method to map a ResultSet row to a ChiTietPhieuNhap object.
     */
    private ChiTietPhieuNhap mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        return new ChiTietPhieuNhap(
            rs.getString("maPhieuNhapThuoc"),
            rs.getString("maLo"),
            rs.getInt("soLuong"),
            rs.getFloat("donGia"),
            rs.getBoolean("isActive")
        );
    }
}
