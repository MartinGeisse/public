/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.application.wicket;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.IExceptionSettings.UnexpectedExceptionDisplay;

/**
 * This class maps exceptions to error pages. It extends the default exception
 * mapper from Wicket to add specialized error pages for specific exceptions
 * (only when in debug mode). The goal is to simplify finding the problem
 * that causes the exception. To avoid over-simplification, all specialized
 * error pages contain the same information as Wicket's exception page
 * at the bottom (including highlighted markup and the full exception
 * stack trace).
 */
public class ExceptionMapper extends DefaultExceptionMapper {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ExceptionMapper.class);

	/* (non-Javadoc)
	 * @see org.apache.wicket.DefaultExceptionMapper#map(java.lang.Exception)
	 */
	@Override
	public IRequestHandler map(final Exception e) {
		ParameterUtil.ensureNotNull(e, "e");
		try {
			final IRequestHandler result = internalMap(e);
			if (result == null) {
				// super.map will log the exception
				return super.map(e);
			} else {
				// log the exception here
				logger.error("Unexpected error occurred", e);
				return result;
			}
		} catch (final RuntimeException e2) {
			logger.error("nested exception in ExceptionMapper: " + e2.getMessage(), e2);
			return new ErrorCodeRequestHandler(500);
		}
	}

	/**
	 * 
	 */
	private IRequestHandler internalMap(final Exception e) {

		// we cannot handle AJAX exceptions, so delegate to the parent class
		final Request request = RequestCycle.get().getRequest();
		if (request instanceof WebRequest) {
			if (((WebRequest)request).isAjax()) {
				return null;
			}
		}

		// our special handling is only for debug mode
		final UnexpectedExceptionDisplay unexpectedExceptionDisplay = Application.get().getExceptionSettings().getUnexpectedExceptionDisplay();
		if (!IExceptionSettings.SHOW_EXCEPTION_PAGE.equals(unexpectedExceptionDisplay)) {
			return null;
		}

		// extract the original cause since that is mostly relevant when finding a specialized error page
		Throwable originalCause = e;
		while (originalCause.getCause() != null) {
			originalCause = originalCause.getCause();
		}

		// handle some known exceptions
		/*
		if (originalCause instanceof UnknownEntityException) {
			return createPageRequestHandler(new UnknownEntityErrorPage(e, (UnknownEntityException)originalCause, extractCurrentPage()));
		}
		*/

		// delegate all others to the parent class
		return null;

	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private Page extractCurrentPage() {
		final RequestCycle requestCycle = RequestCycle.get();
		IRequestHandler handler = requestCycle.getActiveRequestHandler();
		if (handler == null) {
			handler = requestCycle.getRequestHandlerScheduledAfterCurrent();
		}
		if (handler instanceof IPageRequestHandler) {
			final IPageRequestHandler pageRequestHandler = (IPageRequestHandler)handler;
			return (Page)pageRequestHandler.getPage();
		}
		return null;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private RenderPageRequestHandler createPageRequestHandler(final IRequestablePage page) {
		return new RenderPageRequestHandler(new PageProvider(page), RenderPageRequestHandler.RedirectPolicy.NEVER_REDIRECT);
	}
	
}
