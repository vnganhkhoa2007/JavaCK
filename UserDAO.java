package Java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDAO {

	// === XỬ LÝ ĐĂNG NHẬP / ĐĂNG KÝ (Đã có sẵn của bạn) ===
	public boolean checkLogin(String username, String password) {
		boolean result = false;
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "SELECT * FROM ADMIN_ACC WHERE username=? AND password=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, HashUtil.hash(password));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) result = true;
			rs.close(); ps.close(); conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}

	public boolean register(String username, String email, String password) {
		// Code register cũ của bạn...
		return true;
	}

	// === 1. LOGIC QUẢN LÝ TÀI KHOẢN KHÁCH HÀNG ===
	public ArrayList<Object[]> loadAllNguoiDung() {
		ArrayList<Object[]> list = new ArrayList<>();
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "SELECT id, hoten, tuoi, gioitinh FROM NGUOIDUNG";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				list.add(new Object[]{rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)});
			}
			rs.close(); st.close(); conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return list;
	}

	public boolean insertNguoiDung(String id, String hoten, int tuoi, String gioitinh, String username, String password) {
	    boolean result = false;
	    try {
	        Connection conn = DBConnection.getConnection();
	        
	        // Câu lệnh SQL chuẩn xác tuyệt đối theo thứ tự các cột trong Database
	        String sql = "INSERT INTO NGUOIDUNG (id, hoten, tuoi, gioitinh, username, password) VALUES (?, ?, ?, ?, ?, ?)";
	        
	        PreparedStatement st = conn.prepareStatement(sql);
	        st.setString(1, id);
	        st.setString(2, hoten);
	        st.setInt(3, tuoi);
	        st.setString(4, gioitinh);
	        st.setString(5, username);
	        st.setString(6, password); // Nhận chuỗi mật khẩu đã được xử lý mã hóa
	        
	        int rows = st.executeUpdate();
	        if (rows > 0) {
	            result = true;
	        }
	        
	        st.close();
	        conn.close();
	    } catch (Exception e) {
	        System.out.println("Lỗi đồng bộ tại insertNguoiDung (UserDAO):");
	        e.printStackTrace();
	    }
	    return result;
	}

	// === 2. LOGIC CẬP NHẬT THÔNG TIN THUỐC BỆNH NHÂN ===
	public ArrayList<Object[]> loadAllCapPhatThuoc() {
		ArrayList<Object[]> list = new ArrayList<>();
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "SELECT ngaycap, mabenhnhan, tenbenhnhan, tinhtrangbenh, tenthuoc, lieuluong, thanhgia FROM QLCAPPHATTHUOC";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				list.add(new Object[]{
					rs.getString("ngaycap"), rs.getString("mabenhnhan"), rs.getString("tenbenhnhan"),
					rs.getString("tinhtrangbenh"), rs.getString("tenthuoc"), rs.getString("lieuluong"), rs.getDouble("thanhgia")
				});
			}
			rs.close(); st.close(); conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return list;
	}

	public boolean insertCapPhatThuoc(String ngaycap, String mabenhnhan, String tenbenhnhan, String tenthuoc, String lieuluong, double thanhgia, String tinhtrangbenh) {
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "INSERT INTO QLCAPPHATTHUOC (ngaycap, mabenhnhan, tenbenhnhan, tenthuoc, lieuluong, thanhgia, tinhtrangbenh) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ngaycap);
			stmt.setString(2, mabenhnhan);
			stmt.setString(3, tenbenhnhan);
			stmt.setString(4, tenthuoc);
			stmt.setString(5, lieuluong);
			stmt.setDouble(6, thanhgia);
			stmt.setString(7, tinhtrangbenh);
			int row = stmt.executeUpdate();
			stmt.close(); conn.close();
			return row > 0;
		} catch (Exception e) { e.printStackTrace(); return false; }
	}

	public boolean updateCapPhatThuoc(String ngaycap, String mabenhnhan, String tenbenhnhan, String tenthuoc, String lieuluong, double thanhgia, String tinhtrangbenh, String oldMa) {
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "UPDATE QLCAPPHATTHUOC SET ngaycap=?, mabenhnhan=?, tenbenhnhan=?, tenthuoc=?, lieuluong=?, thanhgia=?, tinhtrangbenh=? WHERE mabenhnhan=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ngaycap);
			stmt.setString(2, mabenhnhan);
			stmt.setString(3, tenbenhnhan);
			stmt.setString(4, tenthuoc);
			stmt.setString(5, lieuluong);
			stmt.setDouble(6, thanhgia);
			stmt.setString(7, tinhtrangbenh);
			stmt.setString(8, oldMa);
			int row = stmt.executeUpdate();
			stmt.close(); conn.close();
			return row > 0;
		} catch (Exception e) { e.printStackTrace(); return false; }
	}

	public boolean deleteCapPhatThuoc(String mabenhnhan) {
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "DELETE FROM QLCAPPHATTHUOC WHERE mabenhnhan = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, mabenhnhan);
			int row = stmt.executeUpdate();
			stmt.close(); conn.close();
			return row > 0;
		} catch (Exception e) { e.printStackTrace(); return false; }
	}

	// === 3. LOGIC TRANG KHÁCH HÀNG (USER PORTAL) ===
	public Object[] loadUserInfo(String userId) {
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "SELECT id, hoten, tuoi, gioitinh FROM NGUOIDUNG WHERE id = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Object[] data = new Object[]{rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)};
				rs.close(); st.close(); conn.close();
				return data;
			}
			rs.close(); st.close(); conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}

	public ArrayList<Object[]> loadUserHistory(String userId) {
		ArrayList<Object[]> list = new ArrayList<>();
		try {
			Connection conn = DBConnection.getConnection();
			String sql = "SELECT ngaycap, tinhtrangbenh, tenthuoc, lieuluong FROM QLCAPPHATTHUOC WHERE mabenhnhan = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				list.add(new Object[]{rs.getString("ngaycap"), rs.getString("tinhtrangbenh"), rs.getString("tenthuoc"), rs.getString("lieuluong")});
			}
			rs.close(); st.close(); conn.close();
		} catch (Exception e) { e.printStackTrace(); }
		return list;
	}
	
	public ArrayList<Object[]> searchCapPhatThuocByName(String tenBenhNhan) {
	    ArrayList<Object[]> list = new ArrayList<>();
	    try {
	        Connection conn = DBConnection.getConnection();
	        // Sử dụng toán tử LIKE để tìm kiếm gần đúng theo tên bệnh nhân nhập vào
	        String sql = "SELECT ngaycap, mabenhnhan, tenbenhnhan, tinhtrangbenh, tenthuoc, lieuluong, thanhgia " +
	                     "FROM QLCAPPHATTHUOC WHERE tenbenhnhan LIKE ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, "%" + tenBenhNhan + "%");
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	            list.add(new Object[]{
	                rs.getString("ngaycap"), rs.getString("mabenhnhan"), rs.getString("tenbenhnhan"),
	                rs.getString("tinhtrangbenh"), rs.getString("tenthuoc"), rs.getString("lieuluong"), rs.getDouble("thanhgia")
	            });
	        }
	        rs.close(); ps.close(); conn.close();
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    }
	    return list;
	}
}