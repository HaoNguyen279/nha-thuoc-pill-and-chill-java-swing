package app.Entity;

public class ChiTietPhieuDoiTra {
    private String maPhieuDoiTra;
    private String maThuoc;
    private int soLuong;
    private float donGia;
    private String maLo;
    private String lyDo;
    private boolean isActive; // true = chi tiết có hiệu lực, false = đã hủy/ẩn

    public ChiTietPhieuDoiTra() {
    }

    public ChiTietPhieuDoiTra(String maPhieuDoiTra, String maThuoc, int soLuong, float donGia, String maLo, String lyDo, boolean isActive) {
        this.maPhieuDoiTra = maPhieuDoiTra;
        this.maThuoc = maThuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maLo = maLo;
        this.lyDo = lyDo;
        this.isActive = isActive;
    }

    public String getMaPhieuDoiTra() {
        return maPhieuDoiTra;
    }

    public void setMaPhieuDoiTra(String maPhieuDoiTra) {
        this.maPhieuDoiTra = maPhieuDoiTra;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
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

    public String getMaLo() {
        return maLo;
    }

    public void setMaLo(String maLo) {
        this.maLo = maLo;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem chi tiết đổi trả có đang hiển thị (chưa bị hủy) hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu chi tiết là đã hủy/ẩn */
    public void markAsDeleted() {
        this.isActive = false;
    }


    @Override
    public String toString() {
        return "ChiTietPhieuDoiTra{" +
                "maPhieuDoiTra='" + maPhieuDoiTra + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", maLo='" + maLo + '\'' +
                ", lyDo='" + lyDo + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}