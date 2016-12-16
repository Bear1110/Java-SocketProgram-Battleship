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
	int[] brodcastStatus = {0,0};

	int x = -1;
	int y = -1;
	boolean hit = false;
	
	/**********************game Data**************/
	int playerMap[][][] = new int[2][10][10];
	boolean checkMap[][][] = new boolean[2][10][10];
	int endConditions[] = {17,17};
	/************************************/
	
	@Override
	public void run() {
		
		for(int i = 0 ; i < 2 ; i++){
			for(int j = 0 ; j < 10 ; j++){
				for(int k = 0 ; k < 10 ; k++){
					checkMap[i][j][k]=  true;
				}
			}
		}
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		ExecutorService threadExecutor = Executors.newFixedThreadPool(2);
		try {
			serverSocket = new ServerSocket(5988);
			System.out.println("Server listening requests...");
			for (int i = 0 ; i < 2 ; i++) {// only accept two client
				Socket socket = serverSocket.accept();
				if(i==0){//real client
					views.connectGUI.someOneConnectIn();
					UDP.API.otherIP = socket.getInetAddress();
				}
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
		int other ;
		
		public RequestThread(Socket clientSocket, int id) {
			this.clientSocket = clientSocket;
			this.id = id;
			other = id^1;
		}
		
		public void run() {
			System.out.println("有" + clientSocket.getRemoteSocketAddress() + "連線近來!");
			String finish = "N";
			
			while (!this.clientSocket.isClosed()) {
				try {
					JSONObject ServerData = new JSONObject();
					DataInputStream input = new DataInputStream(this.clientSocket.getInputStream());
					DataOutputStream output = new DataOutputStream(this.clientSocket.getOutputStream());
					////////////////////////////////////////////////////////////////////////////read
					String message = input.readUTF(); // read message from client
					JSONObject messageJSON = new JSONObject(message); // COonvert JSON
					String action = messageJSON.get("action").toString();
					String actionValue = messageJSON.get("actionValue").toString();
					switch(action) {
			           case "Nothing":
			        	   
			        	   break;
			           case "ready": 
			            	ready[id] = 1;
							actionBrodcast = action;
							playerMap[id] = gson.fromJson(messageJSON.getString("Map"),int[][].class);
			                break; 
			            case "attack":
							actionBrodcast = action;
							String[] xy = actionValue.split(",");
							x = Integer.parseInt(xy[0]);
							y = Integer.parseInt(xy[1]);
//							System.out.println("Clicked point: (" + x + "," + y + ")");
							if (checkMap[other][x][y]) {////////
								if (playerMap[other][x][y] == 1) {
									playerMap[other][x][y] = 9;
									finish = GameOver(other);
									hit = true;
								} else {
//									System.out.println(id+"沒打到"+other+"  "+whichTurn);
									whichTurn = whichTurn==1 ? 0 : 1 ;
									hit = false;
								}
								checkMap[other][x][y] = false;
							}
//			            	Print();
			                break;
			        }
					////////////////////////////////////////////////////////////////////////////send
					if(actionBrodcast.equals("ready")  ){
						if(ready[0]==1 && ready[1]==1){
							actionBrodcastValue = 1+"";
							ServerData.put("yourTurn",  (whichTurn==id) ? 1 : 0 );
//							Print();
						}
					}else if(!finish.equals("N")){
						actionBrodcast = "finish";
						actionBrodcastValue = finish;
					}else if (actionBrodcast.equals("attack")){
						
						String yourTurn = whichTurn==id ? "1" : "0";
						ServerData.put("attackLocation",x+","+y);
						ServerData.put("hit",hit+"");
						ServerData.put("yourTurn", yourTurn);
					}
					///////////////////////////////////////////////////////////////////////////////////
					ServerData.put("actionValue", actionBrodcastValue);
					ServerData.put("action", actionBrodcast);
					output.writeUTF(ServerData.toString());
					output.flush();
					
					checkBrocast(brodcastStatus,id,actionBrodcast); //prevent double brocast
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(String.format("連線中斷,%s", clientSocket.getRemoteSocketAddress()));
					try {
						if (this.clientSocket != null && !this.clientSocket.isClosed())
							this.clientSocket.close();
					} catch (IOException ex) {ex.printStackTrace();}
				}

			}

		}
		private void checkBrocast(int[] brodcastStatus , int player,String nowBrodcast){
			if(!actionBrodcast.equals("") && nowBrodcast.equals(actionBrodcast)){
				brodcastStatus[id] = 1;
			}
			if(brodcastStatus[0]*brodcastStatus[1]==1){
				brodcastStatus[0]=0;
				brodcastStatus[1]=0;
				actionBrodcast="";
			}
		}
		
		private String GameOver(int index) {
			String s = "N";
			endConditions[index]--;
			
			if (endConditions[index] == 0) {
					s = (index^1)+"";
			}
			
			return s;
		}
		private void Print(){
			System.out.println("////////////whichTurn"+ whichTurn);
			
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
