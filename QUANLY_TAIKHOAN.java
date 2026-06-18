package Java;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.ArrayList;

public class QUANLY_TAIKHOAN extends JFrame {
    private JTextField txtID, txtName, txtAge;
    private JComboBox<String> cbGender;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnBack;

    public QUANLY_TAIKHOAN() {
        setTitle("ADMIN - QUẢN LÝ TÀI KHOẢN KHÁCH HÀNG");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(245, 246, 250));

        // Layout dạng lưới để xếp các ô nhập liệu cơ bản gọn gàng
        JPanel sidePanel = new JPanel(new GridLayout(11, 1, 8, 8));
        sidePanel.setPreferredSize(new Dimension(300, 0));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        txtID = new JTextField(); 
        txtName = new JTextField(); 
        txtAge = new JTextField();
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cbGender.setBackground(Color.WHITE);

        sidePanel.add(new JLabel("Mã khách hàng (ID):")); sidePanel.add(txtID);
        sidePanel.add(new JLabel("Họ và Tên:")); sidePanel.add(txtName);
        sidePanel.add(new JLabel("Tuổi:")); sidePanel.add(txtAge);
        sidePanel.add(new JLabel("Giới tính:")); sidePanel.add(cbGender);

        btnAdd = new JButton("THÊM MỚI TÀI KHOẢN");
        btnAdd.setBackground(new Color(46, 204, 113)); 
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setOpaque(true); btnAdd.setBorderPainted(false);
        sidePanel.add(new JLabel("")); sidePanel.add(btnAdd);

        btnBack = new JButton("QUAY LẠI MENU");
        btnBack.setBackground(new Color(149, 165, 166));
        btnBack.setForeground(Color.WHITE);
        btnBack.setOpaque(true); btnBack.setBorderPainted(false);
        sidePanel.add(btnBack);

        String[] header = {"Mã ID", "Họ Tên (User)", "Tuổi", "Giới Tính"};
        model = new DefaultTableModel(header, 0);
        table = new JTable(model);

        add(sidePanel, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Lắng nghe sự kiện nút bấm
        btnBack.addActionListener(e -> { this.dispose(); new TRANGCHON_ADMIN(); });
        btnAdd.addActionListener(e -> saveAccount());

        loadData();
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("LOAD_NGUOIDUNG");
            Response res = client.send(req);
            if (res.isStatus() && res.getDataList() != null) {
                for (Object[] row : res.getDataList()) {
                    model.addRow(row);
                }
            }
            client.close();
        } catch (java.net.ConnectException ce) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến Máy chủ! Vui lòng kiểm tra xem Server.java đã khởi động chưa.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void saveAccount() {
        String id = txtID.getText().trim();
        String hoten = txtName.getText().trim();
        String tuoiStr = txtAge.getText().trim();
        String gioitinh = cbGender.getSelectedItem().toString();

        // Kiểm tra dữ liệu nhập cơ bản không để trống
        if (id.isEmpty() || hoten.isEmpty() || tuoiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            int tuoi = Integer.parseInt(tuoiStr);
            ClientSocket client = new ClientSocket();
            Request req = new Request("INSERT_NGUOIDUNG");
            
            // Gán thông tin cá nhân cơ bản vào Request
            req.setId(id); 
            req.setName(hoten); 
            req.setTuoi(tuoi); 
            req.setGioitinh(gioitinh);
            
            // ĐÃ CẬP NHẬT: Tự động lấy thông tin theo yêu cầu của bạn
            // - Username: lấy giá trị chuỗi từ ô nhập Họ và Tên
            // - Password: lấy giá trị chuỗi từ ô nhập Mã ID khách hàng
            req.setUsername(hoten); 
            req.setPassword(HashUtil.hash(id)); // Băm mật khẩu (chính là chuỗi ID) bằng SHA-256 trước khi gửi
            
            Response res = client.send(req);
            JOptionPane.showMessageDialog(this, res.getMessage());
            
            if (res.isStatus()) {
                // Xóa trắng form nhập để chuẩn bị cho lượt nhập tiếp theo
                txtID.setText(""); 
                txtName.setText(""); 
                txtAge.setText("");
                cbGender.setSelectedIndex(0);
                loadData(); // Cập nhật lại danh sách hiển thị trên bảng
            }
            client.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tuổi nhập vào bắt buộc phải là ký tự số nguyên!");
        } catch (java.net.ConnectException ce) {
            JOptionPane.showMessageDialog(this, "Mất kết nối máy chủ! Hãy chắc chắn Server đang chạy.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage());
        }
    }
}