/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

/**
 * This interface is implemented by an object that is able
 * to assess problems of type T.
 *
 * @param <T> the problem type which this assessor can handle
 */
public interface ProblemAssessor<T extends Problem> {

	/**
	 * Assesses a problem. This may or may not be successful.
	 * 
	 * @param problem the problem
	 * @return the assessment, or null if not successful
	 */
	public Assessment assess(T problem);
	
}
