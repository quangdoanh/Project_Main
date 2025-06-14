package DAO;

import config.JDBC;
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
    public int insert(TaiKhoanDTO tk) {
        int result = 0;
        String sql = "INSERT INTO taikhoan (manv,tendangnhap,matkhau,manhomquyen,trangthai) VALUES(?,?,?,?,?)";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1,tk.getManv());
            pst.setString(2,tk.getUsername());
            pst.setString(3,tk.getMatkhau());
            pst.setInt(4,tk.getManhomquyen());
            pst.setInt(5,tk.getTrangthai());
            result = pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(TaiKhoanDTO tk) {
        int result = 0;
        String sql = "UPDATE taikhoan SET tendangnhap=?,trangthai=?,manhomquyen=? WHERE manv=?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1,tk.getUsername());
            pst.setInt(2,tk.getTrangthai());
            pst.setInt(3,tk.getManhomquyen());
            pst.setInt(4,tk.getManv());

            result = pst.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public int updatePass(String email, String hashedPassword) {
        int result = 0;
        try {
            Connection con = JDBC.getConnection();
            String sql = "UPDATE taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv SET `matkhau`=? WHERE email=?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, hashedPassword);
                pst.setString(2, email);
                result = pst.executeUpdate();
            }
            JDBC.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void sendOpt(String email, String opt) {
        int result;
        try {
            Connection con = JDBC.getConnection();
            String sql = "UPDATE taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv SET `otp`=? WHERE email=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, opt);
            pst.setString(2, email);
            result = pst.executeUpdate();
            JDBC.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkOtp(String email, String otp) {
        boolean check = false;
        try {
            Connection con = JDBC.getConnection();
            String sql = "SELECT * FROM taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv WHERE nv.email = ? AND tk.otp = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, otp);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                check = true;
                return check;
            }
            JDBC.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }

    //Xoá tài khoản (đưa về ngừng hoạt động)
    @Override
    public int delete(String t) {
        int result = 0;
        String sql = "UPDATE taikhoan SET trangthai = -1 WHERE manv = ?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1,Integer.parseInt(t));
            result = pst.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    //lấy ra danh sách tất cả tài khoản
    @Override
    public ArrayList<TaiKhoanDTO> selectAll() {
        ArrayList<TaiKhoanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan WHERE trangthai = '1' OR trangthai = '0'";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int manhoquyen = rs.getInt("manhomquyen");
                int trangthai = rs.getInt("trangthai");
                TaiKhoanDTO tk = new TaiKhoanDTO(manv,tendangnhap,matkhau,manhoquyen,trangthai);
                list.add(tk);
            }

        }catch (SQLException e){
            e.printStackTrace();

        }
        return list;
    }

    //tìm tk theo id
    @Override
    public TaiKhoanDTO selectById(int t) {
        TaiKhoanDTO result = null;
        String sql = "SELECT * FROM taikhoan WHERE manv = ?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1,t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");
                result = new TaiKhoanDTO(manv,tendangnhap,matkhau,manhomquyen,trangthai);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public TaiKhoanDTO selectByUser(String t) {
        TaiKhoanDTO result = null;
        try {
            Connection con = JDBC.getConnection();
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
            JDBC.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBC.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND TABLE_NAME = 'taikhoan'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            JDBC.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoanDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    //tìm theo email
    public  TaiKhoanDTO selectByEmail(String email){
        TaiKhoanDTO tk = null;
        String sql = "SELECT * FROM taikhoan tk JOIN nhanvien nv ON tk.manv = nv.manv WHERE nv.email = ?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1,email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");

                tk =new TaiKhoanDTO(manv,tendangnhap,matkhau,manhomquyen,trangthai);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return  tk;
    }
    //Tìm theo tendangnhap
    public  TaiKhoanDTO selectByUserName(String tendn){
        TaiKhoanDTO tk = null;
        String sql = "SELECT * FROM taikhoan WHERE tendangnhap=?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1,tendn);
            ResultSet rs = pst.executeQuery();
            if (rs.next()){
                int manv = rs.getInt("manv");
                String tendangnhap = rs.getString("tendangnhap");
                String matkhau = rs.getString("matkhau");
                int trangthai = rs.getInt("trangthai");
                int manhomquyen = rs.getInt("manhomquyen");

                tk =new TaiKhoanDTO(manv,tendangnhap,matkhau,manhomquyen,trangthai);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return  tk;
    }
}