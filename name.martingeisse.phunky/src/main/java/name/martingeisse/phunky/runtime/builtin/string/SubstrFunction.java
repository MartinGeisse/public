/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunction;
import name.martingeisse.phunky.util.Name;

/**
 * The built-in "substr" function.
 */
@Name("substr")
public final class SubstrFunction extends BuiltinFunction {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		
		// extract parameters
		final String string = getStringParameter(runtime, arguments, 0, null);
		final int stringLength = string.length();
		final int declaredStart = getIntParameter(runtime, arguments, 1, null);
		final int declaredLength = getIntParameter(runtime, arguments, 2, stringLength);
		
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
