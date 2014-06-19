/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.io;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;

/**
 * The built-in "print" function.
 */
public class PrintFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		final String s = getStringParameter(runtime, arguments, 0, null);
		runtime.getOutputWriter().print(s);
		return 1;
	}

}
