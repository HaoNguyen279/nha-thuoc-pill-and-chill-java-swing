package app.DTO;

public class ThuocKemGiaChuanVaGiaHienTaiDTO {
    private String maThuoc;
    private String tenThuoc;
    private String donVi;
    private double giaChuan;
    private double giaHienTai;

    // --- Constructors ---
    public ThuocKemGiaChuanVaGiaHienTaiDTO() {
    }

    public ThuocKemGiaChuanVaGiaHienTaiDTO(String maThuoc, String tenThuoc,
                 String donVi, double giaChuan, double giaHienTai) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.donVi = donVi;
        this.giaChuan = giaChuan;
        this.giaHienTai = giaHienTai;

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

	public String getDonVi() {
		return donVi;
	}

	public void setDonVi(String donVi) {
		this.donVi = donVi;
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

	@Override
	public String toString() {
		return "ThuocKemGiaChuanVaGiaHienTaiDTO [maThuoc=" + maThuoc + ", tenThuoc=" + tenThuoc + ", donVi=" + donVi
				+ ", giaChuan=" + giaChuan + ", giaHienTai=" + giaHienTai + "]";
	}
    
    
    
    
    
}