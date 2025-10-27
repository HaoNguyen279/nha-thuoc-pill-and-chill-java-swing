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
import app.Entity.PhieuDoiTra;

/**
 * DAO (Data Access Object) for the PhieuDoiTra entity.
 * Handles all database operations related to return/exchange notes.
 */
public class PhieuDoiTraDAO {

    /**
     * Retrieves a list of all active return/exchange notes, ordered by the most recent.
     * @return An ArrayList of PhieuDoiTra objects.
     */
    public ArrayList<PhieuDoiTra> getAllPhieuDoiTra() {
        ArrayList<PhieuDoiTra> dsPhieuDoiTra = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDoiTra WHERE isActive = 1 ORDER BY ngayDoiTra DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PhieuDoiTra pdt = new PhieuDoiTra(
                    rs.getString("maPhieuDoiTra"),
                    rs.getDate("ngayDoiTra"),
                    rs.getString("maKH"),
                    rs.getString("maNV"),
                    rs.getBoolean("isActive")
                );
                dsPhieuDoiTra.add(pdt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuDoiTra;
    }

    /**
     * Retrieves a single return/exchange note by its ID.
     * @param id The ID of the note to find.
     * @return A PhieuDoiTra object if found, otherwise null.
     */
    public PhieuDoiTra getPhieuDoiTraById(String id) {
        String sql = "SELECT * FROM PhieuDoiTra WHERE maPhieuDoiTra = ?";
        PhieuDoiTra pdt = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pdt = new PhieuDoiTra(
                        rs.getString("maPhieuDoiTra"),
                        rs.getDate("ngayDoiTra"),
                        rs.getString("maKH"),
                        rs.getString("maNV"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pdt;
    }

    /**
     * Adds a new return/exchange note to the database.
     * @param phieuDoiTra The PhieuDoiTra object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addPhieuDoiTra(PhieuDoiTra phieuDoiTra) {
        // Explicitly list columns to ensure correct order
        String sql = "INSERT INTO PhieuDoiTra (maPhieuDoiTra, ngayDoiTra, maNV, maKH, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, phieuDoiTra.getMaPhieuDoiTra());

            if (phieuDoiTra.getNgayDoiTra() != null) {
                stmt.setDate(2, new java.sql.Date(phieuDoiTra.getNgayDoiTra().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            
            stmt.setString(3, phieuDoiTra.getMaNV());
            stmt.setString(4, phieuDoiTra.getMaKH());
            stmt.setBoolean(5, phieuDoiTra.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Updates an existing return/exchange note's information.
     * @param phieuDoiTra The PhieuDoiTra object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updatePhieuDoiTra(PhieuDoiTra phieuDoiTra) {
        String sql = "UPDATE PhieuDoiTra SET ngayDoiTra = ?, maNV = ?, maKH = ?, isActive = ? WHERE maPhieuDoiTra = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            if (phieuDoiTra.getNgayDoiTra() != null) {
                stmt.setDate(1, new java.sql.Date(phieuDoiTra.getNgayDoiTra().getTime()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            stmt.setString(2, phieuDoiTra.getMaNV());
            stmt.setString(3, phieuDoiTra.getMaKH());
            stmt.setBoolean(4, phieuDoiTra.isIsActive());
            stmt.setString(5, phieuDoiTra.getMaPhieuDoiTra());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cancels a return/exchange note by setting its isActive flag to false (soft delete).
     * @param id The ID of the note to cancel.
     * @return true if the cancellation was successful, false otherwise.
     */
    public boolean deletePhieuDoiTra(String id) {
        String sql = "UPDATE PhieuDoiTra SET isActive = 0 WHERE maPhieuDoiTra = ?";
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
     * Tạo mã phiếu đổi trả tự động theo format PDTXXX
     * @return Mã phiếu đổi trả mới (ví dụ: PDT016)
     */
    public String generateMaPhieuDoiTra() {
        String sql = "SELECT TOP 1 maPhieuDoiTra FROM PhieuDoiTra ORDER BY maPhieuDoiTra DESC";
        String newMaPDT = "PDT001"; // Mã mặc định nếu chưa có phiếu nào
        
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastMaPDT = rs.getString("maPhieuDoiTra");
                // Lấy phần số từ mã cuối (ví dụ: PDT015 -> 015)
                String numberPart = lastMaPDT.substring(3);
                int number = Integer.parseInt(numberPart);
                // Tăng lên 1 và format lại
                newMaPDT = String.format("PDT%03d", number + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return newMaPDT;
    }
}