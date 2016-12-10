package game;

import java.io.Serializable;

public class Message implements Serializable // Cannot mix primitives and objects over stream, so we need a class to carry Strings
{
	private static final long serialVersionUID = 1L;

	private String message;
	
	public Message(String inputMessage)
	{
		message = inputMessage;
	}
	
	public String getMessage()
	{
		return message;
	}
}
