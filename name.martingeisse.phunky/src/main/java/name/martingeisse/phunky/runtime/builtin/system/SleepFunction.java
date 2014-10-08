/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

/**
 * The built-in "sleep" function.
 */
public class SleepFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		int seconds = getIntParameter(runtime, arguments, 0, 0);
		if (seconds < 0) {
			runtime.triggerError("sleep() called with a negative argument");
			return false;
		}
		long startTime = System.currentTimeMillis();
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			long endTime = System.currentTimeMillis();
			return (int)((endTime - startTime) / 1000);
		}
		return 0;
	}

}
