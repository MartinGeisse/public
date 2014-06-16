/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.declaration;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.statement.Statement;

/**
 * TODO
 */
public abstract class OopTypeDefinition implements Statement {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(Environment environment) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
	}

}
