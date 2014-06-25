/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * This enum contains all unary operators.
 */
public enum UnaryOperator {

	/**
	 * Negates the value.
	 */
	NEGATE("-", "") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object applyToValue(final Object operand) {
			if (operand instanceof Integer) {
				return -((Integer)operand);
			} else if (operand instanceof Long) {
				return -((Long)operand);
			} else if (operand instanceof Float) {
				return -((Float)operand);
			} else if (operand instanceof Double) {
				return -((Double)operand);
			} else {
				return -TypeConversionUtil.convertToDouble(operand);
			}
		}

	},

	/**
	 * Logically inverts the value.
	 */
	LOGICAL_NOT("!", "") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object applyToValue(final Object operand) {
			return !TypeConversionUtil.convertToBoolean(operand);
		}

	},

	/**
	 * Inverts every single bit of the value.
	 */
	BITWISE_NOT("~", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object applyToValue(final Object operand) {
			return ~TypeConversionUtil.convertToInt(operand);
		}
		
	},
	
	/**
	 * Increments a variable, then returns its old value.
	 */
	INCREMENT_AND_RETURN_OLD("", "++") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.UnaryOperator#applyToExpression(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpression(Environment environment, Expression expression) {
			final Variable leftHandVariable = leftHandSide.getOrCreateVariable(environment);
			if (leftHandVariable == null) {
				environment.getRuntime().triggerError("cannot assign to " + leftHandSide);
				return null;
			}
			final Object rightHandValue = rightHandSide.evaluate(environment);
			final Object resultValue = operator.applyToValues(leftHandVariable.getValue(), rightHandValue);
			leftHandVariable.setValue(resultValue);
			return resultValue;
			TODO
		}
		
	},
	
	/**
	 * Increments a variable, then returns its new value.
	 */
	INCREMENT_AND_RETURN_NEW("++", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.UnaryOperator#applyToExpression(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpression(Environment environment, Expression expression) {
			TODO
		}
		
	},
	
	/**
	 * Decrements a variable, then returns its old value.
	 */
	DECREMENT_AND_RETURN_OLD("", "--") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.UnaryOperator#applyToExpression(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpression(Environment environment, Expression expression) {
			TODO
		}
		
	},
	
	/**
	 * Decrements a variable, then returns its new value.
	 */
	DECREMENT_AND_RETURN_NEW("--", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.UnaryOperator#applyToExpression(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpression(Environment environment, Expression expression) {
			TODO
		}
		
	},
	
	/**
	 * Evaluates an expression, suppressing errors that occur in it.
	 */
	SUPPRESS_ERRORS("@", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object applyToValue(final Object operand) {
			return operand;
		}
		
	};
	
	/**
	 * the prefixSymbol
	 */
	private final String prefixSymbol;
	
	/**
	 * the postfixSymbol
	 */
	private final String postfixSymbol;

	/**
	 * Constructor.
	 * @param prefixSymbol the symbol to write before the affected expression
	 * @param postfixSymbol the symbol to write after the affected expression
	 */
	private UnaryOperator(String prefixSymbol, String postfixSymbol) {
		this.prefixSymbol = prefixSymbol;
		this.postfixSymbol = postfixSymbol;
	}

	/**
	 * Getter method for the prefixSymbol.
	 * @return the prefixSymbol
	 */
	public final String getPrefixSymbol() {
		return prefixSymbol;
	}
	
	/**
	 * Getter method for the postfixSymbol.
	 * @return the postfixSymbol
	 */
	public final String getPostfixSymbol() {
		return postfixSymbol;
	}
	
	/**
	 * Applies this operator to the specified expression.
	 * @param environment the environment
	 * @param expression the expression
	 * @return the resulting value
	 */
	public Object applyToExpression(final Environment environment, Expression expression) {
		return applyToValue(expression.evaluate(environment));
	}

	/**
	 * Applies this operator to the specified value.
	 * @param value the input value
	 * @return the resulting value
	 */
	public Object applyToValue(Object value) {
		throw new UnsupportedOperationException("operator " + this + " cannot be applied to a value");
	}
	
}
