package com.girnar.explorejaipur.jsondata;

/**
 * The Class MonumentsData.
 */
public class Monuments {
	private String monumentId;
	private String name;
	private String category;
	private String description;
	private String location;
	private String latitude;
	private String longitude;
	private String distance;
	private String distanceBusStand;
	private String image;

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the monumentId
	 */
	public String getMonumentId() {
		return monumentId;
	}

	/**
	 * @param monumentId the monumentId to set
	 */
	public void setMonumentId(String monumentId) {
		this.monumentId = monumentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the distanceBusStand
	 */
	public String getDistanceBusStand() {
		return distanceBusStand;
	}

	/**
	 * @param distanceBusStand the distanceBusStand to set
	 */
	public void setDistanceBusStand(String distanceBusStand) {
		this.distanceBusStand = distanceBusStand;
	}

}
