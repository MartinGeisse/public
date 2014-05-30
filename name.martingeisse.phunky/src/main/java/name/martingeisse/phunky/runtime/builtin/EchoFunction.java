/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;
import name.martingeisse.phunky.util.Name;

/**
 * The built-in "echo" function.
 */
@Name("echo")
public class EchoFunction extends BuiltinFunction {

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
