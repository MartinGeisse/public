/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.operator;

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
		public Object apply(final Object operand) {
			return null;
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
		public Object apply(final Object operand) {
			return null;
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
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Increments a variable, then returns its old value.
	 */
	INCREMENT_AND_RETURN_OLD("", "++") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Increments a variable, then returns its new value.
	 */
	INCREMENT_AND_RETURN_NEW("++", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Decrements a variable, then returns its old value.
	 */
	DECREMENT_AND_RETURN_OLD("", "--") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
		}
		
	},
	
	/**
	 * Decrements a variable, then returns its new value.
	 */
	DECREMENT_AND_RETURN_NEW("--", "") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
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
		public Object apply(final Object operand) {
			return null;
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
	 * Applies this operator to the specified operand.
	 * @param operand the operand
	 * @return the resulting value
	 */
	public abstract Object apply(Object operand);

}
