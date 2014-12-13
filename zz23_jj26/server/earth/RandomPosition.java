package zz23_jj26.server.earth;

import java.io.Serializable;

public class RandomPosition implements Serializable {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -7730988590694338404L;
	
	private double latitude;
	private double longtitude;
	private double offset;
	
	public RandomPosition (double latitude, double longtitude, double offset){
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.offset = offset;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public double getOffset(){
		return offset;
	}

}
