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
	 * @param expectedReturnType the return type expected by the parent state.
	 * Passing null indicates that the caller does not care about the return
	 * type. Pass Void.TYPE to indicate that no return value is expected.
	 * 
	 * TODO: the framework does not yet check that the sub-state actually
	 * passes the expected return type
	 */
	public void pushState(IParserState state, Class<?> expectedReturnType);

	/**
	 * Pops the top-of-stack state from the state stack, causing further SAX
	 * events to be routed to the new top-of-stack. This method also invokes the
	 * consumeReturnData() method of the state (after pop'ing) with the specified
	 * return data.
	 * @param returnData the return data to pass to the new top-of-stack
	 */
	public void popState(Object returnData);
	
}
