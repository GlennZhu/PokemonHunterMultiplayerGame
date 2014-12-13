package zz23_jj26.server.cmd;
import java.util.HashMap;

import javax.swing.JPanel;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;

import zz23_jj26.server.message.game.GameOver;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
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
		// TODO Auto-generated method stub
		HashMap<String, Integer> scoreBoard = host.getData().getScoreBoard();
		
	    JPanel yo = null;
		
		cmdAdpt.addComponentAsWindow(yo, "Score-Board");
		
		
		return null;
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmdAdpt = cmd2ModelAdpt;

	}

}
