/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util.wicket;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Default implementation of {@link IPageBorderFactory}. This implementation
 * takes the class object for the border and invokes the constructor that
 * takes only the wicket id (no model).
 */
public class PageBorderFactory implements IPageBorderFactory {

	/**
	 * the borderClass
	 */
	private Class<? extends WebMarkupContainer> borderClass;

	/**
	 * Constructor.
	 */
	public PageBorderFactory() {
	}

	/**
	 * Constructor.
	 * @param borderClass the class object for the page border
	 */
	public PageBorderFactory(final Class<? extends WebMarkupContainer> borderClass) {
		this.borderClass = borderClass;
	}

	/**
	 * Getter method for the borderClass.
	 * @return the borderClass
	 */
	public Class<? extends WebMarkupContainer> getBorderClass() {
		return borderClass;
	}

	/**
	 * Setter method for the borderClass.
	 * @param borderClass the borderClass to set
	 */
	public void setBorderClass(final Class<? extends WebMarkupContainer> borderClass) {
		this.borderClass = borderClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.util.wicket.IPageBorderFactory#createPageBorder()
	 */
	@Override
	public WebMarkupContainer createPageBorder() {
		try {
			return borderClass.getConstructor(String.class).newInstance(Constants.PAGE_BORDER_ID);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
