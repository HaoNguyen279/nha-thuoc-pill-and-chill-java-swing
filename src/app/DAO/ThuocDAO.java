package app.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietLoThuoc;
import app.Entity.LoThuoc;
import app.Entity.ThongKeThuoc;
import app.Entity.Thuoc;

/**
 * DAO (Data Access Object) for the Thuoc entity.
 * Handles all database operations related to drugs.
 */
public class ThuocDAO {

    /**
     * Retrieves a list of all active drugs from the database.
     * @return An ArrayList of Thuoc objects.
     */
    public ArrayList<Thuoc> getAllThuoc() {
        ArrayList<Thuoc> dsThuoc = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE isActive = 1";
        
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            return dsThuoc;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongTon = rs.getInt("soLuongTon");
                double giaBan = rs.getDouble("giaBan");
                String donVi = rs.getString("donVi");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");
                String maNSX = rs.getString("maNSX");
                boolean isActive = rs.getBoolean("isActive");

                Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive);
                dsThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng ResultSet và Statement, KHÔNG đóng Connection vì nó là singleton
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsThuoc;
    }
    public ArrayList<Thuoc> getAllThuocCoTenNSX() {
        ArrayList<Thuoc> dsThuoc = new ArrayList<>();
        String sql = "Select  t.maThuoc,t.tenThuoc,t.soLuongTon,t.giaBan,t.donVi,t.soLuongToiThieu,nsx.tenNSX from Thuoc t join NhaSanXuat nsx on t.maNSX = nsx.maNSX where t.isActive = 1;";
        
        Connection con = ConnectDB.getConnection();
        if (con == null) {
            return dsThuoc;
        }

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String maThuoc = rs.getString("maThuoc");
           
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongTon = rs.getInt("soLuongTon");
                double giaBan = rs.getDouble("giaBan");
                String donVi = rs.getString("donVi");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");
                String maNSX = rs.getString("tenNSX");
             

                Thuoc thuoc = new Thuoc(maThuoc,  tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, true);
                dsThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng ResultSet và Statement, KHÔNG đóng Connection vì nó là singleton
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dsThuoc;
    }
    
    
    

    /**
     * Retrieves a single drug by its ID.
     * @param id The ID of the drug to find.
     * @return A Thuoc object if found, otherwise null.
     */
    public Thuoc getThuocById(String id) {
        String sql = "SELECT * FROM Thuoc WHERE maThuoc = ?";
        Thuoc thuoc = null;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maThuoc = rs.getString("maThuoc");
                    String tenThuoc = rs.getString("tenThuoc");
                    int soLuongTon = rs.getInt("soLuongTon");
                    double giaBan = rs.getDouble("giaBan");
                    String donVi = rs.getString("donVi");
                    int soLuongToiThieu = rs.getInt("soLuongToiThieu");
                    String maNSX = rs.getString("maNSX");
                    boolean isActive = rs.getBoolean("isActive");
                    
                    thuoc = new Thuoc(maThuoc, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thuoc;
    }


    /**
     * Adds a new drug to the database.
     * @param thuoc The Thuoc object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addThuoc(Thuoc thuoc) {
        String sql = "INSERT INTO Thuoc (maThuoc,tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, thuoc.getMaThuoc());
            stmt.setString(2, thuoc.getTenThuoc());
            stmt.setInt(3, thuoc.getSoLuongTon());
            stmt.setDouble(4, thuoc.getGiaBan());
            stmt.setString(5, thuoc.getDonVi());
            stmt.setInt(6, thuoc.getSoLuongToiThieu());
            stmt.setString(7, thuoc.getMaNSX());
            stmt.setBoolean(8, thuoc.isIsActive()); // Use the provided getter
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public boolean addDsThuoc(ArrayList<Thuoc> dsThuocNhap, ArrayList<ChiTietLoThuoc> dsCTLT) {
        ThuocDAO daoThuoc = new ThuocDAO();
        LoThuocDAO daoLo = new LoThuocDAO();
        ChiTietLoThuocDAO daoCTLT = new ChiTietLoThuocDAO();

      
        ArrayList<Thuoc> dsThuocCoSan = daoThuoc.getAllThuoc();               
        ArrayList<LoThuoc> dsLoThuocCoSan = daoLo.getAllLoThuoc();          
        ArrayList<ChiTietLoThuoc> dsCTLTCoSan = daoCTLT.getAllActiveChiTietLoThuoc(); 
        
        ArrayList<Thuoc> dsThuocInactive = daoThuoc.getAllInactiveThuoc(); // inactive
        ArrayList<LoThuoc> dsLoThuocInactive = daoLo.getAllInactiveLoThuoc();
        ArrayList<ChiTietLoThuoc> dsCTLTInactive = daoCTLT.getAllInactiveChiTietLoThuoc();

        
        for (int i = 0; i < dsThuocNhap.size(); i++) {
            Thuoc thuocNhap = dsThuocNhap.get(i);
            ChiTietLoThuoc ctltNhap = dsCTLT.get(i);

            String maThuoc = thuocNhap.getMaThuoc();
            String maLo = ctltNhap.getMaLo();
            
         
            boolean thuocInactive = dsThuocInactive.stream()
                    .anyMatch(t -> t.getMaThuoc().equals(maThuoc));
            
            boolean loInactive = dsLoThuocInactive.stream()
                    .anyMatch(l -> l.getMaLo().equals(maLo));
            
          
            boolean ctltInactive = dsCTLTInactive.stream()
                    .anyMatch(c -> c.getMaLo().equals(maLo) && c.getMaThuoc().equals(maThuoc));
            
            
            boolean thuocActive = dsThuocCoSan.stream()
                    .anyMatch(t -> t.getMaThuoc().equals(maThuoc));
            boolean loActive = dsLoThuocCoSan.stream()
                    .anyMatch(l -> l.getMaLo().equals(maLo));
            boolean ctltActive = dsCTLTCoSan.stream()
                    .anyMatch(c -> c.getMaLo().equals(maLo) && c.getMaThuoc().equals(maThuoc));
            
            if (thuocInactive) {
                // TH0: Thuốc bị xóa mềm → REACTIVATE
                System.out.println("   ➤ TH0: Reactivate thuốc đã bị xóa");
                daoThuoc.reactivateThuoc(maThuoc);
                thuocActive = true; // Cập nhật trạng thái
            }
            
            if (loInactive) {
                // TH0: Lô bị xóa mềm → REACTIVATE
                System.out.println("   ➤ TH0: Reactivate lô đã bị xóa");
                daoLo.reactivateLoThuoc(maLo);
                loActive = true;
            }
            
            if (ctltInactive) {
                // TH0: CTLT bị xóa mềm → REACTIVATE và cập nhật số lượng
                System.out.println(" TH0: Reactivate CTLT đã bị xóa");
                ChiTietLoThuoc ctltOld = daoCTLT.getChiTietLoThuocByIdIncludeInactive(maLo, maThuoc);
                ctltOld.setIsActive(true);
                ctltOld.setSoLuong(ctltOld.getSoLuong());
                ctltOld.setNgaySanXuat(ctltNhap.getNgaySanXuat()); // Cập nhật thông tin mới
                ctltOld.setHanSuDung(ctltNhap.getHanSuDung());
                ctltOld.setGiaNhap(ctltNhap.getGiaNhap());
                daoCTLT.update(ctltOld);
                ctltActive = true;
            }
            
            if (thuocActive && ctltActive) {
                // TH1: Có thuốc & lô chứa thuốc -> chỉ cập nhật số lượng tổng trong Thuoc
            	
//            	
      
                ChiTietLoThuoc ctltHienTai = daoCTLT.getChiTietLoThuocById(maLo, maThuoc);
            	ctltHienTai.setSoLuong(ctltHienTai.getSoLuong()+thuocNhap.getSoLuongTon());
            	System.out.println(ctltHienTai);
            	daoCTLT.update(ctltHienTai);
            } else if (thuocActive && !ctltActive && loActive) {
                // TH3: Có thuốc, có lô, nhưng lô chưa chứa thuốc -> thêm ChiTietLoThuoc
                daoCTLT.create(ctltNhap);
                
                
            } else if (thuocActive && !loActive) {
                // TH2 (ứng với "có thuốc nhưng chưa có lô") -> tạo LoThuoc mới rồi thêm ChiTietLoThuoc
                LoThuoc newLo = new LoThuoc(maLo, thuocNhap.getMaNSX(), true); 
                daoLo.addLoThuoc(newLo);
                daoCTLT.create(ctltNhap);
                
            } else if (!thuocActive && loActive) {
                // TH4: Thuốc mới, lô đã tồn tại -> thêm Thuoc rồi thêm ChiTietLoThuoc
                daoThuoc.addThuoc(thuocNhap);
                daoCTLT.create(ctltNhap);
            } else {
                // TH5: Thuốc mới & lô mới -> thêm cả 2 + chi tiết lô
                LoThuoc newLo = new LoThuoc(maLo, thuocNhap.getMaNSX(), true);
                daoLo.addLoThuoc(newLo);
                daoThuoc.addThuoc(thuocNhap);
                daoCTLT.create(ctltNhap);
            }
            if (!thuocActive) {
                dsThuocCoSan.add(thuocNhap);
            }
            if (!loActive) {
                dsLoThuocCoSan.add(new LoThuoc(maLo, thuocNhap.getMaNSX(), true));
            }
            if (!ctltActive) {
                dsCTLTCoSan.add(ctltNhap);
            }
        }

        return true;
    }

    /**
     * Updates an existing drug's information in the database.
     * @param thuoc The Thuoc object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateThuoc(Thuoc thuoc) {
        String sql = "UPDATE Thuoc SET tenThuoc = ?, soLuongTon = ?, giaBan = ?, donVi = ?, soLuongToiThieu = ?, maNSX = ?, isActive = ? WHERE maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, thuoc.getTenThuoc());
            stmt.setInt(2, thuoc.getSoLuongTon());
            stmt.setString(4, thuoc.getDonVi());
            stmt.setDouble(3, thuoc.getGiaBan());
            stmt.setInt(5, thuoc.getSoLuongToiThieu());
            stmt.setString(6, thuoc.getMaNSX());
            stmt.setBoolean(7, thuoc.isIsActive()); // Use the provided getter
            stmt.setString(8, thuoc.getMaThuoc());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * Deletes a drug from the database by setting its isActive flag to false (soft delete).
     * @param id The ID of the drug to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteThuoc(String id) {
        String sql = "UPDATE Thuoc SET isActive = 0 WHERE maThuoc = ?";
        int n = 0;
        ChiTietLoThuocDAO ctltDao = new ChiTietLoThuocDAO();
        ctltDao.softDelete(id);
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    
    
    public ArrayList<Thuoc> getAllInactiveThuoc() {
        ArrayList<Thuoc> dsThuoc = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc WHERE isActive = 0";
        Connection con = ConnectDB.getConnection();
        
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Thuoc thuoc = new Thuoc(
                    rs.getString("maThuoc"),
                    rs.getString("tenThuoc"),
                    rs.getInt("soLuongTon"),
                    rs.getDouble("giaBan"),
                    rs.getString("donVi"),
                    rs.getInt("soLuongToiThieu"),
                    rs.getString("maNSX"),
                    rs.getBoolean("isActive")
                );
                dsThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsThuoc;
    }

    /**
     * Kích hoạt lại thuốc đã bị xóa mềm
     */
    public boolean reactivateThuoc(String maThuoc) {
        String sql = "UPDATE Thuoc SET isActive = 1 WHERE maThuoc = ?";
        Connection con = ConnectDB.getConnection();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maThuoc);
            int n = stmt.executeUpdate();
            System.out.println("   → Reactivated Thuoc: " + maThuoc + " (rows: " + n + ")");
            return n > 0;
        } catch (SQLException e) {
            System.err.println("   ❌ Error reactivating Thuoc: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<ThongKeThuoc> thongKeThuocTheoNgay(Date ngay, int topN) {
        ArrayList<ThongKeThuoc> dsThongKe = new ArrayList<>();
        
        String sql = "SELECT " +
            "t.maThuoc, " +
            "t.tenThuoc, " +
            "t.donVi, " +
            "SUM(ct.soLuong) AS soLuongBan, " +
            "COUNT(DISTINCT ct.maHoaDon) AS soLanXuatHien, " +
            "(SELECT COUNT(*) FROM HoaDon WHERE CAST(ngayBan AS DATE) = ? AND isActive = 1) AS tongSoHoaDon, " +
            "SUM(ct.soLuong * ct.donGia) AS doanhThu, " +
            "AVG(ct.donGia) AS giaTriTrungBinh " +
            "FROM Thuoc t " +
            "INNER JOIN ChiTietHoaDon ct ON t.maThuoc = ct.maThuoc " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE CAST(hd.ngayBan AS DATE) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1 " +
            "GROUP BY t.maThuoc, t.tenThuoc, t.donVi " +
            "ORDER BY soLuongBan DESC";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDate(1, ngay);
            stmt.setDate(2, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                dsThongKe = mapResultSetToThongKeThuoc(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Tính toán thứ hạng và tỷ lệ đóng góp
        calculateRankAndPercentage(dsThongKe);
        
        // Lấy top N nếu cần
        if (topN > 0 && dsThongKe.size() > topN) {
            return new ArrayList<>(dsThongKe.subList(0, topN));
        }
        
        return dsThongKe;
    }
    
    /**
     * Thống kê thuốc bán chạy theo tháng
     * @param thang Tháng (1-12)
     * @param nam Năm
     * @param topN Số lượng top cần lấy (0 = lấy tất cả)
     * @return ArrayList<ThongKeThuoc>
     */
    public ArrayList<ThongKeThuoc> thongKeThuocTheoThang(int thang, int nam, int topN) {
        ArrayList<ThongKeThuoc> dsThongKe = new ArrayList<>();
        
        String sql = "SELECT " +
            "t.maThuoc, " +
            "t.tenThuoc, " +
            "t.donVi, " +
            "SUM(ct.soLuong) AS soLuongBan, " +
            "COUNT(DISTINCT ct.maHoaDon) AS soLanXuatHien, " +
            "(SELECT COUNT(*) FROM HoaDon WHERE MONTH(ngayBan) = ? AND YEAR(ngayBan) = ? AND isActive = 1) AS tongSoHoaDon, " +
            "SUM(ct.soLuong * ct.donGia) AS doanhThu, " +
            "AVG(ct.donGia) AS giaTriTrungBinh " +
            "FROM Thuoc t " +
            "INNER JOIN ChiTietHoaDon ct ON t.maThuoc = ct.maThuoc " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE MONTH(hd.ngayBan) = ? AND YEAR(hd.ngayBan) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1 " +
            "GROUP BY t.maThuoc, t.tenThuoc, t.donVi " +
            "ORDER BY soLuongBan DESC";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            stmt.setInt(3, thang);
            stmt.setInt(4, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                dsThongKe = mapResultSetToThongKeThuoc(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Tính toán thứ hạng và tỷ lệ đóng góp
        calculateRankAndPercentage(dsThongKe);
        
        // Lấy top N nếu cần
        if (topN > 0 && dsThongKe.size() > topN) {
            return new ArrayList<>(dsThongKe.subList(0, topN));
        }
        
        return dsThongKe;
    }
    
    /**
     * Thống kê thuốc bán chạy theo năm
     * @param nam Năm
     * @param topN Số lượng top cần lấy (0 = lấy tất cả)
     * @return ArrayList<ThongKeThuoc>
     */
    public ArrayList<ThongKeThuoc> thongKeThuocTheoNam(int nam, int topN) {
        ArrayList<ThongKeThuoc> dsThongKe = new ArrayList<>();
        
        String sql = "SELECT " +
            "t.maThuoc, " +
            "t.tenThuoc, " +
            "t.donVi, " +
            "SUM(ct.soLuong) AS soLuongBan, " +
            "COUNT(DISTINCT ct.maHoaDon) AS soLanXuatHien, " +
            "(SELECT COUNT(*) FROM HoaDon WHERE YEAR(ngayBan) = ? AND isActive = 1) AS tongSoHoaDon, " +
            "SUM(ct.soLuong * ct.donGia) AS doanhThu, " +
            "AVG(ct.donGia) AS giaTriTrungBinh " +
            "FROM Thuoc t " +
            "INNER JOIN ChiTietHoaDon ct ON t.maThuoc = ct.maThuoc " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE YEAR(hd.ngayBan) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1 " +
            "GROUP BY t.maThuoc, t.tenThuoc, t.donVi " +
            "ORDER BY soLuongBan DESC";
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, nam);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                dsThongKe = mapResultSetToThongKeThuoc(rs);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        // Tính toán thứ hạng và tỷ lệ đóng góp
        calculateRankAndPercentage(dsThongKe);
        
        // Lấy top N nếu cần
        if (topN > 0 && dsThongKe.size() > topN) {
            return new ArrayList<>(dsThongKe.subList(0, topN));
        }
        
        return dsThongKe;
    }
    
    
    
    /**
     * Lấy tổng số lượng thuốc bán theo tháng
     * @param thang Tháng (1-12)
     * @param nam Năm
     * @return Tổng số lượng
     */
   
    
    
    
    /**
     * Lấy tổng doanh thu từ thuốc theo ngày
     * @param ngay Ngày cần thống kê
     * @return Tổng doanh thu
     */
    public double getTongDoanhThuThuocTheoNgay(Date ngay) {
        String sql = "SELECT SUM(ct.soLuong * ct.donGia) AS tongDoanhThu " +
            "FROM ChiTietHoaDon ct " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE CAST(hd.ngayBan AS DATE) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1";
        
        double tongDoanhThu = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setDate(1, ngay);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongDoanhThu = rs.getDouble("tongDoanhThu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tongDoanhThu;
    }
    
    /**
     * Lấy tổng doanh thu từ thuốc theo tháng
     * @param thang Tháng (1-12)
     * @param nam Năm
     * @return Tổng doanh thu
     */
    public double getTongDoanhThuThuocTheoThang(int thang, int nam) {
        String sql = "SELECT SUM(ct.soLuong * ct.donGia) AS tongDoanhThu " +
            "FROM ChiTietHoaDon ct " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE MONTH(hd.ngayBan) = ? AND YEAR(hd.ngayBan) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1";
        
        double tongDoanhThu = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, thang);
            stmt.setInt(2, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongDoanhThu = rs.getDouble("tongDoanhThu");
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        return tongDoanhThu;
    }
    
    /**
     * Lấy tổng doanh thu từ thuốc theo năm
     * @param nam Năm
     * @return Tổng doanh thu
     */
    public double getTongDoanhThuThuocTheoNam(int nam) {
        String sql = "SELECT SUM(ct.soLuong * ct.donGia) AS tongDoanhThu " +
            "FROM ChiTietHoaDon ct " +
            "INNER JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon " +
            "WHERE YEAR(hd.ngayBan) = ? " +
            "AND hd.isActive = 1 AND ct.isActive = 1";
        
        double tongDoanhThu = 0;
        
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, nam);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tongDoanhThu = rs.getDouble("tongDoanhThu");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tongDoanhThu;
    }
    
   
    
    
    
    // --- HELPER METHODS ---
    
    /**
     * Map ResultSet sang ArrayList<ThongKeThuoc>
     */
    private ArrayList<ThongKeThuoc> mapResultSetToThongKeThuoc(ResultSet rs) throws SQLException {
        ArrayList<ThongKeThuoc> list = new ArrayList<>();
        
        while (rs.next()) {
            String maThuoc = rs.getString("maThuoc");
            String tenThuoc = rs.getString("tenThuoc");
            String donViTinh = rs.getString("donVi");
            int soLuongBan = rs.getInt("soLuongBan");
            int soLanXuatHien = rs.getInt("soLanXuatHien");
            int tongSoHoaDon = rs.getInt("tongSoHoaDon");
            double doanhThu = rs.getDouble("doanhThu");
            double giaTriTrungBinh = rs.getDouble("giaTriTrungBinh");
            
            ThongKeThuoc tk = new ThongKeThuoc(
                maThuoc, tenThuoc, donViTinh,
                soLuongBan, soLanXuatHien, tongSoHoaDon,
                doanhThu, giaTriTrungBinh,
                0, 0  // tyLeDongGop và thuHang sẽ tính sau
            );
            
            list.add(tk);
        }
        
        return list;
    }
    
    /**
     * Tính thứ hạng và tỷ lệ đóng góp cho danh sách
     */
    private void calculateRankAndPercentage(ArrayList<ThongKeThuoc> dsThongKe) {
        if (dsThongKe.isEmpty()) return;
        
        // Tính tổng số lượng bán
        int tongSoLuongBan = dsThongKe.stream()
            .mapToInt(ThongKeThuoc::getSoLuongBan)
            .sum();
     // Gán thứ hạng và tỷ lệ đóng góp
        for (int i = 0; i < dsThongKe.size(); i++) {
            ThongKeThuoc tk = dsThongKe.get(i);
            
            // Gán thứ hạng
            tk.setThuHang(i + 1);
            
            // Tính tỷ lệ đóng góp
            if (tongSoLuongBan > 0) {
                double tyLe = (tk.getSoLuongBan() * 100.0) / tongSoLuongBan;
                tk.setTyLeDongGop(tyLe);
            }
        }
    }
        
        
}