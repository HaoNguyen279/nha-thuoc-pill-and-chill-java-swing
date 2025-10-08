package app.Entity;

import java.util.Date;

public class Thue {
    private String maThue;
    private float mucThue;
    private Date ngayKetThuc;
    private Date ngayApDung;
    private boolean isActive; // true = đang dùng, false = đã xóa/ẩn (Đã đổi từ 'show')

    // --- Constructors ---
    public Thue() {}

    public Thue(String maThue, float mucThue, Date ngayApDung, Date ngayKetThuc, boolean isActive) {
        this.maThue = maThue;
        this.mucThue = mucThue;
        this.ngayApDung = ngayApDung;
        this.ngayKetThuc = ngayKetThuc;
        this.isActive = isActive; // Đã đổi tên
    }

    // --- Getters & Setters ---
    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public float getMucThue() {
        return mucThue;
    }

    public void setMucThue(float mucThue) {
        this.mucThue = mucThue;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Date getNgayApDung() {
        return ngayApDung;
    }

    public void setNgayApDung(Date ngayApDung) {
        this.ngayApDung = ngayApDung;
    }

    // Getter mới
    public boolean isIsActive() {
        return isActive;
    }

    // Setter mới
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    // --- Các phương thức tiện ích (Đã đổi tên và logic) ---
    /** Đánh dấu thuế đã xóa (ẩn đi, không áp dụng nữa) */
    public void markAsDeleted() {
        this.isActive = false;
    }

    /** Khôi phục thuế đã xóa */
    public void restore() {
        this.isActive = true;
    }

    /** Kiểm tra thuế còn hiệu lực theo ngày hiện tại */
    public boolean isHieuLuc() {
        Date now = new Date();
        // Kiểm tra logic isActive trước
        if (!this.isActive) return false;
        
        // Kiểm tra hiệu lực theo ngày
        if (ngayApDung == null) return false;
        
        // Nếu ngày kết thúc là null, thuế có hiệu lực vô thời hạn từ ngày áp dụng
        if (ngayKetThuc == null) return now.after(ngayApDung);
        
        // Nếu có cả ngày áp dụng và ngày kết thúc
        return now.after(ngayApDung) && now.before(ngayKetThuc);
    }

    @Override
    public String toString() {
        return "Thue {" +
                "maThue='" + maThue + '\'' +
                ", mucThue=" + mucThue +
                ", ngayApDung=" + ngayApDung +
                ", ngayKetThuc=" + ngayKetThuc +
                ", isActive=" + isActive + // Đã đổi tên
                ", hieuLuc=" + isHieuLuc() +
                '}';
    }
}