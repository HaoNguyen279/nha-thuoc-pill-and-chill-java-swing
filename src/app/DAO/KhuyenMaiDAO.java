package app.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.Entity.*;
import app.ConnectDB.ConnectDB;

public class KhuyenMaiDAO {
    ArrayList<KhuyenMai> dskm;
    KhuyenMai km;
    
    public KhuyenMaiDAO() {
        dskm = new ArrayList<KhuyenMai>();
        km = new KhuyenMai();
    }
    
    /**
     * Lấy danh sách tất cả khuyến mãi
     * @return ArrayList<KhuyenMai>
     */
    public ArrayList<KhuyenMai> getListKhuyenMai(){
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM KhuyenMai";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String maKM = rs.getString(1);
                float mucGiam = rs.getFloat(2);
                Date ngayApDung = rs.getDate(3);
                Date ngayKetThuc = rs.getDate(4);
                
                KhuyenMai km1 = new KhuyenMai(maKM, mucGiam, ngayApDung, ngayKetThuc);
                dskm.add(km1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskm;
    }
    
    /**
     * Lấy thông tin khuyến mãi theo mã
     * @param maKM - Mã khuyến mãi cần tìm
     * @return KhuyenMai
     */
    public KhuyenMai getKhuyenMai(String maKM) {
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            
            String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
            PreparedStatement statement = null;
            statement = con.prepareStatement(sql);  
            statement.setString(1, maKM);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                String maKM1 = rs.getString(1);
                float mucGiam = rs.getFloat(2);
                Date ngayApDung = rs.getDate(3);
                Date ngayKetThuc = rs.getDate(4);
                
                km = new KhuyenMai(maKM1, mucGiam, ngayApDung, ngayKetThuc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return km;
    }
    
    /**
     * Cập nhật thông tin khuyến mãi
     * @param km - Đối tượng KhuyenMai cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean update(KhuyenMai km){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            stmt = con.prepareStatement("UPDATE KhuyenMai SET mucGiam=?, ngayApDung=?, ngayKetThuc=? WHERE maKM=?");
            stmt.setFloat(1, km.getMucGiam());
            stmt.setDate(2, km.getNgayApDung());
            stmt.setDate(3, km.getNgayKetThuc());
            stmt.setString(4, km.getMaKM());

            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
   
        return n > 0;	
    }

    /**
     * Thêm mới khuyến mãi vào database
     * @param km - Đối tượng KhuyenMai cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean create(KhuyenMai km) throws SQLException{
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        stmt = con.prepareStatement("INSERT INTO KhuyenMai VALUES(?, ?, ?, ?)");
        stmt.setString(1, km.getMaKM());
        stmt.setFloat(2, km.getMucGiam());
        stmt.setDate(3, km.getNgayApDung());
        stmt.setDate(4, km.getNgayKetThuc());

        n = stmt.executeUpdate();

        return n > 0;
    }
    
    /**
     * Xóa khuyến mãi theo mã
     * @param maKM - Mã khuyến mãi cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean delete(String maKM) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        try {
            stmt = con.prepareStatement("DELETE FROM KhuyenMai WHERE maKM=?");
            stmt.setString(1, maKM);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    /**
     * Lấy danh sách khuyến mãi đang còn hiệu lực
     * @return ArrayList<KhuyenMai>
     */
    public ArrayList<KhuyenMai> getKhuyenMaiHieuLuc(){
        dskm = new ArrayList<KhuyenMai>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM KhuyenMai WHERE GETDATE() BETWEEN ngayApDung AND ngayKetThuc";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String maKM = rs.getString(1);
                float mucGiam = rs.getFloat(2);
                Date ngayApDung = rs.getDate(3);
                Date ngayKetThuc = rs.getDate(4);
                
                KhuyenMai km1 = new KhuyenMai(maKM, mucGiam, ngayApDung, ngayKetThuc);
                dskm.add(km1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskm;
    }
}