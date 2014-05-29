/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonbuilder;


/**
 * Base class for JSON builders.
 * 
 * @param <C> the continuation type
 */
public abstract class AbstractJsonBuilder<C> {

	/**
	 * the builder
	 */
	private final StringBuilder builder;
	
	/**
	 * Constructor.
	 * @param builder the shared string builder
	 */
	AbstractJsonBuilder(StringBuilder builder) {
		this.builder = builder;
	}
	
	/**
	 * Getter method for the builder.
	 * @return the builder
	 */
	final StringBuilder getBuilder() {
		return builder;
	}

	/**
	 * Getter method for the continuation.
	 * @return the continuation
	 */
	abstract C getContinuation();

	/**
	 * This method must be used whenever the caller knows that the current
	 * fragment-to-build has already been built. A common case is when
	 * using helper methods:
	 * 
	 * NestedJsonBuilder<> nestedBuilder = ...;
	 * helpBuild(nestedBuilder);
	 * AbstractJsonBuilder nextBuilder = nestedBuilder.skip(); // <-- nestedBuilder has already built its stuff!
	 * 
	 * @return the next builder to use
	 */
	public C done() {
		return getContinuation();
	}
	
}
