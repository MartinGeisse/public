/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.state;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Uses the JVM serialization mechanism to serialize
 * arbitrary objects.
 */
public final class JvmSerializer implements IPluginStateSerializer<Serializable> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Serializable state) {
		return SerializationUtils.serialize(state);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#deserialize(byte[])
	 */
	@Override
	public Serializable deserialize(byte[] serializedState) {
		return (Serializable)SerializationUtils.deserialize(serializedState);
	}

}
