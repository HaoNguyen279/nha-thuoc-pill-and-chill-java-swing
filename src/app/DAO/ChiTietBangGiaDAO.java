package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietBangGia;

public class ChiTietBangGiaDAO {

    public ArrayList<ChiTietBangGia> getChiTietBangGiaTheoMa(String maBangGia) {
        ArrayList<ChiTietBangGia> dsChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietBangGia WHERE maBangGia = ?";
        Connection con = ConnectDB.getConnection();
        if (con == null) return dsChiTiet;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maBangGia);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
                double donGia = rs.getDouble("donGia");
                String donVi = rs.getString("maDonVi");
                boolean isActive = rs.getBoolean("isActive");

                ChiTietBangGia ct = new ChiTietBangGia(maBangGia, maThuoc, donGia, donVi, isActive);
                dsChiTiet.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTiet;
    }

    public boolean themChiTietBangGia(ChiTietBangGia ct) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO ChiTietBangGia VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, ct.getMaBangGia());
            stmt.setString(2, ct.getMaThuoc());
            stmt.setDouble(3, ct.getDonGia());
            stmt.setString(4, ct.getDonVi());
            stmt.setBoolean(5, ct.isActive());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean themDanhSachChiTietBangGia(ArrayList<ChiTietBangGia> danhSachCT) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO ChiTietBangGia VALUES(?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(sql);
            
            for (ChiTietBangGia ct :  danhSachCT) {
                stmt.setString(1, ct.getMaBangGia());
                stmt.setString(2, ct. getMaThuoc());
                stmt.setDouble(3, ct.getDonGia());
                stmt.setString(4, ct.getDonVi());
                stmt.setBoolean(5, ct.isActive());
                stmt.addBatch(); 
            }
            
            int[] ketQua = stmt.executeBatch(); 
            con.commit();
            

            for (int kq : ketQua) {
                if (kq <= 0 && kq != PreparedStatement.SUCCESS_NO_INFO) {
                    return false;
                }
            }
            return true;
            
        } catch (SQLException e) {
            try {
                con. rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                con.setAutoCommit(true); 
            } catch (SQLException e) {
                e. printStackTrace();
            }
        }
        return false;
    }

    public boolean capNhatChiTietBangGia(ChiTietBangGia ct) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE ChiTietBangGia SET donGia=?, maDonVi=?, isActive=? WHERE maBangGia=? AND maThuoc=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, ct.getDonGia());
            stmt.setString(2, ct.getDonVi());
            stmt.setBoolean(3, ct.isActive());
            stmt.setString(4, ct.getMaBangGia());
            stmt.setString(5, ct.getMaThuoc());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa chi tiết bảng giá theo mã bảng giá và mã thuốc
     * @param maBangGia Mã bảng giá
     * @param maThuoc Mã thuốc
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean xoaChiTietBangGia(String maBangGia, String maThuoc) {
        Connection con = ConnectDB.getConnection();
        String sql = "DELETE FROM ChiTietBangGia WHERE maBangGia=? AND maThuoc=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maBangGia);
            stmt.setString(2, maThuoc);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
