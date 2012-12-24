/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import java.io.PrintWriter;

/**
 * Instances of this interface are kept in a stack by the shell. They are
 * used like (recursive) sub-processes of a Unix shell, except that
 * multiple contexts cannot be run concurrently at a single level.
 */
public interface IShellContext {

	/**
	 * Attempts to auto-complete a command. May fail either because there is
	 * no known auto-completion
	 * @param commandLineBuilder the command line builder
	 * @return the status 
	 */
	public AutocompleteStatus autocomplete(StringBuilder commandLineBuilder);

	/**
	 * Provides suggestions for auto-completion by writing them to the
	 * specified {@link PrintWriter}.
	 * @param commandLineBuilder the command line builder to take the
	 * prefix from
	 * @param out the {@link PrintWriter} to write suggestions to
	 */
	public void suggest(StringBuilder commandLineBuilder, PrintWriter out);
	
	/**
	 * Executes a command.
	 * @param command the command to execute
	 * @param out the output print-writer
	 * @param err the error print-writer
	 * @return the status
	 */
	public ExecuteStatus execute(String command, PrintWriter out, PrintWriter err);
	
	/**
	 * Returns the current sub-context. Used after {@link ExecuteStatus#ENTER}
	 * has been returned to push the sub-context on the context stack.
	 * @return the sub-context
	 */
	public IShellContext getSubContext();

	/**
	 * Possible status results for the autocomplete method.
	 */
	public static enum AutocompleteStatus {
	
		/**
		 * Returned to indicate successful auto-completion.
		 */
		SUCCESS,
		
		/**
		 * Returned to indicate that the specified prefix is unknown.
		 */
		UNKNOWN,
		
		/**
		 * Returned to indicate that the specified prefix can be auto-completed
		 * to multiple commands.
		 */
		AMBIGUOUS;
		
	}

	/**
	 * Possible status results for the execute method.
	 */
	public static enum ExecuteStatus {
		
		/**
		 * Indicates that the command has been executed.
		 */
		OK,
		
		/**
		 * Indicates that the command has been executed and a new sub-context
		 * is available that shall be used until terminated, then the shell
		 * must return to this context.
		 */
		ENTER,
		
		/**
		 * Indicates that the command has been executed and has caused this context
		 * to terminate. The shell will remove the context in response to this status.
		 * Most contexts provide a command called "exit" that simply returns
		 * this status.
		 */
		TERMINATE;
		
		
		
	}
}
