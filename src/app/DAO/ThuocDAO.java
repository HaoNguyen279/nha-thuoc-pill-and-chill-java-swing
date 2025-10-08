package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;

import app.Entity.Thuoc;

public class ThuocDAO {
	ArrayList<Thuoc> dsThuoc;
	
	public ThuocDAO() {
		dsThuoc = new ArrayList<Thuoc>();
		
	}
	
	public ArrayList<Thuoc> getListThuoc(){
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            Statement statement = con.createStatement();
            String sql = "SELECT * FROM THUOC";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String maThuoc = rs.getString(1);
                String maLo = rs.getString(2);
                String tenThuoc = rs.getString(3);
               	int soLuongTon = rs.getInt(4);
                Double giaBan = rs.getDouble(5);
                String donVi = rs.getString(6);
                int soLuongToiThieu = rs.getInt(7);
                String maNSX = rs.getString(8);
                Thuoc thuoc1 = new Thuoc(maThuoc, maLo, tenThuoc, soLuongTon, soLuongTon, donVi, soLuongToiThieu, maNSX);
                dsThuoc.add(thuoc1);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsThuoc;
	}
	
	public Thuoc getThuoc(String maThuoc) {
		Thuoc thuoc1 = new Thuoc();
    	try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM	THUOC where maThuoc = ?";
            PreparedStatement statement = null;
            statement = con.prepareStatement(sql);  
            statement.setString(1, maThuoc);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
            	String maThuoc1 = rs.getString(1);
                String maLo = rs.getString(2);
                String tenThuoc = rs.getString(3);
               	int soLuongTon = rs.getInt(4);
                Double giaBan = rs.getDouble(5);
                String donVi = rs.getString(6);
                int soLuongToiThieu = rs.getInt(7);
                String maNSX = rs.getString(8);
                
                 thuoc1 = new Thuoc(maThuoc1, maLo, tenThuoc, soLuongTon, soLuongTon, donVi, soLuongToiThieu, maNSX);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thuoc1;
    }
    
    public boolean update(Thuoc thuoc){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n =0 ;
        try {
        	stmt = con.prepareStatement("update THUOC set maLo=?,tenThuoc=?,soLuongTon=?,giaBan=?,donVi=?,soLuongToiThieu=?,maNSX=? where MaThuoc=?");
        	stmt.setString(1,thuoc.getMaLo());
        	stmt.setString(2,thuoc.getTenThuoc());
        	stmt.setInt(3,thuoc.getSoLuongTon());
        	stmt.setDouble(4,thuoc.getGiaBan());
        	stmt.setString(5,thuoc.getDonVi());
        	stmt.setInt(6,thuoc.getSoLuongToiThieu());
        	stmt.setString(7, thuoc.getMaNSX());
        	
        	stmt.setString(8, thuoc.getMaThuoc());
        	n = stmt.executeUpdate();
        }catch (SQLException e) {
			e.printStackTrace();
		}
   
        return n>0;	
    }

    public boolean create(Thuoc thuoc) throws SQLException{
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        int n = 0;
        stmt = con.prepareStatement("insert into" +" Thuoc values(?,?,?,?,?,?,?,?)");
        stmt.setString(1,thuoc.getMaThuoc());
        stmt.setString(2,thuoc.getMaLo());
    	stmt.setString(3,thuoc.getTenThuoc());
    	stmt.setInt(4,thuoc.getSoLuongTon());
    	stmt.setDouble(5,thuoc.getGiaBan());
    	stmt.setString(6,thuoc.getDonVi());
    	stmt.setInt(7,thuoc.getSoLuongToiThieu());
    	stmt.setString(8, thuoc.getMaNSX());

        n =stmt.executeUpdate();

        return n>0;
	}
	public static void main(String[] args) {
		ConnectDB.getInstance().connect();
		ThuocDAO dao = new ThuocDAO();
//		ArrayList<Thuoc> thuocds = new ArrayList<Thuoc>();
//		thuocds = dao.getListNhanVien();
//		for(Thuoc thuoc2 : thuocds) {
//			System.out.println(thuoc2);
//		}
		Thuoc thuock = new Thuoc();
		thuock = dao.getThuoc("T001");
		System.out.println(thuock);
		thuock.setMaLo("0101");
		dao.update(thuock);
		thuock = dao.getThuoc("T001");
		System.out.println(thuock);
		try {
			dao.create(thuock);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}

