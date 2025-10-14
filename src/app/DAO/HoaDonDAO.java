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
import app.Entity.HoaDon;

/**
 * DAO (Data Access Object) for the HoaDon entity.
 * Handles all database operations related to invoices.
 */
public class HoaDonDAO {

    /**
     * Retrieves a list of all active invoices, ordered by the most recent date.
     * @return An ArrayList of HoaDon objects.
     */
    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE isActive = 1 ORDER BY ngayBan DESC, maHoaDon DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    /**
     * Retrieves a single invoice by its ID.
     * @param id The ID of the invoice to find.
     * @return A HoaDon object if found, otherwise null.
     */
    public HoaDon getHoaDonById(String id) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        HoaDon hd = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hd = mapResultSetToHoaDon(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hd;
    }
    
    /**
     * Finds active invoices within a specific date range.
     * @param fromDate The start date of the range.
     * @param toDate The end date of the range.
     * @return An ArrayList of matching HoaDon objects.
     */
    public ArrayList<HoaDon> findHoaDonByDateRange(Date fromDate, Date toDate) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE isActive = 1 AND ngayBan BETWEEN ? AND ? ORDER BY ngayBan";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(fromDate.getTime()));
            stmt.setDate(2, new java.sql.Date(toDate.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsHoaDon.add(mapResultSetToHoaDon(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }


    /**
     * Adds a new invoice to the database.
     * @param hd The HoaDon object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (maHoaDon, ngayBan, ghiChu, maNV, maKH, maKM, maThue, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setHoaDonParameters(stmt, hd, false);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing invoice's information.
     * @param hd The HoaDon object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateHoaDon(HoaDon hd) {
        String sql = "UPDATE HoaDon SET ngayBan = ?, ghiChu = ?, maNV = ?, maKH = ?, maKM = ?, maThue = ?, isActive = ? WHERE maHoaDon = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            setHoaDonParameters(stmt, hd, true);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Voids an invoice by setting its isActive flag to false (soft delete).
     * @param id The ID of the invoice to void.
     * @return true if voiding was successful, false otherwise.
     */
    public boolean deleteHoaDon(String id) {
        String sql = "UPDATE HoaDon SET isActive = 0 WHERE maHoaDon = ?";
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

    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        return new HoaDon(
            rs.getString("maHoaDon"),
            rs.getDate("ngayBan"),
            rs.getString("ghiChu"),
            rs.getString("maNV"),
            rs.getString("maKH"),
            rs.getString("maKM"),
            rs.getString("maThue"),
            rs.getBoolean("isActive")
        );
    }

    private void setHoaDonParameters(PreparedStatement stmt, HoaDon hd, boolean isUpdate) throws SQLException {
        int paramIndex = 1;
        if (isUpdate) {
            // Parameters for UPDATE
            if (hd.getNgayBan() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(hd.getNgayBan().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            stmt.setString(paramIndex++, hd.getGhiChu());
            stmt.setString(paramIndex++, hd.getMaNV());
            stmt.setString(paramIndex++, hd.getMaKH());
            stmt.setString(paramIndex++, hd.getMaKM());
            stmt.setString(paramIndex++, hd.getMaThue());
            stmt.setBoolean(paramIndex++, hd.isIsActive());
            stmt.setString(paramIndex++, hd.getMaHoaDon()); // WHERE clause
        } else {
            // Parameters for INSERT
            stmt.setString(paramIndex++, hd.getMaHoaDon());
            if (hd.getNgayBan() != null) {
                stmt.setDate(paramIndex++, new java.sql.Date(hd.getNgayBan().getTime()));
            } else {
                stmt.setNull(paramIndex++, Types.DATE);
            }
            stmt.setString(paramIndex++, hd.getGhiChu());
            stmt.setString(paramIndex++, hd.getMaNV());
            stmt.setString(paramIndex++, hd.getMaKH());
            stmt.setString(paramIndex++, hd.getMaKM());
            stmt.setString(paramIndex++, hd.getMaThue());
            stmt.setBoolean(paramIndex++, hd.isIsActive());
        }
    }
}
