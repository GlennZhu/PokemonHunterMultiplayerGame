package zz23_jj26.server.message.game;

import java.util.UUID;

import common.message.chat.IChatMessage;

/**
 * AddScore message will be sent by server to a client to acknowledge their pokemon collection move is valid
 * @author Jiafang Jiang, Ziliang Zhu, Wei Zeng
 *
 */
public class AddScore implements IChatMessage{

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -7604986521935228083L;
	
	/**
	 * Common UUID for generating MixedDataKey
	 */
	private UUID commonUUID;
	
	/**
	 * The score to display in Client's GUI
	 */
	private int score;
	
	/**
	 * Constructor of the AddScoreMessage
	 * @param commonUUID Common UUID for generating MixedDataKey
	 * @param score The score to display in Client's GUI
	 */
	public AddScore(UUID commonUUID, int score){
		this.commonUUID = commonUUID;
		this.score = score;
	}
	
	/**
	 * Get the common UUID for generating MixedDataKey
	 * @return commonUUID
	 */
	public UUID getUUID(){
		return commonUUID;
	}
	
	/**
	 * Get the score to display in Client's GUI
	 * @return client's score
	 */
	public int getScore(){
		return score;
	}
	

}
