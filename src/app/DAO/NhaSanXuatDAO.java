package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.NhaSanXuat;

/**
 * DAO (Data Access Object) for the NhaSanXuat entity.
 * Handles all database operations related to manufacturers.
 */
public class NhaSanXuatDAO {

    /**
     * Retrieves a list of all active manufacturers, ordered by name.
     * @return An ArrayList of NhaSanXuat objects.
     */
    public ArrayList<NhaSanXuat> getAllNhaSanXuat() {
        ArrayList<NhaSanXuat> dsNhaSanXuat = new ArrayList<>();
        String sql = "SELECT * FROM NhaSanXuat WHERE isActive = 1 ORDER BY tenNSX";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhaSanXuat nsx = new NhaSanXuat(
                    rs.getString("maNSX"),
                    rs.getString("tenNSX"),
                    rs.getString("diaChiNSX"), // Note: DB column name is diaChiNSX
                    rs.getString("soDienThoai"),
                    rs.getBoolean("isActive")
                );
                dsNhaSanXuat.add(nsx);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNhaSanXuat;
    }

    /**
     * Retrieves a single manufacturer by its ID.
     * @param id The ID of the manufacturer to find.
     * @return A NhaSanXuat object if found, otherwise null.
     */
    public NhaSanXuat getNhaSanXuatById(String id) {
        String sql = "SELECT * FROM NhaSanXuat WHERE maNSX = ?";
        NhaSanXuat nsx = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nsx = new NhaSanXuat(
                        rs.getString("maNSX"),
                        rs.getString("tenNSX"),
                        rs.getString("diaChiNSX"), // Note: DB column name is diaChiNSX
                        rs.getString("soDienThoai"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nsx;
    }
    
    /**
     * Retrieves a single manufacturer name by its ID.
     * @param id The ID of the manufacturer to find.
     * @return A String if found, otherwise null.
     */
    public String getTenNSXById(String id) {
        String sql = "SELECT * FROM NhaSanXuat WHERE maNSX = ?";
        String tenNSX = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                      tenNSX =  rs.getString("tenNSX");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenNSX;
    }

    /**
     * Adds a new manufacturer to the database.
     * @param nsx The NhaSanXuat object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addNhaSanXuat(NhaSanXuat nsx) {
        String sql = "INSERT INTO NhaSanXuat (maNSX, tenNSX, diaChiNSX, soDienThoai, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, nsx.getMaNSX());
            stmt.setString(2, nsx.getTenNSX());
            stmt.setString(3, nsx.getDiaChi()); // Map getDiaChi() to diaChiNSX column
            stmt.setString(4, nsx.getSoDienThoai());
            stmt.setBoolean(5, nsx.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing manufacturer's information.
     * @param nsx The NhaSanXuat object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateNhaSanXuat(NhaSanXuat nsx) {
        String sql = "UPDATE NhaSanXuat SET tenNSX = ?, diaChiNSX = ?, soDienThoai = ?, isActive = ? WHERE maNSX = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, nsx.getTenNSX());
            stmt.setString(2, nsx.getDiaChi()); // Map getDiaChi() to diaChiNSX column
            stmt.setString(3, nsx.getSoDienThoai());
            stmt.setBoolean(4, nsx.isIsActive());
            stmt.setString(5, nsx.getMaNSX());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a manufacturer by setting its isActive flag to false (soft delete).
     * @param id The ID of the manufacturer to deactivate.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteNhaSanXuat(String id) {
        String sql = "UPDATE NhaSanXuat SET isActive = 0 WHERE maNSX = ?";
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