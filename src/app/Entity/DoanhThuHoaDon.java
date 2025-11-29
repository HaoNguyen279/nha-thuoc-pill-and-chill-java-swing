package app.Entity;

public class DoanhThuHoaDon {
	int ngay;
	double doanhThu;
	public DoanhThuHoaDon(int ngay,	double doanhThu) {
		super();
		this.ngay = ngay;
		this.doanhThu = doanhThu;
	}
	public DoanhThuHoaDon() {
		super();

	}
	public int getNgay() {
		return ngay;
	}
	public void setNgay(int ngay) {
		this.ngay = ngay;
	}
	public double getDoanhThu() {
		return doanhThu;
	}
	public void setDoanhThu(double doanhThu) {
		this.doanhThu = doanhThu;
	}
}
