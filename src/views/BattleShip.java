package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import player_test.playerController;

public class BattleShip extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	playerController cpu;
	
	public boolean gameStart = false;
	public boolean yourTurn = false;	
	public JFrame mainWindow = new JFrame("遊戲視窗");
	public JFrame conditionWindow;
	public JPanel conditionPanel;
	JFrame shipWindow;
	JPanel playerPanel = new JPanel();
	JPanel shipPanel = new JPanel();
	JButton battleship[] = new JButton[4];
	String shipName[] = {"航空母艦 ","大船","中船","小船"};
	int playerMap[][] = new int[10][10];
	boolean checkMap[][] = new boolean[10][10];
	int shipCount[] = {1, 1, 2, 1};//
	int shipSize[] = {5, 4, 3, 2}; //
	int nowSize = 0;			   //
	int nowShip;				   //
	boolean changePlayer[] = {true, true, true, true};
	boolean shipOrientation = true; //true:left,right false:up,down
	public JLabel condition ;
	
	BufferedImage surface = new BufferedImage(609,940,BufferedImage.TYPE_INT_RGB);
	JLabel view;
	
	int[] dotX = new int[11];
	int[] dotY = new int[21];
	int[] dotXX = new int[11];
	int[] dotYY = new int[21];
	
	BattleShip(int order) {
		initial();
		createMap();
		shipWindow();
		cpu = new playerController(this);
		conditionWindow();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BattleShip window = new BattleShip(1);
					window.mainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void initial() {

		for (int i=0;i<10;i++) {
			for (int j=0;j<10;j++) {
				if (!gameStart) {
					playerMap[i][j] = 0;
				}
				checkMap[i][j] = true;
			}
		}		
	}
	
	public void createMap() {
		
		mainWindow.setSize(609, 940);
		mainWindow.setLocation(60, 50);
		mainWindow.addKeyListener(this);
		mainWindow.addMouseListener(this);
		mainWindow.addMouseMotionListener(this);
		for(int i = 0 ; i < 20 ;i++){
			dotY[i] =  i * 45;
		}
		for(int i = 0 ; i < 11 ;i++){
			dotX[i] =  i * 60;
		}
		playerPanel = new JPanel(){
			protected void paintComponent(Graphics g) {
				super.paintComponents(g);
				
				for (int i = 0; i <= 19; i++) {
					int left = dotY[i];
					g.drawLine(0, left , 600, left );
				}					
				g.setColor(Color.BLACK);
				for (int i = 0; i < 11; i++) {
					int top = dotX[i];
					g.drawLine(top, 0, top , 940);
				}
				Graphics2D g2 = (Graphics2D) g;
		        int fontSize = 40;
		        Font f = new Font("Comic Sans MS", Font.BOLD, fontSize);
		        g2.setFont(f);
				g2.setColor(Color.RED);				
                g2.setStroke(new BasicStroke(10));
                g2.draw(new Line2D.Float(0, 445, 600, 445));
			}
		};
		playerPanel.setSize(600, 940);
		playerPanel.setLocation(0, 0);
		
		Graphics g = surface.getGraphics();
		g.setColor(Color.lightGray);
        g.fillRect(0,0,609,940);
        g.dispose();
        
		view = new JLabel(new ImageIcon(surface));
		
		mainWindow.add(playerPanel);
		mainWindow.add(view);
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	

	public void shipWindow() {
		
		shipWindow = new JFrame("造船室");
		
		battleship[0] = new JButton(shipName[0] + "x1");
		battleship[1] = new JButton(shipName[1] + "x1");
		battleship[2] = new JButton(shipName[2] + "x2");
		battleship[3] = new JButton(shipName[3] + "x1");	

		shipPanel = new JPanel(new GridLayout(1,4));
		
		for (int i=0;i<4;i++) {
			battleship[i].addActionListener(this);
			shipPanel.add(battleship[i]);
		}
		
		shipWindow.setSize(600,140);
		shipWindow.setLocation(mainWindow.location().x+600, mainWindow.location().y);
		shipWindow.add(shipPanel);
		shipWindow.setVisible(true);
		
	}
	@SuppressWarnings("deprecation")
	public void conditionWindow() {
		conditionWindow = new JFrame("狀態列");
		conditionPanel = new JPanel();
		
		conditionWindow.setSize(600,140);
		conditionWindow.setLocation(mainWindow.location().x+600, mainWindow.location().y+140);
		condition = new JLabel("快擺船囉(目前橫向),");
		condition.setFont(new Font("新細明體", Font.PLAIN, 40));
		conditionWindow.add(condition);
		conditionWindow.setVisible(true);
	}
	//
	public void settleShip(int x, int y) {
		boolean vaild = false;
		System.out.println("Clicked point: (" + x + "," + y + ")ddd");
		int[][] shipSize = {{300,235},{240,188},{180,141},{120,94}};
			if (shipOrientation) {
				if (y+nowSize <= 10) {
					if (check(x,y) && nowSize != 0) {
						for (int i=y;i<y+nowSize;i++) {
							playerMap[x][i]++;
							checkMap[x][i] = false;
						}
						int X = dotX[y]+8;
						int Y = dotY[x+10]+20;
						drawPicture(X,Y,shipSize[nowShip][0],47,"src/res/png/"+nowShip+".png");
						music("src/res/wmv/putship.wav");
						judgeShip(nowShip);
						nowSize=0;
						vaild = true;
					}
				}
			} else {
				if (x+nowSize <= 10) {
					if (check(x,y) && nowSize != 0) {
						for (int i=x;i<x+nowSize;i++) {
							playerMap[i][y]++;
							checkMap[i][y] = false;	
						}
						judgeShip(nowShip);
						int X = dotX[y]+10;
						int Y = dotY[x+10]+20;
						drawPicture(X,Y,60,shipSize[nowShip][1]-8,"src/res/png/"+nowShip+"r.png");
						music("src/res/wmv/putship.wav");
						nowSize=0;
						vaild = true;
					}
				}
			}
		if (changePlayer[0] == false && changePlayer[1] == false && changePlayer[2] == false && changePlayer[3] == false ) {
			shipWindow.setVisible(false);
			condition.setText("等待對面擺完,");
			cpu.readyForStart(playerMap);
		}
		if(!vaild)
			JOptionPane.showMessageDialog(mainWindow, "擺不下去啦");
	}
	//檢查放船
	public boolean check(int x, int y) {
		boolean result = true;
		if (shipOrientation) {
			for (int i=y;i<y+nowSize;i++) {
				if (checkMap[x][i] == false) {
					result = false;
				}
			}
		} else {
			for (int i=x;i<x+nowSize;i++) {
				if (checkMap[i][y] == false) {
					result = false;
				}
			}
		}
		return result;
	}
	
	public void Strike(int x, int y) {
		if (yourTurn == true) {
			cpu.attack(x,y);
		} else {
			JOptionPane.showMessageDialog(mainWindow, "現在不是你攻擊唷");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == battleship[0]) {
			nowShip = 0;
			nowSize = shipSize[nowShip];
		} else if (e.getSource() == battleship[1]) {
			nowShip = 1;
			nowSize = shipSize[nowShip];
		} else if (e.getSource() == battleship[2]) {
			nowShip = 2;
			nowSize = shipSize[nowShip];
		} else if (e.getSource() == battleship[3]) {
			nowShip = 3;
			nowSize = shipSize[nowShip];
		}
	}
	

	public void judgeShip(int index) {
		shipCount[index]--;	
		battleship[index].setText(shipName[index] + "x" + shipCount[index]);
		if (shipCount[index]==0) {
			battleship[index].setEnabled(false);
			changePlayer[index] = false;
		}
		
	}
	
	//////////////////////////keyListener/////////////////////////
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
			if(shipOrientation){
				condition.setText("快擺船(目前直向),");
			}else{
				condition.setText("快擺船(目前橫向),");
			}
			shipOrientation = !shipOrientation;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/////////////////////////mouseListener////////////////////////
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (e.getX()) / 60;
		int y = (e.getY() - 33) / 45;
		System.out.println(e.getX() + "," + e.getY());
		System.out.println("Clicked point: (" + x + "," + y + ")ddd");
		int realY = y;
		if (y >= 10) {
			y = y - 10;
		}
		if (gameStart == false) {
			if(realY>=10){
				settleShip(y, x);
				
			}else{
				JOptionPane.showMessageDialog(mainWindow, "你的地盤在下面喔");
			}
		} else {
			
			if(realY<10){
				Strike(y,x);
			}else{
				JOptionPane.showMessageDialog(mainWindow, "別打自己船");
			}
		}
		System.out.println("//////////////////////");
		for (int i=0;i<10;i++) {
			for (int j=0;j<10;j++) {
				System.out.print(playerMap[i][j] + " ");
			}
			System.out.print("   |   ");
			for (int j=0;j<10;j++) {
				System.out.print(playerMap[i][j] + " ");
			}
			System.out.println();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println(this.getX()+" "+this.getY());
		// TODO Auto-generated method stub
		java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("src/res/png/"+nowShip+".png");
		Cursor a = toolkit.createCustomCursor(image , new Point(this.getX(),this.getY()), "");
		mainWindow.setCursor (a);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/////////////////////////mouseListener////////////////////////

	
	////////////////////////mouseMotion//////////////////////////

	@Override
	public void mouseMoved(MouseEvent e) {
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void handleHit (int x , int y, boolean hit,boolean self){
		System.out.println("Clicked point: (" + x + "," + y + ")ddd");
		String pic =(hit) ? "X" : "M";
		
		switch(pic){
			case "X":
				music("src/res/wmv/bomb.wav");
			break;
			case "M":
				music("src/res/wmv/miss.wav");
			break;
		}
		if(self){ //////////////////////////畫在自家
			int X = dotX[y]+8;
			int Y = dotY[x+10]+20;
			drawPicture(X,Y,60,46,"src/res/png/"+pic+".png");
			
		}else{//////////////////////////畫在別人家
			int X = dotX[y]+8;
			int Y = dotY[x]+20;
			drawPicture(X,Y,60,46,"src/res/png/"+pic+".png");
		}
	}
	
	public void music (String path){
		try {

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream((new File(path)));
			AudioFormat audioFormat = audioInputStream.getFormat();
			int bufferSize = (int) Math.min(audioInputStream.getFrameLength()* audioFormat.getFrameSize(),Integer.MAX_VALUE); 
			DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, audioFormat, bufferSize);
			Clip clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception ex) {

		}
	}
	private void drawPicture (int x , int y, int width ,int height,String path){
		try {
			SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                 Graphics g = surface.getGraphics();
	                 BufferedImage image;
					try {
						image = ImageIO.read(new File(path).toURI().toURL());
						g.drawImage(image,x,y ,width,height,null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                 g.dispose();
	                 view.repaint();
	                 playerPanel.validate();
	            }
	        });
		} catch (Exception ex) {

		}
	}

}