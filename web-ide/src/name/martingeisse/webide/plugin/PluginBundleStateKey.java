/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import name.martingeisse.webide.plugin.serializer.IPluginBundleStateSerializer;

/**
 * This key identifies a plugin bundle state variable. It consists of:
 * - a plugin bundle identifier, represented by a {@link PluginBundleStateAccessToken}.
 *   Each bundle has its own state variables, and access to it requires a token.
 * - a user ID. All state is user-specific.
 * - a section number. Bundles can organize their state into multiple "sections" at
 *   will, identified by arbitrary int values.
 * 
 * This class is also used by plugin code to actually load and save bundle state.
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
 *   object. The main consequences are:
 *   
 *    - saved state values should not be modified once they are in the cache,
 *      because code loading the state later may get the original value or a
 *      copy.
 *      
 *    - all load and save operations for the same key *must* use the same
 *      serializer and high-level type, since it is unspecified whether the
 *      serializer is actually used, or a cached value returned. Suppose
 *      calling code A uses Serializer S1 to serialize a value of type T.
 *      Calling code B then loads that value using (de-)serializer S2 of type
 *      U. However, B might still receive an instance of type T if the cached
 *      value is used (immediately resulting in a {@link ClassCastException}
 *      that gets thrown by Java's generic type system).
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
 * TODO: add workspace id for workspace-specific state
 */
public final class PluginBundleStateKey {

	/**
	 * ID of the plugin bundle that stores data, represented by an access token
	 */
	private final PluginBundleStateAccessToken accessToken;

	/**
	 * ID of the user who is using the plugin
	 */
	private final long userId;

	/**
	 * section index for plugin bundles that store multiple data sections
	 */
	private final int section;

	/**
	 * Constructor.
	 * @param accessToken ID of the plugin bundle that stores data, represented by an access token
	 * @param userId ID of the user who is using the plugin
	 * @param section section index for plugin bundles that store multiple data sections
	 */
	public PluginBundleStateKey(PluginBundleStateAccessToken accessToken, long userId, int section) {
		this.accessToken = accessToken;
		this.userId = userId;
		this.section = section;
	}

	/**
	 * Getter method for the accessToken.
	 * @return the accessToken
	 */
	public PluginBundleStateAccessToken getAccessToken() {
		return accessToken;
	}

	/**
	 * Getter method for the userId.
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Getter method for the section.
	 * @return the section
	 */
	public int getSection() {
		return section;
	}

	/**
	 * Saves plugin bundle state for this key.
	 * @param data the state data
	 * @param serializer the serializer
	 */
	public <T> void save(T data, IPluginBundleStateSerializer<T> serializer) {
		PluginStateCache.save(this, data, serializer);
	}

	/**
	 * Loads plugin bundle state for this key. Returns null if the state was not found.
	 * @param serializer the serializer
	 * @return the state data or null
	 */
	public <T> T load(IPluginBundleStateSerializer<T> serializer) {
		return PluginStateCache.load(this, serializer);
	}
	
}
