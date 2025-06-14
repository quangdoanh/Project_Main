package GUI;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import DAO.TaiKhoanDAO;
import helper.BcryptPassword;
import DTO.TaiKhoanDTO;
import GUI.Component.InputForm;
import GUI.Dialog.QuenMatKhau;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log_In extends JFrame implements KeyListener {

    JPanel pnlMain, pnlLogIn;
    JLabel lblImage, lbl3, lbl6, lbl7;
    InputForm txtUsername, txtPassword;
    Color FontColor = new Color(96, 125, 139);

    public Log_In() {
        initComponent();
        txtUsername.setText("vietdeptrai");
        txtPassword.setPass("Viet2k4dz@");
    }

    private void initComponent() {
        this.setSize(new Dimension(1000, 500));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("Đăng nhập");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame jf = this;

        imgIntro();

        pnlMain = new JPanel();
        pnlMain.setBackground(Color.white);
        pnlMain.setBorder(new EmptyBorder(20, 0, 0, 0));
        pnlMain.setPreferredSize(new Dimension(500, 740));
        pnlMain.setLayout(new FlowLayout(1, 0, 10));

        lbl3 = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lbl3.setFont(new Font(FlatRobotoFont.FAMILY_SEMIBOLD, Font.BOLD, 20));
        pnlMain.add(lbl3);

        JPanel paneldn = new JPanel();
        paneldn.setBackground(Color.WHITE); // Changed to white for consistency
        paneldn.setPreferredSize(new Dimension(400, 200));
        paneldn.setLayout(new GridLayout(2, 1));

        txtUsername = new InputForm("Tên đăng nhập");
        paneldn.add(txtUsername);
        txtPassword = new InputForm("Mật khẩu", "password");
        paneldn.add(txtPassword);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        pnlMain.add(paneldn);

        lbl6 = new JLabel("ĐĂNG NHẬP");
        lbl6.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        lbl6.setForeground(Color.white);

        pnlLogIn = new JPanel();
        pnlLogIn.putClientProperty(FlatClientProperties.STYLE, "arc: 99");
        pnlLogIn.setBackground(Color.BLACK);
        pnlLogIn.setPreferredSize(new Dimension(380, 45));
        pnlLogIn.setLayout(new FlowLayout(1, 0, 15));

        pnlLogIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                pnlLogInMouseEntered(evt);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                try {
                    pnlLogInMousePressed(evt);
                } catch (Exception ex) {
                    Logger.getLogger(Log_In.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Log_In.this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                pnlLogInMouseExited(evt);
            }
        });
        pnlLogIn.add(lbl6);

        lbl7 = new JLabel("Quên mật khẩu", JLabel.RIGHT);
        lbl7.setPreferredSize(new Dimension(380, 50));
        lbl7.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 18));
        lbl7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                QuenMatKhau qmk = new QuenMatKhau(jf, true);
                qmk.setVisible(true);
            }
        });
        pnlMain.add(lbl7);

        pnlMain.add(pnlLogIn);

        this.add(pnlMain, BorderLayout.EAST);
    }

    public void checkLogin() throws Exception {
        String usernameCheck = txtUsername.getText().trim();
        String passwordCheck = txtPassword.getPass().trim();

        // Input validation
        if (usernameCheck.isEmpty() || passwordCheck.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (usernameCheck.length() > 50) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được vượt quá 50 ký tự", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }
//        if (passwordCheck.length() < 8) {
//            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 8 ký tự", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
//            return;
//        }

        TaiKhoanDTO tk = TaiKhoanDAO.getInstance().selectByUser(usernameCheck);
        if (tk == null) {
            JOptionPane.showMessageDialog(this, "Tài khoản của bạn không tồn tại trên hệ thống", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tk.getTrangthai() == 0) {
            JOptionPane.showMessageDialog(this, "Tài khoản của bạn đang bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (BcryptPassword.checkPassword(passwordCheck, tk.getMatkhau())) {
            this.dispose();
            Main main = new Main(tk);
            main.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Mật khẩu không khớp", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pnlLogInMousePressed(MouseEvent evt) throws Exception {
        checkLogin();
    }

    private void pnlLogInMouseEntered(MouseEvent evt) {
        pnlLogIn.setBackground(FontColor);
        lbl6.setForeground(Color.black); // Update label color for consistency
    }

    private void pnlLogInMouseExited(MouseEvent evt) {
        pnlLogIn.setBackground(Color.BLACK);
        lbl6.setForeground(Color.white); // Update label color for consistency
    }

    public void imgIntro() {
        JPanel bo = new JPanel();
        bo.setBorder(new EmptyBorder(3, 10, 5, 5));
        bo.setPreferredSize(new Dimension(500, 740));
        bo.setBackground(Color.white);
        this.add(bo, BorderLayout.WEST);

        lblImage = new JLabel();
        lblImage.setIcon(new FlatSVGIcon("./img/login-image.svg"));
        bo.add(lblImage);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                checkLogin();
            } catch (Exception ex) {
                Logger.getLogger(Log_In.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);
        Log_In login = new Log_In();
        login.setVisible(true);
    }
}