/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.variable.PhpValueArray;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

import org.apache.commons.lang3.NotImplementedException;

/**
 * This enum contains all binary operators.
 */
public enum BinaryOperator {

	/**
	 * Evaluates both sides but then always returns the left-hand value.
	 */
	LEFT("") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			return leftHandSide;
		}
		
	},
	
	/**
	 * Evaluates both sides but then always returns the right-hand value.
	 */
	RIGHT("") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			return rightHandSide;
		}
		
	},
	
	/**
	 * Only evaluates the left-hand side and returns its value.
	 */
	SHORTCUT_LEFT("") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operation.BinaryOperator#applyToExpressions(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpressions(Environment environment, Expression leftHandSide, Expression rightHandSide) {
			return leftHandSide.evaluate(environment);
		}
		
	},
	
	/**
	 * Only evaluates the right-hand side and returns its value.
	 */
	SHORTCUT_RIGHT("") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operation.BinaryOperator#applyToExpressions(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpressions(Environment environment, Expression leftHandSide, Expression rightHandSide) {
			return rightHandSide.evaluate(environment);
		}
		
	},
	
	/**
	 * Adds the values.
	 */
	ADD("+") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof PhpValueArray) || (rightHandSide instanceof PhpValueArray)) {
				// this is similar to array_merge, but differs in precedence of LHS/RHS
				// elements and in treatment of numeric indices.
				// see: http://stackoverflow.com/questions/2140090/operator-for-array-in-php
				PhpValueArray result = new PhpValueArray();
				addElements((PhpValueArray)rightHandSide, result);
				addElements((PhpValueArray)leftHandSide, result);
				return result;
			} else if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x + y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x + y;
			}
		}
		
		private void addElements(PhpValueArray from, PhpValueArray to) {
			throw new NotImplementedException(""); // TODO
			// where is PhpVariableArray / PhpValueArray used when it should be generalized?
//			for (Map.Entry<String, Variable> entry : from.getDirectEntryIterable()) {
//				to.getOrCreateVariable(entry.getKey()).setValue(entry.getValue());
//			}
		}

	},

	/**
	 * Subtracts the values.
	 */
	SUBTRACT("-") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x - y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x - y;
			}
		}

	},

	/**
	 * Concatenates the values.
	 */
	CONCATENATE(".") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			final String x = TypeConversionUtil.convertToString(leftHandSide);
			final String y = TypeConversionUtil.convertToString(rightHandSide);
			return x + y;
		}
		
	},
	
	/**
	 * Multiplies the values.
	 */
	MULTIPLY("*") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x * y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x * y;
			}
		}

	},

	/**
	 * Divides the values.
	 */
	DIVIDE("/") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x / y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x / y;
			}
		}

	},

	/**
	 * Computes the remainder of the values.
	 */
	REMAINDER("%") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x % y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x % y;
			}
		}

	},

	/**
	 * Computes x to the power of y.
	 */
	POWER("**") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			final double x = TypeConversionUtil.convertToDouble(leftHandSide);
			final double y = TypeConversionUtil.convertToDouble(rightHandSide);
			final double result = Math.pow(x, y);
			if ((int)result == result) {
				return (int)result;
			} else {
				return result;
			}
		}

	},

	/**
	 * Computes the logical-and of its operands, but only evaluates the right-hand operand
	 * if necessary (i.e. if the left-hand argument converts to true).
	 */
	LOGICAL_SHORTCUT_AND("&&") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.BinaryOperator#applyToExpressions(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpressions(Environment environment, Expression leftHandSide, Expression rightHandSide) {
			boolean leftValue = TypeConversionUtil.convertToBoolean(leftHandSide.evaluate(environment));
			if (!leftValue) {
				return false;
			}
			return TypeConversionUtil.convertToBoolean(rightHandSide.evaluate(environment));
		}

	},
	
	/**
	 * Computes the logical-or of its operands, but only evaluates the right-hand operand
	 * if necessary (i.e. if the left-hand argument converts to false).
	 */
	LOGICAL_SHORTCUT_OR("||") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.expression.operator.BinaryOperator#applyToExpressions(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression, name.martingeisse.phunky.runtime.code.expression.Expression)
		 */
		@Override
		public Object applyToExpressions(Environment environment, Expression leftHandSide, Expression rightHandSide) {
			boolean leftValue = TypeConversionUtil.convertToBoolean(leftHandSide.evaluate(environment));
			if (leftValue) {
				return true;
			}
			return TypeConversionUtil.convertToBoolean(rightHandSide.evaluate(environment));
		}
		
	},
	
	/**
	 * Computes the logical-xor of its operands.
	 */
	LOGICAL_XOR("xor") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			boolean leftValue = TypeConversionUtil.convertToBoolean(leftHandSide);
			boolean rightValue = TypeConversionUtil.convertToBoolean(rightHandSide);
			return (leftValue ^ rightValue);
		}
		
	},
	
	/**
	 * Computes the bitwise-and of its operands.
	 */
	BITWISE_AND("&") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			int leftValue = TypeConversionUtil.convertToInt(leftHandSide);
			int rightValue = TypeConversionUtil.convertToInt(rightHandSide);
			return (leftValue & rightValue);
		}

	},
	
	/**
	 * Computes the bitwise-or of its operands.
	 */
	BITWISE_OR("|") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			int leftValue = TypeConversionUtil.convertToInt(leftHandSide);
			int rightValue = TypeConversionUtil.convertToInt(rightHandSide);
			return (leftValue | rightValue);
		}
		
	},
	
	/**
	 * Computes the bitwise-xor of its operands.
	 */
	BITWISE_XOR("^") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			int leftValue = TypeConversionUtil.convertToInt(leftHandSide);
			int rightValue = TypeConversionUtil.convertToInt(rightHandSide);
			return (leftValue ^ rightValue);
		}
		
	},

	/**
	 * Computes the left operand bit-shifted to the left by as many bits as the right operand.
	 */
	SHIFT_LEFT("<<") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			int leftValue = TypeConversionUtil.convertToInt(leftHandSide);
			int rightValue = TypeConversionUtil.convertToInt(rightHandSide);
			return (leftValue << rightValue);
		}
		
	},
	
	/**
	 * Computes the left operand bit-shifted to the right by as many bits as the right operand.
	 */
	SHIFT_RIGHT(">>") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			int leftValue = TypeConversionUtil.convertToInt(leftHandSide);
			int rightValue = TypeConversionUtil.convertToInt(rightHandSide);
			return (leftValue >> rightValue);
		}
		
	},
	
	/**
	 * Compares the operands for equality.
	 */
	EQUALS("==") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof String) || (rightHandSide instanceof String)) {
				final String x = TypeConversionUtil.convertToString(leftHandSide);
				final String y = TypeConversionUtil.convertToString(rightHandSide);
				return x.equals(y);
			} else if ((leftHandSide instanceof PhpValueArray) || (rightHandSide instanceof PhpValueArray)) {
				throw new NotImplementedException("");
			} else if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x == y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x == y;
			}
		}

	},

	/**
	 * Compares the operands for inequality.
	 */
	NOT_EQUALS("!=") {

		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			final Boolean equal = (Boolean)EQUALS.applyToValues(leftHandSide, rightHandSide);
			return !equal;
		}

	},

	/**
	 * Compares the operands for identity.
	 */
	IDENTICAL("===") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			
			// handle null and identical Java objects
			if (leftHandSide == rightHandSide) {
				return true;
			} else if (leftHandSide == null || rightHandSide == null) {
				return false;
			}
			
			// compare values of the same Java class
			if (leftHandSide.getClass() == rightHandSide.getClass()) {
				if (leftHandSide instanceof String) {
					final String x = (String)leftHandSide;
					final String y = (String)rightHandSide;
					return x.equals(y);
				} else if (leftHandSide instanceof PhpValueArray) {
					throw new NotImplementedException("");
				} else if (leftHandSide instanceof Double) {
					final double x = (Double)leftHandSide;
					final double y = (Double)rightHandSide;
					return x == y;
				} else if (leftHandSide instanceof Float) {
					final float x = (Float)leftHandSide;
					final float y = (Float)rightHandSide;
					return x == y;
				} else if (leftHandSide instanceof Long) {
					final long x = (Long)leftHandSide;
					final long y = (Long)rightHandSide;
					return x == y;
				} else if (leftHandSide instanceof Integer) {
					final int x = (Integer)leftHandSide;
					final int y = (Integer)rightHandSide;
					return x == y;
				} else {
					return false;
				}
			}
			
			// in a few cases, values with a different Java class have the same PHP type
			if ((leftHandSide instanceof Integer) && (rightHandSide instanceof Long)) {
				final int x = (Integer)leftHandSide;
				final long y = (Long)rightHandSide;
				return (x == y);
			}
			if ((leftHandSide instanceof Long) && (rightHandSide instanceof Integer)) {
				final long x = (Long)leftHandSide;
				final int y = (Integer)rightHandSide;
				return (x == y);
			}
			if ((leftHandSide instanceof Float) && (rightHandSide instanceof Double)) {
				final float x = (Float)leftHandSide;
				final double y = (Double)rightHandSide;
				return (x == y);
			}
			if ((leftHandSide instanceof Double) && (rightHandSide instanceof Float)) {
				final double x = (Double)leftHandSide;
				final float y = (Float)rightHandSide;
				return (x == y);
			}
			
			return false;
		}
		
	},
	
	/**
	 * Compares the operands for non-identity.
	 */
	NOT_IDENTICAL("!==") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			final Boolean identical = (Boolean)IDENTICAL.applyToValues(leftHandSide, rightHandSide);
			return !identical;
		}
		
	},
	
	/**
	 * Compares the operands in magnitude, checking whether the left-hand operand is less than the right-hand operand.
	 */
	LESS_THAN("<") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x < y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x < y;
			}
		}
		
	},
	
	/**
	 * Compares the operands in magnitude, checking whether the left-hand operand is less than or equal to the right-hand operand.
	 */
	LESS_EQUAL("<=") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x <= y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x <= y;
			}
		}
		
	},
	
	/**
	 * Compares the operands in magnitude, checking whether the left-hand operand is greater than the right-hand operand.
	 */
	GREATER_THAN(">") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x > y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x > y;
			}
		}
		
	},
	
	/**
	 * Compares the operands in magnitude, checking whether the left-hand operand is greater than or equal to the right-hand operand.
	 */
	GREATER_EQUAL(">=") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.BinaryOperator#applyToValues(java.lang.Object, java.lang.Object)
		 */
		@Override
		public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
			if ((leftHandSide instanceof Double) || (rightHandSide instanceof Double) || (leftHandSide instanceof Float) || (rightHandSide instanceof Float)) {
				final double x = TypeConversionUtil.convertToDouble(leftHandSide);
				final double y = TypeConversionUtil.convertToDouble(rightHandSide);
				return x >= y;
			} else {
				final int x = TypeConversionUtil.convertToInt(leftHandSide);
				final int y = TypeConversionUtil.convertToInt(rightHandSide);
				return x >= y;
			}
		}
		
	};
	
	/**
	 * the symbol
	 */
	private final String symbol;

	/**
	 * Constructor.
	 * @param symbol the operator symbol
	 */
	private BinaryOperator(final String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Getter method for the symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * Applies this operator to the specified sub-expressions.
	 * 
	 * @param environment the environment
	 * @param leftHandSide the left-hand side expression
	 * @param rightHandSide the right-hand side expression
	 * @return the resulting value
	 */
	public Object applyToExpressions(final Environment environment, final Expression leftHandSide, final Expression rightHandSide) {
		return applyToValues(leftHandSide.evaluate(environment), rightHandSide.evaluate(environment));
	}

	/**
	 * Applies this operator to the specified values.
	 * 
	 * Note that not all operators support this; some require to be used on expressions.
	 * Assignment operators are an example for this.
	 * 
	 * Others can be applied to values, but with different semantics than applying them
	 * to the corresponding expressions. The shortcut boolean operators are an example for
	 * this; they do not evaluate the right-hand side expression in all cases.
	 * 
	 * @param leftHandSide the left-hand side value
	 * @param rightHandSide the right-hand side value
	 * @return the resulting value
	 * @throws UnsupportedOperationException if this operator cannot be applied to values
	 */
	public Object applyToValues(final Object leftHandSide, final Object rightHandSide) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("operator " + this + " cannot be applied to values");
	}

}
