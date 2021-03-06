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
 * The built-in "is_scalar" function.
 */
public class IsScalarFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		Object argument = getMixedParameter(runtime, location, arguments, 0, null, true);
		if (argument == null) {
			return false;
		}
		if ((argument instanceof Integer) || (argument instanceof Long)) {
			return true;
		}
		if ((argument instanceof Float) || (argument instanceof Double)) {
			return true;
		}
		if (argument instanceof String) {
			return true;
		}
		if (argument instanceof Boolean) {
			return true;
		}
		return false;
	}

}
