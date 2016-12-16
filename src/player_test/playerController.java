package player_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.google.gson.Gson;

import views.BattleShip;

public class playerController {
	private BattleShip gameView;
	private String action = "Nothing";
	private String actionValue = "";
	int[][] Map ;
	ExecutorService clientReceive = Executors.newSingleThreadExecutor();
	
	public playerController(BattleShip test) {
		gameView = test;
		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
		threadExecutor.execute(new UpdateData()); //Update client
	}
	
	public void readyForStart(int[][] playerMap) {
		action = "ready";
		actionValue = "1";
		Map = playerMap.clone();
	}
	
	public void attack(int x, int y) {
		// TODO Auto-generated method stub
		action = "attack";
		actionValue = x+","+y;
	}
	
	class UpdateData implements Runnable {
		Gson gson = new Gson();
		String dot = "";
		String LabelMessage = "";

		@Override
		public void run() {
			JSONObject ClientData = new JSONObject();
			while(true){
				
				try {Thread.sleep(600);} catch (InterruptedException e) {} 
				
				ClientData.put("action", action);
				ClientData.put("actionValue", actionValue);
				if(action.equals("ready")){
					ClientData.put("Map", gson.toJson(Map));
				}
				TCP.clientModule.sendMessage(ClientData.toString());
				
				String ServerData = TCP.clientModule.readServerMessage();
				JSONObject messageJSON = new JSONObject(ServerData); // 轉成JSON
				
				String action = messageJSON.get("action").toString();
				String actionValue = messageJSON.get("actionValue").toString();
				switch(action) {
		           case "Nothing":
		        	   break;
		           case "ready":
		        	   if(actionValue.equals("1")){    		   
		        		   gameView.gameStart = true;
		        		   if(messageJSON.get("yourTurn").toString().equals("1")){
		        			   gameView.yourTurn = true;
		        			   JOptionPane.showMessageDialog(gameView.mainWindow, "你先攻擊");
		        			   gameView.music("src/res/wmv/yourturn.wav");
		        		   }else{
		        			   JOptionPane.showMessageDialog(gameView.mainWindow, "對方先攻擊");
		        		   }
		        		   gameView.mainWindow.setTitle((Integer.parseInt(messageJSON.get("yourTurn").toString())^1)+"");
		        		   gameView.condition.setText("遊戲開始,");
						 }
		                break; 
		            case "attack":
	            		String hit = messageJSON.get("hit").toString();
	            		String[] attackLocation = messageJSON.get("attackLocation").toString().split(",");
	            		int x = Integer.parseInt(attackLocation[0]);
	            		int y = Integer.parseInt(attackLocation[1]);
		            	if(playerController.this.action.equals("attack")){ //你攻擊 所以畫在對面
		            		gameView.handleHit(x, y, Boolean.parseBoolean(hit),false);
		            	}else{ // 你被打
		            		gameView.handleHit(x, y, Boolean.parseBoolean(hit),true);
		            	}
		            	 if(messageJSON.get("yourTurn").toString().equals("1")){
							 gameView.yourTurn = true;
							 gameView.condition.setText("攻擊中,");
							 gameView.music("src/res/wmv/yourturn.wav");
						 }else{
							 gameView.yourTurn = false;
							 gameView.condition.setText("等待對手攻擊,");
						 }
		            	break;
		            case "finish":
		            	if(actionValue.equals(gameView.mainWindow.getTitle())){
							 JOptionPane.showMessageDialog(gameView.mainWindow, "你贏了大雞巴");
						 }else{
							 JOptionPane.showMessageDialog(gameView.mainWindow, "你輸了廢物");
						 }
						 TCP.clientModule.disconnect();
		            	break;
		        }
				
				LabelMessage = gameView.condition.getText().split(",")[0];
				if(dot.length()>=3){
					dot = dot.replace("...", "");
				}else{
					dot+=".";
				}
				gameView.condition.setText(LabelMessage+","+dot);
				playerController.this.action = "Nothing";
			}
		}
		
	}
}
