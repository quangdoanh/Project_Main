package BUS;

import DAO.KhachHangDAO;
import DAO.NhanVienDAO;
import DTO.OBJECTS.KhachHangDTO;
import DTO.OBJECTS.NhanVienDTO;

import java.util.ArrayList;

public class NhanVienBUS {

    private NhanVienDAO nvDAO = new NhanVienDAO();
    public ArrayList<NhanVienDTO> listNhanVien = new ArrayList<>();

    public NhanVienBUS() {
        listNhanVien = nvDAO.selectAll();
    }

    public ArrayList<NhanVienDTO> getAll() {
        return this.listNhanVien;
    }

    // Trả về vị trí có makhachhang do
    public int getIndexByMaDV(int manhanVien) {

        int vitri = -1;
        for(int i = 0 ; i < this.listNhanVien.size() ; i ++){
            if(listNhanVien.get(i).getManv() == manhanVien && vitri == -1){
                vitri = i;
            }

        }
        return vitri;
    }

    public Boolean add(NhanVienDTO nv) {
        boolean check = nvDAO.insert(nv) != 0;
        if (check) {
            System.out.println("ma nv new" + nv.getManv());
            this.listNhanVien.add(nvDAO.selectById(nv.getManv()));
            System.out.println("Nv new" + nvDAO.selectById(nv.getManv()));
        }
        return check;
    }

    public Boolean delete(NhanVienDTO nv) {
        boolean check = nvDAO.delete(nv.getManv()) != 0;
        if (check) {
            this.listNhanVien.remove(getIndexByMaDV(nv.getManv()));
        }
        return check;
    }

    public Boolean update(NhanVienDTO nv) {
        boolean check = nvDAO.update(nv) != 0;
        if (check) {
            this.listNhanVien.set(getIndexByMaDV(nv.getManv()), nvDAO.selectById(nv.getManv()));
        }
        return check;
    }


    public ArrayList<NhanVienDTO> search(String text, String type) {
        text = text.toLowerCase();
        ArrayList<NhanVienDTO> result = new ArrayList<>();
        switch (type) {
            case "Tất cả" -> {
                for (NhanVienDTO i : this.listNhanVien) {
                    if (i.getHoten().toLowerCase().contains(text) || i.getEmail().toLowerCase().contains(text)
                            || i.getSdt().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Họ tên" -> {
                for (NhanVienDTO i : this.listNhanVien) {
                    if (i.getHoten().toLowerCase().contains(text)) {
                        result.add(i);
                    }
                }
            }
            case "Email" -> {
                for (NhanVienDTO i : this.listNhanVien) {
                    if (i.getEmail().toLowerCase().contains(text)
                    ) {
                        result.add(i);
                    }
                }
            }
            case "Mã nhân viên" -> {
                for (NhanVienDTO i : this.listNhanVien) {
                    if ((Integer.toString(i.getManv()).toLowerCase().contains(text))
                    ) {
                        result.add(i);
                    }
                }
            }
            default ->
                    throw new AssertionError();
        }

        return result;
    }
}
