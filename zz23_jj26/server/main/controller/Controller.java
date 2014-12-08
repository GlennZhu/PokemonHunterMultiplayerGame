package zz23_jj26.server.main.controller;

import java.util.List;

import common.chatroom.IChatroomID;
import common.user.IUser;
import zz23_jj26.server.main.model.ChatroomManager;
import zz23_jj26.server.main.model.IModel2ViewAdapter;
import zz23_jj26.server.main.view.IView2ModelAdapter;
import zz23_jj26.server.main.view.ServerGUI;
import zz23_jj26.server.mini.model.IChatroom;

/**
 * The main controller for the main MVC system.
 * @author austurela
 *
 */
public class Controller {

	/**
	 * The main view (Client)
	 */
	private ServerGUI view;
	/**
	 * The main model to take care of all the chatrooms
	 */
	private ChatroomManager manager;
	
	/**
	 * Constructor
	 */
	public Controller() {
		/**
		 * The view
		 */
		view = new ServerGUI(new IView2ModelAdapter() {
			
			@Override
			public void quit() {
				manager.quitAllRooms();
				System.exit(0);
			}
			
			@Override
			public String connectTo(String remoteHost) {
				return manager.connectTo(remoteHost);
				
			}

			@Override
			public void setName(String text) {
				manager.setName(text);
				
			}

			@Override
			public List<IChatroomID> getRooms(IUser connectedUser) {
				return manager.askRemoteChatRooms(connectedUser);
			}

			@Override
			public void makeRoom(String roomName) {
				manager.makeRoom(roomName);
			}

			@Override
			public void joinRoom(IChatroomID toJoin, IUser selecteduser) {
				manager.askToJoinRoom(toJoin, selecteduser);
				
				
			}

			@Override
			public void sendInvite(IUser userToInvite,
					IChatroomID roomToInviteTo) {
				manager.inviteUserToRoom(userToInvite, roomToInviteTo);
				
			}

		});
		
		/**
		 * The model
		 */
		manager = new ChatroomManager(new IModel2ViewAdapter() {

			@Override
			public void append(String string) {
				view.append(string);
			}

			@Override
			public void setRemoteHost(String hostAddress) {
				view.setRemoteHost(hostAddress);
			}

			@Override
			public void addUserToChatroom(IChatroomID query, String username) {
				view.addUserToChatroom(query, username);
				
			}

			@Override
			public boolean respondToRoomRequest(String room, String name) {
				
				return view.respondToRoomRequest(room, name);
			}

			@Override
			public void addUserToFriends(IUser otherUser) {
				view.addUserToFriendsList(otherUser);
				
			}

			@Override
			public boolean sendInvitePrompt(IChatroomID chatroomID, IUser iUser) {
				return view.sendInvitePrompt(chatroomID, iUser);
			}

			@Override
			public void addNewRoomToList(IChatroom newRoom) {
				view.addNewRoomToList(newRoom);
			}

			@Override
			public void removeRoomFromList(IChatroom roomToQuit) {
				view.removeRoomFromList(roomToQuit);
			}
		});
	}
	
	/**
	 * Starts the view then the model.  The view needs to be started first so that it can display 
	 * the model status updates as it starts.
	 */
	public void start(){
		view.start();
		manager.start();
	}


	/**
	 * Main() method of the client application. Instantiates and then starts the controller.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		(new Controller()).start();
	}

}
