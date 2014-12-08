package common.user;

import java.io.Serializable;
import java.net.Inet4Address;
import java.rmi.RemoteException;

import provided.datapacket.DataPacket;

import common.message.connect.IConnectMessage;

/**
 * Interface for ChatApp Users (one per client)
 *
 * @author Jake Kornblau, Derek Peirce
 *
 */
public interface IUser extends Serializable {

	/** Returns the user's username */
	@Override
	public String toString();

	/**
	 * Returns the IP Address of the user
	 *
	 * @return the IPAddress of the user
	 */
	public Inet4Address getIPAddress();

	/**
	 * Sends a message used for getting or requesting chatrooms.
	 *
	 * @param message
	 *            an IConnectMessage for requesting or inviting a chatroom.
	 * @param sendingUser
	 *            the user who sent the message
	 * @return The results of the request or invite
	 * @throws RemoteException if there was an exception in remotely sending the message to the user.
	 */
	public DataPacket<? extends IConnectMessage> sendMessage(
			DataPacket<? extends IConnectMessage> message, IUser sendingUser)
			throws RemoteException;
}
