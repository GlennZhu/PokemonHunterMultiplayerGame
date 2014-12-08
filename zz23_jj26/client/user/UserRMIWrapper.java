package zz23_jj26.client.user;

import java.net.Inet4Address;
import java.rmi.RemoteException;

import zz23_jj26.client.userremote.IUserConnectRemote;
import common.user.IUser;
import common.user.IUserRMIWrapper;

/**
 * Remote wrapper to transmit User
 * @author austurela
 *
 */
public class UserRMIWrapper implements IUserRMIWrapper{
	/**
	 * User to transmit
	 */
	private IUser user;	
	
	/**
	 * Constructor
	 * @param ip - IP of user
	 * @param username - user's name
	 * @param userremote - Stub of user that user will delegate to
	 */
	public UserRMIWrapper(Inet4Address ip, String username, IUserConnectRemote userremote){
		this.user = new User(ip, username, userremote);
	}
	
	@Override
	/**
	 * Get the user from the wrapper
	 */
	public IUser getUser() throws RemoteException {
		return user;
	}
}
