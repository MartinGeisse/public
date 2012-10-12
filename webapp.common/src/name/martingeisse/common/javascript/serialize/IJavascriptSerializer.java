/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * This class is able to turn Java values into Javascript values.
 * @param <T> the type of values this serializer can handle
 */
public interface IJavascriptSerializer<T> {

	/**
	 * Serializes the specified value.
	 * @param value the value to serialize
	 * @param assembler the Javascript assembler
	 */
	public void serialize(T value, JavascriptAssembler assembler);
	
}
