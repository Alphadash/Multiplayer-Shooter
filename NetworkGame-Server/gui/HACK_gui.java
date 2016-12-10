package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class HACK_gui // Java allows starting of other Jars from code, but they will not display in a console, therefore this hack is required to show the server is running, and to stop it
{

	private JFrame frmServer;

	/**
	 * Create the application.
	 */
	public HACK_gui()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			// Unimportant if it doesn't work
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmServer = new JFrame();
		frmServer.setTitle("Server");
		frmServer.setBounds(100, 100, 200, 80);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmServer.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblCloseThisWindow = new JLabel("Close this window to stop the server");
		panel.add(lblCloseThisWindow);
		
		JLabel lblNewLabel = new JLabel("Listening on port 7777");
		panel.add(lblNewLabel);
		
		frmServer.setVisible(true);
	}

}
