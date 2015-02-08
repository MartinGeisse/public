/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

/**
 * This interface is used by objects that parse attribute values
 */
public interface IAttributeParser {

	/**
	 * Parses the specified attribute value.
	 * 
	 * @param value the textual attribute value
	 * @return the parsed attribute value
	 */
	public Object parse(String value);
	
}
