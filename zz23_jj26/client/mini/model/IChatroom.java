package zz23_jj26.client.mini.model;

import java.io.Serializable;
import java.util.List;

import common.chatroom.IChatroomAdapter;
import common.chatroom.IChatroomID;

/**
 * Interface for an IChatroom
 * @author austurela
 *
 */
public interface IChatroom extends Serializable{
	/**
	 * get the ID of a room
	 * @return the id
	 */
	public IChatroomID getID();
	/**
	 * Get the adapter of everyone in the room
	 * @return the adapters in a list
	 */
	public List<IChatroomAdapter> getAdapters();
	/**
	 * Sent a message to everyone in the room
	 * @param text - the message
	 */
	public void sendTextMessage(String text);
	/**
	 * Get the adapter to yourself
	 * @return an adapter to yourself
	 */
	public IChatroomAdapter getAdapterToMe();
	/**
	 * Start the room
	 */
	public void start();
	/**
	 * Add a new person
	 * @param newGuy - the new person's adapter
	 */
	public void addAdapter(IChatroomAdapter newGuy);
	/**
	 * Quit the room
	 */
	public void quit();
	/**
	 * get a shallow copy of the list of adapters
	 * @return the shallow copy
	 */
	public List<IChatroomAdapter> getCopyAdapters();
	/**
	 * Send a smilie to everyone in the room
	 */
	public void sendSmilie();
	
	
	
}
