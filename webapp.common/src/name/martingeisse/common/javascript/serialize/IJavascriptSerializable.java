/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import name.martingeisse.common.javascript.JavascriptAssembler;

/**
 * Implementations of this interface are able to turn themselves
 * into a Javascript value.
 */
public interface IJavascriptSerializable {

	/**
	 * Serializes this object.
	 * @param assembler the Javascript assembler
	 */
	public void serialize(JavascriptAssembler assembler);

	/**
	 * A serializer implementation that simply invokes the
	 * {@link IJavascriptSerializable#serialize(JavascriptAssembler)}
	 * method.
	 */
	public static final class Serializer implements IJavascriptSerializer<IJavascriptSerializable> {

		/**
		 * The shared instance of this class.
		 */
		public static final Serializer instance = new Serializer();
		
		/* (non-Javadoc)
		 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#serialize(java.lang.Object, name.martingeisse.common.javascript.JavascriptAssembler)
		 */
		@Override
		public void serialize(IJavascriptSerializable value, JavascriptAssembler assembler) {
			value.serialize(assembler);
		}
		
	}
	
}
