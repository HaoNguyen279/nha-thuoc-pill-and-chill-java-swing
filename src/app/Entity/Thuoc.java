package app.Entity;
public class Thuoc {
    private String maThuoc;
    private String maLo;
    private String tenThuoc;
    private int soLuongTon;
    private double giaBan;
    private String donVi;
    private int soLuongToiThieu;
    private String maNSX;
    public Thuoc() {
    	
    }

    

    public Thuoc(String maThuoc, String maLo, String tenThuoc, int soLuongTon, double giaBan, String donVi,
			int soLuongToiThieu, String maNSX) {
		super();
		this.maThuoc = maThuoc;
		this.maLo = maLo;
		this.tenThuoc = tenThuoc;
		this.soLuongTon = soLuongTon;
		this.giaBan = giaBan;
		this.donVi = donVi;
		this.soLuongToiThieu = soLuongToiThieu;
		this.maNSX = maNSX;
	}



	public String getMaThuoc() {
		return maThuoc;
	}



	public void setMaThuoc(String maThuoc) {
		this.maThuoc = maThuoc;
	}



	public String getMaLo() {
		return maLo;
	}



	public void setMaLo(String maLo) {
		this.maLo = maLo;
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



	@Override
    public String toString() {
        return "Thuoc{" +
                "maThuoc='" + maThuoc + '\'' +
                ", maLo='" + maLo + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", soLuongTon=" + soLuongTon +
                '}';
    }
}