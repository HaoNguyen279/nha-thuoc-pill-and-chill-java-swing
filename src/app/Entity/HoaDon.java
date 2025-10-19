package app.Entity;

import java.util.Date;

public class HoaDon {
    private String maHoaDon;
    private Date ngayBan;
    private String ghiChu;
    private String maNV;
    private String maKH;
    private String maKM;
    // Đã thay thế maThue bằng giaTriThue (float/double) và tenLoaiThue (String)
    private double giaTriThue;
    private String tenLoaiThue;
    private boolean isActive; // true = hóa đơn có hiệu lực, false = đã hủy/ẩn

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, Date ngayBan, String ghiChu, String maNV, String maKH, String maKM, double giaTriThue, String tenLoaiThue, boolean isActive) {
        this.maHoaDon = maHoaDon;
        this.ngayBan = ngayBan;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.giaTriThue = giaTriThue;
        this.tenLoaiThue = tenLoaiThue;
        this.isActive = isActive;
    }

    // --- Getters and Setters ---

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Date getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(Date ngayBan) {
        this.ngayBan = ngayBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }
    
    // Getters/Setters mới cho Thuế

    public double getGiaTriThue() {
        return giaTriThue;
    }

    public void setGiaTriThue(double giaTriThue) {
        this.giaTriThue = giaTriThue;
    }

    public String getTenLoaiThue() {
        return tenLoaiThue;
    }

    public void setTenLoaiThue(String tenLoaiThue) {
        this.tenLoaiThue = tenLoaiThue;
    }
    
    // Getters/Setters cũ (Giữ nguyên)

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem hóa đơn có còn hiệu lực (chưa bị hủy/xóa) hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu hóa đơn là đã hủy/ẩn */
    public void markAsCancelled() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", ngayBan=" + ngayBan +
                ", ghiChu='" + ghiChu + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maKH='" + maKH + '\'' +
                ", maKM='" + maKM + '\'' +
                ", giaTriThue=" + giaTriThue + // Đã cập nhật
                ", tenLoaiThue='" + tenLoaiThue + '\'' + // Đã cập nhật
                ", isActive=" + isActive +
                '}';
    }
}
