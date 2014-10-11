/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "bin2hex" function.
 */
public final class Bin2HexFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String binString = getStringParameter(runtime, location, arguments, 0, null);
		StringBuilder builder = new StringBuilder();
		int position = 0, length = binString.length();
		while (position < length) {
			int value = binString.charAt(position);
			if (value > 255) {
				runtime.triggerError("Non-byte string element (value " + value + ") cannot be handled by bin2hex() -- skipping this element.", location);
				continue;
			}
			builder.append(Integer.toHexString(value));
			position++;
		}
		return builder.toString();
	}

}
