package zz23_jj26.server.message.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import zz23_jj26.server.earth.RandomPosition;
import common.chatroom.IChatroomAdapter;
import common.message.chat.IChatMessage;

/**
 * Interface for start game message
 * 
 * @author Jiafang Jiang, Ziliang Zhu, Wei Zeng
 */
public interface IStartGame extends IChatMessage{
	
	/**
	 * Get images for loading into the map
	 * @return a list of buffered images
	 */
	public ArrayList<BufferedImage> getImages();
	
	/**
	 * Get removePokemonMessage and put it into client's MixedDataDictionary for future use
	 * @return an IRequestRemovePokemon message
	 */
	public IRequestRemovePokemon getRemovePokemonMessage();
	
	/**
	 * Get the UUID for generating MixedDataKey
	 * @return UUID for MixedDataKey
	 */
	public UUID getUUID();
	
	public ArrayList<RandomPosition> getPositions();
	
	public HashMap<String, ArrayList<IChatroomAdapter>> getTeams();
		
}
