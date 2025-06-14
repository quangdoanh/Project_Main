package GUI.Panel;

import BUS.*;
import DTO.*;
import GUI.Component.*;
import GUI.Main;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import helper.Formater;
import helper.Validation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public final class TaoPhieuNhap extends JPanel implements ItemListener, ActionListener {
    //1. Khai báo lớp và các biến thành viên
    PanelBorderRadius right, left;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter, left_top, main, content_right_bottom, content_btn;
    JTable tablePhieuNhap, tableSanPham;
    JScrollPane scrollTablePhieuNhap, scrollTableSanPham;
    DefaultTableModel tblModel, tblModelSP;
    ButtonCustom btnAddSp, btnEditSP, btnDelete, btnNhapHang;
    InputForm txtMaphieu, txtNhanVien, txtMaSp, txtTenSp, txtDongia, txtSoLuong;
    SelectForm cbxNhaCungCap, cbxCauhinh;
    JTextField txtTimKiem;
    JLabel lbltongtien;

    Main m;
    Color BackgroundColor = new Color(240, 247, 250);

    SanPhamBUS spBUS = new SanPhamBUS();
    NhaCungCapBUS nccBus = new NhaCungCapBUS();
    PhienBanSanPhamBUS phienbanBus = new PhienBanSanPhamBUS();
    DungLuongRamBUS ramBus = new DungLuongRamBUS();
    DungLuongRomBUS romBus = new DungLuongRomBUS();
    PhieuNhapBUS phieunhapBus = new PhieuNhapBUS();
    MauSacBUS mausacBus = new MauSacBUS();
    NhanVienDTO nvDto;

    ArrayList<SanPhamDTO> listSP = spBUS.getAll();
    ArrayList<PhienBanSanPhamDTO> ch = new ArrayList<>();
    ArrayList<ChiTietPhieuDTO> chitietphieu;
    HashMap<Integer, ArrayList<ChiTietSanPhamDTO>> chitietsanpham = new HashMap<>();
    int maphieunhap;
    int rowPhieuSelect = -1;

    public TaoPhieuNhap(NhanVienDTO nv, String type, Main m) {
        this.nvDto = nv;
        this.m = m;
        maphieunhap = phieunhapBus.phieunhapDAO.getAutoIncrement();//Lấy mã phiếu nhập tự động tăng từ cơ sở dữ liệu.
        chitietphieu = new ArrayList<>();
        initComponent(type);
        loadDataTalbeSanPham(listSP);//Tải danh sách sản phẩm vào bảng sản phẩm
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

        // Phiếu nhập
        tablePhieuNhap = new JTable();
        scrollTablePhieuNhap = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên sản phẩm", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        tablePhieuNhap.setModel(tblModel);
        scrollTablePhieuNhap.setViewportView(tablePhieuNhap);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = tablePhieuNhap.getColumnModel();
        for (int i = 0; i < 8; i++) {
            if (i != 2) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        tablePhieuNhap.getColumnModel().getColumn(2).setPreferredWidth(300);
        tablePhieuNhap.setDefaultEditor(Object.class, null);
        tablePhieuNhap.setFocusable(false);

        tablePhieuNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tablePhieuNhap.getSelectedRow();//Lấy chỉ số dòng được chọn.
                if (index != -1) {
                    setFormChiTietPhieu(chitietphieu.get(index));//Hiển thị thông tin chi tiết sản phẩm lên form.
                    rowPhieuSelect = index;
                    actionbtn("update");
                }
            }
        });

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
        tableSanPham.setDefaultEditor(Object.class, null);
        tableSanPham.setFocusable(false);
//      scrollTableSanPham.setViewportView(tableSanPham);

        tableSanPham.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = tableSanPham.getSelectedRow();
                if (index != -1) {
                    resetForm();
                    setInfoSanPham(listSP.get(index));
                    ChiTietPhieuDTO ctp = checkTonTai();
                    if (ctp == null) {
                        actionbtn("add");
                    } else {
                        actionbtn("update");
                        setFormChiTietPhieu(ctp);
                    }
                }
            }
        });

