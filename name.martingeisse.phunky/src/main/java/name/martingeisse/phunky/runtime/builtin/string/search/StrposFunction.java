/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string.search;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * The built-in "strpos" function.
 */
public class StrposFunction extends AbstractStringSearchFunction {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.string.search.AbstractStringSearchFunction#search(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.String, java.lang.String, int, java.lang.Object[])
	 */
	@Override
	protected Object search(PhpRuntime runtime, CodeLocation location, String haystack, String needle, int offset, Object[] arguments) {
		if (offset < 0 || offset > haystack.length()) {
			runtime.triggerError("string offset out of range: " + offset, location);
		}
		int index = haystack.indexOf(needle);
		return (index == -1 ? Boolean.FALSE : new Long(index));
	}
	
}
