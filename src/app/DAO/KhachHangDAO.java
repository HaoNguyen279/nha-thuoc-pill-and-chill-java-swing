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
        Connection con = ConnectDB.getConnection();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                dsKhachHang.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                kh = mapResultSetToKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            rs = stmt.executeQuery();

            while (rs.next()) {
                dsKhachHang.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsKhachHang;
    }
    public KhachHang findKhachHangByPhone(String soDienThoai) {
        KhachHang kh = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;

        try {
            // Create a fresh connection
            ConnectDB.connect();
            con = ConnectDB.getConnection();
            
            // First, try direct exact match
            String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, soDienThoai);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                kh = mapResultSetToKhachHang(rs);
                return kh;
            }
            
            // If not found with exact match, try with trimmed string
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            
            stmt = con.prepareStatement("SELECT * FROM KhachHang WHERE soDienThoai = ?");
            stmt.setString(1, soDienThoai.trim());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getInt("diemTichLuy"),
                    rs.getBoolean("isActive")
                );
                return kh;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return kh;
    }
    /**
     * Adds a new customer to the database.
     * @param kh The KhachHang object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai, diemTichLuy, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        
        // Make sure we have a connection
        con = ConnectDB.getConnection();

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getTenKH());
            stmt.setString(3, kh.getSoDienThoai());
            stmt.setInt(4, kh.getDiemTichLuy());
            stmt.setBoolean(5, kh.isIsActive());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, kh.getTenKH());
            stmt.setString(2, kh.getSoDienThoai());
            stmt.setInt(3, kh.getDiemTichLuy());
            stmt.setBoolean(4, kh.isIsActive());
            stmt.setString(5, kh.getMaKH());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, id);

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    /**
     * Maps a ResultSet row to a KhachHang object.
     * @param rs The ResultSet containing customer data.
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
    
    /**
     * Generates a new customer code in format KHXXX (e.g., KH001, KH002, ..., KH999).
     * Retrieves the last customer code from database and increments by 1.
     * @return A new customer code string.
     */
    public String generateMaKhachHang() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        String newMaKH = "KH001"; // Default if no customers exist
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Connection con = ConnectDB.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String lastMaKH = rs.getString("maKH");
                // Extract number from KHXXX format
                int num = Integer.parseInt(lastMaKH.substring(2));
                num++;
                // Format back to KHXXX
                newMaKH = String.format("KH%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return newMaKH;
    }
    

}
