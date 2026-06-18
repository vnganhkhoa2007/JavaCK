package Java;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {

	private Socket socket;

	private ObjectOutputStream out;

	private ObjectInputStream in;

	public ClientSocket() throws Exception {

		socket = new Socket("localhost", 9000);

		out = new ObjectOutputStream(socket.getOutputStream());

		in = new ObjectInputStream(socket.getInputStream());

	}

	public Response send(Request request) throws Exception {

		out.writeObject(request);

		out.flush();

		Response response = (Response) in.readObject();

		return response;

	}

	public void close() {

		try {

			socket.close();

		} catch (Exception e) {
		}

	}

}