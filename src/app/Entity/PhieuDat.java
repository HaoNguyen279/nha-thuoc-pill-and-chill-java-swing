package app.Entity;
import java.util.Date;

public class PhieuDat {
    private String maPhieuDat;
    private String maNV;
    private Date ngayDat;
    private String maKH;
    private String ghiChu; 
    private boolean isActive; // true = đơn đặt hàng đang có hiệu lực, false = đã hủy/ẩn
    private boolean isReceived; // true = đã nhận hàng, false = chưa nhận

    public PhieuDat() {
    }

    // Constructor cũ để tương thích ngược (isReceived mặc định là false)
    public PhieuDat(String maPhieuDat, String maNV, Date ngayDat, String maKH, String ghiChu, boolean isActive) {
        this.maPhieuDat = maPhieuDat;
        this.maNV = maNV;
        this.ngayDat = ngayDat;
        this.maKH = maKH;
        this.ghiChu = ghiChu; 
        this.isActive = isActive;
        this.isReceived = false; // Mặc định chưa nhận
    }

    // Constructor đã được cập nhật
    public PhieuDat(String maPhieuDat, String maNV, Date ngayDat, String maKH, String ghiChu, boolean isActive, boolean isReceived) {
        this.maPhieuDat = maPhieuDat;
        this.maNV = maNV;
        this.ngayDat = ngayDat;
        this.maKH = maKH;
        this.ghiChu = ghiChu; 
        this.isActive = isActive;
        this.isReceived = isReceived;
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

    // --- Getter/Setter cho ghiChu ---
    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    // -------------------------------

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean isReceived) {
        this.isReceived = isReceived;
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

    // toString() đã được cập nhật
    @Override
    public String toString() {
        return "PhieuDat{" +
                "maPhieuDat='" + maPhieuDat + '\'' +
                ", maNV='" + maNV + '\'' +
                ", ngayDat=" + ngayDat +
                ", maKH='" + maKH + '\'' +
                ", ghiChu='" + ghiChu + '\'' + 
                ", isActive=" + isActive +
                ", isReceived=" + isReceived +
                '}';
    }
}