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
	int whichOne = 1;
	int [] pressButton = {1,0}; //真的 client 是  前面那個 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = Executors.newFixedThreadPool(3);
		try {
			serverSocket = new ServerSocket(5988);
			System.out.println("Server listening requests...");
			int i = 0;
			while (true) {
				Socket socket = serverSocket.accept();				
				String Ip = socket.getRemoteSocketAddress() + "";
				TCP.serverModule.addClientIPTable(Ip); // add IP
				UDP.API.otherIP = socket.getInetAddress();
				if(i==0)
					views.connectGUI.someOneConnectIn();
				threadExecutor.execute(new RequestThread(socket,i));
				i++;
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
		private String message = "";
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
					
					message = input.readUTF(); // 讀入送到server 的消息
					System.out.println(cha + " 對S說:" + message + pressButton[id]);
					JSONObject messageJSON = new JSONObject(message); // 轉成JSON
					String action = messageJSON.get("action").toString();
					 if(!action.equals("Nothing")){
//						 String actionValue = messageJSON.get("actionValue").toString();
						 if(action.equals("pressButton")){
							 System.out.println("changeValue id:"+id + " other:"+other);
							 pressButton[id] = 0;
							 pressButton[other] = 1; 
						 }
					 }else{
						 System.out.println("Nothing");
					 }
					 
					String CanPress = pressButton[id]+""; 
					ServerData.put("actionValue", CanPress);
					ServerData.put("action", "whichOne");
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
