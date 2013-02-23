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
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * This class implements a context menu item that allows the user
 * to run VVP files. It should be executed on the Verilog source
 * file to which the VVP file belongs (the VVP file itself is
 * intended to be hidden from the user).
 */
public class VerilogSimulatorMenuDelegate implements IContextMenuDelegate<Object, List<FetchResourceResult>, Object> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VerilogSimulatorMenuDelegate.class);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate#invoke(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void invoke(Object context, List<FetchResourceResult> anchor, Object parameter) {
		
		// check anchor
		logger.trace("VerilogSimulatorMenuDelegate invoked...");
		if (anchor.isEmpty()) {
			logger.trace("empty anchor (no file selected for simulation)");
			return;
		}
		FetchResourceResult inputFile = anchor.get(0);
		if (inputFile.getType() != ResourceType.FILE) {
			logger.trace("selected anchor is a folder");
			return;
		}
		logger.trace("selected file for simulation: " + inputFile.getPath());
		
		TemporaryFolder temporaryFolder = null;
		try {
			
			// allocate a temporary folder for the output files
			temporaryFolder = new TemporaryFolder();
			logger.trace("allocation temporary folder: " + temporaryFolder.getInstanceFolder());
			
			// build the command line
			CommandLine commandLine = new CommandLine(Configuration.getBashPath());
			commandLine.addArgument("--login");
			commandLine.addArgument("-c");
			commandLine.addArgument("vvp " + Configuration.getStdinPath(), false);
			logger.trace("command line: " + commandLine);

			// build I/O streams
			ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFile.getContents());
			ByteArrayOutputStream outputStream = null;
			OutputStream errorStream = System.err;
			ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);
			
			// run Icarus
			Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			executor.setWorkingDirectory(temporaryFolder.getInstanceFolder());
			executor.execute(commandLine);
			logger.trace("VVP finished");

			// create the output files
			ResourcePath outputFolderPath = inputFile.getPath().removeLastSegment(false);
			logger.trace("creating output files in folder: " + outputFolderPath);
			for (File file : temporaryFolder.getInstanceFolder().listFiles()) {
				if (file.isFile()) {
					ResourcePath outputPath = outputFolderPath.appendSegment(file.getName(), false);
					logger.trace("creating output file " + outputPath + " from " + file);
					new DeleteResourceOperation(outputPath).run();
					new CreateFileOperation(outputPath, FileUtils.readFileToByteArray(file), true).run();
					logger.trace("output file created");
				} else {
					logger.trace("skipping (not a file): " + file);
				}
			}
			logger.trace("output files created");
			
		} catch (IOException e) {
			logger.error("exception during VVP simulation", e);
			return;
		} finally {
			if (temporaryFolder != null) {
				temporaryFolder.dispose();
			}
		}
		
	}

}
