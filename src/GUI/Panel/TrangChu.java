package GUI.Panel;

import com.formdev.flatlaf.FlatIntelliJLaf;
import GUI.Component.PanelShadow;

import javax.swing.*;
import java.awt.*;

public class TrangChu extends JPanel {

    private JPanel topPanel, centerPanel;
    private PanelShadow[] featureCards;

    // Dữ liệu hiển thị
    private final String[][] features = {
            {
                    "Tính chính xác",
                    "tinhchinhxac_128px.svg",
                    "<html>Mã sản phẩm là một số duy nhất được <br>gán cho từng thiết bị laptop, do đó hệ <br>thống quản lý laptop theo mã sản phẩm <br>sẽ đảm bảo tính chính xác và độ tin cậy<br>cao.</html>"
            },
            {
                    "Tính bảo mật",
                    "tinhbaomat_128px.svg",
                    "<html>Ngăn chặn việc sử dụng các thiết bị<br>laptop giả mạo hoặc bị đánh cắp.<br>Điều này giúp tăng tính bảo mật cho<br>các hoạt động quản lý laptop.</html>"
            },
            {
                    "Tính hiệu quả",
                    "tinhhieuqua_128px.svg",
                    "<html>Dễ dàng xác định được thông tin<br>về từng thiết bị laptop một cách<br>nhanh chóng và chính xác, giúp<br>cho việc quản lý laptop được<br>thực hiện một cách hiệu quả hơn.</html>"
            }
    };

    // Màu sắc giao diện
    private final Color mainColor = Color.WHITE;
    private final Color backgroundColor = new Color(240, 247, 250);

    public TrangChu() {
        initLookAndFeel();
        initComponent();
    }

    private void initLookAndFeel() {
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        setBackground(new Color(24, 24, 24));

        createTopPanel();
        createCenterPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void createTopPanel() {
        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1100, 200));
        topPanel.setBackground(mainColor);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JLabel sloganLabel = new JLabel();
        sloganLabel.setIcon(new ImageIcon(getClass().getResource("/img/slogan1.png")));
        topPanel.add(sloganLabel);
    }

    private void createCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(1100, 800));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));

        featureCards = new PanelShadow[features.length];
        for (int i = 0; i < features.length; i++) {
            featureCards[i] = new PanelShadow(features[i][1], features[i][0], features[i][2]);
            centerPanel.add(featureCards[i]);
        }
    }
}
