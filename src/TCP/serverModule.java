package TCP;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class serverModule {
	static boolean server = false;
	static Vector<String> v = new Vector<String>();
	
	public static void initTCPServer(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new TcpServerThraed());
        server = true;
	}
	public static boolean ifIamServer() {
		return server;
	}
	
	public static Vector<String> getClientIPTable() {
		return v;
	}
	public static void addClientIPTable(String ip) {
		v.addElement(ip);
	}
	
}
