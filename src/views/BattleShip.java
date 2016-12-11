package views;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player_test.playerController;

public class BattleShip extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	playerController cpu;
	
	boolean player = true; //true:player1, false:player2
	
	static JFrame mainWindow = new JFrame("遊戲視窗");
	static JPanel playerPanel[] = new JPanel[2];

	static JFrame shipWindow[] = new JFrame[2];
	static JPanel shipPanel[] = new JPanel[2];
	static JButton battleship[] = new JButton[4];
	String shipName[] = {"航空母艦","大船","中船","小船"};
	static int player1Map[][] = new int[10][10];
	static int player2Map[][] = new int[10][10];
	static boolean check1Map[][] = new boolean[10][10];
	static boolean check2Map[][] = new boolean[10][10];
	int shipCount[] = {1, 1, 2, 1};//
	int shipSize[] = {5, 4, 3, 2}; //
	int nowSize = 0;			   //
	int nowShip;				   //
	boolean changePlayer[] = {true, true, true, true};
	static boolean shipOrientation = true; //true:left,right false:up,down
	int endConditions[] = {17,17};
	boolean gameStart = false;
	
	BattleShip(int order) {
		initial();
		createMap();
		shipWindow(0);
		cpu = new playerController(this);   //遊玩物件
	}
	
	//�����
	public void initial() {

		for (int i=0;i<10;i++) {
			for (int j=0;j<10;j++) {
				if (!gameStart) {
					player1Map[i][j] = 0;
					player2Map[i][j] = 0;
				}
				check1Map[i][j] = true;
				check2Map[i][j] = true;
			}
		}
		
	}
	//���漱���
	public void changePlayer() {
		
		shipWindow[0].setVisible(false);
		
		shipCount[0] = 1;
		shipCount[1] = 1;
		shipCount[2] = 2;
		shipCount[3] = 1;
		nowSize = 0;
		
		for (int i=0;i<4;i++) 
			changePlayer[i] = true;
		
		shipWindow(1);
	}
	//銝餌�閬��
	public void createMap() {
		
		mainWindow.setSize(300, 625);
		mainWindow.setLocation(60, 150);
		mainWindow.addKeyListener(this);
		mainWindow.addMouseListener(this);
		mainWindow.addMouseMotionListener(this);
		
		for (int i=0;i<2;i++)
		{
			playerPanel[i] = new JPanel();
		}
		
		playerPanel[0].setSize(300, 300);
		playerPanel[0].setLocation(0, 0);
		playerPanel[0].setBackground(Color.gray);
		
		playerPanel[1].setSize(300, 300);
		playerPanel[1].setLocation(0, 300);
		playerPanel[1].setBackground(Color.blue);
		
		for (int i=0;i<2;i++) 
			mainWindow.add(playerPanel[i]);
		
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	//��������
	public void shipWindow(int player) {
		
		shipWindow[player] = new JFrame("�摰�" + (player+1) + "������");
		
		battleship[0] = new JButton(shipName[0] + "x1");
		battleship[1] = new JButton(shipName[1] + "x1");
		battleship[2] = new JButton(shipName[2] + "x2");
		battleship[3] = new JButton(shipName[3] + "x1");	

		shipPanel[player] = new JPanel(new GridLayout(1,4));
		
		for (int i=0;i<4;i++) {
			battleship[i].addActionListener(this);
			shipPanel[player].add(battleship[i]);
		}
		
		shipWindow[player].setSize(420,140);
		shipWindow[player].setLocation(mainWindow.location().x+300, mainWindow.location().y);
		shipWindow[player].add(shipPanel[player]);
		shipWindow[player].addKeyListener(this);
		shipWindow[player].setVisible(true);
		
	}
	//�蝵株�
	public void settleShip(int x, int y) {
				
		System.out.println("Clicked point: (" + x + "," + y + ")");
		
			
			if (shipOrientation) {
				if (y+nowSize <= 10) {
					if (check(x,y) && nowSize != 0) {
						for (int i=y;i<y+nowSize;i++) {
							player2Map[x][i]++;
							check2Map[x][i] = false;
						}
						judgeShip(nowShip);
					}
				}
			} else {
				if (x+nowSize <= 10) {
					if (check(x,y) && nowSize != 0) {
						for (int i=x;i<x+nowSize;i++) {
							player2Map[i][y]++;
							check2Map[i][y] = false;
						}
						judgeShip(nowShip);
					}
				}
			}
		if (changePlayer[0] == false && changePlayer[1] == false && changePlayer[2] == false && changePlayer[3] == false ) {
			System.out.print("Change");
			cpu.readyForStart();
		}
		
	}
	//�蝵株�����甇斗��暺���
	public boolean check(int x, int y) {
		
		boolean result = true;
		
		if (player == true) {
			
			if (shipOrientation) {
				for (int i=y;i<y+nowSize;i++) {
					if (check1Map[x][i] == false) {
						result = false;
					}
				}
			} else {
				for (int i=x;i<x+nowSize;i++) {
					if (check1Map[i][y] == false) {
						result = false;
					}
				}
			}
			
		} else {
			
			if (shipOrientation) {
				for (int i=y;i<y+nowSize;i++) {
					if (check2Map[x][i] == false) {
						result = false;
					}
				}
			} else {
				for (int i=x;i<x+nowSize;i++) {
					if (check2Map[i][y] == false) {
						result = false;
					}
				}
			}
			
		}
		
		return result;
	}
	//�����
	public void Strike(int x, int y) {
		
		if (player == true) {
			
			if (check2Map[x][y]) {
				if (player2Map[x][y] == 1) {
					player2Map[x][y] = 9;
					check2Map[x][y] = false;
					GameOver(1);
				} else {
					player = false;
				}
			}
			
		} else {
			
			if (check1Map[x][y]) {
				if (player1Map[x][y] == 1) {
					player1Map[x][y] = 9;
					check1Map[x][y] = false;
					GameOver(0);
				} else {
					player = true;
				}
			}
			
		}
		
	}
	//��蝯���
	public void GameOver(int index) {
		String s;
		endConditions[index]--;
		
		if (endConditions[index] == 0) {
			if (player == true)
				s = "Player1 win!";
			else
				s = "Player2 win!";
			
			JFrame endWindow = new JFrame("������");
			JLabel text = new JLabel("��蝯������" + s);
			endWindow.add(text);
			endWindow.setSize(250,100);
			endWindow.setLocationRelativeTo(null);
			endWindow.setVisible(true);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//��雿輻���		
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
	
	//����鞈��
	public void judgeShip(int index) {
		nowShip = 0;		//��蝺刻�飛0
		nowSize = 0;		//��憭批�飛0
		shipCount[index]--;	//��������
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
			shipOrientation = !shipOrientation; //霈������
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	//////////////////////////keyListener/////////////////////////

	
	/////////////////////////mouseListener////////////////////////
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = e.getX() / 30;
		int y = (e.getY() - 25) / 30;

		if (y >= 10) {
			y = y - 10;
		}
		
		if (gameStart == false) {
			settleShip(y, x);
		} else {
			Strike(y,x);
		}
		
		System.out.println("//////////////////////");
		
		for (int i=0;i<10;i++) {
			for (int j=0;j<10;j++) {
				System.out.print(player1Map[i][j] + " ");
			}
			System.out.print("   |   ");
			for (int j=0;j<10;j++) {
				System.out.print(player2Map[i][j] + " ");
			}
			System.out.println();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.getPoint());
	}
	////////////////////////mouseMotion//////////////////////////

}