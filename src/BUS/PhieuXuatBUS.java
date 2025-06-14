package BUS;

import DAO.ChiTietPhieuXuatDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuXuatDAO;
import DTO.ChiTietPhieuDTO;
import DTO.PhieuXuatDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PhieuXuatBUS {

    private final PhieuXuatDAO phieuXuatDAO = PhieuXuatDAO.getInstance();
    private final ChiTietPhieuXuatDAO chiTietPhieuXuatDAO = ChiTietPhieuXuatDAO.getInstance();
    private final ChiTietSanPhamDAO chiTietSanPhamDAO = ChiTietSanPhamDAO.getInstance();
    private final ArrayList<PhieuXuatDTO> listPhieuXuat;

    NhanVienBUS nvBUS = new NhanVienBUS();
    KhachHangBUS khBUS = new KhachHangBUS();

    public PhieuXuatBUS() {
        this.listPhieuXuat = phieuXuatDAO.selectAll();
    }

    public ArrayList<PhieuXuatDTO> getAll() {
        return this.listPhieuXuat;
    }

    public PhieuXuatDTO getSelect(int index) {
        return listPhieuXuat.get(index);
    }

    public void cancel(int px) {
        phieuXuatDAO.cancel(px);
    }

    public void remove(int px) {
        listPhieuXuat.remove(px);
    }

    public void insert(PhieuXuatDTO px, ArrayList<ChiTietPhieuDTO> ct) {
        phieuXuatDAO.insert(px);
        chiTietPhieuXuatDAO.insert(ct);
    }

    public ArrayList<ChiTietPhieuDTO> selectCTP(int maphieu) {
        return chiTietPhieuXuatDAO.selectAll(Integer.toString(maphieu));
    }

    public ArrayList<PhieuXuatDTO> fillerPhieuXuat(int type, String input, int makh, int manv, Date time_s, Date time_e, String price_minnn, String price_maxxx) {
        String searchInput = (input == null || input.trim().isEmpty()) ? "" : input.trim().toLowerCase();
        // Xử lý giá tiền
        Long price_min = 0L;
        Long price_max = Long.MAX_VALUE;
        try {
            price_min = price_minnn.isEmpty() ? 0L : Long.valueOf(price_minnn);
            price_max = price_maxxx.isEmpty() ? Long.MAX_VALUE : Long.valueOf(price_maxxx);
        } catch (NumberFormatException e) {
            price_min = 0L;
            price_max = Long.MAX_VALUE;
        }

        // Xử lý thời gian
        Timestamp time_start = new Timestamp(time_s != null ? time_s.getTime() : 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time_e != null ? time_e : new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Timestamp time_end = new Timestamp(calendar.getTimeInMillis());

        ArrayList<PhieuXuatDTO> result = new ArrayList<>();
        for (PhieuXuatDTO phieuXuat : getAll()) {
            boolean match = false;

            // Xử lý tìm kiếm linh hoạt hơn
            String maPhieuStr = Integer.toString(phieuXuat.getMaphieu());
            String tenKhachHang = khBUS.getTenKhachHang(phieuXuat.getMakh()).toLowerCase();
            String tenNhanVien = nvBUS.getNameById(phieuXuat.getManguoitao()).toLowerCase();

            if (searchInput.isEmpty()) {
                match = true; // Nếu không có từ khóa, lấy tất cả
            } else {
                switch (type) {
                    case 0: // Tất cả
                        if (maPhieuStr.contains(searchInput) || tenKhachHang.contains(searchInput) || tenNhanVien.contains(searchInput)) {
                            match = true;
                        }
                        break;
                    case 1: // Mã phiếu
                        if (maPhieuStr.contains(searchInput)) {
                            match = true;
                        }
                        break;
                    case 2: // Khách hàng
                        if (tenKhachHang.contains(searchInput)) {
                            match = true;
                        }
                        break;
                    case 3: // Nhân viên
                        if (tenNhanVien.contains(searchInput)) {
                            match = true;
                        }
                        break;
                }
            }


            // Điều kiện lọc
            if (match &&
                    (manv == 0 || phieuXuat.getManguoitao() == manv) &&
                    (makh == 0 || phieuXuat.getMakh() == makh) &&
                    (phieuXuat.getThoigiantao().compareTo(time_start) >= 0) &&
                    (phieuXuat.getThoigiantao().compareTo(time_end) <= 0) &&
                    (phieuXuat.getTongTien() >= price_min) &&
                    (phieuXuat.getTongTien() <= price_max)) {
                result.add(phieuXuat);
            }
        }

        return result;
    }
}