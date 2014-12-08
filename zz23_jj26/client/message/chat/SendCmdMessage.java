package zz23_jj26.client.message.chat;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import common.chatroom.IChatroomAdapter;
import common.message.chat.IChatMessage;
import common.message.chat.ISendCmdMessage;


/**
 * Concrete ISendCmdMessage
 * @author austurela
 *
 */
public class SendCmdMessage implements ISendCmdMessage {
	private static final long serialVersionUID = -5898824878448068804L;
	/**
	 * ID 
	 */
	private Class<?> cmdID;
	/**
	 * The CMd to send
	 */
	private ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, ?, IChatroomAdapter> cmd;

	/**
	 * Constructor
	 * @param cmdID - the id of the cmd
	 * @param cmd - the cmd
	 */
	public SendCmdMessage(Class<?> cmdID, ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, ?, IChatroomAdapter> cmd) {
		this.cmdID = cmdID;
		this.cmd = cmd;
	}

	@Override
	/**
	 * Return the cmd
	 */
	public ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, ?, IChatroomAdapter> getCmd() {
		return cmd;
	}

	@Override
	/**
	 *  Return the id
	 */
	public Class<?> getCmdID() {
		return cmdID;
	}

}
