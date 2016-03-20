/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "substr" function.
 */
public final class SubstrFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		
		// extract parameters
		final String string = getStringParameter(runtime, location, arguments, 0, null);
		final int stringLength = string.length();
		final int declaredStart = (int)getIntegerParameter(runtime, location, arguments, 1, null);
		final int declaredLength = (int)getIntegerParameter(runtime, location, arguments, 2, (long)stringLength);
		
		// shortcut with different return value behavior
		if (declaredLength == 0) {
			return "";
		}
		
		// obtain the actual starting position
		int actualStart;
		if (declaredStart < 0) {
			actualStart = declaredStart + stringLength;
			if (actualStart < 0) {
				actualStart = 0;
			}
		} else if (declaredStart > stringLength) {
			return false;
		} else {
			actualStart = declaredStart;
		}
		
		// obtain the actual ending position
		int actualEnd;
		if (declaredLength < 0) {
			actualEnd = stringLength + declaredLength;
		} else {
			actualEnd = actualStart + declaredLength;
		}
		if (actualEnd < actualStart) {
			return false;
		}
		if (actualEnd > stringLength) {
			actualEnd = stringLength;
		}
		
		return string.substring(actualStart, actualEnd);
	}
	
}
