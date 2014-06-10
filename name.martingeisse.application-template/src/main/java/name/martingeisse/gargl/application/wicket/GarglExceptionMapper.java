/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.gargl.application.wicket;

import name.martingeisse.common.security.authorization.PermissionDeniedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

/**
 * {@link IExceptionMapper} implementation.
 */
public final class GarglExceptionMapper extends DefaultExceptionMapper {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(GarglExceptionMapper.class);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IExceptionMapper#map(java.lang.Exception)
	 */
	@Override
	public IRequestHandler map(Exception e) {
		try {
			
			// we're not interested in wrapping exceptions, so get the original one
			Throwable originalException = ExceptionUtils.getRootCause(e);
			
			// disable caching for error pages
			final Response response = RequestCycle.get().getResponse();
			if (response instanceof WebResponse) {
				((WebResponse)response).disableCaching();
			}
			
			// check for specific exceptions
			if (originalException instanceof PermissionDeniedException) {
				// return new RenderPageRequestHandler(new PageProvider(PermissionDeniedPage.class), RedirectPolicy.ALWAYS_REDIRECT);
			}
			
		} catch (final RuntimeException e2) {
			// log this exception, then use default mapping for the original exception
			logger.error("nested exception in GarglExceptionMapper", e2);
		}

		// fall back to default beahvior
		return super.map(e);
		
	}
	
}
