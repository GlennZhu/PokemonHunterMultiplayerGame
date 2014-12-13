package zz23_jj26.server.main.model;

import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.chatroom.IChatroomAdapter;
import common.chatroom.IChatroomID;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IJoinChatroomMessage;
import common.message.connect.IChatroomInviteMessage;
import common.message.connect.IChatroomsListMessage;
import common.message.connect.IConnectBack;
import common.message.connect.IConnectMessage;
import common.message.connect.IGetChatroomsListMessage;
import common.message.connect.IRejectRequestMessage;
import common.message.connect.IRequestChatroomMessage;
import common.user.IUser;
import common.user.IUserRMIWrapper;
import provided.datapacket.DataPacket;
import provided.util.IVoidLambda;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import zz23_jj26.server.engine.IRemoteTaskViewAdapter;
import zz23_jj26.server.message.chat.JoinChatroomMessage;
import zz23_jj26.server.message.connect.ChatroomInviteMessage;
import zz23_jj26.server.message.connect.ConnectBack;
import zz23_jj26.server.message.connect.ConnectMessageVisitor;
import zz23_jj26.server.message.connect.GetChatroomsListMessage;
import zz23_jj26.server.message.connect.RequestChatroomMessage;
import zz23_jj26.server.mini.controller.MiniController;
import zz23_jj26.server.mini.model.ChatroomID;
import zz23_jj26.server.mini.model.IChatroom;
import zz23_jj26.server.mini.model.IMiniModel2MainModelAdapter;
import zz23_jj26.server.user.UserRMIWrapper;
import zz23_jj26.server.userremote.IUserConnectRemote;

/**
 * The main model
 * @author austurela
 *
 */
public class ChatroomManager{
	/**
	 * The rooms you're in
	 */
	private List<IChatroom> myRooms = new ArrayList<>();

	@SuppressWarnings("unused")
	/**
	 * The stubs you hold on to 
	 */
	private IUserRMIWrapper meUserStub, otherUserStub;
	/**
	 * You as a user and the user you're connected to
	 */
	private IUser meUser, otherUser;
	/**
	 * The others you are connected to
	 */
	private List<IUser> connectedOthers = new ArrayList<>();
	/**
	 * The thing you use to send IConnectMessages
	 */
	private IUserConnectRemote userConnectRemote;
	/**
	 * The stub that holds the other user.
	 */
	private IUserRMIWrapper stub;
	/**
	 * Your username
	 */
	private String myName = "Default";
	/**
	 * The adapter to the view
	 */
	private IModel2ViewAdapter _model2View;
	/**
	 * Create an adapter to yourself to pass into each chatroom.
	 */
	private IMiniModel2MainModelAdapter adapterToMe = new IMiniModel2MainModelAdapter(){

		@Override
		/**
		 * Tell the main model that you as a chatroom are quitting
		 */
		public void quit(IChatroom roomToQuit) {
			myRooms.remove(roomToQuit);
			_model2View.removeRoomFromList(roomToQuit);
		}
	};

	/**
	 * The RMI Registry
	 */
	private Registry registry;
	/**
	 * output command used to put multiple strings up onto the view.
	 */
	private IVoidLambda<String> outputCmd = new IVoidLambda<String>() {
		@Override
		public void apply(String... params) {
			for (String s : params) {
				_model2View.append(s + "\n");
			}
		}
	};

	/**
	 * Factory for the Registry and other uses.
	 */
	IRMIUtils rmiUtils = new RMIUtils(outputCmd);

	/**
	 * Constructor
	 * @param iModel2ViewAdapter - The adapter to the view
	 */
	public ChatroomManager(IModel2ViewAdapter iModel2ViewAdapter) {
		_model2View = iModel2ViewAdapter;
	}

	/**
	 * Starts the model by setting all the required RMI system properties,
	 * starts up the class server and installs the security manager.
	 */
	public void start() {
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);

