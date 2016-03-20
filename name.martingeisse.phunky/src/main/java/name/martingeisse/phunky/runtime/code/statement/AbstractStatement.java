/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.code.CodeLocation;

/**
 * Base class for all statements.
 */
public abstract class AbstractStatement implements Statement {

	/**
	 * the location
	 */
	private CodeLocation location;

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#getLocation()
	 */
	@Override
	public CodeLocation getLocation() {
		return location;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#setLocation(name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public void setLocation(CodeLocation location) {
		this.location = location;
	}
	
}
