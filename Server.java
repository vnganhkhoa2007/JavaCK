package Java;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static final int PORT = 9000;

	public static void main(String[] args) {

		try {

			ServerSocket server = new ServerSocket(PORT); // để mở cổng sever port 9000 và chờ client kết nối

			System.out.println("SERVER đang chạy port " + PORT);

			while (true) {

				Socket socket = server.accept(); // chờ client kết nối

				System.out.println("Client kết nối: " + socket.getInetAddress()); // socket.getInetAndress là Trả về địa
																					// chỉ IP của client.

				ClientHandler handler = new ClientHandler(socket); // Tạo đối tượng xử lý client.

				handler.start(); // để kết nối các client

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}