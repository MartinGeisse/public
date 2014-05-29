/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import name.martingeisse.database.EntityConnectionServletFilter;
import name.martingeisse.jetty.AntiJsessionidUrlFilter;
import name.martingeisse.jetty.GlobalServletContext;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.GzipFilter;
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
	 * @param configuration the configuration for the API
	 * @throws Exception on errors
	 */
	public static void launch(ApiConfiguration configuration) throws Exception {
		logger.debug("Launcher.launch(): begin");

		final EnumSet<DispatcherType> allDispatcherTypes = EnumSet.allOf(DispatcherType.class);

		// create and configure a servlet context
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS | ServletContextHandler.NO_SECURITY);
		context.setContextPath("/");

		// add the global servlet listener
		context.addEventListener(new GlobalServletContext());
		context.addFilter(AntiJsessionidUrlFilter.class, "/*", allDispatcherTypes);

		// add GZIP support
		context.addFilter(GzipFilter.class, "/*", allDispatcherTypes);

		// JDBC connection-closing filter
		context.addFilter(EntityConnectionServletFilter.class, "/*", allDispatcherTypes);

		// add the API Servlet
		RestfulServlet.configuration = configuration;
		context.addServlet(RestfulServlet.class, "/*");

		// configure SSL / HTTPS
		SslContextFactory sslContextFactory = new SslContextFactory("/Users/martin/.keystore");
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
