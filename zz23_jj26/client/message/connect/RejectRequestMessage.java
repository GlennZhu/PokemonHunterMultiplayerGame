package zz23_jj26.client.message.connect;

import common.chatroom.IChatroomID;
import common.message.connect.IRejectRequestMessage;

/**
 * Concrete IRejectRequestMessage
 * @author austurela
 *
 */
public class RejectRequestMessage implements IRejectRequestMessage {
	private static final long serialVersionUID = 6328436258852240820L;
	/**
	 * Id of the room you're not joining
	 */
	private IChatroomID id;
	/**
	 * Constructor
	 * @param id - id of the room
	 */
	public RejectRequestMessage(IChatroomID id) {
		this.id = id;
	}
	@Override
	/**
	 * Return the ID
	 */
	public IChatroomID getChatroomID() {
		return id;
	}

}
