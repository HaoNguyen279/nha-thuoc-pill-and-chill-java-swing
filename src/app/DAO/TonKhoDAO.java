package app.DAO;

import app.ConnectDB.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Lớp DAO xử lý các thao tác liên quan đến tồn kho thuốc
 * Bao gồm cập nhật số lượng khi bán thuốc, nhập thuốc, đổi trả, ...
 */
public class TonKhoDAO {
    
    /**
     * Cập nhật số lượng trong kho sau khi bán thuốc
     * @param dsChiTiet danh sách chi tiết thuốc [maThuoc, tenThuoc, soLuong, donGia]
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean capNhatTonKhoSauKhiBan(ArrayList<Object[]> dsChiTiet) {
        Connection con = null;
        try {
            // Sử dụng getConnection để đảm bảo dùng cùng connection pool
            con = ConnectDB.getConnection();
            
            // Bắt đầu transaction
            con.setAutoCommit(false);
            
            // Sử dụng phương pháp trực tiếp hơn: Lấy tất cả chi tiết lô thuốc
            String sqlGetChiTietLo = "SELECT maLo, maThuoc, soLuong FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1 AND soLuong > 0 ORDER BY ngaySanXuat";
            PreparedStatement stmtGetChiTietLo = con.prepareStatement(sqlGetChiTietLo);
            
            // Truy vấn cập nhật trực tiếp theo maLo và maThuoc (khóa chính tổng hợp)
            String sqlCapNhatLo = "UPDATE ChiTietLoThuoc SET soLuong = ? WHERE maLo = ? AND maThuoc = ?";
            PreparedStatement stmtCapNhatLo = con.prepareStatement(sqlCapNhatLo);
            
            // Truy vấn cập nhật số lượng tổng trong bảng Thuoc - phần này sẽ thực hiện riêng sau khi cập nhật các lô
            
            for (Object[] item : dsChiTiet) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1]; // Để hiển thị thông tin
                int soLuongMua = (Integer) item[2];
                int soLuongCanMua = soLuongMua; // Biến theo dõi số lượng còn cần mua
                
                // Lấy danh sách các lô thuốc
                stmtGetChiTietLo.setString(1, maThuoc);
                ResultSet rs = stmtGetChiTietLo.executeQuery();
                
                boolean duSoLuong = false;
                
                // Duyệt qua các lô thuốc để trừ dần số lượng
                while (rs.next() && soLuongCanMua > 0) {
                    String maLo = rs.getString("maLo");
                    String maThuocLo = rs.getString("maThuoc");
                    int soLuongTonLo = rs.getInt("soLuong");
                    
                    if (soLuongTonLo > 0) {
                        // Xác định số lượng cần trừ từ lô này
                        int soLuongTru = Math.min(soLuongCanMua, soLuongTonLo);
                        int soLuongConLai = soLuongTonLo - soLuongTru;
                        
                        // Cập nhật lô
                        stmtCapNhatLo.setInt(1, soLuongConLai);
                        stmtCapNhatLo.setString(2, maLo);
                        stmtCapNhatLo.setString(3, maThuocLo);
                        
                        int rowsUpdated = stmtCapNhatLo.executeUpdate();
                        if (rowsUpdated > 0) {
                            soLuongCanMua -= soLuongTru;
                            
                            if (soLuongCanMua == 0) {
                                duSoLuong = true;
                                break; // Đã đủ số lượng cần mua
                            }
                        }
                    }
                }
                
                rs.close();
                
                // Kiểm tra nếu không đủ số lượng sau khi đã duyệt qua tất cả các lô
                if (!duSoLuong) {
                    con.rollback();
                    JOptionPane.showMessageDialog(null,
                        "Không đủ số lượng thuốc " + tenThuoc + " (mã " + maThuoc + ") trong kho!\n" +
                        "Còn thiếu " + soLuongCanMua + " đơn vị để hoàn tất giao dịch.",
                        "Cảnh báo tồn kho",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            
            // Đóng statements trước khi thực hiện đồng bộ
            stmtGetChiTietLo.close();
            stmtCapNhatLo.close();
            
            // Cập nhật trực tiếp bảng Thuoc cho từng mã thuốc
            for (Object[] item : dsChiTiet) {
                String maThuoc = (String) item[0];
                String tenThuoc = (String) item[1];
                
                // Đồng bộ số lượng từ ChiTietLoThuoc sang Thuoc
                String sqlFixThuoc = "UPDATE Thuoc SET soLuongTon = (SELECT COALESCE(SUM(soLuong), 0) FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1) WHERE maThuoc = ?";
                PreparedStatement stmtFix = con.prepareStatement(sqlFixThuoc);
                stmtFix.setString(1, maThuoc);
                stmtFix.setString(2, maThuoc);
                stmtFix.executeUpdate();
                
                // Kiểm tra lại số lượng tồn kho sau khi cập nhật
                PreparedStatement stmtCheck = con.prepareStatement("SELECT soLuongTon FROM Thuoc WHERE maThuoc = ?");
                stmtCheck.setString(1, maThuoc);
                ResultSet rsCheck = stmtCheck.executeQuery();
                if (rsCheck.next()) {
                    // Đọc số lượng tồn mới để đảm bảo đã cập nhật
                    rsCheck.getInt("soLuongTon");
                }
                rsCheck.close();
                stmtCheck.close();
                stmtFix.close();
            }
            
            // Commit nếu tất cả cập nhật đều thành công
            con.commit();
            
            return true;
            
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                    System.err.println("Đã rollback cập nhật tồn kho do lỗi: " + e.getMessage());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi cập nhật số lượng tồn kho: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            // Khôi phục auto-commit và đóng connection
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Kiểm tra số lượng tồn kho có đủ để bán hay không
     * @param dsChiTiet danh sách chi tiết thuốc [maThuoc, tenThuoc, soLuong, donGia]
     * @return true nếu đủ số lượng, false nếu không đủ
     */
    public boolean kiemTraDuSoLuong(ArrayList<Object[]> dsChiTiet) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            
            for (Object[] item : dsChiTiet) {
                String maThuoc = (String) item[0];
                int soLuongCanMua = (Integer) item[2];
                
                String sql = "SELECT SUM(soLuong) AS tongSoLuong FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, maThuoc);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int tongSoLuong = rs.getInt("tongSoLuong");
                    if (tongSoLuong < soLuongCanMua) {
                        String tenThuoc = (String) item[1];
                        
                        JOptionPane.showMessageDialog(null,
                            "Không đủ số lượng thuốc " + tenThuoc + " (mã " + maThuoc + ") trong kho!\n" +
                            "Cần: " + soLuongCanMua + " đơn vị, Có: " + tongSoLuong + " đơn vị.",
                            "Cảnh báo tồn kho",
                            JOptionPane.WARNING_MESSAGE);
                            
                        rs.close();
                        stmt.close();
                        return false;
                    }
                }
                
