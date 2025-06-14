package GUI.Panel;

import BUS.*;
import DAO.NhanVienDAO;
import DAO.PhieuXuatDAO;
import DTO.*;
import GUI.Component.*;
import GUI.Dialog.ListKhachHang;
import GUI.Main;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import helper.Formater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;

public final class TaoPhieuXuat extends JPanel {

    PhienBanSanPhamBUS phienBanBus = new PhienBanSanPhamBUS();
    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
    DungLuongRamBUS ramBus = new DungLuongRamBUS();
    DungLuongRomBUS romBus = new DungLuongRomBUS();
    MauSacBUS mausacBus = new MauSacBUS();
    PanelBorderRadius right, left;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter, left_top, main, content_btn;
    JTable tablePhieuXuat, tableSanPham;
    JScrollPane scrollTablePhieuNhap, scrollTableSanPham;
    DefaultTableModel tblModel, tblModelSP;
    ButtonCustom btnAddSp, btnEditSP, btnDelete, btnNhapHang;
    InputForm txtMaphieu, txtNhanVien, txtMaSp, txtTenSp, txtSoluongTon, txtSoLuongXuat, txtGiaXuat;
    SelectForm cbxPhienBan;
    JTextField txtTimKiem;
    Color BackgroundColor = new Color(240, 247, 250);

    int sum;
    int maphieu;
    int manv;
    int makh = -1;
    String type;

    ArrayList<ChiTietPhieuDTO> chitietphieu = new ArrayList<>();
    SanPhamBUS spBUS = new SanPhamBUS();
    PhieuXuatBUS phieuXuatBUS = new PhieuXuatBUS();
    KhachHangBUS khachHangBUS = new KhachHangBUS();
    ArrayList<SanPhamDTO> listSP = spBUS.getAll();
    private ArrayList<PhienBanSanPhamDTO> ch = new ArrayList<>();

    TaiKhoanDTO tk;
    private int mapb;
    private JLabel lbltongtien;
    private JTextField txtKh;
    private Main mainChinh;
    private ButtonCustom btnQuayLai;

    public TaoPhieuXuat(Main mainChinh, TaiKhoanDTO tk, String type) {
        this.mainChinh = mainChinh;
        this.tk = tk;
        this.type = type;
        initComponent(type);
        loadDataTalbeSanPham(listSP);
    }

//    public void initPadding() {
//        pnlBorder1 = new JPanel();
//        pnlBorder1.setPreferredSize(new Dimension(0, 5));
//        pnlBorder1.setBackground(BackgroundColor);
//        this.add(pnlBorder1, BorderLayout.NORTH);
//
//        pnlBorder2 = new JPanel();
//        pnlBorder2.setPreferredSize(new Dimension(0, 5));
//        pnlBorder2.setBackground(BackgroundColor);
//        this.add(pnlBorder2, BorderLayout.SOUTH);
//
//        pnlBorder3 = new JPanel();
//        pnlBorder3.setPreferredSize(new Dimension(5, 0));
//        pnlBorder3.setBackground(BackgroundColor);
//        this.add(pnlBorder3, BorderLayout.EAST);
//
//        pnlBorder4 = new JPanel();
//        pnlBorder4.setPreferredSize(new Dimension(5, 0));
//        pnlBorder4.setBackground(BackgroundColor);
//        this.add(pnlBorder4, BorderLayout.WEST);
//    }

