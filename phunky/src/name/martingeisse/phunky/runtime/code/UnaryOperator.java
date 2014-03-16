/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code;

/**
 * This enum contains all unary operators.
 */
public enum UnaryOperator {

	/**
	 * Negates the value.
	 */
	NEGATE("-") {

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
	LOGICAL_NOT("!") {

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
	BITWISE_NOT("~") {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.phunky.runtime.code.UnaryOperator#apply(java.lang.Object)
		 */
		@Override
		public Object apply(final Object operand) {
			return null;
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
	private UnaryOperator(final String symbol) {
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
	 * Applies this operator to the specified operand.
	 * @param operand the operand
	 * @return the resulting value
	 */
	public abstract Object apply(Object operand);

}
