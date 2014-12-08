package zz23_jj26.server.userremote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import provided.datapacket.DataPacket;
import common.chatroom.IChatroomAdapter;
import common.message.chat.IChatMessage;

/**
 * Stub that lives in a IChatroomToChatroomAdapter. Used to send IChatMessages
 * @author austurela
 *
 */
public interface IUserChatRemote extends Remote, Serializable{
	
	/**
	 * User calls this to delegate sending IChatMessages.
	 * @param message -Message to send
	 * @param sendingAdpt -Adapter that sent the message
	 * @return The response (in a datapacket)
	 * @throws RemoteException
	 */
	public DataPacket<? extends IChatMessage> sendMessage(DataPacket<? extends IChatMessage> message, IChatroomAdapter sendingAdpt) throws RemoteException;
}
