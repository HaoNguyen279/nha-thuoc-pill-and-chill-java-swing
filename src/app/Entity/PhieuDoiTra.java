package app.Entity;

import java.util.Date;

public class PhieuDoiTra {
    private String maPhieuDoiTra;
    private Date ngayDoiTra;
    private String maKH;
    private String maNV;
    private boolean isActive; // true = đang hiển thị, false = đã hủy/ẩn

    public PhieuDoiTra() {
    }

    public PhieuDoiTra(String maPhieuDoiTra, Date ngayDoiTra, String maKH, String maNV, boolean isActive) {
        this.maPhieuDoiTra = maPhieuDoiTra;
        this.ngayDoiTra = ngayDoiTra;
        this.maKH = maKH;
        this.maNV = maNV;
        this.isActive = isActive;
    }

    public String getMaPhieuDoiTra() {
        return maPhieuDoiTra;
    }

    public void setMaPhieuDoiTra(String maPhieuDoiTra) {
        this.maPhieuDoiTra = maPhieuDoiTra;
    }

    public Date getNgayDoiTra() {
        return ngayDoiTra;
    }

    public void setNgayDoiTra(Date ngayDoiTra) {
        this.ngayDoiTra = ngayDoiTra;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Utility methods ---
    
    /** Checks if the return/exchange note is active (not cancelled/deleted) */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Marks the return/exchange note as cancelled/deleted (hidden) */
    public void markAsDeleted() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "PhieuDoiTra{" +
                "maPhieuDoiTra='" + maPhieuDoiTra + '\'' +
                ", ngayDoiTra=" + ngayDoiTra +
                ", maKH='" + maKH + '\'' +
                ", maNV='" + maNV + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}