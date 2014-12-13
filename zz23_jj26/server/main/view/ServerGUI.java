package zz23_jj26.server.main.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JSplitPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JComboBox;

import zz23_jj26.server.mini.model.IChatroom;
import zz23_jj26.server.mini.view.ChatRoomGui;
import zz23_jj26.server.mini.view.IMiniViewToModelAdpt;
import common.chatroom.IChatroomID;
import common.user.IUser;

import java.awt.Insets;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSeparator;

public class ServerGUI extends JFrame {

	/**
	 * The default remote host reference.
	 */
	private static final String DEFAULT_REMOTE_HOST = "localhost"; 

	/**
	 * The adapter to the model.
	 */
	private IView2ModelAdapter model;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6851255293965791909L;
	private JPanel contentPane;
	private final JPanel pnlControl = new JPanel();
	private final JLabel lblSetUsername = new JLabel("Set Username: ");
	private final JTextField txtUsername = new JTextField();
	private final JButton btnSetName = new JButton("Enter");
	private final JLabel lblIpaddress = new JLabel("IPAddress: ");
	private final JTextField txtIPAddress = new JTextField(DEFAULT_REMOTE_HOST);
	private final JButton btnConnect = new JButton("Connect");
	private final JButton btnQuit = new JButton("Quit");
	private final JSplitPane splitPane = new JSplitPane();
	private final JPanel panel = new JPanel();
	private final JLabel lblConnectedUsers = new JLabel("Connected Users: ");
	private final JComboBox<IUser> cbbConnectedUsers = new JComboBox<IUser>();
	private final JButton btnGetRooms = new JButton("Get Rooms");
	private final JLabel lblUsersChatrooms = new JLabel("User's Chatrooms:");
	private final JComboBox<IChatroomID> cbbUserChatrooms = new JComboBox<IChatroomID>();
	private final JButton btnJoin = new JButton("Join");
	private final JLabel lblMakeChatroom = new JLabel("Make Chatroom:");
	private final JTextField txtMakeRoom = new JTextField();
	private final JButton btnMakeRoom = new JButton("Make");
	private final JLabel lblChatroomsImIn = new JLabel("Chatrooms I'm in: ");
	private final JComboBox<IChatroomID> cbbMyChatrooms = new JComboBox<IChatroomID>();
	private final JButton btnInvite = new JButton("Invite");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTextArea textArea = new JTextArea();
	private final JSeparator separator = new JSeparator();

	public void start(){
		setVisible(true);
	}

	/**
	 * Constructor of the class
	 * @param ma the ModelAdapter 
	 */
	public ServerGUI(IView2ModelAdapter ma) {
		super("Server GUI");
		model = ma;
		initGUI();
	}
	
