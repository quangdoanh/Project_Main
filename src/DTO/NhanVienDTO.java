
package DTO;

import java.util.Date;

public class NhanVienDTO {

    private int manv;
    private String hoten;
    private int gioitinh;
    private String sdt;
    private Date ngaysinh;
    private int trangthai;
    private String email;

    public NhanVienDTO() {
    }

    public NhanVienDTO(int manv, String hoten, int gioitinh, Date ngaysinh, String sdt, int trangthai, String email) {
        this.manv = manv;
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.trangthai = trangthai;
        this.email = email;
    }

    public NhanVienDTO(String hoten, int gioitinh, Date ngaysinh, String sdt, int trangthai) {
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.trangthai = trangthai;
        this.email = email;
    }

    public int getManv() {
        return manv;
    }

    public void setManv(int manv) {
        this.manv = manv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
