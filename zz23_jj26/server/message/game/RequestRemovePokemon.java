package zz23_jj26.server.message.game;

import java.util.UUID;

import gov.nasa.worldwind.geom.Position;

public class RequestRemovePokemon implements IRequestRemovePokemon {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 4235248585379216440L;
	
	private double latitude;
	private double longitude;
	private UUID commonUUID;

	@Override
	public void setPosToRemove(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position getPosToRemove(){
		return Position.fromDegrees(latitude, longitude);
	}
	
	public void setUUID(UUID setMe){
		this.commonUUID = setMe;
	}
	
	public UUID getUUID(){
		return commonUUID;
	}

}
