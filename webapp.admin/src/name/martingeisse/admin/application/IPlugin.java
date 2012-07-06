/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;


/**
 * All administration application plugins must implement this interface
 * so customization code can add them to the launcher at startup.
 * 
 * TODO: plugin unboxing: plugins should be able to return multiple other plugins
 * (recursively) which are all considered. Unboxing should occur before
 * contributions are made. This makes plugins more re-usable.
 * 
 */
public interface IPlugin {

	/**
	 * Contributes this plugin's capabilities to the application capabilities.
	 * This method accesses the singelton {@link ApplicationConfiguration}
	 * instance to add capabilities.
	 */
	public void contribute();
	
}
