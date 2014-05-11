/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonbuilder;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

/**
 * Builder implementation for JSON objects.
 *
 * @param <C> the continuation type
 */
public final class JsonObjectBuilder<C> extends AbstractJsonBuilder<C> {

	/**
	 * the parent
	 */
	private final AbstractJsonBuilder<C> parent;
	
	/**
	 * the first
	 */
	private boolean first = true;
	
	/**
	 * Constructor.
	 * @param builder the shared string builder
	 */
	JsonObjectBuilder(StringBuilder builder, AbstractJsonBuilder<C> parent) {
		super(builder);
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see com.shopgate.frontend.experiment.AbstractJsonBuilder#getContinuation()
	 */
	@Override
	C getContinuation() {
		return parent.getContinuation();
	}
	
	/**
	 * Adds an object property
	 * @param name the property name
	 * @return the property builder
	 */
	public final NestedJsonBuilder<JsonObjectBuilder<C>> property(String name) {
		if (first) {
			first = false;
		} else {
			getBuilder().append(", ");
		}
		JavascriptAssemblerUtil.appendStringLiteral(getBuilder(), name);
		getBuilder().append(": ");
		return new NestedJsonBuilder<JsonObjectBuilder<C>>(getBuilder(), this);
	}
	
	/**
	 * Adds an object property.
	 * @param name the property name
	 * @param value the property value (will be converted to JSON)
	 * @return this
	 */
	public final JsonObjectBuilder<C> property(String name, IJsonBuildable value) {
		return property(name).convertBuildable(value);
	}
	
	/**
	 * Ends the list.
	 * @return the continuation
	 */
	public final C end() {
		getBuilder().append("}");
		return getContinuation();
	}
	
}
