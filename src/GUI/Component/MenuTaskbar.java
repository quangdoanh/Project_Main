package GUI.Component;

import DAO.ChiTietQuyenDAO;
import DAO.NhanVienDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NhanVienDTO;
import DTO.NhomQuyenDTO;
import DTO.TaiKhoanDTO;
import GUI.Log_In;
import GUI.Main;
import GUI.Panel.PhieuNhap;
import GUI.Panel.PhieuXuat;
import GUI.Panel.TrangChu;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuTaskbar extends JPanel {
    TrangChu trangChu;
    PhieuNhap phieuNhap;
    PhieuXuat phieuXuat;
    Main main;
    TaiKhoanDTO user;
    public itemTaskbar[] listitem;
    JLabel lblTenNhomQuyen, lblUsername;
    JScrollPane scrollPane;
    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;
    Color FontColor = new Color(96, 125, 139);
    Color DefaultColor = new Color(255, 255, 255);
    Color HowerFontColor = new Color(1, 87, 155);
    Color HowerBackgroundColor = new Color(187, 222, 251);
    private ArrayList<ChiTietQuyenDTO> listQuyen;
    NhomQuyenDTO nhomQuyenDTO;
    public NhanVienDTO nhanVienDTO;
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    // Chỉ giữ lại Trang chủ, Phiếu nhập, Đăng xuất
    String[][] getSt = {
            {"Trang chủ", "home.svg", "trangchu"},
            {"Phiếu nhập", "import.svg", "nhaphang"},
            {"Phiếu xuất", "export.svg", "xuathang"},
            {"Đăng xuất", "log_out.svg", "dangxuat"},
    };

    public MenuTaskbar(Main main, TaiKhoanDTO tk) {
        this.main = main;
        this.user = tk;
        this.nhomQuyenDTO = NhomQuyenDAO.getInstance().selectById(Integer.toString(tk.getManhomquyen()));
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(Integer.toString(tk.getManv()));
        listQuyen = ChiTietQuyenDAO.getInstance().selectAll(Integer.toString(tk.getManhomquyen()));
        initComponent();
    }

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }
    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setOpaque(true);
        this.setBackground(DefaultColor);
        this.setLayout(new BorderLayout(0, 0));

        // Thiết lập pnlTop
        pnlTop = new JPanel();
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        pnlTop.setLayout(new BorderLayout(0, 0));
        this.add(pnlTop, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BorderLayout(0, 0));
        pnlTop.add(info, BorderLayout.CENTER);

        in4(info);

        bar1 = new JPanel();
        bar1.setBackground(new Color(204, 214, 219));
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

        bar2 = new JPanel();
        bar2.setBackground(new Color(204, 214, 219));
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        // Thiết lập pnlCenter
        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(230, 600));
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new FlowLayout(0, 0, 5));

        bar3 = new JPanel();
        bar3.setBackground(new Color(204, 214, 219));
        bar3.setPreferredSize(new Dimension(1, 1));
        this.add(bar3, BorderLayout.EAST);

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        this.add(scrollPane, BorderLayout.CENTER);

        // Thiết lập pnlBottom
        pnlBottom = new JPanel();
        pnlBottom.setPreferredSize(new Dimension(250, 50));
        pnlBottom.setBackground(DefaultColor);
        pnlBottom.setLayout(new BorderLayout(0, 0));

        bar4 = new JPanel();
        bar4.setBackground(new Color(204, 214, 219));
        bar4.setPreferredSize(new Dimension(1, 1));
        pnlBottom.add(bar4, BorderLayout.EAST);

        this.add(pnlBottom, BorderLayout.SOUTH);

        // Khởi tạo các mục menu
        for (int i = 0; i < getSt.length; i++) {
            listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
            if (i == getSt.length - 1) { // Đăng xuất ở dưới cùng
                pnlBottom.add(listitem[i]);
            } else {
                pnlCenter.add(listitem[i]);
                if (i != 0 && !checkRole(getSt[i][2])) {
                    listitem[i].setVisible(false);
                }
            }
        }

        // Đặt trạng thái ban đầu cho Trang chủ
        listitem[0].setBackground(HowerBackgroundColor);
        listitem[0].setForeground(HowerFontColor);
        listitem[0].isSelected = true;

        // Thêm sự kiện cho các mục menu
        listitem[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                trangChu = new TrangChu();
                main.setPanel(trangChu);
                pnlMenuTaskbarMousePress(evt);
            }
        });

        listitem[1].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                phieuNhap = new PhieuNhap(main, nhanVienDTO);
                main.setPanel(phieuNhap);
                pnlMenuTaskbarMousePress(evt);
            }
        });
        listitem[2].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                phieuXuat = new PhieuXuat(main, user);
                main.setPanel(phieuXuat);
                pnlMenuTaskbarMousePress(evt);
            }
        });


        listitem[3].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                int input = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    Log_In login = new Log_In();
                    main.dispose();
                    login.setVisible(true);
                }
                pnlMenuTaskbarMousePress(evt);
            }
        });
    }

    public boolean checkRole(String s) {
        for (ChiTietQuyenDTO quyen : listQuyen) {
            if (quyen.getHanhdong().equals("view") && s.equals(quyen.getMachucnang())) {
                return true;
            }
        }
        return false;
    }

    public void pnlMenuTaskbarMousePress(MouseEvent evt) {
        for (int i = 0; i < getSt.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].isSelected = true;
                listitem[i].setBackground(HowerBackgroundColor);
                listitem[i].setForeground(HowerFontColor);
            } else {
                listitem[i].isSelected = false;
                listitem[i].setBackground(DefaultColor);
                listitem[i].setForeground(FontColor);
            }
        }
    }

    public void resetChange() {
        this.nhanVienDTO = NhanVienDAO.getInstance().selectById(String.valueOf(nhanVienDTO.getManv()));
    }

    public void in4(JPanel info) {
        JPanel pnlIcon = new JPanel(new FlowLayout());
        pnlIcon.setPreferredSize(new Dimension(60, 0));
        pnlIcon.setOpaque(false);
        info.add(pnlIcon, BorderLayout.WEST);
        JLabel lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(50, 70));
        if (nhanVienDTO.getGioitinh() == 1) {
            lblIcon.setIcon(new FlatSVGIcon("./icon/man_50px.svg"));
        } else {
            lblIcon.setIcon(new FlatSVGIcon("./icon/women_50px.svg"));
        }
        pnlIcon.add(lblIcon);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setOpaque(false);
        pnlInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Sửa lỗi ở đây
        pnlInfo.setBorder(new EmptyBorder(15, 0, 0, 0));
        info.add(pnlInfo, BorderLayout.CENTER);

        lblUsername = new JLabel(nhanVienDTO.getHoten());
        lblUsername.putClientProperty("FlatLaf.style", "font: 150% $semibold.font");
        pnlInfo.add(lblUsername);

        lblTenNhomQuyen = new JLabel(nhomQuyenDTO.getTennhomquyen());
        lblTenNhomQuyen.putClientProperty("FlatLaf.style", "font: 120% $light.font");
        lblTenNhomQuyen.setForeground(Color.GRAY);
        pnlInfo.add(lblTenNhomQuyen);

    }
}