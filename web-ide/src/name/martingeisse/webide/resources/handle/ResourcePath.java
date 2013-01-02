/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.handle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A path used to handle workspace resources. A path consists of:
 * 
 * - an optional leading separator
 * - zero or more string segments
 * - an optional trailing separator
 * 
 * Paths are called "absolute" if they have a leading separator, and
 * "relative" if they don't.
 *
 * Paths also have a string representation that uses '/' as the separator:
 * 
 * - a path without segments is represented either by "" (if it has no leading
 *   separator) or "/" (if it does have a leading separator). The trailing
 *   separator is ignored for the string representation of such paths; the
 *   parser assumes that no trailing separator exists.
 *  
 * - a path with segments is represented by joining the segments with a '/'
 *   character and prepending / appending the leading / trailing '/' character.
 *
 * Paths are immutable objects. The {@link #equals(Object)} and {@link #hashCode()}
 * methods are implemented accordingly. All methods used to modify paths return
 * a new path object.
 * 
 * Paths can be concatenated: The right-hand path must be relative; the resulting
 * path is relative if the left-hand path is. Trying to use an absolute path as
 * the right-hand path results in an exception, since it is likely a bug.
 * 
 * Absolute paths can be used to locate workspace resources. Again, using a
 * relative path for this results in an exception, since it is likely a bug.
 * 
 * Paths are comparable using lexicographic ordering on the segments,
 * absolute paths before relative paths, and paths without a trailing
 * separator before those with a trailing separator.
 */
public final class ResourcePath implements Serializable, Iterable<String>, Comparable<ResourcePath> {

	/**
	 * the emptySegments
	 */
	private static String[] emptySegments = new String[0];
	
	/**
	 * the leadingSeparator
	 */
	private final boolean leadingSeparator;

	/**
	 * the trailingSeparator
	 */
	private final boolean trailingSeparator;

	/**
	 * the segmentStorage
	 */
	private final String[] segmentStorage;

	/**
	 * the firstSegmentIndex
	 */
	private final int firstSegmentIndex;

	/**
	 * the segmentCount
	 */
	private final int segmentCount;

	/**
	 * Internal Constructor.
	 */
	ResourcePath(final boolean leadingSeparator, final boolean trailingSeparator, final String[] segmentStorage, final int firstSegmentIndex, final int segmentCount, boolean copy) {
		this.leadingSeparator = leadingSeparator;
		this.trailingSeparator = trailingSeparator;
		if (copy) {
			this.segmentStorage = Arrays.copyOfRange(segmentStorage, firstSegmentIndex, segmentCount);
			this.firstSegmentIndex = 0;
			this.segmentCount = segmentCount;
		} else {
			this.segmentStorage = segmentStorage;
			this.firstSegmentIndex = firstSegmentIndex;
			this.segmentCount = segmentCount;
		}
	}
	
	/**
	 * Constructor.
	 * @param pathToParse the textual representation of a path to parse
	 */
	public ResourcePath(String pathToParse) {

		// some special cases
		if (pathToParse.isEmpty()) {
			leadingSeparator = trailingSeparator = false;
			segmentStorage = emptySegments;
			firstSegmentIndex = segmentCount = 0;
			return;
		}
		if (pathToParse.equals("/")) {
			leadingSeparator = true;
			trailingSeparator = false;
			segmentStorage = emptySegments;
			firstSegmentIndex = segmentCount = 0;
			return;
		}
		
		// parse segments
		...
		
		// no segments at this point means we have something like "//"
		if (segmentCount == 0) {
			throw new IllegalArgumentException("invalid path: " + pathToParse);
		}
		
		/*
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
			 * 
		 */
		
	}

	/**
	 * Getter method for the leadingSeparator.
	 * @return the leadingSeparator
	 */
	public boolean isLeadingSeparator() {
		return leadingSeparator;
	}
	
	/**
	 * Getter method for the trailingSeparator.
	 * @return the trailingSeparator
	 */
	public boolean isTrailingSeparator() {
		return trailingSeparator;
	}
	
	/**
	 * Getter method for the segmentCount.
	 * @return the segmentCount
	 */
	public int getSegmentCount() {
		return segmentCount;
	}
	
	/**
	 * Getter method for the segment with the specified index.
	 * @param index the segment index
	 * @return the segment
	 */
	public String getSegment(int index) {
		if (index < 0 || index >= segmentCount) {
			throw new IndexOutOfBoundsException("segment index " + index + " is out of bounds for segment count " + segmentCount);
		}
		return segmentStorage[firstSegmentIndex + index];
	}

	/**
	 * Getter method for the first segment.
	 * @return the first segment
	 */
	public String getFirstSegment() {
		return getSegment(0);
	}
	
	/**
	 * Getter method for the last segment.
	 * @return the last segment
	 */
	public String getLastSegment() {
		return getSegment(segmentCount - 1);
	}

	/**
	 * Getter method for all segments.
	 * @return the segments
	 */
	public String[] getSegments() {
		String[] result = new String[segmentCount];
		System.arraycopy(segmentStorage, firstSegmentIndex, result, 0, segmentCount);
		return result;
	}
	
	/**
	 * Getter method for a range of segments. The range ends at the end of this path.
	 * @param first the index of the first segment to return
	 * @return the segments
	 */
	public String[] getSegments(int first) {
		return getSegments(first, segmentCount - first);
	}
	
	/**
	 * Getter method for a range of segments.
	 * @param first the index of the first segment to return
	 * @param count the number of segments to return
	 * @return the segments
	 */
	public String[] getSegments(int first, int count) {
		validateRange(first, count);
		String[] result = new String[count];
		System.arraycopy(segmentStorage, firstSegmentIndex + first, result, 0, count);
		return result;
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the first segment being removed.
	 * 
	 * @param withLeadingSeparator whether the returned path should have a leading separator
	 * @return the new path
	 */
	public ResourcePath removeFirstSegment(boolean withLeadingSeparator) {
		return removeFirstSegments(1, withLeadingSeparator);
	}
	
	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the first n segments being removed.
	 * 
	 * @param n the number of segments to remove
	 * @param withLeadingSeparator whether the returned path should have a leading separator
	 * @return the new path
	 */
	public ResourcePath removeFirstSegments(int n, boolean withLeadingSeparator) {
		validateRange(n, segmentCount - n);
		return new ResourcePath(withLeadingSeparator, trailingSeparator, segmentStorage, firstSegmentIndex + n, segmentCount - n, false);
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the specified segment being prepended. 
	 * 
	 * @param segment the segment to prepend
	 * @param withLeadingSeparator whether the returned path should have a leading separator
	 * @return the new path
	 */
	public ResourcePath prependSegment(String segment, boolean withLeadingSeparator) {
	}
	
	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the specified segments being prepended. 
	 * 
	 * @param segments the segments to prepend
	 * @param withLeadingSeparator whether the returned path should have a leading separator
	 * @return the new path
	 */
	public ResourcePath prependSegments(String[] segments, boolean withLeadingSeparator) {
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the last segment being removed.
	 * 
	 * @param withTrailingSeparator whether the returned path should have a trailing separator
	 * @return the new path
	 */
	public ResourcePath removeLastSegment(boolean withTrailingSeparator) {
		return removeLastSegments(1, withTrailingSeparator);
	}
	
	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the last n segments being removed.
	 * 
	 * @param n the number of segments to remove
	 * @param withTrailingSeparator whether the returned path should have a trailing separator
	 * @return the new path
	 */
	public ResourcePath removeLastSegments(int n, boolean withTrailingSeparator) {
		validateRange(0, segmentCount - n);
		return new ResourcePath(leadingSeparator, withTrailingSeparator, segmentStorage, firstSegmentIndex, segmentCount - 1, false);
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the specified segment being appended. 
	 * 
	 * @param segment the segment to append
	 * @param withTrailingSeparator whether the returned path should have a trailing separator
	 * @return the new path
	 */
	public ResourcePath appendSegment(String segment, boolean withTrailingSeparator) {
	}
	
	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the specified segment being appended. 
	 * 
	 * @param segments the segments to append
	 * @param withTrailingSeparator whether the returned path should have a trailing separator
	 * @return the new path
	 */
	public ResourcePath appendSegments(String[] segments, boolean withTrailingSeparator) {
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the specified segment being replaced. The index must be valid according to the
	 * number of segments in this path.
	 * 
	 * @param index the index of the segment to replace
	 * @param segment the new segment
	 * @return the new path
	 */
	public ResourcePath replaceSegment(int index, String segment) {
		String[] newSegments = getSegments();
		newSegments[index] = segment;
		return new ResourcePath(leadingSeparator, trailingSeparator, newSegments, 0, segmentCount, false);
	}

	/**
	 * Returns a resource path that has the same segments as this path except for
	 * the last segment being replaced. The number of segments must be greater than 0.
	 * 
	 * @param segment the new segment
	 * @return the new path
	 */
	public ResourcePath replaceLastSegment(String segment) {
		return replaceSegment(segmentCount - 1, segment);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object untypedOther) {
		
		// identity check
		if (this == untypedOther) {
			return true;
		}
		
		// type check
		if (!(untypedOther instanceof ResourcePath)) {
			return false;
		}
		ResourcePath other = (ResourcePath)untypedOther;
			
		// compare simple properties
		if (leadingSeparator != other.leadingSeparator) {
			return false;
		}
		if (trailingSeparator != other.trailingSeparator) {
			return false;
		}
		if (segmentCount != other.segmentCount) {
			return false;
		}
		
		// compare segments
		if (segmentStorage == other.segmentStorage) {
			return (firstSegmentIndex == other.firstSegmentIndex);
		}
		for (int i=0; i<segmentCount; i++) {
			if (!segmentStorage[firstSegmentIndex + i].equals(other.segmentStorage[other.firstSegmentIndex + i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(leadingSeparator);
		builder.append(trailingSeparator);
		for (String segment : this) {
			builder.append(segment);
		}
		return builder.toHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {
			
			// number of iterated segments
			private int position = 0;
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public String next() {
				if (position >= segmentCount) {
					throw new NoSuchElementException();
				}
				String segment = segmentStorage[firstSegmentIndex + position];
				position++;
				return segment;
			}
			
			@Override
			public boolean hasNext() {
				return (position < segmentCount);
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (segmentCount == 0) {
			return (leadingSeparator ? "/" : "");
		}
		StringBuilder builder = new StringBuilder();
		if (leadingSeparator) {
			builder.append('/');
		}
		boolean first = true;
		for (int i=0; i<segmentCount; i++) {
			if (first) {
				first = false;
			} else {
				builder.append('/');
			}
			builder.append(segmentStorage[firstSegmentIndex + i]);
		}
		if (trailingSeparator) {
			builder.append('/');
		}
		return super.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ResourcePath other) {
		if (leadingSeparator != other.leadingSeparator) {
			return (leadingSeparator ? -1 : 1);
		}
		int position = 0;
		while (true) {
			if (position == segmentCount) {
				if (position == other.segmentCount) {
					break;
				} else {
					return -1;
				}
			} else if (position == other.segmentCount) {
				return 1;
			}
			int segmentResult = segmentStorage[firstSegmentIndex + position].compareTo(other.segmentStorage[other.firstSegmentIndex + position]);
			if (segmentResult != 0) {
				return segmentResult;
			}
			position++;
		}
		if (trailingSeparator != other.trailingSeparator) {
			return (trailingSeparator ? 1 : -1);
		} else {
			return 0;
		}
	}
	
	/**
	 * Ensures that the specified range is valid:
	 * - (first >= 0)
	 * - (count >= 0)
	 * - (first + count <= segmentCount)
	 */
	private void validateRange(int first, int count) {
		if (first < 0) {
			throw new IndexOutOfBoundsException("'first' is negative");
		}
		if (count < 0) {
			throw new IndexOutOfBoundsException("'count' is negative");
		}
		if (first + count > segmentCount) {
			throw new IndexOutOfBoundsException("end of range is greater than the number of segments");
		}
	}
	
}
