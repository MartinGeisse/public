/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.json;


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

    // override
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
		Util.appendStringLiteral(getBuilder(), name);
		getBuilder().append(": ");
		return new NestedJsonBuilder<JsonObjectBuilder<C>>(getBuilder(), this);
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
