package GUI.Panels;

import BUS.KhachHangBUS;
import DTO.OBJECTS.KhachHangDTO;
import GUI.Components.JTableExcel;
import GUI.Components.Mainfunction;
import GUI.Components.SearchFunction;
import GUI.Dialogs.KhachHangDialog;
import GUI.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class KhachHangPanel extends JPanel implements ActionListener, ItemListener {

    // ActionListener :  lắng nghe các sự kiện hành động
    // ItemListener   :  thay đổi trạng thái

    JPanel contentTable, functionBar ;
//    JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);

    //  Table
    JTable tableKhachHang;
    DefaultTableModel tblModel;
    JScrollPane scrollTableKhachHang;

    public KhachHangBUS khachhangBUS = new KhachHangBUS();
    public ArrayList<KhachHangDTO> listkh = khachhangBUS.getAll(); // lấy dữ liệu ds db qua bus
    //Components
    // MainFunction
    Mainfunction mainFunction;
    //  Search
    SearchFunction search;

    Main m;

    Color BackgroundColor = new Color(240, 247, 250);

    public KhachHangPanel(Main m){
        this.m = m ;
        initComponent();
        loadDataTable(listkh);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0 ,0));
        this.setOpaque(true);

        tblModel = new DefaultTableModel();
        tableKhachHang = new JTable();
        scrollTableKhachHang = new JScrollPane() ;
        String[] header = new String[]{"Mã khách hàng", "Tên khách hàng", "Địa chỉ", "Số điện thoại", "Ngày tham gia"};
        tblModel.setColumnIdentifiers(header);
        tableKhachHang.setModel(tblModel); // thiết lập cấu trúc và đối tượng
        //tableKhachHang.setFocusable(false);

        DefaultTableCellRenderer cell = new DefaultTableCellRenderer();
        cell.setHorizontalAlignment(JLabel.CENTER);
        tableKhachHang.getColumnModel().getColumn(0).setCellRenderer(cell);
        tableKhachHang.getColumnModel().getColumn(1).setCellRenderer(cell);
        tableKhachHang.getColumnModel().getColumn(2).setCellRenderer(cell);
        tableKhachHang.getColumnModel().getColumn(3).setCellRenderer(cell);
        tableKhachHang.getColumnModel().getColumn(4).setCellRenderer(cell);
        tableKhachHang.setDefaultEditor(Object.class, null); // không cho chỉnh sửa
//        tableKhachHang.setAutoCreateRowSorter(true); tự động sắp xếp
        scrollTableKhachHang.setViewportView(tableKhachHang);


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
        String[] str = {"Tất cả", "Mã khách hàng", "Tên khách hàng", "Địa chỉ", "Số điện thoại"};
        search = new SearchFunction(str);

        search.cbxChoose.addItemListener(this); // đăng ký lắng nghe Change

        // Khi dữ nguyên combox và thay đổi input search
        search.txtSearchForm.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e) {
                String type = (String) search.cbxChoose.getSelectedItem();
                String txt = search.txtSearchForm.getText();
                System.out.println("tim kiem"+ txt);
                listkh = khachhangBUS.search(txt, type); // tìm kiếm khách hàng trả về 1 mảng
                System.out.println("kq tk"+ listkh);
                loadDataTable(listkh);
            }
        });

        search.btnReset.addActionListener((ActionEvent e) -> {
            search.txtSearchForm.setText("");
            search.cbxChoose.setSelectedIndex(0);
            listkh = khachhangBUS.getAll();
            loadDataTable(listkh);
        });

        // end search
        functionBar.add(search);

        this.add(functionBar,BorderLayout.NORTH);

        // Bảng hiển thị
        contentTable = new JPanel();
        contentTable.setBackground(Color.GREEN);
        contentTable.setLayout(new BoxLayout(contentTable, BoxLayout.Y_AXIS));
        contentTable.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentTable.add(scrollTableKhachHang);

        this.add(contentTable, BorderLayout.CENTER);

    }

    public  void loadDataTable(ArrayList<KhachHangDTO> result){
            tblModel.setRowCount(0);
            for (KhachHangDTO kh : result){
                tblModel.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoten(),
                        kh.getDiachi(),
                        kh.getSdt(),
                        kh.getNgaythamgia()
                });
            }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            System.out.println("ok");

            KhachHangDialog khDialog = new KhachHangDialog(this, null, "Thêm khách hàng", true, "create");
        }else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = tableKhachHang.getSelectedRow();
            if (index != -1) {
                KhachHangDialog khDialog = new KhachHangDialog(this,  null, "Chỉnh sửa khách hàng", true, "update", listkh.get(index));
            }else{
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if(e.getSource() == mainFunction.btn.get("delete")){
            int index = tableKhachHang.getSelectedRow();
            if (index != -1) {
                int result  = JOptionPane.showConfirmDialog(null,
                        "Bạn có chắc chắn muốn xóa khách hàng ?", "Xóa khách hàng",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

               if(result == JOptionPane.OK_OPTION){
                   khachhangBUS.delete(listkh.get(index));
                   loadDataTable(listkh);
                   System.out.println("Xóa thành cong" );
               }
            }else{
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = tableKhachHang.getSelectedRow();
            if (index != -1) {
                KhachHangDialog khDialog = new KhachHangDialog(this,  null, "Chi tiết khách hàng", true, "view", listkh.get(index));
            }else{
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng");
            }
        }else if(e.getSource() == mainFunction.btn.get("export")){
            JTableExcel.exportJTableExcel(tableKhachHang);
        }else if(e.getSource() == mainFunction.btn.get("import")){
            JTableExcel.importJTableExcelKH(khachhangBUS);
            loadDataTable(listkh);
        }
//        System.out.println(e.getSource());
        System.out.println("Nút"+e.getActionCommand());
        System.out.println("-------------");
        System.out.println(mainFunction.btn.get("create"));
    }

    // khi combox thay đôi và input search giữ nguyên
    @Override
    public void itemStateChanged(ItemEvent e) {
        String type = (String) search.cbxChoose.getSelectedItem();
        String txt = search.txtSearchForm.getText();
        System.out.println("tim kiem"+ txt);
        listkh = khachhangBUS.search(txt, type); // tìm kiếm khách hàng trả về 1 mảng mới
        System.out.println("kq tk"+ listkh);
        loadDataTable(listkh);
    }
}
