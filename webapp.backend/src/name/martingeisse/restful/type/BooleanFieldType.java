/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;

/**
 * The boolean type. This type expects values to be either instances
 * of {@link Boolean}, or one of the strings "0" and "1".
 */
public class BooleanFieldType implements IFieldType {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#normalize(java.lang.Object)
	 */
	@Override
	public Boolean normalize(Object value) {
		if (value instanceof Boolean) {
			return (Boolean)value;
		} else if (value instanceof Integer) {
			int i = (Integer)value;
			if (i == 0) {
				return false;
			} else if (i == 1) {
				return true;
			}
		} else if (value instanceof String) {
			String s = (String)value;
			if (s.equals("0")) {
				return false;
			} else if (s.equals("1")) {
				return true;
			}
		}
		throw new InvalidValueForTypeException(value, this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToText(java.lang.Object)
	 */
	@Override
	public String convertToText(Object value) {
		return (normalize(value)) ? "1" : "0";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToJson(java.lang.Object)
	 */
	@Override
	public String convertToJson(Object value) {
		return (normalize(value)) ? "true" : "false";
	}
	
}
