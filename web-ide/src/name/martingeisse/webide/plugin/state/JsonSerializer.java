/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin.state;

import java.nio.charset.Charset;

import org.json.simple.JSONValue;

/**
 * Serializes a structure of JSON-compatible values to
 * JSON text.
 */
public final class JsonSerializer implements IPluginBundleStateSerializer<Object> {

	//
	private static final Charset charset = Charset.forName("utf-8");

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Object state) {
		return JSONValue.toJSONString(state).getBytes(charset);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.plugin.state.IPluginStateSerializer#deserialize(byte[])
	 */
	@Override
	public Object deserialize(byte[] serializedState) {
		return JSONValue.parse(new String(serializedState, charset));
	}

}
