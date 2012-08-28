/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;

/**
 * The integer type. This type expects values to be either instances
 * of {@link Integer} or strings containing the decimal representation
 * of an integer value.
 */
public class IntegerFieldType implements IFieldType {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#normalize(java.lang.Object)
	 */
	@Override
	public Integer normalize(Object value) {
		if (value instanceof Integer) {
			return (Integer)value;
		} else if (value instanceof String) {
			try {
				return Integer.parseInt((String)value);
			} catch (NumberFormatException e) {
			}
		}
		throw new InvalidValueForTypeException(value, this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToText(java.lang.Object)
	 */
	@Override
	public String convertToText(Object value) {
		return Integer.toString(normalize(value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToJson(java.lang.Object)
	 */
	@Override
	public String convertToJson(Object value) {
		return Integer.toString(normalize(value));
	}

}
