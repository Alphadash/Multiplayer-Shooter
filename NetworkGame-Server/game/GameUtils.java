package game;

import com.badlogic.gdx.math.Rectangle;

public class GameUtils
{
	public static Rectangle createRectangle(int inputX, int inputY)
	{
		return createRectangle(inputX, inputY, 32, 32);
	}
	
	public static Rectangle createRectangle(float inputX, float inputY, float inputW, float inputH)
	{
		Rectangle rectangle = new Rectangle();
		rectangle.x = inputX;
		rectangle.y = inputY;
		rectangle.width = inputW;
		rectangle.height = inputH;
		return rectangle;
	}
}
