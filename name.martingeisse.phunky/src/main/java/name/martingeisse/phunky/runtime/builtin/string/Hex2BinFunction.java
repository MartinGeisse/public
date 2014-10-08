/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

/**
 * The built-in "hex2bin" function.
 */
public final class Hex2BinFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		String hexString = getStringParameter(runtime, arguments, 0, null);
		StringBuilder builder = new StringBuilder();
		int position = 0, length = hexString.length() & ~1;
		while (position < length) {
			int highDigitValue = Character.digit(hexString.charAt(position), 16);
			int lowDigitValue = Character.digit(hexString.charAt(position + 1), 16);
			int value = (highDigitValue << 4) + lowDigitValue;
			builder.append((char)value);
			position += 2;
		}
		return builder.toString();
	}

}
