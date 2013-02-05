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
import java.util.List;

import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

/**
 * This façade is used by the builder thread to invoke the Icarus Verilog compiler.
 */
public class VerilogCompilerFacade {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(VerilogCompilerFacade.class);
	
	/**
	 * This method is run by the builder thread.
	 */
	public static void performCompilation() {
		new RecursiveResourceOperation(new ResourcePath("/")) {
			@Override
			protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
				for (FetchResourceResult result : fetchResults) {
					if (result.getType() == ResourceType.FILE && "v".equals(result.getPath().getExtension())) {
						performCompilation(result);
					}
				}
			}
		}.run();
	}
	
	/**
	 * Compiles a single Verilog source file.
	 */
	private static void performCompilation(FetchResourceResult inputFile) {
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
