package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietLoThuoc;

/**
 * DAO (Data Access Object) for the ChiTietLoThuoc entity.
 * Handles all database operations related to medicine batch details.
 */
public class ChiTietLoThuocDAO {

    /**
     * Retrieves all detail lines for a specific medicine batch.
     * @param maLo The ID of the parent medicine batch.
     * @return An ArrayList of ChiTietLoThuoc objects.
     */
    public ArrayList<ChiTietLoThuoc> getChiTietByMaLo(String maLo) {
        ArrayList<ChiTietLoThuoc> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE maLo = ? AND isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, maLo);

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
     * Retrieves a single medicine batch detail by its composite key.
     * @param maLo The ID of the medicine batch.
     * @param maThuoc The ID of the medicine.
     * @return A ChiTietLoThuoc object if found, otherwise null.
     */
    public ChiTietLoThuoc getChiTietById(String maLo, String maThuoc) {
        String sql = "SELECT * FROM ChiTietLoThuoc WHERE maLo = ? AND maThuoc = ?";
        ChiTietLoThuoc ctlt = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLo);
            stmt.setString(2, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ctlt = mapResultSetToChiTiet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctlt;
    }

    /**
     * Adds a new medicine batch detail to the database.
     * @param ctlt The ChiTietLoThuoc object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addChiTietLoThuoc(ChiTietLoThuoc ctlt) {
        String sql = "INSERT INTO ChiTietLoThuoc (maLo, maThuoc, ngaySanXuat, hanSuDung, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setChiTietParameters(stmt, ctlt, false);
            n = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing medicine batch detail.
     * @param ctlt The ChiTietLoThuoc object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateChiTietLoThuoc(ChiTietLoThuoc ctlt) {
        String sql = "UPDATE ChiTietLoThuoc SET ngaySanXuat = ?, hanSuDung = ?, isActive = ? WHERE maLo = ? AND maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setChiTietParameters(stmt, ctlt, true);
            n = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a medicine batch detail by setting its isActive flag to false (soft delete).
     * @param maLo The ID of the medicine batch.
     * @param maThuoc The ID of the medicine.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteChiTietLoThuoc(String maLo, String maThuoc) {
        String sql = "UPDATE ChiTietLoThuoc SET isActive = 0 WHERE maLo = ? AND maThuoc = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maLo);
            stmt.setString(2, maThuoc);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // --- Helper Methods ---

    private ChiTietLoThuoc mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        return new ChiTietLoThuoc(
            rs.getString("maLo"),
            rs.getString("maThuoc"),
            rs.getDate("ngaySanXuat"),
            rs.getDate("hanSuDung"),
            rs.getBoolean("isActive")
        );
    }

    private void setChiTietParameters(PreparedStatement stmt, ChiTietLoThuoc ctlt, boolean isUpdate) throws SQLException {
        if (isUpdate) {
            // For UPDATE statement
            if (ctlt.getNgaySanXuat() != null) {
                stmt.setDate(1, new java.sql.Date(ctlt.getNgaySanXuat().getTime()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            if (ctlt.getHanSuDung() != null) {
                stmt.setDate(2, new java.sql.Date(ctlt.getHanSuDung().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setBoolean(3, ctlt.isIsActive());
            stmt.setString(4, ctlt.getMaLo());
            stmt.setString(5, ctlt.getMaThuoc());
        } else {
            // For INSERT statement
            stmt.setString(1, ctlt.getMaLo());
            stmt.setString(2, ctlt.getMaThuoc());

            if (ctlt.getNgaySanXuat() != null) {
                stmt.setDate(3, new java.sql.Date(ctlt.getNgaySanXuat().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (ctlt.getHanSuDung() != null) {
                stmt.setDate(4, new java.sql.Date(ctlt.getHanSuDung().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setBoolean(5, ctlt.isIsActive());
        }
    }
}
