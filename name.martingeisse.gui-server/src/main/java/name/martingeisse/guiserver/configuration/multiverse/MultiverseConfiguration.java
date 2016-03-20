/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.multiverse;

/**
 * Represents the configuration of the whole multiverse.
 */
public interface MultiverseConfiguration {

	/**
	 * Obtains the configuration for the hyperspace.
	 * 
	 * @return the hyperspace configuration
	 */
	public HyperspaceConfiguration getHyperspaceConfiguration();
	
	/**
	 * Obtains a universe configuration.
	 * 
	 * @param id the universe ID
	 * @return the configuration for that universe, or null if the universe doesn't exist
	 */
	public UniverseConfiguration getUniverseConfiguration(String id);

	/**
	 * Reloads a universe configuration. Does nothing if the universe doesn't exist.
	 * 
	 * @param id the universe ID
	 */
	public void reloadUniverseConfiguration(String id);

}
