/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.mobsc;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import name.martingeisse.common.database.EntityConnectionServletFilter;
import name.martingeisse.mobsc.application.Initializer;
import name.martingeisse.mobsc.application.ShowcaseWicketApplication;

import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * This class contains the main method.
 */
public class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		Initializer.initialize();
		launchWeb();
	}
	
	/**
	 * Launches the web user interface.
	 */
	private static void launchWeb() throws Exception {
		final EnumSet<DispatcherType> allDispatcherTypes = EnumSet.allOf(DispatcherType.class);
		
		// create and configure a servlet context
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.getSessionHandler().getSessionManager().setMaxInactiveInterval(30 * 60);
		
		// add the entity connection filter
		final Filter entityConnectionFilter = new EntityConnectionServletFilter();
		final FilterHolder entityConnectionFilterHolder = new FilterHolder(entityConnectionFilter);
		context.addFilter(entityConnectionFilterHolder, "/*", allDispatcherTypes);
		
		// add the Wicket filter
		final Filter wicketFilter = new WicketFilter();
		FilterHolder wicketFilterHolder = new FilterHolder(wicketFilter);
		wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		wicketFilterHolder.setInitParameter("applicationClassName", ShowcaseWicketApplication.class.getCanonicalName());
		context.addFilter(wicketFilterHolder, "/*", allDispatcherTypes);
		
		// a default servlet is needed, otherwise the filters cannot catch the request
		context.addServlet(DefaultServlet.class, "/*");

		final Server server = new Server(8080);
		server.setHandler(context);
		server.start();
		server.join();
		
	}
	
}
