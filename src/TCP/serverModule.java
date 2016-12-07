package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverModule {
	static Vector<String> v = new Vector<String>();
	
	public static void initTCPServer(){
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = Executors.newFixedThreadPool(20);
		try {
			serverSocket = new ServerSocket(5988);
			System.out.println("Server listening requests...");
			while (true) {
				Socket socket = serverSocket.accept();
				String Ip = socket.getRemoteSocketAddress() + "";
				v.addElement(Ip);
				threadExecutor.execute(new RequestThread(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (threadExecutor != null)
				threadExecutor.shutdown();
			if (serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static Vector<String> getClientIPTable() {
		return v;
	}
	
}
