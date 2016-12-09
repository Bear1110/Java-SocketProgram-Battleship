package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

public class TcpServerThraed implements Runnable {
	/**********below is server data***************/
	int [] pressButton = {1,0}; //real client [key] is 0 
	String actionBrodcast = "";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = Executors.newFixedThreadPool(2);
		try {
			serverSocket = new ServerSocket(5988);
			System.out.println("Server listening requests...");
			for (int i = 0 ; i < 2 ; i++) {// only accept two client
				Socket socket = serverSocket.accept();
				UDP.API.otherIP = socket.getInetAddress();
				if(i==0)//real client
					views.connectGUI.someOneConnectIn();
				threadExecutor.execute(new RequestThread(socket,i));
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
	
	class RequestThread implements Runnable {
		
		private Socket clientSocket;
		int id ;
		String cha;
		int other ;
		public RequestThread(Socket clientSocket, int id) {
			this.clientSocket = clientSocket;
			this.id = id;
			if(id==0){
				cha = "Client";
				other = 1;
			}else{
				cha = "Server";
				other = 0;
			}
		}
		
		
		
		public void run() {
			System.out.println("有" + clientSocket.getRemoteSocketAddress() + "連線進來!");
			JSONObject ServerData = new JSONObject();
			
			while (!this.clientSocket.isClosed()) {
				try {
					DataInputStream input = new DataInputStream(this.clientSocket.getInputStream());
					DataOutputStream output = new DataOutputStream(this.clientSocket.getOutputStream());
					////////////////////////////////////////////////////////////////////////////read
					String message = input.readUTF(); // read message from client
					System.out.println(cha + " 對S說:" + message + pressButton[id]);
					JSONObject messageJSON = new JSONObject(message); // COonvert JSON
					String action = messageJSON.get("action").toString();
					 if(!action.equals("Nothing")){
						 String actionValue = messageJSON.get("actionValue").toString();
						 if(action.equals("pressButton")){
							 pressButton[id] = 0;
							 pressButton[other] = 1; 
							 actionBrodcast = "enableButton";
						 }
					 }
					 ////////////////////////////////////////////////////////////////////////////send 
					String CanPress = pressButton[id]+""; 
					ServerData.put("actionValue", CanPress);
					ServerData.put("action", actionBrodcast);
					output.writeUTF(ServerData.toString());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(String.format("連線中斷,%s", clientSocket.getRemoteSocketAddress()));
					try {
						if (this.clientSocket != null && !this.clientSocket.isClosed())
							this.clientSocket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			}

		}	
	}

}
