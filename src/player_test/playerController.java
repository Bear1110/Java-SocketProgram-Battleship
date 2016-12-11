package player_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import views.BattleShip;

public class playerController {
	BattleShip gameView;
	String action = "Nothing";
	String actionValue = "";
	String lastDo = "";
	
	ExecutorService clientReceive = Executors.newSingleThreadExecutor();
	
	
	public playerController(BattleShip test) {
		gameView = test;
		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
		threadExecutor.execute(new UpdateData()); //Update client
	}
	
	
	
	class UpdateData implements Runnable {

		@Override
		public void run() {
			JSONObject ClientData = new JSONObject();
			while(true){
				
				try {Thread.sleep(1000);} catch (InterruptedException e) {} 
				
				ClientData.put("action", action);
				ClientData.put("actionValue", actionValue);
				TCP.clientModule.sendMessage(ClientData.toString());
				
				String ServerData = TCP.clientModule.readServerMessage();
				JSONObject messageJSON = new JSONObject(ServerData); // Âà¦¨JSON
				
				String action = messageJSON.get("action").toString();
				 if(!action.equals("Nothing")){
					 String actionValue = messageJSON.get("actionValue").toString();
					 
					 if( action.equals("ready") && !lastDo.equals("ready") ){
						 if(actionValue.equals("1")){
							 lastDo = action;
							 System.out.println("sssss");
						 }
					 }
					 
					 
				 }
				playerController.this.action = "Nothing";
			}
		}
		
	}



	public void readyForStart() {
		action = "ready";
		actionValue = "1";
	}
}
