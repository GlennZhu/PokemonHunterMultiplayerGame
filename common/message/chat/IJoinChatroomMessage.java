package common.message.chat;

import common.chatroom.IChatroomAdapter;

/**
 * Data type for JoinChatroom message, which indicates to a user that the
 * sending user has joined the chatroom and should be added to the list of
 * chatroom recipients, it should be responded to with a respective
 * INullMessage
 *
 * @author Derek Peirce
 */
public interface IJoinChatroomMessage extends IChatMessage {

    /**
     * Gets the adapter to the chatroom that sent the message
     *
     * @return the adapter to the chatroom that sent the message
     */
    public IChatroomAdapter getAdapter();

}