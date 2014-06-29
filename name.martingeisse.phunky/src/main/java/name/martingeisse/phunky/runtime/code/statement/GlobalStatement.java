/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import org.apache.commons.lang3.NotImplementedException;

/**
 * TODO
 */
public final class GlobalStatement extends AbstractStatement {

	/**
	 * the variableName
	 */
	private final String variableName;

	/**
	 * Constructor.
	 * @param variableName the variable name to make global
	 */
	public GlobalStatement(final String variableName) {
		this.variableName = variableName;
	}

	/**
	 * Getter method for the variableName.
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(Environment environment) {
		throw new NotImplementedException("");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("global ");
		dumper.print(variableName);
		dumper.println(";");
	}
	
}
