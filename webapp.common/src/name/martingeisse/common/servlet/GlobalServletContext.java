/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This class allows access to the servlet context from anywhere.
 */
public class GlobalServletContext implements ServletContextListener, Serializable {

	/**
	 * the servletContext
	 */
	private static ServletContext servletContext;

	/**
	 * Getter method for the servletContext.
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		servletContext = event.getServletContext();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		servletContext = null;
	}

}
