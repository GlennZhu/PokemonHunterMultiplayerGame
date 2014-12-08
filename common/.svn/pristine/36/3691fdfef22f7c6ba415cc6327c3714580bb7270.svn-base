package common.chatroom;

import java.io.Serializable;
import java.rmi.RemoteException;

import provided.datapacket.DataPacket;

import common.message.chat.IChatMessage;
import common.user.IUser;

/**
 * Adapter to a chatroom instance. There is one adapter for
 * each instance of a chatroom for each user.
 *
 * @author Derek Peirce, Jake Kornblau
 *
 */
public interface IChatroomAdapter extends Serializable {

	/**
	 * Gets the user on the other end of the adapter
	 *
	 * @return user at the other end of the adapter
	 */
	public IUser getUser();

	/**
	 * Gets the ID of the chatroom this adapter adapts
	 *
	 * @return the chatroom ID
	 */
	public IChatroomID getChatroomID();

	/**
	 * Sends the message to the other chatroom
	 *
	 * @param message
	 *            a DataPacket containing the message to be send to the other
	 *            chatroom
	 * @param sendingAdapter
	 * 			  the IChatroomToChatroomAdapter for this adapter.
	 * @return A DataPacket containing an IChatroomMessage with the response of
	 *         sending the message.
	 * @throws RemoteException if there was an exception in trying to remotely send the message to the IChatroomToChatroomAdapter.
	 */
	public DataPacket<? extends IChatMessage> sendChatroomMessage(
			DataPacket<? extends IChatMessage> message, IChatroomAdapter sendingAdapter) throws RemoteException;


	/**
	 * Determines if two IChatroomToChatroomAdapters are equal. Instances must still
	 * retain their original equality even after multiple deserialzation.
	 *
	 * @param o the Object to determine equality to.
	 * @return a boolean true o
	 */
	@Override
	public abstract boolean equals(Object o);

	/**
	 * Two IChatroomToChatroomAdapters must return the same hash code
	 * if they are equal according to the equals function.
	 *
	 * @return the integer hash-code for this IChatroomToChatroomAdapter
	 */
	@Override
	public abstract int hashCode();
}