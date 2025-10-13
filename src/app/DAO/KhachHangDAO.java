package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.KhachHang;

/**
 * DAO (Data Access Object) for the KhachHang entity.
 * Handles all database operations related to customers.
 */
public class KhachHangDAO {

    /**
     * Retrieves a list of all active customers, ordered by name.
     * @return An ArrayList of KhachHang objects.
     */
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE isActive = 1 ORDER BY tenKH";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsKhachHang.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }

    /**
     * Retrieves a single customer by their ID.
     * @param id The ID of the customer to find.
     * @return A KhachHang object if found, otherwise null.
     */
    public KhachHang getKhachHangById(String id) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        KhachHang kh = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kh = mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kh;
    }

    /**
     * Finds active customers by a keyword matching their name or phone number.
     * @param keyword The search term.
     * @return An ArrayList of matching KhachHang objects.
     */
    public ArrayList<KhachHang> findKhachHang(String keyword) {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE isActive = 1 AND (tenKH LIKE ? OR soDienThoai LIKE ?)";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsKhachHang.add(mapResultSetToKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }

    /**
     * Adds a new customer to the database.
     * @param kh The KhachHang object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai, diemTichLuy, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getTenKH());
            stmt.setString(3, kh.getSoDienThoai());
            stmt.setInt(4, kh.getDiemTichLuy());
            stmt.setBoolean(5, kh.isIsActive());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing customer's information.
     * @param kh The KhachHang object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, soDienThoai = ?, diemTichLuy = ?, isActive = ? WHERE maKH = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, kh.getTenKH());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setInt(3, kh.getDiemTichLuy());
            stmt.setBoolean(4, kh.isIsActive());
            stmt.setString(5, kh.getMaKH());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deactivates a customer by setting their isActive flag to false (soft delete).
     * @param id The ID of the customer to deactivate.
     * @return true if the deactivation was successful, false otherwise.
     */
    public boolean deleteKhachHang(String id) {
        String sql = "UPDATE KhachHang SET isActive = 0 WHERE maKH = ?";
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

    /**
     * Helper method to map a ResultSet row to a KhachHang object.
     * @param rs The ResultSet to map.
     * @return A KhachHang object.
     * @throws SQLException
     */
    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
            rs.getString("maKH"),
            rs.getString("tenKH"),
            rs.getString("soDienThoai"),
            rs.getInt("diemTichLuy"),
            rs.getBoolean("isActive")
        );
    }
}
