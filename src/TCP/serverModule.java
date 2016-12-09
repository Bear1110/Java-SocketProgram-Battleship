package TCP;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class serverModule {
	public static void initTCPServer(){
		ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new TcpServerThraed());
	}	
}
