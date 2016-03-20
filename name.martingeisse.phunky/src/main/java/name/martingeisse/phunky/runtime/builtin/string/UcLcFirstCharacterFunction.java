/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "ucfirst" and "lcfirst" functions.
 */
public final class UcLcFirstCharacterFunction extends BuiltinFunctionWithValueParametersOnly {

	/**
	 * the upper
	 */
	private final boolean upper;
	
	/**
	 * Constructor.
	 * @param upper true to make the first character upper-case, false for lower-case
	 */
	public UcLcFirstCharacterFunction(boolean upper) {
		this.upper = upper;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String s = getStringParameter(runtime, location, arguments, 0, null);
		if (s.isEmpty()) {
			return s;
		}
		char first = s.charAt(0);
		char ucfirst = (upper ? Character.toUpperCase(first) : Character.toLowerCase(first));
		if (first == ucfirst) {
			return s;
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(ucfirst);
			builder.append(s, 1, s.length());
			return builder.toString();
		}
	}
	
}
