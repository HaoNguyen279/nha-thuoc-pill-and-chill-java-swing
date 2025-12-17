package app.Entity;

public class DonVi {
	private String maDonVi;
	private String tenDonVi;
	private boolean isActive;
	
	public DonVi(String maDonVi, String tenDonVi, boolean isActive) {
		super();
		this.maDonVi = maDonVi;
		this.tenDonVi = tenDonVi;
		this.isActive = isActive;
	}

	public String getMaDonVi() {
		return maDonVi;
	}

	public String getTenDonVi() {
		return tenDonVi;
	}

	public void setTenDonVi(String tenDonVi) {
		this.tenDonVi = tenDonVi;
	}
	

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "DonVi [maDonVi=" + maDonVi + ", tenDonVi=" + tenDonVi + "]";
	}
	
	
	
}
