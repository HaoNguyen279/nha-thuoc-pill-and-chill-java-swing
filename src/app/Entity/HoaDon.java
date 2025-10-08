package app.Entity;

import java.util.Date;

public class HoaDon {
    private String maHoaDon;
    private Date ngayBan;
    private String ghiChu;
    private String maNV;
    private String maKH;
    private String maKM;
    private String maThue;
    private boolean isActive; // true = hóa đơn có hiệu lực, false = đã hủy/ẩn

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, Date ngayBan, String ghiChu, String maNV, String maKH, String maKM, String maThue, boolean isActive) {
        this.maHoaDon = maHoaDon;
        this.ngayBan = ngayBan;
        this.ghiChu = ghiChu;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.maThue = maThue;
        this.isActive = isActive;
    }

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

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

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
                ", maThue='" + maThue + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}