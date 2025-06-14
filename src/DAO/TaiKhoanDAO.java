package DAO;

import config.ketnoiDTB;
import helper.BcryptPassword;
import DTO.TaiKhoanDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaiKhoanDAO implements DAOinterface<TaiKhoanDTO> {

    public static TaiKhoanDAO getInstance() {
        return new TaiKhoanDAO();
    }

    @Override
    public int insert(TaiKhoanDTO t) {
        int result = 0;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "INSERT INTO `taikhoan`(`manv`,`tendangnhap`,`matkhau`,`manhomquyen`,`trangthai`) VALUES (?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getManv());
            pst.setString(2, t.getUsername());
            // Mã hóa mật khẩu trước khi lưu
            String hashedPassword = BcryptPassword.hashPassword(t.getMatkhau());
            pst.setString(3, hashedPassword);
            pst.setInt(4, t.getManhomquyen());
            pst.setInt(5, t.getTrangthai());
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int update(TaiKhoanDTO t) {
        int result = 0;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "UPDATE `taikhoan` SET `tendangnhap`=?,`trangthai`=?,`manhomquyen`=? WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getUsername());
            pst.setInt(2, t.getTrangthai());
            pst.setInt(3, t.getManhomquyen());
            pst.setInt(4, t.getManv());
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int updatePass(String email, String hashedPassword) {
        int result = 0;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "UPDATE taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv SET `matkhau`=? WHERE email=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, hashedPassword);
                pst.setString(2, email);
                result = pst.executeUpdate();
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public TaiKhoanDTO selectByEmail(String t) {
        TaiKhoanDTO tk = null;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT * FROM taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv WHERE nv.email = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau"); // Đây là mật khẩu đã mã hóa
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                tk = new TaiKhoanDTO(manv, tendangnhap, matkhau, manhomquyen, trangthai);
                return tk;
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tk;
    }

    public void sendOpt(String email, String opt) {
        int result;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "UPDATE taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv SET `otp`=? WHERE email=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, opt);
            pst.setString(2, email);
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkOtp(String email, String otp) {
        boolean check = false;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT * FROM taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv WHERE nv.email = ? AND tk.otp = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, otp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                check = true;
                return check;
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "UPDATE `taikhoan` SET `trangthai`='-1' WHERE manv = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(t));
            result = pst.executeUpdate();
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        ArrayList<TaiKhoanDTO> result = new ArrayList<>();
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE trangthai = '0' OR trangthai = '1'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manv = rs.getInt("manv");
                String username = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau"); // Đây là mật khẩu đã mã hóa
                int manhomquyen = rs.getInt("manhomquyen");
                int trangthai = rs.getInt("trangthai");
                TaiKhoanDTO tk = new TaiKhoanDTO(manv, username, matkhau, manhomquyen, trangthai);
                result.add(tk);
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public TaiKhoanDTO selectById(String t) {
        TaiKhoanDTO result = null;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE manv=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau"); // Đây là mật khẩu đã mã hóa
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                result = new TaiKhoanDTO(manv, tendangnhap, matkhau, manhomquyen, trangthai);
                return result; // Sửa lỗi: trả về result thay vì tk
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public TaiKhoanDTO selectByUser(String t) {
        TaiKhoanDTO result = null;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT * FROM taikhoan WHERE tendangnhap=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau"); // Đây là mật khẩu đã mã hóa
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                result = new TaiKhoanDTO(manv, tendangnhap, matkhau, manhomquyen, trangthai);
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = ketnoiDTB.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'taikhoan'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            ketnoiDTB.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}