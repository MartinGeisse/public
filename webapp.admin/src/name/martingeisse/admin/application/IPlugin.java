/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

/**
 * All administration application plugins must implement this interface
 * so customization code can add them to the launcher at startup.
 */
public interface IPlugin {

	/**
	 * Unboxes this plugin, returning additional plugins to consider.
	 * This allows plugins to be re-used as part of a larger plugin.
	 * Note that unboxing is not a proper dependency mechanism -- if two
	 * plugins "contain" the same helper plugin, it will be added twice.
	 * Rather, this mechanism is meant for "distributions" of plugins.
	 * 
	 * Returned plugins are unboxed recursively.
	 * 
	 * @return the contained plugins. May return null instead of an empty
	 * array to indicate that it does not contain any other plugins.
	 */
	public IPlugin[] unbox();

	/**
	 * Contributes this plugin's capabilities to the application capabilities.
	 * This method accesses the singleton {@link ApplicationConfiguration}
	 * instance to add capabilities.
	 */
	public void contribute();

}
