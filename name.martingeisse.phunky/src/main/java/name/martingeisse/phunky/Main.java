/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import name.martingeisse.phunky.runtime.PhpRuntime;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * The main class.
 */
public final class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (currently ignored)
	 */
	public static void main(String[] args) {
		for (File file : new File("samples").listFiles()) {
			testSample(file);
		}
	}

	@SuppressWarnings("unused")
	private static void dumpSample(File file) {
		try {
			PhpRuntime runtime = new PhpRuntime();
			runtime.getInterpreter().dump(file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void testSample(File file) {
		System.out.print(file.getPath() + ": ");
		String phunkyOutput = getPhunkyOutput(file);
		byte[] phunkyOutputBytes = phunkyOutput.getBytes(StandardCharsets.UTF_8);
		byte[] phpOutput = getPhpOutput(file);
		if (phunkyOutputBytes == null || phpOutput == null) {
			return;
		}
		boolean equal = Arrays.equals(phunkyOutputBytes, phpOutput);
		if (equal) {
			System.out.println("OK");
			return;
		}
		System.out.println("WRONG");
		String phpOutputAsString;
		try {
			phpOutputAsString = new String(phpOutput, StandardCharsets.UTF_8);
		} catch (Exception e) {
			System.out.println("--> could not convert PHP output to string via UTF-8:");
			System.out.println("--> " + new String(phpOutput, StandardCharsets.ISO_8859_1));
			return;
		}
		System.out.println("--> Phunky: " + phunkyOutput);
		System.out.println("--> PHP: " + phpOutputAsString);
	}
	
	/**
	 * 
	 */
	private static String getPhunkyOutput(File file) {
		try {
			StringWriter outputLatch = new StringWriter();
			PhpRuntime runtime = new PhpRuntime();
			runtime.setOutputWriter(outputLatch);
			runtime.applyStandardDefinitions();
			runtime.getInterpreter().execute(file);
			runtime.flushOutputWriter();
			return outputLatch.toString();
		} catch (Exception e) {
			System.out.println("-> exception in Phunky: " + e);
			e.printStackTrace(System.out);
			return null;
		}
	}

	/**
	 * 
	 */
	private static byte[] getPhpOutput(File file) {
		try {
			
			// build PHP command line
			CommandLine commandLine = new CommandLine("php");
			commandLine.addArgument(file.getPath());
			
			// prepare I/O streams
			ByteArrayOutputStream outputLatch = new ByteArrayOutputStream();
			PumpStreamHandler streamHandler = new PumpStreamHandler(outputLatch, System.err, null);
			
			// execute PHP
			DefaultExecutor executor = new DefaultExecutor();
			executor.setWatchdog(new ExecuteWatchdog(60000));
			executor.setStreamHandler(streamHandler);
			executor.execute(commandLine);
			
			// capture the output
			return outputLatch.toByteArray();
			
		} catch (Exception e) {
			System.out.println("-> exception in PHP exec: " + e);
			return null;
		}
	}

}
