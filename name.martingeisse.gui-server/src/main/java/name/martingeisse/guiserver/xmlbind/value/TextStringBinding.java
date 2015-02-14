/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.value;

/**
 * This parser allows arbitrary textual content and just
 * returns it as a string.
 */
public final class TextStringBinding implements TextValueBinding<String> {

	/**
	 * The shared instance of this class.
	 */
	public static final TextStringBinding INSTANCE = new TextStringBinding();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.value.TextValueBinding#parse(java.lang.String)
	 */
	@Override
	public String parse(String value) {
		return value;
	}
	
}
