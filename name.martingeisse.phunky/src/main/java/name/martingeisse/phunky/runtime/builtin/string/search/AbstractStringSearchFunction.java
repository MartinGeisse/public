/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string.search;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

/**
 * Base class for the built-in string searching functions. These all take 
 * similar arguments: The first one is the "haystack", that is, the string
 * to search in. The second one is the "needle", which is either a string
 * to search for (some subclasses only extract the first character) or
 * the ordinal value of a character to search for.
 * 
 * This base class distinguishes these cases. Therefore, it provides a
 * string-oriented search function as well as a character-oriented one.
 * The default implementations call each other, so subclasses must
 * override at least one of them. These default implementations take
 * the first character of the haystack string, and turn the haystack
 * character into a string, respectively.
 * 
 * Some of the functions take a third parameter that specifies the starting
 * offset in the haystack. The offset can be negative to count from the end.
 * This base class recognizes the offset and normalizes it to a position
 * between 0..haystack.length() -- however, it depends on the subclass
 * whether this offset is used at all.
 */
public abstract class AbstractStringSearchFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		final String haystack = getStringParameter(runtime, arguments, 0, null);
		final Object rawNeedle = getMixedParameter(runtime, arguments, 1, null, true);
		final int offset = (int)getIntegerParameter(runtime, arguments, 2, 0L);
		if (rawNeedle == null) {
			return null;
		} else if (rawNeedle instanceof String) {
			return search(runtime, haystack, (String)rawNeedle, offset, arguments);
		} else {
			char needle = (char)getIntegerParameter(runtime, arguments, 1, null);
			return search(runtime, haystack, needle, offset, arguments);
		}
	}

	/**
	 * Searches the haystack for the needle.
	 * @param runtime the runtime
	 * @param haystack the haystack to search
	 * @param needle the needle to look for
	 * @param arguments the arguments, in case additional arguments can be recognized
	 * @return the function return value
	 */
	protected Object search(PhpRuntime runtime, String haystack, String needle, int offset, Object[] arguments) {
		char needleCharacter = (needle.isEmpty() ? 0 : needle.charAt(0));
		return search(runtime, haystack, needleCharacter, offset, arguments);
	}

	/**
	 * Searches the haystack for the needle, which is a single character. The default
	 * implementation turns the character into a string and uses the string-oriented
	 * search method.
	 * 
	 * Subclasses are free to implement a 
	 * 
	 * @param runtime the runtime
	 * @param haystack the haystack to search
	 * @param needle the needle to look for
	 * @param arguments the arguments, in case additional arguments can be recognized
	 * @return the function return value
	 */
	protected Object search(PhpRuntime runtime, String haystack, char needle, int offset, Object[] arguments) {
		return search(runtime, haystack, Character.toString(needle), offset, arguments);
	}

}
