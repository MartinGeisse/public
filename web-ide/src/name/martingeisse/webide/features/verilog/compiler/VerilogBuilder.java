/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.build.BuilderResourceDelta;
import name.martingeisse.webide.resources.build.IBuilder;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

/**
 * This builder compiles Verilog source code (.v files) to
 * Icarus simulation models (.vvp files).
 */
public class VerilogBuilder implements IBuilder {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VerilogBuilder.class);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.build.IBuilder#incrementalBuild(name.martingeisse.common.javascript.analyze.JsonAnalyzer, java.util.Set)
	 */
	@Override
	public void incrementalBuild(final JsonAnalyzer descriptorAnalyzer, final Set<BuilderResourceDelta> deltas) {
		/* Since we compile each .v file to a .vvp sibling, and deep deltas affect all descendants alike,
		 * we can treat deep deltas as flat and then filter by extension.
		 */
		for (final BuilderResourceDelta delta : deltas) {
			final String extension = delta.getPath().getExtension();
			if (extension != null && extension.equals("v")) {
				compile(descriptorAnalyzer, delta.getPath());
			}
		}
	}

	private void compile(final JsonAnalyzer descriptorAnalyzer, final ResourcePath path) {
		// System.out.println("Compiling verilog file: " + path);

		final FetchSingleResourceOperation operation = new FetchSingleResourceOperation(path);
		operation.run();
		final FetchResourceResult inputFile = operation.getResult();
		if (inputFile.getType() != ResourceType.FILE) {
			return;
		}
		
		try {
			
			// preparation
			ResourcePath inputPath = inputFile.getPath();
			String inputName = inputPath.getLastSegment();
			String outputName = inputName.substring(0, inputName.length() - 2) + ".vvp";
			final ResourcePath outputPath = inputPath.removeLastSegment(false).appendSegment(outputName, false);
			new DeleteResourceOperation(outputPath).run();

			// build the command line
			CommandLine commandLine = new CommandLine(Configuration.getIverilogPath());
			commandLine.addArgument("-o");
			commandLine.addArgument(Configuration.getStdoutPath());
			commandLine.addArgument(Configuration.getStdinPath());

			// build I/O streams
			ByteArrayInputStream inputStream = new ByteArrayInputStream(inputFile.getContents());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			OutputStream errorStream = System.err;
			ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);
			
			// run Icarus
			Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			executor.execute(commandLine);

			// create the output file
			new CreateFileOperation(outputPath, outputStream.toByteArray(), true).run();
			
		} catch (IOException e) {
			// TODO error message
			logger.error(e);
			return;
		}
		
	}

}
