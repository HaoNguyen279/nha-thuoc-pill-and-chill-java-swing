package app.GUI;

import java.util.ArrayList;

/**
 * Interface định nghĩa callback khi lập phiếu đặt thuốc thành công
 * Để chuyển thông tin từ XacNhanLapPhieuDatThuocFrame về LapPhieuDatThuocPanel
 */
public interface PhieuDatCallback {
    
    /**
     * Được gọi khi lập phiếu đặt thuốc thành công
     * @param dsChiTiet Danh sách chi tiết các thuốc đã đặt, mỗi phần tử là một mảng Object[]
     *                  bao gồm: [maThuoc, tenThuoc, soLuongDaDat, donGia]
     * @param maPhieuDat Mã phiếu đặt đã lập
     */
    void onPhieuDatSuccess(ArrayList<Object[]> dsChiTiet, String maPhieuDat);
}