package zz23_jj26.server.message.connect;

import java.util.List;

import common.chatroom.IChatroomID;
import common.message.connect.IChatroomsListMessage;

/**
 * Concrete IChatroomListMessage
 * @author austurela
 *
 */
public class ChatroomListMessage implements IChatroomsListMessage {

	private static final long serialVersionUID = 2008477849974224834L;
	/**
	 * List of chatroomId's
	 */
	private List<IChatroomID> listOfID;
	
	/**
	 * Constructor
	 * @param listOfID -list of chatroomId
	 */
    public ChatroomListMessage(List<IChatroomID> listOfID) {
		this.listOfID = listOfID;
	}
	
	@Override
	/**
	 * Get the chatroomID list
	 */
	public List<IChatroomID> getChatroomIDs() {
		return listOfID;
	}

}
