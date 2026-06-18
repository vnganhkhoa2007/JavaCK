package Java;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

			while (true) {
				Request request = (Request) input.readObject();
				System.out.println("Nhận yêu cầu: " + request.getAction());
				Response response = new Response();
				UserDAO dao = new UserDAO();

				switch (request.getAction()) {
				case "LOGIN":	
					boolean login = dao.checkLogin(request.getUsername(), request.getPassword());
					response.setStatus(login);
					response.setMessage(login ? "Đăng nhập thành công" : "Sai tài khoản hoặc mật khẩu");
					break;

				case "REGISTER":
					boolean register = dao.register(request.getUsername(), request.getName(), request.getPassword());
					response.setStatus(register);
					response.setMessage(register ? "Đăng ký thành công" : "Username đã tồn tại");
					break;

				// --- 1. QUẢN LÝ TÀI KHOẢN ---
				case "LOAD_NGUOIDUNG":
					ArrayList<Object[]> listND = dao.loadAllNguoiDung();
					response.setStatus(true);
					response.setDataList(listND);
					break;

				case "INSERT_NGUOIDUNG":
					if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
			        request.getPassword() == null || request.getPassword().trim().isEmpty()) {
			        
			        response.setStatus(false);
			        response.setMessage("Thêm thất bại: Username và Password không được để trống!");
			    } else {
					boolean insND = dao.insertNguoiDung(request.getId(), request.getName(), request.getTuoi(), request.getGioitinh(), request.getUsername(), request.getPassword());
					response.setStatus(insND);
					response.setMessage(insND ? "Thêm tài khoản thành công!" : "ID đã tồn tại hoặc dữ liệu lỗi!");
			    }break;

				// --- 2. CẬP NHẬT THÔNG TIN THUỐC ---
				case "LOAD_THUOC":
					ArrayList<Object[]> listThuoc = dao.loadAllCapPhatThuoc();
					response.setStatus(true);
					response.setDataList(listThuoc);
					break;

				case "INSERT_THUOC":
					boolean insThuoc = dao.insertCapPhatThuoc(request.getNgaycap(), request.getMabenhnhan(), request.getTenbenhnhan(),
							request.getTenthuoc(), request.getLieuluong(), request.getThanhgia(), request.getTinhtrangbenh());
					response.setStatus(insThuoc);
					response.setMessage(insThuoc ? "Thêm đơn thuốc thành công!" : "Lỗi thêm đơn thuốc!");
					break;

				case "UPDATE_THUOC":
					boolean updThuoc = dao.updateCapPhatThuoc(request.getNgaycap(), request.getMabenhnhan(), request.getTenbenhnhan(),
							request.getTenthuoc(), request.getLieuluong(), request.getThanhgia(), request.getTinhtrangbenh(), request.getOldMa());
					response.setStatus(updThuoc);
					response.setMessage(updThuoc ? "Cập nhật dữ liệu thành công!" : "Cập nhật thất bại!");
					break;

				case "DELETE_THUOC":
					boolean delThuoc = dao.deleteCapPhatThuoc(request.getMabenhnhan());
					response.setStatus(delThuoc);
					response.setMessage(delThuoc ? "Đã xóa đơn thuốc thành công!" : "Xóa thất bại!");
					break;

				// --- 3. TRANG KHÁCH HÀNG ---
				case "LOAD_USER_INFO":
					Object[] userInfo = dao.loadUserInfo(request.getId());
					if(userInfo != null) {
						response.setStatus(true);
						response.setSingleData(userInfo);
					} else {
						response.setStatus(false);
					}
					break;

				case "LOAD_USER_HISTORY":
					ArrayList<Object[]> listHist = dao.loadUserHistory(request.getId());	
					response.setStatus(true);
					response.setDataList(listHist);
					break;
					
					// Thêm case xử lý tìm kiếm theo tên bệnh nhân vào switch-case của ClientHandler
				case "SEARCH_THUOC_BY_NAME":
				    ArrayList<Object[]> listSearch = dao.searchCapPhatThuocByName(request.getTenbenhnhan());
				    response.setStatus(true);
				    response.setDataList(listSearch);
				    break;

				default:
					response.setStatus(false);
					response.setMessage("Không có chức năng");
				}

				output.writeObject(response);
				output.flush();
			}
		} catch (Exception e) {
			System.out.println("Client ngắt kết nối.");
		}
	}
}