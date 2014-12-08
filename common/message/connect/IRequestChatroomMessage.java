package common.message.connect;

import common.chatroom.IChatroomID;

/**
 * Initial request sent in order to join a chatroom with a specific ID.
 * Should be responded to with an IChatroomInviteMessage if the request is
 * accepted, an IRejectRequestMessge if the request is rejected.
 *
 * @author Wilson Beebe, Derek Peirce
 */
public interface IRequestChatroomMessage extends IConnectMessage {

	/**
	 * The chatroom ID the person is trying to join.
	 *
	 * @return the chatroom ID the person is trying to join
	 */
	public IChatroomID getChatroomID();

	/**
	 * The name of the person who is trying to join.
	 * @return requester's name
	 */
	default public String getName() {
		return "Someone";
	}
}
