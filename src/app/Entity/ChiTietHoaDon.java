package app.Entity;

public class ChiTietHoaDon {
    private String maHoaDon;
    private String maThuoc;
    private String maLo; // <-- ĐÃ THÊM
    private int soLuong;
    private float donGia;
    private boolean isActive; // true = chi tiết đang hiển thị, false = đã hủy/ẩn

    public ChiTietHoaDon() {
    }

    // Constructor đã được cập nhật
    public ChiTietHoaDon(String maHoaDon, String maThuoc, String maLo, int soLuong, float donGia, boolean isActive) {
        this.maHoaDon = maHoaDon;
        this.maThuoc = maThuoc;
        this.maLo = maLo;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.isActive = isActive;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
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

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getDonGia() {
        return donGia;
    }

    public void setDonGia(float donGia) {
        this.donGia = donGia;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chi tiết hóa đơn có còn hiệu lực hay không */
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
        return "ChiTietHoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", maLo='" + maLo + '\'' + // <-- ĐÃ THÊM
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", isActive=" + isActive +
                '}';
    }
}