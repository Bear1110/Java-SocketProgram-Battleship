package player_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import views.Game;

public class test {
	boolean if_ImServer = TCP.serverModule.ifIamServer();
	Game gameView;
	String action = "Nothing";
	String actionValue = "";
	ExecutorService clientReceive = Executors.newSingleThreadExecutor();
	
	
	public test(Game test) {
		gameView = test;
		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
		threadExecutor.execute(new UpdateData()); // °²ªºclient
	}
	
	public void pushButton(Game test) {
		action = "pressButton";
		actionValue = "1";
		test.button.setEnabled(false);
	}
	
	class UpdateData implements Runnable {

		@Override
		public void run() {
			JSONObject ClientData = new JSONObject();
			while(true){
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ClientData.put("action", action);
				ClientData.put("actionValue", actionValue);
				TCP.clientModule.sendMessage(ClientData.toString());
				
				String ServerData = TCP.clientModule.readServerMessage();
				JSONObject messageJSON = new JSONObject(ServerData); // Âà¦¨JSON
				
				String action = messageJSON.get("action").toString();
				 if(!action.equals("Nothing")){
					 String actionValue = messageJSON.get("actionValue").toString();
					 if(action.equals("whichOne")){
						 if(actionValue.equals("1")){
							 gameView.button.setEnabled(true);
						 }
					 }
				 }
				test.this.action = "Nothing";
			}
		}
		
	}
}
