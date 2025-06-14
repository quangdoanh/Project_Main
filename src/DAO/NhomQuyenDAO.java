package DAO;

import DTO.NhomQuyenDTO;
import config.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Tran Nhat Sinh
 */
public class NhomQuyenDAO implements DAOinterface<NhomQuyenDTO> {

    public static  NhomQuyenDAO getInstance(){
        return new NhomQuyenDAO();
    }
    @Override
    public int insert(NhomQuyenDTO nhomQuyen) {
        int result =0;
        String sql = "INSERT INTO nhomquyen(tennhomquyen,trangthai) VALUES (?,1)";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1,nhomQuyen.getTennhomquyen());
            result = pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(NhomQuyenDTO nhomQuyen) {
        int result = 0;
        String sql = "UPDATE nhomquyen SET tennhomquyen=? WHERE manhomquyen=?";
        try(Connection conn = JDBC.getConnection();//mở kết nối đến csdl
            PreparedStatement pst = conn.prepareStatement(sql)){// khảo tạo độis tươn từ cái sql  để có thể gán giá trị truyeeng vòa ?,? :1,2
            pst.setString(1,nhomQuyen.getTennhomquyen());
            pst.setInt(2,nhomQuyen.getManhomquyen());
            result = pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(String t) {
        int result = 0;
        String sql = "UPDATE nhomquyen SET trangthai = 0 WHERE manhomquyen = ? ";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1,t);
            result =pst.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public ArrayList<NhomQuyenDTO> selectAll() {
        ArrayList<NhomQuyenDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhomquyen WHERE trangthai = 1";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                int manhomquyen = rs.getInt("manhomquyen");
                String tennhomquyen = rs.getString("tennhomquyen");
                NhomQuyenDTO dvt = new NhomQuyenDTO(manhomquyen,tennhomquyen);
                list.add(dvt);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public NhomQuyenDTO selectById(int t) {
        NhomQuyenDTO list = null;
        String sql = "SELECT * FROM nhomquyen WHERE manhomquyen = ?";
        try(Connection conn = JDBC.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1,t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                int manhomquyen = rs.getInt("manhomquyen");
                String tennhomquyen = rs.getString("tennhomquyen");
                list = new NhomQuyenDTO(manhomquyen,tennhomquyen);
            }
        } catch (SQLException e) {

        }
        return list;
    }

    @Override
    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBC.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quanlikhohang' AND   TABLE_NAME   = 'nhomquyen'";
            PreparedStatement pst =  con.prepareStatement(sql);
            ResultSet rs2 = pst.executeQuery();
            if (!rs2.isBeforeFirst()) {
                System.out.println("No data");
            } else {
                while (rs2.next()) {
                    result = rs2.getInt("AUTO_INCREMENT");

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
