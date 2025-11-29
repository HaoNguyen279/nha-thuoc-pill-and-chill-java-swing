package app.Entity;

public class ChucVu {
    private String maChucVu;
    private String tenChucVu;
    private boolean isActive;

    public ChucVu() {
    }

    public ChucVu(String maChucVu, String tenChucVu, boolean isActive) {
        this.maChucVu = maChucVu;
        this.tenChucVu = tenChucVu;
        this.isActive = isActive;
    }

    public String getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(String maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chức vụ có đang hoạt động/hiển thị hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu chức vụ là đã ẩn/xóa */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return tenChucVu;
    }
}