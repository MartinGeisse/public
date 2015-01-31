/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.guiserver.application;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.security.SecurityUtil;
import name.martingeisse.database.EntityConnectionServletFilter;
import name.martingeisse.guiserver.application.security.GuiSecurityProvider;
import name.martingeisse.guiserver.application.wicket.GuiWicketApplication;
import name.martingeisse.jetty.FlushFilter;
import name.martingeisse.jetty.NoFlushOrCloseFilter;
import name.martingeisse.jetty.TimingFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.IncludableGzipFilter;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
/**
 * This class initializes various parts of the system through a single
 * static method. Before that method is called, however, the {@link ServerConfiguration}
 * must be initialized.
 */
public class Initializer {

	/**
	 * Content-Types to compress on-the-fly with GZIP.
	 */
	private static final String[] gzipContentTypes = { "text/html", "text/plain", "text/xml", "text/css", "text/javascript", "application/xhtml+xml", "application/javascript", "application/x-javascript", "image/svg+xml" };

	/**
	 * Prevent instantiation.
	 */
	private Initializer() {
	}

	/**
	 * Initializes the base system.
	 */
	public static void initializeBase() {
		// TODO set default locale
		DateTimeZone.setDefault(DateTimeZone.UTC);
		Constants.timeZone = DateTimeZone.forID("Europe/Berlin");
		Constants.internalDateFormatter = JavascriptAssembler.defaultDateFormatter = DateTimeFormat.forPattern("YYYY-MM-dd").withZone(Constants.timeZone);
		Constants.internalDateTimeFormatter = JavascriptAssembler.defaultDateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").withZone(Constants.timeZone);
		Constants.loggingDateFormatter = DateTimeFormat.forPattern("dd.MM.YYYY").withZone(Constants.timeZone);
		Constants.loggingDateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm:ss").withZone(Constants.timeZone);
		Constants.uiDateFormatter = Constants.loggingDateFormatter;
		Constants.uiDateTimeFormatter = Constants.loggingDateTimeFormatter;
		SecurityUtil.initialize(new GuiSecurityProvider());
	}

	/**
	 * Launches the web user interface.
	 */
	public static void initializeWeb() {
		final EnumSet<DispatcherType> allDispatcherTypes = EnumSet.allOf(DispatcherType.class);

		// create and configure a servlet context
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.getSessionHandler().getSessionManager().setMaxInactiveInterval(30 * 60);
		context.setInitParameter("org.eclipse.jetty.servlet.SessionIdPathParameterName", "none");

		// log timing info for all requests
		addGlobalFilter(context, new TimingFilter());

		// make sure all responses get flushed
		addGlobalFilter(context, new FlushFilter());

		// add GZIP filter
		final FilterHolder gzipFilterHolder = new FilterHolder(new IncludableGzipFilter());
		gzipFilterHolder.setInitParameter("mimeTypes", StringUtils.join(gzipContentTypes, ','));
		context.addFilter(gzipFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

		// add sidekicks and helper filters
		addGlobalFilter(context, new EntityConnectionServletFilter());

		// for Wicket, prevent flushing or closing the response
		addGlobalFilter(context, new NoFlushOrCloseFilter());

		// add the Wicket filter
		final Filter wicketFilter = new WicketFilter();
		final FilterHolder wicketFilterHolder = new FilterHolder(wicketFilter);
		wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		wicketFilterHolder.setInitParameter("applicationClassName", GuiWicketApplication.class.getCanonicalName());
		context.addFilter(wicketFilterHolder, "/*", allDispatcherTypes);

		// a default servlet is needed, otherwise the filters cannot catch the request
		context.addServlet(DefaultServlet.class, "/*");

		// start the server
		final Server server = new Server(8080);
		server.setHandler(context);
		try {
			server.start();
			server.join();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 */
	private static void addGlobalFilter(final ServletContextHandler context, final Filter filter) {
		final FilterHolder filterHolder = new FilterHolder(filter);
		context.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));
	}

}
