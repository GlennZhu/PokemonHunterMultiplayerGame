package zz23_jj26.client.message.connect;

import java.util.ArrayList;
import java.util.List;

import common.chatroom.IChatroomAdapter;
import common.chatroom.IChatroomID;
import common.message.connect.IChatroomInviteMessage;

/**
 * Concrete IChatRoomInviteMessage
 * @author austurela
 *
 */
public class ChatroomInviteMessage implements IChatroomInviteMessage{
	
	private static final long serialVersionUID = -839461875751005870L;
	/**
	 * The room ID
	 */
	IChatroomID rmID;
	/**
	 * List of adapters
	 */
	List<IChatroomAdapter> myRooms;
	
	/**
	 * Constructor
	 * @param rmID - ID of room
	 * @param myRooms - Adapters
	 */
	public ChatroomInviteMessage(IChatroomID rmID, List<IChatroomAdapter> myRooms) {
		this.rmID = rmID;
		this.myRooms = myRooms;
	}
	
	@Override
	/**
	 * Get the id of the room
	 */
	public IChatroomID getChatroomID() {
		return rmID;
	}

	@Override
	/**
	 * Get the adapter of the room as a shallow copy
	 */
	public List<IChatroomAdapter> getMemberAdapters() {
		ArrayList<IChatroomAdapter> returnVal = new ArrayList<IChatroomAdapter>();
		for (IChatroomAdapter rm : myRooms){
			if (rm.getChatroomID().equals(rmID))
				returnVal.add(rm);
		}
		return returnVal;
	}
}
