package zz23_jj26.server.message.connect;

import common.chatroom.IChatroomID;
import common.message.connect.IRequestChatroomMessage;

/**
 * Concrete IRequestChatroomMessage
 * @author austurela
 *
 */
public class RequestChatroomMessage implements IRequestChatroomMessage{
	private static final long serialVersionUID = -471878883126200965L;

	/**
	 * Id of room you request to join
	 */
	private IChatroomID id; 
	/**
	 * Your name
	 */
	private String name;
	
	/**
	 * Constructor
	 * @param id - the room id
	 * @param name - your name
	 */
	public RequestChatroomMessage(IChatroomID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	/**
	 * Get the room id
	 */
	public IChatroomID getChatroomID() {
		return id;
	}

	@Override
	/**
	 * Get your name
	 */
	public String getName(){
		return name;
	}
}
