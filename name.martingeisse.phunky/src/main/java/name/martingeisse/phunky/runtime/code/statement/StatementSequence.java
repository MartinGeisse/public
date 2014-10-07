/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.json.JsonListBuilder;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * A sequence of statements that acts as a single statement by
 * executing the sub-statements one after another.
 */
public final class StatementSequence extends AbstractStatement implements Iterable<Statement> {

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
	 * Constructor.
	 * @param statements the sub-statements
	 */
	public StatementSequence(final List<Statement> statements) {
		this.statements = statements.toArray(new Statement[statements.size()]);
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.Statement#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		for (Statement statement : statements) {
			statement.dump(dumper);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("sequence");
		JsonListBuilder<?> subsub = sub.property("statements").list();
		for (Statement statement : statements) {
			statement.toJson(subsub.element());
		}
		subsub.end();
		sub.end();
	}

}
