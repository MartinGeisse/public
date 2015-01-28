/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.console;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class handles the client-side console and command execution.
 */
public class Console {
	
	/**
	 * the commandHandler
	 */
	private IConsoleCommandHandler commandHandler;

	/**
	 * the inputLine
	 */
	private final StringBuilder inputLine;
	
	/**
	 * the outputLines
	 */
	private final ConcurrentLinkedQueue<String> outputLines;

	/**
	 * Constructor.
	 */
	public Console() {
		setCommandHandler(null);
		inputLine = new StringBuilder();
		outputLines = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Setter method for the commandHandler.
	 * @param commandHandler the commandHandler to set
	 */
	public void setCommandHandler(IConsoleCommandHandler commandHandler) {
		this.commandHandler = (commandHandler == null ? new NullConsoleCommandHandler() : commandHandler);
	}
	
	/**
	 * Getter method for the commandHandler.
	 * @return the commandHandler
	 */
	public IConsoleCommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	/**
	 * Returns the currently visible output lines.
	 * @return the output lines
	 */
	public ConcurrentLinkedQueue<String> getCurrentOutputLines() {
		return outputLines;
	}

	/**
	 * Returns the current contents of the input line
	 * @return the input line
	 */
	public CharSequence getCurrentInputLine() {
		return inputLine;
	}

	/**
	 * Consumes a whole string of input characters at once.
	 * @param s the string of input characters to consume
	 */
	public final void consumeInputString(final String s) {
		for (int i = 0; i < s.length(); i++) {
			consumeInputCharacter(s.charAt(i));
		}
	}

	/**
	 * Consumes a single input character.
	 * @param c the input character to consume
	 */
	public final void consumeInputCharacter(final char c) {
		if (c == '\n' || c == '\r') {
			executeCommandLine(inputLine.toString());
			inputLine.setLength(0);
		} else if (c == '\b' || c == 127) {
			if (inputLine.length() > 0) {
				inputLine.setLength(inputLine.length() - 1);
			}
		} else {
			inputLine.append(c);
		}
	}

	/**
	 * Causes the console to execute a command line programmatically.
	 * @param commandLine the command line to execute
	 */
	public final void executeCommandLine(String commandLine) {
		
		// remove special characters
		{
			StringBuilder builder = new StringBuilder();
			for (int i=0; i<commandLine.length(); i++) {
				char c = commandLine.charAt(i);
				if (c >= 32) {
					builder.append(c);
				}
			}
			commandLine = builder.toString();
		}
		
		// split into segments
		String[] segments = StringUtils.split(commandLine);
		if (segments.length == 0) {
			return;
		}
		
		// invoke the command handler
		String command = segments[0];
		String[] args = ArrayUtils.subarray(segments, 1, segments.length);
		commandHandler.handleCommand(this, command, args);
		
	}

	/**
	 * Prints the specified string on the console.
	 * @param s the string to print
	 */
	public final void print(final String s) {
		for (final String line : StringUtils.split(s, '\n')) {
			outputLines.add(line);
		}
		while (outputLines.size() > 15) {
			outputLines.poll();
		}
	}

	/**
	 * Prints the specified string on the console, followed by a newline character
	 * @param line the line to print
	 */
	public final void println(final String line) {
		print(line);
		print("\n");
	}

	/**
	 * Prints a newline character on the console.
	 */
	public final void println() {
		print("\n");
	}

}
