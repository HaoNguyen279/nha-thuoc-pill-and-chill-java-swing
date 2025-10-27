package app.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import app.ConnectDB.ConnectDB;
import app.Entity.ChiTietLoThuoc;
import app.Entity.LoThuoc;
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
                String maLo = rs.getString("maLo");
                String tenThuoc = rs.getString("tenThuoc");
                int soLuongTon = rs.getInt("soLuongTon");
                double giaBan = rs.getDouble("giaBan");
                String donVi = rs.getString("donVi");
                int soLuongToiThieu = rs.getInt("soLuongToiThieu");
                String maNSX = rs.getString("maNSX");
                boolean isActive = rs.getBoolean("isActive");

                Thuoc thuoc = new Thuoc(maThuoc, maLo, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive);
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
                    String maLo = rs.getString("maLo");
                    String tenThuoc = rs.getString("tenThuoc");
                    int soLuongTon = rs.getInt("soLuongTon");
                    double giaBan = rs.getDouble("giaBan");
                    String donVi = rs.getString("donVi");
                    int soLuongToiThieu = rs.getInt("soLuongToiThieu");
                    String maNSX = rs.getString("maNSX");
                    boolean isActive = rs.getBoolean("isActive");
                    
                    thuoc = new Thuoc(maThuoc, maLo, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return thuoc;
    }

    /**
     * Retrieves a single drug by its maThuoc and maLo.
     * @param maThuoc The ID of the drug to find.
     * @param maLo The batch/lot ID of the drug to find.
     * @return A Thuoc object if found, otherwise null.
     */
    public Thuoc getThuocByMaThuocAndMaLo(String maThuoc, String maLo) {
        String sql = "SELECT * FROM Thuoc WHERE maThuoc = ? AND maLo = ?";
        Thuoc thuoc = null;

        Connection con = ConnectDB.getConnection();
        if (con == null) {
            return thuoc;
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            stmt.setString(2, maLo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                thuoc = new Thuoc(
                    rs.getString("maThuoc"),
                    rs.getString("maLo"),
                    rs.getString("tenThuoc"),
                    rs.getInt("soLuongTon"),
                    rs.getDouble("giaBan"),
                    rs.getString("donVi"),
                    rs.getInt("soLuongToiThieu"),
                    rs.getString("maNSX"),
                    rs.getBoolean("isActive")
                );
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
        return thuoc;
    }

    /**
     * Adds a new drug to the database.
     * @param thuoc The Thuoc object to add.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean addThuoc(Thuoc thuoc) {
        String sql = "INSERT INTO Thuoc (maThuoc, maLo, tenThuoc, soLuongTon, giaBan, donVi, soLuongToiThieu, maNSX, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, thuoc.getMaThuoc());
            stmt.setString(2, thuoc.getMaLo());
            stmt.setString(3, thuoc.getTenThuoc());
            stmt.setInt(4, thuoc.getSoLuongTon());
            stmt.setDouble(5, thuoc.getGiaBan());
            stmt.setString(6, thuoc.getDonVi());
            stmt.setInt(7, thuoc.getSoLuongToiThieu());
            stmt.setString(8, thuoc.getMaNSX());
            stmt.setBoolean(9, thuoc.isIsActive()); // Use the provided getter
            
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

        for (int i = 0; i < dsThuocNhap.size(); i++) {
            Thuoc thuocNhap = dsThuocNhap.get(i);
            ChiTietLoThuoc ctltNhap = dsCTLT.get(i);

            String maThuoc = thuocNhap.getMaThuoc();
            String maLo = thuocNhap.getMaLo(); 
        
            boolean thuocExists = dsThuocCoSan.stream()
                    .anyMatch(t -> t.getMaThuoc().equals(maThuoc));
            boolean loExists = dsLoThuocCoSan.stream()
                    .anyMatch(l -> l.getMaLo().equals(maLo));
            boolean ctltExists = dsCTLTCoSan.stream()
                    .anyMatch(c -> c.getMaLo().equals(maLo) && c.getMaThuoc().equals(maThuoc));

            if (thuocExists && ctltExists) {
                // TH1: Có thuốc & lô chứa thuốc -> chỉ cập nhật số lượng tổng trong Thuoc
                Thuoc thuocHienTai = daoThuoc.getThuocById(maThuoc);
                thuocHienTai.setSoLuongTon(thuocHienTai.getSoLuongTon() + thuocNhap.getSoLuongTon());
                daoThuoc.updateThuoc(thuocHienTai);

            } else if (thuocExists && !ctltExists && loExists) {
                // TH3: Có thuốc, có lô, nhưng lô chưa chứa thuốc -> thêm ChiTietLoThuoc
                daoCTLT.create(ctltNhap);
      
                Thuoc thuocHienTai = daoThuoc.getThuocById(maThuoc);
                thuocHienTai.setSoLuongTon(thuocHienTai.getSoLuongTon() + ctltNhap.getSoLuong());
                daoThuoc.updateThuoc(thuocHienTai);
            } else if (thuocExists && !loExists) {
                // TH2 (ứng với "có thuốc nhưng chưa có lô") -> tạo LoThuoc mới rồi thêm ChiTietLoThuoc
                LoThuoc newLo = new LoThuoc(maLo, thuocNhap.getMaNSX(), true); 
                daoLo.addLoThuoc(newLo);
                daoCTLT.create(ctltNhap);

                
                Thuoc thuocHienTai = daoThuoc.getThuocById(maThuoc);
                thuocHienTai.setSoLuongTon(thuocHienTai.getSoLuongTon() + ctltNhap.getSoLuong());
                daoThuoc.updateThuoc(thuocHienTai);
            } else if (!thuocExists && loExists) {
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
        }

        return true;
    }

    /**
     * Updates an existing drug's information in the database.
     * @param thuoc The Thuoc object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateThuoc(Thuoc thuoc) {
        String sql = "UPDATE Thuoc SET maLo = ?, tenThuoc = ?, soLuongTon = ?, giaBan = ?, donVi = ?, soLuongToiThieu = ?, maNSX = ?, isActive = ? WHERE maThuoc = ?";
        int n = 0;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, thuoc.getMaLo());
            stmt.setString(2, thuoc.getTenThuoc());
            stmt.setInt(3, thuoc.getSoLuongTon());
            stmt.setDouble(4, thuoc.getGiaBan());
            stmt.setString(5, thuoc.getDonVi());
            stmt.setInt(6, thuoc.getSoLuongToiThieu());
            stmt.setString(7, thuoc.getMaNSX());
            stmt.setBoolean(8, thuoc.isIsActive()); // Use the provided getter
            stmt.setString(9, thuoc.getMaThuoc());
            
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
        
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}