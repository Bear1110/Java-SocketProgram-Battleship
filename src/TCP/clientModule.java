package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bear
 *
 */
public class clientModule {
	
	private static Socket socket = null;
	ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	public static boolean connectServer(String serverip){
		String host = serverip;
		int port = 5988;
		try {
			socket = new Socket(host, port);
			UDP.API.otherIP = InetAddress.getByName(host);
			return true;
		}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 這只是例子 不要用可以刪掉
	 * @param MoveCode   
	 *  0    1          2          3        4
	 *  get  TURNEAST   TURNSOUTH  TURNWEST TURNNORTH
	 */
	public static void inputMoves(int MoveCode) {
		String[] acctivity = {"GET","TURNEAST","TURNSOUTH","TURNWEST","TURNNORTH"};
		
		try {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(acctivity[MoveCode]); // 傳東西給server
			output.flush(); // 清空緩衝區域 將東西強制送出
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void sendMessage(String Message){
		try {
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(Message); // 傳東西給server
			output.flush(); // 清空緩衝區域 將東西強制送出
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String readServerMessage() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			String message = input.readUTF(); // 傳東西給server
			return message;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error!clientModule.readServerMessage()";
	}
	public static void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
