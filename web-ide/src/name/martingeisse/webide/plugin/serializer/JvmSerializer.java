/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin.serializer;

import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Uses the JVM serialization mechanism to serialize
 * arbitrary {@link Serializable} objects.
 */
public final class JvmSerializer implements IPluginBundleStateSerializer<Serializable> {

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
