package UDP;

public class DEMO {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UDP.UDPAPI.iniUDPServer();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UDP.UDPAPI.sendUDPMessage("tttt");
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
