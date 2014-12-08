package zz23_jj26.client.message.connect;

import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomID;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.connect.IChatroomInviteMessage;
import common.message.connect.IChatroomsListMessage;
import common.message.connect.IConnectBack;
import common.message.connect.IConnectMessage;
import common.message.connect.IGetChatroomsListMessage;
import common.message.connect.IRejectRequestMessage;
import common.message.connect.IRequestChatroomMessage;
import common.user.IUser;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import zz23_jj26.client.main.model.ChatroomManager;
import zz23_jj26.client.mini.model.IChatroom;

/**
 * Visitor to handle IConnectMessage hosts
 * @author austurela
 *
 */
public class ConnectMessageVisitor extends DataPacketAlgo<DataPacket<? extends IConnectMessage>, Object>  {

	private static final long serialVersionUID = -6211099874796969110L;


	/**
	 * Default command
	 */
	public static final ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, Object, Object> nullCmd = 
			new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, Object, Object>() {

		private static final long serialVersionUID = -8304812530331451612L;

		@Override
		public DataPacket<? extends IConnectMessage> apply(Class<?> index,
				DataPacket<Object> host, Object... params) {
			return null;
		}

		@Override
		public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		}

	};

	/**
	 * Constructor. Default command is null
	 */
	public ConnectMessageVisitor(){
		super(nullCmd);
		init();
	}

	/**
	 * Constructor. Choose default
	 * @param defaultCmd - desired default cmd.
	 */
	public ConnectMessageVisitor(
			ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, Object, Object> defaultCmd) {
		super(defaultCmd);
		init();
	}

	/**
	 * Install all the basic IConnectMessage types
	 */
	private void init() {
		setCmd(IGetChatroomsListMessage.class, 
				new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, IConnectMessage, Object>() {
			private static final long serialVersionUID = 2968475484645555517L;

			@Override
			public DataPacket<? extends IConnectMessage> apply(
					Class<?> index, DataPacket<IConnectMessage> host,
					Object... params) {
				return new DataPacket<IChatroomsListMessage>(
						IChatroomsListMessage.class, new ChatroomListMessage(((ChatroomManager)params[0]).getChatroomID()));
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub

			}
		});

		setCmd(IRequestChatroomMessage.class,
				new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, IConnectMessage, Object>() {
			private static final long serialVersionUID = 214739225500876658L;

			@Override
			public DataPacket<? extends IConnectMessage> apply(Class<?> 
			index, DataPacket<IConnectMessage> host, Object... params) {
				IRequestChatroomMessage requestmessage = (IRequestChatroomMessage)host.getData();
				boolean accept = ((ChatroomManager)params[0]).respondRoomRequest(requestmessage.getChatroomID().toString(), 
						requestmessage.getName());

				IChatroomID query = ((IRequestChatroomMessage) (host.getData())).getChatroomID();
				if(accept){

					for(IChatroom room : ((ChatroomManager)params[0]).getMyRooms()){
						if(room.getID().equals(query)){
							IChatroomInviteMessage invite = new ChatroomInviteMessage(query, room.getAdapters());
							return new DataPacket<IChatroomInviteMessage>(IChatroomInviteMessage.class, invite); 
						}
					}
					return new DataPacket<IRejectRequestMessage>(IRejectRequestMessage.class, new RejectRequestMessage(query));
				}
				else{
					return new DataPacket<IRejectRequestMessage>(IRejectRequestMessage.class, new RejectRequestMessage(query));
				}
			}

			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {

			}
		});

		setCmd(IRejectRequestMessage.class,
				new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, IConnectMessage, Object>() {

			private static final long serialVersionUID = 7029514905529182052L;

			@Override
			public DataPacket<? extends IConnectMessage> apply(Class<?> 
			index, DataPacket<IConnectMessage> host, Object... params) {
				return new DataPacket<INullMessage>(INullMessage.class,
						NullMessage.SINGLETON);
			}

			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {

			}
		});


		setCmd(IConnectBack.class, 
				new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, IConnectMessage, Object>() {

			private static final long serialVersionUID = -3063815960935311836L;

			@Override
			public DataPacket<? extends IConnectMessage> apply(
					Class<?> index, DataPacket<IConnectMessage> host,
					Object... params) {
				System.out.println("was told to add a user");
				((ChatroomManager)params[0]).addUserTofriends((IUser) params[1]);
				return new DataPacket<INullMessage>(
						INullMessage.class, NullMessage.SINGLETON);
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// TODO Auto-generated method stub

			}
		});


		setCmd(IChatroomInviteMessage.class, 
				new ADataPacketAlgoCmd<DataPacket<? extends IConnectMessage>, IConnectMessage, Object>() {
					private static final long serialVersionUID = -1264632771599280718L;

			@Override
			public DataPacket<? extends IConnectMessage> apply(
					Class<?> index, DataPacket<IConnectMessage> host,
					Object... params) {
				IChatroomInviteMessage invite = (IChatroomInviteMessage) host.getData();
				boolean accept = ((ChatroomManager)params[0]).sendInvitePrompt(invite.getChatroomID(), (IUser)params[1]);
				ChatroomManager manager = ((ChatroomManager)params[0]);
				if(accept){
					manager.joinRoom(invite);
					return new DataPacket<INullMessage>(
							INullMessage.class, NullMessage.SINGLETON);
				} else {
					return new DataPacket<IRejectRequestMessage>(IRejectRequestMessage.class, new RejectRequestMessage(invite.getChatroomID()));
				}

			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}
		});
	}
}
