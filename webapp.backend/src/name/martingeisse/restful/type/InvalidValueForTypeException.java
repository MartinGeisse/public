/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;

/**
 * This exception type is used when a value is passed as an argument
 * that does no conform to the rules of an {@link IFieldType}.
 * Just like field types are a more abstract way of representing
 * types than class objects, this exception is the abstract
 * couterpart of {@link ClassCastException}.
 */
public class InvalidValueForTypeException extends RuntimeException {

	/**
	 * Constructor.
	 * @param value the value which did not conform to the rules of the type
	 * @param type the type
	 */
	public InvalidValueForTypeException(Object value, IFieldType type) {
		super("Invalid value for type " + type + ": " + value.getClass());
	}

}
