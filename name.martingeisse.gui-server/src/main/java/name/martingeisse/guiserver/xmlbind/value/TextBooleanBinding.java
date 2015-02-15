/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.value;

/**
 * This parser allows the textual values "true" and "false" and converts
 * them to their boolean equivalent. Any other value triggers an error.
 */
public final class TextBooleanBinding implements TextValueBinding<Boolean> {

	/**
	 * The shared instance of this class.
	 */
	public static final TextBooleanBinding INSTANCE = new TextBooleanBinding();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.value.TextValueBinding#parse(java.lang.String)
	 */
	@Override
	public Boolean parse(String value) {
		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		} else {
			throw new RuntimeException("invalid boolean value: " + value);
		}
	}
	
}
