package app. DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.Entity.DonVi;
import app.ConnectDB.*;

public class DonViDAO {
	
	private Connection con;
	
	public DonViDAO() {
		con = ConnectDB.getConnection();
	}
	
	/**
	 * Lấy tất cả đơn vị
	 * @return ArrayList<DonVi>
	 */
	public ArrayList<DonVi> getAllDonVi() {
		ArrayList<DonVi> dsDonVi = new ArrayList<>();
		String sql = "SELECT * FROM DonVi";
		
		try (Statement stmt = con.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			
			while (rs.next()) {
				String maDonVi = rs. getString("maDonVi");
				String tenDonVi = rs.getString("tenDonVi");
				
				DonVi donVi = new DonVi(maDonVi, tenDonVi, true);
				dsDonVi.add(donVi);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dsDonVi;
	}
	
	/**
	 * Tìm đơn vị theo mã
	 * @param maDonVi
	 * @return DonVi hoặc null nếu không tìm thấy
	 */
	public DonVi getDonViByMa(String maDonVi) {
		String sql = "SELECT * FROM DonVi WHERE maDonVi = ?";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, maDonVi);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String tenDonVi = rs.getString("tenDonVi");
					return new DonVi(maDonVi, tenDonVi, true);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Tìm đơn vị theo tên
	 * @param tenDonVi
	 * @return List<DonVi>
	 */
	public List<DonVi> getDonViByTen(String tenDonVi) {
		List<DonVi> dsDonVi = new ArrayList<>();
		String sql = "SELECT * FROM DonVi WHERE tenDonVi LIKE ?";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, "%" + tenDonVi + "%");
			
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String maDV = rs.getString("maDonVi");
					String tenDV = rs.getString("tenDonVi");
					
					DonVi donVi = new DonVi(maDV, tenDV, true);
					dsDonVi.add(donVi);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return dsDonVi;
	}
	
	/**
	 * Thêm đơn vị mới
	 * @param donVi
	 * @return true nếu thêm thành công, false nếu thất bại
	 */
	public boolean addDonVi(DonVi donVi) {
		String sql = "INSERT INTO DonVi (maDonVi, tenDonVi, isActive) VALUES (?, ?, ?)";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, donVi.getMaDonVi());
			pstmt.setString(2, donVi.getTenDonVi());
			pstmt.setBoolean(3, donVi.isActive());
			
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Cập nhật thông tin đơn vị
	 * @param donVi
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 */
	public boolean updateDonVi(DonVi donVi) {
		String sql = "UPDATE DonVi SET tenDonVi = ? WHERE maDonVi = ?";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, donVi.getTenDonVi());
			pstmt.setString(2, donVi.getMaDonVi());
			
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Xóa đơn vị theo mã
	 * @param maDonVi
	 * @return true nếu xóa thành công, false nếu thất bại
	 */
	public boolean deleteDonVi(String maDonVi) {
		String sql = "UPDATE DonVi SET isActive = 0 WHERE maDonVi = ?";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, maDonVi);
			
			int rowsAffected = pstmt. executeUpdate();
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Kiểm tra đơn vị có tồn tại không
	 * @param maDonVi
	 * @return true nếu tồn tại, false nếu không
	 */
	public boolean isExist(String maDonVi) {
		String sql = "SELECT COUNT(*) FROM DonVi WHERE maDonVi = ? ";
		
		try (PreparedStatement pstmt = con. prepareStatement(sql)) {
			
			pstmt.setString(1, maDonVi);
			
			try (ResultSet rs = pstmt. executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Đếm số lượng đơn vị
	 * @return số lượng đơn vị
	 */
	public int countDonVi() {
		String sql = "SELECT COUNT(*) FROM DonVi";
		
		try (Statement stmt = con.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			
			if (rs.next()) {
				return rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}