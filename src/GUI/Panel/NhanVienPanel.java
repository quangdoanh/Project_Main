package GUI.Panels;

import BUS.NhanVienBUS;
import DTO.OBJECTS.KhachHangDTO;
import DTO.OBJECTS.NhanVienDTO;
import GUI.Components.JTableExcel;
import GUI.Components.Mainfunction;
import GUI.Components.SearchFunction;
import GUI.Dialogs.KhachHangDialog;
import GUI.Dialogs.NhanVienDialog;
import GUI.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NhanVienPanel extends JPanel implements ActionListener, ItemListener {

    Main m;
    public NhanVienBUS nvBus = new NhanVienBUS();
    public ArrayList<NhanVienDTO> listnv = nvBus.getAll();

    JPanel contentTable, functionBar;
    Mainfunction mainFunction;

    JTable tableNhanVien;
    DefaultTableModel tblModel;
    JScrollPane scrollTableNhanVien;

    SearchFunction search;

    Color BackgroundColor = new Color(240, 247, 250);
    public NhanVienPanel(Main m){
        this.m = m ;
        initComponent();
        loadDataTable(listnv);
    }

    private void initComponent(){
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0 ,0));
        this.setOpaque(true); // vẽ màu nền bg

        tblModel = new DefaultTableModel();
        tableNhanVien = new JTable();
        scrollTableNhanVien = new JScrollPane();
        String header[] =  new String[]{"Mã nhân viên", "Họ tên", "Giới tính", "Ngày Sinh", "SDT", "Email"};
        tblModel.setColumnIdentifiers(header);
        tableNhanVien.setModel(tblModel);
        scrollTableNhanVien.setViewportView(tableNhanVien);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableNhanVien.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tableNhanVien.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        m.add(scrollTableNhanVien);

        // Function
        functionBar = new JPanel();

        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0)); // main function & search
        functionBar.setBackground(Color.YELLOW);
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10)); // padding

        // Mainfunction
        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new Mainfunction(m.user.getManhomquyen(),"khachhang",action); // thêm các button
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this); // đăng ký sự kiện hành động
        }
        functionBar.add(mainFunction);

        // Search
        String[] str = {"Tất cả", "Mã nhân viên", "Email", "Họ tên"};
        search = new SearchFunction(str);

        search.cbxChoose.addItemListener(this); // đăng ký lắng nghe Change

        // Khi dữ nguyên combox và Thay Đổi Input search
        search.txtSearchForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                System.out.println("tim kiem"+ txt);
                listnv = nvBus.search(txt, type); // tìm kiếm khách hàng trả về 1 mảng mới
                System.out.println("kq tk"+ listnv);
                loadDataTable(listnv);
            }
        });

        // Button lam moi
        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            search.cbxChoose.setSelectedIndex(0); // Chon tat ca
            listnv = nvBus.getAll();
            loadDataTable(listnv);
        });

        // end search
        functionBar.add(search);

        this.add(functionBar,BorderLayout.NORTH);

        // Bảng hiển thị
        contentTable = new JPanel();
        contentTable.setBackground(Color.GREEN);
        contentTable.setLayout(new BoxLayout(contentTable, BoxLayout.Y_AXIS));
        contentTable.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentTable.add(scrollTableNhanVien);

        this.add(contentTable, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<NhanVienDTO> list) {
        System.out.println("sau khi load"+list);
        tblModel.setRowCount(0);
        for (NhanVienDTO nhanVien : list) {
            String ngaysinhInput = nhanVien.getNgaysinh();
            String day = ngaysinhInput.substring(0, 2);
            String month = ngaysinhInput.substring(2, 4);
            String year = ngaysinhInput.substring(4, 8);
            String ngaysinhOutput = year + "-" + month + "-" + day;
            tblModel.addRow(new Object[]{
                    nhanVien.getManv(),
                    nhanVien.getHoten(),
                    nhanVien.getGioitinh() == 1 ? "Nam" : "Nữ",
                    ngaysinhOutput,
                    nhanVien.getSdt(),
                    nhanVien.getEmail()
            });
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mainFunction.btn.get("create")){
            System.out.println("Chạy vào đây");
            NhanVienDialog khDialog = new NhanVienDialog(this, null, "Thêm nhân viên", true, "create");
        }else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = tableNhanVien.getSelectedRow();
            if (index != -1) {
                NhanVienDialog khDialog = new NhanVienDialog(this, null, "Chỉnh sửa nhân viên", true, "update", listnv.get(index));
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if(e.getSource() == mainFunction.btn.get("delete")) {
            int index = tableNhanVien.getSelectedRow();
            if (index != -1) {
                int result = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa nhan vien ?", "Xóa nhan vien",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    nvBus.delete(listnv.get(index));
                    loadDataTable(listnv);
                    System.out.println("Xóa thành cong");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = tableNhanVien.getSelectedRow();
            if (index != -1) {
                NhanVienDialog khDialog = new NhanVienDialog(this,  null, "Chi tiết nhan vien", true, "view", listnv.get(index));
            }else{
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if(e.getSource() == mainFunction.btn.get("export")) {
                JTableExcel.exportJTableExcel(tableNhanVien);
        }else if(e.getSource() == mainFunction.btn.get("import")){
            JTableExcel.importJTableExcelNV(nvBus);
            loadDataTable(listnv);
        }
    }

    // Khi dữ nguyên input và Thay Đổi Input ComboBox
    @Override
    public void itemStateChanged(ItemEvent e) {
        // chỉ dùng cho JComboBox----CheckBox --- JRadioButton
            String type = (String) search.cbxChoose.getSelectedItem();
            String text = search.txtSearchForm.getText();
            System.out.println("tim kiem"+ text);
            listnv = nvBus.search(text,type);
            System.out.println("kq tk"+ listnv);
            loadDataTable(listnv);
    }
}
