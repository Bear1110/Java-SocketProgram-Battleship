package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class connectGUI {
	/***********非視窗元件 屬性**********/
	String Name = "計網助教";
	
	/***********宣告視窗元件屬性**********/
	private JFrame frame;
	JLabel yourIpLable = new JLabel("你的IP是:");
	JButton createServer = new JButton("建立房間");
	JButton connectIp = new JButton("建立連線");
	JButton chatSubmit = new JButton("送出");
	JPanel panel;
	JLayeredPane layeredPane = new JLayeredPane();
	JTextField wantTalkWhat;
	final JTextField targetIp = new JTextField();//ddd
	final JPanel chatroom = new JPanel();
	final static JScrollPane scrollPane = new JScrollPane();
	final JLabel lblip = new JLabel("請輸入IP");
	static JTextArea textArea = new JTextArea("等候玩家中");
	static ExecutorService waitMessageService = Executors.newSingleThreadExecutor();
	private final JLabel NameLabel = new JLabel("暱稱:");
	private final JTextField inputName = new JTextField(Name);
	private final JButton submitName = new JButton("修改暱稱");
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					connectGUI window = new connectGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public connectGUI() {
		initialize();
		createEvents();
	}

	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		inputName.setColumns(10);
		frame = new JFrame();
		frame.setBounds(100, 100, 486, 383);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 關掉這個視窗會全關
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 關掉這個視窗不會全關
        
		String IP = null;
	    try {
			IP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {e.printStackTrace();}
		yourIpLable.setText("你的IP是 :"+IP);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(34)
							.addComponent(yourIpLable)
							.addGap(49)
							.addComponent(NameLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(inputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(submitName, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(65)
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(67, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(yourIpLable)
						.addComponent(NameLabel)
						.addComponent(inputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(submitName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel = new JPanel();
		panel.setBounds(0, 0, 358, 272);
		layeredPane.add(panel);
		targetIp.setText("192.168.0.106");
		targetIp.setColumns(10);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblip)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(connectIp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(targetIp, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
					.addGap(90))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(118)
					.addComponent(createServer, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(131, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(47)
					.addComponent(createServer, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblip)
						.addComponent(targetIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(connectIp)
					.addGap(41))
		);
		panel.setLayout(gl_panel);
		chatroom.setBounds(0, 0, 358, 272);
		
		layeredPane.add(chatroom);
		
		wantTalkWhat = new JTextField();
		wantTalkWhat.setColumns(10);
		
		
		
		GroupLayout gl_chatroom = new GroupLayout(chatroom);
		gl_chatroom.setHorizontalGroup(
			gl_chatroom.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_chatroom.createSequentialGroup()
					.addContainerGap()
					.addComponent(wantTalkWhat, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chatSubmit)
					.addGap(12))
		);
		gl_chatroom.setVerticalGroup(
			gl_chatroom.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_chatroom.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_chatroom.createParallelGroup(Alignment.BASELINE)
						.addComponent(wantTalkWhat, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chatSubmit))
					.addContainerGap())
		);
		scrollPane.setViewportView(textArea);
		
		textArea.setEditable(false);
		chatroom.setLayout(gl_chatroom);
		chatroom.setVisible(false);
		frame.getContentPane().setLayout(groupLayout);
		
	}
	/**
	 *  創建 按鈕事件 或是其他事件區域
	 */
	private void createEvents() {
		
		createServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(frame, "建立房間囉");
		        TCP.serverModule.initTCPServer();
		        panel.setVisible(false);
		        chatroom.setVisible(true);
		        waitMessageService.execute(new waitMessageUpdate());
//		        layeredPane.setLayer(panel_1, 5);
			}
		});
		connectIp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(TCP.clientModule.connectServer(targetIp.getText())){
					JOptionPane.showMessageDialog(frame, "連線成功");
					panel.setVisible(false);
			        chatroom.setVisible(true);
					Game window = new Game();
					//UDP.API.iniUDPServer();
				}else
					JOptionPane.showMessageDialog(frame, "連線失敗");
			}
		});
		chatSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textArea.append(Name+":"+wantTalkWhat.getText()+"\n");
				UDP.API.sendUDPMessage(Name+":"+wantTalkWhat.getText());
				scrollTobutton();
			}
		});
		submitName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Name = inputName.getText();
			}
		});
	}
	public static void someOneConnectIn(){
		//UDP.API.iniUDPServer();
		waitMessageService.shutdownNow();
		TCP.clientModule.connectServer("127.0.0.1");
        Game window = new Game();
	}
	public static void receiveChat(String msg){
		textArea.append(msg+"\n");
		scrollTobutton();
	}
	public static void scrollTobutton(){
		JScrollBar vertical = scrollPane.getVerticalScrollBar(); // 捲到最底下用的
		vertical.setValue( vertical.getMaximum() );
	}
	class waitMessageUpdate implements Runnable {
		public void run() {
			try {
				while(true){
					Thread.sleep(800);
					textArea.append(".\n");
					scrollTobutton();
				}
			} catch (InterruptedException e) {}
		}
	}	
}
