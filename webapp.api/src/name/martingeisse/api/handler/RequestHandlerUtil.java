/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

/**
 * Utility methods to deal with request handlers.
 */
public final class RequestHandlerUtil {

	/**
	 * Prevent instantiation.
	 */
	private RequestHandlerUtil() {
	}
	
	/**
	 * Returns a short (one-line) description of the specified handler.
	 * @param handler the handler
	 * @return the description
	 */
	public static String getShortDescription(IRequestHandler handler) {
		ParameterUtil.ensureNotNull(handler, "handler");
		if (handler instanceof ISelfDescribingRequestHandler) {
			String description = ((ISelfDescribingRequestHandler)handler).getShortDescription();
			return ReturnValueUtil.nullNotAllowed(description, "ISelfDescribingRequestHandler.getShortDescription() in " + handler.getClass());
		} else {
			return handler.getClass().getName();
		}
	}
	
}
