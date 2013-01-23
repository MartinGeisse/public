/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.state;


/**
 * {@link PluginBundleStateKey} delegates plugin state handling to this class.
 */
class PluginStateCache {

	/**
	 * Prevent instantiation.
	 */
	private PluginStateCache() {
	}

	/**
	 * 
	 */
	static <T> void save(PluginBundleStateKey key, T data, IPluginBundleStateSerializer<T> serializer) {
		
	}
	
	/**
	 * 
	 */
	static <T> T load(PluginBundleStateKey key, IPluginBundleStateSerializer<T> serializer) {
		
	}
	
}
