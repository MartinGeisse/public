/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.io;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * The built-in "echo" function.
 */
public class EchoFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		for (Object argument : arguments) {
			runtime.getOutputWriter().print(TypeConversionUtil.convertToString(argument));
		}
		return null;
	}

}