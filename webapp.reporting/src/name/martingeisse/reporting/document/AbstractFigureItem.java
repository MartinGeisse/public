/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;


/**
 * Common handling for bound and unbound tables, charts, nested tables, and so on.
 */
public abstract class AbstractFigureItem implements IBlockItem {

	/**
	 * the caption
	 */
	private String caption;

	/**
	 * Constructor.
	 */
	public AbstractFigureItem() {
	}

	/**
	 * Getter method for the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Setter method for the caption.
	 * @param caption the caption to set
	 */
	public void setCaption(final String caption) {
		this.caption = caption;
	}

}
