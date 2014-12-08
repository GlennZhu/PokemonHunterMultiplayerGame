package common.message.connect;

import java.util.List;

import common.chatroom.IChatroomID;
import common.chatroom.IChatroomAdapter;

/**
 * Message that invites a user to a pre-existing chatroom. Response is an
 * INullMessage if the user is not yet in the chatroom (and therefore accepts),
 * IRejectRequestMessage if the user is already in the chatroom (and therefore rejects).
 *
 * @author Jake Kornblau, Alden Higgens, Derek Peirce
 */
public interface IChatroomInviteMessage extends IConnectMessage {

    /**
     * Get the ID of the chatroom.
     *
     * @return the chatroom ID
     */
    public IChatroomID getChatroomID();

    /**
     * Get the adapters to the chatrooms of the other members of the chatroom.
     *
     * @return a list of adapters to the chatroom objects.
     */
    public List<IChatroomAdapter> getMemberAdapters();

	/**
	 * The name of the person who is trying to invite you.
	 * @return inviter's name
	 */
	default public String getName() {
		return "Someone";
	}

}