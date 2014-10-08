/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

import org.apache.commons.lang3.StringUtils;

/**
 * General-purpose function that finds a substring ("needle") within a larger string ("haystack").
 */
public final class FindSubstringFunction extends BuiltinFunctionWithValueParametersOnly {

	/**
	 * the caseSensitive
	 */
	private final boolean caseSensitive;

	/**
	 * the reverseSearch
	 */
	private final boolean reverseSearch;

	/**
	 * the gotoEndOfSubstring
	 */
	private final boolean gotoEndOfSubstring;

	/**
	 * the returnPrefix
	 */
	private final boolean returnPrefix;

	/**
	 * the returnContents
	 */
	private final boolean returnSuffix;

	/**
	 * Constructor.
	 * 
	 * If neither returnPrefix nor returnSuffix are true, the position itself is returned. If the
	 * substring was not found, the function returns false.
	 * 
	 * @param caseSensitive whether the search is case-sensitive
	 * @param reverseSearch whether to search from the end of the string, rather than the beginning
	 * @param gotoEndOfSubstring if false, uses the position of the first character of the substring. If true,
	 * uses the position of the first character *after* the substring
	 * @param returnPrefix if true, returns the substring from the beginning to the position found
	 * @param returnSuffix if true, returns the substring from the position found to the end of the string
	 */
	public FindSubstringFunction(boolean caseSensitive, boolean reverseSearch, boolean gotoEndOfSubstring, boolean returnPrefix, boolean returnSuffix) {
		this.caseSensitive = caseSensitive;
		this.reverseSearch = reverseSearch;
		this.gotoEndOfSubstring = gotoEndOfSubstring;
		this.returnPrefix = returnPrefix;
		this.returnSuffix = returnSuffix;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		final String haystack = getStringParameter(runtime, arguments, 0, null);
		final String needle = getStringParameter(runtime, arguments, 0, null);
		final int startPosition;
		if (caseSensitive) {
			if (reverseSearch) {
				startPosition = StringUtils.lastIndexOf(haystack, needle);
			} else {
				startPosition = StringUtils.indexOf(haystack, needle);
			}
		} else {
			if (reverseSearch) {
				startPosition = StringUtils.lastIndexOfIgnoreCase(haystack, needle);
			} else {
				startPosition = StringUtils.indexOfIgnoreCase(haystack, needle);
			}
		}
		final int position = (gotoEndOfSubstring ? (startPosition + needle.length()) : startPosition);
		if (position == -1) {
			return false;
		} else if (returnPrefix) {
			return haystack.substring(0, position);
		} else if (returnSuffix) {
			return haystack.substring(position);
		} else {
			return position;
		}
	}

}
