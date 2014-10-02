/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import java.util.Iterator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * Base class for function calls, methods calls, and several special
 * syntactic forms. This base class manages the parameter expressions.
 * 
 * This object also acts as an {@link Iterable} for the parameter expressions.
 */
public abstract class AbstractCallExpression extends AbstractComputeExpression implements Iterable<Expression> {

	/**
	 * the parameters
	 */
	private final Expression[] parameters;

	/**
	 * Constructor.
	 * @param parameters the parameter expressions
	 */
	public AbstractCallExpression(final Expression[] parameters) {
		this.parameters = parameters.clone();
	}

	/**
	 * Getter method for the number of parameters.
	 * @return the number of parameters
	 */
	public int getParameterCount() {
		return parameters.length;
	}
	
	/**
	 * Getter method for a single parameter.
	 * @param index the index
	 * @return the parameter
	 */
	public Expression getParameter(int index) {
		return parameters[index];
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Expression> iterator() {
		return new ArrayIterator(parameters);
	}

	/**
	 * Evaluates the parameter expressions from left to right
	 * @param environment the environment
	 * @return the parameter values
	 */
	protected final Object[] evaluateParameters(Environment environment) {
		Object[] results = new Object[parameters.length];
		for (int i=0; i<parameters.length; i++) {
			results[i] = parameters[i].evaluate(environment);
		}
		return results;
	}

	/**
	 * Dumps the parameter expressions using the specified code dumper.
	 * @param dumper the dumper
	 */
	protected final void dumpParameters(CodeDumper dumper) {
		dumper.print('(');
		boolean first = true;
		for (Expression parameter : parameters) {
			if (first) {
				first = false;
			} else {
				dumper.print(", ");
			}
			parameter.dump(dumper);
		}
		dumper.print(')');
	}
	
}
