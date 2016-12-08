package UDP;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class API {

	public static InetAddress otherIP ;
	/**
	 * 
	 */
	public static void iniUDPServer() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new exUDPServer(5555));
	}

	public static void sendUDPMessage(String msg) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		try {
			executorService.execute(new exUDPSend(otherIP, 5555, msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
