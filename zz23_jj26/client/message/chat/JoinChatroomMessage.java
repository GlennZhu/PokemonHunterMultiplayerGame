package zz23_jj26.client.message.chat;

import common.chatroom.IChatroomAdapter;
import common.message.chat.IJoinChatroomMessage;

/**
 * Concrete IJoinChatroomMessage
 *
 */
public class JoinChatroomMessage implements IJoinChatroomMessage {

	private static final long serialVersionUID = -2279647627255001297L;
	/**
	 * Sending adapter
	 */
	IChatroomAdapter adapterToMe;
	
	/**
	 * Constructor
	 * @param adapter - sending adapter
	 */
    public JoinChatroomMessage(IChatroomAdapter adapter) {
		adapterToMe = adapter;
	}
	
	@Override
	/**
	 * return the sending adapter
	 */
	public IChatroomAdapter getAdapter() {
		return adapterToMe;
	}

}
