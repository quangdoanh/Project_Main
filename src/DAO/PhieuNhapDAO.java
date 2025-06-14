package DAO;

import DTO.ChiTietPhieuDTO;
import DTO.ChiTietSanPhamDTO;
import DTO.PhieuNhapDTO;
import config.ketnoiDTB;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PhieuNhapDAO implements DAOinterface<PhieuNhapDTO> {

    public static PhieuNhapDAO getInstance() {
        return new PhieuNhapDAO();
    }


    @Override
    public int insert(PhieuNhapDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "INSERT INTO `phieunhap`(`maphieunhap`, `thoigian`, `manhacungcap`, `nguoitao`, `tongtien`) VALUES (?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMaphieu());
            pst.setTimestamp(2, t.getThoigiantao());
            pst.setInt(3, t.getManhacungcap());
            pst.setInt(4, t.getManguoitao());
            pst.setDouble(5, t.getTongTien());
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(PhieuNhapDTO t) {
        int result = 0;
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "UPDATE `phieunhap` SET `thoigian`=?,`manhacungcap`=?,`tongtien`=?,`trangthai`=? WHERE `maphieunhap`=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setTimestamp(1, t.getThoigiantao());
            pst.setInt(2, t.getManhacungcap());
            pst.setLong(3, t.getTongTien());
            pst.setInt(4, t.getTrangthai());
            pst.setInt(5, t.getMaphieu());
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "UPDATE phieunhap SET trangthai = 0 WHERE maphieunhap = ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<PhieuNhapDTO> selectAll() {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "SELECT * FROM phieunhap ORDER BY maphieunhap DESC";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("maphieunhap");
                Timestamp thoigiantao = rs.getTimestamp("thoigian");
                int mancc = rs.getInt("manhacungcap");
                int nguoitao = rs.getInt("nguoitao");
                long tongtien = rs.getLong("tongtien");
                int trangthai = rs.getInt("trangthai");
                PhieuNhapDTO phieunhap = new PhieuNhapDTO(mancc, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
                result.add(phieunhap);
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    @Override
    public PhieuNhapDTO selectById(String t) {
        PhieuNhapDTO result = null;
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "SELECT * FROM phieunhap WHERE maphieunhap=?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("maphieunhap");
                Timestamp thoigiantao = rs.getTimestamp("thoigian");
                int mancc = rs.getInt("manhacungcap");
                int nguoitao = rs.getInt("nguoitao");
                long tongtien = rs.getLong("tongtien");
                int trangthai = rs.getInt("trangthai");
                result = new PhieuNhapDTO(mancc, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
            }
            ketnoiDTB.closeConnection(con);
        } catch (Exception e) {
        }
        return result;
    }

    public ArrayList<PhieuNhapDTO> statistical(long min, long max) {
        ArrayList<PhieuNhapDTO> result = new ArrayList<>();
        try {
            Connection con = (Connection) ketnoiDTB.getConnection();
            String sql = "SELECT * FROM phieunhap WHERE tongtien BETWEEN ? AND ?";
            PreparedStatement pst = (PreparedStatement) con.prepareStatement(sql);
            pst.setLong(1, min);
            pst.setLong(2,max);

            ResultSet rs = (ResultSet) pst.executeQuery();
            while (rs.next()) {
                int maphieu = rs.getInt("maphieunhap");
                Timestamp thoigiantao = rs.getTimestamp("thoigian");
                int mancc = rs.getInt("manhacungcap");
                int nguoitao = rs.getInt("nguoitao");
                long tongtien = rs.getLong("tongtien");
                int trangthai = rs.getInt("trangthai");
                PhieuNhapDTO phieunhap = new PhieuNhapDTO(mancc, maphieu, nguoitao, thoigiantao, tongtien, trangthai);
                result.add(phieunhap);
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException e) {
        }
        return result;
    }

    public boolean checkCancelPn(int maphieu) {
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT trangthai FROM phieunhap WHERE maphieunhap = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int trangthai = rs.getInt("trangthai");
                return trangthai == 1;
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException e) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public int cancelPhieuNhap(int maphieu) {
        int result = 0;
        // Cập nhật số lượng tồn kho
        ArrayList<ChiTietPhieuDTO> arrCt = ChiTietPhieuNhapDAO.getInstance().selectAll(Integer.toString(maphieu));
        for (ChiTietPhieuDTO chiTietPhieuNhapDTO : arrCt) {
            PhienBanSanPhamDAO.getInstance().updateSoLuongTon(chiTietPhieuNhapDTO.getMaphienbansp(), -chiTietPhieuNhapDTO.getSoluong());
        }
        // Xóa chi tiết phiếu
        ChiTietPhieuNhapDAO.getInstance().delete(Integer.toString(maphieu));
        // Xóa phiếu nhập
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "DELETE FROM phieunhap WHERE maphieunhap = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, maphieu);
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'phieunhap'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery();
            if (rs2.next()) {
                result = rs2.getInt("AUTO_INCREMENT");
            } else {
                Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, "Không tìm thấy bảng phieunhap trong schema quanlikhohang.");
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(PhieuNhapDAO.class.getName()).log(Level.SEVERE, "Lỗi khi lấy AUTO_INCREMENT: " + ex.getMessage(), ex);
        }
        return result;
    }

}