//        initPadding();
//        Thiết lập bố cục giao diện chính
        contentCenter = new JPanel();
        contentCenter.setPreferredSize(new Dimension(1100, 600));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(5, 5));
        this.add(contentCenter, BorderLayout.CENTER);

        left = new PanelBorderRadius();
        left.setLayout(new BorderLayout(0, 5));
        left.setBackground(Color.white);

        left_top = new JPanel(); // Chứa tất cả phần ở phía trái trên cùng
        left_top.setLayout(new BorderLayout());
        left_top.setBorder(new EmptyBorder(5, 5, 10, 10));
        left_top.setOpaque(false);

//        Phần tìm kiếm và bảng sản phẩm
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


//        Form nhập liệu sản phẩm
        content_right = new JPanel(new BorderLayout(5, 5));
        content_right.setOpaque(false);
        content_right_top = new JPanel(new BorderLayout());
        content_right_top.setPreferredSize(new Dimension(100, 200));
        txtMaSp = new InputForm("Mã sản phẩm");
        txtMaSp.setEditable(false);
        txtTenSp = new InputForm("Tên sản phẩm");
        txtTenSp.setEditable(false);
        String[] arrCauhinh = {"Chọn sản phẩm"};
        JPanel content_right_top_cbx = new JPanel(new BorderLayout());
        content_right_top_cbx.setPreferredSize(new Dimension(100, 100));
        cbxCauhinh = new SelectForm("Cấu hình", arrCauhinh);
        cbxCauhinh.cbb.addItemListener(this);
        txtDongia = new InputForm("Giá nhập");
        PlainDocument dongia = (PlainDocument) txtDongia.getTxtForm().getDocument();
        dongia.setDocumentFilter((new NumericDocumentFilter()));
        content_right_top_cbx.add(cbxCauhinh, BorderLayout.WEST);
        content_right_top_cbx.add(txtDongia, BorderLayout.CENTER);
        content_right_top.add(txtMaSp, BorderLayout.WEST);
        content_right_top.add(txtTenSp, BorderLayout.CENTER);
        content_right_top.add(content_right_top_cbx, BorderLayout.SOUTH);

        content_right_bottom = new JPanel(new CardLayout());

        JPanel card_content_one = new JPanel(new BorderLayout());
        card_content_one.setBackground(Color.white);
        card_content_one.setPreferredSize(new Dimension(100, 90));
        JPanel card_content_one_model = new JPanel(new BorderLayout());
        card_content_one_model.setPreferredSize(new Dimension(100, 90));
        txtSoLuong = new InputForm("Số lượng");
        PlainDocument soluong = (PlainDocument) txtSoLuong.getTxtForm().getDocument();
        soluong.setDocumentFilter((new NumericDocumentFilter()));
        card_content_one_model.add(txtSoLuong, BorderLayout.CENTER);
        card_content_one.add(card_content_one_model, BorderLayout.NORTH);


        content_right_bottom.add(card_content_one);

        content_right.add(content_right_top, BorderLayout.NORTH);
        content_right.add(content_right_bottom, BorderLayout.CENTER);

        content_top.add(content_left);
        content_top.add(content_right);

        content_btn = new JPanel();
        content_btn.setPreferredSize(new Dimension(0, 47));
        content_btn.setLayout(new GridLayout(1, 4, 5, 5));
        content_btn.setBorder(new EmptyBorder(8, 5, 0, 10));
        content_btn.setOpaque(false);
        btnAddSp = new ButtonCustom("Thêm sản phẩm", "success", 14);
        btnEditSP = new ButtonCustom("Sửa sản phẩm", "warning", 14);
        btnDelete = new ButtonCustom("Xoá sản phẩm", "danger", 14);
        btnAddSp.addActionListener(this);
        btnEditSP.addActionListener(this);
        btnDelete.addActionListener(this);
        btnEditSP.setEnabled(false);
        btnDelete.setEnabled(false);
        content_btn.add(btnAddSp);
        content_btn.add(btnEditSP);
        content_btn.add(btnDelete);

        left_top.add(content_top, BorderLayout.CENTER);

