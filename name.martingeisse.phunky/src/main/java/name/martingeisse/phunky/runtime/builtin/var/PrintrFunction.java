/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.var;

import java.io.PrintWriter;
import java.util.Map.Entry;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;

/**
 * The built-in "print_r" function.
 */
public class PrintrFunction extends BuiltinFunctionWithValueParametersOnly {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.BuiltinFunctionWithValueParametersOnly#call(name.martingeisse.phunky.runtime.PhpRuntime, name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, CodeLocation location, Object[] arguments) {
		for (Object argument : arguments) {
			dump(runtime, argument, 0);
		}
		return null;
	}
	
	/**
	 * 
	 */
	private void dump(PhpRuntime runtime, Object value, int indentation) {
		PrintWriter w = runtime.getOutputWriter();
		if (value == null) {
			w.println("NULL");
		} else if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double || value instanceof Boolean || value instanceof String) {
			w.println(value);
		} else if (value instanceof PhpValueArray) {
			PhpValueArray array = (PhpValueArray)value;
			w.println("Array");
			w.println("(");
			for (Entry<String, Object> entry : array.getKeyValueEntryIterable()) {
				printIndentation(runtime, indentation + 1);
				w.print("[" + entry.getKey() + "] => ");
				dump(runtime, entry.getValue(), indentation + 1);
			}
			printIndentation(runtime, indentation);
			w.println(")");
		} else {
			// TODO: objects, ...
			w.println("OTHER");
		}
	}
	
	/**
	 * 
	 */
	private void printIndentation(PhpRuntime runtime, int indentation) {
		for (int i=0; i<indentation; i++) {
			runtime.getOutputWriter().print("    ");
		}
	}

}
