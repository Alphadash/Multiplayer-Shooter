package game;

import java.io.Serializable;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements Serializable
{
	/**
	 * 1L: Hitbox and offset
	 * 2L: Added owner (for kill tracking)
	 */
	private static final long serialVersionUID = 2L;
	
	private String owner;
	private Rectangle hitbox;
	private Vector2 offset;

	public Bullet(String inputOwner, Rectangle inputHitbox, Vector2 inputOffset)
	{
		owner = inputOwner;
		hitbox = inputHitbox;
		offset = inputOffset;
	}
	
	public String getOwner()
	{
		return owner;
	}
	
	public Rectangle getHitbox()
	{
		return hitbox;
	}
	
	public float getX()
	{
		return hitbox.x;
	}
	
	public void addX(float inputX)
	{
		hitbox.x += inputX;
	}
	
	public float getY()
	{
		return hitbox.y;
	}
	
	public void addY(float inputY)
	{
		hitbox.y += inputY;
	}
	
	public float getOffsetX()
	{
		return offset.x;
	}
	
	public float getOffsetY()
	{
		return offset.y;
	}
}
