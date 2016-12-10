package model;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionHandler implements Runnable
{
	private boolean alive;
	
	private int listenPort;
	private ServerSocket serverSocket;

	ConnectionHandler(int inputListenPort)
	{
		listenPort = inputListenPort;
		serverSocket = null;
	}
	
	@Override
	public void run()
	{
		alive = true;
		try (ServerSocket ss = new ServerSocket(listenPort))
		{
			serverSocket = ss;
			waitForConnections();
		}
		catch (IOException e)
		{
			System.out.println("Error: Could not listen on port: " + listenPort);
		}
	}
	
	private void waitForConnections()
	{
		while (alive)
		{
			try
			{
				new Thread(new ClientHandler(serverSocket.accept())).start();
			}
			catch (IOException e)
			{
				System.out.println("Error: Waiting for client failed on port: " + listenPort);
			}
		}
	}
}