                rs.close();
                stmt.close();
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra số lượng tồn kho của một thuốc cụ thể
     * @param maThuoc mã thuốc cần kiểm tra
     * @return số lượng tồn kho của thuốc đó, hoặc 0 nếu không tìm thấy hoặc có lỗi
     */
    public int getSoLuongTonKho(String maThuoc) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            
            // Kiểm tra trong bảng Thuoc
            String sql = "SELECT soLuongTon FROM Thuoc WHERE maThuoc = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int soLuong = rs.getInt("soLuongTon");
                rs.close();
                stmt.close();
                return soLuong;
            }
            
            rs.close();
            stmt.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Đồng bộ số lượng tồn kho từ ChiTietLoThuoc sang Thuoc
     * Phương thức này sẽ tính tổng số lượng trong các lô thuốc và cập nhật vào bảng Thuoc
     * @param maThuoc mã thuốc cần đồng bộ, nếu null sẽ đồng bộ tất cả các thuốc
     * @return true nếu đồng bộ thành công, false nếu có lỗi
     */
    public boolean dongBoSoLuongTon(String maThuoc) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);
            
            if (maThuoc != null) {
                // Đồng bộ cho một thuốc cụ thể - dùng cú pháp tương thích với SQL Server
                String sql = "UPDATE Thuoc SET soLuongTon = (SELECT COALESCE(SUM(soLuong), 0) FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1) WHERE maThuoc = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, maThuoc);
                stmt.setString(2, maThuoc);
                stmt.executeUpdate();
                stmt.close();
                

                
                // Kiểm tra kết quả đồng bộ
                PreparedStatement checkStmt = con.prepareStatement("SELECT soLuongTon FROM Thuoc WHERE maThuoc = ?");
                checkStmt.setString(1, maThuoc);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {


                }
                rs.close();
                checkStmt.close();
            } else {
                // Đồng bộ cho tất cả thuốc - sửa lại cú pháp SQL cho đúng với SQL Server
                String sql = "UPDATE Thuoc SET soLuongTon = (SELECT COALESCE(SUM(c.soLuong), 0) FROM ChiTietLoThuoc c WHERE c.maThuoc = Thuoc.maThuoc AND c.isActive = 1)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.executeUpdate();

                stmt.close();
            }
            
            con.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
                System.err.println("Lỗi SQL khi đồng bộ số lượng tồn: " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Cập nhật số lượng trong kho khi nhập thuốc
     * @param maLo mã lô thuốc
     * @param maThuoc mã thuốc
     * @param soLuongNhap số lượng nhập thêm
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean capNhatTonKhoSauKhiNhap(String maLo, String maThuoc, int soLuongNhap) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);
            
            // Cập nhật số lượng trong ChiTietLoThuoc
            String sqlCapNhatLo = "UPDATE ChiTietLoThuoc SET soLuong = soLuong + ? WHERE maLo = ? AND maThuoc = ?";
            PreparedStatement stmtCapNhatLo = con.prepareStatement(sqlCapNhatLo);
            stmtCapNhatLo.setInt(1, soLuongNhap);
            stmtCapNhatLo.setString(2, maLo);
            stmtCapNhatLo.setString(3, maThuoc);
            stmtCapNhatLo.executeUpdate();
            
            // Cập nhật số lượng tổng trong bảng Thuoc
            String sqlCapNhatThuoc = "UPDATE Thuoc SET soLuongTon = soLuongTon + ? WHERE maThuoc = ?";
            PreparedStatement stmtCapNhatThuoc = con.prepareStatement(sqlCapNhatThuoc);
            stmtCapNhatThuoc.setInt(1, soLuongNhap);
            stmtCapNhatThuoc.setString(2, maThuoc);
            stmtCapNhatThuoc.executeUpdate();
            
            con.commit();
            
            stmtCapNhatLo.close();
            stmtCapNhatThuoc.close();
            
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Cập nhật số lượng tồn kho sau khi đặt thuốc
     * Trừ số lượng tồn kho vì nhà thuốc sẽ chừa ra thuốc ngay khi có phiếu đặt
     * @param dsChiTiet danh sách chi tiết thuốc [maThuoc, tenThuoc, soLuong, donGia, thanhTien, maLo]
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean capNhatTonKhoSauKhiDat(ArrayList<Object[]> dsChiTiet) {
        Connection con = null;
        try {
            // Sử dụng getConnection để đảm bảo dùng cùng connection pool
            con = ConnectDB.getConnection();
            
            // Bắt đầu transaction
            con.setAutoCommit(false);
            
            // Sử dụng phương pháp trực tiếp hơn: Lấy tất cả chi tiết lô thuốc
            String sqlGetChiTietLo = "SELECT maLo, maThuoc, soLuong FROM ChiTietLoThuoc WHERE maThuoc = ? AND isActive = 1 AND soLuong > 0 ORDER BY ngaySanXuat";
            PreparedStatement stmtGetChiTietLo = con.prepareStatement(sqlGetChiTietLo);
            
            // Truy vấn cập nhật trực tiếp theo maLo và maThuoc (khóa chính tổng hợp)
            String sqlCapNhatLo = "UPDATE ChiTietLoThuoc SET soLuong = ? WHERE maLo = ? AND maThuoc = ?";
            PreparedStatement stmtCapNhatLo = con.prepareStatement(sqlCapNhatLo);
            
            for (Object[] item : dsChiTiet) {
                String maThuoc = (String) item[0];
                int soLuongDat = (Integer) item[2];
                int soLuongCanDat = soLuongDat; // Biến theo dõi số lượng còn cần đặt
                
                // Lấy danh sách các lô thuốc
                stmtGetChiTietLo.setString(1, maThuoc);
                ResultSet rs = stmtGetChiTietLo.executeQuery();
                
                boolean duSoLuong = false;
                
                while (rs.next() && soLuongCanDat > 0) {
                    String maLo = rs.getString("maLo");
                    int soLuongTon = rs.getInt("soLuong");
                    
                    int soLuongTru = Math.min(soLuongCanDat, soLuongTon);
                    int soLuongConLai = soLuongTon - soLuongTru;
                    
                    // Cập nhật số lượng trong lô
                    stmtCapNhatLo.setInt(1, soLuongConLai);
                    stmtCapNhatLo.setString(2, maLo);
                    stmtCapNhatLo.setString(3, maThuoc);
                    stmtCapNhatLo.executeUpdate();
                    
                    soLuongCanDat -= soLuongTru;
                    
                    if (soLuongCanDat == 0) {
                        duSoLuong = true;
                        break;
                    }
                }
                
                rs.close();
                
                if (!duSoLuong) {
                    // Rollback nếu không đủ số lượng
                    con.rollback();
                    JOptionPane.showMessageDialog(null,
                        "Không đủ số lượng tồn kho cho thuốc: " + maThuoc + "\n" +
                        "Số lượng cần: " + soLuongDat + ", còn thiếu: " + soLuongCanDat,
                        "Không đủ tồn kho",
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            stmtGetChiTietLo.close();
            stmtCapNhatLo.close();
            
            con.commit();
            return true;
            
        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Tính toán số lượng tồn kho đã được reserved (đặt trước) cho một thuốc
     * @param maThuoc mã thuốc cần kiểm tra
     * @return số lượng đã được đặt trước
     */
    public int getSoLuongReserved(String maThuoc) {
        Connection con = null;
        try {
            con = ConnectDB.getConnection();
            
            // Tính tổng số lượng đã đặt trước từ các phiếu đặt chưa nhận hàng
            String sql = "SELECT SUM(ctpd.soLuong) as tongSoLuongDat " +
                        "FROM ChiTietPhieuDat ctpd " +
                        "INNER JOIN PhieuDat pd ON ctpd.maPhieuDat = pd.maPhieuDat " +
                        "WHERE ctpd.maThuoc = ? AND ctpd.isActive = 1 AND pd.isActive = 1 AND pd.isReceived = 0";
                        
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int soLuongReserved = rs.getInt("tongSoLuongDat");
                rs.close();
                stmt.close();
                return soLuongReserved;
            }
            
            rs.close();
            stmt.close();
            return 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Tính toán số lượng tồn kho khả dụng (Available Stock = On Hand - Reserved)
     * @param maThuoc mã thuốc cần kiểm tra
     * @return số lượng khả dụng để bán
     */
    public int getSoLuongAvailable(String maThuoc) {
        int onHand = getSoLuongTonKho(maThuoc);
        int reserved = getSoLuongReserved(maThuoc);
        return Math.max(0, onHand - reserved);
    }

    /**
     * Kiểm tra số lượng tồn kho có đủ để bán hay không (xét đến số lượng đã đặt trước)
     * @param dsChiTiet danh sách chi tiết thuốc [maThuoc, tenThuoc, soLuong, donGia]
     * @return true nếu đủ số lượng, false nếu không đủ
     */
    public boolean kiemTraDuSoLuongAvailable(ArrayList<Object[]> dsChiTiet) {
        for (Object[] item : dsChiTiet) {
            String maThuoc = (String) item[0];
            String tenThuoc = (String) item[1];
            int soLuongCanMua = (Integer) item[2];
            
            // Lấy số lượng tồn thực tế (On Hand)
            int onHand = getSoLuongTonKho(maThuoc);
            
            // Lấy số lượng đã đặt trước (Reserved)
            int reserved = getSoLuongReserved(maThuoc);
            
            // Tính số lượng khả dụng (Available)
            int available = Math.max(0, onHand - reserved);
            
            if (available < soLuongCanMua) {
                // Hiển thị thông báo chi tiết về tồn kho
                String message = "Không đủ số lượng thuốc " + tenThuoc + " (mã " + maThuoc + ") khả dụng!\n\n" +
                               "📦 Tồn kho thực tế (On Hand): " + onHand + " viên\n" +
                               "🔒 Đã đặt trước (Reserved): " + reserved + " viên\n" +
                               "✅ Khả dụng để bán (Available): " + available + " viên\n\n" +
                               "❌ Cần: " + soLuongCanMua + " viên\n" +
                               "💡 Đề xuất: Chỉ có thể bán tối đa " + available + " viên";
                
                if (reserved > 0) {
                    message += "\n\n⚠️ Lý do: " + reserved + " viên đã được khách khác đặt trước.";
                }
                
                javax.swing.JOptionPane.showMessageDialog(null,
                    message,
                    "Cảnh báo tồn kho",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                    
                return false;
            }
        }
        
        return true;
    }
}