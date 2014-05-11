/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * An {@link IJavascriptSerializer} decorator that passes the
 * {@link IJavascriptSerializer#serialize(Object, name.martingeisse.common.javascript.JavascriptAssembler)}
 * method to the decorated serializer, but no callbacks.
 * 
 * @param <T> the type being serialized
 */
public class NoCallbackSerializerDecorator<T> extends AbstractJavascriptSerializer<T> {

	/**
	 * the decoratedSerializer
	 */
	private final IJavascriptSerializer<T> decoratedSerializer;
	
	/**
	 * Constructor.
	 * @param decoratedSerializer the decorated serializer
	 */
	public NoCallbackSerializerDecorator(IJavascriptSerializer<T> decoratedSerializer) {
		this.decoratedSerializer = decoratedSerializer;
	}
	
	/**
	 * Getter method for the decoratedSerializer.
	 * @return the decoratedSerializer
	 */
	public IJavascriptSerializer<T> getDecoratedSerializer() {
		return decoratedSerializer;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#serialize(java.lang.Object, name.martingeisse.common.javascript.JavascriptAssembler)
	 */
	@Override
	public void serialize(T value, JavascriptAssembler assembler) {
		decoratedSerializer.serialize(value, assembler);
	}
	
}
