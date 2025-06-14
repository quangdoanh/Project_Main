package GUI.Component;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Tran Nhat Sinh
 */
public class HeaderTitle extends JPanel{
    private JLabel lblTitle;
    
    public void initComponent(String title) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(22, 122, 198));
        this.setPreferredSize(new Dimension(400, 60));
        
        
        lblTitle = new JLabel();
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, 1, 18));
        lblTitle.setForeground(new Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setText(title);
        lblTitle.setPreferredSize(new Dimension(400, 50));
        this.add(lblTitle, BorderLayout.CENTER);
    }
    
    public HeaderTitle(String title) {
        initComponent(title);
    }
}
