package zz23_jj26.server.cmd;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import zz23_jj26.server.message.game.GameOver;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

public class GameOverCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, GameOver, IChatroomAdapter> {

	private static final long serialVersionUID = -2573824494797367957L;
	/**
	 * A adapter to the model that is not passed
	 */
	private transient ICmd2ModelAdapter cmdAdpt;
		
	public GameOverCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}

	@Override
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<GameOver> host, IChatroomAdapter... params) {

		HashMap<String, Integer> scoreBoard = host.getData().getScoreBoard();
		String winner = host.getData().getWinningTeam();
		
	    JPanel panel = new JPanel();
	    
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{50, 70, 5, 0};
		gbl_panel.rowHeights = new int[]{16, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblGameOver = new JLabel("Game Over");
		GridBagConstraints gbc_lblGameOver = new GridBagConstraints();
		gbc_lblGameOver.insets = new Insets(0, 0, 5, 0);
		gbc_lblGameOver.anchor = GridBagConstraints.NORTH;
		gbc_lblGameOver.gridx = 2;
		gbc_lblGameOver.gridy = 0;
		lblGameOver.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		panel.add(lblGameOver, gbc_lblGameOver);
		
		JLabel lblWinner = new JLabel("Winner is: " + winner);
		GridBagConstraints gbc_lblWinner = new GridBagConstraints();
		gbc_lblWinner.insets = new Insets(0, 0, 5, 0);
		gbc_lblWinner.anchor = GridBagConstraints.NORTH;
		gbc_lblWinner.gridx = 2;
		gbc_lblWinner.gridy = 1;
		lblWinner.setFont(new Font("Lucida Grande", Font.ITALIC, 16));
		panel.add(lblWinner, gbc_lblWinner);
		
		JLabel lblScoreBoard = new JLabel("Score Board");
		GridBagConstraints gbc_lblScoreBoard = new GridBagConstraints();
		gbc_lblScoreBoard.insets = new Insets(0, 0, 5, 0);
		gbc_lblScoreBoard.anchor = GridBagConstraints.NORTH;
		gbc_lblScoreBoard.gridx = 2;
		gbc_lblScoreBoard.gridy = 2;
		lblScoreBoard.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		panel.add(lblScoreBoard, gbc_lblScoreBoard);
		
		int i = 3;
		
		for (Entry<String, Integer> entry: scoreBoard.entrySet()){
			JLabel lblTeamName = new JLabel(entry.getKey());
			GridBagConstraints gbc_lblTeamName = new GridBagConstraints();
			gbc_lblTeamName.insets = new Insets(0, 0, 5, 5);
			gbc_lblTeamName.anchor = GridBagConstraints.CENTER;
			gbc_lblTeamName.gridx = 1;
			gbc_lblTeamName.gridy = i;
			panel.add(lblTeamName, gbc_lblTeamName);
			
			JLabel lblTeamScore = new JLabel(Integer.toString(entry.getValue()));
			GridBagConstraints gbc_lblTeamScore = new GridBagConstraints();
			gbc_lblTeamScore.insets = new Insets(0, 0, 5, 5);
			gbc_lblTeamScore.anchor = GridBagConstraints.CENTER;
			gbc_lblTeamScore.gridx = 3;
			gbc_lblTeamScore.gridy = i;
			panel.add(lblTeamScore, gbc_lblTeamScore);
			
			i++;
		}
		
		cmdAdpt.addComponentAsWindow(panel, "Score-Board");
		
		
		return new DataPacket<INullMessage>( INullMessage.class, NullMessage.SINGLETON);
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmdAdpt = cmd2ModelAdpt;

	}

}
