/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.value;

/**
 * This parser recognizes integer values.
 */
public final class IntegerValueParser implements ValueParser<Integer> {

	/**
	 * The shared instance of this class.
	 */
	public static final IntegerValueParser INSTANCE = new IntegerValueParser();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.value.ValueParser#parse(java.lang.String)
	 */
	@Override
	public Integer parse(String value) {
		return Integer.valueOf(value);
	}
	
}
