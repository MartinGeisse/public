/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.data;

/**
 * A map node.
 */
public final class OsmNode extends OsmTaggable implements OsmRelationMember {

	/**
	 * the id
	 */
	private long id;

	/**
	 * the latitude
	 */
	private double latitude;

	/**
	 * the longitude
	 */
	private double longitude;

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter method for the latitude.
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Setter method for the latitude.
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Getter method for the longitude.
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Setter method for the longitude.
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
