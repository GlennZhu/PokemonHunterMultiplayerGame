package common.message.chat;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;

import common.chatroom.IChatroomAdapter;

/**
 * Message for sending the lambda for an unknown command. Should be responded to with an INullMessage
 *
 * @author Wilson Beebe, Jake Kornblau, Derek Peirce
 */
public interface ISendCmdMessage extends IChatMessage {

    /**
     * The command being sent.
     *
     * @return the command that was sent.
     */
    public ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, ?, IChatroomAdapter> getCmd();

    /**
     * Gets the id the command to be installed.
     *
     * @return the Class on which the command is to operate
     */
    public Class<?> getCmdID();
}