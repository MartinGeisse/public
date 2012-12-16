/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Utility methods to deal with AJAX requests and specifically
 * to have common methods for AJAX/non-AJAX handling.
 */
public class AjaxRequestUtil {

	/**
	 * Prevent instantiation.
	 */
	private AjaxRequestUtil() {
	}
	
	/**
	 * Obtains the {@link AjaxRequestTarget} for the current request. Returns
	 * null for non-AJAX requests.
	 * @return the ART
	 */
	public static AjaxRequestTarget getAjaxRequestTarget() {
		RequestCycle requestCycle = RequestCycle.get();
		IRequestHandler scheduledHandler = requestCycle.getRequestHandlerScheduledAfterCurrent();
		if (scheduledHandler instanceof AjaxRequestTarget) {
			return (AjaxRequestTarget)scheduledHandler;
		}
		IRequestHandler currentHandler = requestCycle.getActiveRequestHandler();
		if (currentHandler instanceof AjaxRequestTarget) {
			return (AjaxRequestTarget)currentHandler;
		}
		return null;
	}
	
	/**
	 * Marks components for (re-)rendering. If the current request is an AJAX
	 * request, this adds the components to the ART. Otherwise this does nothing
	 * since the components will be rendered anyway.
	 * @param components the components
	 */
	public static void markForRender(Component... components) {
		AjaxRequestTarget art = getAjaxRequestTarget();
		if (art != null) {
			art.add(components);
		}
	}
	
}
