/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin;

import name.martingeisse.webide.plugin.state.IPluginStateSerializer;

/**
 * {@link InternalPluginUtil} delegates plugin state handling to this class.
 * 
 * Plugin state can be stored at multiple levels:
 * 
 * - plugin bundles can use runtime variables just like any Java code. However,
 *   this data is lost when the plugin is unloaded, e.g. to free memory.
 *   Plugins should therefore save non-transient information through the
 *   state cache.
 *   
 * - The state cache may keep state values (still Java objects) passed to
 *   it to make them available to subsequent load requests. As long as the
 *   plugin does not overwrite the cache entry, the cache is free to keep
 *   such values indefinitely. Any load request will just return the cached
 *   object. The main consequence is that saved state values should not be 
 *   modified once they are in the cache, because code loading the state
 *   later may get the original value or a copy.
 * 
 * - The state cache may keep the serialized state values and on subsequent
 *   load requests just de-serialize them. As long as the plugin does not
 *   overwrite the cache entry, the cache is free to keep such values
 *   indefinitely.
 * 
 * - Saved state is written to the database eventually. If none of the other
 *   mechanisms has a copy of state to load, it will be loaded from the
 *   database, then de-serialized.
 * 
 */
class PluginStateCache {

	/**
	 * Prevent instantiation.
	 */
	private PluginStateCache() {
	}

	static <T> void save(Key key, T data, IPluginStateSerializer<T> serializer) {
		
	}
	
	/**
	 * Immutable class that stores the information that is used as the key
	 * in both the state cache and underlying storage.
	 */
	final class Key {
		
		/**
		 * ID of the plugin bundle that stores data
		 */
		final long pluginBundleId;
		
		/**
		 * ID of the user who is using the plugin
		 */
		final long userId;
		
		/**
		 * section index for plugin bundles that store multiple data sections
		 */
		final int section;
		
		/**
		 * Constructor.
		 */
		Key(long pluginBundleId, long userId, int section) {
			this.pluginBundleId = pluginBundleId;
			this.userId = userId;
			this.section = section;
		}
		
	}
	
}
