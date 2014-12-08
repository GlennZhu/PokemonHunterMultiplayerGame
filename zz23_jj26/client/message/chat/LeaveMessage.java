package zz23_jj26.client.message.chat;

import common.chatroom.IChatroomAdapter;
import common.message.chat.ILeaveMessage;

/**
 * Concrete ILeaveMessage
 * @author austurela
 *
 */
public class LeaveMessage implements ILeaveMessage {
	private static final long serialVersionUID = 8755222288901214353L;
	/**
	 * Sending adapter
	 */
	private IChatroomAdapter adapterToSender;
	
	/**
	 * Constructor
	 * @param adapterToSender - Sending adapter
	 */
	public LeaveMessage(IChatroomAdapter adapterToSender) {
		this.adapterToSender = adapterToSender;
	}
	
	@Override
	/**
	 * get the Sending adapter
	 */
	public IChatroomAdapter getAdapter() {
		
		return adapterToSender;
	}

}
