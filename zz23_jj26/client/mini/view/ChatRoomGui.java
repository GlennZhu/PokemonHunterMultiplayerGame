package zz23_jj26.client.mini.view;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;


/**
* The view of the client MVC system.
* 
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ChatRoomGui extends JFrame {

	/**
	 * SerialVersionUId for the class.
	 */
	private static final long serialVersionUID = -199099598475124566L;


	/**
	 * The adapter to the model.
	 */
	private IMiniViewToModelAdpt model;

	/**
	 * The top control panel
	 */
	private JPanel controlPnl;
	
	/**
	 * The status output text area
	 */
	private JTextArea outputTA;
	
	/**
	 * The quit button
	 */
	private JButton quitBtn;
	private JPanel panel;
	private JLabel lblInsertMessageHere;
	private JTextField textField;
	private JButton btnSubmit;
	private String roomName;
	private JLabel lblRoomname;
	private JButton smiliebutton;
	private JButton btnStartGame;

	/**
	 * Constructor of the class
	 * @param ma 		the ModelAdapter 
	 * @param roomName 	room name
	 */
	public ChatRoomGui(IMiniViewToModelAdpt ma, String roomName) {
		super("Client GUI");
		model = ma;
		this.roomName = roomName; 
		initGUI();
	}

	/**
	 * Initializes the view and its components.
	 */
	protected void initGUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			/**
			 * tell the model to quit if the window is closing, even if very slowly.
			 */
			public void windowClosing(WindowEvent evt) {
				System.out.println("this.windowClosing, event="+evt);
				model.quit();
				setVisible(false);
				dispose();
			}
		});
		setSize(800,400);
		controlPnl = new JPanel();
		outputTA = new JTextArea();
		JScrollPane scroll = new JScrollPane(outputTA);
		Container contentPane = getContentPane();
		contentPane.add(controlPnl, BorderLayout.NORTH);
		controlPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblRoomname = new JLabel("Roomname: " + roomName);
		lblRoomname.setToolTipText("The name of the room.");
		controlPnl.add(lblRoomname);
		{
			quitBtn = new JButton();
			controlPnl.add(quitBtn);
			quitBtn.setText("Quit");
			quitBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					System.out.println("quitBtn.actionPerformed, event="+evt);
					model.quit();
					setVisible(false);
					dispose();
				}
			});
		}
		contentPane.add(scroll, BorderLayout.CENTER);
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		lblInsertMessageHere = new JLabel("Insert Message Here: ");
		panel.add(lblInsertMessageHere);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar() == '\n'){
					model.sendText(textField.getText());
					textField.setText("");
				}
			}
		});
		panel.add(textField);
		textField.setColumns(10);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.sendText(textField.getText());
				textField.setText("");
			}
		});
		panel.add(btnSubmit);
		
		smiliebutton = new JButton(":)");
		smiliebutton.setEnabled(false);
		smiliebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.sendSmilie();
			}
		});
		smiliebutton.setToolTipText("Send a smiley");
		panel.add(smiliebutton);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.setEnabled(false);
		panel.add(btnStartGame);
	}
	
	
	/**
	 * Append the given string(s) to the view's output text adapter.  
	 * @param s the string to display.
	 */
	public void append(String s) {
		outputTA.append(s);
		//Force the JScrollPane to go to scroll down to the new text
		outputTA.setCaretPosition(outputTA.getText().length());
	}

	/**
	 * Starts the view by making it visible.
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * Print to the chatroom's view
	 * @param text - text to print
	 */
	public void printToView(String text) {
		append(text + "\n");
		System.out.println(text + "\n");
	}

	public void addComponent(Component component) {
		getContentPane().removeAll();
		getContentPane().add(component, BorderLayout.CENTER);
		setVisible(true);
	}
}