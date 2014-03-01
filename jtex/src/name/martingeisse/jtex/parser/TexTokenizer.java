/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.jtex.parser;

/**
 * Keeps the tokenizer state for an input file.
 */
public final class TexTokenizer {

	/**
	 * the state
	 */
	private int state;

	/**
	 * the index
	 */
	private int index;

	/**
	 * the start
	 */
	private int start;

	/**
	 * the loc
	 */
	private int loc;

	/**
	 * the limit (end-of-line, inclusive)
	 */
	private int limit;

	/**
	 * the name
	 */
	private int name;

	/**
	 * Constructor.
	 */
	public TexTokenizer() {
	}

	/**
	 * Copies all values from another tokenizer.
	 * @param other the other tokenizer to copy from
	 */
	public void copyFrom(TexTokenizer other) {
		this.state = other.state;
		this.index = other.index;
		this.start = other.start;
		this.loc = other.loc;
		this.limit = other.limit;
		this.name = other.name;
	}
	
	/**
	 * Getter method for the state.
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * Setter method for the state.
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Getter method for the index.
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Setter method for the index.
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Getter method for the start.
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Setter method for the start.
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * Getter method for the loc.
	 * @return the loc
	 */
	public int getLoc() {
		return loc;
	}

	/**
	 * Setter method for the loc.
	 * @param loc the loc to set
	 */
	public void setLoc(int loc) {
		this.loc = loc;
	}

	/**
	 * Getter method for the limit.
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Setter method for the limit.
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public int getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(int name) {
		this.name = name;
	}

}
