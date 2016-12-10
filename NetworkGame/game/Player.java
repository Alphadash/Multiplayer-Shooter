package game;

import java.io.Serializable;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Player implements Serializable
{
	/**
	 * 1L: First edition, name/health/position
	 * 2L: Put lastShotTime into Player class
	 * 3L: Added timeOfDeath
	 * 4L: Added random location method
	 */
	private static final long serialVersionUID = 4L;
	
	private String name;
	private float health;
	private float posX, posY;
	private Long lastShotTime;
	private Long timeOfDeath;
	
	public Player(String inputName)
	{
		name = inputName;
		health = 100f;
		lastShotTime = TimeUtils.nanoTime();
		timeOfDeath = 0L;
		setRandomLocation();
	}
	
	public void setRandomLocation()
	{
		if (MathUtils.random(1) == 0) // Spawn somewhere near top/bottom
		{
			posX = MathUtils.random(42f, 758f);
			posY = 42f + (MathUtils.random(1) * 556f);
		}
		else // Spawn somewhere near left/right
		{
			posX = 42f + (MathUtils.random(1) * 716f);
			posY = MathUtils.random(42f, 598f);
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public float getHealth()
	{
		return health;
	}
	
	public void setHealth(float inputHealth)
	{
		health = inputHealth;
	}
	
	public float getX()
	{
		return posX;
	}
	
	public void setX(Float inputX)
	{
		posX = inputX;
	}
	
	public void addX(Float inputAdd)
	{
		posX += inputAdd;
	}
	
	public float getY()
	{
		return posY;
	}
	
	public void setY(Float inputY)
	{
		posY = inputY;
	}
	
	public void addY(Float inputAdd)
	{
		posY += inputAdd;
	}
	
	public Rectangle getHitbox()
	{
		return GameUtils.createRectangle(posX, posY, 20f, 14f);
	}
	
	public Long getLastShotTime()
	{
		return lastShotTime;
	}
	
	public void updateLastShotTime()
	{
		lastShotTime = TimeUtils.nanoTime();
	}
	
	public Long getTimeOfDeath()
	{
		return timeOfDeath;
	}
	
	public void updateTimeOfDeath()
	{
		timeOfDeath = TimeUtils.nanoTime();
	}
}
