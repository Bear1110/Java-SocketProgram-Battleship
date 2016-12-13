package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import player_test.playerController;

public class BattleShip extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	playerController cpu;
	
	public boolean gameStart = false;
	public boolean yourTurn = false;	
	public JFrame mainWindow = new JFrame("遊戲視窗");
	public JFrame conditionWindow;
	public JPanel conditionPanel;
	JFrame shipWindow;
	JPanel playerPanel[] = new JPanel[2];
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
		
		for (int i=0;i<2;i++)
		{
			playerPanel[i] = new JPanel(){
				protected void paintComponent(Graphics g) {
					super.paintComponents(g);
					
					for (int i = 0; i <= 18; i++) {
						int left = 40 + i * 45;
						g.drawLine(0, left , 600, left );
					}
					
					g.setColor(Color.BLACK);
					for (int i = 0; i < 10; i++) {
						int top = 51 + i * 60;
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
			playerPanel[i].setSize(600, 470);
		}
		playerPanel[0].setLocation(0, 0);
		playerPanel[1].setLocation(0, 470);
		
		for (int i=0;i<2;i++) 
			mainWindow.add(playerPanel[i]);
		
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//嚙踝�蕭謍蕭嚙踝嚙踐嚙踐▽嚙踝蕭
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
		shipWindow.addKeyListener(this);
		shipWindow.setVisible(true);
		
	}
	public void conditionWindow() {		
		conditionWindow = new JFrame("狀態列");
		conditionPanel = new JPanel();
		
		conditionWindow.setSize(600,140);
		conditionWindow.setLocation(mainWindow.location().x+600, mainWindow.location().y+140);
		condition = new JLabel("快擺船囉");
		condition.setFont(new Font("新細明體", Font.PLAIN, 40));
		conditionWindow.add(condition);
		conditionWindow.setVisible(true);
		
	}
	
	//嚙踐����蕭謍�
	public void settleShip(int x, int y) {
				
		System.out.println("Clicked point: (" + x + "," + y + ")");
		
			
			if (shipOrientation) {
				if (y+nowSize <= 10) {
					if (check(x,y) && nowSize != 0) {
						for (int i=y;i<y+nowSize;i++) {
							playerMap[x][i]++;
							checkMap[x][i] = false;
						}
						judgeShip(nowShip);
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
					}
				}
			}
		if (changePlayer[0] == false && changePlayer[1] == false && changePlayer[2] == false && changePlayer[3] == false ) {
			shipWindow.setVisible(false);
			System.out.print("Change");
			cpu.readyForStart(playerMap);
		}
		
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
		// TODO Auto-generated method stub
		//嚙踝�蕭謘頛魂�蕭�嚙踝蕭謢對��		
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
	
	//嚙踐�蕭��蕭��蕭謍�嚙踝蕭
	public void judgeShip(int index) {
		nowShip = 0;		//嚙踝�蕭謍�嚙踐���0
		nowSize = 0;		//嚙踝�蕭謍�嚙踐憌�0
		shipCount[index]--;	//嚙踝�蕭謍蕭�嚙踐�蕭�嚙踝蕭嚙�
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
		// TODO Auto-generated method stub
		int x = (e.getX()) / 60;
		int y = (e.getY() - 33) / 45;
		//System.out.println(e.getX() + "," + e.getY());
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/////////////////////////mouseListener////////////////////////

	
	////////////////////////mouseMotion//////////////////////////

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
//		System.out.println(e.getPoint());
		int x = (e.getX()) / 60;
		int y = (e.getY() - 33) / 45;
		//System.out.println(e.getX() + "," + e.getY());
		//System.out.println(x +"," + y);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}