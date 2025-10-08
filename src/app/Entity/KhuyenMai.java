package app.Entity;

import java.util.Date;

public class KhuyenMai {
    private String maKM;
    private float mucGiamGia; // Đã đổi tên từ phanTramGia để phù hợp với SQL script
    private Date ngayApDung;
    private Date ngayKetThuc;
    private boolean isActive; // true = đang hiển thị/áp dụng, false = đã xóa/ẩn

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, float mucGiamGia, Date ngayApDung, Date ngayKetThuc, boolean isActive) {
        this.maKM = maKM;
        this.mucGiamGia = mucGiamGia;
        this.ngayApDung = ngayApDung;
        this.ngayKetThuc = ngayKetThuc;
        this.isActive = isActive;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public float getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(float mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
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
    
    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // --- Phương thức tiện ích ---
    
    /** Kiểm tra xem khuyến mãi có đang được áp dụng và hiển thị hay không */
    public boolean isVisible() {
        return this.isActive;
    }
    
    /** Đánh dấu khuyến mãi là đã xóa/ẩn */
    public void markAsDeleted() {
        this.isActive = false;
    }

    /** Kiểm tra khuyến mãi còn hiệu lực theo ngày hiện tại */
    public boolean isHieuLuc() {
        Date now = new Date();
        // Kiểm tra isActive trước
        if (!this.isActive) return false;
        
        // Kiểm tra hiệu lực theo ngày
        if (ngayApDung == null) return false;
        
        // So sánh ngày (chỉ là ước lượng, cần xử lý múi giờ và thời gian cụ thể hơn trong ứng dụng thực tế)
        return !now.before(ngayApDung) && !now.after(ngayKetThuc);
    }


    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", mucGiamGia=" + mucGiamGia +
                ", ngayApDung=" + ngayApDung +
                ", ngayKetThuc=" + ngayKetThuc +
                ", isActive=" + isActive +
                '}';
    }
}