//        Bảng phiếu nhập
        main = new JPanel();
        main.setOpaque(false);
        main.setPreferredSize(new Dimension(0, 250));
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
        right_top = new JPanel(new GridLayout(4, 1, 0, 0));
        right_top.setPreferredSize(new Dimension(300, 360));
        right_top.setOpaque(false);
        txtMaphieu = new InputForm("Mã phiếu nhập");
        txtMaphieu.setText("PN" + maphieunhap);
        txtMaphieu.setEditable(false);
        txtNhanVien = new InputForm("Nhân viên nhập");
        txtNhanVien.setText(nvDto.getHoten());
        txtNhanVien.setEditable(false);
        cbxNhaCungCap = new SelectForm("Nhà cung cấp", nccBus.getArrTenNhaCungCap());
        right_top.add(txtMaphieu);
        right_top.add(txtNhanVien);
        right_top.add(cbxNhaCungCap);

        right_center = new JPanel();
        right_center.setPreferredSize(new Dimension(100, 100));
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
        right_bottom.add(pn_tongtien);

        btnNhapHang = new ButtonCustom("Nhập hàng", "excel", 14);
        btnNhapHang.addActionListener(this);
        right_bottom.add(btnNhapHang);
        left_top.add(content_btn, BorderLayout.SOUTH);

        right.add(right_top, BorderLayout.NORTH);
        right.add(right_center, BorderLayout.CENTER);
        right.add(right_bottom, BorderLayout.SOUTH);

        contentCenter.add(left, BorderLayout.CENTER);
        contentCenter.add(right, BorderLayout.EAST);
    }
