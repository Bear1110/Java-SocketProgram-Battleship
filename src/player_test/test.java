package player_test;

import views.Game;

public class test {
	boolean if_ImServer = TCP.serverModule.ifIamServer();
	Game gameView;

	
	public test(Game test) {
		gameView = test;
		if (if_ImServer)
			gameView.button.setEnabled(false);
	}
	
	public void freebutton(){
		
	}
//	gameView.button.setEnable(true);
	public void pushButton() {
		int i;
		if (if_ImServer)
			i = 0;
		else
			TCP.clientModule.sendMessage("pushButton");
	}
}
