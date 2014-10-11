/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "sleep" function.
 */
public class SleepFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		long seconds = getIntegerParameter(runtime, location, arguments, 0, 0L);
		if (seconds < 0) {
			runtime.triggerError("sleep() called with a negative argument", location);
			return false;
		}
		long startTime = System.currentTimeMillis();
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			long endTime = System.currentTimeMillis();
			return (long)((endTime - startTime) / 1000);
		}
		return 0;
	}

}
