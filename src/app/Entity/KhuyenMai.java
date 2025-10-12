package app.Entity;
import java.sql.Date;

public class KhuyenMai {
    private String maKM;
    private float mucGiam;
    private Date ngayApDung;
    private Date ngayKetThuc;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, float mucGiam, Date ngayApDung, Date ngayKetThuc) {
        this.maKM = maKM;
        this.mucGiam = mucGiam;
        this.ngayApDung = ngayApDung;
        this.ngayKetThuc = ngayKetThuc;
        
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public float getMucGiam() {
        return mucGiam;
    }

    public void setMucGiam(float mucGiam) {
        this.mucGiam = mucGiam;
    }

    public Date getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(Date ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", mucGiam=" + mucGiam +
                ", ngayApDung=" + ngayApDung +
                ", ngayKetThuc=" + ngayKetThuc +
                '}';
    }
}