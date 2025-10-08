package app.Entity;

import java.util.Date;

public class PhieuNhapThuoc {
    private String maPhieuNhapThuoc;
    private String maNV;
    private Date ngayNhap;
    private boolean isActive; // true = đang hiển thị, false = đã hủy/ẩn

    public PhieuNhapThuoc() {
    }

    public PhieuNhapThuoc(String maPhieuNhapThuoc, String maNV, Date ngayNhap, boolean isActive) {
        this.maPhieuNhapThuoc = maPhieuNhapThuoc;
        this.maNV = maNV;
        this.ngayNhap = ngayNhap;
        this.isActive = isActive;
    }

    public String getMaPhieuNhapThuoc() {
        return maPhieuNhapThuoc;
    }

    public void setMaPhieuNhapThuoc(String maPhieuNhapThuoc) {
        this.maPhieuNhapThuoc = maPhieuNhapThuoc;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Utility methods ---
    
    /** Checks if the import note is active (not cancelled/deleted) */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Marks the import note as deleted/cancelled (hidden) */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "PhieuNhapThuoc{" +
                "maPhieuNhapThuoc='" + maPhieuNhapThuoc + '\'' +
                ", maNV='" + maNV + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", isActive=" + isActive +
                '}';
    }
}