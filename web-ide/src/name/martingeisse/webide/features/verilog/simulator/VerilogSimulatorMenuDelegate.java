/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.simulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;
import name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

/**
 * This class implements a context menu item that allows the user
 * to run VVP files. It should be executed on the Verilog source
 * file to which the VVP file belongs (the VVP file itself is
 * intended to be hidden from the user).
 */
public class VerilogSimulatorMenuDelegate implements IContextMenuDelegate<Object, List<ResourcePath>, Object> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VerilogSimulatorMenuDelegate.class);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate#invoke(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void invoke(Object context, List<ResourcePath> anchor, Object parameter) {
		
		// check anchor
		logger.trace("VerilogSimulatorMenuDelegate invoked...");
		if (anchor.isEmpty()) {
			logger.trace("empty anchor (no file selected for simulation)");
			return;
		}
		ResourcePath inputFilePath = anchor.get(0);
		File inputFile = Workspace.map(inputFilePath);
		if (!inputFile.isFile()) {
			logger.trace("selected anchor is not a file");
			return;
		}
		logger.trace("selected file for simulation: " + inputFile.getPath());
		
		try {
			
			// build the command line
			CommandLine commandLine = new CommandLine(Configuration.getBashPath());
			commandLine.addArgument("--login");
			commandLine.addArgument("-c");
			commandLine.addArgument("vvp '" + inputFile.getName() + "'", false);
			logger.trace("command line: " + commandLine);

			// build I/O streams
			ByteArrayInputStream inputStream = null;
			ByteArrayOutputStream outputStream = null;
			OutputStream errorStream = System.err;
			ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);
			
			// run Icarus
			Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			executor.setWorkingDirectory(inputFile.getParentFile());
			executor.execute(commandLine);
			logger.trace("VVP finished");

		} catch (IOException e) {
			logger.error("exception during VVP simulation", e);
			return;
		}
		
	}

}
