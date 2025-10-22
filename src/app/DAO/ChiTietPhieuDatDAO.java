package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietPhieuDat;

/**
 * DAO (Data Access Object) cho thực thể ChiTietPhieuDat.
 * Chịu trách nhiệm cho tất cả các hoạt động cơ sở dữ liệu liên quan đến chi tiết phiếu đặt.
 */
public class ChiTietPhieuDatDAO {

    /**
     * Lấy tất cả các chi tiết phiếu đặt đang hoạt động (isActive = 1) theo mã phiếu đặt.
     * @param maPhieuDat Mã của phiếu đặt cần lấy chi tiết.
     * @return ArrayList chứa các đối tượng ChiTietPhieuDat.
     */
    public ArrayList<ChiTietPhieuDat> getAllByPhieuDatId(String maPhieuDat) {
        ArrayList<ChiTietPhieuDat> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND isActive = 1";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuDat ct = new ChiTietPhieuDat(
                        rs.getString("maPhieuDat"),
                        rs.getString("maThuoc"),
                        rs.getString("maLo"), 
                        rs.getString("tenThuoc"),
                        rs.getInt("soLuong"),
                        rs.getBoolean("isActive")
                    );
                    dsChiTiet.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    /**
     * Lấy một chi tiết phiếu đặt cụ thể dựa trên khóa chính tổng hợp.
     * @param maPhieuDat Mã phiếu đặt.
     * @param maThuoc Mã thuốc.
     * @param maLo Mã lô.  
     * @return Đối tượng ChiTietPhieuDat nếu tìm thấy, ngược lại trả về null.
     */
    public ChiTietPhieuDat getChiTietById(String maPhieuDat, String maThuoc, String maLo) {
        // Cập nhật SQL và tham số
        String sql = "SELECT * FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND maThuoc = ? AND maLo = ?";
        ChiTietPhieuDat chiTiet = null;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maThuoc);
            stmt.setString(3, maLo); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    chiTiet = new ChiTietPhieuDat(
                        rs.getString("maPhieuDat"),
                        rs.getString("maThuoc"),
                        rs.getString("maLo"), 
                        rs.getString("tenThuoc"),
                        rs.getInt("soLuong"),
                        rs.getBoolean("isActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTiet;
    }

    /**
     * Thêm một chi tiết phiếu đặt mới vào cơ sở dữ liệu.
     * @param chiTiet Đối tượng ChiTietPhieuDat cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean create(ChiTietPhieuDat chiTiet) {
        // Cập nhật SQL và tham số
        String sql = "INSERT INTO ChiTietPhieuDat (maPhieuDat, maThuoc, maLo, tenThuoc, soLuong, isActive) VALUES (?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, chiTiet.getMaPhieuDat());
            stmt.setString(2, chiTiet.getMaThuoc());
            stmt.setString(3, chiTiet.getMaLo());
            stmt.setString(4, chiTiet.getTenThuoc());
            stmt.setInt(5, chiTiet.getSoLuong());
            stmt.setBoolean(6, chiTiet.isIsActive());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Cập nhật thông tin của một chi tiết phiếu đặt.
     * @param chiTiet Đối tượng ChiTietPhieuDat chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean update(ChiTietPhieuDat chiTiet) {
        // Cập nhật SQL (WHERE clause) và tham số
        String sql = "UPDATE ChiTietPhieuDat SET tenThuoc = ?, soLuong = ?, isActive = ? WHERE maPhieuDat = ? AND maThuoc = ? AND maLo = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, chiTiet.getTenThuoc());
            stmt.setInt(2, chiTiet.getSoLuong());
            stmt.setBoolean(3, chiTiet.isIsActive());
            stmt.setString(4, chiTiet.getMaPhieuDat());
            stmt.setString(5, chiTiet.getMaThuoc());
            stmt.setString(6, chiTiet.getMaLo());  

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Xóa mềm một chi tiết phiếu đặt bằng cách đặt isActive = 0.
     * @param maPhieuDat Mã phiếu đặt của chi tiết cần xóa.
     * @param maThuoc Mã thuốc của chi tiết cần xóa.
     * @param maLo Mã lô của chi tiết cần xóa.  
     * @return true nếu thành công, false nếu thất bại.
     */
    public boolean softDelete(String maPhieuDat, String maThuoc, String maLo) {
        // Cập nhật SQL (WHERE clause) và tham số
        String sql = "UPDATE ChiTietPhieuDat SET isActive = 0 WHERE maPhieuDat = ? AND maThuoc = ? AND maLo = ?";
        int n = 0;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            stmt.setString(2, maThuoc);
            stmt.setString(3, maLo);  
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    /**
     * Tính tổng số lượng thuốc trong một phiếu đặt (chỉ tính các chi tiết đang hoạt động).
     * @param maPhieuDat Mã phiếu đặt cần tính tổng.
     * @return Tổng số lượng.
     */
    public int getTongSoLuongTrongPhieu(String maPhieuDat) {
        int tongSoLuong = 0;
        // Câu lệnh này không cần thay đổi
        String sql = "SELECT SUM(soLuong) FROM ChiTietPhieuDat WHERE maPhieuDat = ? AND isActive = 1";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuDat);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongSoLuong = rs.getInt(1); // Lấy kết quả từ cột đầu tiên
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongSoLuong;
    }
    
    // Lưu ý: Phương thức getTongTienPhieuDat không thể được triển khai một cách chính xác
    // vì lớp ChiTietPhieuDat không chứa thông tin về đơn giá. Để tính tổng tiền,
    // cần phải JOIN với bảng chứa thông tin thuốc (ví dụ: bảng Thuoc) để lấy đơn giá.
    // Ví dụ câu lệnh SQL có thể là:
    // SELECT SUM(ct.soLuong * t.giaBan) FROM ChiTietPhieuDat ct JOIN Thuoc t ON ct.maThuoc = t.maThuoc WHERE ct.maPhieuDat = ?
}