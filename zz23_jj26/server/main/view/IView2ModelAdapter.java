package zz23_jj26.server.main.view;

import java.util.List;

import common.chatroom.IChatroomID;
import common.user.IUser;

/**
 * Adapter the view uses to communicate to the model
 */
public interface IView2ModelAdapter {
	/**
	 * Requests that model connect to the RMI Registry at the given remote host
	 * 
	 * @param remoteHost
	 *            The remote host to connect to.
	 * @return A status string regarding the connection result
	 */
	public String connectTo(String remoteHost);

	/**
	 * Quits the applications and gracefully shuts down the RMI-related resources.
	 */
	public void quit();

	/**
	 * Choose your name
	 * @param text - desired name
	 */
	public void setName(String text);

	/**
	 * Return a list of the selected user's rooms
	 *  
	 * @param connectedUser - the user to get the rooms that they're in 
	 * @return the list of rooms.
	 */
	public List<IChatroomID> getRooms(IUser connectedUser);

	/**
	 * Make a room with the given name
	 * @param roomName -name of the room
	 */
	public void makeRoom(String roomName);

	/**
	 * Join the selected room that the given user is in
	 * @param toJoin - the room to join
	 * @param iUser - the user that was in the room that you want to send you an invite
	 */
	public void joinRoom(IChatroomID toJoin, IUser iUser);

	/**
	 * Send an invite to a user to join the room that you're in
	 * @param userToInvite - user to invite
	 * @param roomToInviteTo -room that you're in.
	 */
	public void sendInvite(IUser userToInvite, IChatroomID roomToInviteTo);
	
}