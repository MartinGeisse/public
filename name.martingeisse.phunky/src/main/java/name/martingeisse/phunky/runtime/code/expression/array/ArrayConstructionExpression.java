/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

import org.apache.commons.lang3.tuple.Pair;

/**
 * This expression constructs an array from keys and values.
 */
public class ArrayConstructionExpression extends AbstractComputeExpression {

	/**
	 * the elements
	 */
	private final List<Pair<Expression, Expression>> elements;

	/**
	 * Constructor.
	 * @param elements the elements
	 */
	public ArrayConstructionExpression(final List<Pair<Expression, Expression>> elements) {
		this.elements = elements;
	}
	
	/**
	 * Getter method for the elements.
	 * @return the elements
	 */
	public List<Pair<Expression, Expression>> getElements() {
		return elements;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		Map<String, Object> evaluatedElements = new HashMap<>();
		int autoIndex = 0;
		for (Pair<Expression, Expression> element : elements) {
			Object value = element.getRight().evaluate(environment);
			if (element.getLeft() == null) {
				evaluatedElements.put(Integer.toString(autoIndex), value);
				autoIndex++;
			} else {
				String key = TypeConversionUtil.convertToString(element.getLeft().evaluate(environment));
				evaluatedElements.put(key, value);
			}
		}
		return PhpValueArray.fromKeyValueEntries(evaluatedElements.entrySet());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("array(");
		boolean first = true;
		for (Pair<Expression, Expression> element : elements) {
			if (first) {
				first = false;
			} else {
				dumper.print(", ");
			}
			if (element.getLeft() != null) {
				element.getLeft().dump(dumper);
				dumper.print(" => ");
			}
			element.getRight().dump(dumper);
		}
		dumper.print(')');
	}

}
