/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.serializer;

/**
 * An instance of this interface is used whenever plugin bundle state
 * is loaded/saved from/to the database and its associated cache.
 * It converts between high-level state objects and byte arrays.
 *
 * @param <T> the state type
 */
public interface IPluginBundleStateSerializer<T> {

	/**
	 * Serializes the specified state value.
	 * @param state the state to serialize
	 * @return the serialized data
	 */
	public byte[] serialize(T state);
	
	/**
	 * De-serializes the specified serialized state data.
	 * @param serializedState the serialized data
	 * @return the state value
	 */
	public T deserialize(byte[] serializedState);
	
}
