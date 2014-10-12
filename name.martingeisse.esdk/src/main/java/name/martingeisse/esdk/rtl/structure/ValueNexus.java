/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure;

/**
 * Common base class for ports and registers. An instance of this
 * class deal with values of type T and allows to attach meta-information
 * for synthesis.
 *
 * @param <T> the value type
 * @param <N> the concrete nexus subtype, used to implement method chaining
 */
public abstract class ValueNexus<T, N extends ValueNexus<T, N>> {

	/**
	 * the name
	 */
	private String name;
	
	/**
	 * the synthesisWidth
	 */
	private int synthesisWidth;

	/**
	 * @return this
	 */
	protected abstract N getThis();
	
	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 * @return this
	 */
	public N setName(String name) {
		this.name = name;
		return getThis();
	}

	/**
	 * Getter method for the synthesisWidth.
	 * @return the synthesisWidth
	 */
	public int getSynthesisWidth() {
		return synthesisWidth;
	}

	/**
	 * Setter method for the synthesisWidth.
	 * @param synthesisWidth the synthesisWidth to set
	 * @return this
	 */
	public N setSynthesisWidth(int synthesisWidth) {
		this.synthesisWidth = synthesisWidth;
		return getThis();
	}
	
}
