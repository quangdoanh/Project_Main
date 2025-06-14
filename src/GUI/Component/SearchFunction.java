package GUI.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SearchFunction extends JPanel {
    public JComboBox<String> cbxChoose;
    public JButton btnReset;
    public JTextField txtSearchForm;

    public SearchFunction(String str[]) {
        initComponent(str);
    }

    private  void initComponent(String str[]){
        this.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        JPanel  jpSearch = new JPanel(new BorderLayout(5, 10));
        jpSearch.setBorder(new EmptyBorder(18,15,18,15));
        jpSearch.setBackground(Color.WHITE);
        // Combobox
        cbxChoose = new JComboBox();
        cbxChoose.setModel(new DefaultComboBoxModel<>(str));
        cbxChoose.setPreferredSize(new Dimension(140, 0));
        jpSearch.add(cbxChoose,BorderLayout.WEST);
        // TextField
        txtSearchForm = new JTextField("Nhập nội dung tìm kiếm...");
        jpSearch.add(txtSearchForm,BorderLayout.CENTER);
        //Button
        btnReset = new JButton("Làm mới");
        btnReset.setPreferredSize(new Dimension(125, 0));
        jpSearch.add(btnReset,BorderLayout.EAST);

        this.add(jpSearch);
    }


}
