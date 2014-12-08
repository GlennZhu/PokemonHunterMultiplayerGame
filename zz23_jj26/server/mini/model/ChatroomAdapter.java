package zz23_jj26.server.mini.model;

import java.rmi.RemoteException;

import zz23_jj26.server.userremote.IUserChatRemote;
import provided.datapacket.DataPacket;
import common.chatroom.IChatroomAdapter;
import common.chatroom.IChatroomID;
import common.message.chat.IChatMessage;
import common.user.IUser;

/**
 * Concrete IChatroomToChatroomAdapter
 * @author austurela
 *
 */
public class ChatroomAdapter implements IChatroomAdapter {

	private static final long serialVersionUID = -3772812198717654082L;
	/**
	 * User it points to
	 */
	private IUser meUser;
	/**
	 * Room you're in
	 */
	private IChatroomID id;
	/**
	 * Stub to the user it points to
	 */
	private IUserChatRemote algoStub;

	/**
	 * Constructor
	 * @param meuser -user it points to
	 * @param id - id of the room
	 * @param algoStub - stub of the user it points to
	 */
	public ChatroomAdapter(IUser meuser, IChatroomID id, IUserChatRemote algoStub){
		this.meUser = meuser;
		this.id = id;
		this.algoStub = algoStub;
	}

	@Override
	/**
	 * Return user it points to
	 */
	public IUser getUser() {
		return meUser;
	}

	@Override
	/**
	 * returnthe ID of the room
	 */
	public IChatroomID getChatroomID() {
		return id;
	}

	@Override
	/**
	 * Delegate the visitor to the stub
	 */
	public DataPacket<? extends IChatMessage> sendChatroomMessage(
			DataPacket<? extends IChatMessage> message,
			IChatroomAdapter sendingAdapter) throws RemoteException {
		return algoStub.sendMessage(message, sendingAdapter);
	}
	
	@Override
	/**
	 * Check for equality
	 */
	public boolean equals(Object other){
		if (other == this) return true;
		if (!(other instanceof IChatroomAdapter)) return false;
		IChatroomAdapter otherAdpt = (IChatroomAdapter) other;
		return meUser.equals(otherAdpt.getUser());
	}
	
	@Override
	/**
	 * User for equality checks
	 */
	public int hashCode(){
		return meUser.hashCode();
	}
}