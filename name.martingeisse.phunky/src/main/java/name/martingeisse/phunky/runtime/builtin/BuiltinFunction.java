/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.Callable;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;
import name.martingeisse.phunky.util.Name;

/**
 * Helper class to simplify the implementation of built-in functions.
 */
public abstract class BuiltinFunction implements Callable {

	/**
	 * Returns the name of this function, as specified by the {@link Name} annotation.
	 * @return the name
	 */
	public String getName() {
		Name name = getClass().getAnnotation(Name.class);
		return (name != null ? name.value() : getClass().getSimpleName());
	}
	
	/**
	 * Returns argument #position as a string. If the number of arguments is less than
	 * that, and a defaultValue is specified, it is returned. Otherwise, this method triggers
	 * an error and the empty string is returned.
	 * 
	 * @param runtime the runtime
	 * @param arguments the function arguments
	 * @param position the argument position
	 * @param defaultValue the default value, or null if none
	 * @return the value (never null)
	 */
	protected final String getStringParameter(PhpRuntime runtime, Object[] arguments, int position, String defaultValue) {
		if (arguments.length <= position) {
			if (defaultValue == null) {
				runtime.triggerError("missing argument #" + position);
				return "";
			} else {
				return defaultValue;
			}
		}
		return TypeConversionUtil.convertToString(arguments[position]);
	}
	
	/**
	 * Returns argument #position as an integer. If the number of arguments is less than
	 * that, and a defaultValue is specified, it is returned. Otherwise, this method triggers
	 * an error and 0 is returned.
	 * 
	 * @param runtime the runtime
	 * @param arguments the function arguments
	 * @param position the argument position
	 * @param defaultValue the default value, or null if none
	 * @return the value
	 */
	protected final int getIntParameter(PhpRuntime runtime, Object[] arguments, int position, Integer defaultValue) {
		if (arguments.length <= position) {
			if (defaultValue == null) {
				runtime.triggerError("missing argument #" + position);
				return 0;
			} else {
				return defaultValue;
			}
		}
		return TypeConversionUtil.convertToInt(arguments[position]);
	}
	
}
