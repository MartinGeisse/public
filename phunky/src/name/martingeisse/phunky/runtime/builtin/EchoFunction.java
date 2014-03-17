/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.PhpRuntime;

/**
 * The built-in "echo" function.
 */
public class EchoFunction implements Callable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		if (arguments.length >= 1) {
			Object argument = arguments[0];
			String text = (argument == null ? "" : argument.toString());
			runtime.getOutputWriter().print(text);
		}
		return null;
	}

}
