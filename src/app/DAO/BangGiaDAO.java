package app.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.BangGia;

public class BangGiaDAO {

    public ArrayList<BangGia> getAllBangGia() {
        ArrayList<BangGia> dsBangGia = new ArrayList<>();
        String sql = "SELECT * FROM BangGia";
        Connection con = ConnectDB.getConnection();
        if (con == null) return dsBangGia;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maBangGia = rs.getString("maBangGia");
                String tenBangGia = rs.getString("tenBangGia");
                String loaiGia = rs.getString("loaiGia");
                Date ngayApDung = rs.getDate("ngayApDung");
                Date ngayKetThuc = rs.getDate("ngayKetThuc");
                String trangThai = rs.getString("trangThai");
                String ghiChu = rs.getString("ghiChu");
                int doUuTien = rs.getInt("doUuTien");
                boolean isActive = rs.getBoolean("isActive");

                BangGia bg = new BangGia(maBangGia, tenBangGia, loaiGia, ngayApDung, ngayKetThuc, trangThai, ghiChu, doUuTien, isActive);
                dsBangGia.add(bg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsBangGia;
    }

    public BangGia getBangGiaTheoMa(String maBangGia) {
        BangGia bg = null;
        String sql = "SELECT * FROM BangGia WHERE maBangGia = ?";
        Connection con = ConnectDB.getConnection();
        if (con == null) return null;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maBangGia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String tenBangGia = rs.getString("tenBangGia");
                String loaiGia = rs.getString("loaiGia");
                Date ngayApDung = rs.getDate("ngayApDung");
                Date ngayKetThuc = rs.getDate("ngayKetThuc");
                String trangThai = rs.getString("trangThai");
                String ghiChu = rs.getString("ghiChu");
                int doUuTien = rs.getInt("doUuTien");
                boolean isActive = rs.getBoolean("isActive");

                bg = new BangGia(maBangGia, tenBangGia, loaiGia, ngayApDung, ngayKetThuc, trangThai, ghiChu, doUuTien, isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bg;
    }

    public boolean themBangGia(BangGia bg) {
        Connection con = ConnectDB.getConnection();
        String sql = "INSERT INTO BangGia VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, bg.getMaBangGia());
            stmt.setString(2, bg.getTenBangGia());
            stmt.setString(3, bg.getLoaiGia());
            stmt.setDate(4, bg.getNgayApDung());
            stmt.setDate(5, bg.getNgayKetThuc());
            stmt.setString(6, bg.getTrangThai());
            stmt.setString(7, bg.getGhiChu());
            stmt.setInt(8, bg.getDoUuTien());
            stmt.setBoolean(9, bg.isActive());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatBangGia(BangGia bg) {
        Connection con = ConnectDB.getConnection();
        String sql = "UPDATE BangGia SET tenBangGia=?, loaiGia=?, ngayApDung=?, ngayKetThuc=?, trangThai=?, ghiChu=?, doUuTien=?, isActive=? WHERE maBangGia=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, bg.getTenBangGia());
            stmt.setString(2, bg.getLoaiGia());
            stmt.setDate(3, bg.getNgayApDung());
            stmt.setDate(4, bg.getNgayKetThuc());
            stmt.setString(5, bg.getTrangThai());
            stmt.setString(6, bg.getGhiChu());
            stmt.setInt(7, bg.getDoUuTien());
            stmt.setBoolean(8, bg.isActive());
            stmt.setString(9, bg.getMaBangGia());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
