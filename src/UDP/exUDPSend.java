package UDP;

import java.net.*;

public class exUDPSend extends Thread {
	int port;
	InetAddress server;
	String msg;

	public exUDPSend(InetAddress IP, int pPort, String pMsg) throws Exception {
		port = pPort;
		server = IP;
		msg = pMsg;
	}

	public void run() {
		try {
			byte buffer[] = msg.getBytes(); 
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, port);
			DatagramSocket socket = new DatagramSocket(); // 
			socket.send(packet); 
			socket.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}