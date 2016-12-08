package UDP;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class API {

	public static String otherIP = "";
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
			executorService.execute(new exUDPClientSend("127.0.0.1", 5555, msg));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
