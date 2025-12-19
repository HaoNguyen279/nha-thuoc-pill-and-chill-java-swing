package app.Entity;

import java.util.Date;

public class BangGia {
    private String maBangGia;
    private String tenBangGia;
    private String loaiGia;
    private Date ngayApDung;
    private Date ngayKetThuc;
    private String trangThai;
    private String ghiChu;
    private int doUuTien;
    private boolean isActive;

    public BangGia() {
    }

    public BangGia(String maBangGia, String tenBangGia, String loaiGia, Date ngayApDung, Date ngayKetThuc, String trangThai, String ghiChu, int doUuTien, boolean isActive) {
        this.maBangGia = maBangGia;
        this.tenBangGia = tenBangGia;
        this.loaiGia = loaiGia;
        this.ngayApDung = ngayApDung;
        this.ngayKetThuc = ngayKetThuc;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.doUuTien = doUuTien;
        this.isActive = isActive;
    }


	public String getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(String maBangGia) {
        this.maBangGia = maBangGia;
    }

    public String getTenBangGia() {
        return tenBangGia;
    }

    public void setTenBangGia(String tenBangGia) {
        this.tenBangGia = tenBangGia;
    }

    public String getLoaiGia() {
        return loaiGia;
    }

    public void setLoaiGia(String loaiGia) {
        this.loaiGia = loaiGia;
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getDoUuTien() {
        return doUuTien;
    }

    public void setDoUuTien(int doUuTien) {
        this.doUuTien = doUuTien;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "BangGia{" +
                "maBangGia='" + maBangGia + '\'' +
                ", tenBangGia='" + tenBangGia + '\'' +
                ", loaiGia='" + loaiGia + '\'' +
                ", ngayApDung=" + ngayApDung +
                ", ngayKetThuc=" + ngayKetThuc +
                ", trangThai='" + trangThai + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", doUuTien=" + doUuTien +
                ", isActive=" + isActive +
                '}';
    }
}
