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
import app.Entity.PhieuDat;

/**
 * DAO (Data Access Object) for the PhieuDat entity.
 * Handles all database operations related to order notes.
 */
public class PhieuDatDAO {

    /**
     * Retrieves a list of all active order notes, ordered by the most recent.
     * @return An ArrayList of PhieuDat objects.
     */
    public ArrayList<PhieuDat> getAllPhieuDat() {
        ArrayList<PhieuDat> dsPhieuDat = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDat WHERE isActive = 1 ORDER BY ngayDat DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuDat pd = new PhieuDat(
                    rs.getString("maPhieuDat"),
                    rs.getString("maNV"),
                    rs.getDate("ngayDat"),
                    rs.getString("maKH"),
                    rs.getBoolean("isActive")
                );
                dsPhieuDat.add(pd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuDat;
    }

    /**
     * Retrieves a single order note by its ID.
     * @param id The ID of the order note to find.
     * @return A PhieuDat object if found, otherwise null.
     */
    public PhieuDat getPhieuDatById(String id) {
        String sql = "SELECT * FROM PhieuDat WHERE maPhieuDat = ?";
        PhieuDat pd = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pd = new PhieuDat(
                        rs.getString("maPhieuDat"),
                        rs.getString("maNV"),
                        rs.getDate("ngayDat"),
                        rs.getString("maKH"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pd;
    }

    /**
     * Adds a new order note to the database.
     * @param phieuDat The PhieuDat object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addPhieuDat(PhieuDat phieuDat) {
        String sql = "INSERT INTO PhieuDat (maPhieuDat, maNV, ngayDat, maKH, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuDat.getMaPhieuDat());
            stmt.setString(2, phieuDat.getMaNV());

            if (phieuDat.getNgayDat() != null) {
                stmt.setDate(3, new java.sql.Date(phieuDat.getNgayDat().getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            
            stmt.setString(4, phieuDat.getMaKH());
            stmt.setBoolean(5, phieuDat.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing order note's information.
     * @param phieuDat The PhieuDat object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updatePhieuDat(PhieuDat phieuDat) {
        String sql = "UPDATE PhieuDat SET maNV = ?, ngayDat = ?, maKH = ?, isActive = ? WHERE maPhieuDat = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuDat.getMaNV());

            if (phieuDat.getNgayDat() != null) {
                stmt.setDate(2, new java.sql.Date(phieuDat.getNgayDat().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setString(3, phieuDat.getMaKH());
            stmt.setBoolean(4, phieuDat.isIsActive());
            stmt.setString(5, phieuDat.getMaPhieuDat());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cancels an order note by setting its isActive flag to false (soft delete).
     * @param id The ID of the order note to cancel.
     * @return true if the cancellation was successful, false otherwise.
     */
    public boolean deletePhieuDat(String id) {
        String sql = "UPDATE PhieuDat SET isActive = 0 WHERE maPhieuDat = ?";
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