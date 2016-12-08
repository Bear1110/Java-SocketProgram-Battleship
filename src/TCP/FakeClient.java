package TCP;

public class FakeClient implements Runnable {
	public static boolean wait = true;

	public static void freebutton() {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			while (!wait) {

			}

			/*
			 * ©È¥L¤Ó¦£ ºÎ­Ó 10²@¬í
			 */
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
