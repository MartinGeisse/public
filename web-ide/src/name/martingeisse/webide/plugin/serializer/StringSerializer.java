/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin.serializer;

import java.nio.charset.Charset;

/**
 * Expects the state value to be a string, and serializes it
 * by UTF-8 encoding it.
 */
public final class StringSerializer implements IPluginBundleStateSerializer<String> {

	//
	private static final Charset charset = Charset.forName("utf-8");
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(String state) {
		return state.getBytes(charset);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#deserialize(byte[])
	 */
	@Override
	public String deserialize(byte[] serializedState) {
		return new String(serializedState, charset);
	}

}
