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
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.build.BuilderResourceDelta;
import name.martingeisse.webide.resources.build.IBuilder;

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
	 * @see name.martingeisse.webide.resources.build.IBuilder#incrementalBuild(long, name.martingeisse.common.javascript.analyze.JsonAnalyzer, java.util.Set)
	 */
	@Override
	public void incrementalBuild(long workspaceId, JsonAnalyzer descriptorAnalyzer, Set<BuilderResourceDelta> deltas) {
		ResourcePath sourcePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("sourcePath").expectString()); 
		ResourcePath binaryPath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("binaryPath").expectString());
		for (final BuilderResourceDelta delta : deltas) {
			
			// TODO
			/*
			if (delta.isDeep()) {
			} else {
			}
			*/
			
			ResourcePath deltaPath = delta.getPath();
			if ("v".equals(deltaPath.getExtension()) && sourcePath.isPrefixOf(deltaPath)) {
				ResourcePath relativeDeltaPath = deltaPath.removeFirstSegments(sourcePath.getSegmentCount(), false);
				ResourcePath inputFilePath = sourcePath.concat(relativeDeltaPath, false);
				ResourcePath outputFilePath = binaryPath.concat(relativeDeltaPath, false).replaceExtension("vvp");
				
				compile(workspaceId, descriptorAnalyzer, inputFilePath, outputFilePath);
			}
			
		}
	}

	@SuppressWarnings("unused")
	private void cleanBuild(ResourcePath sourcePath, ResourcePath binaryPath) {
		// TODO
		// new DeleteResourceOperation(binaryPath).run();
		// cleanBuildStep(sourcePath, binaryPath);
	}

	@SuppressWarnings("unused")
	private void cleanBuildStep(ResourcePath sourcePath, ResourcePath binaryPath) {
		// TODO
	}

	private void compile(long workspaceId, final JsonAnalyzer descriptorAnalyzer, ResourcePath inputFilePath, ResourcePath outputFilePath) {
		try {
			
			// prepare
			ResourceHandle inputFile = new ResourceHandle(workspaceId, inputFilePath);
			ResourceHandle outputFile = new ResourceHandle(workspaceId, outputFilePath);
			
			// read the input file
			byte[] inputData = inputFile.readBinaryFile(false);
			if (inputData == null) {
				return;
			}
			
			// delete previous output file
			outputFile.delete();

			// build the command line
			CommandLine commandLine = new CommandLine(Configuration.getIverilogPath());
			commandLine.addArgument("-o");
			commandLine.addArgument(Configuration.getStdoutPath());
			commandLine.addArgument(Configuration.getStdinPath());

			// build I/O streams
			ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			OutputStream errorStream = System.err;
			ExecuteStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream, inputStream);
			
			// run Icarus
			Executor executor = new DefaultExecutor();
			executor.setStreamHandler(streamHandler);
			executor.execute(commandLine);

			// create the output file
			outputFile.writeFile(outputStream.toByteArray(), true, true);
			
		} catch (IOException e) {
			// TODO error message
			logger.error(e);
			return;
		}
		
	}

}
