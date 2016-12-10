package game;

import java.io.Serializable;

public class Score implements Serializable // Cannot mix primitives and objects over stream, so we need a class to carry Strings
{
	private static final long serialVersionUID = 1L;

	private String message;
	
	public Score(String inputMessage)
	{
		message = inputMessage;
	}
	
	public String getMessage()
	{
		return message;
	}
}
