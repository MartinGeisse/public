/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

/**
 * This item contains plain text.
 */
public class TextInlineItem implements IInlineItem {

	/**
	 * the text
	 */
	private String text;

	/**
	 * Constructor.
	 */
	public TextInlineItem() {
	}

	/**
	 * Constructor.
	 * @param text the text content of this item
	 */
	public TextInlineItem(final String text) {
		this.text = text;
	}

	/**
	 * Getter method for the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter method for the text.
	 * @param text the text to set
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData()
	 */
	@Override
	public TextInlineItem bindToData() {
		return this;
	}
	
}
