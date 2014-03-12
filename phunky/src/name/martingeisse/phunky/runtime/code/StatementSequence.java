/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

import java.util.Iterator;
import name.martingeisse.phunky.runtime.Environment;
import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * A sequence of statements that acts as a single statement by
 * executing the sub-statements one after another.
 */
public final class StatementSequence implements Statement, Iterable<Statement> {

	/**
	 * the statements
	 */
	private final Statement[] statements;

	/**
	 * Constructor.
	 * @param statements the sub-statements
	 */
	public StatementSequence(final Statement... statements) {
		this.statements = statements.clone();
	}

	/**
	 * Getter method for the number of sub-statements.
	 * @return the number of sub-statements
	 */
	public int getStatementCount() {
		return statements.length;
	}
	
	/**
	 * Getter method for a single statement.
	 * @param index the index
	 * @return the statement
	 */
	public Statement getStatement(int index) {
		return statements[index];
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Statement> iterator() {
		return new ArrayIterator(statements);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#execute(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public void execute(Environment environment) {
		for (Statement statement : statements) {
			statement.execute(environment);
		}
	}
	
}