    private void initComponent(String type) {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Phiếu xuất
        tablePhieuXuat = new JTable();
        scrollTablePhieuNhap = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên sản phẩm", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        tablePhieuXuat.setModel(tblModel);
        scrollTablePhieuNhap.setViewportView(tablePhieuXuat);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tablePhieuXuat.getColumnModel();
        for (int i = 0; i < 8; i++) {
            if (i != 2) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        tablePhieuXuat.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablePhieuXuat.setFocusable(false);
        tablePhieuXuat.setDefaultEditor(Object.class, null);
        scrollTablePhieuNhap.setViewportView(tablePhieuXuat);

        // Table sản phẩm
        tableSanPham = new JTable();
        scrollTableSanPham = new JScrollPane();
        tblModelSP = new DefaultTableModel();
        String[] headerSP = new String[]{"Mã SP", "Tên sản phẩm", "Số lượng tồn"};
        tblModelSP.setColumnIdentifiers(headerSP);
        tableSanPham.setModel(tblModelSP);
        scrollTableSanPham.setViewportView(tableSanPham);
        tableSanPham.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableSanPham.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableSanPham.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableSanPham.setFocusable(false);
        scrollTableSanPham.setViewportView(tableSanPham);

//        initPadding();

        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(5, 5));
        this.add(contentCenter, BorderLayout.CENTER);

        left = new PanelBorderRadius();
        left.setLayout(new BorderLayout(0, 5));
        left.setBackground(Color.white);

        left_top = new JPanel();
        left_top.setLayout(new BorderLayout());
        left_top.setBorder(new EmptyBorder(5, 5, 10, 10));
        left_top.setOpaque(false);

        JPanel content_top, content_left, content_right, content_right_top;
        content_top = new JPanel(new GridLayout(1, 2, 5, 5));
        content_top.setOpaque(false);
        content_left = new JPanel(new BorderLayout(5, 5));
        content_left.setOpaque(false);
        content_left.setPreferredSize(new Dimension(0, 300));

        txtTimKiem = new JTextField();
        txtTimKiem.putClientProperty("JTextField.placeholderText", "Tên sản phẩm, mã sản phẩm...");
        txtTimKiem.putClientProperty("JTextField.showClearButton", true);
        txtTimKiem.putClientProperty("JTextField.leadingIcon", new FlatSVGIcon("./icon/search.svg"));
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ArrayList<SanPhamDTO> rs = spBUS.search(txtTimKiem.getText());
                loadDataTalbeSanPham(rs);
            }
        });
        txtTimKiem.setPreferredSize(new Dimension(100, 40));
        content_left.add(txtTimKiem, BorderLayout.NORTH);
        content_left.add(scrollTableSanPham, BorderLayout.CENTER);

        content_right = new JPanel(new BorderLayout(5, 5));
        content_right.setOpaque(false);
        content_right.setBackground(Color.WHITE);

        content_right_top = new JPanel(new BorderLayout());
        content_right_top.setPreferredSize(new Dimension(100, 165));
        txtMaSp = new InputForm("Mã SP");
        txtMaSp.setEditable(false);
        txtTenSp = new InputForm("Tên sản phẩm");
        txtTenSp.setEditable(false);
        String[] arrCauhinh = {"Chọn sản phẩm"};
        JPanel panlePXGX = new JPanel(new GridLayout(1, 4));
        panlePXGX.setPreferredSize(new Dimension(100, 90));
        cbxPhienBan = new SelectForm("Cấu hình", arrCauhinh);
        txtGiaXuat = new InputForm("Giá xuất");
        txtSoluongTon = new InputForm("Số lượng tồn");
        txtSoluongTon.setEditable(false);
        txtSoLuongXuat = new InputForm("Số lượng xuất");
        txtSoLuongXuat.setEditable(true); // Cho phép chỉnh sửa số lượng
        panlePXGX.add(cbxPhienBan);
        panlePXGX.add(txtGiaXuat);
        panlePXGX.add(txtSoluongTon);
        panlePXGX.add(txtSoLuongXuat);
        content_right_top.add(txtMaSp, BorderLayout.WEST);
        content_right_top.add(txtTenSp, BorderLayout.CENTER);
        content_right_top.add(panlePXGX, BorderLayout.SOUTH);
        cbxPhienBan.getCbb().addItemListener((ItemEvent e) -> {
            mapb = ch.get(cbxPhienBan.getSelectedIndex()).getMaphienbansp();
            if (checkTonTai()) {
                actionbtn("update");
            } else {
                actionbtn("add");
            }
            // Cập nhật giá nhập và số lượng tồn khi chọn cấu hình
            int gianhap = ch.get(cbxPhienBan.getSelectedIndex()).getGianhap(); // Giả định có phương thức này
            txtGiaXuat.setText(String.valueOf(gianhap));
            int soluongTon = ch.get(cbxPhienBan.getSelectedIndex()).getSoluongton(); // Giả định có phương thức này
            txtSoluongTon.setText(String.valueOf(soluongTon));
            txtSoLuongXuat.setText("1"); // Đặt mặc định số lượng là 1
        });

        content_right.add(content_right_top, BorderLayout.NORTH);

        content_top.add(content_left);
        content_top.add(content_right);

        content_btn = new JPanel();
        content_btn.setPreferredSize(new Dimension(0, 47));
        content_btn.setLayout(new GridLayout(1, 3, 5, 5)); // Giảm từ 4 xuống 3, loại bỏ btnImport
        content_btn.setBorder(new EmptyBorder(8, 5, 0, 10));
        content_btn.setOpaque(false);
        btnAddSp = new ButtonCustom("Thêm sản phẩm", "success", 14);
        btnEditSP = new ButtonCustom("Sửa sản phẩm", "warning", 14);
        btnDelete = new ButtonCustom("Xoá sản phẩm", "danger", 14);
        content_btn.add(btnAddSp);
        content_btn.add(btnEditSP);
        content_btn.add(btnDelete);

        btnAddSp.addActionListener((ActionEvent e) -> {
            if (checkInfo()) {
                getInfo();
                Notification notification = new Notification(mainChinh, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Thêm sản phẩm thành công!");
                notification.showNotification();
                loadDataTableChiTietPhieu(chitietphieu);
                actionbtn("update");
            }
        });

        btnEditSP.addActionListener((ActionEvent e) -> {
            int selectedRow = tablePhieuXuat.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần chỉnh sửa!");
                return;
            }

            if (checkInfo()) {
                try {
                    // Lấy thông tin mới từ giao diện
                    int dongia = Integer.parseInt(txtGiaXuat.getText().trim());
                    int soluong = Integer.parseInt(txtSoLuongXuat.getText().trim());
                    int maphienbansp = ch.get(cbxPhienBan.getSelectedIndex()).getMaphienbansp();

                    // Cập nhật ChiTietPhieuDTO
                    ChiTietPhieuDTO ctphieu = chitietphieu.get(selectedRow);
                    ctphieu.setDongia(dongia);
                    ctphieu.setSoluong(soluong);
                    ctphieu.setMaphienbansp(maphienbansp); // Cập nhật phiên bản nếu cần

                    // Làm mới bảng
                    loadDataTableChiTietPhieu(chitietphieu);
                    actionbtn("update");

                    // Thông báo thành công
                    Notification notification = new Notification(mainChinh, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Chỉnh sửa sản phẩm thành công!");
                    notification.showNotification();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Giá xuất và số lượng phải là số hợp lệ!");
                }
            }
        });

        btnDelete.addActionListener((ActionEvent e) -> {
            int index = tablePhieuXuat.getSelectedRow();
            if (index < 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn cấu hình cần xóa");
            } else {
                chitietphieu.remove(index);
                loadDataTableChiTietPhieu(chitietphieu);
            }
        });

        left_top.add(content_top, BorderLayout.CENTER);
        left_top.add(content_btn, BorderLayout.SOUTH);

        main = new JPanel();
        main.setOpaque(false);
        main.setPreferredSize(new Dimension(0, 280));
        main.setBorder(new EmptyBorder(0, 5, 10, 10));
        BoxLayout boxly = new BoxLayout(main, BoxLayout.Y_AXIS);
        main.setLayout(boxly);
        main.add(scrollTablePhieuNhap);
        left.add(left_top, BorderLayout.CENTER);
        left.add(main, BorderLayout.SOUTH);

        right = new PanelBorderRadius();
        right.setPreferredSize(new Dimension(320, 0));
        right.setBorder(new EmptyBorder(5, 5, 5, 5));
        right.setLayout(new BorderLayout());

        JPanel right_top, right_center, right_bottom, pn_tongtien;
        right_top = new JPanel(new GridLayout(2, 1, 0, 0));
        right_top.setPreferredSize(new Dimension(300, 180));
        txtMaphieu = new InputForm("Mã phiếu xuất");
        txtMaphieu.setEditable(false);
        txtNhanVien = new InputForm("Nhân viên xuất");
        txtNhanVien.setEditable(false);
        maphieu = PhieuXuatDAO.getInstance().getAutoIncrement();
        manv = tk.getManv();
        txtMaphieu.setText("PX" + maphieu);
        NhanVienDTO nhanvien = NhanVienDAO.getInstance().selectById(tk.getManv() + "");
        txtNhanVien.setText(nhanvien.getHoten());

        right_top.add(txtMaphieu);
        right_top.add(txtNhanVien);

        right_center = new JPanel(new BorderLayout());
        JPanel khachJPanel = new JPanel(new BorderLayout());
        khachJPanel.setPreferredSize(new Dimension(0, 40));
        khachJPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        khachJPanel.setOpaque(false);
        JPanel kJPanelLeft = new JPanel(new GridLayout(1, 1));
        kJPanelLeft.setOpaque(false);
        kJPanelLeft.setPreferredSize(new Dimension(40, 0));
        ButtonCustom btnKh = new ButtonCustom("Chọn khách hàng", "success", 14);
        kJPanelLeft.add(btnKh);
        btnKh.addActionListener((ActionEvent e) -> {
            ListKhachHang listkh = new ListKhachHang(TaoPhieuXuat.this, owner, "Chọn khách hàng", true);
        });

        txtKh = new JTextField("");
        txtKh.setEditable(false);
        khachJPanel.add(kJPanelLeft, BorderLayout.EAST);
        khachJPanel.add(txtKh, BorderLayout.CENTER);
        JPanel khPanel = new JPanel(new GridLayout(2, 1, 5, 0));
        khPanel.setBackground(Color.WHITE);
        khPanel.setPreferredSize(new Dimension(0, 80));
        JLabel khachKhangJLabel = new JLabel("Khách hàng");
        khachKhangJLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        khPanel.add(khachKhangJLabel);
        khPanel.add(khachJPanel);

        right_center.add(khPanel, BorderLayout.NORTH);
        right_center.setOpaque(false);

        right_bottom = new JPanel(new GridLayout(2, 1));
        right_bottom.setPreferredSize(new Dimension(300, 100));
        right_bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        right_bottom.setOpaque(false);

        pn_tongtien = new JPanel(new FlowLayout(1, 20, 0));
        pn_tongtien.setOpaque(false);
        JLabel lbltien = new JLabel("TỔNG TIỀN: ");
        lbltien.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lbltongtien = new JLabel("0đ");
        lbltongtien.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lbltien.setForeground(new Color(255, 51, 51));
        pn_tongtien.add(lbltien);
        pn_tongtien.add(lbltongtien);

        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableSanPham.getSelectedRow();
                if (index != -1) {
                    setInfoSanPham(listSP.get(index));
                    // Cập nhật số lượng tồn và giá nhập khi click vào sản phẩm
                    txtSoluongTon.setText(String.valueOf(listSP.get(index).getSoluongton()));
                    if (!ch.isEmpty()) {
                        txtGiaXuat.setText(String.valueOf(ch.get(0).getGianhap())); // Giá nhập của phiên bản đầu tiên
                    }
                }
                if (!checkTonTai()) {
                    actionbtn("add");
                } else {
                    actionbtn("update");
                }
            }
        });

        tablePhieuXuat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tablePhieuXuat.getSelectedRow();
                if (index != -1) {
                    actionbtn("update");
                    setPhieuSelected();
                }
            }
        });

        btnNhapHang = new ButtonCustom("Xuất hàng", "excel", 14);
        btnQuayLai = new ButtonCustom("Quay lại", "excel", 14);
        right_bottom.add(pn_tongtien);
        if (type.equals("create")) {
            right_bottom.add(btnNhapHang);
        } else if (type.equals("detail")) {
            right_bottom.add(btnQuayLai);
        }

        btnNhapHang.addActionListener((ActionEvent e) -> {
            if (chitietphieu.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm");
            } else if (makh == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng");
            } else {
                int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo phiếu xuất !", "Xác nhận tạo phiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (input == 0) {
                    long now = System.currentTimeMillis();
                    Timestamp currenTime = new Timestamp(now);
                    PhieuXuatDTO phieuXuat = new PhieuXuatDTO(makh, maphieu, tk.getManv(), currenTime, sum, 1);
                    phieuXuatBUS.insert(phieuXuat, chitietphieu);
                    JOptionPane.showMessageDialog(null, "Xuất hàng thành công !");
                    mainChinh.setPanel(new PhieuXuat(mainChinh, tk));
                }
            }
        });

        btnQuayLai.addActionListener((ActionEvent e) -> {
            PhieuXuat phieuXuatPanel = new PhieuXuat(mainChinh, tk);
            mainChinh.setPanel(phieuXuatPanel);
        });

        right.add(right_top, BorderLayout.NORTH);
        right.add(right_center, BorderLayout.CENTER);
        right.add(right_bottom, BorderLayout.SOUTH);

        contentCenter.add(left, BorderLayout.CENTER);
        contentCenter.add(right, BorderLayout.EAST);
        actionbtn("add");
    }

    public void loadDataTalbeSanPham(ArrayList<SanPhamDTO> result) {
        tblModelSP.setRowCount(0);
        for (SanPhamDTO sp : result) {
            tblModelSP.addRow(new Object[]{sp.getMasp(), sp.getTensp(), sp.getSoluongton()});
        }
    }

    public void setInfoSanPham(SanPhamDTO sp) {
        this.txtMaSp.setText(Integer.toString(sp.getMasp()));
        this.txtTenSp.setText(sp.getTensp());
        ch = phienBanBus.getAll(sp.getMasp());
        int size = ch.size();
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = romBus.getKichThuocById(ch.get(i).getRom()) + "GB - "
                    + ramBus.getKichThuocById(ch.get(i).getRam()) + "GB - " + mausacBus.getTenMau(ch.get(i).getMausac());
        }
        this.cbxPhienBan.setArr(arr);
        mapb = ch.get(0).getMaphienbansp();
        // Cập nhật số lượng tồn và giá nhập từ phiên bản đầu tiên
        txtSoluongTon.setText(String.valueOf(sp.getSoluongton()));
        if (!ch.isEmpty()) {
            txtGiaXuat.setText(String.valueOf(ch.get(0).getGianhap())); // Giá nhập của phiên bản đầu tiên
        }
    }

    public boolean checkInfo() {
        boolean check = true;
        if (txtMaSp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm");
            check = false;
        }
        if (txtGiaXuat.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập giá xuất");
            check = false;
        }
        try {
            int soluong = Integer.parseInt(txtSoLuongXuat.getText().trim());
            if (soluong <= 0) {
                JOptionPane.showMessageDialog(null, "Số lượng xuất phải lớn hơn 0");
                check = false;
            } else if (soluong > Integer.parseInt(txtSoluongTon.getText().trim())) {
                JOptionPane.showMessageDialog(null, "Số lượng xuất vượt quá số lượng tồn");
                check = false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Số lượng xuất phải là số hợp lệ");
            check = false;
        }
        return check;
    }

    public ChiTietPhieuDTO getInfo() {
        int dongia = Integer.parseInt(txtGiaXuat.getText().trim());
        int soluong = Integer.parseInt(txtSoLuongXuat.getText().trim());
        ChiTietPhieuDTO ctpx = new ChiTietPhieuDTO(maphieu, mapb, soluong, dongia);
        chitietphieu.add(ctpx);
        return ctpx;
    }

    public void actionbtn(String type) {
        boolean val_1 = type.equals("add");
        boolean val_2 = type.equals("update");
        btnAddSp.setEnabled(val_1);
        btnEditSP.setEnabled(val_2);
        btnDelete.setEnabled(val_2);
        content_btn.revalidate();
        content_btn.repaint();
    }

    public boolean checkTonTai() {
        boolean check = false;
        int pb = ch.get(cbxPhienBan.getSelectedIndex()).getMaphienbansp();
        for (ChiTietPhieuDTO chiTietPhieu : chitietphieu) {
            if (chiTietPhieu.getMaphienbansp() == pb) {
                return true;
            }
        }
        return check;
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuDTO> ctPhieu) {
        tblModel.setRowCount(0);
        int size = ctPhieu.size();
        sum = 0;
        for (int i = 0; i < size; i++) {
            PhienBanSanPhamDTO phienban = phienBanBus.getByMaPhienBan(ctPhieu.get(i).getMaphienbansp());
            sum += ctPhieu.get(i).getDongia() * ctPhieu.get(i).getSoluong();
            tblModel.addRow(new Object[]{
                    i + 1, phienban.getMasp(), spBUS.getByMaSP(phienban.getMasp()).getTensp(),
                    ramBus.getKichThuocById(phienban.getRam()) + "GB",
                    romBus.getKichThuocById(phienban.getRom()) + "GB", mausacBus.getTenMau(phienban.getMausac()),
                    Formater.FormatVND(ctPhieu.get(i).getDongia()), ctPhieu.get(i).getSoluong()
            });
        }
        lbltongtien.setText(Formater.FormatVND(sum));
    }

    public void setKhachHang(int index) {
        makh = index;
        KhachHangDTO khachhang = khachHangBUS.selectKh(makh);
        txtKh.setText(khachhang.getHoten());
    }

    public void setPhieuSelected() {
        ChiTietPhieuDTO ctphieu = chitietphieu.get(tablePhieuXuat.getSelectedRow());
        SanPhamDTO spSel = spBUS.getSp(ctphieu.getMaphienbansp());
        setInfoSanPham(spSel);
        cbxPhienBan.setSelectedItem(ctphieu.getMaphienbansp() + "");
        txtGiaXuat.setText(String.valueOf(ctphieu.getDongia()));
        txtSoLuongXuat.setText(String.valueOf(ctphieu.getSoluong()));
    }
}