package Java;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class THEMTHUOC extends JFrame {
    private JTextField txtNgayCap, mabenhnhan, tenbenhnhan, txtTinhTrang, tenthuoc, lieuluong, thanhgia;
    private JTable bangcapthuoc;
    private DefaultTableModel model;
    private Color primaryColor = new Color(44, 62, 80);
    private Color accentColor = new Color(52, 152, 219);

    public THEMTHUOC() {
        setTitle("ADMIN - CẬP NHẬT THÔNG TIN THUỐC BỆNH NHÂN");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel formWrapper = new JPanel(new GridLayout(15, 1, 4, 4));
        formWrapper.setPreferredSize(new Dimension(300, 540));
        formWrapper.setBorder(new EmptyBorder(0, 10, 0, 0)); // Tạo khoảng cách nhỏ bên sườn trái
        
        txtNgayCap = new JTextField(); mabenhnhan = new JTextField(); tenbenhnhan = new JTextField();
        txtTinhTrang = new JTextField(); tenthuoc = new JTextField(); lieuluong = new JTextField(); thanhgia = new JTextField();

        formWrapper.add(new JLabel("Ngày cấp:")); formWrapper.add(txtNgayCap);
        formWrapper.add(new JLabel("Mã bệnh nhân:")); formWrapper.add(mabenhnhan);
        formWrapper.add(new JLabel("Tên bệnh nhân:")); formWrapper.add(tenbenhnhan);
        formWrapper.add(new JLabel("Tình trạng bệnh:")); formWrapper.add(txtTinhTrang);
        formWrapper.add(new JLabel("Tên thuốc:")); formWrapper.add(tenthuoc);
        formWrapper.add(new JLabel("Liều lượng:")); formWrapper.add(lieuluong);
        formWrapper.add(new JLabel("Thành giá:")); formWrapper.add(thanhgia);

        String[] thongtin = {"Ngày Cấp", "Mã BN", "Tên BN", "Tình Trạng", "Thuốc", "Liều", "Giá Tiền"};
        model = new DefaultTableModel(thongtin, 0);
        bangcapthuoc = new JTable(model);

        add(formWrapper, BorderLayout.WEST);
        add(new JScrollPane(bangcapthuoc), BorderLayout.CENTER);

        // --- 🛠️ ĐÃ SỬA: CẤU HÌNH LẠI KHU VỰC THÀNH PHẦN NÚT BẤM KHÔNG BỊ MẤT CHỮ ---
        // Sử dụng GridLayout(1, 6, 10, 0) để chia đều 6 nút trên 1 hàng ngang, khoảng cách giữa các nút là 10px
        JPanel panelButtons = new JPanel(new GridLayout(1, 6, 10, 0));
        panelButtons.setBorder(new EmptyBorder(10, 15, 20, 15)); // Tạo khoảng đệm trống bao quanh thanh nút bấm

        JButton btnSearch = new JButton("Tìm kiếm"); 
        btnSearch.setBackground(new Color(155, 89, 182)); btnSearch.setForeground(Color.WHITE);
        btnSearch.setOpaque(true); btnSearch.setBorderPainted(false);
        
        JButton btnInvoice = new JButton("In hóa đơn"); 
        btnInvoice.setBackground(new Color(230, 126, 34)); btnInvoice.setForeground(Color.WHITE);
        btnInvoice.setOpaque(true); btnInvoice.setBorderPainted(false);
        
        JButton b1 = new JButton("Xác nhận"); 
        b1.setBackground(accentColor); b1.setForeground(Color.WHITE);
        b1.setOpaque(true); b1.setBorderPainted(false);
        
        JButton b2 = new JButton("Xóa"); 
        b2.setBackground(Color.RED); b2.setForeground(Color.WHITE);
        b2.setOpaque(true); b2.setBorderPainted(false);
        
        JButton b3 = new JButton("Cập nhật"); 
        b3.setBackground(Color.GREEN); b3.setForeground(Color.WHITE);
        b3.setOpaque(true); b3.setBorderPainted(false);
        
        JButton btnBack = new JButton("Quay lại Menu");
        btnBack.setBackground(new Color(149, 165, 166)); btnBack.setForeground(Color.WHITE);
        btnBack.setOpaque(true); btnBack.setBorderPainted(false);

        // Đưa các nút vào thanh panel theo thứ tự mong muốn
        panelButtons.add(btnSearch); 
        panelButtons.add(btnInvoice); 
        panelButtons.add(b1); 
        panelButtons.add(b2); 
        panelButtons.add(b3); 
        panelButtons.add(btnBack);
        
        add(panelButtons, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---
        btnBack.addActionListener(e -> { this.dispose(); new TRANGCHON_ADMIN(); });
        b1.addActionListener(e -> { if (saveToDatabase()) clearForm(); });

        b2.addActionListener(e -> {
            int rowSel = bangcapthuoc.getSelectedRow();
            if (rowSel != -1) {
                String ma = model.getValueAt(rowSel, 1).toString();
                removeFromDatabase(ma);
            }
        });

        b3.addActionListener(e -> {
            int rowSel = bangcapthuoc.getSelectedRow();
            if (rowSel != -1) {
                showUpdateDialog(model.getValueAt(rowSel, 1).toString(), rowSel);
            }
        });

        // --- SỰ KIỆN TÌM KIẾM THEO TÊN BỆNH NHÂN ---
        btnSearch.addActionListener(e -> {
            String targetName = tenbenhnhan.getText().trim();
            if (targetName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên bệnh nhân vào ô trống để tìm kiếm!");
                loadDataFromDatabase();
                return;
            }
            searchByName(targetName);
        });

        // --- SỰ KIỆN IN HÓA ĐƠN ---
        btnInvoice.addActionListener(e -> {
            int rowSel = bangcapthuoc.getSelectedRow();
            if (rowSel == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng bệnh nhân trong bảng để in hóa đơn!");
                return;
            }
            showInvoiceDialog(rowSel);
        });

        loadDataFromDatabase();
        setVisible(true);
    }

    private void loadDataFromDatabase() {
        model.setRowCount(0);
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("LOAD_THUOC");
            Response res = client.send(req);
            if (res.isStatus() && res.getDataList() != null) {
                for (Object[] row : res.getDataList()) model.addRow(row);
            }
            client.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void searchByName(String name) {
        model.setRowCount(0);
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("SEARCH_THUOC_BY_NAME");
            req.setTenbenhnhan(name);
            Response res = client.send(req);
            
            if (res.isStatus() && res.getDataList() != null) {
                for (Object[] row : res.getDataList()) {
                    model.addRow(row);
                }
                if (res.getDataList().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu đơn thuốc cho bệnh nhân: " + name);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm hoặc không có kết quả!");
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối Socket khi tìm kiếm!");
        }
    }

    private void showInvoiceDialog(int rowSel) {
        String ngay = model.getValueAt(rowSel, 0).toString();
        String ma = model.getValueAt(rowSel, 1).toString();
        String ten = model.getValueAt(rowSel, 2).toString();
        String tinhtrang = model.getValueAt(rowSel, 3).toString();
        String thuoc = model.getValueAt(rowSel, 4).toString();
        String lieu = model.getValueAt(rowSel, 5).toString();
        String gia = model.getValueAt(rowSel, 6).toString();

        JDialog invoiceDialog = new JDialog(this, "HÓA ĐƠN THANH TOÁN", true);
        invoiceDialog.setSize(450, 500);
        invoiceDialog.setLayout(new BorderLayout(10, 10));
        invoiceDialog.setLocationRelativeTo(this);

        JTextArea taInvoice = new JTextArea();
        taInvoice.setFont(new Font("Monospaced", Font.PLAIN, 13));
        taInvoice.setEditable(false);
        taInvoice.setBorder(new EmptyBorder(15, 15, 15, 15));

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("           HÓA ĐƠN TIỀN THUỐC            \n");
        sb.append("=========================================\n\n");
        sb.append(String.format(" Ngày cấp đơn : %s\n", ngay));
        sb.append(String.format(" Mã bệnh nhân : %s\n", ma));
        sb.append(String.format(" Tên bệnh nhân: %s\n", ten));
        sb.append(String.format(" Tình trạng   : %s\n", tinhtrang));
        sb.append("-----------------------------------------\n");
        sb.append(String.format(" Chi tiết thuốc: %s\n", thuoc));
        sb.append(String.format(" Liều lượng    : %s\n", lieu));
        sb.append("-----------------------------------------\n");
        sb.append(String.format(" THÀNH TIỀN   : %s VND\n\n", gia));
        sb.append("=========================================\n");
        sb.append("        CẢM ƠN BẠN ĐÃ SỬ DỤNG DỊCH VỤ     \n");
        sb.append("=========================================");
        
        taInvoice.setText(sb.toString());
        invoiceDialog.add(new JScrollPane(taInvoice), BorderLayout.CENTER);

        JPanel pnlInvoiceBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnConfirmPrint = new JButton("Xác nhận & Xuất File");
        btnConfirmPrint.setBackground(new Color(46, 204, 113));
        btnConfirmPrint.setForeground(Color.WHITE);
        btnConfirmPrint.setOpaque(true); btnConfirmPrint.setBorderPainted(false);
        
        JButton btnCancelPrint = new JButton("Hủy bỏ");
        btnCancelPrint.setBackground(Color.GRAY);
        btnCancelPrint.setForeground(Color.WHITE);
        btnCancelPrint.setOpaque(true); btnCancelPrint.setBorderPainted(false);

        pnlInvoiceBtns.add(btnConfirmPrint);
        pnlInvoiceBtns.add(btnCancelPrint);
        invoiceDialog.add(pnlInvoiceBtns, BorderLayout.SOUTH);

        btnConfirmPrint.addActionListener(ev -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu hóa đơn");
            fileChooser.setSelectedFile(new java.io.File("HoaDon_" + ma + ".txt"));
            
            int userSelection = fileChooser.showSaveDialog(invoiceDialog);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                    writer.write(taInvoice.getText());
                    JOptionPane.showMessageDialog(invoiceDialog, "Lưu hóa đơn thành công tại:\n" + fileToSave.getAbsolutePath());
                    invoiceDialog.dispose();
                } catch (IOException ioEx) {
                    JOptionPane.showMessageDialog(invoiceDialog, "Lỗi xảy ra trong quá trình xuất file!");
                    ioEx.printStackTrace();
                }
            }
        });

        btnCancelPrint.addActionListener(ev -> invoiceDialog.dispose());
        invoiceDialog.setVisible(true);
    }

    private boolean saveToDatabase() {
        try {
            double gia = Double.parseDouble(thanhgia.getText().trim());
            ClientSocket client = new ClientSocket();
            Request req = new Request("INSERT_THUOC");
            req.setNgaycap(txtNgayCap.getText().trim());
            req.setMabenhnhan(mabenhnhan.getText().trim());
            req.setTenbenhnhan(tenbenhnhan.getText().trim());
            req.setTenthuoc(tenthuoc.getText().trim());
            req.setLieuluong(lieuluong.getText().trim());
            req.setThanhgia(gia);
            req.setTinhtrangbenh(txtTinhTrang.getText().trim());

            Response res = client.send(req);
            JOptionPane.showMessageDialog(this, res.getMessage());
            client.close();
            loadDataFromDatabase();
            return res.isStatus();
        } catch (Exception e) { return false; }
    }

    private void removeFromDatabase(String idBenhNhan) {
        try {
            ClientSocket client = new ClientSocket();
            Request req = new Request("DELETE_THUOC");
            req.setMabenhnhan(idBenhNhan);
            Response res = client.send(req);
            JOptionPane.showMessageDialog(this, res.getMessage());
            client.close();
            loadDataFromDatabase();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showUpdateDialog(String oldMa, int rowSel) {
        JFrame f1 = new JFrame("Cập nhật đơn thuốc");
        f1.setSize(400, 400); f1.setLayout(new GridLayout(8, 2));
        JTextField n1 = new JTextField(model.getValueAt(rowSel, 0).toString());
        JTextField n2 = new JTextField(model.getValueAt(rowSel, 1).toString());
        JTextField n3 = new JTextField(model.getValueAt(rowSel, 2).toString());
        JTextField n4 = new JTextField(model.getValueAt(rowSel, 3).toString());
        JTextField n5 = new JTextField(model.getValueAt(rowSel, 4).toString());
        JTextField n6 = new JTextField(model.getValueAt(rowSel, 5).toString());
        JTextField n7 = new JTextField(model.getValueAt(rowSel, 6).toString());
        JButton btnOK = new JButton("Xác nhận");

        f1.add(new JLabel("Ngày:")); f1.add(n1); f1.add(new JLabel("Mã BN:")); f1.add(n2);
        f1.add(new JLabel("Tên:")); f1.add(n3); f1.add(new JLabel("Tình trạng:")); f1.add(n4);
        f1.add(new JLabel("Thuốc:")); f1.add(n5); f1.add(new JLabel("Liều:")); f1.add(n6);
        f1.add(new JLabel("Giá:")); f1.add(n7); f1.add(new JLabel("")); f1.add(btnOK);
        f1.setLocationRelativeTo(null); f1.setVisible(true);

        btnOK.addActionListener(ev -> {
            try {
                ClientSocket client = new ClientSocket();
                Request req = new Request("UPDATE_THUOC");
                req.setNgaycap(n1.getText().trim()); req.setMabenhnhan(n2.getText().trim());
                req.setTenbenhnhan(n3.getText().trim()); req.setTinhtrangbenh(n4.getText().trim());
                req.setTenthuoc(n5.getText().trim()); req.setLieuluong(n6.getText().trim());
                req.setThanhgia(Double.parseDouble(n7.getText().trim())); req.setOldMa(oldMa);

                Response res = client.send(req);
                JOptionPane.showMessageDialog(f1, res.getMessage());
                client.close(); f1.dispose(); loadDataFromDatabase();
            } catch (Exception ex) { ex.printStackTrace(); }
        });
    }

    private void clearForm() {
        txtNgayCap.setText(""); mabenhnhan.setText(""); tenbenhnhan.setText("");
        txtTinhTrang.setText(""); tenthuoc.setText(""); lieuluong.setText(""); thanhgia.setText("");
    }
}