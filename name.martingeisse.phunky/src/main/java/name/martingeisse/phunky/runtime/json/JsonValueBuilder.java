/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.json;


/**
 * Base class for "value" builders, i.e. builders that are
 * used where JSON values are expected, as opposed to
 * list elements or object properties.
 * 
 * @param <C> the continuation type
 */
public abstract class JsonValueBuilder<C> extends AbstractJsonBuilder<C> {

	/**
	 * Constructor.
	 * @param builder the shared string builder
	 */
	JsonValueBuilder(final StringBuilder builder) {
		super(builder);
	}
	
	/**
	 * Builds the null literal. (This function should have been called null(),
	 * but Java doesn't allow that).
	 * 
	 * @return the continuation
	 */
	public final C nullLiteral() {
		getBuilder().append("null");
		return getContinuation();
	}

	/**
	 * Builds a boolean literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C bool(boolean value) {
		getBuilder().append(value);
		return getContinuation();
	}

	/**
	 * Builds a boolean literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C boolOrNull(Boolean value) {
		if (value == null) {
			nullLiteral();
		} else {
			getBuilder().append(value);
		}
		return getContinuation();
	}
	
	/**
	 * Builds an integer number literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C number(int value) {
		getBuilder().append(value);
		return getContinuation();
	}
	
	/**
	 * Builds an integer number literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C numberOrNull(Integer value) {
		if (value == null) {
			nullLiteral();
		} else {
			getBuilder().append(value);
		}
		return getContinuation();
	}
	
	/**
	 * Builds a long integer number literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C number(long value) {
		getBuilder().append(value);
		return getContinuation();
	}
	
	/**
	 * Builds a long integer number literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C numberOrNull(Long value) {
		if (value == null) {
			nullLiteral();
		} else {
			getBuilder().append(value);
		}
		return getContinuation();
	}
	
	/**
	 * Builds a float number literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C number(float value) {
		getBuilder().append(value);
		return getContinuation();
	}
	
	/**
	 * Builds a float number literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C numberOrNull(Float value) {
		if (value == null) {
			nullLiteral();
		} else {
			getBuilder().append(value);
		}
		return getContinuation();
	}
	
	/**
	 * Builds a double number literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C number(double value) {
		getBuilder().append(value);
		return getContinuation();
	}
	
	/**
	 * Builds a double number literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C numberOrNull(Double value) {
		if (value == null) {
			nullLiteral();
		} else {
			getBuilder().append(value);
		}
		return getContinuation();
	}
	
	/**
	 * Builds a string literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C string(String value) {
		Util.appendStringLiteral(getBuilder(), value);
		return getContinuation();
	}
	
	/**
	 * Builds a string literal or the null literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C stringOrNull(String value) {
		if (value == null) {
			nullLiteral();
		} else {
			Util.appendStringLiteral(getBuilder(), value);
		}
		return getContinuation();
	}
	
	/**
	 * Begins building a list.
	 * @return the list builder.
	 */
	public final JsonListBuilder<C> list() {
		getBuilder().append("[");
		return new JsonListBuilder<C>(getBuilder(), this);
	}
	
	/**
	 * Begins building an object.
	 * @return the object builder.
	 */
	public final JsonObjectBuilder<C> object() {
		getBuilder().append("{");
		return new JsonObjectBuilder<C>(getBuilder(), this);
	}
	
}
