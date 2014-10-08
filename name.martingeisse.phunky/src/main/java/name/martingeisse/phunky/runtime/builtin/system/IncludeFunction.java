/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.system;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;

/**
 * Implements the "include", "include_once", "require" and "require_once" functions.
 * 
 * If the "once" flag is set, then any requested file will first be checked against
 * the set.
 */
public final class IncludeFunction extends BuiltinFunctionWithValueParametersOnly {

	/**
	 * the once
	 */
	private final boolean once;

	/**
	 * the required
	 */
	private final boolean required;

	/**
	 * Constructor.
	 * @param once whether this function included each file only once, i.e. checks each file
	 * with the alreadyIncludedFiles set before actually including it
	 * @param required whether to trigger an error for files that cannot be loaded
	 */
	public IncludeFunction(final boolean once, final boolean required) {
		this.once = once;
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		runtime.getInterpreter().include(getStringParameter(runtime, arguments, 0, null), once, required);
		return null;
	}
	
}
