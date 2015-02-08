/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.attribute;

/**
 * This parser allows arbitrary attribute content and just
 * returns it as a string.
 */
public final class TextAttributeParser implements IAttributeParser {

	/**
	 * The shared instance of this class.
	 */
	public static final TextAttributeParser INSTANCE = new TextAttributeParser();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.attribute.IAttributeParser#parse(java.lang.String)
	 */
	@Override
	public Object parse(String value) {
		return value;
	}
	
}
