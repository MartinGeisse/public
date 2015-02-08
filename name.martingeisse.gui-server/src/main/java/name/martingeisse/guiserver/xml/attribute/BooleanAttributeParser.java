/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

/**
 * This parser allows the attribute values "true" and "false" and converts
 * them to their boolean equivalent. Any other value triggers an error.
 */
public final class BooleanAttributeParser implements IAttributeParser {

	/**
	 * The shared instance of this class.
	 */
	public static final BooleanAttributeParser INSTANCE = new BooleanAttributeParser();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.attribute.IAttributeParser#parse(java.lang.String)
	 */
	@Override
	public Object parse(String value) {
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return true;
		} else {
			throw new RuntimeException("invalid value for boolean attribute: " + value);
		}
	}
	
}
