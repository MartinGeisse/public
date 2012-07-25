/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

/**
 * TODO: document me
 *
 */
public enum BinaryOperator {

	/**
	 * the ADD
	 */
	ADD("+"),
	
	/**
	 * the SUBTRACT
	 */
	SUBTRACT("-"),
	
	/**
	 * the MULTIPLY
	 */
	MULTIPLY("*"),
	
	/**
	 * the DIVIDE
	 */
	DIVIDE("/"),
	
	/**
	 * the EQUAL
	 */
	EQUAL("="),

	/**
	 * the NOT_EQUAL
	 */
	NOT_EQUAL("!="),
	
	/**
	 * the LESS_THAN
	 */
	LESS_THAN("<"),

	/**
	 * the LESS_EQUAL
	 */
	LESS_EQUAL("<="),
	
	/**
	 * the GREATER_THAN
	 */
	GREATER_THAN(">"),

	/**
	 * the GREATER_EQUAL
	 */
	GREATER_EQUAL(">=");
	
	/**
	 * the symbol
	 */
	private final String symbol;

	/**
	 * Constructor.
	 */
	private BinaryOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Getter method for the symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}
	
}
