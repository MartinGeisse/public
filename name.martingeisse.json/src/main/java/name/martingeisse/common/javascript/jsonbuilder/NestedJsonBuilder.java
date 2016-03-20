/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonbuilder;

/**
 * Builder implementation for JSON values that are nested within other values.
 *
 * @param <P> the parent builder type
 */
public final class NestedJsonBuilder<P extends AbstractJsonBuilder<?>> extends JsonValueBuilder<P> {

	/**
	 * the parent
	 */
	private final P parent;
	
	/**
	 * Constructor.
	 * @param builder the shared string builder
	 * @param parent the parent builder
	 */
	NestedJsonBuilder(StringBuilder builder, P parent) {
		super(builder);
		this.parent = parent;
	}

    // override
	@Override
	P getContinuation() {
		return parent;
	}
	
}
