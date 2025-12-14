package app.Entity;

public class ChiTietBangGia {
    private String maBangGia;
    private String maThuoc;
    private double donGia;
    private String donVi;
    private boolean isActive;

    public ChiTietBangGia() {
    }

    public ChiTietBangGia(String maBangGia, String maThuoc, double donGia, String donVi, boolean isActive) {
        this.maBangGia = maBangGia;
        this.maThuoc = maThuoc;
        this.donGia = donGia;
        this.donVi = donVi;
        this.isActive = isActive;
    }

    public String getMaBangGia() {
        return maBangGia;
    }

    public void setMaBangGia(String maBangGia) {
        this.maBangGia = maBangGia;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ChiTietBangGia{" +
                "maBangGia='" + maBangGia + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", donGia=" + donGia +
                ", donVi='" + donVi + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
