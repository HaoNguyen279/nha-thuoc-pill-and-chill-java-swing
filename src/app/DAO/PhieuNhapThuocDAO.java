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
import app.Entity.PhieuNhapThuoc;

/**
 * DAO (Data Access Object) for the PhieuNhapThuoc entity.
 * Handles all database operations related to drug import notes.
 */
public class PhieuNhapThuocDAO {

    /**
     * Retrieves a list of all active drug import notes, ordered by the most recent.
     * @return An ArrayList of PhieuNhapThuoc objects.
     */
    public ArrayList<PhieuNhapThuoc> getAllPhieuNhapThuoc() {
        ArrayList<PhieuNhapThuoc> dsPhieuNhap = new ArrayList<>();
        // Ordering by date descending is useful for viewing recent transactions first
        String sql = "SELECT * FROM PhieuNhapThuoc WHERE isActive = 1 ORDER BY ngayNhap DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuNhapThuoc pnt = new PhieuNhapThuoc(
                    rs.getString("maPhieuNhapThuoc"),
                    rs.getString("maNV"),
                    rs.getDate("ngayNhap"),
                    rs.getBoolean("isActive")
                );
                dsPhieuNhap.add(pnt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuNhap;
    }
    
    public String taoMaTuDong() {
        String prefix = "PNT";
        String sql = "SELECT MAX(maPhieuNhapThuoc) AS maxMa FROM PhieuNhapThuoc";
        try (Connection con = ConnectDB.getInstance().getConnection();
        	Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa != null) {
                    // Tách số phía sau tiền tố PN
                    int num = Integer.parseInt(maxMa.substring(prefix.length()));
                    num++;
                    return String.format("%s%03d", prefix, num); // ví dụ PN0005
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Nếu bảng trống
        return prefix + "001";
    }


    /**
     * Retrieves a single drug import note by its ID.
     * @param id The ID of the import note to find.
     * @return A PhieuNhapThuoc object if found, otherwise null.
     */
    public PhieuNhapThuoc getPhieuNhapThuocById(String id) {
        String sql = "SELECT * FROM PhieuNhapThuoc WHERE maPhieuNhapThuoc = ?";
        PhieuNhapThuoc pnt = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pnt = new PhieuNhapThuoc(
                        rs.getString("maPhieuNhapThuoc"),
                        rs.getString("maNV"),
                        rs.getDate("ngayNhap"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pnt;
    }

    /**
     * Adds a new drug import note to the database.
     * @param phieuNhap The PhieuNhapThuoc object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addPhieuNhapThuoc(PhieuNhapThuoc phieuNhap) {
        String sql = "INSERT INTO PhieuNhapThuoc (maPhieuNhapThuoc, maNV, ngayNhap, isActive) VALUES (?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuNhap.getMaPhieuNhapThuoc());
            stmt.setString(2, phieuNhap.getMaNV());

            // Convert java.util.Date to java.sql.Date, handling nulls
            if (phieuNhap.getNgayNhap() != null) {
                stmt.setDate(3, new java.sql.Date(phieuNhap.getNgayNhap().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setBoolean(4, phieuNhap.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing drug import note's information.
     * @param phieuNhap The PhieuNhapThuoc object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updatePhieuNhapThuoc(PhieuNhapThuoc phieuNhap) {
        String sql = "UPDATE PhieuNhapThuoc SET maNV = ?, ngayNhap = ?, isActive = ? WHERE maPhieuNhapThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuNhap.getMaNV());

            if (phieuNhap.getNgayNhap() != null) {
                stmt.setDate(2, new java.sql.Date(phieuNhap.getNgayNhap().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setBoolean(3, phieuNhap.isIsActive());
            stmt.setString(4, phieuNhap.getMaPhieuNhapThuoc());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cancels an import note by setting its isActive flag to false (soft delete).
     * @param id The ID of the import note to cancel.
     * @return true if the cancellation was successful, false otherwise.
     */
    public boolean deletePhieuNhapThuoc(String id) {
        String sql = "UPDATE PhieuNhapThuoc SET isActive = 0 WHERE maPhieuNhapThuoc = ?";
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
}