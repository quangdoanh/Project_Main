package GUI.Components;

import javax.swing.*;
import java.awt.*;

public class ButtonFunction extends JButton {

    String action;

    public ButtonFunction(String text, String action){

        this.setBackground(Color.RED);
        this.action = action;
        this.setForeground(new Color(1, 88, 155));
        this.setText(text);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //this.setFocusable(false); // tắt sự kiện bằng bàn phím
//        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER); // Văn bản sẽ được căn giữa (ở giữa)
//        this.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

    }

    public String getBAction() {
        return this.action;
    }
}
