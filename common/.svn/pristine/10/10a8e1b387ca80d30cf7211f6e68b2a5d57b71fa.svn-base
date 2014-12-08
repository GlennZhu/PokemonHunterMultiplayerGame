package common.user;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote object that wraps a serializable IUser. Placed in a registry under the BOUND_NAME for other users to access.
 *
 * @author Jake Kornblau, Derek Peirce
 *
 */
public interface IUserRMIWrapper extends Remote {
	/** String used for bounding the client user in the registry */
	public static final String BOUND_NAME_CLIENT = "IUser_Client";
	
	/** String used for bounding the server user in the registry */
	public static final String BOUND_NAME_SERVER = "IUser_Server";
	
	/** Port for exporting the client's user wrapper */
	public static final int EXPORT_PORT_CLIENT = 2101;
	
	/** Port for exporting the server's user wrapper */
	public static final int EXPORT_PORT_SERVER = 2102;

	/**
	 * Gets the IUser from the remote user.
	 * @return the IUser of the remote user
	 * @throws RemoteException if there was an exception in remotely getting the IUser.
	 */
	public IUser getUser() throws RemoteException;
}
