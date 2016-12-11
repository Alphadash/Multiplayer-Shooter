package gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class JoinGame extends JDialog
{
	private JTextField txfName;
	private JTextField txfIP;
	private JTextField txfPort;
	
	private static final String PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	
	public JoinGame(Point p)
	{
		setTitle("Join Game");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 200);
		setLocation(p);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
		);
		
		JLabel lblPlayerName = new JLabel("Player Name:");
		
		txfName = new JTextField();
		txfName.setColumns(10);
		
		JLabel lblIP = new JLabel("Server IP:");
		
		JLabel lblPort = new JLabel("Server Port:");
		
		txfIP = new JTextField();
		txfIP.setDocument(new JTextFieldLimit(15));
		txfIP.setColumns(10);
		
		
		txfPort = new JTextField();
		txfPort.setDocument(new JTextFieldLimit(4));
		txfPort.setColumns(10);
		txfPort.setText("7777");
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					String nPort = txfPort.getText();
					String nIP = txfIP.getText();
					String nName = txfName.getText();
					
					if (!isNumber(nPort))
					{
						JOptionPane.showMessageDialog(null, "You did not enter the correct IP and/or Port");
					}
					else if(!nName.isEmpty() && !nPort.isEmpty() && validate(nIP))
					{
						Runtime.getRuntime().exec("java -jar game.jar " + txfName.getText() + " " + txfIP.getText() + " " + txfPort.getText());
					}
				
				}
				catch(Exception exc)
				{
					JOptionPane.showMessageDialog(null, "Something went wrong!");
				}
				
			}
		});
		getRootPane().setDefaultButton(btnJoin);
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(btnJoin, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPlayerName)
								.addComponent(lblPort)
								.addComponent(lblIP))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(txfIP, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addComponent(txfPort, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
								.addComponent(txfName, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPlayerName)
						.addComponent(txfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIP)
						.addComponent(txfIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(txfPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancel)
						.addComponent(btnJoin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
	}
	
	public static boolean validate(final String ip){   
	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();  
	}
	
	private boolean isNumber(String n) {
		try {
			Integer.parseInt(n);
			return true;
		} 
		catch (NumberFormatException nfe) {
			return false;
		}
	}
}
	
	

