/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonbuilder;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

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
	 * Builds a string literal.
	 * 
	 * @param value the value of the literal
	 * @return the continuation
	 */
	public final C string(String value) {
		JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
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
			JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), value);
		}
		return getContinuation();
	}
	
	/**
	 * Converts an object to a JSON value and puts it into the
	 * place used by this builder.
	 * 
	 * @param jsonBuildable the object to convert to JSON
	 * @return the continuation
	 */
	public final C convert(IJsonBuildable jsonBuildable) {
		jsonBuildable.toJson(this);
		return getContinuation();
	}
	
	/**
	 * Converts an object to a JSON value and puts it into the
	 * place used by this builder, or appends the null literal
	 * if the argument is null.
	 * 
	 * @param jsonBuildable the object to convert to JSON
	 * @return the continuation
	 */
	public final C convertOrNull(IJsonBuildable jsonBuildable) {
		if (jsonBuildable == null) {
			nullLiteral();
		} else {
			jsonBuildable.toJson(this);
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
	 * Builds a list of JSON-buildable objects.
	 * @param elements the list elements (will be converted to JSON)
	 * @return the continuation
	 */
	public final C list(IJsonBuildable... elements) {
		JsonListBuilder<C> listBuilder = list();
		for (IJsonBuildable element : elements) {
			listBuilder.element(element);
		}
		return listBuilder.end();
	}
	
	/**
	 * Builds a list of JSON-buildable objects.
	 * @param elements the list elements (will be converted to JSON)
	 * @return the continuation
	 */
	public final C list(Iterable<? extends IJsonBuildable> elements) {
		JsonListBuilder<C> listBuilder = list();
		for (IJsonBuildable element : elements) {
			listBuilder.element(element);
		}
		return listBuilder.end();
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
