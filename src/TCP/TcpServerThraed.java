package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONObject;

import com.google.gson.Gson;

public class TcpServerThraed implements Runnable {
	/**********below is server data***************/
	int [] ready = {0,0}; //real client [key] is 0
	int whichTurn = 0;
	String actionBrodcast = "";
	String actionBrodcastValue = "";
	int BrodcastCount = 0;
	
	/************************************/
	
	int shipCount[] = {1, 1, 2, 1};//
	int shipSize[] = {5, 4, 3, 2}; //
	int nowSize = 0;			   //
	int nowShip;				   //
	
	int playerMap[][][] = new int[2][10][10];
	static boolean checkMap[][][] = new boolean[2][10][10];
	boolean changePlayer[] = {true, true, true, true};
	static boolean shipOrientation = true; //true:left,right false:up,down

	boolean gameStart = false;
	int endConditions[] = {17,17};
	/************************************/
	
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

		Gson gson = new Gson();
		private Socket clientSocket;
		int id ;
		String cha;
		int other ;
		String lastDo = "";
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
			
			while (!this.clientSocket.isClosed()) {
				try {
					JSONObject ServerData = new JSONObject();
					DataInputStream input = new DataInputStream(this.clientSocket.getInputStream());
					DataOutputStream output = new DataOutputStream(this.clientSocket.getOutputStream());
					////////////////////////////////////////////////////////////////////////////read
					String message = input.readUTF(); // read message from client
//					System.out.println(cha + " 對S說:" + message + ready[id]);
					JSONObject messageJSON = new JSONObject(message); // COonvert JSON
					String action = messageJSON.get("action").toString();
					 if(!action.equals("Nothing")){
						 String actionValue = messageJSON.get("actionValue").toString();
						 if(action.equals("ready")){
							 ready[id] = 1;
							 actionBrodcast = action;
							 playerMap[id] = gson.fromJson(messageJSON.getString("Map"),int[][].class);
						 }else if (action.equals("attack")){
							 Print();
							 whichTurn = whichTurn==1 ? 0 : 1 ;
							 actionBrodcast = action;
							 int target = id==1 ? 0 : 1; 
							 String[] xy = actionValue.split(",");
							 int x = Integer.parseInt(xy[0]);
							 int y = Integer.parseInt(xy[1]);
							 if (checkMap[target][x][y]) {
							 	if (playerMap[target][x][y] == 1) {
							 		playerMap[target][x][y] = 9;
									checkMap[target][x][y] = false;
									GameOver(target);
								} else {
									System.out.println(id+"沒打到"+target);
									whichTurn = whichTurn==1 ? 0 : 1 ;
									System.out.println(id+"沒打到"+target);
								}
							}
						 }
					 }
					 ////////////////////////////////////////////////////////////////////////////send
					if(actionBrodcast.equals("ready") && !lastDo.equals("yourTurn")){
						if(ready[0]==1 && ready[1]==1){
							actionBrodcastValue = 1+"";
							ServerData.put("yourTurn",  (whichTurn==id) ? 1 : 0 );
							lastDo = "yourTurn";
							Print();
						}
					}else if (actionBrodcast.equals("attack")){
						
						ServerData.put("yourTurn", whichTurn==id ? 1 : 0);
						
					}
					///////////////////////////////////////////////////////////////////////////////////
					ServerData.put("actionValue", actionBrodcastValue);
					ServerData.put("action", actionBrodcast);
					output.writeUTF(ServerData.toString());
					output.flush();
					BrodcastCount++;
					if(BrodcastCount==2){
						BrodcastCount=0;
						actionBrodcast="";
					}
					
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
		
		public String GameOver(int index) {
			String s = "N";
			endConditions[index]--;
			
			if (endConditions[index] == 0) {
				if (whichTurn == 1)
					s = "Player1 win!";
				else
					s = "Player2 win!";
			}
			
			return s;
		}
		public void Print(){
			System.out.println("//////////////////////"+ whichTurn);
			
			for (int i=0;i<10;i++) {
				for (int j=0;j<10;j++) {
					System.out.print(playerMap[0][i][j] + " ");
				}
				System.out.print("   |   ");
				for (int j=0;j<10;j++) {
					System.out.print(playerMap[1][i][j] + " ");
				}
				System.out.println();
			}
		}
	}

}
