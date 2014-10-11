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
import name.martingeisse.phunky.runtime.variable.PhpArray;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * The built-in "var_dump" function.
 */
public class VarDumpFunction extends BuiltinFunctionWithValueParametersOnly {

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
		printIndentation(runtime, indentation);
		if (value == null) {
			w.println("NULL");
		} else if (value instanceof Integer || value instanceof Long) {
			w.println("int(" + value + ")");
		} else if (value instanceof Float || value instanceof Double) {
			w.println("float(" + TypeConversionUtil.convertToString(value) + ")");
		} else if (value instanceof Boolean) {
			w.println("bool(" + value + ")");
		} else if (value instanceof String) {
			String s = (String)value;
			w.println("string(" + s.length() + ") \"" + s + "\"");
		} else if (value instanceof PhpValueArray) {
			PhpValueArray array = (PhpValueArray)value;
			w.println("array(" + array.size() + ") {");
			for (Entry<String, Object> entry : array.getKeyValueEntryIterable()) {
				printIndentation(runtime, indentation + 1);
				if (PhpArray.isNumericKey(entry.getKey())) {
					w.println("[" + entry.getKey() + "]=>");
				} else {
					w.println("[\"" + entry.getKey() + "\"]=>");
				}
				dump(runtime, entry.getValue(), indentation + 1);
			}
			printIndentation(runtime, indentation);
			w.println("}");
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
			runtime.getOutputWriter().print("  ");
		}
	}

}
