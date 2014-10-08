/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import java.util.Iterator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpCallable;
import name.martingeisse.phunky.runtime.code.CodeDumper;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * Base class for function calls, methods calls, and several special
 * syntactic forms. This base class manages the argument expressions.
 * 
 * This object also acts as an {@link Iterable} for the argument expressions.
 */
public abstract class AbstractCallExpression extends AbstractComputeExpression implements Iterable<Expression> {

	/**
	 * the argumentExpressions
	 */
	private final Expression[] argumentExpressions;

	/**
	 * Constructor.
	 * @param argumentExpressions the argument expressions
	 */
	public AbstractCallExpression(final Expression[] argumentExpressions) {
		this.argumentExpressions = argumentExpressions.clone();
	}

	/**
	 * Getter method for the number of argument expressions.
	 * @return the number of argument expressions
	 */
	public int getArgumentExpressionCount() {
		return argumentExpressions.length;
	}
	
	/**
	 * Getter method for a single argument expression.
	 * @param index the index
	 * @return the argument expression
	 */
	public Expression getArgumentExpression(int index) {
		return argumentExpressions[index];
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Expression> iterator() {
		return new ArrayIterator(argumentExpressions);
	}

	/**
	 * Calls the specified callable, using the specified environment and the argument
	 * expressions from this object.
	 * 
	 * @param callable the callable to call
	 * @return the return value
	 */
	protected final Object call(PhpCallable callable, Environment callerEnvironment) {
		return callable.call(callerEnvironment, argumentExpressions);
	}
	
	/**
	 * Dumps the argument expressions using the specified code dumper.
	 * @param dumper the dumper
	 */
	protected final void dumpArgumentExpressions(CodeDumper dumper) {
		dumper.print('(');
		boolean first = true;
		for (Expression argumentExpression : argumentExpressions) {
			if (first) {
				first = false;
			} else {
				dumper.print(", ");
			}
			argumentExpression.dump(dumper);
		}
		dumper.print(')');
	}
	
}
