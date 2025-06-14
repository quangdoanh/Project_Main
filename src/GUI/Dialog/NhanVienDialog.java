package GUI.Dialogs;

import BUS.NhanVienBUS;
import DAO.KhachHangDAO;
import DAO.NhanVienDAO;
import DTO.OBJECTS.KhachHangDTO;
import DTO.OBJECTS.NhanVienDTO;
import GUI.Components.InputForm;
import GUI.Panels.KhachHangPanel;
import GUI.Panels.NhanVienPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class NhanVienDialog  extends JDialog implements MouseListener {

    NhanVienPanel NVpl;
    private JPanel pnlMain, pnlButtom ;
    private JButton btnThem, btnCapNhat, btnHuyBo;
    NhanVienDTO nhanvienDTO;
    private InputForm name, sdt, email , ngaysinh;
    private ButtonGroup gender;
    private JRadioButton male;
    private JRadioButton female;

    // thêm
    public NhanVienDialog(NhanVienPanel NV, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.NVpl = NV;
        name = new InputForm("Tên Nhân Viên");
        sdt = new InputForm("Số điện thoại");
        email = new InputForm("Email");
        ngaysinh = new InputForm("Ngày sinh");
        male = new JRadioButton("Nam");
        female = new JRadioButton("Nữ");
        gender = new ButtonGroup();
        gender.add(male);
        gender.add(female);
        initComponents(title, type);
    }
    // sửa
    public NhanVienDialog(NhanVienPanel NV, JFrame owner, String title, boolean modal, String type, NhanVienDTO nv) {
        super(owner, title, modal);
        this.NVpl = NV;
        this.nhanvienDTO = nv;
        name = new InputForm("Tên Nhân Viên");
        name.setText(nv.getHoten());
        sdt = new InputForm("Số điện thoại");
        sdt.setText(nv.getSdt());
        email = new InputForm("Email");
        email.setText(nv.getEmail());
        ngaysinh = new InputForm("Ngày sinh");
        ngaysinh.setText(nv.getNgaysinh());
        male = new JRadioButton("Nam");
        female = new JRadioButton("Nữ");
        gender = new ButtonGroup();
        gender.add(male);
        gender.add(female);
        if (nv.getGioitinh() == 1) {
            male.setSelected(true);
        } else {
            female.setSelected(true);
        }
        initComponents(title, type);
    }

    public void initComponents(String title, String type){
        this.setSize(new Dimension(700, 700));
        this.setLayout(new BorderLayout(0, 0));
        // Title
        JLabel titlePage = new JLabel(title, JLabel.CENTER); // căn giữa nếu muốn
        titlePage.setPreferredSize(new Dimension(500, 60));
        titlePage.setOpaque(true); // BẮT BUỘC để màu nền hiện ra vì mặc ko vẽ nền
        titlePage.setBackground(Color.YELLOW); //
        //Content
        pnlMain = new JPanel(new GridLayout(5, 1, 20, 0));
        pnlMain.setBackground(Color.BLUE);

        JPanel jgender = new JPanel(new GridLayout(1, 2,10, 0));
        jgender.setBorder(new EmptyBorder(0, 10, 5, 10));
        jgender.setOpaque(true);
        jgender.setBackground(Color.white);

        male.setOpaque(true);
        female.setOpaque(true);

        male.setBackground(Color.white);
        female.setBackground(Color.white);

        jgender.add(male);
        jgender.add(female);

        pnlMain.add(name);
        pnlMain.add(sdt);
        pnlMain.add(email);
        pnlMain.add(jgender);
        pnlMain.add(ngaysinh);

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
                name.setDisable();
                sdt.setDisable();
                email.setDisable();
                ngaysinh.setDisable();
                male.setEnabled(false);
                female.setEnabled(false);
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

    boolean Validation() {
        // Kiểm tra tên nhân viên
        if (name.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra số điện thoại
        if (sdt.getText().isEmpty() || !sdt.getText().matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được rỗng và phải là 10 số, bắt đầu bằng 0", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra email  // ^[\w.-]+@[\w.-]+\.[a-zA-Z]{2,}$
        if (email.getText().isEmpty() || !email.getText().matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            JOptionPane.showMessageDialog(this, "Email không được rỗng và phải đúng định dạng (vd: ten@email.com)", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra ngày sinh
        if (ngaysinh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được rỗng", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!ngaysinh.getText().matches("\\d{2}\\d{2}\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Ngày sinh phải đúng định dạng ddMMyyyy", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Kiểm tra giới tính
        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính", "Cảnh báo !", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == btnThem && Validation() ){
            int id = NhanVienDAO.getInstance().getAutoIncrement(); //

            System.out.println("ma nv khoi tao"+id);
            // chọn giới tính
            int txt_gender = -1;
            if (male.isSelected()) {
                System.out.println("Nam");
                txt_gender = 1;
            } else if (female.isSelected()) {
                System.out.println("Nữ");
                txt_gender = 0;
            }

            if(NVpl.nvBus.add(new NhanVienDTO(id,name.getText(), txt_gender, ngaysinh.getText(), sdt.getText(), 1, email.getText()))){
                System.out.println("Them thanh cong" + NVpl.listnv );
                NVpl.loadDataTable(NVpl.listnv);
                dispose(); // đóng cửa so va giai phong tai nguyen
            }
        }else if (e.getSource() == btnCapNhat && Validation()) {

            // chọn giới tính
            int txt_gender = -1;
            if (male.isSelected()) {
                System.out.println("Nam");
                txt_gender = 1;
            } else if (female.isSelected()) {
                System.out.println("Nữ");
                txt_gender = 0;
            }

            if(NVpl.nvBus.update(new NhanVienDTO(nhanvienDTO.getManv(),name.getText(),txt_gender , ngaysinh.getText(), sdt.getText(), 1, email.getText()))) {
                NVpl.loadDataTable(NVpl.listnv);
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
