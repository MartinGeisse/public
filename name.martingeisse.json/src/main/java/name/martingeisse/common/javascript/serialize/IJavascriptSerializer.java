/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * Implementations of this interface are able to turn Java values
 * into Javascript values.
 * 
 * This interface also provides onBefore/onAfter callbacks that
 * should be invoked by clients using the interface. Clients may
 * use either the single-element or iterable-based callbacks.
 * 
 * @param <T> the type of values this serializer can handle
 */
public interface IJavascriptSerializer<T> {

	/**
	 * Serializes the specified value.
	 * @param value the value to serialize
	 * @param assembler the Javascript assembler
	 */
	public void serialize(T value, JavascriptAssembler assembler);

	/**
	 * Prepares the specified value for serialization. This allows to
	 * perform implementation-specific work before
	 * {@link #serialize(Object, JavascriptAssembler)} is called.
	 * 
	 * @param value the value that will be serialized
	 */
	public void onBeforeSerialize(T value);
	
	/**
	 * Prepares the specified values for serialization. This allows to
	 * perform implementation-specific work before
	 * {@link #serialize(Object, JavascriptAssembler)} is called.
	 * 
	 * @param values the values that will be serialized
	 */
	public void onBeforeSerializeMultiple(Iterable<? extends T> values);
	
	/**
	 * Cleans the specified value after serialization. This allows to
	 * perform implementation-specific work after
	 * {@link #serialize(Object, JavascriptAssembler)} is called.
	 * 
	 * @param value the value that have been serialized
	 */
	public void onAfterSerialize(T value);
	
	/**
	 * Cleans the specified values after serialization. This allows to
	 * perform implementation-specific work after
	 * {@link #serialize(Object, JavascriptAssembler)} is called.
	 * 
	 * @param values the values that have been serialized
	 */
	public void onAfterSerializeMultiple(Iterable<? extends T> values);
	
}
