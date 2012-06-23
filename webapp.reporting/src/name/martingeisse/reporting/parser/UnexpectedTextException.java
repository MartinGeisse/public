/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This exception type indicates unexpected character content (typically
 * when character content appears in elements that are expected to contain
 * only sub-elements or even expected to be empty).
 */
public class UnexpectedTextException extends ParserException {

	/**
	 * the text
	 */
	private final String text;

	/**
	 * the info
	 */
	private final String info;

	/**
	 * Constructor.
	 * @param text the text that was found but unexpected
	 * @param info an informational string about what was expected instead
	 */
	public UnexpectedTextException(final String text, final String info) {
		super(createMessage(text, info));
		this.text = text;
		this.info = info;
	}

	/**
	 * 
	 */
	private static String createMessage(final String text, final String info) {
		return "unexpected text content: \"" + text + "\" (" + info + ")";
	}

	/**
	 * Getter method for the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Getter method for the info.
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

}
