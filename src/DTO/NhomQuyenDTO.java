/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;


public class NhomQuyenDTO {
    private int manhomquyen;
    private String tennhomquyen;
    public NhomQuyenDTO() {
    }
    public NhomQuyenDTO(int manhomquyen, String tennhomquyen) {
        this.manhomquyen = manhomquyen;
        this.tennhomquyen = tennhomquyen;
    }
    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getTennhomquyen() {
        return tennhomquyen;
    }

    public void setTennhomquyen(String tennhomquyen) {
        this.tennhomquyen = tennhomquyen;
    }
}
