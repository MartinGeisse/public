/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

/**
 * An assessment is the result of successfully assessing a
 * {@link Problem}. This usually implies that the problem has
 * matched a known problem pattern.
 * 
 * For now, the assessment just consists of a piece of text.
 * For the future, this is intended to be structured user-readable
 * content as well as possible {@link Action}s.
 */
public interface Assessment {

	/**
	 * Returns the assessment text.
	 * @return the assessment text
	 */
	public String getText();
	
}
