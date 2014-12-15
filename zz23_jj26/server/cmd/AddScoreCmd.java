package zz23_jj26.server.cmd;

import java.util.UUID;

import javax.swing.JLabel;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import zz23_jj26.server.message.game.AddScore;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

/**
 * Each client will update their current score according to this command
 * @author Jiafang Jiang, Ziliang Zhu, Wei Zeng
 *
 */
public class AddScoreCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, AddScore, IChatroomAdapter> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -360292282332893480L;

	/**
	 * A adapter to the model that is not passed
	 */
	private transient ICmd2ModelAdapter cmdAdpt;
		
	/**
	 * Constructor of AddScoreCmd
	 * @param cmdAdpt Command to model adapter
	 */
	public AddScoreCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}
	
	@Override
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<AddScore> host, IChatroomAdapter... params) {
		
		UUID commonUUID = host.getData().getUUID();
		int score = host.getData().getScore();
		IMixedDataDictionary dict = cmdAdpt.getMixedDataDictionary();
		MixedDataKey<JLabel> keyToScoreLabel = new MixedDataKey<JLabel>(commonUUID, "score", JLabel.class);
		
		// Client will retrive their score label from MDD and update it
		JLabel scoreLabel = dict.get(keyToScoreLabel);
		scoreLabel.setText(Integer.toString(score));
		
		return new DataPacket<INullMessage>(
				INullMessage.class,
				NullMessage.SINGLETON);
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmdAdpt = cmd2ModelAdpt;		
	}

}
