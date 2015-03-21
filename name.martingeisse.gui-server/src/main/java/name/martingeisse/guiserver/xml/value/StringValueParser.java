/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.value;

/**
 * This parser allows arbitrary textual content and just
 * returns it as a string.
 */
public final class StringValueParser implements ValueParser<String> {

	/**
	 * The shared instance of this class.
	 */
	public static final StringValueParser INSTANCE = new StringValueParser();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.value.ValueParser#parse(java.lang.String)
	 */
	@Override
	public String parse(String value) {
		return value;
	}
	
}
