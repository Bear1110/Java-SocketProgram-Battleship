package TCP;

public class iniServerThraed implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TCP.serverModule.initTCPServer();

	}

}
