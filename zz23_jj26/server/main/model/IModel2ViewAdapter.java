package zz23_jj26.server.main.model;

import zz23_jj26.server.mini.model.IChatroom;
import common.chatroom.IChatroomID;
import common.user.IUser;

public interface IModel2ViewAdapter {

	public void append(String string);

	/**
	 * Sets the displayed remote host address.
	 * @param hostAddress The IP address or host name of the remote host.
	 */
	public void setRemoteHost(String hostAddress);

	/**
	 * Add a given user to the chatroom
	 * @param query - the chatroom
	 * @param string -the username
	 */
	public void addUserToChatroom(IChatroomID query, String string);

	/**
	 * Respond to a request to join a room
	 * @param room - the room they wish to join
	 * @param name - the user that wishes to join
	 * @return true if accept. False otherwise
	 */
	public boolean respondToRoomRequest(String room, String name);

	/**
	 * Add a user to your list of friends
	 * @param otherUser2 - the user to add
	 */
	public void addUserToFriends(IUser otherUser2);

	/**
	 * Send an invite prompt to a user if they want to join the room
	 * @param chatroomID - the room they may want to join
	 * @param iUser - the user that may want to join
	 * @return - true if they accept. false if they decline.
	 */
	public boolean sendInvitePrompt(IChatroomID chatroomID, IUser iUser);

	/**
	 * Add a new room to your list of rooms
	 * @param newRoom - the room to add
	 */
	public void addNewRoomToList(IChatroom newRoom);

	/**
	 * Quit from a given room 
	 * @param roomToQuit - room to quit from.
	 */
	public void removeRoomFromList(IChatroom roomToQuit);
}
