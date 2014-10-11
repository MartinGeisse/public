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
 * The built-in "str_repeat" function.
 */
public final class StrRepeatFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String s = getStringParameter(runtime, location, arguments, 0, null);
		int multiplier = (int)getIntegerParameter(runtime, location, arguments, 1, null);
		StringBuilder builder = new StringBuilder(s.length() * multiplier);
		while (multiplier > 0) {
			builder.append(s);
			multiplier--;
		}
		return builder.toString();
	}

}
