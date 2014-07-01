/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The built-in "define" function.
 */
public class DefineFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		String name = getStringParameter(runtime, arguments, 0, null);
		Object value = getMixedParameter(runtime, arguments, 1, null, true);
		if (value != null && !TypeConversionUtil.isScalar(value)) {
			runtime.triggerError("cannot define constant '" + name + "' with non-scalar, non-null value");
			return false;
		}
		if (runtime.getConstants().containsKey(name)) {
			runtime.triggerError("constant '" + name + "' already defined");
			return false;
		}
		runtime.getConstants().put(name, value);
		return true;
	}

}
