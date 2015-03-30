/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.util.NoSuchElementException;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;

import name.martingeisse.guiserver.configuration.multiverse.DefaultMultiverseConfiguration;
import name.martingeisse.guiserver.configuration.multiverse.MultiverseConfiguration;
import name.martingeisse.guiserver.configuration.multiverse.UniverseConfiguration;

/**
 * Stores the multiverse configuration as a singleton object.
 */
public final class ConfigurationHolder {
	
	/**
	 * Prevent instantiation.
	 */
	private ConfigurationHolder() {
	}

	/**
	 * the multiverseConfiguration
	 */
	private static MultiverseConfiguration multiverseConfiguration;
	
	/**
	 * 
	 */
	public static void initialize() {
		multiverseConfiguration = new DefaultMultiverseConfiguration();
	}
	
	/**
	 * Getter method for the multiverseConfiguration.
	 * @return the multiverseConfiguration
	 */
	public static MultiverseConfiguration getMultiverseConfiguration() {
		return multiverseConfiguration;
	}

	/**
	 * Obtains the {@link UniverseConfiguration} for the current request.
	 * @return the {@link UniverseConfiguration} for the current request, or null if the current request
	 * cannot be mapped to a universe
	 */
	public static UniverseConfiguration getRequestUniverseConfiguration() {
		Request request = RequestCycle.get().getRequest();
		if (request instanceof WebRequest) {
			String hostHeader = ((WebRequest)request).getHeader("host");
			if (hostHeader != null) {
				String universeId = multiverseConfiguration.getHyperspaceConfiguration().mapHostToUniverseId(hostHeader);
				if (universeId != null) {
					return multiverseConfiguration.getUniverseConfiguration(universeId);
				}
			}
		}
		return null;
	}

	/**
	 * Obtains the {@link UniverseConfiguration} for the current request, throwing an exception if no such
	 * universe configuration can be found.
	 * 
	 * @return the {@link UniverseConfiguration} for the current request
	 */
	public static UniverseConfiguration needRequestUniverseConfiguration() throws NoSuchElementException {
		UniverseConfiguration universeConfiguration = getRequestUniverseConfiguration();
		if (universeConfiguration == null) {
			throw new NoSuchUniverseException("request universe not found");
		}
		return universeConfiguration;
	}

}
