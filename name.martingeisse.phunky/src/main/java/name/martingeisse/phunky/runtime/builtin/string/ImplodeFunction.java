/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.string;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;

import org.apache.commons.lang3.NotImplementedException;

/**
 * The built-in "implode" and "join" functions.
 */
public final class ImplodeFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		throw new NotImplementedException(""); // TODO
		
//		TODO variable array !?
//			
//		// extract parameters
//		if (arguments.length != 2) {
//			runtime.triggerError("implode() expects exactly 2 arguments, has " + arguments.length);
//			return "";
//		}
//		boolean firstIsArray = (arguments[0] instanceof PhpVariableArray);
//		boolean secondIsArray = (arguments[1] instanceof PhpVariableArray);
//		PhpVariableArray array;
//		String glue;
//		if (firstIsArray) {
//			if (secondIsArray) {
//				runtime.triggerError("Both arguments to implode() are arrays; cannot determine argument order");
//				return "";
//			} else {
//				array = (PhpVariableArray)arguments[0];
//				glue = TypeConversionUtil.convertToString(arguments[1]);
//			}
//		} else {
//			if (secondIsArray) {
//				glue = TypeConversionUtil.convertToString(arguments[0]);
//				array = (PhpVariableArray)arguments[1];
//			} else {
//				runtime.triggerError("Neither argument to implode() is an array");
//				return "";
//			}
//		}
//		
//		// implode the array
//		StringBuilder builder = new StringBuilder();
//		boolean first = true;
//		for (Object value : array.getDirectValueIterable()) {
//			if (first) {
//				first = false;
//			} else {
//				builder.append(glue);
//			}
//			builder.append(TypeConversionUtil.convertToString(value));
//		}
//		return builder.toString();

	}
	
}
