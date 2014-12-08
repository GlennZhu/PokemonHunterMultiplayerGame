package common.message.connect;

import java.util.List;

import common.chatroom.IChatroomID;

/**
 * A message listing the chatrooms that a person is connected to.
 *
 * @author Jake Kornblau, Alden Higgens, Derek Peirce
 */
public interface IChatroomsListMessage extends IConnectMessage {

    /**
     * Return a list of chatrooms that the user is connected to.
     *
     * @return the list of chatrooms
     */
    public List<IChatroomID> getChatroomIDs();

}