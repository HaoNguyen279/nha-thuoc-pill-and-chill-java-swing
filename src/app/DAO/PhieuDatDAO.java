package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.PhieuDat;

/**
 * DAO (Data Access Object) for the PhieuDat entity.
 * Handles all database operations related to order notes.
 * (Đã cập nhật để bao gồm 'ghiChu')
 */
public class PhieuDatDAO {

    /**
     * Phương thức nội bộ để ánh xạ một hàng ResultSet thành đối tượng PhieuDat.
     */
    private PhieuDat mapResultSetToPhieuDat(ResultSet rs) throws SQLException {
        return new PhieuDat(
            rs.getString("maPhieuDat"),
            rs.getString("maNV"),
            rs.getDate("ngayDat"),
            rs.getString("maKH"),
            rs.getString("ghiChu"), 
            rs.getBoolean("isActive"),
            rs.getBoolean("isReceived")
        );
    }

    /**
     * Lấy tất cả các phiếu đặt đang hoạt động, sắp xếp theo ngày mới nhất.
     * @return An ArrayList of PhieuDat objects.
     */
    public ArrayList<PhieuDat> getAllPhieuDat() {
        ArrayList<PhieuDat> dsPhieuDat = new ArrayList<>();
        // Giả định bảng PhieuDat đã có cột ghiChu và isReceived
        String sql = "SELECT * FROM PhieuDat WHERE isActive = 1 ORDER BY ngayDat DESC";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Sử dụng phương thức helper
                dsPhieuDat.add(mapResultSetToPhieuDat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuDat;
    }

    /**
     * Lấy một phiếu đặt theo ID.
     * @param id The ID of the order note to find.
     * @return A PhieuDat object if found, otherwise null.
     */
    public PhieuDat getPhieuDatById(String id) {
        // Giả định bảng PhieuDat đã có cột ghiChu và isReceived
        String sql = "SELECT * FROM PhieuDat WHERE maPhieuDat = ?";
        PhieuDat pd = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Sử dụng phương thức helper
                    pd = mapResultSetToPhieuDat(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pd;
    }

    /**
     * Thêm một phiếu đặt mới vào CSDL.
     * @param phieuDat The PhieuDat object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addPhieuDat(PhieuDat phieuDat) {
        // Cập nhật SQL và tham số
        String sql = "INSERT INTO PhieuDat (maPhieuDat, maNV, ngayDat, maKH, ghiChu, isActive, isReceived) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setString(5, phieuDat.getGhiChu()); 
            stmt.setBoolean(6, phieuDat.isIsActive());
            stmt.setBoolean(7, phieuDat.isReceived());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cập nhật thông tin của một phiếu đặt.
     * @param phieuDat The PhieuDat object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updatePhieuDat(PhieuDat phieuDat) {
        // Cập nhật SQL và tham số
        String sql = "UPDATE PhieuDat SET maNV = ?, ngayDat = ?, maKH = ?, ghiChu = ?, isActive = ?, isReceived = ? WHERE maPhieuDat = ?";
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
            stmt.setString(4, phieuDat.getGhiChu()); 
            stmt.setBoolean(5, phieuDat.isIsActive());
            stmt.setBoolean(6, phieuDat.isReceived());
            stmt.setString(7, phieuDat.getMaPhieuDat());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Tạo mã phiếu đặt tiếp theo (Pattern: PDXXX).
     * @return The next order number (e.g., "PD001", "PD011", etc.)
     */
    public String generateNextMaPhieuDat() {
        String sql = "SELECT TOP 1 maPhieuDat FROM PhieuDat ORDER BY maPhieuDat DESC";
        String nextMaPhieuDat = "PD001"; // Default if no records exist
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastMaPhieuDat = rs.getString("maPhieuDat");
                
                // Trích xuất phần số từ mã cuối cùng
                if (lastMaPhieuDat != null && lastMaPhieuDat.startsWith("PD")) {
                    try {
                        String numericPart = lastMaPhieuDat.substring(2); // Lấy "XXX" từ "PDXXX"
                        int lastNumber = Integer.parseInt(numericPart);
                        int nextNumber = lastNumber + 1;
                        
                        // Định dạng lại thành 3 chữ số
                        nextMaPhieuDat = String.format("PD%03d", nextNumber);
                    } catch (NumberFormatException e) {
                        // Nếu có lỗi, quay về mặc định
                        System.err.println("Lỗi khi phân tích mã phiếu đặt: " + lastMaPhieuDat);
                        nextMaPhieuDat = "PD001";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Quay về mặc định nếu có lỗi DB
            nextMaPhieuDat = "PD001";
        }
        
        return nextMaPhieuDat;
    }

    /**
     * Hủy một phiếu đặt (soft delete).
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

    /**
     * Cập nhật trạng thái đã nhận hàng cho một phiếu đặt
     * @param maPhieuDat mã phiếu đặt cần cập nhật
     * @param isReceived true = đã nhận hàng, false = chưa nhận hàng
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean updateReceivedStatus(String maPhieuDat, boolean isReceived) {
        String sql = "UPDATE PhieuDat SET isReceived = ? WHERE maPhieuDat = ?";
        int n = 0;
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isReceived);
            stmt.setString(2, maPhieuDat);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    
    //lala
    public ArrayList<PhieuDat> getAllPhieuDat5Field() {
        ArrayList<PhieuDat> dsPhieuDat = new ArrayList<>();
        // Giả định bảng PhieuDat đã có cột ghiChu và isReceived
        String sql = "SELECT maPhieuDat, tenNV, ngayDat, tenKH, ghiChu, pd.isActive, pd.isReceived \n"
        		+ "FROM PhieuDat pd JOIN NhanVien nv ON pd.maNV = nv.maNV\n"
        		+ "	JOIN KhachHang kh ON kh.maKH = pd.maKH\n"
        		+ "ORDER BY maPhieuDat DESC ";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Sử dụng phương thức helper
                dsPhieuDat.add(mapResultSetToPhieuDat5Field(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuDat;
    }

    //lala
    private PhieuDat mapResultSetToPhieuDat5Field(ResultSet rs) throws SQLException {
        return new PhieuDat(
            rs.getString("maPhieuDat"),
            rs.getString("tenNV"),
            rs.getDate("ngayDat"),
            rs.getString("tenKH"),
            rs.getString("ghiChu"), 
            rs.getBoolean("isActive"),
            rs.getBoolean("isReceived")
        );
    }
    
  //lala
    public ArrayList<PhieuDat> findPhieuDatByThangNam(int thang, int nam) {
        ArrayList<PhieuDat> dsPhieuDat = new ArrayList<>();
        String sql = "SELECT maPhieuDat, tenNV, ngayDat, tenKH, ghiChu, pd.isActive, pd.isReceived\n"
        		+ "FROM PhieuDat pd JOIN NhanVien nv ON pd.maNV = nv.maNV\n"
        		+ "	JOIN KhachHang kh ON kh.maKH = pd.maKH\n"
        		+ "WHERE pd.isActive = 1 AND MONTH(ngayDat) = ? AND YEAR(ngayDat) = ?\n"
        		+ "ORDER BY ngayDat DESC, maPhieuDat DESC";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	dsPhieuDat.add(mapResultSetToPhieuDat5Field(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuDat;
    }
}