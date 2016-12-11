package player_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.google.gson.Gson;

import views.BattleShip;

public class playerController {
	BattleShip gameView;
	String action = "Nothing";
	String actionValue = "";
	String lastDo = "";
	int[][] Map ;
	
	
	ExecutorService clientReceive = Executors.newSingleThreadExecutor();
	
	
	public playerController(BattleShip test) {
		gameView = test;
		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
		threadExecutor.execute(new UpdateData()); //Update client
	}
	
	
	
	class UpdateData implements Runnable {
		Gson gson = new Gson();

		@Override
		public void run() {
			JSONObject ClientData = new JSONObject();
			while(true){
				
				try {Thread.sleep(1000);} catch (InterruptedException e) {} 
				
				ClientData.put("action", action);
				ClientData.put("actionValue", actionValue);
				if(action.equals("ready")){
					ClientData.put("Map", gson.toJson(Map));
				}
				TCP.clientModule.sendMessage(ClientData.toString());
				
				String ServerData = TCP.clientModule.readServerMessage();
				JSONObject messageJSON = new JSONObject(ServerData); // 轉成JSON
				
				String action = messageJSON.get("action").toString();
				 if(!action.equals("Nothing")){
					 String actionValue = messageJSON.get("actionValue").toString();
					 
					 if( action.equals("ready") && !lastDo.equals("ready") ){
						 if(actionValue.equals("1")){
							 lastDo = action;
							 gameView.gameStart = true;
							 if(messageJSON.get("yourTurn").toString().equals("1")){
								 gameView.yourTurn = true;
								 
							 }
							 gameView.mainWindow.setTitle((Integer.parseInt(messageJSON.get("yourTurn").toString())^1)+"");
						 }
					 }else if (action.equals("attack") ){
						 if(messageJSON.get("yourTurn").toString().equals("1")){
							 gameView.yourTurn = true;
							 JOptionPane.showMessageDialog(gameView.mainWindow, "換你了");
						 }
					 }
					 
					 
				 }
				playerController.this.action = "Nothing";
			}
		}
		
	}



	public void readyForStart(int[][] player2Map) {
		action = "ready";
		actionValue = "1";
		Map = player2Map.clone();
	}
	public void attack(int x, int y) {
		// TODO Auto-generated method stub
		action = "attack";
		actionValue = x+","+y;
	}
}
