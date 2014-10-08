/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.ExitException;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The built-in "die" function.
 */
public class DieFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		int exitCode = 0;
		String exitMessage = "";
		if (arguments.length > 0) {
			if (arguments[0] instanceof Integer) {
				exitCode = (Integer)arguments[0];
			} else {
				exitMessage = TypeConversionUtil.convertToString(arguments[0]);
			}
		}
		runtime.getOutputWriter().println(exitMessage);
		throw new ExitException(exitCode);
	}

}
