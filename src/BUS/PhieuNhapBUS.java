package BUS;

import DAO.ChiTietPhieuNhapDAO;
import DAO.ChiTietSanPhamDAO;
import DAO.PhieuNhapDAO;
import DTO.ChiTietPhieuDTO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhieuNhapDTO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PhieuNhapBUS {

    public final PhieuNhapDAO phieunhapDAO = new PhieuNhapDAO();
    public final ChiTietPhieuNhapDAO ctPhieuNhapDAO = new ChiTietPhieuNhapDAO();
    public final ChiTietSanPhamDAO chitietsanphamDAO = new ChiTietSanPhamDAO();

    NhaCungCapBUS nccBUS = new NhaCungCapBUS();
    NhanVienBUS nvBUS = new NhanVienBUS();

    ArrayList<PhieuNhapDTO> listPhieuNhap;

    public PhieuNhapBUS() {
    }

    public ArrayList<PhieuNhapDTO> getAll() {
        this.listPhieuNhap = phieunhapDAO.selectAll();
        return this.listPhieuNhap;
    }

    public ArrayList<PhieuNhapDTO> getAllList() {
        return this.listPhieuNhap;
    }

    public ArrayList<ChiTietSanPhamDTO> convertHashMapToArray(HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> chitietsanpham) {
        ArrayList<ChiTietSanPhamDTO> result = new ArrayList<>();
        for (ArrayList<ChiTietSanPhamDTO> ctsp : chitietsanpham.values()) {
            result.addAll(ctsp);
        }
        return result;
    }

    public ArrayList<ChiTietPhieuDTO> getChiTietPhieu_Type(int maphieunhap) {
        ArrayList<ChiTietPhieuDTO> arr = ctPhieuNhapDAO.selectAll(Integer.toString(maphieunhap));
        ArrayList<ChiTietPhieuDTO> result = new ArrayList<>();
        for (ChiTietPhieuDTO i : arr) {
            result.add(i);
        }
        return result;
    }

    public boolean add(PhieuNhapDTO phieu, ArrayList<ChiTietPhieuDTO> ctPhieu) {
        boolean check = phieunhapDAO.insert(phieu) != 0;
        if (check) {
            check = ctPhieuNhapDAO.insert(ctPhieu) != 0;
        }
        return check;
    }

    public ChiTietPhieuDTO findCT(ArrayList<ChiTietPhieuDTO> ctphieu, int mapb) {
        ChiTietPhieuDTO p = null;
        int i = 0;
        while (i < ctphieu.size() && p == null) {
            if (ctphieu.get(i).getMaphienbansp() == mapb) {
                p = ctphieu.get(i);
            } else {
                i++;
            }
        }
        return p;
    }

    public long getTongTien(ArrayList<ChiTietPhieuDTO> ctphieu) {
        long result = 0;
        for (ChiTietPhieuDTO item : ctphieu) {
            result += item.getDongia() * item.getSoluong();
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> fillerPhieuNhap(int type, String input, int mancc, int manv, Date time_s, Date time_e, String price_minnn, String price_maxxx) {
        // Xử lý ngoại lệ cho giá trị tiền
        Long price_min = 0L;
        Long price_max = Long.MAX_VALUE;
        try {
            price_min = !price_minnn.trim().isEmpty() ? Long.valueOf(price_minnn.trim()) : 0L;
            price_max = !price_maxxx.trim().isEmpty() ? Long.valueOf(price_maxxx.trim()) : Long.MAX_VALUE;
        } catch (NumberFormatException ex) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu lỗi
        }

        // Xử lý thời gian
        Timestamp time_start = new Timestamp(time_s.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time_e.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59); // Bao gồm toàn bộ ngày
        calendar.set(Calendar.MILLISECOND, 999);
        Timestamp time_end = new Timestamp(calendar.getTimeInMillis());

        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        for (PhieuNhapDTO phieuNhap : getAllList()) {
            boolean match = false;
            String inputLower = input != null ? input.trim().toLowerCase() : ""; // Chuyển input thành chữ thường
            switch (type) {
                case 0 -> { // Tất cả
                    if (Integer.toString(phieuNhap.getMaphieu()).contains(inputLower)
                            || nccBUS.getTenNhaCungCap(phieuNhap.getManhacungcap()).toLowerCase().contains(inputLower)
                            || nvBUS.getNameById(phieuNhap.getManguoitao()).toLowerCase().contains(inputLower)) {
                        match = true;
                    }
                }
                case 1 -> { // Mã phiếu nhập
                    if (Integer.toString(phieuNhap.getMaphieu()).contains(inputLower)) {
                        match = true;
                    }
                }
                case 2 -> { // Nhà cung cấp
                    if (nccBUS.getTenNhaCungCap(phieuNhap.getManhacungcap()).toLowerCase().contains(inputLower)) {
                        match = true;
                    }
                }
                case 3 -> { // Nhân viên nhập
                    if (nvBUS.getNameById(phieuNhap.getManguoitao()).toLowerCase().contains(inputLower)) {
                        match = true;
                    }
                }
            }

            if (match
                    && (manv == 0 || phieuNhap.getManguoitao() == manv)
                    && (mancc == 0 || phieuNhap.getManhacungcap() == mancc)
                    && (phieuNhap.getThoigiantao().compareTo(time_start) >= 0)
                    && (phieuNhap.getThoigiantao().compareTo(time_end) <= 0)
                    && phieuNhap.getTongTien() >= price_min
                    && phieuNhap.getTongTien() <= price_max) {
                result.add(phieuNhap);
            }
        }
        return result;
    }

    public boolean checkCancelPn(int maphieu) {
        return phieunhapDAO.checkCancelPn(maphieu);
    }

    public int cancelPhieuNhap(int maphieu) {
        return phieunhapDAO.cancelPhieuNhap(maphieu);
    }

}
