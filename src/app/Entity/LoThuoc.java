package app.Entity;

public class LoThuoc {
    private String maLo;
    private String maNSX;
    private boolean isActive; // true = đang hiển thị, false = đã ẩn

    public LoThuoc() {
    }

    public LoThuoc(String maLo, String maNSX, boolean isActive) {
        this.maLo = maLo;
        this.maNSX = maNSX;
        this.isActive = isActive;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getMaNSX() {
        return maNSX;
    }

    public void setMaNSX(String maNSX) {
        this.maNSX = maNSX;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem lô thuốc có đang hiển thị (chưa bị ẩn) hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu lô thuốc là đã ẩn/xóa */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "LoThuoc{" +
                "maLo='" + maLo + '\'' +
                ", maNSX='" + maNSX + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}