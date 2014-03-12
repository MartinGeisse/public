/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.Callable;

/**
 * The built-in "echo" function.
 */
public class EchoFunction implements Callable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(java.lang.Object[])
	 */
	@Override
	public Object call(Object[] arguments) {
		if (arguments.length >= 1) {
			Object argument = arguments[0];
			String text = (argument == null ? "" : argument.toString());
			System.out.println(text);
		}
		return null;
	}

}
