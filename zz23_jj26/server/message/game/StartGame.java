package zz23_jj26.server.message.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import common.chatroom.IChatroomAdapter;

import zz23_jj26.server.earth.RandomPosition;
import zz23_jj26.server.main.model.SerializedImage;

public class StartGame implements IStartGame{
	private static final long serialVersionUID = 3379993531380808396L;

	private ArrayList<SerializedImage> images;
	private UUID uuid;
	private ArrayList<RandomPosition> positions;
	private HashMap<String, ArrayList<IChatroomAdapter>> teams;
//	private RequestRemovePokemon requestMsg;

	
	public StartGame(ArrayList<SerializedImage> srlImg, ArrayList<RandomPosition> positions, UUID uuid, HashMap<String, ArrayList<IChatroomAdapter>> teams){
		this.images = srlImg;
		this.uuid = uuid;
		this.positions = positions;
		this.teams = teams;
	}
	
	@Override
	public ArrayList<BufferedImage> getImages() {
		ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
		for (SerializedImage img: images){
			image.add(img.getImage());
		}
		return image;
	}

	@Override
	public IRequestRemovePokemon getRemovePokemonMessage() {
		return new RequestRemovePokemon();
	}
	
	public UUID getUUID(){
		return this.uuid;
	}

	@Override
	public ArrayList<RandomPosition> getPositions() {
		return positions;
	}
	
	public HashMap<String, ArrayList<IChatroomAdapter>> getTeams(){
		return teams;
	}
}
