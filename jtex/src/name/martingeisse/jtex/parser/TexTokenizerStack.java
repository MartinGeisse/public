/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.parser;

import java.util.Stack;

/**
 * Keeps a stack of {@link TexTokenizer} objects.
 */
public final class TexTokenizerStack {

	/**
	 * the stack
	 */
	private final Stack<TexTokenizer> stack = new Stack<>();
	
	/**
	 * the current
	 */
	private TexTokenizer current;
	
	/**
	 * Constructor.
	 * @param initialTokenizer the initial tokenizer
	 */
	public TexTokenizerStack(TexTokenizer initialTokenizer) {
		if (initialTokenizer == null) {
			throw new IllegalArgumentException("initialTokenizer argument is null");
		}
		this.current = initialTokenizer;
	}
	
	/**
	 * Getter method for the current.
	 * @return the current
	 */
	public TexTokenizer getCurrent() {
		return current;
	}
	
	/**
	 * Pushes a tokenizer on the stack and makes it the current tokenizer.
	 * @param tokenizer the tokenizer to push
	 */
	public void push(TexTokenizer tokenizer) {
		if (tokenizer == null) {
			throw new IllegalArgumentException("tokenizer argument is null");
		}
		stack.push(current);
		current = tokenizer;
	}

	/**
	 * Pops the current tokenizer off the stack and makes the previous tokenizer the current one.
	 */
	public void pop() {
		if (stack.isEmpty()) {
			throw new IllegalStateException("no previous tokenizer");
		}
		current = stack.pop();
	}

}
