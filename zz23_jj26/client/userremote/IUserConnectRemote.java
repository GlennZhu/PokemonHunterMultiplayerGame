package zz23_jj26.client.userremote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import provided.datapacket.DataPacket;
import common.message.connect.IConnectMessage;
import common.user.IUser;

/**
 * Stub that lives in a user. Used to send IConnectMessages
 * @author austurela
 *
 */
public interface IUserConnectRemote extends Remote, Serializable{
	
	/**
	 * Delegate to the stub to send the message.
	 * @param message - IconnectMessage to be sent
	 * @param sendingUser -User that sent the message
	 * @return - The IConnectMessage response. (in a datapacket).
	 * @throws RemoteException	remoteException
	 */
	public DataPacket<? extends IConnectMessage> sendMessage(DataPacket<? extends IConnectMessage> message, IUser sendingUser) throws RemoteException;
}
