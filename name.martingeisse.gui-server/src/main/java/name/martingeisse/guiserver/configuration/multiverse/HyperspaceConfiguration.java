/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.multiverse;

import name.martingeisse.guiserver.configuration.storage.MultiverseStorage;

/**
 * Represents the configuration of the hyperspace, i.e. those parts of the
 * multiverse that do not belong to a single universe.
 */
public interface HyperspaceConfiguration {
	
	/**
	 * Obtains the universe storage implementation for the whole multiverse.
	 * 
	 * @return the multiverse storage
	 */
	public MultiverseStorage getMultiverseStorage();
	
	/**
	 * Maps a "Host" header value from a request to a universe ID.
	 * 
	 * @param host the host header value
	 * @return the universe ID
	 */
	public String mapHostToUniverseId(String host);
	
}
