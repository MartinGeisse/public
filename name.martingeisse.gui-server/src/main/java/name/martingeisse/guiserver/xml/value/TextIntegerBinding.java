/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.value;

/**
 * This parser recognizes integer values.
 */
public final class TextIntegerBinding implements TextValueBinding<Integer> {

	/**
	 * The shared instance of this class.
	 */
	public static final TextIntegerBinding INSTANCE = new TextIntegerBinding();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.value.TextValueBinding#parse(java.lang.String)
	 */
	@Override
	public Integer parse(String value) {
		return Integer.valueOf(value);
	}
	
}
