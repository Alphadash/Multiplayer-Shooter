package gui;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

public class Start
{
	private static String TITLE = "Mega Death";
	
	private JFrame frmNetworkGame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Start window = new Start();
					window.frmNetworkGame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmNetworkGame = new JFrame();
		frmNetworkGame.setTitle(TITLE);
		frmNetworkGame.setBounds(100, 100, 165, 130);
		frmNetworkGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		GroupLayout groupLayout = new GroupLayout(frmNetworkGame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
		);
		
		JLabel lblWelcomeTo = new JLabel("Welcome to " + TITLE);
		
		JButton btnJoinServer = new JButton("Join Server");
		btnJoinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point p = frmNetworkGame.getLocationOnScreen();
				p.setLocation(p.getX() + 175.0, p.getY());
				new JoinGame(p).setVisible(true);
			}
		});
		
		JButton btnHostServer = new JButton("Host Server");
		btnHostServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					Runtime.getRuntime().exec("java -jar server.jar");
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnJoinServer, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnHostServer)
						.addComponent(lblWelcomeTo))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblWelcomeTo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnJoinServer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnHostServer)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		frmNetworkGame.getContentPane().setLayout(groupLayout);
	}
}
