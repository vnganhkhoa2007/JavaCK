package Java;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private String action;
	private String name;
	private String username;
	private String password;
	
	// Các trường bổ sung cho Quản lý tài khoản và Đơn thuốc
	private String id;
	private int tuoi;
	private String gioitinh;
	
	private String ngaycap;
	private String mabenhnhan;
	private String tenbenhnhan;
	private String tenthuoc;
	private String lieuluong;
	private double thanhgia;
	private String tinhtrangbenh;
	private String oldMa; // Dùng khi cập nhật đơn thuốc

	public Request(String action) {
		this.action = action;
	}

	// Getter và Setter cho tất cả các thuộc tính
	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public int getTuoi() { return tuoi; }
	public void setTuoi(int tuoi) { this.tuoi = tuoi; }
	public String getGioitinh() { return gioitinh; }
	public void setGioitinh(String gioitinh) { this.gioitinh = gioitinh; }
	public String getNgaycap() { return ngaycap; }
	public void setNgaycap(String ngaycap) { this.ngaycap = ngaycap; }
	public String getMabenhnhan() { return mabenhnhan; }
	public void setMabenhnhan(String mabenhnhan) { this.mabenhnhan = mabenhnhan; }
	public String getTenbenhnhan() { return tenbenhnhan; }
	public void setTenbenhnhan(String tenbenhnhan) { this.tenbenhnhan = tenbenhnhan; }
	public String getTenthuoc() { return tenthuoc; }
	public void setTenthuoc(String tenthuoc) { this.tenthuoc = tenthuoc; }
	public String getLieuluong() { return lieuluong; }
	public void setLieuluong(String lieuluong) { this.lieuluong = lieuluong; }
	public double getThanhgia() { return thanhgia; }
	public void setThanhgia(double thanhgia) { this.thanhgia = thanhgia; }
	public String getTinhtrangbenh() { return tinhtrangbenh; }
	public void setTinhtrangbenh(String tinhtrangbenh) { this.tinhtrangbenh = tinhtrangbenh; }
	public String getOldMa() { return oldMa; }
	public void setOldMa(String oldMa) { this.oldMa = oldMa; }
}