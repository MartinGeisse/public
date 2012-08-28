/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;

import name.martingeisse.restful.handler.table.ITableDataProvider;

/**
 * Instances of this class specify the meaning of a data field
 * returned by various data sources, for example {@link ITableDataProvider}.
 * Field values are returned as objects, and Java code would
 * normally use an object's class to determine its meaning.
 * The field type further specializes the meaning, so for example
 * several fields can use the class {@link String}, yet have
 * different meaning without wrapping the string in a specialized object.
 * 
 * This interface provides some methods to deal with data values,
 * but in general code will have to treat various implementations
 * of the interface as special cases.
 */
public interface IFieldType {
	
	/**
	 * This method can be used to obtain a "normalized" representation
	 * of the specified value. Clients normally do not need to call this
	 * method since the other methods normalize their inputs
	 * automatically.
	 * 
	 * @param value the value to normalize
	 * @return the normalized value
	 */
	public Object normalize(Object value);
	
	/**
	 * Expects the specified value to be of this field type and converts it
	 * to a plain-text representation. No specific syntax is assumed for the
	 * text format, so this type tries to find a simple formal representation.
	 * 
	 * For example, {@link BooleanFieldType} converts (boolean) values to
	 * the strings "0" and "1" (as opposed to, for example, "true" and "false",
	 * since the latter would imply a specific syntax).
	 * 
	 * This method is used in CSV generation, since CSV does not specify
	 * any syntax beyond "values are stored as strings".
	 * 
	 * @param value the value to convert, which must be an object accepted by this type
	 * @return the plain-text representation
	 */
	public String convertToText(Object value);

	/**
	 * Expects the specified value to be of this field type and converts it
	 * to JSON.
	 * 
	 * @param value the value to convert, which must be an object accepted by this type
	 * @return the JSON representation
	 */
	public String convertToJson(Object value);
	
}
