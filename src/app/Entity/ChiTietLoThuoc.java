package app.Entity;

import java.util.Date;

public class ChiTietLoThuoc {
    private String maLo;
    private String maThuoc;
    private Date ngaySanXuat;
    private Date hanSuDung;
    private boolean isActive; // true = đang hiển thị, false = đã xóa/ẩn

    public ChiTietLoThuoc() {
    }

    public ChiTietLoThuoc(String maLo, String maThuoc, Date ngaySanXuat, Date hanSuDung, boolean isActive) {
        this.maLo = maLo;
        this.maThuoc = maThuoc;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
        this.isActive = isActive;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public Date getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(Date ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public Date getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(Date hanSuDung) {
        this.hanSuDung = hanSuDung;
    }
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chi tiết lô thuốc có đang hiển thị (chưa bị ẩn) hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu chi tiết là đã ẩn/xóa */
    public void markAsDeleted() {
        this.isActive = false;
    }


    @Override
    public String toString() {
        return "ChiTietLoThuoc{" +
                "maLo='" + maLo + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", ngaySanXuat=" + ngaySanXuat +
                ", hanSuDung=" + hanSuDung +
                ", isActive=" + isActive +
                '}';
    }
}