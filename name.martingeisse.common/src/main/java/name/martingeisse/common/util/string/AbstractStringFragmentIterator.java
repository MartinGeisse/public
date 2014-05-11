/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract superclass for iterators that produce substrings of a given value string.
 */
public abstract class AbstractStringFragmentIterator implements Iterator<String> {

	/**
	 * the value
	 */
	private final String value;

	/**
	 * the startPosition
	 */
	private int startPosition;

	/**
	 * the endPosition
	 */
	private int endPosition;

	/**
	 * Constructor.
	 * @param value the value string from which substrings are iterated.
	 */
	public AbstractStringFragmentIterator(final String value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		this.value = value;
		this.startPosition = -1;
		this.endPosition = -1;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Getter method for the startPosition.
	 * @return the startPosition
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/**
	 * Setter method for the startPosition.
	 * @param startPosition the startPosition to set
	 */
	public void setStartPosition(final int startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * Getter method for the endPosition.
	 * @return the endPosition
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/**
	 * Setter method for the endPosition.
	 * @param endPosition the endPosition to set
	 */
	public void setEndPosition(final int endPosition) {
		this.endPosition = endPosition;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public abstract boolean hasNext();

	/**
	 * Moves to the next element by setting the start and end position.
	 * @return true if another element was found, false if not.
	 */
	protected abstract boolean moveToNext();

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public String next() {
		if (moveToNext()) {
			return value.substring(startPosition, endPosition);
		} else {
			throw new NoSuchElementException();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
