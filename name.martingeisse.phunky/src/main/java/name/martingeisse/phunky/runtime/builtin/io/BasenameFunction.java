/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.io;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

/**
 * The built-in "basename" function.
 */
public class BasenameFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		String path = getStringParameter(runtime, arguments, 0, null);
		final String suffix = getStringParameter(runtime, arguments, 1, "");
		int index = path.lastIndexOf('/');
		if (index != -1) {
			path = path.substring(index + 1);
		}
		if (!suffix.isEmpty() && path.endsWith(suffix)) {
			path = path.substring(0, path.length() - suffix.length());
		}
		return path;
	}

}
