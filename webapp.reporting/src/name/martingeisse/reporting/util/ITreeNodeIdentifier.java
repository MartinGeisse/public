/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.util;

/**
 * <p>This interface is implemented by identifiers of all kind that are
 * used to select a node in a tree structure by walking a path starting
 * at the root node. The textual representation of such identifiers
 * consists of all path segments joined together with some separator.
 * Instances of this class are immutable.</p>
 * 
 * <p>For purposes of comparison and equality, the separator is considered to
 * be different from an equal string occurring in a segment. In general, this
 * means that turning a segmented identifier to a string and back may result
 * in an identifier which is not equal to the original identifier, because
 * a substring which was originally part of a segment was recognized as
 * a separator. Most types of segmented identifiers therefore do not allow
 * the separator to occur as part of a segment.</p>
 * 
 * <p>In theory, file system paths are segmented identifiers too, but
 * implementations for file path handling already exist which do not
 * use <code>SegmentedIdentifier</code>.</p>
 * 
 * <p>A <code>SegmentedIdentifier</code>, unlike file system paths, does not
 * know the concept of a relative path or identifier. All identifiers are
 * absolute in that their segments describe the path from the root to the
 * identified node.</p>
 * 
 * <p>The <code>SegmentedIdentifier</code> of the root node has no segments,
 * and its textual representation is the empty string.</p>
 * 
 * <p>This class should be parameterized with the concrete type of identifier
 * to allow more specific parameter and return types<p>
 * 
 * @param <T> the concrete subtype
 */
public interface ITreeNodeIdentifier<T extends ITreeNodeIdentifier<T>> {
	
	/**
	 * Returns the number of segments in this identifier. 
	 * @return the number of segments
	 */
	public int getSegmentCount();

	/**
	 * Returns the segment at the specified index, starting at 0 for the
	 * first segment. Throws a <code>IndexOutOfBoundsException</code> if the
	 * index is not in the range of valid segment indices, i.e. is negative or
	 * greater or equal to the number of segments.
	 * 
	 * @param index the index of the segment to return
	 * @return the segment
	 */
	public String getSegment(int index);

	/**
	 * Returns the segment at the specified index, starting at 0 for the
	 * first segment. Returns <code>null</code> if the index is not in the
	 * range of valid segment indices, i.e. is negative or greater or equal to
	 * the number of segments.
	 * 
	 * @param index the index of the segment to return
	 * @return the segment, or null if out of range
	 */
	public String getSegmentWeak(int index);

	/**
	 * Returns the preferred separator string which is used in the textual
	 * representation of this identifier to separate segments.
	 * 
	 * @return the separator
	 */
	public String getSeparator();

	/**
	 * Returns <code>true</code> if an only if this is the identifier of the
	 * root node. This method is equivalent to <code>(getSegmentCount() == 0)</code>.
	 * 
	 * @return true if this is the identifier of the root, false if not
	 */
	public boolean isRoot();

	/**
	 * This method returns the first segment, and is equivalent to
	 * <code>getSegment(0)</code>.
	 * 
	 * @return the first segment
	 */
	public String getFirstSegment();

	/**
	 * This method returns the first segment, and is equivalent to
	 * <code>getSegmentWeak(0)</code>.
	 * 
	 * @return the first segment, or null
	 */
	public String getFirstSegmentWeak();

	/**
	 * This method returns the last segment, and is equivalent to
	 * <code>getSegment(getSegmentCount() - 1)</code>.
	 * 
	 * @return the last segment
	 */
	public String getLastSegment();

	/**
	 * This method returns the last segment, and is equivalent to
	 * <code>getSegmentWeak(getSegmentCount() - 1)</code>.
	 * 
	 * @return the last segment, or null
	 */
	public String getLastSegmentWeak();

	/**
	 * Returns all segments in an array. The array is a copy of the
	 * internal segment storage and may be modified without affecting
	 * this identifier.
	 * 
	 * @return the segments
	 */
	public String[] getSegments();

	/**
	 * <p>Returns a subrange of the segments in an array. The array is a copy
	 * of the internal segment storage and may be modified without affecting
	 * this identifier.</p>
	 * 
	 * <p>Throws an <code>IndexOutOfBoundsException</code> if either argument is
	 * negative, or if the sum of the arguments is greater than the segment
	 * count of this identifier.</p>
	 * 
	 * @param first the index of the first segment to return
	 * @param count the number of segments to return
	 * @return the segments
	 */
	public String[] getSegments(int first, int count);

