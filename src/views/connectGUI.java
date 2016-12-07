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

public class connectGUI {

	private JFrame frame;
	JButton createServer = new JButton("建立房間");
	JLabel yourIpLable = new JLabel("\u4F60\u7684IP\u662F");
	JButton connectIp = new JButton("\u9023\u7DDA");
	private JPanel panel;
	private final JTextField targetIp = new JTextField();

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
		frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 關掉這個視窗會全關
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 關掉這個視窗不會全關
		panel = new JPanel();
		targetIp.setColumns(10);
        
        
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
							.addContainerGap()
							.addComponent(yourIpLable))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(63)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(74, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(7)
					.addComponent(yourIpLable)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(36, Short.MAX_VALUE))
		);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(104)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(connectIp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(createServer, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(63)
							.addComponent(targetIp, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(69, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(53)
					.addComponent(createServer)
					.addGap(52)
					.addComponent(connectIp)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(targetIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
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
				ExecutorService executorService = Executors.newSingleThreadExecutor();
		        executorService.execute(new TCP.iniServerThraed());
		        frame.getContentPane().remove(panel);
		        frame.repaint();
		        Game window = new Game();
			}
		});
		connectIp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(TCP.clientModule.connectServer(targetIp.getText()))
					JOptionPane.showMessageDialog(frame, "連線成功");
				else
					JOptionPane.showMessageDialog(frame, "連線失敗");
			}
		});
	}
}
