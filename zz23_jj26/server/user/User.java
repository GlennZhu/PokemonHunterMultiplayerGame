package zz23_jj26.server.user;

import java.net.Inet4Address;
import java.rmi.RemoteException;

import zz23_jj26.server.userremote.IUserConnectRemote;
import provided.datapacket.DataPacket;
import common.message.connect.IConnectMessage;
import common.user.IUser;

/**
 * A serializable IUser that has a remote stub in it
 * @author austurela
 *
 */
public class User implements IUser{

	private static final long serialVersionUID = 2128354556837649369L;
	/**
	 * Name of the user.
	 */
	private String username;
	/**
	 * The user's IP
	 */
	private Inet4Address ip;
	/**
	 * The remote stub that lives in the user. Message sending is delegated to it.
	 */
	private IUserConnectRemote userremote;

	/**
	 * Constructor
	 * @param ip - ipaddress
	 * @param username - desired name
	 * @param userremote - the stub to delegate to
	 */
	public User(Inet4Address ip, String username, IUserConnectRemote userremote) {
		this.ip = ip;
		this.username = username;
		this.userremote = userremote;
	}
	
	
	/**
	 * Return the IP address
	 * 
	 * 
	 */
	@Override
	public Inet4Address getIPAddress() {
		return ip;
	}

	@Override
	/**
	 * Send a message by delegating to the stub
	 */
	public DataPacket<? extends IConnectMessage> sendMessage(
			DataPacket<? extends IConnectMessage> message, IUser sender)
			throws RemoteException {
		return userremote.sendMessage(message, sender);
	}
	
	@Override
	/**
	 * Return the user's name
	 */
	public String toString(){
		return username;
	}

	@Override
	/**
	 * Check equality
	 */
	public boolean equals(Object other){
		if (other == this) return true;
		if (!(other instanceof User)) return false;
		User otherUser = (User)other;
		return username.toString().equals(otherUser.toString()) && ip.equals(otherUser.getIPAddress());
	}
	
	@Override
	/**
	 * For equality testing
	 */
	public int hashCode(){
		return username.hashCode() + ip.hashCode();
	}

	
}
