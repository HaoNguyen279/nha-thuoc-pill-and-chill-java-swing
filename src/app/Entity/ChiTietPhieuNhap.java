package app.Entity;

public class ChiTietPhieuNhap {
    private String maPhieuNhapThuoc;
    private String maLo;
    private int soLuong;
    private float donGia;
    private boolean isActive; // true = chi tiết đang hiển thị, false = đã hủy/ẩn

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(String maPhieuNhapThuoc, String maLo, int soLuong, float donGia, boolean isActive) {
        this.maPhieuNhapThuoc = maPhieuNhapThuoc;
        this.maLo = maLo;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.isActive = isActive;
    }

    public String getMaPhieuNhapThuoc() {
        return maPhieuNhapThuoc;
    }

    public void setMaPhieuNhapThuoc(String maPhieuNhapThuoc) {
        this.maPhieuNhapThuoc = maPhieuNhapThuoc;
    }

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getDonGia() {
        return donGia;
    }

    public void setDonGia(float donGia) {
        this.donGia = donGia;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chi tiết phiếu nhập có còn hiệu lực hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu chi tiết là đã hủy/ẩn */
    public void markAsDeleted() {
        this.isActive = false;
    }


    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPhieuNhapThuoc='" + maPhieuNhapThuoc + '\'' +
                ", maLo='" + maLo + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", isActive=" + isActive +
                '}';
    }
}