	/**
	 * <p>Returns a subrange of the segments in an array. The array is a copy
	 * of the internal segment storage and may be modified without affecting
	 * this identifier.</p>
	 * 
	 * <p>Returns <code>null</code> if either argument is
	 * negative, or if the sum of the arguments is greater than the segment
	 * count of this identifier.</p>
	 * 
	 * @param first the index of the first segment to return
	 * @param count the number of segments to return
	 * @return the segments, or null
	 */
	public String[] getSegmentsWeak(int first, int count);

	/**
	 * Returns a subrange of the segments in an array, starting at the
	 * specified index. This method is equivalent to
	 * <code>getSegments(first, getSegmentCount() - first)</code>.
	 * The array is a copy of the internal segment storage and may be modified
	 * without affecting this identifier.
	 * Throws an <code>IndexOutOfBoundsException</code> if the starting index
	 * is negative or greater than the segment count.
	 * 
	 * @param first the index of the first segment to return
	 * @return the segments
	 */
	public String[] getSegments(int first);

	/**
	 * Returns a subrange of the segments in an array, starting at the
	 * specified index. This method is equivalent to
	 * <code>getSegmentsWeak(first, getSegmentCount() - first)</code>.
	 * The array is a copy of the internal segment storage and may be modified
	 * without affecting this identifier.
	 * Returns <code>null</code> if the starting index is negative or greater
	 * than the segment count.
	 * 
	 * @param first the index of the first segment to return
	 * @return the segments, or null
	 */
	public String[] getSegmentsWeak(int first);

	/**
	 * Returns the parent identifier, which is an identifier of the
	 * same type and separator, with the last segment missing.
	 * Throws an <code>IllegalStateException</code> if invoked on the root
	 * node.
	 * 
	 * @return the identifier of the parent node
	 */
	public T getParent();

	/**
	 * Returns the parent identifier, which is an identifier of the
	 * same type and separator, with the last segment missing.
	 * Returns <code>null</code> if invoked on the root node.
	 * 
	 * @return the identifier of the parent node, or null
	 */
	public T getParentWeak();

	/**
	 * Returns the textual representation of the parent identifier. This
	 * method is equivalent to <code>getParent().toString()</code> but is
	 * generally faster.
	 * Throws an <code>IllegalStateException</code> if invoked on the root
	 * node.
	 * 
	 * @return the textual representation of the identifier of the parent node
	 */
	public String parentToString();

	/**
	 * Returns the textual representation of the parent identifier. This
	 * method is equivalent to
	 * <code>(getParentWeak() == null) ? null : getParent().toString()</code>
	 * but is generally faster.
	 * Returns <code>null</code> if invoked on the root node.
	 * 
	 * @return the textual representation of the identifier of the parent node, or null
	 */
	public String parentToStringWeak();

	/**
	 * Returns the textual representation of the parent identifier using
	 * the specified separator instead of the natural one. This method is
	 * equivalent to <code>getParent().toString(separator)</code> but is
	 * generally faster.
	 * Throws an <code>IllegalStateException</code> if invoked on the root
	 * node.
	 * 
	 * @param separator the separator to use
	 * @return the textual representation of the identifier of the parent node, using the specified identifier
	 */
	public String parentToString(String separator);

	/**
	 * Returns the textual representation of the parent identifier using
	 * the specified separator instead of the natural one. This method is
	 * equivalent to 
	 * <code>(getParentWeak() == null) ? null :
	 * getParent().toString(separator)</code> but is generally faster.
	 * Returns <code>null</code> if invoked on the root node.
	 * 
	 * @param separator the separator to use
	 * @return the textual representation of the identifier of the parent node, using the specified identifier, or null
	 */
	public String parentToStringWeak(String separator);

	/**
	 * Returns a segmented identifier by appending the specified segment
	 * to the end of this identifier.
	 *  
	 * @param segment the segment to append
	 * @return the new identifier
	 */
	public T append(String segment);

	/**
	 * Returns a segmented identifier by appending the specified segments
	 * to the end of this identifier. 
	 *  
	 * @param segments the segments to append
	 * @return the new identifier
	 */
	public T append(String[] segments);

