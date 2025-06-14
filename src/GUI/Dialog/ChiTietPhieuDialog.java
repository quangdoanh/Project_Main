package GUI.Dialog;

import BUS.PhienBanSanPhamBUS;
import BUS.PhieuNhapBUS;
import BUS.PhieuXuatBUS;
import DAO.*;
import DTO.*;
import GUI.Component.ButtonCustom;
import GUI.Component.HeaderTitle;
import GUI.Component.InputForm;
import helper.Formater;
import helper.writePDF;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public final class ChiTietPhieuDialog extends JDialog implements ActionListener {

    HeaderTitle titlePage;
    JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn;
    InputForm txtMaPhieu, txtNhanVien, txtNhaCungCap, txtThoiGian;
    DefaultTableModel tblModel;
    JTable table;
    JScrollPane scrollTable;

    PhieuNhapDTO phieunhap;
    PhieuXuatDTO phieuxuat;
    PhienBanSanPhamBUS phienbanBus = new PhienBanSanPhamBUS();
    PhieuNhapBUS phieunhapBus;
    PhieuXuatBUS phieuxuatBus;

    ButtonCustom btnPdf, btnHuyBo;

    ArrayList<ChiTietPhieuDTO> chitietphieu;

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuNhapDTO phieunhapDTO) {
        super(owner, title, modal);
        this.phieunhap = phieunhapDTO;
        phieunhapBus = new PhieuNhapBUS();
        chitietphieu = phieunhapBus.getChiTietPhieu_Type(phieunhapDTO.getMaphieu());
        initComponent(title);
        initPhieuNhap();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public ChiTietPhieuDialog(JFrame owner, String title, boolean modal, PhieuXuatDTO phieuxuatDTO) {
        super(owner, title, modal);
        this.phieuxuat = phieuxuatDTO;
        phieuxuatBus = new PhieuXuatBUS();
        chitietphieu = phieuxuatBus.selectCTP(phieuxuatDTO.getMaphieu());
        initComponent(title);
        initPhieuXuat();
        loadDataTableChiTietPhieu(chitietphieu);
        this.setVisible(true);
    }

    public void initPhieuNhap() {
        txtMaPhieu.setText("PN" + Integer.toString(this.phieunhap.getMaphieu()));
        txtNhaCungCap.setText(NhaCungCapDAO.getInstance().selectById(phieunhap.getManhacungcap() + "").getTenncc());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieunhap.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieunhap.getThoigiantao()));
    }

    public void initPhieuXuat() {
        txtMaPhieu.setText("PX" + Integer.toString(this.phieuxuat.getMaphieu()));
        txtNhaCungCap.setTitle("Khách hàng");
        txtNhaCungCap.setText(KhachHangDAO.getInstance().selectById(phieuxuat.getMakh() + "").getHoten());
        txtNhanVien.setText(NhanVienDAO.getInstance().selectById(phieuxuat.getManguoitao() + "").getHoten());
        txtThoiGian.setText(Formater.FormatTime(phieuxuat.getThoigiantao()));
    }

    public void loadDataTableChiTietPhieu(ArrayList<ChiTietPhieuDTO> ctPhieu) {
        tblModel.setRowCount(0);
        int size = ctPhieu.size();
        for (int i = 0; i < size; i++) {
            PhienBanSanPhamDTO pb = phienbanBus.getByMaPhienBan(ctPhieu.get(i).getMaphienbansp());
            tblModel.addRow(new Object[]{
                    i + 1,
                    pb.getMasp(),
                    SanPhamDAO.getInstance().selectById(pb.getMasp() + "").getTensp(),
                    DungLuongRamDAO.getInstance().selectById(pb.getRam() + "").getDungluongram() + "GB",
                    DungLuongRomDAO.getInstance().selectById(pb.getRom() + "").getDungluongrom() + "GB",
                    MauSacDAO.getInstance().selectById(pb.getMausac() + "").getTenmau(),
                    Formater.FormatVND(ctPhieu.get(i).getDongia()),
                    ctPhieu.get(i).getSoluong()
            });
        }
    }

    public void initComponent(String title) {
        this.setSize(new Dimension(900, 500)); // Reduced width since IMEI table is removed
        this.setLayout(new BorderLayout(0, 0));
        titlePage = new HeaderTitle(title.toUpperCase());

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(1, 4, 5, 5));
        txtMaPhieu = new InputForm("Mã phiếu");
        txtNhanVien = new InputForm("Nhân viên nhập");
        txtNhaCungCap = new InputForm("Nhà cung cấp");
        txtThoiGian = new InputForm("Thời gian tạo");

        txtMaPhieu.setEditable(false);
        txtNhanVien.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtThoiGian.setEditable(false);

        pnmain_top.add(txtMaPhieu);
        pnmain_top.add(txtNhanVien);
        pnmain_top.add(txtNhaCungCap);
        pnmain_top.add(txtThoiGian);

        pnmain_bottom = new JPanel(new BorderLayout(5, 5));
        pnmain_bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnmain_bottom.setBackground(Color.WHITE);

        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"STT", "Mã SP", "Tên SP", "RAM", "ROM", "Màu sắc", "Đơn giá", "Số lượng"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        scrollTable.setViewportView(table);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        pnmain_bottom.add(scrollTable, BorderLayout.CENTER);

        pnmain_btn = new JPanel(new FlowLayout());
        pnmain_btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        pnmain_btn.setBackground(Color.WHITE);
        btnPdf = new ButtonCustom("Xuất file PDF", "success", 14);
        btnHuyBo = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnPdf.addActionListener(this);
        btnHuyBo.addActionListener(this);
        pnmain_btn.add(btnPdf);
        pnmain_btn.add(btnHuyBo);

        pnmain.add(pnmain_top, BorderLayout.NORTH);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);
        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(titlePage, BorderLayout.NORTH);
        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnHuyBo) {
            dispose();
        } else if (source == btnPdf) {
            writePDF w = new writePDF();
            if (this.phieuxuat != null) {
                w.writePX(phieuxuat.getMaphieu());
            } else if (this.phieunhap != null) {
                w.writePN(phieunhap.getMaphieu());
            }
        }
    }
}