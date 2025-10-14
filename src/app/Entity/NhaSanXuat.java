package app.Entity;


public class NhaSanXuat {
    private String maNSX;
    private String tenNSX;
    private String diaChi;
    private String soDienThoai;
    private boolean isActive; // true = đang hoạt động/hiển thị, false = đã ẩn/ngừng hợp tác

    public NhaSanXuat() {
    }

    public NhaSanXuat(String maNSX, String tenNSX, String diaChi, String soDienThoai, boolean isActive) {
        this.maNSX = maNSX;
        this.tenNSX = tenNSX;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.isActive = isActive;
    }

    public String getMaNSX() {
        return maNSX;
    }

    public void setMaNSX(String maNSX) {
        this.maNSX = maNSX;
    }

    public String getTenNSX() {
        return tenNSX;
    }

    public void setTenNSX(String tenNSX) {
        this.tenNSX = tenNSX;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem nhà sản xuất có đang hoạt động/hiển thị hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu nhà sản xuất là đã ẩn (ngừng hợp tác) */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "NhaSanXuat{" +
                "maNSX='" + maNSX + '\'' +
                ", tenNSX='" + tenNSX + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}