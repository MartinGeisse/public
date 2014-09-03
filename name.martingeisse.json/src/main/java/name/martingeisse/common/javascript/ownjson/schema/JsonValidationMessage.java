/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstNode;

/**
 * A message in a {@link JsonValidationReport}.
 */
public final class JsonValidationMessage {

	/**
	 * the source
	 */
	private final JsonAstNode source;

	/**
	 * the message
	 */
	private final String message;

	/**
	 * the fatal
	 */
	private final boolean fatal;

	/**
	 * Constructor.
	 * @param source the AST node from which this message originates
	 * @param message the message text
	 * @param fatal whether this message indicates a fatal error
	 */
	public JsonValidationMessage(final JsonAstNode source, final String message, final boolean fatal) {
		this.source = source;
		this.message = message;
		this.fatal = fatal;
	}

	/**
	 * Getter method for the source.
	 * @return the source
	 */
	public JsonAstNode getSource() {
		return source;
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Getter method for the fatal.
	 * @return the fatal
	 */
	public boolean isFatal() {
		return fatal;
	}

}
