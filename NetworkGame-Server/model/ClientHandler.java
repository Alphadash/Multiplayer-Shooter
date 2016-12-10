package model;

import game.Bullet;
import game.Message;
import game.Player;
import game.Score;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.math.Rectangle;

public class ClientHandler implements Runnable, Observer
{
	private boolean alive;
	
	private Socket clientSocket;
	
	private Thread threadIn, threadOut;
	
	private ClientOutput out;
	
	public ClientHandler(Socket inputSocket)
	{
		clientSocket = inputSocket;
	}

	@Override
	public void run()
	{
		alive = true;
		threadIn = new Thread(new ClientInput());
		threadOut = new Thread(out = new ClientOutput());
		threadIn.start();
		threadOut.start();
	}
	
	private synchronized void closeConnection()
	{
		alive = false;
		threadIn.interrupt();
		threadOut.interrupt();
		try
		{
			clientSocket.close();
		}
		catch (IOException e)
		{
			System.out.println("Error: Error closing client socket");
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg1 instanceof Object[])
		{
			Object[] hax = (Object[]) arg1;
			if (hax[0] != ClientHandler.this) out.send(hax[1]);
		}
		else if ((arg1 instanceof Message) || (arg1 instanceof Score))
		{
			out.send(arg1);
		}
	}
	
	public void sendBox(Rectangle arg)
	{
		out.send(arg);
	}
	
	private class ClientInput implements Runnable
	{
		@Override
		public void run()
		{
			Server server = Server.getInstance();
			ObjectInputStream objIn = null;
			
			try
			{
				objIn = new ObjectInputStream(clientSocket.getInputStream());

				if (objIn != null)
				{
					 // Get instance once, instead of wasting cycles every iteration of the loop
					Object receive = null;
					while (alive && ((receive = objIn.readUnshared()) != null)) // Unshared to avoid double reading of serialized object
					{
						if (receive instanceof Player)
						{
							server.updateClient(ClientHandler.this, (Player) receive); // Only one server, Observer pattern would be overkill
						}
						else if (receive instanceof Bullet)
						{
							server.sendBullet(ClientHandler.this, (Bullet) receive); // Only one server, Observer pattern would be overkill
						}
						else if (receive instanceof Score)
						{
							server.broadcastScore((Score) receive);
						}
						else if (receive instanceof Message)
						{
							if (((Message) receive).getMessage().equals("JOIN")) server.addClient(ClientHandler.this);
							else if (((Message) receive).getMessage().equals("QUIT"))
							{
								break;
							}
						}
						receive = null; // Network code is time-critical, having one object and resetting its reference is much faster than allocating a new object each iteration of the loop
					}
				}
			}
			catch (SocketException e)
			{
				if (!Thread.currentThread().isInterrupted()) // If the thread IS interrupted, we expect the stream to have closed
				{
					System.out.println("Error: Client quit unexpectedly");
				}
			}
			catch (IOException e)
			{
				if (!Thread.currentThread().isInterrupted()) // If the thread IS interrupted, we expect the stream to have closed
				{
					System.out.println("Error: Could not create input reader or stream closed during reading");
					e.printStackTrace();
				}
			}
			catch (ClassNotFoundException e)
			{
				System.out.println("Error: Error while trying to read object from stream");
			}
			finally
			{
				server.removeClient(ClientHandler.this);
				closeConnection();
			}
		}
	}
	
	private class ClientOutput implements Runnable
	{
		private ObjectOutputStream objOut;
		
		@Override
		public void run()
		{
			try
			{
				objOut = new ObjectOutputStream(clientSocket.getOutputStream());
				objOut.flush();
				Server.getInstance().addObserver(ClientHandler.this);
			}
			catch (IOException e)
			{
				System.out.println("Error: Could not create output writer");
				closeConnection();
			}
		}

		public void send(Object arg)
		{
			if (alive)
			{
				try
				{
					objOut.writeUnshared(arg); // Unshared to avoid caching of serializable object
					objOut.flush();
				}
				catch (IOException e)
				{
					System.out.println("Error: Could not write object to stream");
				}
			}
			else System.out.println("Warning: Output is not yet ready");
		}
	}
}
