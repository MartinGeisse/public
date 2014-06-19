/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.var;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;

/**
 * The built-in "is_bool" function.
 */
public class IsBoolFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		return (arguments.length > 0 && arguments[0] instanceof Boolean);
	}

}
