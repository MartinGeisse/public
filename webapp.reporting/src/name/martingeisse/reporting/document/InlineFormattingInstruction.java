/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

/**
 * This enum specifies formatting instructions that can be applied to inline content.
 */
public enum InlineFormattingInstruction {
	
	/**
	 * the NONE
	 */
	NONE("span"),
	
	/**
	 * the BOLD
	 */
	BOLD("b"),
	
	/**
	 * the ITALIC
	 */
	ITALIC("i"),
	
	/**
	 * the UNDERLINE
	 */
	UNDERLINE("u"),
	
	/**
	 * the STRIKE_THROUGH
	 */
	STRIKE_THROUGH("s");

	/**
	 * the htmlElement
	 */
	private String htmlElement;

	/**
	 * Constructor.
	 */
	private InlineFormattingInstruction(String htmlElement) {
		this.htmlElement = htmlElement;
	}
	
	/**
	 * Getter method for the htmlElement.
	 * @return the htmlElement
	 */
	public String getHtmlElement() {
		return htmlElement;
	}
	
}
