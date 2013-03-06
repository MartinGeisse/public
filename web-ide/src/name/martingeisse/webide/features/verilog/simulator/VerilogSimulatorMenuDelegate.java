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

import name.martingeisse.common.util.TemporaryFolder;
import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.resources.ResourceHandle;
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
public class VerilogSimulatorMenuDelegate implements IContextMenuDelegate<Object, List<ResourceHandle>, Object> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VerilogSimulatorMenuDelegate.class);

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate#invoke(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void invoke(final Object context, final List<ResourceHandle> anchor, final Object parameter) {

		// check anchor
		logger.trace("VerilogSimulatorMenuDelegate invoked...");
		if (anchor.isEmpty()) {
			logger.trace("empty anchor (no file selected for simulation)");
			return;
		}
		final ResourceHandle inputFile = anchor.get(0);
		
		// make sure the anchor element is a file
		if (!inputFile.isFile()) {
			logger.trace("selected anchor is not a file");
			return;
		}
		logger.trace("selected file for simulation: " + inputFile.getPath());

		// catch exceptions to cleanly de-allocate the temporary folder
		TemporaryFolder temporaryFolder = null;
		try {

			// allocate a temporary folder for the output files
			temporaryFolder = new TemporaryFolder();
			logger.trace("allocation temporary folder: " + temporaryFolder.getInstanceFolder());

			// build the command line
			final CommandLine commandLine = new CommandLine(Configuration.getBashPath());
			commandLine.addArgument("--login");
			commandLine.addArgument("-c");
			commandLine.addArgument("vvp " + Configuration.getStdinPath(), false);
			logger.trace("command line: " + commandLine);

			// build I/O streams
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFile.readBinaryFile(true));
			final ByteArrayOutputStream outputStream = null;
			final OutputStream errorStream = System.err;
			final ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);

			// run Icarus
			final Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			executor.setWorkingDirectory(temporaryFolder.getInstanceFolder());
			executor.execute(commandLine);
			logger.trace("VVP finished");

			// create the output files
			final ResourceHandle outputFolder = inputFile.getParent();
			logger.trace("creating output files in folder: " + outputFolder);
			for (final File temporaryFile : temporaryFolder.getInstanceFolder().listFiles()) {
				if (temporaryFile.isFile()) {
					final ResourceHandle outputFile = outputFolder.getChild(temporaryFile.getName());
					logger.trace("creating output file " + outputFile + " from " + temporaryFile);
					outputFile.writeFile(temporaryFile, true, true);
					logger.trace("output file created");
				} else {
					logger.trace("skipping (not a file): " + temporaryFile);
				}
			}
			logger.trace("output files created");

		} catch (final IOException e) {
			logger.error("exception during VVP simulation", e);
			return;
		} finally {
			if (temporaryFolder != null) {
				temporaryFolder.dispose();
			}
		}

	}

}
