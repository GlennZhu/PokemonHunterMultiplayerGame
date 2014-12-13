package zz23_jj26.client.mini.model;

import java.awt.Component;
import java.awt.Window;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.extvisitor.AExtVisitor;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import zz23_jj26.client.engine.IRemoteTaskViewAdapter;
import zz23_jj26.client.message.chat.IStartGame;
import zz23_jj26.client.message.chat.IUnknownTest;
import zz23_jj26.client.message.chat.LeaveMessage;
import zz23_jj26.client.message.chat.RequestCmdMessage;
import zz23_jj26.client.message.chat.SendCmdMessage;
import zz23_jj26.client.message.chat.StartGame;
import zz23_jj26.client.message.chat.TextMessage;
import zz23_jj26.client.message.chat.UnknownTest;
import zz23_jj26.client.userremote.IUserChatRemote;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.chatroom.IChatroomID;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;
import common.message.chat.IJoinChatroomMessage;
import common.message.chat.ILeaveMessage;
import common.message.chat.IRequestCmdMessage;
import common.message.chat.ISendCmdMessage;
import common.message.chat.ITextMessage;
import common.user.IUser;

/**
 * Represents a chat room
 * 
 * @author austurela
 *
 */
public class Chatroom implements IChatroom {
	private static final long serialVersionUID = -7660973234784731388L;
	
	/** Identifier of a chatroom */
	private IChatroomID id;
	/**
	 * a collection of adapters to chatroom instances of other users in the
	 * chatroom.
	 */
	private List<IChatroomAdapter> adapters = new ArrayList<IChatroomAdapter>();
	/**
	 * An adapter to yourself
	 */
	private IChatroomAdapter adapterToMe;
	/**
	 * Your user
	 */
	private IUser meUser;	
	/**
	 * Keep stub for garbage collection
	 */

	private IUserChatRemote keepAlgo;
	/**
	 * Keep stub for garbage collection
	 */

	private IUserChatRemote algoStub;
	/**
	 * Visitor for IChatMessage
	 */
	private AExtVisitor<DataPacket<? extends IChatMessage>, Class<?>, IChatroomAdapter, ADataPacket> chatVisitor;
	/**
	 * adapter to miniview
	 */
	private IMModel2MViewAdapter miniview;
	/**
	 * Adapter to main model
	 */
	private IMiniModel2MainModelAdapter adapterToMain;
	/**
	 * command to this model adapter
	 */
	private ICmd2ModelAdapter cmdAdapter = new ICmd2ModelAdapter() {
		
		private IMixedDataDictionary dict = new MixedDataDictionary();
		
		@Override
		public IMixedDataDictionary getMixedDataDictionary() {
			return dict;
		}
		
		@Override
		public void append(String text) {
			miniview.printToView(text);
		}
		
		@Override
		public void addComponent(Component component, String string) {
			component.setName(string);
			miniview.addComponent(component);
		}

		@Override
		public Window addComponentAsWindow(Component component, String label) {
			zz23_jj26.popupWindow.View returnVal = new zz23_jj26.popupWindow.View();
			component.setName(label);
			returnVal.addComponent(component);
			return returnVal;
		}

		@Override
		public IChatroomAdapter getChatroomAdapter() {
			return adapterToMe;
		}
	}; 

