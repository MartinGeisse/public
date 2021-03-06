/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.var;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "is_numeric" function.
 */
public class IsNumericFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		Object argument = getMixedParameter(runtime, location, arguments, 0, null, true);
		if ((argument instanceof Integer) || (argument instanceof Long) || (argument instanceof Float) || (argument instanceof Double)) {
			return true;
		}
		if (argument instanceof String) {
			String s = (String)argument;
			try {
				Double.parseDouble(s);
				return true;
			} catch (NumberFormatException e) {
			}
			try {
				Long.parseLong(s);
				return true;
			} catch (NumberFormatException e) {
			}
			return false;
		}
		return false;
	}

}
