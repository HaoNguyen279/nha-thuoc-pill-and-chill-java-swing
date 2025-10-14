package app.Entity;

import java.util.Date;

/**
 * Lớp ChiTietLoThuoc đại diện cho một chi tiết cụ thể của một lô thuốc trong cơ sở dữ liệu.
 * Nó là bảng nối giữa Thuốc và Lô Thuốc.
 */
public class ChiTietLoThuoc {
    private String maLo;
    private String maThuoc;
    private Date ngaySanXuat;
    private Date hanSuDung;
    private boolean isActive; // Trạng thái của lô thuốc trong chi tiết này

    /**
     * Hàm dựng mặc định.
     */
    public ChiTietLoThuoc() {
    }

    /**
     * Hàm dựng đầy đủ các tham số.
     * @param maLo Mã lô thuốc.
     * @param maThuoc Mã thuốc.
     * @param ngaySanXuat Ngày sản xuất.
     * @param hanSuDung Hạn sử dụng.
     * @param isActive Trạng thái hoạt động.
     */
    public ChiTietLoThuoc(String maLo, String maThuoc, Date ngaySanXuat, Date hanSuDung, boolean isActive) {
        this.maLo = maLo;
        this.maThuoc = maThuoc;
        this.ngaySanXuat = ngaySanXuat;
        this.hanSuDung = hanSuDung;
        this.isActive = isActive;
    }

    // --- GETTERS AND SETTERS ---

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

    // --- toString METHOD ---

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
