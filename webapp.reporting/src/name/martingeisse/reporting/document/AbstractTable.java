/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;


/**
 * Common handling for bound and unbound tables.
 */
public abstract class AbstractTable implements IBlockItem {

	/**
	 * the caption
	 */
	private String caption;

	/**
	 * Constructor.
	 */
	public AbstractTable() {
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