//    Các phương thức hỗ trợ
    public void loadDataTalbeSanPham(ArrayList<SanPhamDTO> result) {
        tblModelSP.setRowCount(0);
        for (SanPhamDTO sp : result) {
            tblModelSP.addRow(new Object[]{sp.getMasp(), sp.getTensp(), sp.getSoluongton()});
        }
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuDTO> ctPhieu) {
        tblModel.setRowCount(0);
        int size = ctPhieu.size();
        for (int i = 0; i < size; i++) {
            PhienBanSanPhamDTO pb = phienbanBus.getByMaPhienBan(ctPhieu.get(i).getMaphienbansp());
            tblModel.addRow(new Object[]{
                i + 1, pb.getMasp(), spBUS.getByMaSP(pb.getMasp()).getTensp(), ramBus.getKichThuocById(pb.getRam()) + "GB",
                romBus.getKichThuocById(pb.getRom()) + "GB", mausacBus.getTenMau(pb.getMausac()),
                Formater.FormatVND(ctPhieu.get(i).getDongia()), ctPhieu.get(i).getSoluong()
            });
        }
        lbltongtien.setText(Formater.FormatVND(phieunhapBus.getTongTien(ctPhieu)));
    }

    public void setInfoSanPham(SanPhamDTO sp) {
        this.txtMaSp.setText(Integer.toString(sp.getMasp()));
        this.txtTenSp.setText(sp.getTensp());
        ch = phienbanBus.getAll(sp.getMasp());
        cbxCauhinh.setArr(getCauHinhPhienBan(sp.getMasp()));
        this.txtDongia.setText(Integer.toString(ch.get(0).getGianhap()));
    }

    public ChiTietPhieuDTO getInfoChiTietPhieu() {
        if (!validateInput()) return null;
        try {
            int maphienbansp = ch.get(cbxCauhinh.cbb.getSelectedIndex()).getMaphienbansp();
            int gianhap = Integer.parseInt(txtDongia.getText());
            int soluong = Integer.parseInt(txtSoLuong.getText());
            ChiTietPhieuDTO ctphieu = new ChiTietPhieuDTO(maphieunhap, maphienbansp, soluong, gianhap);
            return ctphieu;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    //Thay đổi giá tùy theo cấu hình
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source == cbxCauhinh.cbb) {
            int index = cbxCauhinh.cbb.getSelectedIndex();
            this.txtDongia.setText(Integer.toString(ch.get(index).getGianhap()));
            ChiTietPhieuDTO ctp = checkTonTai();
            if (ctp == null) {
                actionbtn("add");
                this.txtSoLuong.setText("");
            } else {
                actionbtn("update");
                setFormChiTietPhieu(ctp);
            }
        }
    }

    public void addCtPhieu() {
        ChiTietPhieuDTO ctphieu = getInfoChiTietPhieu();
        if (ctphieu == null) return;
        ChiTietPhieuDTO p = phieunhapBus.findCT(chitietphieu, ctphieu.getMaphienbansp());
        if (p == null) {
            chitietphieu.add(ctphieu);
            Notification notification = new Notification(m, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Thêm sản phẩm thành công!");
            notification.showNotification();
            loadDataTableChiTietPhieu(chitietphieu);
            resetForm();
        } else {
            int input = JOptionPane.showConfirmDialog(this, "Sản phẩm đã tồn tại trong phiếu!\nBạn có muốn chỉnh sửa không?", "Sản phẩm đã tồn tại!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (input == 0) {
                setFormChiTietPhieu(p);
                actionbtn("update");
            }
        }
    }

    public ChiTietPhieuDTO checkTonTai() {
        int mapb = ch.get(cbxCauhinh.cbb.getSelectedIndex()).getMaphienbansp();
        ChiTietPhieuDTO p = phieunhapBus.findCT(chitietphieu, mapb);
        return p;
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


    public void setFormChiTietPhieu(ChiTietPhieuDTO phieu) {
        PhienBanSanPhamDTO pb = phienbanBus.getByMaPhienBan(phieu.getMaphienbansp());
        this.txtMaSp.setText(Integer.toString(pb.getMasp()));
        this.txtTenSp.setText(spBUS.getByMaSP(pb.getMasp()).getTensp());
        this.cbxCauhinh.setArr(getCauHinhPhienBan(pb.getMasp()));
        this.cbxCauhinh.setSelectedIndex(phienbanBus.getIndexByMaPhienBan(ch, phieu.getMaphienbansp()));
        this.txtDongia.setText(Integer.toString(phieu.getDongia()));
    }

    public ArrayList<ChiTietSanPhamDTO> findMaPhienBan(int mapb) {
        return chitietsanpham.get(mapb);
    }


    public String[] getCauHinhPhienBan(int masp) {
        ch = phienbanBus.getAll(masp);
        int size = ch.size();
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = romBus.getKichThuocById(ch.get(i).getRom()) + "GB - "
                    + ramBus.getKichThuocById(ch.get(i).getRam()) + "GB - " + mausacBus.getTenMau(ch.get(i).getMausac());
        }
        return arr;
    }

    public void resetForm() {
        this.txtMaSp.setText("");
        this.txtTenSp.setText("");
        String[] arr = {"Chọn sản phẩm"};
        this.cbxCauhinh.setArr(arr);
        this.txtDongia.setText("");
        this.txtSoLuong.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnAddSp) {
            addCtPhieu();
        } else if (source == btnDelete) {
            int index = tablePhieuNhap.getSelectedRow();
            chitietphieu.remove(index);
            actionbtn("add");
            loadDataTableChiTietPhieu(chitietphieu);
            resetForm();
        } else if (source == btnEditSP) {
            ChiTietPhieuDTO ctphieu = getInfoChiTietPhieu();
            if (ctphieu == null) return;
            chitietphieu.set(rowPhieuSelect, ctphieu);
            loadDataTableChiTietPhieu(chitietphieu);
            Notification notification = new Notification(m, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, "Sửa sản phẩm thành công!");
            notification.showNotification();
            resetForm();
            actionbtn("add");
        } else if (source == btnNhapHang) {
            eventBtnNhapHang();
        }
    }

    public void eventBtnNhapHang() {
        if (chitietphieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong phiếu!", "Cảnh báo!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int input = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn tạo phiếu nhập?", "Xác nhận tạo phiếu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (input == 0) {
            try {
                int mancc = nccBus.getByIndex(cbxNhaCungCap.getSelectedIndex()).getMancc();
                long now = System.currentTimeMillis();
                Timestamp currenTime = new Timestamp(now);
                PhieuNhapDTO pn = new PhieuNhapDTO(mancc, maphieunhap, nvDto.getManv(), currenTime, phieunhapBus.getTongTien(chitietphieu), 1);
                boolean result = phieunhapBus.add(pn, chitietphieu);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Nhập hàng thành công!");
                    PhieuNhap pnlPhieu = new PhieuNhap(m, nvDto);
                    m.setPanel(pnlPhieu);
                } else {
                    JOptionPane.showMessageDialog(this, "Nhập hàng không thành công! Vui lòng kiểm tra dữ liệu.", "Cảnh báo!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Cảnh báo!", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public boolean validateInput() {
        String soluong = txtSoLuong.getText();
        String dongia = txtDongia.getText();
        if (soluong.isEmpty() || !Validation.isNumber(soluong) || Integer.parseInt(soluong) <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dongia.isEmpty() || !Validation.isNumber(dongia) || Integer.parseInt(dongia) <= 0) {
            JOptionPane.showMessageDialog(this, "Giá nhập không hợp lệ!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (cbxCauhinh.cbb.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấu hình sản phẩm!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtMaSp.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
