/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.nodejs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.webide.application.Configuration;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

/**
 * Base class for NodeJS server processes. Each subclass should
 * have an associated Javascript file (which is inherited and
 * can be overridden) that is passed to the NodeJS command-line
 * tool. The server is started by the start() method.
 */
public abstract class AbstractNodejsServer {

	/**
	 * Constructor.
	 */
	public AbstractNodejsServer() {
	}

	/**
	 * Starts this server.
	 */
	public void start() {
		
		// determine the path of the associated script
		URL url = getScriptUrl();
		if (!url.getProtocol().equals("file")) {
			throw new RuntimeException("unsupported protocol for associated script URL: " + url);
		}
		File scriptFile = new File(url.getPath());
		
		// build the command line
		CommandLine commandLine = new CommandLine(Configuration.getBashPath());
		commandLine.addArgument("--login");
		commandLine.addArgument("-c");
		commandLine.addArgument("node " + scriptFile.getName(), false);

		// build I/O streams
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		OutputStream errorStream = System.err;
		ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);
		
		// build an environment map that contains the path to the node_modules
		Map<String, String> environment = new HashMap<String, String>();
		environment.put("NODE_PATH", new File("lib/node_modules").getAbsolutePath());
		
		// run Node.js
		Executor executor = new DefaultExecutor();
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		executor.setStreamHandler(streamHandler);
		try {
			executor.setWorkingDirectory(scriptFile.getParentFile());
			executor.execute(commandLine, environment, new ExecuteResultHandler() {
				
				@Override
				public void onProcessFailed(ExecuteException e) {
				}
				
				@Override
				public void onProcessComplete(int exitValue) {
				}
				
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	protected URL getScriptUrl() {
		for (Class<?> c = getClass(); c != null; c = c.getSuperclass()) {
			URL url = c.getResource(c.getSimpleName() + ".js");
			if (url != null) {
				return url;
			}
		}
		throw new RuntimeException("no associated script for NodeJS server could be found");
	}
	
	
}
