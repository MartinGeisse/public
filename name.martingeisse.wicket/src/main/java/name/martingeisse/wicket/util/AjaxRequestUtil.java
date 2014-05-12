/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import org.apache.log4j.Logger;
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
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AjaxRequestUtil.class);
	
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
	 * @return true for AJAX requests, false for other requests
	 */
	public static boolean isAjax() {
		return getAjaxRequestTarget() != null;
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
	
	/**
	 * Returns true if and only if the argument is not null.
	 * Also outputs an error message if it is null.
	 */
	private static boolean checkTarget(AjaxRequestTarget target) {
		if (target == null) {
			logger.error("This method cannot be used in non-AJAX requests", new Exception());
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Opens a Javascript alert box with a fixed message. Does nothing visible
	 * but logs an error in non-AJAX requests.
	 * @param message the message to show
	 */
	public static void alert(String message) {
		AjaxRequestTarget target = getAjaxRequestTarget();
		if (checkTarget(target)) {
			target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(message) + "');");
		}
	}

	/**
	 * Focuses a component via Javascript. Does nothing visible
	 * but logs an error in non-AJAX requests.
	 * @param component the component to focus
	 */
	public static void focus(Component component) {
		AjaxRequestTarget target = getAjaxRequestTarget();
		if (checkTarget(target)) {
			target.focusComponent(component);
		}
	}
	
}
