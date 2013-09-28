/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.common.database.EntityConnectionServletFilter;
import name.martingeisse.common.servlet.AntiJsessionidUrlFilter;
import name.martingeisse.common.servlet.GlobalServletContext;
import name.martingeisse.common.servlet.SideEffectsOriginRestrictionFilter;

import org.apache.log4j.Logger;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * This class starts the admin web server and is typically invoked by the
 * main method.
 */
public class WebServerLauncher {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(WebServerLauncher.class);

	/**
	 * Launches the web server.
	 * @throws Exception on errors
	 */
	public static void launch() throws Exception {
		logger.debug("WebServerLauncher.launch(): begin");

		final EnumSet<DispatcherType> allDispatcherTypes = EnumSet.allOf(DispatcherType.class);

		// create and configure a servlet context
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.getSessionHandler().getSessionManager().setMaxInactiveInterval(30 * 60);
		context.getSessionHandler().getSessionManager().setSessionIdPathParameterName("none");

		// add the global servlet listener
		context.addEventListener(new GlobalServletContext());
		context.addFilter(SideEffectsOriginRestrictionFilter.class, "/*", allDispatcherTypes);
		context.addFilter(AntiJsessionidUrlFilter.class, "/*", allDispatcherTypes);

		// the GZIP filter seems to cause problems on Jetty. The HTTP response either has
		// an incorrect or duplicate Content-Length header (my tools won't tell me...)
		//context.addFilter(GzipFilter.class, "/*", allDispatcherTypes);

		// JDBC connection-closing filter
		context.addFilter(EntityConnectionServletFilter.class, "/*", allDispatcherTypes);

		// add the Wicket filter
		final Filter wicketFilter = new WicketFilter();
		final FilterHolder wicketFilterHolder = new FilterHolder(wicketFilter);
		wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		wicketFilterHolder.setInitParameter("applicationClassName", AdminWicketApplication.class.getCanonicalName());
		context.addFilter(wicketFilterHolder, "/*", allDispatcherTypes);

		// a default servlet is needed, otherwise the filters cannot catch the request
		context.addServlet(DefaultServlet.class, "/*");

		// configure SSL / HTTPS
		//		SslContextFactory sslContextFactory = new SslContextFactory("/Users/martin/.keystore");
		//		sslContextFactory.setKeyStorePassword("changeit");
		//		SslSocketConnector sslSocketConnector = new SslSocketConnector(sslContextFactory);
		//		sslSocketConnector.setPort(8443);

		final Server server = new Server(8888);
		//		server.addConnector(sslSocketConnector);
		server.setHandler(context);
		server.start();
		server.join();

		logger.debug("WebServerLauncher.launch(): end");
	}

}
