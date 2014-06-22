/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import org.apache.commons.lang3.NotImplementedException;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;

/**
 * The "break" statement.
 */
public final class BreakStatement implements Statement {

	/**
	 * Constructor.
	 */
	public BreakStatement() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(final Environment environment) {
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.println("break;");
	}
	
}
