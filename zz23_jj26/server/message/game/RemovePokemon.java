package zz23_jj26.server.message.game;


import java.util.UUID;

import gov.nasa.worldwind.geom.Position;
import common.message.chat.IChatMessage;

public class RemovePokemon implements IChatMessage {

	private static final long serialVersionUID = -4896442957425189512L;
	
	private double lat;
	private double lon;
	private UUID commonUUID;

	public RemovePokemon(double mylatitude, double mylongitude, UUID commonUUID){
		lat = mylatitude;
		lon = mylongitude;
		this.commonUUID = commonUUID;
	}
	
	public Position getPos(){
		return Position.fromDegrees(lat, lon);
	}
	
	public UUID getUUID(){
		return commonUUID;
	}
}