	private void initGUI() {
		
		this.addWindowListener(new WindowAdapter() {
			/**
			 * tell the model to quit if the window is closing, even if very slowly.
			 */
			public void windowClosing(WindowEvent evt) {
				System.out.println("this.windowClosing, event="+evt);
				model.quit();
				System.exit(0);
			}
		});
		
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(pnlControl, BorderLayout.NORTH);
		
		pnlControl.add(lblSetUsername);
		txtUsername.setColumns(10);
		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar() == '\n'){
					String name = txtUsername.getText().trim();
					if(!name.matches("\\s*")){
						setNameAction();
					}
				}
			}
		});
		txtUsername.setToolTipText("Put desired username in here.");
		pnlControl.add(txtUsername);
		
		btnSetName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtUsername.getText().trim();
				if(!name.matches("\\s*")){
					setNameAction();
				}
			}
		});
		btnSetName.setToolTipText("Set your username.");
		pnlControl.add(btnSetName);
		
		pnlControl.add(lblIpaddress);
		
		txtIPAddress.setToolTipText("Put the IP address of the person you want to connect to.");
		txtIPAddress.setColumns(10);
		txtIPAddress.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar() == '\n'){
					connect();
				}
			}
		});
		pnlControl.add(txtIPAddress);
		
		btnConnect.setEnabled(false);
		btnConnect.setToolTipText("Connect to the specified IP.");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connect();
			}
		});
		pnlControl.add(btnConnect);
		
		btnQuit.setToolTipText("Quit.");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("quitBtn.actionPerformed, event="+evt);
				model.quit();
				System.exit(0);
			}
		});
		pnlControl.add(btnQuit);
		
		splitPane.setDividerSize(2);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		splitPane.setLeftComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 114, 0};
		gbl_panel.rowHeights = new int[]{16, 0, 0, 0, 32, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		GridBagConstraints gbc_lblConnectedUsers = new GridBagConstraints();
		gbc_lblConnectedUsers.insets = new Insets(0, 0, 5, 5);
		gbc_lblConnectedUsers.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblConnectedUsers.gridx = 0;
		gbc_lblConnectedUsers.gridy = 0;
		panel.add(lblConnectedUsers, gbc_lblConnectedUsers);
		
		GridBagConstraints gbc_cbbConnectedUsers = new GridBagConstraints();
		gbc_cbbConnectedUsers.insets = new Insets(0, 0, 5, 5);
		gbc_cbbConnectedUsers.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbConnectedUsers.gridx = 0;
		gbc_cbbConnectedUsers.gridy = 1;
		cbbConnectedUsers.setToolTipText("A list of users you are connected to.");
		panel.add(cbbConnectedUsers, gbc_cbbConnectedUsers);
		
		GridBagConstraints gbc_btnGetRooms = new GridBagConstraints();
		gbc_btnGetRooms.insets = new Insets(0, 0, 5, 0);
		gbc_btnGetRooms.gridx = 1;
		gbc_btnGetRooms.gridy = 1;
		btnGetRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					cbbUserChatrooms.removeAllItems();
					IUser chosen = cbbConnectedUsers.getItemAt(cbbConnectedUsers.getSelectedIndex());
					List<IChatroomID> roomList = model.getRooms(chosen);
					for(IChatroomID id : roomList)
						cbbUserChatrooms.addItem(id);
				}
				catch(Exception excpt) {
					append("Calculate Get Info exception: "+excpt+"\n");
				}
			}
		});
		btnGetRooms.setEnabled(false);
		btnGetRooms.setToolTipText("Get the available rooms from the user that you've connected to.");
		panel.add(btnGetRooms, gbc_btnGetRooms);
		
		GridBagConstraints gbc_lblUsersChatrooms = new GridBagConstraints();
		gbc_lblUsersChatrooms.anchor = GridBagConstraints.WEST;
		gbc_lblUsersChatrooms.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsersChatrooms.gridx = 0;
		gbc_lblUsersChatrooms.gridy = 2;
		panel.add(lblUsersChatrooms, gbc_lblUsersChatrooms);
		
		GridBagConstraints gbc_cbbUserChatrooms = new GridBagConstraints();
		gbc_cbbUserChatrooms.insets = new Insets(0, 0, 5, 5);
		gbc_cbbUserChatrooms.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbUserChatrooms.gridx = 0;
		gbc_cbbUserChatrooms.gridy = 3;
		cbbUserChatrooms.setToolTipText("Select the user's chat room.");
		panel.add(cbbUserChatrooms, gbc_cbbUserChatrooms);
		
		GridBagConstraints gbc_btnJoin = new GridBagConstraints();
		gbc_btnJoin.anchor = GridBagConstraints.WEST;
		gbc_btnJoin.insets = new Insets(0, 0, 5, 0);
		gbc_btnJoin.gridx = 1;
		gbc_btnJoin.gridy = 3;
		btnJoin.setEnabled(false);
		btnJoin.setToolTipText("Ask to join the selected chatroom.");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.joinRoom(cbbUserChatrooms.getItemAt(cbbUserChatrooms.getSelectedIndex()), 
						cbbConnectedUsers.getItemAt(cbbConnectedUsers.getSelectedIndex()));
			}
		});
		panel.add(btnJoin, gbc_btnJoin);
		
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 4;
		panel.add(separator, gbc_separator);
		
		GridBagConstraints gbc_lblMakeChatroom = new GridBagConstraints();
		gbc_lblMakeChatroom.anchor = GridBagConstraints.WEST;
		gbc_lblMakeChatroom.insets = new Insets(0, 0, 5, 5);
		gbc_lblMakeChatroom.gridx = 0;
		gbc_lblMakeChatroom.gridy = 5;
		panel.add(lblMakeChatroom, gbc_lblMakeChatroom);
		
		GridBagConstraints gbc_txtMakeRoom = new GridBagConstraints();
		gbc_txtMakeRoom.insets = new Insets(0, 0, 5, 5);
		gbc_txtMakeRoom.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMakeRoom.gridx = 0;
		gbc_txtMakeRoom.gridy = 6;
		txtMakeRoom.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar() == '\n'){
					String roomName = txtMakeRoom.getText().trim();
					if (!roomName.isEmpty()){
						model.makeRoom(roomName);
					}
					txtMakeRoom.setText("");
				}
			}
		});
		panel.add(txtMakeRoom, gbc_txtMakeRoom);
		
		GridBagConstraints gbc_btnMakeRoom = new GridBagConstraints();
		gbc_btnMakeRoom.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakeRoom.anchor = GridBagConstraints.WEST;
		gbc_btnMakeRoom.gridx = 1;
		gbc_btnMakeRoom.gridy = 6;
		btnMakeRoom.setToolTipText("Make a room with the specified name.");
		btnMakeRoom.setEnabled(false);
		btnMakeRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String roomName = txtMakeRoom.getText().trim();
				if (!roomName.isEmpty()){
					model.makeRoom(roomName);
				}
				txtMakeRoom.setText("");
			}
		});
		panel.add(btnMakeRoom, gbc_btnMakeRoom);
		
		GridBagConstraints gbc_lblChatroomsImIn = new GridBagConstraints();
		gbc_lblChatroomsImIn.anchor = GridBagConstraints.WEST;
		gbc_lblChatroomsImIn.insets = new Insets(0, 0, 5, 5);
		gbc_lblChatroomsImIn.gridx = 0;
		gbc_lblChatroomsImIn.gridy = 7;
		panel.add(lblChatroomsImIn, gbc_lblChatroomsImIn);
		
		GridBagConstraints gbc_cbbMyChatrooms = new GridBagConstraints();
		gbc_cbbMyChatrooms.insets = new Insets(0, 0, 0, 5);
		gbc_cbbMyChatrooms.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbbMyChatrooms.gridx = 0;
		gbc_cbbMyChatrooms.gridy = 8;
		cbbMyChatrooms.setToolTipText("Select a room to invite a connected user to.");
		panel.add(cbbMyChatrooms, gbc_cbbMyChatrooms);
		
		GridBagConstraints gbc_btnInvite = new GridBagConstraints();
		gbc_btnInvite.anchor = GridBagConstraints.WEST;
		gbc_btnInvite.gridx = 1;
		gbc_btnInvite.gridy = 8;
		btnInvite.setEnabled(false);
		btnInvite.setToolTipText("Invite the currently selected connected user to the room.");
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IChatroomID roomToInviteTo = cbbMyChatrooms.getItemAt(cbbMyChatrooms.getSelectedIndex());
				IUser userToInvite = cbbConnectedUsers.getItemAt(cbbConnectedUsers.getSelectedIndex());
				model.sendInvite(userToInvite, roomToInviteTo);
			}
		});
		panel.add(btnInvite, gbc_btnInvite);
		scrollPane.setViewportBorder(new EmptyBorder(0, 5, 0, 0));
		
		splitPane.setRightComponent(scrollPane);
		textArea.setLineWrap(true);
		
		scrollPane.setViewportView(textArea);
	}

	/**
	 * Have the model connect to the remote server.
	 */
	private void connect() {
		append("Connecting...\n");
		append(model.connectTo(txtIPAddress.getText())+"\n");
	}
	
	/**
	 * What happens when you click the button to set name
	 */
	private void setNameAction(){
		model.setName(txtUsername.getText());
		btnSetName.setEnabled(false);
		btnConnect.setEnabled(true);
		btnGetRooms.setEnabled(true);
		btnMakeRoom.setEnabled(true);
		btnInvite.setEnabled(true);
		btnJoin.setEnabled(true);
	}
	
	/**
	 * Set the displayed remote host text field to the actual remote system's IP address or host name 
	 * @param host The name of the remote host 
	 */
	public void setRemoteHost(String host){
		txtIPAddress.setText(host);
	}
	
	/**
	 * Append the given string(s) to the view's output text adapter.  
	 * @param s the string to display.
	 */
	public void append(String s) {
		textArea.append(s);
		//Force the JScrollPane to go to scroll down to the new text
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	/**
	 * Add the user to the chatroom
	 * @param query - the room
	 * @param username - the user
	 */
	public void addUserToChatroom(IChatroomID query, String username) {
		textArea.append(username + " has joined the chatroom.\n");
		//Force the JScrollPane to go to scroll down to the new text
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	/**
	 * Make a miniview for a chatroom
	 * @param iMiniViewToModelAdpt - Make a adapter to pass to the miniview
	 * @param roomName - The name of the chatroom
	 * @return -  an new miniview for the chatroom
	 */
	public ChatRoomGui makeMiniView(IMiniViewToModelAdpt iMiniViewToModelAdpt, String roomName) {
		return new ChatRoomGui(iMiniViewToModelAdpt, roomName);
	}

	/**
	 * Respond to a room request with a option box
	 * @param room - name of the room
	 * @param name - name of the user that wants to join
	 * @return - true if accept. False otherwise.
	 */
	public boolean respondToRoomRequest(String room, String name) {
		Object[] options = {"Accept",
                "Decline"};
		int n = JOptionPane.showOptionDialog(this,
			    name + " has requested to join room " + room,
			    "Room Join Request",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
		return n == 0;
	}

	/**
	 * Add a user to your list of friends
	 * @param otherUser - the user to add
	 */
	public void addUserToFriendsList(IUser otherUser) {
		cbbConnectedUsers.addItem(otherUser);
		cbbConnectedUsers.setSelectedIndex(cbbConnectedUsers.getItemCount() -1);
	}

	/**
	 * Send a room join invite with a option box
	 * @param room - name of the room
	 * @param name - name of the user that you want to invite
	 * @return - true if accept. False otherwise.
	 */
	public boolean sendInvitePrompt(IChatroomID chatroomID, IUser iUser) {
		Object[] options = {"Accept",
        "Decline"};
		int n = JOptionPane.showOptionDialog(this,
			    iUser.toString() + " invited you to join room " + chatroomID,
			    "Room Join Invite",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[0]);
		return n == 0;
	}

	/** 
	 * Add a room to your list of rooms
	 * @param newRoom - the room to add
	 */
	public void addNewRoomToList(IChatroom newRoom) {
		cbbMyChatrooms.addItem(newRoom.getID());
		
	}

	/**
	 * Remove a room from your list of rooms.
	 * @param roomToQuit - the room to remove.
	 */
	public void removeRoomFromList(IChatroom roomToQuit) {
		for(int i = 0; i < cbbMyChatrooms.getItemCount(); i++){
			if(roomToQuit.getID().equals(cbbMyChatrooms.getItemAt(i))){
				cbbMyChatrooms.removeItemAt(i);
				break;
			}
		}
	}
}
