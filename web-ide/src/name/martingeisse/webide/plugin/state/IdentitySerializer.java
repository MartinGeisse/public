/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.state;

/**
 * This serializer does not convert anything. It just passes
 * state values that already are byte arrays.
 */
public final class IdentitySerializer implements IPluginBundleStateSerializer<byte[]> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(byte[] state) {
		return state;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#deserialize(byte[])
	 */
	@Override
	public byte[] deserialize(byte[] serializedState) {
		return serializedState;
	}

}
