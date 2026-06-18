package Java;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean status;
	private String message;
	
	// Thuộc tính nhận dữ liệu dạng bảng hoặc mảng đối tượng phức tạp
	private ArrayList<Object[]> dataList; 
	private Object[] singleData; // Dùng để gửi thông tin đơn lẻ (như thông tin 1 User)

	public boolean isStatus() { return status; }
	public void setStatus(boolean status) { this.status = status; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
	
	public ArrayList<Object[]> getDataList() { return dataList; }
	public void setDataList(ArrayList<Object[]> dataList) { this.dataList = dataList; }
	public Object[] getSingleData() { return singleData; }
	public void setSingleData(Object[] singleData) { this.singleData = singleData; }
}