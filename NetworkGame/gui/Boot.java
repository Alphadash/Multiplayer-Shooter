package gui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Boot
{
	public static void main(String[] args)
	{
		new LwjglApplication(new Game(args[0], args[1], Integer.valueOf(args[2])), "Game", 800, 640, true);
	}
}
