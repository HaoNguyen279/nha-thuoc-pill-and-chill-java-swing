package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

import app.ConnectDB.ConnectDB;
import app.Entity.KhuyenMai;

/**
 * DAO (Data Access Object) for the KhuyenMai entity.
 * Handles all database operations related to promotions.
 */
public class KhuyenMaiDAO {

    /**
     * Retrieves a list of all currently valid promotions (active and within the date range).
     * @return An ArrayList of currently effective KhuyenMai objects.
     */
    public ArrayList<KhuyenMai> getKhuyenMaiHieuLuc() {
        ArrayList<KhuyenMai> dsKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE isActive = 1 AND ? >= ngayApDung AND ? <= ngayKetThuc";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // Get current date as java.sql.Date for DB comparison
            java.sql.Date homNay = new java.sql.Date(new Date().getTime());
            stmt.setDate(1, homNay);
            stmt.setDate(2, homNay);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsKhuyenMai.add(mapResultSetToKhuyenMai(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhuyenMai;
    }

    /**
     * Retrieves a list of all active promotions, ordered by end date.
     * @return An ArrayList of all active KhuyenMai objects.
     */
    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        ArrayList<KhuyenMai> dsKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE isActive = 1 ORDER BY ngayKetThuc DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsKhuyenMai.add(mapResultSetToKhuyenMai(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhuyenMai;
    }

    /**
     * Retrieves a single promotion by its ID.
     * @param id The ID of the promotion to find.
     * @return A KhuyenMai object if found, otherwise null.
     */
    public KhuyenMai getKhuyenMaiById(String id) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
        KhuyenMai km = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    km = mapResultSetToKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return km;
    }

    /**
     * Adds a new promotion to the database.
     * @param km The KhuyenMai object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, mucGiamGia, ngayApDung, ngayKetThuc, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setKhuyenMaiParameters(stmt, km, false);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing promotion's information.
     * @param km The KhuyenMai object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET mucGiamGia = ?, ngayApDung = ?, ngayKetThuc = ?, isActive = ? WHERE maKM = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setKhuyenMaiParameters(stmt, km, true);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a promotion by setting its isActive flag to false (soft delete).
     * @param id The ID of the promotion to deactivate.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteKhuyenMai(String id) {
        String sql = "UPDATE KhuyenMai SET isActive = 0 WHERE maKM = ?";
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
    
    // --- Helper Methods ---

    /**
     * Maps a ResultSet row to a KhuyenMai object.
     * @param rs The ResultSet to map.
     * @return A KhuyenMai object.
     * @throws SQLException If a database access error occurs.
     */
    private KhuyenMai mapResultSetToKhuyenMai(ResultSet rs) throws SQLException {
        return new KhuyenMai(
            rs.getString("maKM"),
            rs.getFloat("mucGiamGia"),
            rs.getDate("ngayApDung"),
            rs.getDate("ngayKetThuc"),
            rs.getBoolean("isActive")
        );
    }

    /**
     * Sets parameters for a PreparedStatement for an add or update operation.
     * @param stmt The PreparedStatement to configure.
     * @param km The KhuyenMai object containing the data.
     * @param isUpdate A boolean flag, true if it's an update, false for an insert.
     * @throws SQLException If a database access error occurs.
     */
    private void setKhuyenMaiParameters(PreparedStatement stmt, KhuyenMai km, boolean isUpdate) throws SQLException {
        int paramIndex = 1;
        if (isUpdate) {
            stmt.setFloat(paramIndex++, km.getMucGiamGia());

            if (km.getNgayApDung() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(km.getNgayApDung().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }

            if (km.getNgayKetThuc() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(km.getNgayKetThuc().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            
            stmt.setBoolean(paramIndex++, km.isIsActive());
            stmt.setString(paramIndex++, km.getMaKM());
        } else {
            stmt.setString(paramIndex++, km.getMaKM());
            stmt.setFloat(paramIndex++, km.getMucGiamGia());

            if (km.getNgayApDung() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(km.getNgayApDung().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }

            if (km.getNgayKetThuc() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(km.getNgayKetThuc().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            
            stmt.setBoolean(paramIndex++, km.isIsActive());
        }
    }
}