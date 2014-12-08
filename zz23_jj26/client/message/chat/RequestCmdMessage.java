package zz23_jj26.client.message.chat;

import common.message.chat.IRequestCmdMessage;

/**
 * Concrete IRequestCmdMessage
 * @author austurela
 *
 */
public class RequestCmdMessage implements IRequestCmdMessage{
	private static final long serialVersionUID = 7336465455373486129L;
	/**
	 * The ID of the message
	 */
	private Class<?> messageID;
	
	/**
	 * Constructor
	 * @param messageID - the id
	 */
	public RequestCmdMessage(Class<?> messageID) {
		this.messageID = messageID;
	}
	
	@Override
	/**
	 * Return the ID
	 */
	public Class<?> getMessageID() {
		return messageID;
	}
}
