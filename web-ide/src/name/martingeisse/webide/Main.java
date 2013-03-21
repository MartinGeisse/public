/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide;

import static org.atmosphere.cpr.ApplicationConfig.FILTER_CLASS;
import static org.atmosphere.cpr.ApplicationConfig.SERVLET_CLASS;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.webide.application.IdeLauncher;
import name.martingeisse.webide.application.WebIdeApplication;
import name.martingeisse.webide.editor.codemirror.ot.HelloServer;
import name.martingeisse.webide.ssh.ShellFactory;

import org.apache.sshd.SshServer;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.wicket.protocol.http.WicketFilter;
import org.atmosphere.cpr.MeteorServlet;
import org.eclipse.jetty.rewrite.handler.ProxyRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.Rule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
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
		
		// create and configure a servlet context
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.getSessionHandler().getSessionManager().setMaxInactiveInterval(30 * 60);
		context.getSessionHandler().getSessionManager().setSessionIdPathParameterName(null);
		
		// add the Meteor servlet, also adding Wicket filter configuration
		final Servlet atmosphereServlet = new MeteorServlet();
		ServletHolder atmosphereServletHolder = new ServletHolder(atmosphereServlet);
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

		// create a multiplexing handler for Node.js servers, and start those servers
		new HelloServer().start();
		ProxyRule helloProxyRule = new ProxyRule();
		helloProxyRule.setPattern("/internal-api/ot/*");
		helloProxyRule.setProxyTo("http://localhost:8081");
		
		RewriteHandler rewriteHandler = new RewriteHandler();
		rewriteHandler.addRule(new DummyRule());
		rewriteHandler.addRule(helloProxyRule);
		HandlerCollection handlerCollection = new HandlerCollection();
		handlerCollection.addHandler(rewriteHandler);
		handlerCollection.addHandler(context);
		
		// start the server
		final Server server = new Server(8080);
		server.setHandler(handlerCollection);
		server.start();
		server.join();
		
	}

	private static class DummyRule extends Rule {

		/* (non-Javadoc)
		 * @see org.eclipse.jetty.rewrite.handler.Rule#matchAndApply(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
		 */
		@Override
		public String matchAndApply(String target, HttpServletRequest request, HttpServletResponse response) throws IOException {
			return null;
		}
		
	}
	
}
