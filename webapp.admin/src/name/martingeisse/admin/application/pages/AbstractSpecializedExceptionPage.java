/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.pages;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.pages.ExceptionErrorPage;

/**
 * Base class for exception error pages that are specialized for
 * certain types of exceptions. These pages basically look like
 * Wicket's exception page but display additional information
 * above the stack trace.
 */
public abstract class AbstractSpecializedExceptionPage extends ExceptionErrorPage {

	/**
	 * Constructor.
	 * @param throwable the exception
	 * @param page the page where the exception occurred
	 */
	public AbstractSpecializedExceptionPage(Throwable throwable, Page page) {
		super(ParameterUtil.ensureNotNull(throwable, "throwable"), page);
	}

}
