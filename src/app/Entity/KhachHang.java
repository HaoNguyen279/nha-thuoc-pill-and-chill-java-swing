package app.Entity;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String soDienThoai;
    private int diemTichLuy;
    private boolean isActive; // true = đang hoạt động/hiển thị, false = đã xóa/ẩn

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String soDienThoai, int diemTichLuy, boolean isActive) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
        this.diemTichLuy = diemTichLuy;
        this.isActive = isActive;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem khách hàng có đang hoạt động/hiển thị hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu khách hàng là đã ẩn/xóa */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diemTichLuy=" + diemTichLuy +
                ", isActive=" + isActive +
                '}';
    }
}