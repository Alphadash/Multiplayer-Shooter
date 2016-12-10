package model;

import game.Bullet;
import game.GameUtils;
import game.Message;
import game.Player;
import game.Score;
import gui.HACK_gui;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Server extends Observable // Singleton
{
	private static Server uniqueInstance;
	
	private Array<Rectangle> boxes;
	
	private Map<ClientHandler, Player> clients;
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() { // Hack, see class for details
			public void run()
			{
				new HACK_gui();
			}
		});
		
		new Thread(new ConnectionHandler(7777)).start();
	}
	
	private Server()
	{
		clients = new HashMap<ClientHandler, Player>();
		boxes = new Array<Rectangle>();
		for (int i = 0; i < 25; i++) boxes.add(GameUtils.createRectangle(MathUtils.random(3, 22) * 32, MathUtils.random(3, 17) * 32));
	}
	
	public synchronized static Server getInstance()
	{
		if (uniqueInstance == null) uniqueInstance = new Server();
		return uniqueInstance;
	}
	
	public synchronized void addClient(ClientHandler inputClient)
	{
		clients.put(inputClient, null);
		for (Rectangle box : boxes) inputClient.sendBox(box);
	}
	
	public synchronized void removeClient(ClientHandler inputClient)
	{
		deleteObserver(inputClient);
		setChanged();
		notifyObservers(new Message("QUIT " + clients.get(inputClient).getName()));
		clients.remove(inputClient);
	}
	
	public synchronized void updateClient(ClientHandler inputClient, Player inputPlayer)
	{
		clients.put(inputClient, inputPlayer);
		Object[] hax = {inputClient, inputPlayer}; // TODO Temporary hack until custom observer pattern
		setChanged();
		notifyObservers(hax);
	}
	
	public synchronized void sendBullet(ClientHandler inputClient, Bullet inputBullet)
	{
		Object[] hax = {inputClient, inputBullet}; // TODO Temporary hack until custom observer pattern
		setChanged();
		notifyObservers(hax);
	}
	
	public synchronized void broadcastScore(Score score)
	{
		setChanged();
		notifyObservers(score);
	}
}
