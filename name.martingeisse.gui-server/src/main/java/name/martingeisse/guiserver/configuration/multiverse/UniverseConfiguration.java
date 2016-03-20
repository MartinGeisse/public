/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.multiverse;

import name.martingeisse.guiserver.configuration.element.Element;
import name.martingeisse.guiserver.configuration.storage.UniverseStorage;
import name.martingeisse.guiserver.template.IConfigurationSnippet;

import org.apache.wicket.request.mapper.ICompoundRequestMapper;

/**
 * The configuration for a single universe after it was loaded from a {@link UniverseStorage}.
 */
public interface UniverseConfiguration {

	/**
	 * Getter method for the serialNumber.
	 * @return the serialNumber
	 */
	public int getSerialNumber();
	
	/**
	 * Mounts the Wicket URLs needed by this configuration.
	 * 
	 * @param compoundRequestMapper the compount request mapper to register with
	 */
	public void mountWicketUrls(ICompoundRequestMapper compoundRequestMapper);
	
	/**
	 * Obtains a specific configuration element. Throws an exception if the element
	 * doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public <T extends Element> T getElement(Class<T> type, String path);

	/**
	 * Obtains a specific configuration element. Returns null if the element doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public <T extends Element> T getElementOrNull(Class<T> type, String path);

	/**
	 * Obtains a specific configuration element. Throws an exception if the element
	 * doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public Element getElement(String path);
	
	/**
	 * Obtains a specific configuration element. Returns null if the element doesn't exist.
	 * 
	 * @param type the type of configuration element to obtain
	 * @param path the path to the element
	 * @return the configuration element
	 */
	public Element getElementOrNull(String path);
	
	/**
	 * Obtains a configuration snippet.
	 * 
	 * @param handle the snippet handle
	 * @return the snippet
	 */
	public IConfigurationSnippet getSnippet(int handle);

}
