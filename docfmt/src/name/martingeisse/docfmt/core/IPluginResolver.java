/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.core;

/**
 * Hides the logic that provides installed plugins.
 */
public interface IPluginResolver {

	/**
	 * Resolves the specified plugin ID and returns the loaded plugin.
	 * 
	 * @param pluginId the plugin ID
	 * @return the plugin
	 */
	public Plugin resolvePlugin(String pluginId);
	
}
