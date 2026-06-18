	
	package Java;
	
	import java.awt.*;
	import java.sql.*;
	import javax.swing.*;
	import javax.swing.border.*;
	import javax.swing.table.DefaultTableModel;
	
	public class TRANGCHON_ADMIN extends JFrame {
	
	    private JLabel lblTongBN;
	    private JLabel lblTongDon;
	    private JLabel lblDoanhThu;
	    private JLabel lblTaiKhoan;
	
	    private JTable table;
	
	    private final String dbUrl =
	            "jdbc:sqlserver://MSI:1433;databaseName=QLCAPTHUOC;integratedSecurity=true;encrypt=false;";
	
	    public TRANGCHON_ADMIN() {
	
	        setTitle("ADMIN DASHBOARD - MEDICARE");
	        setSize(1300, 750);
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new BorderLayout());
	
	        Color primary = new Color(44, 62, 80);
	        Color bg = new Color(245, 246, 250);
	
	        getContentPane().setBackground(bg);
	
	        // ================= HEADER =================
	
	        JPanel header = new JPanel(new BorderLayout());
	        header.setBackground(primary);
	        header.setPreferredSize(new Dimension(1300, 80));
	
	        JLabel title = new JLabel("   MEDICARE ADMIN DASHBOARD");
	        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
	        title.setForeground(Color.WHITE);
	
	        JLabel time = new JLabel();
	        time.setForeground(Color.WHITE);
	        time.setFont(new Font("Segoe UI", Font.BOLD, 18));
	
	        Timer timer = new Timer(1000, e -> {
	            java.text.SimpleDateFormat sdf =
	                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            time.setText(sdf.format(new java.util.Date()) + "   ");
	        });
	
	        timer.start();
	
	        header.add(title, BorderLayout.WEST);
	        header.add(time, BorderLayout.EAST);
	
	        add(header, BorderLayout.NORTH);
	
	        // ================= CENTER =================
	
	        JPanel centerPanel = new JPanel(new BorderLayout());
	        centerPanel.setBackground(bg);
	
	        // ======= CARDS ========
	
	        JPanel cards = new JPanel(new GridLayout(1, 4, 20, 20));
	        cards.setBorder(new EmptyBorder(20, 20, 20, 20));
	        cards.setBackground(bg);
	
	        lblTongBN = createCard(cards,
	                "TỔNG BỆNH NHÂN",
	                new Color(52, 152, 219));
	
	        lblTongDon = createCard(cards,
	                "TỔNG ĐƠN THUỐC",
	                new Color(46, 204, 113));
	
	        lblDoanhThu = createCard(cards,
	                "DOANH THU",
	                new Color(155, 89, 182));
	
	        lblTaiKhoan = createCard(cards,
	                "TÀI KHOẢN",
	                new Color(231, 76, 60));
	
	        centerPanel.add(cards, BorderLayout.NORTH);
	
	        // ===== TABLE =====
	
	        String[] columns = {
	                "Ngày cấp",
	                "Mã BN",
	                "Tên bệnh nhân",
	                "Thuốc",
	                "Liều lượng",
	                "Thành tiền"
	        };
	
	        DefaultTableModel model =
	                new DefaultTableModel(columns, 0);
	
	        table = new JTable(model);
	
	        table.setRowHeight(35);
	
	        table.getTableHeader().setFont(
	                new Font("Segoe UI", Font.BOLD, 14));
	
	        JScrollPane scroll = new JScrollPane(table);
	
	        scroll.setBorder(new EmptyBorder(0, 20, 20, 20));
	
	        centerPanel.add(scroll, BorderLayout.CENTER);
	
	        add(centerPanel, BorderLayout.CENTER);
	
	        // ================= FOOTER =================
	
	        JPanel footer = new JPanel(new FlowLayout(
	                FlowLayout.CENTER, 25, 15));
	
	        footer.setBackground(bg);
	
	        JButton btnThuoc = createButton(
	                "QUẢN LÝ THUỐC",
	                new Color(52, 152, 219));
	
	        JButton btnTK = createButton(
	                "QUẢN LÝ TÀI KHOẢN",
	                new Color(46, 204, 113));
	
	        JButton btnRefresh = createButton(
	                "LÀM MỚI",
	                new Color(241, 196, 15));
	
	        JButton btnLogout = createButton(
	                "ĐĂNG XUẤT",
	                new Color(231, 76, 60));
	
	        footer.add(btnThuoc);
	        footer.add(btnTK);
	        footer.add(btnRefresh);
	        footer.add(btnLogout);
	
	        add(footer, BorderLayout.SOUTH);
	
	        // ================= EVENTS =================
	
	        btnThuoc.addActionListener(e -> {
	            this.dispose();
	            new THEMTHUOC();
	        });
	
	        btnTK.addActionListener(e -> {
	            this.dispose();
	            new QUANLY_TAIKHOAN();
	        });
	
	        btnRefresh.addActionListener(e -> {
	            loadDashboardData();
	        });
	
	        btnLogout.addActionListener(e -> {
	
	            int confirm = JOptionPane.showConfirmDialog(
	                    this,
	                    "Bạn muốn đăng xuất?",
	                    "Xác nhận",
	                    JOptionPane.YES_NO_OPTION
	            );
	
	            if (confirm == JOptionPane.YES_OPTION) {
	                this.dispose();
	                new DANGNHAP();
	            }
	        });
	
	        loadDashboardData();
	
	        setVisible(true);
	    }
	
	    // ================= CARD =================
	
	    private JLabel createCard(
	            JPanel parent,
	            String title,
	            Color color
	    ) {
	
	        JPanel card = new JPanel(new BorderLayout());
	
	        card.setBackground(color);
	
	        card.setBorder(new CompoundBorder(
	                new LineBorder(color.darker(), 2),
	                new EmptyBorder(20, 20, 20, 20)
	        ));
	
	        JLabel lblTitle = new JLabel(title);
	
	        lblTitle.setForeground(Color.WHITE);
	
	        lblTitle.setFont(new Font(
	                "Segoe UI",
	                Font.BOLD,
	                18
	        ));
	
	        JLabel lblValue = new JLabel("0");
	
	        lblValue.setForeground(Color.WHITE);
	
	        lblValue.setFont(new Font(
	                "Segoe UI",
	                Font.BOLD,
	                32
	        ));
	
	        card.add(lblTitle, BorderLayout.NORTH);
	        card.add(lblValue, BorderLayout.CENTER);
	
	        parent.add(card);
	
	        return lblValue;
	    }
	
	    // ================= BUTTON =================
	
	    private JButton createButton(
	            String text,
	            Color color
	    ) {
	
	        JButton btn = new JButton(text);
	
	        btn.setPreferredSize(new Dimension(220, 50));
	
	        btn.setBackground(color);
	
	        btn.setForeground(Color.WHITE);
	
	        btn.setFont(new Font(
	                "Segoe UI",
	                Font.BOLD,
	                15
	        ));
	
	        btn.setFocusPainted(false);
	
	        btn.setBorder(null);
	
	        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	
	        return btn;
	    }
	
	    // ================= LOAD DATA =================
	
	    private void loadDashboardData() {
	
	        try {
	
	            Class.forName(
	                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
	            );
	
	            Connection conn =
	                    DriverManager.getConnection(dbUrl);
	
	            // ===== Tổng bệnh nhân =====
	
	            Statement st1 = conn.createStatement();
	
	            ResultSet rs1 =
	                    st1.executeQuery(
	                            "SELECT COUNT(*) FROM NGUOIDUNG"
	                    );
	
	            if (rs1.next()) {
	                lblTongBN.setText(rs1.getString(1));
	            }
	
	            // ===== Tổng đơn =====
	
	            Statement st2 = conn.createStatement();
	
	            ResultSet rs2 =
	                    st2.executeQuery(
	                            "SELECT COUNT(*) FROM QLCAPPHATTHUOC"
	                    );
	
	            if (rs2.next()) {
	                lblTongDon.setText(rs2.getString(1));
	            }
	
	            // ===== Doanh thu =====
	
	            Statement st3 = conn.createStatement();
	
	            ResultSet rs3 =
	                    st3.executeQuery(
	                            "SELECT SUM(thanhgia) FROM QLCAPPHATTHUOC"
	                    );
	
	            if (rs3.next()) {
	
	                double doanhthu = rs3.getDouble(1);
	
	                lblDoanhThu.setText(
	                        String.format("%,.0f VNĐ", doanhthu)
	                );
	            }
	
	            // ===== Tổng tài khoản =====
	
	            Statement st4 = conn.createStatement();
	
	            ResultSet rs4 =
	                    st4.executeQuery(
	                            "SELECT COUNT(*) FROM NGUOIDUNG"
	                    );
	
	            if (rs4.next()) {
	                lblTaiKhoan.setText(rs4.getString(1));
	            }
	
	            // ===== TABLE =====
	
	            DefaultTableModel model =
	                    (DefaultTableModel) table.getModel();
	
	            model.setRowCount(0);
	
	            Statement st5 = conn.createStatement();
	
	            ResultSet rs5 =
	                    st5.executeQuery(
	                            "SELECT TOP 20 * FROM QLCAPPHATTHUOC ORDER BY ngaycap DESC"
	                    );
	
	            while (rs5.next()) {
	
	                model.addRow(new Object[]{
	
	                        rs5.getString("ngaycap"),
	                        rs5.getString("mabenhnhan"),
	                        rs5.getString("tenbenhnhan"),
	                        rs5.getString("tenthuoc"),
	                        rs5.getString("lieuluong"),
	                        rs5.getDouble("thanhgia")
	
	                });
	            }
	
	            conn.close();
	
	        } catch (Exception ex) {
	
	            ex.printStackTrace();
	
	            JOptionPane.showMessageDialog(
	                    this,
	                    "Lỗi dashboard: " + ex.getMessage()
	            );
	        }
	    }
	
	    public static void main(String[] args) {
	        new TRANGCHON_ADMIN();
	    }
	}
