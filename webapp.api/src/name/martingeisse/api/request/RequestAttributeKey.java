/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import org.apache.log4j.Logger;

/**
 * Instances of this class are used to select attributes of a
 * request cycle. Attributes are selected by the identity of the
 * attribute key.
 * 
 * The key may have a description for logging purposes, but the
 * description does not affect how attributes are selected: If
 * two keys have the same description, they still select different
 * attributes; they just look the same in logging output.
 * 
 * @param <T> the attribute type
 */
public class RequestAttributeKey<T> {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(RequestAttributeKey.class);
	
	/**
	 * the valueClass
	 */
	private final Class<T> valueClass;
	
	/**
	 * the description
	 */
	private final String description;
	
	/**
	 * Constructor.
	 * @param valueClass the class for attribute values
	 */
	public RequestAttributeKey(Class<T> valueClass) {
		this.valueClass = valueClass;
		this.description = null;
	}

	/**
	 * Constructor.
	 * @param valueClass the class for attribute values
	 * @param description the attribute description
	 */
	public RequestAttributeKey(Class<T> valueClass, String description) {
		this.valueClass = valueClass;
		this.description = description;
	}
	
	/**
	 * Getter method for the valueClass.
	 * @return the valueClass
	 */
	public Class<T> getValueClass() {
		return valueClass;
	}
	
	/**
	 * Getter method for the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Casts the specified value to the attribute type. This method logs extra
	 * output using the key description of the cast fails.
	 * 
	 * @param value the value to cast
	 * @return the cast value
	 */
	T cast(Object value) {
		try {
			return valueClass.cast(value);
		} catch (ClassCastException e) {
			logger.error("cannot cast value " + value + " to type " + valueClass + " for request attribute key " + description, e);
			throw e;
		}
	}
	
}
