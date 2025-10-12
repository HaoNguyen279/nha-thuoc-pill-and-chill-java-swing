package app.Entity;
public class KhachHang {
    private String maKH;
    private String tenKH;
    private String soDienThoai;
    private int diemTichLuy;
    private String diaChi;
    private boolean isActive;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String soDienThoai, int diemTichLuy , boolean isActive) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
        this.diemTichLuy = diemTichLuy;
        this.diaChi = diaChi;
        this.isActive = isActive;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
   public boolean isIsActive() {
	   return isActive;
   }
   public void setIsActive(boolean is) {
	   this.isActive = is;
   }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diemTichLuy=" + diemTichLuy +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}