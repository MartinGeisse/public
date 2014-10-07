/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.json;

/**
 * Builder implementation for JSON lists.
 *
 * @param <C> the continuation type
 */
public final class JsonListBuilder<C> extends AbstractJsonBuilder<C> {

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
	JsonListBuilder(StringBuilder builder, AbstractJsonBuilder<C> parent) {
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
	 * Adds a list element.
	 * @return the element builder
	 */
	public final NestedJsonBuilder<JsonListBuilder<C>> element() {
		if (first) {
			first = false;
		} else {
			getBuilder().append(", ");
		}
		return new NestedJsonBuilder<JsonListBuilder<C>>(getBuilder(), this);
	}

	/**
	 * Ends the list.
	 * @return the continuation
	 */
	public final C end() {
		getBuilder().append("]");
		return getContinuation();
	}
	
}
