package gui;

import game.Bullet;
import game.Message;
import game.Player;
import game.Score;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

public class Game implements ApplicationListener
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 640;
	
	private Texture groundImage, playerImage, borderImage, boxImage, bulletImage;
	private SpriteBatch batch;
	private BitmapFont playerFont;
	private BitmapFont enemyFont;
	private OrthographicCamera camera;
	
	private Array<Rectangle> boxes;
	
	private Array<Bullet> bullets;
	private Array<Bullet> enemyBullets;
	
	private Player playerOne;
	private ObjectMap<String, Player> opponents;
	private ObjectMap<String, Integer> scoreboard;
	
	private Sound pistolShot;
	
	public Game(String inputName, String inputAddress, int inputPort)
	{
		playerOne = new Player(inputName);
		address = inputAddress;
		port = inputPort;
	}

	@Override
	public void create()
	{
		groundImage = new Texture(Gdx.files.internal("assets/ground.png"));
		playerImage = new Texture(Gdx.files.internal("assets/man_n_s.png"));
		borderImage = new Texture(Gdx.files.internal("assets/border.png"));
		boxImage = new Texture(Gdx.files.internal("assets/box.png"));
		bulletImage = new Texture(Gdx.files.internal("assets/bullet.png"));
		
		pistolShot = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/pistol_shot.mp3"));
		
		bullets = new Array<Bullet>();
		enemyBullets = new Array<Bullet>();
		
		boxes = new Array<Rectangle>();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
		
		playerFont = new BitmapFont();
		enemyFont = new BitmapFont();
		playerFont.setColor(0f, 1f, 0f, 1f);
		enemyFont.setColor(1f, 0f, 0f, 1f);

		opponents = new ObjectMap<String, Player>();
		scoreboard = new ObjectMap<String, Integer>();
		
		test();
	}

	@Override
	public void render()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(groundImage, 32, 32);
		for (int i = 0; i <= (WIDTH / 32); i++) for (int z = 0; z <= 1; z++) batch.draw(borderImage, i * 32, z * (HEIGHT - 32));
		for (int i = 0; i <= (HEIGHT / 32); i++) for (int z = 0; z <= 1; z++) batch.draw(borderImage, z * (WIDTH - 32), i * 32);
		for (Bullet bul : bullets)
		{
			batch.draw(bulletImage, bul.getX(), bul.getY());
			bul.addX(bul.getOffsetX() * deltaTime * 300);
			bul.addY(bul.getOffsetY() * deltaTime * 300);
			
			if(bul.getX() < 32) bullets.removeValue(bul, false);
			if(bul.getX() > WIDTH - 35) bullets.removeValue(bul, false);
			if(bul.getY() < 32) bullets.removeValue(bul, false);
			if(bul.getY() > HEIGHT - 35) bullets.removeValue(bul, false);
			for (Rectangle box : boxes)
			{
				if (box.overlaps(bul.getHitbox()))
				{
					bullets.removeValue(bul, false);
					break;
				}
			}
		}
		for (Bullet bul2 : enemyBullets) // TODO merge with duplicate code above
		{
			batch.draw(bulletImage, bul2.getX(), bul2.getY());
			bul2.addX(bul2.getOffsetX() * deltaTime * 300);
			bul2.addY(bul2.getOffsetY() * deltaTime * 300);
			
			if (playerOne.getHealth() > 0f && playerOne.getHitbox().overlaps(bul2.getHitbox()))
			{
				playerOne.setHealth(0f);
				playerOne.updateTimeOfDeath();
				out.send(new Score(bul2.getOwner()));
			}
			
			if(bul2.getX() < 32) enemyBullets.removeValue(bul2, false);
			if(bul2.getX() > WIDTH - 35) enemyBullets.removeValue(bul2, false);
			if(bul2.getY() < 32) enemyBullets.removeValue(bul2, false);
			if(bul2.getY() > HEIGHT - 35) enemyBullets.removeValue(bul2, false);
			for (Rectangle box : boxes)
			{
				if (box.overlaps(bul2.getHitbox()))
				{
					enemyBullets.removeValue(bul2, false);
					break;
				}
			}
		}
		for (Rectangle box : boxes)
		{
			batch.draw(boxImage, box.x, box.y);
		}
		for (Player ply : opponents.values())
		{
			if (ply.getHealth() > 0f)
			{
				batch.draw(playerImage, ply.getX(), ply.getY());
				enemyFont.draw(batch, ply.getName(), ply.getX(), ply.getY());
			}
			
		}
		if (playerOne.getHealth() > 0f) // Draw last to be on top of everything else
		{
			batch.draw(playerImage, playerOne.getX(), playerOne.getY());
			playerFont.draw(batch, playerOne.getName(), playerOne.getX(), playerOne.getY());
		}
		else
		{
			enemyFont.draw(batch, "You're dead!", 350, 350);
			enemyFont.draw(batch, "You will respawn after 3 seconds", 290, 325);
		}
		int offset = 0;
		for (String player : scoreboard.keys())
		{
			playerFont.draw(batch, player + ": " + scoreboard.get(player), 700f, 625f - (offset * 25f));
			offset++;
		}
		batch.end();

		if(TimeUtils.nanoTime() - playerOne.getLastShotTime() > 100000000L)
		{
			if(Gdx.input.isTouched() && playerOne.getHealth() > 0f)
			{
				Vector3 rawPos = new Vector3();
				rawPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(rawPos);

				Vector2 touchPos = new Vector2(rawPos.x, rawPos.y);
				Vector2 delta = touchPos.sub(new Vector2(playerOne.getX() + 15, playerOne.getY() + 15));
				delta.nor();

				Rectangle bullet = new Rectangle();
				bullet.x = playerOne.getX() + 15;
				bullet.y = playerOne.getY() + 15;
				bullet.width = 5;
				bullet.height = 5;
				
				Bullet bul = new Bullet(playerOne.getName(), bullet, delta);
				bullets.add(bul);
				out.send(bul);
				playerOne.updateLastShotTime();
				pistolShot.play(0.5f);
			}
		}
		
		Float movement = 200 * deltaTime;
		boolean collision = false;
		Rectangle currPosition = new Rectangle(playerOne.getHitbox());
		Float startX = currPosition.x;
		Float startY = currPosition.y;
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
		{
			currPosition.x += movement;
			for (Rectangle box : boxes)
			{
				if (box.overlaps(currPosition))
				{
					collision = true;
					break;
				}
			}
			currPosition.x = startX;
			if (!collision) playerOne.addX(movement);
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
		{
			collision = false;
			currPosition.x -= movement;
			for (Rectangle box : boxes)
			{
				if (box.overlaps(currPosition))
				{
					collision = true;
					break;
				}
			}
			currPosition.x = startX;
			if (!collision) playerOne.addX(-movement);
		}
		if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
		{
			collision = false;
			currPosition.y += movement;
			for (Rectangle box : boxes)
			{
				if (box.overlaps(currPosition))
				{
					collision = true;
					break;
				}
			}
			currPosition.y = startY;
			if (!collision) playerOne.addY(movement);
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
		{
			collision = false;
			currPosition.y -= movement;
			for (Rectangle box : boxes)
			{
				if (box.overlaps(currPosition))
				{
					collision = true;
					break;
				}
			}
			if (!collision) playerOne.addY(-movement);
		}

		// Hardcoded boundaries, cheaper than doing rectangle checks
		if (playerOne.getX() < 32) playerOne.setX(32f);
		if (playerOne.getX() > WIDTH - 48) playerOne.setX(WIDTH - 48f);
		if (playerOne.getY() < 32) playerOne.setY(32f);
		if (playerOne.getY() > HEIGHT - 48) playerOne.setY(HEIGHT - 48f);
		
		if ((playerOne.getHealth() == 0f) && (TimeUtils.nanoTime() - playerOne.getTimeOfDeath() > 3000000000L)) // Time to respawn
		{
			playerOne.setHealth(100f);
			playerOne.setRandomLocation();
		}
		
		out.send(playerOne);
	}

	@Override
	public void dispose()
	{
		out.send(new Message("QUIT"));
		closeConnection();
		
		groundImage.dispose();
		playerImage.dispose();
		borderImage.dispose();
		boxImage.dispose();
		bulletImage.dispose();
		batch.dispose();
		
		pistolShot.dispose();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
	
	
	// ANYTHING BELOW HERE FOR TESTING PURPOSES ONLY
	private boolean alive;
	
	private String address;
	private int port;
	
	private Socket clientSocket;
	
	private Thread threadIn, threadOut;
	
	private ClientOutput out;
	
	private void test()
	{
		try
		{
			clientSocket = new Socket(address, port);
		}
		catch (IOException e)
		{
			System.out.println("Error: Error creating socket to server");
		}
		
		alive = true;
		threadIn = new Thread(new ClientInput());
		threadOut = new Thread(out = new ClientOutput());
		threadIn.start();
		threadOut.start();
		
		try
		{
			threadOut.join(); // Wait for the thread to establish connection
			out.send(new Message("JOIN"));
		}
		catch (InterruptedException e)
		{
			System.out.println("Error: Error while waiting for connection to server");
		}
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
	
	private class ClientInput implements Runnable
	{
		@Override
		public void run()
		{
			ObjectInputStream objIn = null;
			try
			{
				objIn = new ObjectInputStream(clientSocket.getInputStream());

				if (objIn != null)
				{
					Object receive = null;
					while (alive && ((receive = objIn.readUnshared()) != null))
					{
						if (receive instanceof Player)
						{
							opponents.put(((Player) receive).getName(), (Player) receive);
						}
						else if (receive instanceof Bullet)
						{
							enemyBullets.add((Bullet) receive);
						}
						else if (receive instanceof Score)
						{
							scoreboard.put(((Score) receive).getMessage(), scoreboard.get(((Score) receive).getMessage(), 0) + 1);
						}
						else if (receive instanceof Message)
						{
							opponents.remove(((Message) receive).getMessage().substring(5));
						}
						else if (receive instanceof Rectangle)
						{
							boxes.add((Rectangle) receive);
						}
						receive = null; // Network code is time-critical, having one object and resetting its reference is much faster than allocating a new object each iteration of the loop
					}
				}
			}
			catch (IOException e)
			{
				if (alive) // If the thread IS interrupted, we expect the stream to have closed
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
				if (!Thread.currentThread().isInterrupted()) // If the thread IS interrupted, we expect the stream to have closed
				{
					closeConnection();
				}
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
			}
			catch (IOException e)
			{
				System.out.println("Error: Could not create output writer");
				closeConnection();
			}
		}
		
		public synchronized void send(Object arg)
		{
			if (alive)
			{
				try
				{
					objOut.writeUnshared(arg);
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
