/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide;

import static org.atmosphere.cpr.ApplicationConfig.FILTER_CLASS;
import static org.atmosphere.cpr.ApplicationConfig.SERVLET_CLASS;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import name.martingeisse.common.servlet.EnforceUtf8Filter;
import name.martingeisse.webide.application.IdeLauncher;
import name.martingeisse.webide.application.WebIdeApplication;
import name.martingeisse.webide.editor.codemirror.ot.CodeMirrorOtServer;
import name.martingeisse.webide.ssh.ShellFactory;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.wicket.protocol.http.WicketFilter;
import org.atmosphere.cpr.MeteorServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * The main class.
 */
public class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		IdeLauncher.initialize(true);
		launchSsh();
		launchWeb();
	}

	/**
	 * Launches the SSH user interface.
	 */
	private static void launchSsh() throws Exception {

		final SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(9090);
		final PasswordAuthenticator auth = new PasswordAuthenticator() {
			@Override
			public boolean authenticate(final String username, final String password, final ServerSession session) {
				return true;
			}
		};
		sshd.setPasswordAuthenticator(auth);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
		sshd.setShellFactory(new ShellFactory());
		sshd.start();

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
		context.getSessionHandler().getSessionManager().setSessionIdPathParameterName(null);
		
		// add UTF-8 enforcement filter
		final Filter utf8Filter = new EnforceUtf8Filter();
		FilterHolder utf8FilterHolder = new FilterHolder(utf8Filter);
		context.addFilter(utf8FilterHolder, "/*", allDispatcherTypes);

		// add the Meteor servlet, also adding Wicket filter configuration
		final Servlet atmosphereServlet = new MeteorServlet();
		final ServletHolder atmosphereServletHolder = new ServletHolder(atmosphereServlet);
		atmosphereServletHolder.setInitParameter(FILTER_CLASS, WicketFilter.class.getCanonicalName());
		atmosphereServletHolder.setInitParameter(SERVLET_CLASS, DefaultServlet.class.getCanonicalName());
		atmosphereServletHolder.setInitParameter("org.atmosphere.useWebSocket", "false");
		atmosphereServletHolder.setInitParameter("org.atmosphere.useNative", "true");
		atmosphereServletHolder.setInitParameter("applicationClassName", WebIdeApplication.class.getCanonicalName());
		atmosphereServletHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		atmosphereServletHolder.setInitParameter("org.atmosphere.cpr.sessionSupport", "true");
		//		atmosphereServletHolder.setInitParameter("org.atmosphere.websocket.WebSocketProtocol", "org.atmosphere.websocket.protocol.EchoProtocol");
		atmosphereServletHolder.setInitParameter("org.atmosphere.cpr.broadcastFilterClasses", "org.apache.wicket.atmosphere.TrackMessageSizeFilter");
		// atmosphereServletHolder.setAsyncSupported(true);
		context.addServlet(atmosphereServletHolder, "/*");

		// start the OT server
		new CodeMirrorOtServer().start();
		
		/*
		// start message interface for companion processes
		InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
		CompanionProcessMessageServer.start(8082);
		
		// TODO remove
		new NodejsCompanionProcess(new File("resource/companion-test/test1.js")) {
			@Override
			protected void onEventReceived(IpcEvent event) {
				String type = event.getType();
				@SuppressWarnings("unchecked")
				Map<String, Object> data = (Map<String, Object>)event.getData();
				int a = ((Number)data.get("a")).intValue(); 
				int b = ((Number)data.get("b")).intValue(); 
				int result = (type.equals("add") ? a + b : type.equals("mul") ? a * b : 0);
				IpcEvent resultEvent = new IpcEvent("result", null, result);
				sendEvent(resultEvent);
			};
		}.start();
		*/
		
		// start the main server
		final Server server = new Server(8080);
		server.setHandler(context);
		server.start();
		server.join();

	}

}
