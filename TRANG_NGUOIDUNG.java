package Java;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.ArrayList;

public class TRANG_NGUOIDUNG extends JFrame {
    private String id;
    private JLabel lblName, lblAge, lblID, lblGender;
    private JTable table;
    private DefaultTableModel model;
    
    // Bảng màu giao diện hiện đại (Modern UI Profile)
    private final Color primaryColor = new Color(44, 62, 80);    // Xanh thẫm lịch lãm
    private final Color accentColor = new Color(52, 152, 219);    // Xanh nước biển sáng
    private final Color bgLight = new Color(245, 246, 250);       // Nền xám trắng dễ chịu
    private final Color cardBg = new Color(255, 255, 255);        // Trắng tuyệt đối cho thẻ thông tin

    public TRANG_NGUOIDUNG(String userID) {
        this.id = userID;
        
        setTitle("MEDICARE - CỔNG THÔNG TIN KHÁCH HÀNG");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(25, 25));
        getContentPane().setBackground(bgLight);

        // ==========================================
        // 1. TOP PANEL: THANH TIÊU ĐỀ HỆ THỐNG
        // ==========================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(primaryColor);
        topPanel.setPreferredSize(new Dimension(1100, 70));
        topPanel.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        JLabel lblTitle = new JLabel("✚ CỔNG THÔNG TIN SỨC KHỎE KHÁCH HÀNG - MEDICARE");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle, BorderLayout.WEST);
        
        // NÚT ĐĂNG XUẤT ĐƯỢC ĐƯA LÊN GÓC PHẢI TOP BAR (CHUẨN UX/UI)
        JButton btnLogout = new JButton("Đăng xuất ➜");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogout.setBackground(new Color(231, 76, 60)); // Màu đỏ Flat
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new DANGNHAP(); // Quay lại trang đăng nhập
            }
        });
        
        JPanel btnWrapper = new JPanel(new GridBagLayout());
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnLogout);
        topPanel.add(btnWrapper, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. WEST PANEL: THẺ HỒ SƠ KHÁCH HÀNG (USER CARD)
        // ==========================================
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(cardBg);
        leftPanel.setPreferredSize(new Dimension(320, 0));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 230), 1, true),
                new EmptyBorder(25, 20, 25, 20)
        ));

        // Icon giả lập hồ sơ người dùng
        JLabel lblUserIcon = new JLabel("👤", SwingConstants.CENTER);
        lblUserIcon.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblUserIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblUserIcon);
        leftPanel.add(Box.createVerticalStrut(15));

        JLabel lblCardTitle = new JLabel("THÔNG TIN HỒ SƠ");
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCardTitle.setForeground(primaryColor);
        lblCardTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblCardTitle);
        leftPanel.add(Box.createVerticalStrut(25));

        // Các trường hiển thị dữ liệu (Đã cấu trúc lại nhãn thông tin rõ ràng)
        lblID = new JLabel("Mã bệnh nhân: " + id);
        lblName = new JLabel("Họ và tên: Đang tải...");
        lblAge = new JLabel("Tuổi: Đang tải...");
        lblGender = new JLabel("Giới tính: Đang tải...");

        JLabel[] infoLabels = {lblID, lblName, lblAge, lblGender};
        for (JLabel lbl : infoLabels) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(new Color(44, 62, 80));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            leftPanel.add(lbl);
            leftPanel.add(Box.createVerticalStrut(15));
        }

        // Bọc Panel trái lại bằng khoảng đệm để không dính lề cạnh khung hình
        JPanel westWrapper = new JPanel(new BorderLayout());
        westWrapper.setOpaque(false);
        westWrapper.setBorder(new EmptyBorder(0, 25, 25, 0));
        westWrapper.add(leftPanel, BorderLayout.CENTER);
        add(westWrapper, BorderLayout.WEST);

        // ==========================================
        // 3. CENTER PANEL: LỊCH SỬ CẤP PHÁT THUỐC
        // ==========================================
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 0, 25, 25));

        JLabel lblTableTitle = new JLabel("📋 LỊCH SỬ ĐƠN THUỐC ĐÃ ĐƯỢC CẤP PHÁT");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(primaryColor);
        centerPanel.add(lblTableTitle, BorderLayout.NORTH);

        // Cấu trúc bảng hiện đại
        String[] headers = {"Ngày Cấp Nhận", "Tình Trạng Bệnh Lý", "Đơn Thuốc Chỉ Định", "Liều Lượng Sử Dụng"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khách hàng chỉ có quyền xem, không được chỉnh sửa thông tin thuốc
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(230, 233, 240));
        table.setShowVerticalLines(false);
     // --- CẤU HÌNH HEADER CHO BẢNG (ÉP CHỮ HIỆN RÕ MÀU TRẮNG TUYỆT ĐỐI) ---
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40)); // Tăng độ cao tiêu đề cho thoáng

        // Tạo Renderer tùy biến ép thuộc tính hiển thị cho thanh tiêu đề
        TableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                // Dùng một JLabel để làm tiêu đề ô
                JLabel lblHeader = new JLabel(value.toString(), SwingConstants.LEFT); 
                lblHeader.setOpaque(true);
                lblHeader.setBackground(primaryColor); // Nền xanh thẫm lịch lãm
                lblHeader.setForeground(Color.WHITE);  // CHỮ TRẮNG TINH KHÔI - KHÔNG BỊ MỜ
                lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Chữ đậm rõ ràng
                
                // Tạo khoảng đệm padding 10px lề trái phải cho chữ tiêu đề cột đẹp mắt
                lblHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
                
                return lblHeader;
            }
        };

        // Áp dụng bộ renderer cứng này vào thanh tiêu đề của JTable
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // 🛠️ ĐÃ SỬA LỖI TẠI ĐÂY: Tạo cell renderer chuẩn kế thừa đầy đủ hàm vẽ chữ và căn lề
        DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                // Gọi hàm của lớp cha để thực hiện việc đổ dữ liệu và xử lý màu dòng được chọn
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Tạo khoảng đệm trống 10 pixel ở sườn trái và phải của văn bản trong ô
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        };
        table.setDefaultRenderer(Object.class, paddingRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(210, 215, 225), 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Tự động tải dữ liệu đồng bộ qua Server socket khi mở trang
        loadInfo();
        loadHistory();
        
        setVisible(true);
    }

    private void loadInfo() {
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("LOAD_USER_INFO");
            req.setId(id); // Truyền ID tài khoản đăng nhập để đối soát
            
            Response res = client.send(req);
            if (res.isStatus() && res.getSingleData() != null) {
                Object[] data = res.getSingleData();
                
                String maND = String.valueOf(data[0]);
                String hoTen = String.valueOf(data[1]);
                String tuoi = String.valueOf(data[2]);
                String gioiTinh = String.valueOf(data[3]);

                // Cập nhật giao diện một cách trực quan
                lblID.setText("<html><b>Mã bệnh nhân:</b> " + maND + "</html>");
                lblName.setText("<html><b>Họ và tên:</b> " + hoTen + "</html>");
                lblAge.setText("<html><b>Tuổi:</b> " + tuoi + "</html>");
                lblGender.setText("<html><b>Giới tính:</b> " + gioiTinh + "</html>");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu hồ sơ cá nhân trên hệ thống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi đồng bộ hồ sơ từ Server: " + e.getMessage(), "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory() {
        model.setRowCount(0); // Làm sạch dữ liệu bảng trước khi nạp mới
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("LOAD_USER_HISTORY");
            req.setId(id); // Lọc lịch sử thuốc theo ID khách hàng
            
            Response res = client.send(req);
            if (res.isStatus() && res.getDataList() != null) {
                ArrayList<Object[]> records = res.getDataList();
                for (Object[] row : records) {
                    // Đưa dữ liệu bóc tách được trực tiếp vào hàng hiển thị của bảng
                    model.addRow(new Object[]{
                        row[0], // Ngày cấp nhận
                        row[1], // Tình trạng bệnh lý
                        row[2], // Tên đơn thuốc chỉ định
                        row[3]  // Liều lượng sử dụng
                    });
                }
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi nạp danh sách thuốc từ Server: " + e.getMessage(), "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }
}