package BUS;

import DAO.ChiTietQuyenDAO;
import DAO.NhomQuyenDAO;
import DTO.OBJECTS.ChiTietQuyenDTO;
import DTO.OBJECTS.NhomQuyenDTO;

import java.util.ArrayList;

public class NhomQuyenBUS {

    private final NhomQuyenDAO nhomquyenDAO = new NhomQuyenDAO();
    private final ChiTietQuyenDAO chitietquyenDAO = new ChiTietQuyenDAO();
    private final ArrayList<NhomQuyenDTO> listNhomQuyen;

    public NhomQuyenBUS() {
        this.listNhomQuyen = nhomquyenDAO.selectAll();
    }

    public ArrayList<NhomQuyenDTO> getAll() {
        return this.listNhomQuyen;
    }

    public NhomQuyenDTO getByIndex(int index) {
        return this.listNhomQuyen.get(index);
    }

    public boolean add(String nqdto, ArrayList<ChiTietQuyenDTO> ctquyen) {
        NhomQuyenDTO nq = new NhomQuyenDTO(nhomquyenDAO.getAutoIncrement(), nqdto);
        boolean check = nhomquyenDAO.insert(nq) != 0;
        if (check) {
            this.listNhomQuyen.add(nq);
            this.addChiTietQuyen(ctquyen);
        }
        return check;
    }

    public boolean update(NhomQuyenDTO nhomquyen, ArrayList<ChiTietQuyenDTO> chitietquyen,int index) {
        boolean check = nhomquyenDAO.update(nhomquyen) != 0;
        if (check) {
            this.removeChiTietQuyen(Integer.toString(nhomquyen.getManhomquyen()));
            this.addChiTietQuyen(chitietquyen);
            this.listNhomQuyen.set(index, nhomquyen);
        }
        return check;
    }

    public boolean delete(NhomQuyenDTO nqdto) {
        boolean check = nhomquyenDAO.delete(nqdto.getManhomquyen()) != 0;
        if (check) {
            this.listNhomQuyen.remove(nqdto);
        }
        return check;
    }

    public ArrayList<ChiTietQuyenDTO> getChiTietQuyen(String manhomquyen) {
        return chitietquyenDAO.selectAll(manhomquyen);
    }

    public boolean addChiTietQuyen(ArrayList<ChiTietQuyenDTO> listctquyen) {
        boolean check = chitietquyenDAO.insert(listctquyen) != 0;
        return check;
    }

    public boolean removeChiTietQuyen(String manhomquyen) {
        boolean check = chitietquyenDAO.delete(manhomquyen) != 0;
        return check;
    }

    public boolean checkPermisson(int maquyen, String chucnang, String hanhdong) {
        ArrayList<ChiTietQuyenDTO> ctquyen = this.getChiTietQuyen(Integer.toString(maquyen)); // danh sach quyen thuc hien cua ma quyen( tk do)(ma quyen - chucnang -hanhdong)
        boolean check = false;
        int i = 0;
        // kiểm tra trong danhn sach quyền đó  có tồn tại chức năng đó với hành động tương tự đó không
        while (i < ctquyen.size() && check==false) {
            if(ctquyen.get(i).getMachucnang().equals(chucnang) && ctquyen.get(i).getHanhdong().equals(hanhdong)) {
                check = true;
            } else {
                i++;
            }
        }
        return check;
    }

    public ArrayList<NhomQuyenDTO> search(String text) {
        ArrayList<NhomQuyenDTO> result = new ArrayList<>();
        for(NhomQuyenDTO i : this.listNhomQuyen) {
            if(Integer.toString(i.getManhomquyen()).contains(text) || i.getTennhomquyen().toLowerCase().contains(text.toLowerCase())) {
                result.add(i);
            }
        }
        return result;
    }
}
