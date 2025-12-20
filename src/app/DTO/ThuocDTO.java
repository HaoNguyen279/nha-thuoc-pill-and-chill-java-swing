package app.DTO;

/**
 * DTO cho Thuốc với thông tin đầy đủ bao gồm:
 * - Giá chuẩn (giá bán cơ bản)
 * - Giá hiện tại (giá bán theo bảng giá)
 * - Mã đơn vị và Tên đơn vị
 */
public class ThuocDTO {
    private String maThuoc;
    private String tenThuoc;
    private int soLuongTon;
    private double giaChuan;        // Giá bán cơ bản (giá gốc)
    private double giaHienTai;      // Giá bán hiện tại (theo bảng giá)
    private String tenDonVi;        // Tên đơn vị (để hiển thị)
    private String maDonVi;         // Mã đơn vị (để lưu database) - QUAN TRỌNG!
    private int soLuongToiThieu;
    private String maNSX;
    private boolean isActive;

    // Constructor mặc định
    public ThuocDTO() {
    }

    // Constructor đầy đủ (với maDonVi)
    public ThuocDTO(String maThuoc, String tenThuoc, int soLuongTon, 
                    double giaChuan, double giaHienTai, String tenDonVi, String maDonVi,
                    int soLuongToiThieu, String maNSX, boolean isActive) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.soLuongTon = soLuongTon;
        this.giaChuan = giaChuan;
        this.giaHienTai = giaHienTai;
        this.tenDonVi = tenDonVi;
        this.maDonVi = maDonVi;
        this.soLuongToiThieu = soLuongToiThieu;
        this.maNSX = maNSX;
        this.isActive = isActive;
    }

    // Getters and Setters
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

    public double getGiaChuan() {
        return giaChuan;
    }

    public void setGiaChuan(double giaChuan) {
        this.giaChuan = giaChuan;
    }

    public double getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(double giaHienTai) {
        this.giaHienTai = giaHienTai;
    }

    public String getTenDonVi() {
        return tenDonVi;
    }

    public void setTenDonVi(String tenDonVi) {
        this.tenDonVi = tenDonVi;
    }

    public String getMaDonVi() {
        return maDonVi;
    }

    public void setMaDonVi(String maDonVi) {
        this.maDonVi = maDonVi;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    // Phương thức tiện ích
    
    /**
     * Kiểm tra xem giá hiện tại có khác giá chuẩn không
     * @return true nếu đang có giảm giá/tăng giá
     */
    public boolean isDifferentPrice() {
        return Math.abs(giaHienTai - giaChuan) > 0.01;
    }
    
    /**
     * Tính phần trăm thay đổi giá
     * @return Phần trăm thay đổi (âm = giảm giá, dương = tăng giá)
     */
    public double getPriceChangePercent() {
        if (giaChuan == 0) return 0;
        return ((giaHienTai - giaChuan) / giaChuan) * 100;
    }
    
    /**
     * Kiểm tra xem thuốc có đang giảm giá không
     * @return true nếu giá hiện tại thấp hơn giá chuẩn
     */
    public boolean isOnSale() {
        return giaHienTai < giaChuan;
    }
    
    /**
     * Kiểm tra xem thuốc có đang tăng giá không
     * @return true nếu giá hiện tại cao hơn giá chuẩn
     */
    public boolean isPriceIncreased() {
        return giaHienTai > giaChuan;
    }
    
    /**
     * Kiểm tra xem thuốc có sắp hết hàng không
     * @return true nếu số lượng tồn <= số lượng tối thiểu
     */
    public boolean isLowStock() {
        return soLuongTon <= soLuongToiThieu;
    }
    
    /**
     * Đánh dấu thuốc là đã xóa (ẩn)
     */
    public void markAsDeleted() {
        this.isActive = false;
    }
    
    /**
     * Khôi phục thuốc đã bị ẩn
     */
    public void restore() {
        this.isActive = true;
    }

    @Override
    public String toString() {
        return "ThuocDTO {" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", soLuongTon=" + soLuongTon +
                ", giaChuan=" + giaChuan +
                ", giaHienTai=" + giaHienTai +
                ", tenDonVi='" + tenDonVi + '\'' +
                ", maDonVi='" + maDonVi + '\'' +
                ", soLuongToiThieu=" + soLuongToiThieu +
                ", maNSX='" + maNSX + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThuocDTO thuocDTO = (ThuocDTO) o;
        return maThuoc != null && maThuoc.equals(thuocDTO.maThuoc);
    }

    @Override
    public int hashCode() {
        return maThuoc != null ? maThuoc.hashCode() : 0;
    }
}