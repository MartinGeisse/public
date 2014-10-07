/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.json;

/**
 * This class is the starting point for assembling JSON.
 */
public final class JsonBuilder extends JsonValueBuilder<String> {

	/**
	 * Constructor.
	 */
	public JsonBuilder() {
		super(new StringBuilder());
	}

	/* (non-Javadoc)
	 * @see com.shopgate.frontend.experiment.AbstractJsonBuilder#getContinuation()
	 */
	@Override
	String getContinuation() {
		return getBuilder().toString();
	}
	
}
