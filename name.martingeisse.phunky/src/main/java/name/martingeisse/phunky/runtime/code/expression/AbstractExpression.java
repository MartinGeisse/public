/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * Base class for all expressions.
 */
public abstract class AbstractExpression implements Expression {

	/**
	 * the location
	 */
	private CodeLocation location;

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#getLocation()
	 */
	@Override
	public CodeLocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#setLocation(name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public void setLocation(CodeLocation location) {
		this.location = location;
	}
	
}
