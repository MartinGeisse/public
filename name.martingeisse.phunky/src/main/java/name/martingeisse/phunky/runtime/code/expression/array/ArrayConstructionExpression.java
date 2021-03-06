/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression.array;

import java.util.List;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.expression.AbstractComputeExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.json.JsonListBuilder;
import name.martingeisse.phunky.runtime.json.JsonObjectBuilder;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
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
		PhpVariableArray builder = new PhpVariableArray();
		for (Pair<Expression, Expression> element : elements) {
			boolean hasKey = (element.getLeft() != null);
			Object originalKey = (hasKey ? element.getLeft().evaluate(environment) : null);
			Object value = element.getRight().evaluate(environment);
			if (!hasKey) {
				builder.append().setValue(value);
			} else {
				String key = TypeConversionUtil.convertToArrayKey(originalKey);
				builder.getOrCreateVariable(key).setValue(value);
			}
		}
		return builder.toValueArray();
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

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.statement.Statement#toJson(name.martingeisse.phunky.runtime.json.JsonValueBuilder)
	 */
	@Override
	public void toJson(JsonValueBuilder<?> builder) {
		JsonObjectBuilder<?> sub = builder.object().property("type").string("arrayConstruction");
		JsonListBuilder<?> subsub = sub.property("elements").list();
		for (Pair<Expression, Expression> element : elements) {
			JsonObjectBuilder<?> subsubsub = subsub.element().object();
			if (element.getKey() != null) {
				element.getKey().toJson(subsubsub.property("key"));
			}
			element.getValue().toJson(subsubsub.property("value"));
			subsubsub.end();
		}
		subsub.end();
		sub.end();
	}

}
