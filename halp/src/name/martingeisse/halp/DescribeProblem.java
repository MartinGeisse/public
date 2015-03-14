/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.halp;

import java.io.File;

/**
 * Represents the problem of describing something.
 * 
 * The thing to describe is represented by an arbitrary
 * Java object. However, the problem is NOT to describe
 * that object, but to describe the thing represented by
 * it. For example, the description of a {@link File} object
 * would not that it is a File object for some path, but
 * a description of the file represented by that object.
 *
 * @param <T> the type of thing to describe
 */
public class DescribeProblem<T> implements Problem {

	/**
	 * the thing
	 */
	private final T thing;

	/**
	 * Constructor.
	 * @param thing the thing to describe
	 */
	public DescribeProblem(T thing) {
		this.thing = thing;
	}

	/**
	 * Getter method for the thing.
	 * @return the thing
	 */
	public T getThing() {
		return thing;
	}
	
}
