package zz23_jj26.client.mini.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.chatroom.IChatroomID;

/**
 * Id of a chatroom
 * @author austurela
 *
 */
public class ChatroomID implements IChatroomID{
	
	private static final long serialVersionUID = 5311778547052367452L;
	/**
	 * Name of the room
	 */
	private String roomName;
	/**
	 * Timestamp of the room's creating
	 */
	private long timestamp;
	/**
	 * IP of the  original creator
	 */
	private InetAddress ip;
	
	@SuppressWarnings("unused")
	private ChatroomID(){}
	
	/**
	 * construcutor
	 * @param name -desired room name
	 */
	public ChatroomID(String name) {
		this.roomName = name;
		timestamp = System.currentTimeMillis();
		try{
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the time stamp of creation
	 * @return the time
	 */
	public long getTimestamp(){
		return timestamp;
	}
	
	/**
	 * Get the ip of the original creator
	 * @return the IP
	 */
	public InetAddress getIP(){
		return ip;
	}
	
	@Override
	/**
	 * Get the name of the room
	 */
	public String toString(){
		return roomName;
	}

	@Override
	/**
	 * Check for equality between the ids of chatrooms
	 */
	public boolean equals(Object other){
        if (other == this) {
            return true;
        }
        if (!(other instanceof ChatroomID)) {
            return false;
        }
		ChatroomID otherID = (ChatroomID)other;
		return roomName.equals(otherID.toString()) && ip.equals(otherID.getIP()) && (timestamp == otherID.getTimestamp());
	}
	
	@Override
	/**
	 * Used in equality checks
	 */
	public int hashCode(){
		return roomName.hashCode() + ip.hashCode();
	}
}
