/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages.border;

import java.lang.reflect.Constructor;

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
			if (borderClass == null) {
				throw new IllegalStateException("borderClass is null");
			}
			Constructor<? extends WebMarkupContainer> constructor = borderClass.getConstructor(String.class);
			if (constructor == null) {
				throw new IllegalStateException("no appropriate constructor");
			}
			return constructor.newInstance(PageBorderUtil.PAGE_BORDER_ID);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
