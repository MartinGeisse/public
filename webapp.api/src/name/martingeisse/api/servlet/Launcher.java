/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.common.database.EntityConnectionServletFilter;
import name.martingeisse.common.servlet.AntiJsessionidUrlFilter;
import name.martingeisse.common.servlet.GlobalServletContext;
import name.martingeisse.common.servlet.SideEffectsOriginRestrictionFilter;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * This class starts the admin system and is typically invoked by the
 * main method.
 */
public class Launcher {
	
	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(Launcher.class);

	/**
	 * Launches the server.
	 * 
	 * @param masterRequestHandler the master request handler
	 * @throws Exception on errors
	 */
	public static void launch(IRequestHandler masterRequestHandler) throws Exception {
		logger.debug("Launcher.launch(): begin");

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

		// add the API Servlet
		RestfulServlet.masterRequestHandler = masterRequestHandler;
		context.addServlet(RestfulServlet.class, "/*");

		// configure SSL / HTTPS
		SslContextFactory sslContextFactory = new SslContextFactory("/Users/geisse/.keystore");
		sslContextFactory.setKeyStorePassword("changeit");
		SslSocketConnector sslSocketConnector = new SslSocketConnector(sslContextFactory);
		sslSocketConnector.setPort(8889);

		final Server server = new Server(8888);
		server.addConnector(sslSocketConnector);
		server.setHandler(context);
		server.start();
		server.join();

		logger.debug("Launcher.launch(): end");
	}

}
