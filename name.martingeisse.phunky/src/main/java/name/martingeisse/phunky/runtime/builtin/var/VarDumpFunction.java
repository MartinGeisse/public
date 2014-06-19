/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.var;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;

/**
 * The built-in "var_dump" function.
 */
public class VarDumpFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		for (Object argument : arguments) {
			dump(runtime, argument, 0);
		}
		return null;
	}
	
	/**
	 * 
	 */
	private void dump(PhpRuntime runtime, Object value, int indentation) {
		printIndentation(runtime, indentation);
		if (value == null) {
			runtime.getOutputWriter().print("NULL");
		} else if (value instanceof Integer) {
			runtime.getOutputWriter().print("int(" + value + ")");
		} else if (value instanceof Boolean) {
			runtime.getOutputWriter().print("bool(" + value + ")");
		} else if (value instanceof String) {
			String s = (String)value;
			runtime.getOutputWriter().print("string(" + s.length() + ") \"" + s + "\"");
		} else {
			// TODO: arrays, floats, objects, ...
			runtime.getOutputWriter().print("OTHER");
		}
	}
	
	/**
	 * 
	 */
	private void printIndentation(PhpRuntime runtime, int indentation) {
		for (int i=0; i<indentation; i++) {
			runtime.getOutputWriter().print("  ");
		}
	}

}
