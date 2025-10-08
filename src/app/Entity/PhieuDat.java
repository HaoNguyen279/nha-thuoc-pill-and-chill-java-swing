package app.Entity;
import java.util.Date;

public class PhieuDat {
    private String maPhieuDat;
    private String maNV;
    private Date ngayDat;
    private String maKH;
    private boolean isActive; // true = đơn đặt hàng đang có hiệu lực, false = đã hủy/ẩn

    public PhieuDat() {
    }

    public PhieuDat(String maPhieuDat, String maNV, Date ngayDat, String maKH, boolean isActive) {
        this.maPhieuDat = maPhieuDat;
        this.maNV = maNV;
        this.ngayDat = ngayDat;
        this.maKH = maKH;
        this.isActive = isActive;
    }

    public String getMaPhieuDat() {
        return maPhieuDat;
    }

    public void setMaPhieuDat(String maPhieuDat) {
        this.maPhieuDat = maPhieuDat;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Date ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem phiếu đặt có còn hoạt động hay không (chưa hủy) */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu phiếu đặt là đã hủy/ẩn đi */
    public void markAsDeleted() {
        this.isActive = false;
    }


    @Override
    public String toString() {
        return "PhieuDat{" +
                "maPhieuDat='" + maPhieuDat + '\'' +
                ", maNV='" + maNV + '\'' +
                ", ngayDat=" + ngayDat +
                ", maKH='" + maKH + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}