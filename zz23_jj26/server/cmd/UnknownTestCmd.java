package zz23_jj26.server.cmd;

import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;

/**
 * Concrete class for the test command
 * @author austurela
 *
 */
public class UnknownTestCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>{
	private static final long serialVersionUID = -122135912082317178L;
	
	/**
	 * A adapter to the model that is not passed
	 */
	private transient ICmd2ModelAdapter cmdAdpt;

	/**
	 * Constructor
	 * @param cmdAdpt - The adapter to the model
	 */
	public UnknownTestCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}
	
	@Override
	/**
	 * Apply the visitor
	 */
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<IChatMessage> host, IChatroomAdapter... params) {
		cmdAdpt.append(":---)");
		return new DataPacket<INullMessage>(
				INullMessage.class,
				NullMessage.SINGLETON);
	}

	@Override
	/**
	 * Set the adapter to the model
	 */
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		cmdAdpt = cmd2ModelAdpt;
	}
}