		_model2View.append("Waiting..." + "\n");
	}

	/**
	 * Stops the server by shutting down the class server.
	 */
	public void stop() {
		try {
			registry.unbind(IUserRMIWrapper.BOUND_NAME_SERVER);
			System.out.println("UserEngine: " + IUserRMIWrapper.BOUND_NAME_SERVER
					+ " has been unbound.");
			rmiUtils.stopRMI();
			System.exit(0);
		} catch (Exception e) {
			System.err.println("UserEngine: Error unbinding "
					+ IUserRMIWrapper.BOUND_NAME_SERVER + ":\n" + e);
			System.exit(-1);
		}
	}

	/**
	 * begin connected to the user with the given IP
	 * @param remoteHost - User's IP
	 * @return A string to be printed as status
	 */
	public String connectTo(String remoteHost) {
		try {
			_model2View.append("Locating registry at " + remoteHost + "...\n");
			Registry registry = rmiUtils.getRemoteRegistry(remoteHost);
			_model2View.append("Found registry: " + registry + "\n");
			otherUserStub = (IUserRMIWrapper) registry
					.lookup(IUserRMIWrapper.BOUND_NAME_SERVER);
			otherUser = otherUserStub.getUser();
			_model2View.append("Found remote IUserRMIWrapper object: "
					+ otherUserStub + "\n");
			_model2View.append("Found remote user object: " + otherUserStub
					+ "\n");

			addUserTofriends(otherUser);

			otherUser.sendMessage(new DataPacket<IConnectBack>(IConnectBack.class, new ConnectBack()), meUser);

		} catch (Exception e) {
			_model2View.append("Exception connecting to " + remoteHost + ": "
					+ e + "\n");
			e.printStackTrace();
			return "No connection established!";
		}
		return "Connection to " + remoteHost + " established!";
	}

	/**
	 * Add the given user to your list of connected users
	 * @param otherUser2 - the other user
	 */
	public void addUserTofriends(IUser otherUser2) {
		System.out.println("Was told to add user: " + otherUser2.toString());
		for (IUser other : connectedOthers){
			if (other.equals(otherUser2)) return;
		}
		connectedOthers.add(otherUser2);
		_model2View.addUserToFriends(otherUser2);

	}

	/**
	 * Get a list of all the chatrooms you're in
	 * @return the list of chatrooms
	 */
	public List<IChatroomID> getChatroomID() {
		// installRooms();
		List<IChatroomID> returnVal = new ArrayList<IChatroomID>();
		for (IChatroom c : myRooms)
			returnVal.add(c.getID());
		// roomIDs = returnVal;
		return returnVal;
	}

	/**
	 * Set your username to the string
	 * @param text - desired name
	 */
	public void setName(String text) {
		myName = text;
		initialize();
	}

	/**
	 * Begin the necessary steps for RMI
	 */
	private void initialize() {
		try {
			// For server
			Inet4Address ipAddress = (Inet4Address) Inet4Address
					.getByName(System.getProperty("java.rmi.server.hostname"));
			userConnectRemote = (IUserConnectRemote) UnicastRemoteObject
					.exportObject(new IUserConnectRemote() {


						private static final long serialVersionUID = -5055042900471244862L;

						@Override
						public DataPacket<? extends IConnectMessage> sendMessage(
								DataPacket<? extends IConnectMessage> message,
								IUser sendingUser) throws RemoteException {
							return message.execute(new ConnectMessageVisitor(),
									ChatroomManager.this, sendingUser);
						}
					}, IRemoteTaskViewAdapter.BOUND_PORT_SERVER);

			IUserRMIWrapper meUserWrapper = new UserRMIWrapper(ipAddress,
					myName, userConnectRemote);
			meUser = meUserWrapper.getUser();
			_model2View.append("Instantiated new IUserRMIWrapper: "
					+ meUserWrapper + "\n");
			stub = (IUserRMIWrapper) UnicastRemoteObject
					.exportObject(meUserWrapper,
							IRemoteTaskViewAdapter.BOUND_PORT_SERVER);
			// Use this technique rather than the simpler
			// "registry.rebind(name, engine);"
			// because it enables us to specify a port number so we can open
			// that port on the firewall
			_model2View.append("Looking for registry..." + "\n");
			registry = rmiUtils.getLocalRegistry();
			_model2View.append("Found registry: " + registry + "\n");
			registry.rebind(IUserRMIWrapper.BOUND_NAME_SERVER, stub);
			_model2View.append("IUserRMIWrapper bound to "
					+ IUserRMIWrapper.BOUND_NAME_SERVER + "\n");

			// For client
			_model2View.setRemoteHost(rmiUtils.getLocalAddress());
		} catch (Exception e) {
			System.err.println("UserEngine exception:" + "\n");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Get a list of the rooms you're in
	 * @return list of IChatrooms
	 */
	public List<IChatroom> getMyRooms() {
		return new ArrayList<>(myRooms);
	}

	/**
	 * Make a new room
	 * @param roomName - the room name
	 */
	public void makeRoom(String roomName) {
		MiniController miniController = new MiniController(new ChatroomID(roomName), meUser, adapterToMe);
		miniController.start();
		IChatroom newRoom = miniController.getModel();
		myRooms.add(newRoom);
		_model2View.addNewRoomToList(newRoom);
	}

	/**
	 * Ask the user you're connected to for their rooms
	 * @param connectedUser - user whose rooms you  want
	 * @return A list of the rooms they're in
	 */
	public List<IChatroomID> askRemoteChatRooms(IUser connectedUser) {
		DataPacket<IGetChatroomsListMessage> requestRooms = new DataPacket<IGetChatroomsListMessage>(
				IGetChatroomsListMessage.class, new GetChatroomsListMessage());
		try {

			List<IChatroomID> listOfRoomsMessage = ((IChatroomsListMessage) connectedUser.sendMessage(requestRooms,
					meUser).getData()).getChatroomIDs();
			connectedUser.sendMessage(new DataPacket<INullMessage>(INullMessage.class, NullMessage.SINGLETON), meUser);
			return listOfRoomsMessage;
		} catch (RemoteException e) {
			_model2View.append("Exception getting list of chatrooms" + "\n");
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/** 
	 * Ask a user to join a room they're in
	 * @param toJoin -The room to join
	 * @param selecteduser - The user to ask
	 */
	public void askToJoinRoom(IChatroomID toJoin, IUser selecteduser) {
		if (toJoin == null || selecteduser == null) return;
		for (IChatroom rm : myRooms){
			System.out.println(rm.getID().toString() + " " + toJoin.toString());
			if (rm.getID().equals(toJoin)) return;
		}

		DataPacket<IRequestChatroomMessage> requestRoom = new DataPacket<>(
				IRequestChatroomMessage.class, new RequestChatroomMessage(toJoin, myName));
		try {
			IConnectMessage returnMsg = selecteduser.sendMessage(requestRoom, meUser).getData();
			if (returnMsg instanceof IChatroomInviteMessage){
				IChatroomInviteMessage invite = (IChatroomInviteMessage)returnMsg;

				MiniController miniController = new MiniController(toJoin, meUser, adapterToMe);
				miniController.start();
				IChatroom newRoom = miniController.getModel();
				for(IChatroomAdapter adpt : invite.getMemberAdapters())
					newRoom.addAdapter(adpt);
				myRooms.add(newRoom);	

				IChatroomAdapter adapterToMe = newRoom.getAdapterToMe();
				newRoom.sendTextMessage(meUser.toString() + " has joined the room.");

				DataPacket<IJoinChatroomMessage> query = new DataPacket<>(IJoinChatroomMessage.class, new JoinChatroomMessage(adapterToMe));	
				for (IChatroomAdapter i : invite.getMemberAdapters()){
					i.sendChatroomMessage(query, adapterToMe);
				}
				_model2View.addNewRoomToList(newRoom);
			} else {
				_model2View.append("Congratulations! Join room request rejected\n");
				selecteduser.sendMessage(new DataPacket<INullMessage>(INullMessage.class, NullMessage.SINGLETON), meUser);
			}
		} catch (RemoteException e) {
			_model2View.append("Exception getting list of chatrooms" + "\n");
			e.printStackTrace();
		}
	}

	/**
	 * Join a room once you have an invite
	 * @param invite -The invite
	 */
	public void joinRoom(IChatroomInviteMessage invite){
		IChatroomID checkRepeat = invite.getChatroomID();
		for (IChatroom rm : myRooms){
			if (rm.getID().equals(checkRepeat)) return;
		}
		MiniController miniController = new MiniController(invite.getChatroomID(), meUser, adapterToMe);
		miniController.start();
		IChatroom newRoom = miniController.getModel();
		for(IChatroomAdapter adpt : invite.getMemberAdapters())
			newRoom.addAdapter(adpt);
		myRooms.add(newRoom);	

		IChatroomAdapter adapterToMe = newRoom.getAdapterToMe();
		newRoom.sendTextMessage(meUser.toString() + " has joined the room.");

		DataPacket<IJoinChatroomMessage> query = new DataPacket<>(IJoinChatroomMessage.class, new JoinChatroomMessage(adapterToMe));	
		for (IChatroomAdapter i : invite.getMemberAdapters()){
			try {
				i.sendChatroomMessage(query, adapterToMe);
			} catch (RemoteException e) {
				e.printStackTrace();
				return;
			}
		}
		_model2View.addNewRoomToList(newRoom);
	}

	/**
	 * Respond to a request to join a room you're in
	 * @param room - the room
	 * @param name - The name of the person asking to join
	 * @return True if allowed. False if declined
	 */
	public boolean respondRoomRequest(String room, String name) {
		return _model2View.respondToRoomRequest(room, name);
	}

	/**
	 * Print a string to the view
	 * @param string - to print
	 */
	public void printToView(String string) {
		_model2View.append(string);
	}

	/**
	 * Quit all the rooms that you're in / have open.
	 */
	public void quitAllRooms(){
		for(IChatroom room: myRooms){
			room.quit();
		}
	}

	/**
	 * Invite the selected user to the given room
	 * @param userToInvite - user to invite
	 * @param roomToInviteTo - room to invite to
	 */
	public void inviteUserToRoom(IUser userToInvite, IChatroomID roomToInviteTo) {
		if (userToInvite == null || roomToInviteTo == null) return;
		IChatroom roomToJoin = null;
		// Check if the user to be invited is already in my chatroom
		for (IChatroom rm : myRooms){
			IChatroomID thisRoomID = rm.getID();
			if (thisRoomID.equals(roomToInviteTo)){
				roomToJoin = rm;
				List<IChatroomAdapter> adps = rm.getAdapters();
				for (IChatroomAdapter c : adps){
					if (c.getUser().equals(userToInvite)) return;
				}
			}
		}
		// If there is no such room
		if(roomToJoin == null){
			return;
		}
		IChatroomInviteMessage invitemsg = new ChatroomInviteMessage(roomToInviteTo, roomToJoin.getCopyAdapters()); 
		DataPacket<IChatroomInviteMessage> packet = new DataPacket<IChatroomInviteMessage>(IChatroomInviteMessage.class, invitemsg);
		try {
			IConnectMessage returnMsg = userToInvite.sendMessage(packet, meUser).getData();
			if(returnMsg instanceof IRejectRequestMessage){
				_model2View.append(userToInvite.toString() + " declined your invitation to chatroom: " + roomToInviteTo.toString() + "\n");
			}
			userToInvite.sendMessage(new DataPacket<INullMessage>(INullMessage.class, NullMessage.SINGLETON), meUser);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Ask a user if they want to join a given room
	 * @param chatroomID - the room they may wish to join
	 * @param iUser -the user to ask
	 * @return -true if they accept. false otherwise.
	 */
	public boolean sendInvitePrompt(IChatroomID chatroomID, IUser iUser) {
		return _model2View.sendInvitePrompt(chatroomID, iUser);
	}
}
