/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import org.apache.commons.lang3.StringUtils;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;

/**
 * The built-in "levenshtein" function.
 */
public final class LevenshteinFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		String s1 = getStringParameter(runtime, arguments, 0, null);
		String s2 = getStringParameter(runtime, arguments, 1, null);
		return StringUtils.getLevenshteinDistance(s1, s2);
	}
	
}