	/**
	 * Constructor for a chatroom
	 * @param id - id of the room
	 * @param me - your user
	 * @param miniview - adapter to the miniview
	 * @param adapterToMe2 - Chatroomadapter to your user
	 */
	public Chatroom(IChatroomID id, IUser me, IMModel2MViewAdapter miniview,
			IMiniModel2MainModelAdapter adapterToMe2) {
		this.id = id;
		meUser = me;
		adapterToMe = makeAdapterToMe();
		chatVisitor = new AExtVisitor<DataPacket<? extends IChatMessage>, Class<?>, IChatroomAdapter, ADataPacket>(
				new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, Object, IChatroomAdapter>() {

					private static final long serialVersionUID = 981230839366920892L;
					

					@Override
					public DataPacket<? extends IChatMessage> apply(
							Class<?> index, DataPacket<Object> host,
							IChatroomAdapter... params) {
						System.out.println("Received unknown message");
						IChatroomAdapter sendingAdpt = (IChatroomAdapter) params[0];
						try {
							DataPacket<? extends IChatMessage>	cmdMsg = sendingAdpt.sendChatroomMessage(
									new DataPacket<IRequestCmdMessage>(IRequestCmdMessage.class, 
											new RequestCmdMessage(index)), adapterToMe);
							System.out.println("here");
							ISendCmdMessage sendCmdMsg = (ISendCmdMessage) cmdMsg.getData();
							ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, ?, IChatroomAdapter> cmd = sendCmdMsg
									.getCmd();
							System.out.println("Setting local cmd adapter");
							cmd.setCmd2ModelAdpt(cmdAdapter);
							chatVisitor.setCmd(sendCmdMsg.getCmdID(),
									sendCmdMsg.getCmd());
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						host.execute(chatVisitor, params);
						return new DataPacket<INullMessage>(INullMessage.class,
								NullMessage.SINGLETON);
					}

					@Override
					public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
						System.out.println("Should not call");
					}

				}) {
			private static final long serialVersionUID = 5928472004092037409L;
		};

		chatVisitor
				.setCmd(ITextMessage.class,
						new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>() {
							private static final long serialVersionUID = 2065544413798448779L;

							@Override
							public DataPacket<? extends IChatMessage> apply(
									Class<?> index,
									DataPacket<IChatMessage> host,
									IChatroomAdapter... params) {
								IChatroomAdapter adpt = (IChatroomAdapter) params[0];
								String text = ((ITextMessage) host.getData())
										.getText();
								miniview.printToView(adpt.getUser().toString()
										+ ": " + text);
								return new DataPacket<INullMessage>(
										INullMessage.class,
										NullMessage.SINGLETON);
							}

							@Override
							public void setCmd2ModelAdpt(
									ICmd2ModelAdapter cmd2ModelAdpt) {
							}
						});

		chatVisitor
				.setCmd(IJoinChatroomMessage.class,
						new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>() {
							private static final long serialVersionUID = -215362774366690243L;

							@Override
							public DataPacket<? extends IChatMessage> apply(
									Class<?> index,
									DataPacket<IChatMessage> host,
									IChatroomAdapter... params) {
								IChatroomAdapter newGuy = ((IJoinChatroomMessage) host
										.getData()).getAdapter();
								addAdapter(newGuy);
								return new DataPacket<INullMessage>(
										INullMessage.class,
										NullMessage.SINGLETON);
							}

							@Override
							public void setCmd2ModelAdpt(
									ICmd2ModelAdapter cmd2ModelAdpt) {
							}
						});

		chatVisitor
				.setCmd(ILeaveMessage.class,
						new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>() {
							private static final long serialVersionUID = 2343906338360464741L;

							@Override
							public DataPacket<? extends IChatMessage> apply(
									Class<?> index,
									DataPacket<IChatMessage> host,
									IChatroomAdapter... params) {
								IChatroomAdapter leavingGuy = ((ILeaveMessage) host
										.getData()).getAdapter();
								adapters.remove(leavingGuy);
								return new DataPacket<INullMessage>(
										INullMessage.class,
										NullMessage.SINGLETON);
							}

							@Override
							public void setCmd2ModelAdpt(
									ICmd2ModelAdapter cmd2ModelAdpt) {
							}
						});

		chatVisitor
				.setCmd(IRequestCmdMessage.class,
						new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>() {
							private static final long serialVersionUID = 361377945231880991L;

							@Override
							public DataPacket<? extends IChatMessage> apply(
									Class<?> index,
									DataPacket<IChatMessage> host,
									IChatroomAdapter... params) {
								IRequestCmdMessage request = (IRequestCmdMessage) host
										.getData();
								Class<?> myIndex = request.getMessageID();
								return new DataPacket<ISendCmdMessage>(
										ISendCmdMessage.class,
										new SendCmdMessage(
												myIndex,
												(ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, Class<?>, IChatroomAdapter>) chatVisitor
														.getCmd(myIndex)));
							}

							@Override
							public void setCmd2ModelAdpt(
									ICmd2ModelAdapter cmd2ModelAdpt) {
							}
						});

//		chatVisitor.setCmd(IUnknownTest.class, new UnknownTestCmd(cmdAdapter));
//		chatVisitor.setCmd(IStartGame.class, new StartGameCmd(cmdAdapter));

		this.miniview = miniview;
		adapterToMain = adapterToMe2;
	}

