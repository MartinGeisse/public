/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import java.util.Comparator;

/**
 * This comparator compares two {@link IGetScore} objects by their score. Normal
 * ordering is by ascending score, reversed ordering by descending score.
 * 
 * Shared instances are available for ascending and descending comparison.
 */
public class ScoreComparator implements Comparator<IGetScore> {

	/**
	 * The shared instance of this class used for ascending-score ordering.
	 */
	public static final ScoreComparator ASCENDING = new ScoreComparator(false);

	/**
	 * The shared instance of this class used for descending-score ordering.
	 */
	public static final ScoreComparator DESCENDING = new ScoreComparator(true);

	/**
	 * the reversed
	 */
	private final boolean reversed;

	/**
	 * Constructor.
	 * @param reversed whether this comparator is reversed
	 */
	public ScoreComparator(final boolean reversed) {
		this.reversed = reversed;
	}

	/**
	 * Getter method for the reversed.
	 * @return the reversed
	 */
	public boolean isReversed() {
		return reversed;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final IGetScore o1, final IGetScore o2) {
		final int normal = o1.getScore() - o2.getScore();
		return (reversed ? -normal : normal);
	}

}
