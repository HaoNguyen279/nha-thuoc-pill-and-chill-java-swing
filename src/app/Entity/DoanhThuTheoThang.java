package app.Entity;

/**
 * Entity class representing monthly revenue statistics
 */
public class DoanhThuTheoThang {
    private int thang;
    private int nam;
    private double tongDoanhThu;
    private int soLuongHoaDon;

    public DoanhThuTheoThang() {
    }

    public DoanhThuTheoThang(int thang, int nam, double tongDoanhThu, int soLuongHoaDon) {
        this.thang = thang;
        this.nam = nam;
        this.tongDoanhThu = tongDoanhThu;
        this.soLuongHoaDon = soLuongHoaDon;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public double getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(double tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public int getSoLuongHoaDon() {
        return soLuongHoaDon;
    }

    public void setSoLuongHoaDon(int soLuongHoaDon) {
        this.soLuongHoaDon = soLuongHoaDon;
    }

    public String getTenThang() {
        return "Tháng " + thang + "/" + nam;
    }

    @Override
    public String toString() {
        return "DoanhThuTheoThang{" +
                "thang=" + thang +
                ", nam=" + nam +
                ", tongDoanhThu=" + tongDoanhThu +
                ", soLuongHoaDon=" + soLuongHoaDon +
                '}';
    }
}