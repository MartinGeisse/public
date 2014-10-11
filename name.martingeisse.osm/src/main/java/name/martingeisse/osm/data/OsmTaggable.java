/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.osm.data;

import java.util.HashMap;

/**
 * Base class for OSM objects that can have tags.
 */
public class OsmTaggable {

	/**
	 * the tags
	 */
	private final HashMap<String, String> tags = new HashMap<>();
	
	/**
	 * Getter method for the tags.
	 * @return the tags
	 */
	public HashMap<String, String> getTags() {
		return tags;
	}
	
}
