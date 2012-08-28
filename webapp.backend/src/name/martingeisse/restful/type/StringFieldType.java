/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;

import org.json.simple.JSONValue;

/**
 * The string type. This type accepts any non-null values
 * and uses the toString() method to turn values to strings.
 */
public class StringFieldType implements IFieldType {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#normalize(java.lang.Object)
	 */
	@Override
	public String normalize(Object value) {
		return value.toString();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToText(java.lang.Object)
	 */
	@Override
	public String convertToText(Object value) {
		return normalize(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToJson(java.lang.Object)
	 */
	@Override
	public String convertToJson(Object value) {
		return JSONValue.toJSONString(normalize(value));
	}

}
