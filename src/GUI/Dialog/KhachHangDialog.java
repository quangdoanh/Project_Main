package GUI.Dialogs;

import DAO.KhachHangDAO;
import DTO.OBJECTS.KhachHangDTO;
import GUI.Components.InputForm;
import GUI.Panels.KhachHangPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KhachHangDialog extends JDialog implements MouseListener {
    KhachHangPanel KHpl;
    private JPanel pnlMain, pnlButtom ;
    private InputForm tenKH, sdtKH, diachiKH;
    private JTextField maKH;
    private JButton btnThem, btnCapNhat, btnHuyBo;
    KhachHangDTO kh;

    // thêm
    public KhachHangDialog(KhachHangPanel KH, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.KHpl = KH;
        tenKH = new InputForm("Tên khách hàng");
        sdtKH = new InputForm("Số điện thoại");
        diachiKH = new InputForm("Địa chỉ");
        initComponents(title, type);
    }
    // sửa
    public KhachHangDialog(KhachHangPanel KH, JFrame owner, String title, boolean modal, String type, KhachHangDTO kh) {
        super(owner, title, modal);
        this.kh=kh;
//        maKH = new JTextField();
//        maKH.setText(""+kh.getMaKH());
        tenKH = new InputForm("Tên khách hàng");
        tenKH.setText(kh.getHoten());
        sdtKH = new InputForm("Số điện thoại");
        sdtKH.setText(kh.getSdt());
        diachiKH = new InputForm("Địa chỉ");
        diachiKH.setText(kh.getDiachi());
        this.KHpl = KH;
        initComponents(title, type);
    }

    public void initComponents(String title, String type){
        this.setSize(new Dimension(500, 500));
        this.setLayout(new BorderLayout(0, 0));
        // Title
        JLabel titlePage = new JLabel(title, JLabel.CENTER); // căn giữa nếu muốn
        titlePage.setPreferredSize(new Dimension(500, 60));
        titlePage.setOpaque(true); // BẮT BUỘC để màu nền hiện ra vì mặc ko vẽ nền
        titlePage.setBackground(Color.YELLOW); //
        //Content
        pnlMain = new JPanel(new GridLayout(3, 1, 20, 0));
        pnlMain.setBackground(Color.BLUE);

        pnlMain.add(tenKH);
        pnlMain.add(sdtKH);
        pnlMain.add(diachiKH);

        //Button
        pnlButtom = new JPanel(new FlowLayout());
        pnlButtom.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnlButtom.setBackground(Color.YELLOW);

        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Sửa");
        btnHuyBo = new JButton("Hủy Bỏ");

        //Add MouseListener btn
        btnThem.addMouseListener(this);
        btnCapNhat.addMouseListener(this);
        btnHuyBo.addMouseListener(this);

        switch (type) {
            case "create" -> {
                pnlButtom.add(btnThem);
                break;
            }
            case "update" -> {
                pnlButtom.add(btnCapNhat);
                break;
            }
            case "view" -> {
                tenKH.setDisable();
                sdtKH.setDisable();
                diachiKH.setDisable();


            }
//            default ->
//                    throw new AssertionError(); // ?
        }
        pnlButtom.add(btnHuyBo);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtom, BorderLayout.SOUTH);
        this.setLocationRelativeTo(null); // căn giữa màn hình nếu mà đặt cái components khác thì căn giữa components đó
        this.setVisible(true);

    }

    boolean Validation(){
        if (tenKH.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if (sdtKH.getText().isEmpty() || !sdtKH.getText().matches("0\\d{9}") || sdtKH.getText().length() != 10) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được rỗng và phải là 10 ký tự số", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            System.out.println("Chạy vào dk2");
            return false;
        }
        else  if (diachiKH.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        System.out.println("khong chạy qua điều kiện nào");

        return true;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == btnThem && Validation() ){
            int id = KhachHangDAO.getInstance().getAutoIncrement(); //6
            System.out.println(id);
            if(KHpl.khachhangBUS.add(new KhachHangDTO(id,tenKH.getText(), sdtKH.getText(), diachiKH.getText()))){
                System.out.println("Them thanh cong" );
                KHpl.loadDataTable(KHpl.listkh);
                dispose(); // đóng cửa so va giai phong tai nguyen
            }
        }else if (e.getSource() == btnCapNhat && Validation()) {
            if(KHpl.khachhangBUS.update(new KhachHangDTO(kh.getMaKH(), tenKH.getText(), sdtKH.getText(), diachiKH.getText()))) {
                KHpl.loadDataTable(KHpl.listkh);
                System.out.println("Cập nhật thanh cong" );
                dispose();
            }
        }else if (e.getSource() == btnHuyBo) {
            dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
