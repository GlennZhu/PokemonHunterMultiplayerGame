package zz23_jj26.server.message.game;

import java.util.UUID;

import gov.nasa.worldwind.geom.Position;
import common.message.chat.IChatMessage;

public interface IRequestRemovePokemon extends IChatMessage {

	public void setPosToRemove(double latitude, double longitude);
	
	public Position getPosToRemove();
	
	public void setUUID(UUID setMe);
	
	public UUID getUUID();
	
}
