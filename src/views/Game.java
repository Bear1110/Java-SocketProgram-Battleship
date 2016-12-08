package views;

import javax.swing.JFrame;

import player_test.test;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Game {
	Game myself;
	private JFrame frame;
	test player;

	public JButton button = new JButton("\u4E00\u4EBA\u6309\u4E00\u4E0B");

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public Game() {
		initialize();
		createEvents();
		frame.setVisible(true);
		player = new test(this);   //¹Cª±ª«¥ó
		myself = this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(184, Short.MAX_VALUE)
					.addComponent(button)
					.addGap(163))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(112)
					.addComponent(button)
					.addContainerGap(126, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	private void createEvents(){
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				player.pushButton(myself);
			}
		});
	}
}
