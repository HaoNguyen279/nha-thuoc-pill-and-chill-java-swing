package app.Entity;

/**
 * Entity class representing monthly revenue statistics
 */
public class DoanhThuTheoThang {
    private int thang;
    private int nam;
    private double doanhThu;
    private int soLuongHoaDon;

    public DoanhThuTheoThang() {
    }

    public DoanhThuTheoThang(int thang, int nam, double doanhThu, int soLuongHoaDon) {
        this.thang = thang;
        this.nam = nam;
        this.doanhThu = doanhThu;
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

    public double getDoanhThu() {
        return doanhThu;
    }

    public void setdoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
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
                ", doanhThu=" + doanhThu +
                ", soLuongHoaDon=" + soLuongHoaDon +
                '}';
    }
}