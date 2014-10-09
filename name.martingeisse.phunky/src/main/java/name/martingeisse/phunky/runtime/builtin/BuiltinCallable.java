/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin;

import name.martingeisse.phunky.runtime.PhpCallable;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * Helper class to simplify the implementation of built-in functions
 * and special forms
 */
public abstract class BuiltinCallable implements PhpCallable {

	/**
	 * the name
	 */
	private String name;

	/**
	 * Constructor.
	 */
	public BuiltinCallable() {
		setName(getClass().getSimpleName());
	}
	
	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 * @return this for chaining
	 */
	public BuiltinCallable setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Returns argument #position without any type conversion. If the number of arguments is less than
	 * that, the defaultValue is returned. If so, and triggerErrorOnDefault is true, then this method
	 * also triggers an error.
	 * 
	 * @param runtime the runtime
	 * @param arguments the function arguments
	 * @param position the argument position
	 * @param defaultValue the default value
	 * @param triggerErrorOnDefault whether the absence of an argument is an error
	 * @return the value
	 */
	protected final Object getMixedParameter(PhpRuntime runtime, Object[] arguments, int position, Object defaultValue, boolean triggerErrorOnDefault) {
		if (arguments.length <= position) {
			if (triggerErrorOnDefault) {
				runtime.triggerError("missing argument #" + position);
			}
			return defaultValue;
		} else {
			return arguments[position];
		}
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
	protected final long getIntegerParameter(PhpRuntime runtime, Object[] arguments, int position, Long defaultValue) {
		if (arguments.length <= position) {
			if (defaultValue == null) {
				runtime.triggerError("missing argument #" + position);
				return 0;
			} else {
				return defaultValue;
			}
		}
		return TypeConversionUtil.convertToInteger(arguments[position]);
	}
	
	/**
	 * Returns argument #position as a boolean. If the number of arguments is less than
	 * that, and a defaultValue is specified, it is returned. Otherwise, this method triggers
	 * an error and false is returned.
	 * 
	 * @param runtime the runtime
	 * @param arguments the function arguments
	 * @param position the argument position
	 * @param defaultValue the default value, or null if none
	 * @return the value
	 */
	protected final boolean getBooleanParameter(PhpRuntime runtime, Object[] arguments, int position, Boolean defaultValue) {
		if (arguments.length <= position) {
			if (defaultValue == null) {
				runtime.triggerError("missing argument #" + position);
				return false;
			} else {
				return defaultValue;
			}
		}
		return TypeConversionUtil.convertToBoolean(arguments[position]);
	}

}
