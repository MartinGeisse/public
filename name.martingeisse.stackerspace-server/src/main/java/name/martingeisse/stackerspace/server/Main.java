/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import name.martingeisse.common.javascript.JavascriptAssembler;
import name.martingeisse.stackd.common.task.TaskSystem;
import name.martingeisse.stackd.server.network.StackdNettyPipelineFactory;
import name.martingeisse.stackerspace.common.StackerspaceCommonConstants;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import com.datastax.driver.core.Cluster;

/**
 * The main class for the game server.
 */
public class Main {

	/**
	 * The main method.
	 * 
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {
		
		// core initialization
		parseCommandLine(args);
		initializeBase();
		
		// game server
		new Thread() {
			@Override
			public void run() {
				StackerspaceServer stackerspaceServer = new StackerspaceServer();
				final ChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
				final ServerBootstrap bootstrap = new ServerBootstrap(factory);
				bootstrap.setPipelineFactory(new StackdNettyPipelineFactory<StackerspaceSession>(stackerspaceServer));
				bootstrap.setOption("child.tcpNoDelay", true);
				bootstrap.setOption("child.keepAlive", true);
				bootstrap.bind(new InetSocketAddress(StackerspaceCommonConstants.NETWORK_PORT));
			}
		}.start();
		
	}

	/**
	 * Parses the command line and sets static variables in this class.
	 */
	private static void parseCommandLine(final String[] args) throws IOException {
		if (args.length == 0) {
			Configuration.initializeFromClasspathConfig();
		} else if (args.length == 1) {
			Configuration.initializeFromConfigFile(new File(args[0]));
		} else {
			System.err.println("usage: Main [config.properties]");
			System.exit(1);
		}
	}

	/**
	 * Initializes URL handlers, time zones and the database.
	 * @throws IOException on I/O errors
	 */
	private static void initializeBase() throws IOException {

		// initialize time zone
		final DateTimeZone timeZone = DateTimeZone.UTC;
		JavascriptAssembler.defaultDateFormatter = DateTimeFormat.forPattern("YYYY-MM-dd").withZone(timeZone);
		JavascriptAssembler.defaultDateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").withZone(timeZone);

		// initialize task system
		TaskSystem.initialize();
		
		// initialize Cassandra database
		Databases.cassandraCluster = Cluster.builder().addContactPoint("localhost").build();
		Databases.world = Databases.cassandraCluster.connect("stackerspace");
		
	}

}
