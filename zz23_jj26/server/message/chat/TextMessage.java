package zz23_jj26.server.message.chat;

import common.message.chat.ITextMessage;

/**
 * Concrete ITextMessage
 * @author austurela
 *
 */
public class TextMessage implements ITextMessage {

	private static final long serialVersionUID = -2497326541758597378L;
	/**
	 * The string in the text
	 */
	private String myText;

	/**
	 * Constructor
	 * @param text - to be send
	 **/
	public TextMessage(String text) {
		this.myText = text; 
	}
	
	@Override
	/**
	 * get the text
	 */
	public String getText() {
		return myText;
	}

}
