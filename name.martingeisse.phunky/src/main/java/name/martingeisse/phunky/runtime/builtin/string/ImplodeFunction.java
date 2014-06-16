/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.value.PhpArray;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;
import name.martingeisse.phunky.util.Name;

/**
 * The built-in "implode" function.
 */
@Name("implode")
public final class ImplodeFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		
		// extract parameters
		if (arguments.length != 2) {
			runtime.triggerError("implode() expects exactly 2 arguments, has " + arguments.length);
			return "";
		}
		boolean firstIsArray = (arguments[0] instanceof PhpArray);
		boolean secondIsArray = (arguments[1] instanceof PhpArray);
		PhpArray array;
		String glue;
		if (firstIsArray) {
			if (secondIsArray) {
				runtime.triggerError("Both arguments to implode() are arrays; cannot determine argument order");
				return "";
			} else {
				array = (PhpArray)arguments[0];
				glue = TypeConversionUtil.convertToString(arguments[1]);
			}
		} else {
			if (secondIsArray) {
				glue = TypeConversionUtil.convertToString(arguments[0]);
				array = (PhpArray)arguments[1];
			} else {
				runtime.triggerError("Neither argument to implode() is an array");
				return "";
			}
		}
		
		// implode the array
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Object value : array.values()) {
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
