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
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * This class starts the API system and is typically invoked by the
 * main method. Applications are free to ignore this launcher and
 * perform the initialization themselves; this is useful for example
 * if the same port is also used for a regular web GUI.
 */
public class ApiLauncher {
	
	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ApiLauncher.class);

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
		ApiConfiguration.setInstance(configuration);
		context.addServlet(RestfulApiServlet.class, "/*");

		// build SSL configuration
		SslContextFactory sslContextFactory = new SslContextFactory("/Users/martin/.keystore");
		sslContextFactory.setKeyStorePassword("changeit");
		
		// build the server object
		final Server server = new Server();
		
		// build HTTP(S) configurations
		HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration.setSecurePort(8443);
		HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);
		httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
		
		// build connection factories
		HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfiguration);
		HttpConnectionFactory httpsConnectionFactory = new HttpConnectionFactory(httpsConfiguration);
		SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, "http/1.1");
		
		// build connectors and add them to the server
		ServerConnector httpConnector = new ServerConnector(server, httpConnectionFactory);
		httpConnector.setPort(8080);
		server.addConnector(httpConnector);
		ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpsConnectionFactory);
		httpsConnector.setPort(8443);
		server.addConnector(httpsConnector);
		
		// start the server
		server.setHandler(context);
		try {
			server.start();
			server.join();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

		logger.debug("Launcher.launch(): end");
	}

}
