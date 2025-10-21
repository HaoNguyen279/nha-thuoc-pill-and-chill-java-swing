package app.Entity;

public class ChiTietPhieuDat {
    private String maPhieuDat;
    private String maThuoc;
    private String maLo; // <-- ĐÃ THÊM
    private String tenThuoc;
    private int soLuong;
    private boolean isActive; // true = chi tiết đang hiển thị, false = đã hủy/ẩn

    public ChiTietPhieuDat() {
    }

    // Constructor đã được cập nhật
    public ChiTietPhieuDat(String maPhieuDat, String maThuoc, String maLo, String tenThuoc, int soLuong, boolean isActive) {
        this.maPhieuDat = maPhieuDat;
        this.maThuoc = maThuoc;
        this.maLo = maLo;
        this.tenThuoc = tenThuoc;
        this.soLuong = soLuong;
        this.isActive = isActive;
    }

    public String getMaPhieuDat() {
        return maPhieuDat;
    }

    public void setMaPhieuDat(String maPhieuDat) {
        this.maPhieuDat = maPhieuDat;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    // --- Getter/Setter cho maLo ---
    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }
    // -----------------------------

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chi tiết phiếu đặt có còn hiệu lực hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu chi tiết là đã hủy/ẩn */
    public void markAsDeleted() {
        this.isActive = false;
    }

    // toString() đã được cập nhật
    @Override
    public String toString() {
        return "ChiTietPhieuDat{" +
                "maPhieuDat='" + maPhieuDat + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", maLo='" + maLo + '\'' + // <-- ĐÃ THÊM
                ", tenThuoc='" + tenThuoc + '\'' +
                ", soLuong=" + soLuong +
                ", isActive=" + isActive +
                '}';
    }
}