package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChucVu;
import app.Entity.NhanVien;
import app.Entity.TaiKhoan;

/**
 * DAO (Data Access Object) cho thực thể NhanVien.
 * Chịu trách nhiệm xử lý tất cả các thao tác cơ sở dữ liệu liên quan đến nhân viên.
 */
public class NhanVienDAO {

    /**
     * Lấy danh sách tất cả nhân viên đang hoạt động từ cơ sở dữ liệu.
     * @return Một ArrayList chứa các đối tượng NhanVien.
     */
    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
        String sql = "SELECT maNV, tenNV, maChucVu, soDienThoai, isActive FROM NhanVien WHERE isActive = 1"; // Chỉ lấy nhân viên đang làm việc
        Connection con = ConnectDB.getInstance().getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            ChucVuDAO cvDAO = new ChucVuDAO();

            
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                String maChucVu = rs.getString("maChucVu");
                String soDienThoai = rs.getString("soDienThoai");
                boolean isActive = rs.getBoolean("isActive");
                
                NhanVien nv = new NhanVien(maNV, tenNV, maChucVu, soDienThoai, isActive);
                dsNhanVien.add(nv);
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
        return dsNhanVien;
    }

    /**
     * Lấy thông tin một nhân viên dựa vào mã nhân viên (ID).
     * @param id Mã của nhân viên cần tìm.
     * @return Một đối tượng NhanVien nếu tìm thấy, ngược lại trả về null.
     */
    public NhanVien getNhanVienById(String id) {
        String sql = "SELECT * FROM NhanVien WHERE isActive = 1 AND maNV =?";
        NhanVien nv = null; 
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                String chucVu = ""; // Mặc định nếu không có trường chucVu
                
                // Kiểm tra xem có trường chucVu hay không trước khi truy cập
                try {
                    chucVu = rs.getString("maChucVu");
                } catch (SQLException e) {
                    // Có thể thử với tên trường khác nếu có
                    try {
                        chucVu = rs.getString("viTri"); // Thử với tên trường có thể là viTri
                    } catch (SQLException e2) {
                        // Không làm gì, giữ giá trị mặc định
                    }
                }
                
                String soDienThoai = rs.getString("soDienThoai");
                boolean isActive = rs.getBoolean("isActive");
                
                nv = new NhanVien(maNV, tenNV, chucVu, soDienThoai, isActive);
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
        return nv;
    }

    /**
     * Thêm một nhân viên mới vào cơ sở dữ liệu.
     * @param nhanVien Đối tượng NhanVien cần thêm.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addNhanVien(NhanVien nhanVien) {
        String sql = "INSERT INTO NhanVien (maNV, tenNV, maChucVu, soDienThoai, isActive) VALUES (?, ?, ?, ?, ?)";
        int n = 0;
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getMaNV());
            stmt.setString(2, nhanVien.getTenNV());
            stmt.setString(3, nhanVien.getmaChucVu());
            stmt.setString(4, nhanVien.getSoDienThoai());
            stmt.setBoolean(5, nhanVien.isIsActive());
            
            n = stmt.executeUpdate();
            
    		TaiKhoanDAO tkDAO = new TaiKhoanDAO();
    		TaiKhoan tk = new TaiKhoan(nhanVien.getMaNV(),"1",true);
    		tkDAO.addTaiKhoan(tk);
    		
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
     * Cập nhật thông tin của một nhân viên đã có trong cơ sở dữ liệu.
     * @param nhanVien Đối tượng NhanVien chứa thông tin đã được cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updateNhanVien(NhanVien nhanVien) {
        String sql = "UPDATE NhanVien SET tenNV = ?, maChucVu = ?, soDienThoai = ?, isActive = ? WHERE maNV = ?";
        int n = 0;
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getTenNV());
            stmt.setString(2, nhanVien.getmaChucVu());
            stmt.setString(3, nhanVien.getSoDienThoai());
            stmt.setBoolean(4, nhanVien.isIsActive());
            stmt.setString(5, nhanVien.getMaNV());
            
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
     * Xóa một nhân viên khỏi hệ thống bằng cách cập nhật trạng thái isActive = 0 (xóa mềm).
     * @param id Mã của nhân viên cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteNhanVien(String id) {
        String sql = "UPDATE NhanVien SET isActive = 0 WHERE maNV = ?";
        int n = 0;
        Connection con = ConnectDB.getInstance().getConnection();
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
    
    
    public boolean isQuanLy(String id) {
        String sql = "SELECT maChucVu FROM NhanVien WHERE isActive = 1 AND maNV =?";
        NhanVien nv = null; 
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String chucVu = ""; 
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                chucVu = ""; // Mặc định nếu không có trường chucVu
                
                try {
                    chucVu = rs.getString("maChucVu");
                } catch (SQLException e) {
                	System.err.println("11");
                }
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
        return chucVu.equalsIgnoreCase("QLY");
    }
    
    // Đã có phương thức getNhanVienById ở trên nên phần này đã bị xóa để tránh trùng lặp
}