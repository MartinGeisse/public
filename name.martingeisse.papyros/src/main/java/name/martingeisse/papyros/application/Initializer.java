/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.papyros.application;

import java.util.EnumSet;
import java.util.Locale;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import name.martingeisse.api.handler.DefaultMasterHandler;
import name.martingeisse.api.handler.misc.NotFoundHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.servlet.ApiConfiguration;
import name.martingeisse.api.servlet.ApiServletFilter;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.common.security.SecurityUtil;
import name.martingeisse.database.EntityConnectionServletFilter;
import name.martingeisse.jetty.AntiJsessionidUrlFilter;
import name.martingeisse.jetty.FlushFilter;
import name.martingeisse.jetty.GlobalServletContext;
import name.martingeisse.jetty.NoFlushOrCloseFilter;
import name.martingeisse.papyros.api.ApiRootHandler;
import name.martingeisse.papyros.application.security.PapyrosSecurityProvider;
import name.martingeisse.papyros.application.wicket.PapyrosWicketApplication;
import name.martingeisse.sql.EntityConnectionManager;
import name.martingeisse.sql.MysqlDatabaseDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.IncludableGzipFilter;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import com.datastax.driver.core.Cluster;
import com.hazelcast.core.Hazelcast;
/**
 * This class initializes various parts of the system through a single
 * static method. Before that method is called, however, the {@link Configuration}
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
		SecurityUtil.initialize(new PapyrosSecurityProvider());
	}

	/**
	 * Initializes the SQL DB connection.
	 */
	public static void initializeSqlDatabase() {
		final MysqlDatabaseDescriptor database = new MysqlDatabaseDescriptor();
		database.setDisplayName("main database");
		database.setUrl(Configuration.databaseUrl.getMandatoryValue());
		database.setUsername(Configuration.databaseUsername.getMandatoryValue());
		database.setPassword(Configuration.databasePassword.getMandatoryValue());
		database.setDefaultTimeZone(Constants.timeZone);
		database.initialize();
		EntityConnectionManager.initializeDatabaseDescriptors(database);
		PapyrosSqldb.database = database;
	}

	/**
	 * Initializes the Cassandra DB connection.
	 */
	public static void initializeCassandraDatabase() {
		PapyrosCassandra.cluster = Cluster.builder().addContactPoint("localhost").build();
		PapyrosCassandra.session = PapyrosCassandra.cluster.connect("papyros");
	}

	/**
	 * Initializes the Hazelcast cluster connection.
	 */
	public static void initializeHazelcast() {
		PapyrosHazelcast.instance = Hazelcast.newHazelcastInstance();
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
		
		// make the servlet context globally available
		context.addEventListener(new GlobalServletContext());

		// prevent JSESSIONID from appearing
		context.addFilter(AntiJsessionidUrlFilter.class, "/*", allDispatcherTypes);
		
		// log timing info for all requests
		// addGlobalFilter(context, new TimingFilter());

		// make sure all responses get flushed
		addGlobalFilter(context, new FlushFilter());

		// add GZIP filter
		final FilterHolder gzipFilterHolder = new FilterHolder(new IncludableGzipFilter());
		gzipFilterHolder.setInitParameter("mimeTypes", StringUtils.join(gzipContentTypes, ','));
		context.addFilter(gzipFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

		// add sidekicks and helper filters
		addGlobalFilter(context, new EntityConnectionServletFilter());

		// add API filter
		{
			ApiRequestCycle.setUseSessions(false);
			DefaultMasterHandler masterHandler = new DefaultMasterHandler();
			masterHandler.setApplicationRequestHandler(new ApiRootHandler());
			masterHandler.getInterceptHandlers().put("/favicon.ico", new NotFoundHandler(false));
			ApiConfiguration configuration = new ApiConfiguration();
			configuration.setMasterRequestHandler(masterHandler);
			configuration.getLocalizationConfiguration().setGlobalFallback(Locale.US);
			ApiConfiguration.setInstance(configuration);
		}
		addGlobalFilter(context, new ApiServletFilter() {
			@Override
			protected boolean isApiRequest(ServletRequest request) {
				if (request instanceof HttpServletRequest) {
					HttpServletRequest httpRequest = (HttpServletRequest)request;
					return httpRequest.getHeader("host").startsWith("api.");
				} else {
					return false;
				}
			}
		});
		
		// for Wicket, prevent flushing or closing the response
		addGlobalFilter(context, new NoFlushOrCloseFilter());

		// add the Wicket filter
		final Filter wicketFilter = new WicketFilter();
		final FilterHolder wicketFilterHolder = new FilterHolder(wicketFilter);
		wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		wicketFilterHolder.setInitParameter("applicationClassName", PapyrosWicketApplication.class.getCanonicalName());
		// wicketFilterHolder.setInitParameter("configuration", "deployment");
		context.addFilter(wicketFilterHolder, "/*", allDispatcherTypes);

		// a default servlet is needed, otherwise the filters cannot catch the request
		context.addServlet(DefaultServlet.class, "/*");

		// configure SSL / HTTPS
		SslContextFactory sslContextFactory = new SslContextFactory("/Users/martin/.keystore");
		sslContextFactory.setKeyStorePassword("changeit");
		SslSocketConnector sslSocketConnector = new SslSocketConnector(sslContextFactory);
		sslSocketConnector.setPort(8081);
		
		// start the server
		final Server server = new Server(8080);
		server.addConnector(sslSocketConnector);
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
