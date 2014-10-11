/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;

import org.apache.commons.lang3.StringUtils;

/**
 * The built-in "trim", "ltrim", "rtrim" and "chop" functions.
 */
public final class TrimFunction extends BuiltinFunctionWithValueParametersOnly {

	/**
	 * the left
	 */
	private final boolean trimLeft;

	/**
	 * the trimRight
	 */
	private final boolean trimRight;

	/**
	 * Constructor.
	 * @param trimLeft whether to trim characters on the left side of the input string
	 * @param trimRight whether to trim characters on the right side of the input string
	 */
	public TrimFunction(final boolean trimLeft, final boolean trimRight) {
		this.trimLeft = trimLeft;
		this.trimRight = trimRight;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {

		// extract parameters
		final String string = getStringParameter(runtime, location, arguments, 0, null);
		final String charlist = getStringParameter(runtime, location, arguments, 1, " \t\n\r\0\13"); // TODO support "..." syntax
		
		// trim the string
		if (trimLeft) {
			if (trimRight) {
				return StringUtils.strip(string, charlist);
			} else {
				return StringUtils.stripStart(string, charlist);
			}
		} else {
			if (trimRight) {
				return StringUtils.stripEnd(string, charlist);
			} else {
				return string;
			}
		}
		
	}

}
