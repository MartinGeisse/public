/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string;

import org.apache.commons.codec.digest.DigestUtils;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * The built-in "md5" function.
 */
public final class Md5Function extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.Callable#call(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.Object[])
	 */
	@Override
	public Object call(PhpRuntime runtime, Object[] arguments) {
		String s = getStringParameter(runtime, arguments, 0, null);
		boolean raw = getBooleanParameter(runtime, arguments, 1, false);
		byte[] md5 = DigestUtils.md5(TypeConversionUtil.mapStringDirectlyToBinary(s));
		
	}

}
