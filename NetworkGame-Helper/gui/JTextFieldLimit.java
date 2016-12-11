package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {
	
	  private int limit;
	  
	  public JTextFieldLimit(int l)
	  {
		  limit = l;
	  }
	  
	  public void insertString(int offset, String s, AttributeSet set) throws BadLocationException 
	  {
		  if (s == null)
			  return;
		  else if (getLength() + s.length() <= limit)
		  {
			  super.insertString(offset, s, set);
		  }
	  }
}
