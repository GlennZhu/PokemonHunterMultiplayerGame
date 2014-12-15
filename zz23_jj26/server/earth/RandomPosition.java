package zz23_jj26.server.earth;

import java.io.Serializable;

/**
 * A serializable class for storing a random position
 * @author Jiafang Jiang
 *
 */
public class RandomPosition implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -7730988590694338404L;
	
	/**
	 * Latitude of the position
	 */
	private double latitude;
	
	/**
	 * Longitude of the position
	 */
	private double longitude;
	
	/**
	 * Offset for setting up other two laton corners
	 */
	private double offset;
	
	/**
	 * Constructor of RandomPosition
	 * @param latitude Latitude of the position
	 * @param longitude Longitude of the position
	 * @param offset Offset for setting up other two laton corners
	 */
	public RandomPosition (double latitude, double longitude, double offset){
		this.latitude = latitude;
		this.longitude = longitude;
		this.offset = offset;
	}

	/**
	 * Return the latitude of the position
	 * @return the latitude of the position
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Return the longitude of the position
	 * @return the longitude of the position
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Return the offset for setting up other two laton corners
	 * @return the offset for setting up other two laton corners
	 */
	public double getOffset(){
		return offset;
	}

}
