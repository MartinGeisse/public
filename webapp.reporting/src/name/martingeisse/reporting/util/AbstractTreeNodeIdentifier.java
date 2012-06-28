/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a generic but abstract implementation of <code>SegmentedIdentifier</code>.
 * Concrete implementations can subclass this class and must only implement a few more methods.
 * 
 * @param <T> the concrete subtype
 */
public abstract class AbstractTreeNodeIdentifier<T extends ITreeNodeIdentifier<T>> implements ITreeNodeIdentifier<T> {
	
	/**
	 * The internal array of segments
	 */
	protected String[] segments;

	/**
	 * Creates a new instance from the specified segments. The
	 * <code>copy</code> argument specifies whether the array is copied
	 * or shared. The caller must ensure that this constructor is not
	 * used in a way that violates immutability of this class.
	 */
	protected AbstractTreeNodeIdentifier(final String[] segments, final boolean copy) {
		if (copy) {
			this.segments = new String[segments.length];
			System.arraycopy(segments, 0, this.segments, 0, segments.length);
		} else {
			this.segments = segments;
		}
	}

	// helper method
	private static String[] getSubrange(final String[] in, final int first, final int count) {
		final String[] subrange = new String[count];
		System.arraycopy(in, first, subrange, 0, count);
		return subrange;
	}

	/**
	 * Creates a new instance from a subrange of the specified segment
	 * array. Throws an <code>ArrayIndexOutOfBoundsException</code> if the
	 * specified subrange is not fully contained in the segments array.
	 */
	protected AbstractTreeNodeIdentifier(final String[] segments, final int first, final int count) {
		this(getSubrange(segments, first, count), false);
	}

	/**
	 * Create a new instance of the same type as this instance from the
	 * specified segments. The <code>copy</code> argument specifies whether
	 * the array is copied or shared. The caller must ensure that this
	 * constructor is not used in a way that violates immutability of this
	 * class.
	 */
	protected abstract T internalCreate(String[] segments, boolean copy);

	/**
	 * This method throws an <code>IllegalStateException</code> if
	 * invoked on the root node. Otherwise, does nothing.
	 */
	public void mayNotBeRoot() {
		if (segments.length == 0) {
			throw new IllegalStateException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSegmentCount() {
		return segments.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSegment(final int index) {
		return segments[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSegmentWeak(final int index) {
		if (index < 0) {
			return null;
		}
		if (index >= segments.length) {
			return null;
		}
		return segments[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String getSeparator();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRoot() {
		return segments.length == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstSegment() {
		return segments[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstSegmentWeak() {
		return getSegmentWeak(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastSegment() {
		return segments[segments.length - 1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastSegmentWeak() {
		return getSegmentWeak(segments.length - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSegments() {
		return getSegments(0, segments.length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSegments(final int first, final int count) {
		if ((first < 0) || (count < 0) || (first + count > segments.length)) {
			throw new IndexOutOfBoundsException();
		}

		final String[] result = new String[count];
		System.arraycopy(segments, first, result, 0, count);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSegmentsWeak(final int first, final int count) {
		if ((first < 0) || (count < 0) || (first + count > segments.length)) {
			return null;
		}

		final String[] result = new String[count];
		System.arraycopy(segments, first, result, 0, count);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSegments(final int first) {
		return getSegments(first, segments.length - first);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSegmentsWeak(final int first) {
		return getSegmentsWeak(first, segments.length - first);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getParent() {
		mayNotBeRoot();
		return internalCreate(getSegments(0, segments.length - 1), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getParentWeak() {
		return (segments.length == 0) ? null : getParent();
	}

	/**
	 * Returns the textual representation of an identifier using the
	 * specified segments and separator. Throws an
	 * <code>IndexOutOfBoundsException</code> if the specified range is not
	 * fully inside the bounds of the segment array.
	 * 
	 * @param segments the segments to turn into a string
	 * @param first the index of the first segment to consider
	 * @param count the number of segments to consider
	 * @param separator the separator to use
	 * @return the string
	 */
	public static String toString(final String[] segments, int first, int count, final String separator) {
		if (count == 0) {
			return "";
		}

		String result = segments[first];
		while (true) {
			first++;
			count--;
			if (count == 0) {
				break;
			}
			result = result + separator + segments[first];
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parentToString() {
		return parentToString(getSeparator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parentToStringWeak() {
		return parentToStringWeak(getSeparator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parentToString(final String separator) {
		mayNotBeRoot();
		return toString(segments, 0, segments.length - 1, separator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parentToStringWeak(final String separator) {
		return (segments.length == 0) ? null : parentToString(separator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T append(final String segment) {
		final String[] newSegments = new String[segments.length + 1];
		System.arraycopy(segments, 0, newSegments, 0, segments.length);
		newSegments[segments.length] = segment;
		return internalCreate(newSegments, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T append(final String[] otherSegments) {
		final String[] newSegments = new String[segments.length + otherSegments.length];
		System.arraycopy(segments, 0, newSegments, 0, segments.length);
		System.arraycopy(otherSegments, 0, newSegments, segments.length, otherSegments.length);
		return internalCreate(newSegments, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPrefixOf(final ITreeNodeIdentifier<T> other) {
		if (segments.length > other.getSegmentCount()) {
			return false;
		}

		for (int i = 0; i < segments.length; i++) {
			if (segments[i].equals(other.getSegment(i)) == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getCommonPrefix(final ITreeNodeIdentifier<T> other) {
		int i = 0;
		while (i < segments.length && i < other.getSegmentCount() && segments[i].equals(other.getSegments(i))) {
			i++;
		}
		return internalCreate(getSegments(0, i), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T convertType(final ITreeNodeIdentifier<T> other) {
		return internalCreate(other.getSegments(), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T buildSameType(final String[] segments) {
		return internalCreate(segments, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T parse(final String textual) {
		return internalCreate(splitIntoSegments(textual, getSeparator()), false);
	}

	/**
	 * Splits the specified string around the specified separator and
	 * returns the resulting strings in an array.
	 * 
	 * @param textual the textual representation of an identifier to split
	 * @param separator the separator to split at
	 * @return the segments
	 */
	public static String[] splitIntoSegments(String textual, final String separator) {
		if (textual.length() == 0) {
			return new String[0];
		}
		final int sepLength = separator.length();
		final List<String> newSegments = new ArrayList<String>();

		while (true) {
			final int index = textual.indexOf(separator);
			if (index == -1) {
				break;
			}
			newSegments.add(textual.substring(0, index));
			textual = textual.substring(index + sepLength);
		}

		newSegments.add(textual);
		return newSegments.toArray(new String[newSegments.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract boolean equals(Object other);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalSegments(final ITreeNodeIdentifier<T> other) {
		if (segments.length != other.getSegmentCount()) {
			return false;
		}

		for (int i = 0; i < segments.length; i++) {
			if (segments[i].equals(other.getSegment(i)) == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// there won't be a lot of different types or separators
		return Arrays.deepHashCode(segments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString(getSeparator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(final String separator) {
		return toString(segments, 0, segments.length, separator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lexicographicOrdering(final ITreeNodeIdentifier<T> other) {
		int i = 0;
		final int thisLength = segments.length;
		final int otherLength = other.getSegmentCount();

		while (i < thisLength && i < otherLength && segments[i].equals(other.getSegments(i))) {
			i++;
		}

		if (i == thisLength) {
			if (i == otherLength) {
				return 0;
			} else {
				return -1;
			}
		} else if (i == otherLength) {
			return 1;
		} else {
			return segments[i].compareTo(other.getSegment(i));
		}
	}
}
