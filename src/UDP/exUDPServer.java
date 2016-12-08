package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class exUDPServer implements Runnable {
	int port; // 連接埠
	DatagramSocket socket;

	public exUDPServer(int pPort) {
		port = pPort; // 設定連接埠。
	}

	public void run() {
		final int SIZE = 8192; // 設定最大的訊息大小為 8192.
		byte buffer[] = new byte[SIZE]; // 設定訊息暫存區
		for (int count = 0;; count++) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);			
			try {
				socket = new DatagramSocket(port);
				socket.receive(packet); // 接收封包。
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 設定接收的 UDP Socket.
			
			String msg = new String(buffer, 0, packet.getLength()); // 將接收訊息轉換為字串。
			System.out.println(count + " : receive = " + msg); // 印出接收到的訊息。
			socket.close(); // 關閉 UDP Socket.
		}
	}

}
