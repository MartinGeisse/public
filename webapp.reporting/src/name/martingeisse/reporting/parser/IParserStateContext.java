/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * The parser passes an implementation of this interface when invoking
 * parser state methods. The parser states use this interface to control
 * the parsing process.
 */
public interface IParserStateContext {

	/**
	 * Pushes the specified state on the state stack, causing further SAX events
	 * to be routed to this new state. This method also invokes the startState()
	 * method of the state (after pushing) to allow initialization.
	 * @param state the state to push
	 */
	public void pushState(IParserState state);

	/**
	 * Pops the top-of-stack state from the state stack, causing further SAX
	 * events to be routed to the new top-of-stack. This method also invokes the
	 * consumeReturnData() method of the state (after pop'ing) with the specified
	 * return data.
	 * @param returnData the return data to pass to the new top-of-stack
	 */
	public void popState(Object returnData);
	
}
