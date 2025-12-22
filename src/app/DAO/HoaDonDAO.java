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
import app.DTO.HoaDonKemGiaDTO;
import app.DTO.HoaDonKemGiaDTO;
import app.Entity.DoanhThuHoaDon;
import app.Entity.DoanhThuTheoThang;
import app.Entity.HoaDon;
import app.Entity.NhanVien;
import app.Entity.NhanVien;
import app.Entity.ThongKeNhanVien;

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
     * Lấy danh sách hóa đơn của một khách hàng kèm tổng tiền
     */
    public ArrayList<HoaDonKemGiaDTO> getHoaDonTheoKhachHang(String maKH) {
        ArrayList<HoaDonKemGiaDTO> dsHoaDonKemGia = new ArrayList<>();
        String sql = "SELECT hd.maHoaDon, nv.tenNV, kh.tenKH, hd.ngayBan, hd.ghiChu, SUM(cthd.donGia * cthd.soLuong) AS tongTien\n"
                + "FROM HoaDon hd\n"
                + "JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
                + "JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
                + "JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon\n"
                + "WHERE hd.isActive = 1 AND hd.maKH = ?\n"
                + "GROUP BY hd.maHoaDon, nv.tenNV, kh.tenKH, hd.ngayBan, hd.ghiChu\n"
                + "ORDER BY hd.ngayBan DESC, hd.maHoaDon DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsHoaDonKemGia.add(mapResToInvoiceWithTotal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsHoaDonKemGia;
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
     * Tạo mã hóa đơn tự động theo định dạng HD + 5 chữ số
     * @return Mã hóa đơn mới (VD: HD00001)
     */
    public String generateMaHoaDon() {
        String newMaHD = null;
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastMaHD = rs.getString("maHoaDon");
                // Lấy phần số từ mã hóa đơn cuối (VD: HD00150 -> 150)
                int number = Integer.parseInt(lastMaHD.substring(2));
                // Tăng lên 1
                number++;
                // Format lại thành HD00151
                newMaHD = String.format("HD%05d", number);
            } else {
                // Nếu chưa có hóa đơn nào, bắt đầu từ HD00001
                newMaHD = "HD00001";
            }
            
            // Kiểm tra xem mã vừa sinh có tồn tại không (để chắc chắn)
            while (checkMaHoaDonExists(newMaHD)) {
                int number = Integer.parseInt(newMaHD.substring(2));
                number++;
                newMaHD = String.format("HD%05d", number);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return newMaHD;
    }

    /**
     * Kiểm tra mã hóa đơn đã tồn tại chưa
     */
    private boolean checkMaHoaDonExists(String maHD) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
    
    /**
     * Thống kê doanh thu theo từng tháng trong năm bằng stored procedure
     * @param nam Năm cần thống kê
     * @return ArrayList chứa doanh thu của từng tháng trong năm
     */
    public ArrayList<DoanhThuTheoThang> thongKeDoanhThuTheoThang(int nam) {
        ArrayList<DoanhThuTheoThang> dsDoanhThu = new ArrayList<>();
        String sql = "{CALL sp_ThongKeDoanhThuTheoThang(?)}";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // Set parameter cho stored procedure
            stmt.setInt(1, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int thang = rs.getInt("Thang");
                    double doanhThu = rs.getDouble("DoanhThu");
                    
                    // Tạo đối tượng DoanhThuTheoThang
                    DoanhThuTheoThang dt = new DoanhThuTheoThang();
                    dt.setThang(thang);
                    dt.setNam(nam);
                    dt.setdoanhThu(doanhThu);
                    dt.setSoLuongHoaDon(0); // Không có thông tin số lượng hóa đơn từ SP này
                    
                    dsDoanhThu.add(dt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsDoanhThu;
    }
    /**
     * Thống kê số hóa đơn trong ngày bằng stored procedure
     * @param nam Năm cần thống kê, thang là tháng thống kê
     * @return ArrayList ngày và số hóa đơn
     */
    public ArrayList<DoanhThuHoaDon> getDoanhThuTheoNgay(int thang,int nam) {
    	ArrayList<DoanhThuHoaDon> result = new ArrayList<DoanhThuHoaDon>();
        String sql = "{CALL sp_ThongKeDoanhThuTheoNgay(?,?)}";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // Set parameter cho stored procedure
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int ngay = rs.getInt("Ngay");
                    int doanhThu = rs.getInt("doanhThu");
                   
                    DoanhThuHoaDon hd = new DoanhThuHoaDon();
                    hd.setNgay(ngay);
                    hd.setDoanhThu(doanhThu);
                    result.add(hd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public double getDoanhThuCuaThang(int thang, int nam) {

        String sql = "{CALL sp_GetDoanhThuCuaThang(?,?)}";
        double result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // Set parameter cho stored procedure
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getDouble("TongDoanhThu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        
        return result;
    }
    public double getDoanhThuCuaNam(int nam) {
        String sql = "{CALL sp_GetDoanhThuCuaNam(?)}";
        double result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            // Set parameter cho stored procedure
            stmt.setInt(1, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getDouble("TongDoanhThu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        
        return result;
    }
    
    public int getSoHoaDonTheoThang(int thang, int nam) {
        String sql = "{CALL sp_GetSoHoaDonTheoThang(?,?)}";
        int result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            // Set parameter cho stored procedure
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getInt("SoHoaDon");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }
    
    public int getSoHoaDonTheoNam(int nam) {
        String sql = "{CALL sp_GetSoHoaDonTheoNam(?)}";
        int result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            // Set parameter cho stored procedure
            stmt.setInt(1, nam);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result = rs.getInt("SoHoaDon");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }
    
    /**
     * Lấy doanh thu trung bình của 1 ngày trong tháng.
     * Gọi stored procedure sp_GetDoanhThuTrungBinhTheoNgay.
     * @param thang Tháng cần thống kê (1-12).
     * @param nam Năm cần thống kê.
     * @return Doanh thu trung bình mỗi ngày trong tháng.
     */
    public double getDoanhThuTrungBinhTheoNgay(int thang, int nam) {
        String sql = "{CALL sp_GetDoanhThuTrungBinhTheoNgay(?,?)}";
        double result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            // Set parameter cho stored procedure
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble("DoanhThuTrungBinhMoiNgay");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }
    
    /**
     * Lấy doanh thu trung bình của mỗi tháng trong năm.
     * Gọi stored procedure sp_GetDoanhThuTrungBinhTheoThang.
     * @param nam Năm cần thống kê.
     * @return Doanh thu trung bình mỗi tháng trong năm (tính theo số tháng đã qua).
     */
    public double getDoanhThuTrungBinhTheoThang(int nam) {
        String sql = "{CALL sp_GetDoanhThuTrungBinhTheoThang(?)}";
        double result = 0;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            // Set parameter cho stored procedure
            stmt.setInt(1, nam);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getDouble("DoanhThuTrungBinhMoiThang");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }
    
    
  //Thống kê doanh thu của nhân viên
    public ArrayList<ThongKeNhanVien> thongKeDoanhThuNhanVien(int nam) {
        ArrayList<ThongKeNhanVien> dsThongKe = new ArrayList<>();
        
        // Tính tổng doanh thu công ty để tính tỷ lệ đóng góp
        double tongDoanhThuCongTy = getDoanhThuCuaNam(nam);
        
        String sql = "SELECT " +
            "hd.maNV, " +
            "nv.tenNV, " +
            "COUNT(DISTINCT hd.maHoaDon) AS soLuongDonHang, " +
            "COUNT(DISTINCT hd.maKH) AS soLuongKhachHang, " +
            "SUM((ct.soLuong * ct.donGia) * (1 + hd.giaTriThue)) AS doanhThu " +
            "FROM HoaDon hd " +
            "INNER JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
            "INNER JOIN NhanVien nv ON hd.maNV = nv.maNV " +
            "WHERE hd.isActive = 1 AND ct.isActive = 1 " +
            "AND YEAR(hd.ngayBan) = ? " +
            "GROUP BY hd.maNV, nv.tenNV " +
            "ORDER BY doanhThu DESC";
        
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nam);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                int soLuongDonHang = rs.getInt("soLuongDonHang");
                int soLuongKhachHang = rs.getInt("soLuongKhachHang");
                double doanhThu = rs.getDouble("doanhThu");
                double giaTriTrungBinh = doanhThu / soLuongDonHang;
                double tyLeDongGop = (doanhThu / tongDoanhThuCongTy) * 100;
                
                ThongKeNhanVien tk = new ThongKeNhanVien(
                    maNV, tenNV, soLuongDonHang, soLuongKhachHang, 
                    doanhThu, giaTriTrungBinh, tyLeDongGop
                );
                dsThongKe.add(tk);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return dsThongKe;
    }
    
    
    
    
    /**
     * Lấy top N nhân viên có doanh thu cao nhất
     */
    public ArrayList<ThongKeNhanVien> getTopNhanVien(int nam, int topN) {
        ArrayList<ThongKeNhanVien> dsThongKe = thongKeDoanhThuNhanVien(nam);
        
        // Đã được sắp xếp DESC trong query
        if(dsThongKe.size() > topN) {
            return new ArrayList<>(dsThongKe.subList(0, topN));
        }
        
        return dsThongKe;
    }
    
    
    
    /**
     * Lấy doanh thu của nhân viên theo tháng trong năm
     * Hao
     */
    public ArrayList<DoanhThuTheoThang> thongKeDoanhThuNhanVienTheoThang(String maNV, int nam) {
        ArrayList<DoanhThuTheoThang> dsDoanhThu = new ArrayList<>();
        
        String sql = "SELECT " +
            "MONTH(hd.ngayBan) AS thang, " +
            "SUM((ct.soLuong * ct.donGia) * (1 + hd.giaTriThue)) AS doanhThu " +
            "FROM HoaDon hd " +
            "INNER JOIN ChiTietHoaDon ct ON hd.maHoaDon = ct.maHoaDon " +
            "WHERE hd.maNV = ? AND YEAR(hd.ngayBan) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1 " +
            "GROUP BY MONTH(hd.ngayBan) " +
            "ORDER BY thang";
        
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            stmt.setInt(2, nam);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                int thang = rs.getInt("thang");
                double doanhThu = rs.getDouble("doanhThu");
                DoanhThuTheoThang dt = new DoanhThuTheoThang(thang,nam,doanhThu);
                dsDoanhThu.add(dt);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return dsDoanhThu;
    }
    
    /**
     * Lấy list năm có hóa đơn
     * Hao
     */
    public ArrayList<Integer> getNamCoHoaDon() {
        ArrayList<Integer> res = new ArrayList<>();
        String sql = "select distinct year(ngayBan) as nam from HoaDon";
        Connection con = ConnectDB.getInstance().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(rs.getInt("nam"));
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
    	return res;
    }
    /**
     * Lấy list tháng có hóa đơn trong năm
     * Hao
     */
    public ArrayList<Integer> getThangCoHoaDonTrongNam(int nam) {
        ArrayList<Integer> res = new ArrayList<>();
        String sql = "select distinct month(ngayBan) as thang from HoaDon where year(ngayBan) = ?";
        
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nam);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
            	res.add(rs.getInt("thang"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    	return res;
    }
    
    /**
     * Lấy hóa đơn theo tháng, năm 
     * Hao
     */
    public ArrayList<HoaDonKemGiaDTO> getHoaDonTrongThang(int thang, int nam) {
        ArrayList<HoaDonKemGiaDTO> dsHoaDonKemGia = new ArrayList<>();
        String sql = "SELECT hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu, SUM(cthd.donGia*cthd.soLuong ) as tongTien\n"
        		+ "FROM HoaDon hd JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
        		+ "	JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
        		+ "	JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon\n"
        		+ "WHERE hd.isActive = 1 AND datepart(MM, hd.ngayBan) = ? AND year(hd.ngayBan) = ?\n"
        		+ "GROUP BY hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu\n"
        		+ "ORDER BY ngayBan DESC, hd.maHoaDon DESC";
        

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
            	dsHoaDonKemGia.add(mapResToInvoiceWithTotal(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return dsHoaDonKemGia;
    }
    
    /**
     * Lấy hóa đơn theo năm
     * Hao
     */
    public ArrayList<HoaDonKemGiaDTO> getHoaDonTrongNam(int nam) {
        ArrayList<HoaDonKemGiaDTO> dsHoaDonKemGia = new ArrayList<>();
        String sql = "SELECT hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu, SUM(cthd.donGia*cthd.soLuong ) as tongTien\n"
        		+ "FROM HoaDon hd JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
        		+ "	JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
        		+ "	JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon\n"
        		+ "WHERE hd.isActive = 1 AND year(hd.ngayBan) = ?\n"
        		+ "GROUP BY hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu\n"
        		+ "ORDER BY ngayBan DESC, hd.maHoaDon DESC";

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nam);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
            	dsHoaDonKemGia.add(mapResToInvoiceWithTotal(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return dsHoaDonKemGia;
    }
    
    public ArrayList<HoaDonKemGiaDTO> getHoaDonTrongNamCuaNhanVien(int nam,String maNV) {
        ArrayList<HoaDonKemGiaDTO> dsHoaDonKemGia = new ArrayList<>();
        String sql = "SELECT hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu, SUM(cthd.donGia*cthd.soLuong ) as tongTien\n"
        		+ "FROM HoaDon hd JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
        		+ "	JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
        		+ "	JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon\n"
        		+ "WHERE hd.isActive = 1 AND year(hd.ngayBan) = ?  AND nv.maNV = ?\n"
        		+ "GROUP BY hd.maHoaDon, tenNV, tenKH, ngayBan, ghiChu\n"
        		+ "ORDER BY ngayBan DESC, hd.maHoaDon DESC";

        try {
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, nam);
            stmt.setString(2,maNV);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
            	dsHoaDonKemGia.add(mapResToInvoiceWithTotal(rs));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return dsHoaDonKemGia;
    }
    
    
    
    
    
    /**
     * Clone function, map res to invoice with total
     * Hao
     */
    
    private HoaDonKemGiaDTO mapResToInvoiceWithTotal(ResultSet rs) throws SQLException {
        return new HoaDonKemGiaDTO(
            rs.getString("maHoaDon"),
            rs.getString("tenNV"),
            rs.getString("tenKH"),
            rs.getDate("ngayBan"),
            rs.getString("ghiChu"),
            rs.getDouble("tongTien")
        );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public ArrayList<HoaDon> getAllHoaDon5Field() {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT maHoaDon, tenNV, tenKH, ngayBan, ghiChu, hd.isActive\n"
        		+ "FROM HoaDon hd JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
        		+ "	JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
        		+ "WHERE hd.isActive = 1"
        		+ "ORDER BY ngayBan DESC, maHoaDon DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsHoaDon.add(mapResultSetToHoaDon5Field(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    
    public ArrayList<HoaDon> getAllHoaDon5Field(String maThuoc) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql =
        	    "SELECT hd.maHoaDon, nv.tenNV, kh.tenKH, ngayBan, ghiChu, hd.isActive\n" +
        	    "FROM HoaDon hd \n" +
        	    "JOIN NhanVien nv ON hd.maNV = nv.maNV \n" +
        	    "JOIN KhachHang kh ON kh.maKH = hd.maKH \n" +
        	    "JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon \n" +
        	    "WHERE hd.isActive = 1 AND cthd.maThuoc = ? \n" +
        	    "GROUP BY hd.maHoaDon, nv.tenNV, kh.tenKH, ngayBan, ghiChu, hd.isActive \n" +
        	    "ORDER BY ngayBan DESC, hd.maHoaDon DESC";

        try (Connection con = ConnectDB.getInstance().getConnection()) {
             PreparedStatement stmt = con.prepareStatement(sql);
        	 stmt.setString(1, maThuoc);
            
        	 try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsHoaDon.add(mapResultSetToHoaDon5Field(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
        
        
        
        
        
    }
    
    private HoaDon mapResultSetToHoaDon5Field(ResultSet rs) throws SQLException {
        return new HoaDon(
            rs.getString("maHoaDon"),
            rs.getString("tenNV"),
            rs.getString("tenKH"),
            rs.getDate("ngayBan"),
            rs.getString("ghiChu"),
            rs.getBoolean("isActive")
        );
    }
    
    //lala
    public ArrayList<HoaDon> findHoaDonByThangNam(int thang, int nam) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT maHoaDon, tenNV, tenKH, ngayBan, ghiChu, hd.isActive\n"
        		+ "FROM HoaDon hd JOIN KhachHang kh ON hd.maKH = kh.maKH\n"
        		+ "	JOIN NhanVien nv ON nv.maNV = hd.maNV\n"
        		+ "WHERE hd.isActive = 1 AND MONTH(ngayBan) = ? AND YEAR(ngayBan) = ?\n"
        		+ "ORDER BY ngayBan DESC, maHoaDon DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dsHoaDon.add(mapResultSetToHoaDon5Field(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }
    
    /**
     * Cập nhật điểm tích lũy cho khách hàng sau khi lưu hóa đơn
     */
    public boolean capNhatDiemTichLuy(String maHoaDon) {
        String sql = "UPDATE KhachHang " +
                     "SET diemTichLuy = ISNULL(diemTichLuy, 0) + ISNULL(( " +
                     "    SELECT FLOOR(SUM(ct.soLuong * ct.donGia) / 1000) " +
                     "    FROM ChiTietHoaDon ct " +
                     "    WHERE ct.maHoaDon = ? AND ct.isActive = 1 " +
                     "), 0) " +
                     "FROM KhachHang kh " +
                     "INNER JOIN HoaDon hd ON kh.maKH = hd.maKH " +
                     "WHERE hd.maHoaDon = ? AND hd.maKH IS NOT NULL";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Trừ điểm tích lũy đã sử dụng cho khách hàng
     * @param maKhachHang Mã khách hàng
     * @param diemSuDung Số điểm đã sử dụng
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean truDiemTichLuy(String maKhachHang, int diemSuDung) {
        if (maKhachHang == null || maKhachHang.isEmpty() || diemSuDung <= 0) {
            return true; // Không có điểm để trừ
        }
        
        String sql = "UPDATE KhachHang SET diemTichLuy = diemTichLuy - ? WHERE maKH = ? AND diemTichLuy >= ?";
        
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, diemSuDung);
            stmt.setString(2, maKhachHang);
            stmt.setInt(3, diemSuDung);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
}





