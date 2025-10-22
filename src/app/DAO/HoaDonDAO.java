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
        // Cập nhật SQL: Xóa maThue, thêm giaTriThue và tenLoaiThue
        String sql = "INSERT INTO HoaDon (maHoaDon, ngayBan, ghiChu, maNV, maKH, maKM, giaTriThue, tenLoaiThue, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        // Cập nhật SQL: Xóa maThue, thêm giaTriThue và tenLoaiThue
        String sql = "UPDATE HoaDon SET ngayBan = ?, ghiChu = ?, maNV = ?, maKH = ?, maKM = ?, giaTriThue = ?, tenLoaiThue = ?, isActive = ? WHERE maHoaDon = ?";
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

    /**
     * Map ResultSet to HoaDon entity. Đã cập nhật để đọc giaTriThue và tenLoaiThue.
     */
    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {
        return new HoaDon(
            rs.getString("maHoaDon"),
            rs.getDate("ngayBan"),
            rs.getString("ghiChu"),
            rs.getString("maNV"),
            rs.getString("maKH"),
            rs.getString("maKM"),
            rs.getDouble("giaTriThue"), // Đã cập nhật
            rs.getString("tenLoaiThue"), // Đã cập nhật
            rs.getBoolean("isActive")
        );
    }

    /**
     * Sets parameters for PreparedStatement (used for INSERT and UPDATE). Đã cập nhật để set giaTriThue và tenLoaiThue.
     */
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
            
            // Tham số mới
            stmt.setDouble(paramIndex++, hd.getGiaTriThue()); 
            stmt.setString(paramIndex++, hd.getTenLoaiThue());
            
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
            
            // Tham số mới
            stmt.setDouble(paramIndex++, hd.getGiaTriThue()); 
            stmt.setString(paramIndex++, hd.getTenLoaiThue()); 

            stmt.setBoolean(paramIndex++, hd.isIsActive());
        }
    }
    
    /**
     * Tạo mã hóa đơn tự động theo format HDXXX
     * @return Mã hóa đơn mới (ví dụ: HD051)
     */
    public String generateMaHoaDon() {
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";
        String newMaHD = "HD001"; // Mã mặc định nếu chưa có hóa đơn nào
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastMaHD = rs.getString("maHoaDon");
                // Lấy phần số từ mã cuối (ví dụ: HD050 -> 050)
                String numberPart = lastMaHD.substring(2);
                int number = Integer.parseInt(numberPart);
                // Tăng lên 1 và format lại
                newMaHD = String.format("HD%03d", number + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return newMaHD;
    }
    
    /**
     * Lưu hóa đơn và chi tiết hóa đơn vào database
     * @param chiTietData Danh sách chi tiết hóa đơn (maThuoc, tenThuoc, soLuong, donGia)
     * @param tongTien Tổng tiền hóa đơn (Không dùng trong phương thức này nhưng giữ để tương thích)
     * @param maHoaDon Mã hóa đơn
     * @param maNhanVien Mã nhân viên lập hóa đơn
     * @param maKhachHang Mã khách hàng (có thể null)
     * @param maKhuyenMai Mã khuyến mãi (có thể null)
     * @param ghiChu Ghi chú hóa đơn (có thể null)
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveHoaDon(ArrayList<Object[]> chiTietData, double tongTien, String maHoaDon, String maNhanVien, 
    						 String maKhachHang, String maKhuyenMai, String ghiChu) {
        Connection con = null;
        boolean success = false;
        
        try {
            // Lấy connection và set auto commit = false để thực hiện transaction
            con = ConnectDB.getConnection(); // Sử dụng getConnection() để lấy connection
            con.setAutoCommit(false);
            
            // 1. Lưu thông tin hóa đơn
            // Sử dụng thuế VAT 10% là mức thuế mặc định theo yêu cầu
            double giaTriThue = 0.10; // Mặc định 10%
            String tenLoaiThue = "VAT 10%";
            
            HoaDon hoaDon = new HoaDon(
                maHoaDon,
                new java.util.Date(), 
                ghiChu != null ? ghiChu : "", 
                maNhanVien,
                maKhachHang,
                maKhuyenMai,
                giaTriThue, // Sử dụng giá trị thuế đã tính
                tenLoaiThue, // Sử dụng tên loại thuế đã xác định
                true 
            );
            
            // Cập nhật SQL: Xóa maThue, thêm giaTriThue và tenLoaiThue
            String sqlHoaDon = "INSERT INTO HoaDon (maHoaDon, ngayBan, ghiChu, maNV, maKH, maKM, giaTriThue, tenLoaiThue, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlHoaDon)) {
                setHoaDonParameters(stmt, hoaDon, false);
                stmt.executeUpdate();
            }
            
            // 2. Lưu chi tiết hóa đơn
            String sqlChiTiet = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, soLuong, donGia, isActive) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlChiTiet)) {
                for (Object[] item : chiTietData) {
                    String maThuoc = (String) item[0];
                    // Tên thuốc (item[1]) không cần thiết trong ChiTietHoaDon
                    int soLuong = (Integer) item[2];
                    // donGia trong chiTietData là float, nhưng cột trong DB là FLOAT/Double. Sử dụng setDouble/setFloat.
                    float donGia = (Float) item[3];
                    
                    stmt.setString(1, maHoaDon);
                    stmt.setString(2, maThuoc);
                    stmt.setInt(3, soLuong);
                    stmt.setDouble(4, donGia); // Dùng setDouble hoặc setFloat
                    stmt.setBoolean(5, true);
                    
                    stmt.executeUpdate();
                }
            }
            
            // Commit transaction
            con.commit();
            success = true;
        } catch (SQLException e) {
            // Rollback nếu có lỗi
            try {
                if (con != null) {
                    System.err.println("Transaction rolled back: " + e.getMessage());
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Reset auto commit
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    /**
     * Phương thức đơn giản hơn để lưu hóa đơn, không yêu cầu các tham số bổ sung
     * @param chiTietData Danh sách chi tiết hóa đơn
     * @param tongTien Tổng tiền hóa đơn
     * @param maHoaDon Mã hóa đơn
     * @param maNhanVien Mã nhân viên lập hóa đơn
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveHoaDon(ArrayList<Object[]> chiTietData, double tongTien, String maHoaDon, String maNhanVien) {
        return saveHoaDon(chiTietData, tongTien, maHoaDon, maNhanVien, null, null, null);
    }
    
    /**
     * Lưu hóa đơn với maLo bao gồm trong chi tiết
     * @param chiTietData Dữ liệu chi tiết [maThuoc, tenThuoc, soLuong, donGia, thanhTien, maLo]
     * @param tongTien Tổng tiền hóa đơn
     * @param maHoaDon Mã hóa đơn
     * @param maNhanVien Mã nhân viên
     * @param maKhachHang Mã khách hàng (có thể null)
     * @param maKhuyenMai Mã khuyến mãi (có thể null)
     * @param ghiChu Ghi chú (có thể null)
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveHoaDonWithMaLo(ArrayList<Object[]> chiTietData, double tongTien, String maHoaDon, String maNhanVien, 
    						 String maKhachHang, String maKhuyenMai, String ghiChu) {
        Connection con = null;
        boolean success = false;
        
        try {
            // Lấy connection và set auto commit = false để thực hiện transaction
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);
            
            // 1. Lưu thông tin hóa đơn
            double giaTriThue = 0.10; // Mặc định 10%
            String tenLoaiThue = "VAT 10%";
            
            HoaDon hoaDon = new HoaDon(
                maHoaDon,
                new java.util.Date(), 
                ghiChu != null ? ghiChu : "", 
                maNhanVien,
                maKhachHang,
                maKhuyenMai,
                giaTriThue,
                tenLoaiThue,
                true 
            );
            
            String sqlHoaDon = "INSERT INTO HoaDon (maHoaDon, ngayBan, ghiChu, maNV, maKH, maKM, giaTriThue, tenLoaiThue, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlHoaDon)) {
                setHoaDonParameters(stmt, hoaDon, false);
                stmt.executeUpdate();
            }
            
            // 2. Lưu chi tiết hóa đơn với maLo
            String sqlChiTiet = "INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, maLo, soLuong, donGia, isActive) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlChiTiet)) {
                for (Object[] item : chiTietData) {
                    String maThuoc = (String) item[0];
                    // item[1] = tenThuoc (không cần thiết trong ChiTietHoaDon)
                    int soLuong = (Integer) item[2];
                    float donGia = (Float) item[3];
                    // item[4] = thanhTien (không cần thiết)
                    String maLo = item.length > 5 ? (String) item[5] : "N/A"; // Lấy maLo nếu có
                    
                    stmt.setString(1, maHoaDon);
                    stmt.setString(2, maThuoc);
                    stmt.setString(3, maLo);
                    stmt.setInt(4, soLuong);
                    stmt.setDouble(5, donGia);
                    stmt.setBoolean(6, true);
                    
                    stmt.executeUpdate();
                }
            }
            
            // Commit transaction
            con.commit();
            success = true;
            
        } catch (SQLException e) {
            // Rollback nếu có lỗi
            if (con != null) {
                try {
                    con.rollback();
                    System.err.println("Transaction rolled back: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Khôi phục auto commit
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return success;
    }
}
