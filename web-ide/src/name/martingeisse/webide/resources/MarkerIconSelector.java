/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

/**
 * This enum type selects an icon for a marker.
 */
public enum MarkerIconSelector {

	/**
	 * used for warning markers
	 */
	WARNING("error.png"),
	
	/**
	 * used for error markers
	 */
	ERROR("exclamation.png"),
	
	/**
	 * used if no other icon is appropriate
	 */
	MISSING_ICON("tag_blue.png");
	
	/**
	 * the filename
	 */
	private final String filename;
	
	/**
	 * Constructor.
	 * @param filename the simple icon filename
	 */
	private MarkerIconSelector(String filename) {
		this.filename = filename;
	}

	/**
	 * Getter method for the filename.
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
}
