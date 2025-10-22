package app.GUI;

import java.util.ArrayList;

/**
 * Interface định nghĩa callback khi lập hóa đơn thành công
 * Để chuyển thông tin từ XacNhanLapHoaDonFrame về LapHoaDonPanel
 */
public interface HoaDonCallback {
    
    /**
     * Được gọi khi lập hóa đơn thành công
     * @param dsChiTiet Danh sách chi tiết các sản phẩm đã bán, mỗi phần tử là một mảng Object[]
     *                  bao gồm: [maThuoc, tenThuoc, soLuongDaBan, donGia]
     * @param maHoaDon Mã hóa đơn đã lập
     */
    void onHoaDonSuccess(ArrayList<Object[]> dsChiTiet, String maHoaDon);
}