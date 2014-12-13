package zz23_jj26.server.mini.model;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Renderable;

import java.awt.Component;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import zz23_jj26.server.cmd.AddScoreCmd;
import zz23_jj26.server.cmd.IExtendedCmdAdapter;
import zz23_jj26.server.cmd.RemovePokemonCmd;
import zz23_jj26.server.cmd.StartGameCmd;
import zz23_jj26.server.cmd.UnknownTestCmd;
import zz23_jj26.server.earth.RandomPosition;
import zz23_jj26.server.engine.IRemoteTaskViewAdapter;
import zz23_jj26.server.main.model.SerializedImage;
import zz23_jj26.server.message.chat.IUnknownTest;
import zz23_jj26.server.message.chat.LeaveMessage;
import zz23_jj26.server.message.chat.RequestCmdMessage;
import zz23_jj26.server.message.chat.SendCmdMessage;
import zz23_jj26.server.message.chat.TextMessage;
import zz23_jj26.server.message.chat.UnknownTest;
import zz23_jj26.server.message.game.AddScore;
import zz23_jj26.server.message.game.GameOver;
import zz23_jj26.server.message.game.IRequestRemovePokemon;
import zz23_jj26.server.message.game.IStartGame;
import zz23_jj26.server.message.game.RemovePokemon;
import zz23_jj26.server.message.game.StartGame;
import zz23_jj26.server.userremote.IUserChatRemote;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.extvisitor.AExtVisitor;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataDictionary;
import provided.mixedData.MixedDataKey;
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
	
	private HashMap<IChatroomAdapter, Integer> scoreBoard = new HashMap<IChatroomAdapter, Integer>();
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
	 * Used to get the extendedCmdAdapter from MixedDictionary from CmdAdapter
	 */
	private MixedDataKey<IExtendedCmdAdapter> extendedCmdAdapterKey = new MixedDataKey<IExtendedCmdAdapter>(UUID.randomUUID(), "desc", IExtendedCmdAdapter.class);

	public MixedDataKey<IExtendedCmdAdapter> getExtendedCmdAdapterKey() {
		return extendedCmdAdapterKey;
	}

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
						System.out.println("Received unknown message type " + index);
						IChatroomAdapter sendingAdpt = (IChatroomAdapter) params[0];
						try {
							DataPacket<? extends IChatMessage>	cmdMsg = sendingAdpt.sendChatroomMessage(
									new DataPacket<IRequestCmdMessage>(IRequestCmdMessage.class,
											new RequestCmdMessage(index)), adapterToMe);
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
								IUser thisUser = adpt.getUser();
								miniview.printToView(thisUser.toString()
										+ ": " + text);
								if (text.equals("START")){
									sendTextMessage(thisUser + " requested to start the game. Starting...please wait");
									sendStartGame();
								}
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
								// IUnknownTest
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
		
		chatVisitor.setCmd(IUnknownTest.class, new UnknownTestCmd(cmdAdapter));
		chatVisitor.setCmd(IStartGame.class, new StartGameCmd(cmdAdapter));
		chatVisitor.setCmd(RemovePokemon.class, new RemovePokemonCmd(cmdAdapter));
		chatVisitor.setCmd(AddScore.class, new AddScoreCmd(cmdAdapter));
		chatVisitor.setCmd(IRequestRemovePokemon.class, 
					new ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IRequestRemovePokemon, IChatroomAdapter>() {
						private static final long serialVersionUID = 361377945231880991L;
			
						@Override
						public DataPacket<? extends IChatMessage> apply(Class<?> index,
								DataPacket<IRequestRemovePokemon> host, IChatroomAdapter... params) {
							IRequestRemovePokemon request = (IRequestRemovePokemon) host
									.getData();
							System.out.println("wo men you mei you dao zhe li a");
							IChatroomAdapter thisUser = params[0];
							Position posToRemove = request.getPosToRemove();
							//TODO: change this, extract out the UUID from startGameCmd if we are using the same UUID.
							// Move that UUID to be set in Chatroom.java and pass in to startGameCmd as param.
							UUID commonUUID = request.getUUID();
							@SuppressWarnings("rawtypes")
							MixedDataKey<Map> mk = new MixedDataKey<Map>(commonUUID,"imageMap", Map.class);
							@SuppressWarnings("unchecked")
							Map<Position, Renderable> imageMap = cmdAdapter.getMixedDataDictionary().get(mk);
							if (imageMap.containsKey(posToRemove)){
								// increment score of this user in the score board
								if(scoreBoard.containsKey(thisUser)){
									scoreBoard.put(thisUser, scoreBoard.get(thisUser) + 1);
								} else {
									scoreBoard.put(thisUser, 1);
								}
								AddScore addScoreMsg = new AddScore(commonUUID, scoreBoard.get(thisUser));
								DataPacket<AddScore> addScore = new DataPacket<AddScore>(AddScore.class, addScoreMsg);
								new Thread() {
									public void run() {		
										try {			
											thisUser.sendChatroomMessage(addScore, adapterToMe);
										} catch (RemoteException e) {
											e.printStackTrace();
										}

									}
								}.start();
								// Send out delete postion msg to everyone.
								RemovePokemon removeMsg = new RemovePokemon(posToRemove.latitude.degrees, posToRemove.longitude.degrees, commonUUID);
								DataPacket<RemovePokemon> message = new DataPacket<RemovePokemon>(RemovePokemon.class, removeMsg);
								for(IChatroomAdapter c: adapters){
									new Thread() {
										public void run() {		
											try {			
												System.out.println("check check, have we been RemovePokemon?");
												c.sendChatroomMessage(message, adapterToMe);
											} catch (RemoteException e) {
												e.printStackTrace();
											}

										}
									}.start();
								}
							} else {
								//TODO: We do something if the pokemon is already taken.
							}
							return new DataPacket<INullMessage>(
									INullMessage.class,
									NullMessage.SINGLETON);
						}
			
						@Override
						public void setCmd2ModelAdpt(
								ICmd2ModelAdapter cmd2ModelAdpt) {
						}
					});
		
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
		ArrayList<SerializedImage> images = new ArrayList<SerializedImage>();
		File imageFolder = new File("zz23_jj26/server/image");
		File[] listOfFiles = imageFolder.listFiles();
		for (File file: listOfFiles){
			if (file.isFile()){
				BufferedImage img = null;
				try {
				    img = ImageIO.read(file);
				    images.add(new SerializedImage(img));
				} catch (IOException e) {
					System.out.println("Read file " + file.getName() + " failed");
				}
			}
		}
		
		
		
		Random rnd = new Random();
		
		ArrayList<RandomPosition> posList = new ArrayList<RandomPosition>();
		
		for (int i = 0; i < images.size(); i++){
			double latiPos = rnd.nextInt(358) + rnd.nextDouble() * 2 - 1 - 180;
			double longtiPos = rnd.nextInt(358) + rnd.nextDouble() * 2 - 1 - 180;
			double offset = rnd.nextDouble();
			posList.add(new RandomPosition(latiPos, longtiPos, offset));
		}
		
		UUID useThisUUID = UUID.randomUUID();
		System.out.println("start game message UUID " + useThisUUID);
		IStartGame message = new StartGame(images, posList, useThisUUID);
		
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
		TimerTask myTimerTask = new TimerTask(){

			@Override
			public void run() {
				HashMap<String, Integer> finalScoreBoard= new HashMap<String, Integer>();
				for(Entry<IChatroomAdapter, Integer> en: scoreBoard.entrySet()){
					finalScoreBoard.put(en.getKey().getUser().toString(), en.getValue());
				}
				GameOver message = new GameOver(finalScoreBoard);
				DataPacket<GameOver> gameOverMsgPacket = new DataPacket<GameOver>(
						GameOver.class, message);
				
				for (IChatroomAdapter adpt : adapters) {
					new Thread() {
						public void run() {
							try {
								adpt.sendChatroomMessage(gameOverMsgPacket, adapterToMe);
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
			
		};
		Timer myTimer = new Timer();
		myTimer.schedule(myTimerTask, 30000L);
	}
	
	
}
