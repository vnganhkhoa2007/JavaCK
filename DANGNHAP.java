package Java;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class DANGNHAP extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JRadioButton rdoAdmin, rdoUser;

    private final String DB_URL = "jdbc:sqlserver://MSI:1433;databaseName=QLCAPTHUOC;integratedSecurity=true;encrypt=false;";
    private final String EMAIL_GUI = "lyran11119999@gmail.com"; 
    private final String MAT_KHAU_APP = "avluqfzserimvbge"; 

    public DANGNHAP() {
        setTitle("MEDICARE - HỆ THỐNG QUẢN LÝ");
        setSize(900, 580); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);	
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(44, 62, 80));
        leftPanel.setPreferredSize(new Dimension(400, 550));
        
        JLabel lblLogo = new JLabel("✚");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 100));
        lblLogo.setForeground(new Color(52, 152, 219));
        
        JLabel lblName = new JLabel("MEDICARE");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 35));
        lblName.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; leftPanel.add(lblLogo, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(10,0,0,0); leftPanel.add(lblName, gbc);

        JPanel rightPanel = new JPanel(null);
        rightPanel.setBackground(Color.WHITE);

        JLabel lblWelcome = new JLabel("Chào mừng trở lại!");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(44, 62, 80)); // Đảm bảo màu chữ hiển thị rõ
        lblWelcome.setBounds(60, 40, 300, 40);
        rightPanel.add(lblWelcome);

        rdoAdmin = new JRadioButton("Admin");
        rdoUser = new JRadioButton("User", true);
        rdoAdmin.setBackground(Color.WHITE); rdoUser.setBackground(Color.WHITE);
        rdoAdmin.setForeground(Color.DARK_GRAY); rdoUser.setForeground(Color.DARK_GRAY);
        ButtonGroup bg = new ButtonGroup(); bg.add(rdoAdmin); bg.add(rdoUser);
        rdoAdmin.setBounds(60, 95, 80, 30); rdoUser.setBounds(150, 95, 120, 30);
        rightPanel.add(rdoAdmin); rightPanel.add(rdoUser);

        txtUser = createStyledTextField("Họ tên / Username", 140, rightPanel);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(60, 215, 360, 45);
        // Sửa lỗi đè màu chữ tiêu đề viền bằng cách đặt rõ màu và font
        txtPass.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "ID / Mật khẩu", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.PLAIN, 12), Color.DARK_GRAY
        ));
        rightPanel.add(txtPass);

        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(60, 290, 360, 50);
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setOpaque(true);          // KHẮC PHỤC ĐÈ MÀU CHỮ TRÊN WINDOWS
        btnLogin.setBorderPainted(false);  // KHẮC PHỤC ĐÈ MÀU CHỮ TRÊN WINDOWS
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnLogin);

        JButton btnRegisterAdmin = new JButton("Đăng ký tài khoản Admin");
        btnRegisterAdmin.setBounds(60, 360, 180, 30);
        btnRegisterAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRegisterAdmin.setForeground(new Color(41, 128, 185));
        btnRegisterAdmin.setContentAreaFilled(false);
        btnRegisterAdmin.setBorderPainted(false);
        btnRegisterAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnRegisterAdmin);

        JButton btnForgotPassword = new JButton("Quên mật khẩu Admin?");
        btnForgotPassword.setBounds(240, 360, 180, 30);
        btnForgotPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnForgotPassword.setForeground(new Color(192, 57, 43));
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(btnForgotPassword);

        btnLogin.addActionListener(e -> xuLyDangNhap());
        btnRegisterAdmin.addActionListener(e -> hienThiFormDangKyAdmin());
        btnForgotPassword.addActionListener(e -> hienThiFormNhapEmail());

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JTextField createStyledTextField(String title, int y, JPanel p) {
        JTextField f = new JTextField();
        f.setBounds(60, y, 360, 45);
        // Sửa lỗi đè màu tiêu đề chữ của TextField
        f.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            title, 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Segoe UI", Font.PLAIN, 12), Color.DARK_GRAY
        ));
        p.add(f);
        return f;
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = String.valueOf(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (rdoAdmin.isSelected()) {
                    String sql = "SELECT * FROM ADMIN_ACC WHERE username = ? AND password = ?";
                    try (PreparedStatement st = conn.prepareStatement(sql)) {
                    	st.setString(1, user);
                    	st.setString(2, HashUtil.hash(pass));
                        try (ResultSet rs = st.executeQuery()) {
                            if (rs.next()) {
                                JOptionPane.showMessageDialog(this, "Đăng nhập với quyền Admin thành công!");
                                this.dispose(); 
                                new TRANGCHON_ADMIN();
                            } else {
                                JOptionPane.showMessageDialog(this, "Sai tài khoản, mật khẩu hoặc tài khoản Admin chưa đăng ký!");
                            }
                        }
                    }
                } else {
                    String sql = "SELECT * FROM NGUOIDUNG WHERE hoten = ? AND id = ?";
                    try (PreparedStatement st = conn.prepareStatement(sql)) {
                    	st.setString(1, user);
                    	st.setString(2, pass);
                    	
                        System.out.println("User = " + user);
                        System.out.println("Pass = " + pass);
                        try (ResultSet rs = st.executeQuery()) {
                        	if (rs.next()) {

                        	    String id = rs.getString("id");

                        	    JOptionPane.showMessageDialog(this,
                        	            "Đăng nhập Người dùng thành công!");

                        	    this.dispose();
                        	    new TRANG_NGUOIDUNG(id);
                        	} else {
                                JOptionPane.showMessageDialog(this, "Sai thông tin tài khoản người dùng!");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
            ex.printStackTrace();
        }
    }

    private void hienThiFormDangKyAdmin() {
        JDialog regDialog = new JDialog(this, "Đăng ký tài khoản Admin mới", true);
        regDialog.setSize(400, 360);
        regDialog.setLocationRelativeTo(this);
        regDialog.setLayout(null);

        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN ADMIN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBounds(50, 15, 300, 30);
        regDialog.add(lblTitle);

        JTextField txtRegUser = new JTextField();
        txtRegUser.setBounds(50, 60, 300, 45);
        txtRegUser.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Tên đăng nhập (Username)", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        regDialog.add(txtRegUser);

        JPasswordField txtRegPass = new JPasswordField();
        txtRegPass.setBounds(50, 120, 300, 45);
        txtRegPass.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Mật khẩu", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        regDialog.add(txtRegPass);

        JTextField txtRegEmail = new JTextField();
        txtRegEmail.setBounds(50, 180, 300, 45);
        txtRegEmail.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Email liên kết nhận OTP", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        regDialog.add(txtRegEmail);

        JButton btnSubmit = new JButton("ĐĂNG KÝ HỆ THỐNG");
        btnSubmit.setBounds(50, 250, 300, 45);
        btnSubmit.setBackground(new Color(46, 204, 113));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setOpaque(true);
        btnSubmit.setBorderPainted(false);
        regDialog.add(btnSubmit);

        btnSubmit.addActionListener(e -> {
            String user = txtRegUser.getText().trim();
            String pass = String.valueOf(txtRegPass.getPassword()).trim();
            String email = txtRegEmail.getText().trim();

            if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(regDialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(regDialog, "Định dạng Email không hợp lệ!");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO ADMIN_ACC (username, password, email) VALUES (?, ?, ?)";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                	st.setString(1, user);
                	st.setString(2, HashUtil.hash(pass));
                	st.setString(3, email);
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể dùng tài khoản này để đăng nhập.");
                    regDialog.dispose();
                }
            }catch (SQLException ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(
                    regDialog,
                    ex.getMessage()
                );
            }
        });

        regDialog.setVisible(true);
    }

    private void hienThiFormNhapEmail() {
        JDialog emailDialog = new JDialog(this, "Quên mật khẩu - Bước 1", true);
        emailDialog.setSize(400, 230);
        emailDialog.setLocationRelativeTo(this);
        emailDialog.setLayout(null);

        JLabel lblTitle = new JLabel("KHÔI PHỤC MẬT KHẨU ADMIN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setBounds(50, 15, 300, 30);
        emailDialog.add(lblTitle);

        JTextField txtEmailInput = new JTextField();
        txtEmailInput.setBounds(50, 60, 300, 45);
        txtEmailInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Nhập Email đã đăng ký", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        emailDialog.add(txtEmailInput);

        JButton btnNext = new JButton("GỬI MÃ XÁC THỰC OTP");
        btnNext.setBounds(50, 125, 300, 40);
        btnNext.setBackground(new Color(52, 152, 219));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNext.setOpaque(true);
        btnNext.setBorderPainted(false);
        emailDialog.add(btnNext);

        btnNext.addActionListener(e -> {
            String emailInput = txtEmailInput.getText().trim();
            if (emailInput.isEmpty()) {
                JOptionPane.showMessageDialog(emailDialog, "Vui lòng nhập Email!");
                return;
            }

            boolean exists = false;
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "SELECT username FROM ADMIN_ACC WHERE email = ?";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    st.setString(1, emailInput);
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) exists = true;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (!exists) {
                JOptionPane.showMessageDialog(emailDialog, "Email này chưa được đăng ký làm tài khoản Admin!");
                return;
            }

            String generatedOTP = String.format("%06d", new Random().nextInt(999999));
            emailDialog.dispose();

            ProgressMonitor pm = new ProgressMonitor(this, "Đang gửi mã bảo mật đến Gmail của bạn...", "", 0, 100);
            pm.setMillisToDecideToPopup(0);
            pm.setProgress(30);

            new Thread(() -> {
                boolean success = thucHienGuiMail(emailInput, generatedOTP);
                pm.setProgress(100);

                if (success) {
                    SwingUtilities.invokeLater(() -> hienThiFormNhapOTP(emailInput, generatedOTP));
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Lỗi kết nối máy chủ Mail! Vui lòng thử lại sau."));
                }
            }).start();
        });

        emailDialog.setVisible(true);
    }

    private void hienThiFormNhapOTP(String emailTarget, String correctOTP) {
        JDialog otpDialog = new JDialog(this, "Quên mật khẩu - Bước 2", true);
        otpDialog.setSize(400, 230);
        otpDialog.setLocationRelativeTo(this);
        otpDialog.setLayout(null);

        JLabel lblTitle = new JLabel("XÁC THỰC MÃ BẢO MẬT OTP", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setBounds(50, 15, 300, 30);
        otpDialog.add(lblTitle);

        JTextField txtOTPInput = new JTextField();
        txtOTPInput.setBounds(50, 60, 300, 45);
        txtOTPInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Nhập mã OTP gồm 6 chữ số", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        otpDialog.add(txtOTPInput);

        JButton btnVerify = new JButton("XÁC NHẬN MÃ OTP");
        btnVerify.setBounds(50, 125, 300, 40);
        btnVerify.setBackground(new Color(46, 204, 113));
        btnVerify.setForeground(Color.WHITE);
        btnVerify.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerify.setOpaque(true);
        btnVerify.setBorderPainted(false);
        otpDialog.add(btnVerify);

        btnVerify.addActionListener(e -> {
            String otpInput = txtOTPInput.getText().trim();
            if (otpInput.equals(correctOTP)) {
                otpDialog.dispose();
                hienThiFormDatLaiMatKhau(emailTarget);
            } else {
                JOptionPane.showMessageDialog(otpDialog, "Mã OTP không chính xác! Vui lòng kiểm tra lại hộp thư.");
            }
        });

        otpDialog.setVisible(true);
    }

    private void hienThiFormDatLaiMatKhau(String emailTarget) {
        JDialog resetDialog = new JDialog(this, "Quên mật khẩu - Bước 3", true);
        resetDialog.setSize(400, 300);
        resetDialog.setLocationRelativeTo(this);
        resetDialog.setLayout(null);

        JLabel lblTitle = new JLabel("THIẾT LẬP MẬT KHẨU MỚI", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setBounds(50, 15, 300, 30);
        resetDialog.add(lblTitle);

        JPasswordField txtNewPass = new JPasswordField();
        txtNewPass.setBounds(50, 65, 300, 45);
        txtNewPass.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Mật khẩu mới", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        resetDialog.add(txtNewPass);

        JPasswordField txtConfirmPass = new JPasswordField();
        txtConfirmPass.setBounds(50, 125, 300, 45);
        txtConfirmPass.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Xác nhận lại mật khẩu", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
        resetDialog.add(txtConfirmPass);

        JButton btnConfirm = new JButton("XÁC NHẬN THAY ĐỔI");
        btnConfirm.setBounds(50, 195, 300, 45);
        btnConfirm.setBackground(new Color(230, 126, 34));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirm.setOpaque(true);
        btnConfirm.setBorderPainted(false);
        resetDialog.add(btnConfirm);

        btnConfirm.addActionListener(e -> {
            String newPass = String.valueOf(txtNewPass.getPassword()).trim();
            String confirmPass = String.valueOf(txtConfirmPass.getPassword()).trim();

            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(resetDialog, "Vui lòng nhập đầy đủ các trường!");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(resetDialog, "Mật khẩu xác nhận không trùng khớp!");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "UPDATE ADMIN_ACC SET password = ? WHERE email = ?";
                try (PreparedStatement st = conn.prepareStatement(sql)) {
                	st.setString(1, HashUtil.hash(newPass));
                	st.setString(2, emailTarget);
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công! Hệ thống quay lại màn hình đăng nhập.");
                    resetDialog.dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(resetDialog, "Lỗi cập nhật dữ liệu!");
                ex.printStackTrace();
            }
        });

        resetDialog.setVisible(true);
    }

    private boolean thucHienGuiMail(String emailNhan, String otp) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        // BỔ SUNG THÊM 2 DÒNG DƯỚI ĐÂY ĐỂ ÉP SỬ DỤNG GIAO THỨC TLSv1.2 / TLSv1.3 CHUẨN GOOGLE
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_GUI, MAT_KHAU_APP);
            }
        });
        
        // ... Giữ nguyên phần code gửi mail try-catch phía dưới ...
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_GUI));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailNhan));
            message.setSubject("MEDICARE SYSTEM - MA BAO MAT OTP");
            
            String htmlContent = "<h2>Hệ thống Y tế MEDICARE</h2>"
                    + "<p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản Admin.</p>"
                    + "<p>Mã bảo mật OTP của bạn là: <b style='font-size:24px; color:#e74c3c;'>" + otp + "</b></p>"
                    + "<p><i>Lưu ý: Mã có giá trị sử dụng một lần. Vui lòng không tiết lộ mã này.</i></p>";
            
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
        new DANGNHAP();
    }
}