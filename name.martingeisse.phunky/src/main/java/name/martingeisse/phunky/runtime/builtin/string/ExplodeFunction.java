/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import java.util.ArrayList;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;

/**
 * The built-in "explode" function.
 */
public final class ExplodeFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		
		// extract parameters
		final String delimiter = getStringParameter(runtime, arguments, 0, null);
		String subject = getStringParameter(runtime, arguments, 1, null);
		final int limit = getIntParameter(runtime, arguments, 2, Integer.MAX_VALUE);
		
		// special cases
		if (limit == 0 || limit == 1) {
			return subject;
		}
		if (subject.isEmpty()) {
			return new PhpVariableArray();
		}
		
		// TODO implement
		if (limit < 0) {
			throw new RuntimeException("explode() with negative limit not yet implemented");
		}
		
		// explode the string
		
		TODO Variable array!?
		
		ArrayList<String> segments = new ArrayList<String>();
		final int delimiterLength = delimiter.length();
		while (limit > 1) {
			int index = subject.indexOf(delimiter);
			if (index == -1) {
				segments.add(subject);
				return PhpVariableArray.fromValues(segments);
			}
			segments.add(subject.substring(0, index));
			subject = subject.substring(index + delimiterLength);
		}
		segments.add(subject);
		return PhpVariableArray.fromValues(segments);

	}
	
}
