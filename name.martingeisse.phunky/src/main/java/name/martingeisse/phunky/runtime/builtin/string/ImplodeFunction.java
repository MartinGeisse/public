/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The built-in "implode" and "join" functions.
 */
public final class ImplodeFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
			
		// extract parameters
		if (arguments.length != 2) {
			runtime.triggerError("implode() expects exactly 2 arguments, has " + arguments.length, location);
			return "";
		}
		boolean firstIsArray = (arguments[0] instanceof PhpValueArray);
		boolean secondIsArray = (arguments[1] instanceof PhpValueArray);
		PhpValueArray array;
		String glue;
		if (firstIsArray) {
			if (secondIsArray) {
				runtime.triggerError("Both arguments to implode() are arrays; cannot determine argument order", location);
				return "";
			} else {
				array = (PhpValueArray)arguments[0];
				glue = TypeConversionUtil.convertToString(arguments[1]);
			}
		} else {
			if (secondIsArray) {
				glue = TypeConversionUtil.convertToString(arguments[0]);
				array = (PhpValueArray)arguments[1];
			} else {
				runtime.triggerError("Neither argument to implode() is an array", location);
				return "";
			}
		}
		
		// implode the array
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object value : array.getValueIterable()) {
			if (first) {
				first = false;
			} else {
				builder.append(glue);
			}
			builder.append(TypeConversionUtil.convertToString(value));
		}
		return builder.toString();

	}
	
}
