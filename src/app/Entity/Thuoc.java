package app.Entity;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private int soLuongTon;
    private double giaBan;
    private String donVi;
    private int soLuongToiThieu;
    private String maNSX;
    private boolean isActive; // true = đang hiển thị, false = đã xóa (ẩn)

    // --- Constructors ---
    public Thuoc() {
    }

    public Thuoc(String maThuoc, String tenThuoc, int soLuongTon, double giaBan, 
                 String donVi, int soLuongToiThieu, String maNSX, boolean isActive) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.soLuongTon = soLuongTon;
        this.giaBan = giaBan;
        this.donVi = donVi;
        this.soLuongToiThieu = soLuongToiThieu;
        this.maNSX = maNSX;
        this.isActive = isActive;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getSoLuongToiThieu() {
        return soLuongToiThieu;
    }

    public void setSoLuongToiThieu(int soLuongToiThieu) {
        this.soLuongToiThieu = soLuongToiThieu;
    }

    public String getMaNSX() {
        return maNSX;
    }

    public void setMaNSX(String maNSX) {
        this.maNSX = maNSX;
    }

    // Getter mới cho isActive (theo chuẩn boolean getter là is...)
    public boolean isIsActive() {
        return isActive;
    }

    // Setter mới cho isActive
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // --- Phương thức tiện ích (Đã đổi tên và logic) ---
    /** Đánh dấu thuốc là đã xóa (ẩn đi) */
    public void markAsDeleted() {
        this.isActive = false;
    }

    /** Khôi phục thuốc đã bị ẩn */
    public void restore() {
        this.isActive = true;
    }

    /** Kiểm tra thuốc còn hiển thị hay không */
    public boolean isVisible() {
        return this.isActive;
    }

    @Override
    public String toString() {
        return "Thuoc {" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", soLuongTon=" + soLuongTon +
                ", giaBan=" + giaBan +
                ", donVi='" + donVi + '\'' +
                ", soLuongToiThieu=" + soLuongToiThieu +
                ", maNSX='" + maNSX + '\'' +
                ", isActive=" + isActive + // Đã đổi tên
                '}';
    }
}