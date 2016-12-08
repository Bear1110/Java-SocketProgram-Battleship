package TCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServerThraed implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = Executors.newFixedThreadPool(20);
		try {
			serverSocket = new ServerSocket(5988);
			System.out.println("Server listening requests...");
			while (true) {
				Socket socket = serverSocket.accept();
				String Ip = socket.getRemoteSocketAddress() + "";
				TCP.serverModule.addClientIPTable(Ip); // add IP
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

}