	/**
	 * Returns true if and only if this identifier is equal to or is a prefix of the
	 * specified other identifier. To be so, this identifier must have at
	 * most as many segments as the other identifier, and the segments
	 * of this identifier must be equal to the corresponding first
	 * segments of the other identifier. The separators are ignored by
	 * this method, so <code>true</code> may be returned even if the
	 * separators are different.
	 * 
	 * @param other the other identifier
	 * @return true if this identifier is a prefix of the other identifier, false if not
	 */
	public boolean isPrefixOf(ITreeNodeIdentifier<T> other);

	/**
	 * Returns the longest common prefix of this identifier and the specified
	 * other identifier, that is, an identifier which <code>isPrefixOf</code>
	 * this and the other identifier, and of all identifiers which fulfil
	 * that rule, the one with the greatest segment count. The concrete
	 * type and separator of the returned identifier are the same as for
	 * this identifier. Note that the separator is ignored in finding the
	 * common prefix, so the common prefix of two identifiers with different
	 * separator is not necessarily empty.
	 * 
	 * @param other the other identifier
	 * @return the common prefix
	 */
	public T getCommonPrefix(ITreeNodeIdentifier<T> other);

	/**
	 * Returns an identifier of the same type and separator as this
	 * identifier, but with the segments from the specified other identifier.
	 * 
	 * @param other the identifier to take the segments from
	 * @return the converted identifier
	 */
	public T convertType(ITreeNodeIdentifier<T> other);

	/**
	 * Returns an identifier of the same type and separator as this
	 * identifier, but with the segments from the specified array.
	 * 
	 * @param segments the segments to use
	 * @return the identifier
	 */
	public T buildSameType(String[] segments);

	/**
	 * Use the type of this identifier to parse a string to another identifier
	 * of the same type. The segments of the parsed identifier are taken
	 * from the specified string, and the separator is taken from this
	 * identifier. This method is equivalent to splitting the string around
	 * the separator of this identifier, then calling
	 * <code>buildSameType</code> on the resulting segments.
	 * 
	 * @param textual the textual representation to parse
	 * @return the parsed identifier
	 */
	public T parse(String textual);

	/**
	 * Returns true if and only if this identifier is equal to the specified other
	 * identifier. Two identifiers are equal if they have the same
	 * concrete type, separator, number of segments, and segment contents.
	 * Returns <code>false</code> if the other object is not a
	 * <code>SegmentedIdentifier</code>.
	 */
	@Override
	public boolean equals(Object other);

	/**
	 * Returns true if and only if this identifier has the same number of segments
	 * and segment contents as the specified other identifier. The concrete
	 * types and separators of the two identifiers are ignored in this
	 * comparison. 
	 * 
	 * @param other the other identifier to compare with
	 * @return true if the segments are equal to the other identifier's segments, false if not
	 */
	public boolean equalSegments(ITreeNodeIdentifier<T> other);

	/**
	 * Returns the hash code for this identifier.
	 */
	@Override
	public int hashCode();

	/**
	 * Returns the textual representation of this segmented identifier. This
	 * is equivalent to calling <code>toString(getSeparator())</code>.
	 */
	@Override
	public String toString();

	/**
	 * Returns the textual representation of this segmented identifier using
	 * a different separator than its natural one.
	 * 
	 * @param separator the separator to use
	 * @return the textual representation of this identifier using the specified separator
	 */
	public String toString(String separator);

	/**
	 * <p>Determines the lexicographic ordering of this identifier and the
	 * specified other identifier. Returns a negative number, or zero, or a
	 * positive number if this identifier is, respectively, smaller, equal,
	 * or larger than the other identifier.</p>
	 * 
	 * <p>Lexicographic ordering of segmented identifiers is a straightforward
	 * extension of lexicographic string ordering: If the two strings differ
	 * at some segment, then the result is the lexicographic ordering of the
	 * differing segments. Otherwise, if one string has fewer segments, then
	 * it is smaller. Otherwise, they are equal in the sense of the
	 * <code>equalSegments</code> method, and also in the sense of the
	 * ordering.</p>
	 * 
	 * <p>Note that lexicographic ordering does not take the type or separator
	 * into account.</p> 
	 * 
	 * @param other the other identifier to compare with
	 * @return a negative number if this identifier is smaller than the other
	 * identifier; a positive number if it is greater than the other identifier,
	 * and zero if the identifiers are equal with respect to ordering
	 */
	public int lexicographicOrdering(ITreeNodeIdentifier<T> other);

}
