/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * Implementors are able to accept and return a read-only flag.
 */
public interface IReadOnlyAware {

	/**
	 * Getter method for the readOnly.
	 * @return the readOnly
	 */
	public boolean isReadOnly();

	/**
	 * Setter method for the readOnly.
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly);

}
