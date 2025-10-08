package app.Entity;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String chucVu;
    private String soDienThoai;
    private boolean isActive; // true = đang làm việc, false = đã nghỉ/ẩn

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String chucVu, String soDienThoai, boolean isActive) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.soDienThoai = soDienThoai;
        this.isActive = isActive;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
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
    
    /** Kiểm tra xem nhân viên có đang hoạt động hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu nhân viên là đã nghỉ/ẩn */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", chucVu='" + chucVu + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}