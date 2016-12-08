package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 處理Client端的Request執行續。
 * @version
 */
class RequestThread implements Runnable {
	private Socket clientSocket;
	private String message = "";
	String Ip = "";

	public RequestThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public void run() {
		
		System.out.println("有" + clientSocket.getRemoteSocketAddress() + "連線進來!");
		DataInputStream input = null;
		DataOutputStream output = null;
		try {
			input = new DataInputStream(this.clientSocket.getInputStream());
			output = new DataOutputStream(this.clientSocket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (!this.clientSocket.isClosed()) {
			try {
				message = input.readUTF(); // 讀入送到server 的消息
				System.out.println(Ip + " 對 Server說:" + message);
				
//				output.writeUTF("sdfasdfasdf"); // 送資料
//				output.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(String.format("連線中斷,%s", clientSocket.getRemoteSocketAddress()));
				try {
					if (input != null)
						input.close();
					if (output != null)
						output.close();
					if (this.clientSocket != null && !this.clientSocket.isClosed())
						this.clientSocket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}

	}
}	