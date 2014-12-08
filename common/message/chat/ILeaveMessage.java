package common.message.chat;

import common.chatroom.IChatroomAdapter;

/**
 * Data type for ILeaveChatroom message, which indicates that the sender is
 * leaving the chatroom. It should be responded to with an INullMessage
 *
 * @author Wilson Beebe
 *
 */
public interface ILeaveMessage extends IChatMessage {

    /**
     * Gets the adapter to the chatroom that sent the message
     *
     * @return the adapter to the chatroom that sent the message
     */
    public IChatroomAdapter getAdapter() ;

}