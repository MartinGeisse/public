/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import name.martingeisse.webide.ssh.ShellFactory;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.ws.jetty.Jetty7WebSocketFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

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
		IdeLauncher.initialize();
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
		
		// add the Wicket filter
		// final Filter wicketFilter = new WicketFilter();
		final Filter wicketFilter = new Jetty7WebSocketFilter();
		FilterHolder wicketFilterHolder = new FilterHolder(wicketFilter);
		wicketFilterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		wicketFilterHolder.setInitParameter("applicationClassName", WebIdeApplication.class.getCanonicalName());
		context.addFilter(wicketFilterHolder, "/*", allDispatcherTypes);
		
		// a default servlet is needed, otherwise the filters cannot catch the request
		context.addServlet(DefaultServlet.class, "/*");

		final Server server = new Server(8080);
		server.setHandler(context);
		server.start();
		server.join();
		
	}
	
}
