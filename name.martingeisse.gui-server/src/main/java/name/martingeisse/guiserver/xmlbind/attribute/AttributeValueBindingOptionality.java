/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.attribute;

/**
 * Specifies whether an attribute is optional, and whether it has
 * a default value.
 */
public enum AttributeValueBindingOptionality {

	/**
	 * The attribute is mandatory. Any specified default value is ignored.
	 */
	MANDATORY,
	
	/**
	 * The attribute is optional without any default value. If the attribute
	 * is missing in the XML, null is used for its value.
	 */
	OPTIONAL,
	
	/**
	 * The attribute is optional, and a default value is used if it is not
	 * specified.
	 */
	OPTIONAL_WITH_DEFAULT;
	
}
