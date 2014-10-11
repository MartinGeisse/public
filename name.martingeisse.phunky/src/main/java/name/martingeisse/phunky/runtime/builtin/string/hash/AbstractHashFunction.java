/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string.hash;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * Base class for hash code computing functions such as md5() and sha1().
 */
public abstract class AbstractHashFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String s = getStringParameter(runtime, location, arguments, 0, null);
		boolean raw = getBooleanParameter(runtime, location, arguments, 1, false);
		byte[] hashCode = hash(TypeConversionUtil.mapStringDirectlyToBinary(s));
		StringBuilder builder = new StringBuilder();
		for (byte b : hashCode) {
			int value = (b & 0xff);
			if (raw) {
				builder.append((char)value);
			} else {
				builder.append(Integer.toHexString(value));
			}
		}
		return builder.toString();
	}

	/**
	 * Computes the actual hash code, based purely on binary data.
	 * @param data the input data
	 * @return the hash data
	 */
	protected abstract byte[] hash(byte[] data);
	
}
