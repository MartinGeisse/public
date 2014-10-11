/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.io;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "basename" function.
 */
public class BasenameFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		String path = getStringParameter(runtime, location, arguments, 0, null);
		final String suffix = getStringParameter(runtime, location, arguments, 1, "");
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
