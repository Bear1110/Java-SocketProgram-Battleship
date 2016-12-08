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
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class connectGUI {
	connectGUI window;
	private JFrame frame;
	JLabel yourIpLable = new JLabel("你的IP是:");
	JButton createServer = new JButton("建立房間");
	JButton connectIp = new JButton("建立連線");
	JButton chatSubmit = new JButton("送出");
	JPanel panel;
	JLayeredPane layeredPane = new JLayeredPane();
	final JTextField targetIp = new JTextField();
	final JPanel chatroom = new JPanel();
	JTextArea textArea = new JTextArea();
	JTextField textField;
	final JScrollPane scrollPane = new JScrollPane();
	final JLabel lblip = new JLabel("請輸入IP");
	static ExecutorService waitMessageService = Executors.newSingleThreadExecutor();

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
		frame = new JFrame();
		frame.setBounds(100, 100, 482, 354);
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
							.addComponent(yourIpLable))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(65)
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(yourIpLable)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(layeredPane, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel = new JPanel();
		panel.setBounds(0, 0, 358, 272);
		layeredPane.add(panel);
		targetIp.setText("192.168.0.106");
		targetIp.setColumns(10);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblip)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(targetIp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(1)
							.addComponent(connectIp, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)))
					.addGap(90))
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(120)
					.addComponent(createServer, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(129, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(45)
					.addComponent(createServer, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblip)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(connectIp)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(targetIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(70))
		);
		panel.setLayout(gl_panel);
		chatroom.setBounds(0, 0, 358, 272);
		
		layeredPane.add(chatroom);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		
		
		GroupLayout gl_chatroom = new GroupLayout(chatroom);
		gl_chatroom.setHorizontalGroup(
			gl_chatroom.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_chatroom.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
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
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chatSubmit))
					.addContainerGap())
		);
		scrollPane.setViewportView(textArea);
		
		textArea.setText("等候玩家中");
		textArea.setEditable(false);
		chatroom.setLayout(gl_chatroom);
		frame.getContentPane().setLayout(groupLayout);
		
	}
	/**
	 * 
	 * 
	 */
	private void createEvents() {
		
		createServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(frame, "建立房間囉");
		        TCP.serverModule.initTCPServer(window);
		        panel.setVisible(false);
		        waitMessageService.execute(new waitMessageUpdate());
//		        layeredPane.setLayer(panel_1, 5);
		        Game window = new Game();
			}
		});
		connectIp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(TCP.clientModule.connectServer(targetIp.getText())){
					JOptionPane.showMessageDialog(frame, "連線成功");
					panel.setVisible(false);
					Game window = new Game();
					UDP.API.iniUDPServer();
				}else
					JOptionPane.showMessageDialog(frame, "連線失敗");
			}
		});
		chatSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UDP.API.sendUDPMessage("test");
			}
		});
	}
	public static void someOneConnectIn(){
		UDP.API.iniUDPServer();
		waitMessageService.shutdownNow();
	}
	class waitMessageUpdate implements Runnable {
		public void run() {
			try {
				while(true){
					Thread.sleep(1000);
					textArea.append(".\n");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
