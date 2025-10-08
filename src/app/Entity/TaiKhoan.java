package app.Entity;

public class TaiKhoan {
    private String maNV;
    private String matKhau;
    private boolean isActive; // true = đang hoạt động, false = đã bị khóa/xóa

    public TaiKhoan() {
    }

    public TaiKhoan(String maNV, String matKhau, boolean isActive) {
        this.maNV = maNV;
        this.matKhau = matKhau;
        this.isActive = isActive;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // --- Phương thức tiện ích ---
    /** Kiểm tra xem tài khoản có đang hoạt động hay không */
    public boolean isAccountActive() {
        return this.isActive;
    }
    
    /** Khóa (vô hiệu hóa) tài khoản */
    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maNV='" + maNV + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}