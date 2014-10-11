/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The built-in "define" function.
 */
public class DefineFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String name = getStringParameter(runtime, location, arguments, 0, null);
		Object value = getMixedParameter(runtime, location, arguments, 1, null, true);
		if (value != null && !TypeConversionUtil.isScalar(value)) {
			runtime.triggerError("cannot define constant '" + name + "' with non-scalar, non-null value", location);
			return false;
		}
		if (runtime.getConstants().containsKey(name)) {
			runtime.triggerError("constant '" + name + "' already defined", location);
			return false;
		}
		runtime.getConstants().put(name, value);
		return true;
	}

}