	/**
	 * Send a text message to everyone in the room
	 * @param text- message to be sent
	 */
	public void sendTextMessage(String text) {
		ITextMessage message = new TextMessage(text);
		DataPacket<ITextMessage> messagePacket = new DataPacket<ITextMessage>(
				ITextMessage.class, message);
		BlockingQueue<DataPacket<? extends IChatMessage>> returnedMessages = new LinkedBlockingQueue<>();

		for (IChatroomAdapter adpt : adapters) {
			// Instantiate a thread object
			new Thread() {
				public void run() {

					try {
						returnedMessages.offer(adpt.sendChatroomMessage(
								messagePacket, adapterToMe));
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}.start();
		}

	}

	@Override
	/**
	 * Check for equality of chatroom
	 */
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Chatroom))
			return false;
		return ((Chatroom) other).id.equals(id);
	}

	@Override
	/**
	 * Also for equality
	 */
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	/**
	 * Room to string
	 */
	public String toString() {
		return id.toString();
	}

	/**
	 * Get the ID of the room
	 */
	public IChatroomID getID() {
		return id;
	}

	/**
	 * Get all the adapters in the room (reference not copy)
	 */
	public List<IChatroomAdapter> getAdapters() {
		return adapters;
	}

	/**
	 * Get a adapter to yourself
	 */
	public IChatroomAdapter getAdapterToMe() {
		return adapterToMe;
	}

	/**
	 * Make an adapter to yourself
	 * @return an IChatroomToChatroomAdapter that points to yourself
	 */
	private IChatroomAdapter makeAdapterToMe() {
		keepAlgo = new IUserChatRemote() {
			private static final long serialVersionUID = 6206924441095578486L;

			@Override
			public DataPacket<? extends IChatMessage> sendMessage(
					DataPacket<? extends IChatMessage> message,
					IChatroomAdapter sendingAdpt)
					throws RemoteException {
				return message.execute(chatVisitor, sendingAdpt);
			}
		};

		try {

			algoStub = (IUserChatRemote) UnicastRemoteObject.exportObject(
					keepAlgo, IRemoteTaskViewAdapter.BOUND_PORT_SERVER);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		IChatroomAdapter newAdapt = new ChatroomAdapter(
				meUser, id, algoStub);
		adapters.add(newAdapt);
		return newAdapt;
	}

	@Override
	/**
	 * Start the minimodel
	 */
	public void start() {

	}

	@Override
	/**
	 * Add an adapter to someone joingin the room
	 */
	public void addAdapter(IChatroomAdapter newGuy) {
		adapters.add(newGuy);

	}

	@Override
	/**
	 * Quit the room.
	 */
	public void quit() {

		adapters.remove(adapterToMe);
		sendTextMessage(meUser.toString() + " has left the room.");
		ILeaveMessage message = new LeaveMessage(adapterToMe);
		DataPacket<ILeaveMessage> leavePacket = new DataPacket<>(
				ILeaveMessage.class, message);
		BlockingQueue<DataPacket<? extends IChatMessage>> returnedMessages = new LinkedBlockingQueue<>();

		for (IChatroomAdapter adpt : adapters) {
			// Instantiate a thread object
			new Thread() {
				public void run() {

					try {
						returnedMessages.offer(adpt.sendChatroomMessage(
								leavePacket, adapterToMe));
					} catch (RemoteException e) {
						e.printStackTrace();
					}

				}
			}.start();
		}
		adapterToMain.quit(this);
	}

	@Override
	/**
	 * Get a copy of the list of adapters to everyone in the room
	 */
	public List<IChatroomAdapter> getCopyAdapters() {
		return new ArrayList<IChatroomAdapter>(getAdapters());
	}

	@Override
	/**
	 * Send a smile to everyone
	 */
	public void sendSmilie() {
		IUnknownTest message = new UnknownTest();
		DataPacket<IUnknownTest> messagePacket = new DataPacket<IUnknownTest>(
				IUnknownTest.class, message);
		BlockingQueue<DataPacket<? extends IChatMessage>> returnedMessages = new LinkedBlockingQueue<>();

		for (IChatroomAdapter adpt : adapters) {
			// Instantiate a thread object
			new Thread() {
				public void run() {
					try {
						returnedMessages.offer(adpt.sendChatroomMessage(
								messagePacket, adapterToMe));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

	}

	@Override
	public void sendStartGame() {
		IStartGame message = new StartGame();
		DataPacket<IStartGame> messagePacket = new DataPacket<IStartGame>(
				IStartGame.class, message);
		for (IChatroomAdapter adpt : adapters) {
			// Instantiate a thread object
			new Thread() {
				public void run() {
					try {
						adpt.sendChatroomMessage(messagePacket, adapterToMe);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}
