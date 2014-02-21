/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.jtex.parser;

import java.util.Stack;

/**
 * Keeps a stack of {@link TexParser} objects.
 */
public final class TexParserStack {

	/**
	 * the stack
	 */
	private final Stack<TexParser> stack = new Stack<>();
	
	/**
	 * the current
	 */
	private TexParser current;
	
	/**
	 * Constructor.
	 * @param initialParser the initial parser
	 */
	public TexParserStack(TexParser initialParser) {
		if (initialParser == null) {
			throw new IllegalArgumentException("initialParser argument is null");
		}
		this.current = initialParser;
	}
	
	/**
	 * Getter method for the current.
	 * @return the current
	 */
	public TexParser getCurrent() {
		return current;
	}
	
	/**
	 * Pushes a parser on the stack and makes it the current parser.
	 * @param parser the parser to push
	 */
	public void push(TexParser parser) {
		if (parser == null) {
			throw new IllegalArgumentException("parser argument is null");
		}
		stack.push(current);
		current = parser;
	}

	/**
	 * Pops the current parser off the stack and makes the previous parser the current one.
	 */
	public void pop() {
		if (stack.isEmpty()) {
			throw new IllegalStateException("no previous parser");
		}
		current = stack.pop();
	}

}
