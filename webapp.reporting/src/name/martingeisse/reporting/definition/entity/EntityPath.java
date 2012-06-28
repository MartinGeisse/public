/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import name.martingeisse.reporting.util.AbstractTreeNodeIdentifier;

/**
 * A path that selects source tables or properties within entity queries.
 */
public final class EntityPath extends AbstractTreeNodeIdentifier<EntityPath> {
	
	/**
	 * The root identifier. 
	 */
	public static final EntityPath root = new EntityPath(new String[0]);

	/**
	 * Create a new defined model identifier from the specified segments.
	 * The argument is copied, so subsequent changes will not affect
	 * this identifier.
	 * 
	 * @param segments the segments to use
	 */
	public EntityPath(final String[] segments) {
		super(segments, true);
	}

	/**
	 * Create a new defined model identifier from a subrange of the specified
	 * segments. The argument is copied, so subsequent changes will not affect
	 * this identifier.
	 * 
	 * @param segments the segments to use
	 * @param first the index of the first segment to use
	 * @param count the number of segments to use
	 */
	public EntityPath(final String[] segments, final int first, final int count) {
		super(segments, first, count);
	}

	// internal constructor that allows to specify segment array sharing
	private EntityPath(final String[] segments, final boolean copy) {
		super(segments, copy);
	}

	/**
	 * Creates a defined model identifier from its textual representation.
	 * This method must not be called on the textual representation of
	 * generated model identifiers.
	 * 
	 * @param s the textual representation to parse
	 */
	public EntityPath(final String s) {
		super(AbstractTreeNodeIdentifier.splitIntoSegments(s, "."), false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EntityPath internalCreate(final String[] segments, final boolean copy) {
		return new EntityPath(segments, copy);
	}

	/**
	 * Returns the separator string for defined model identifier, which
	 * is <code>"."</code>.
	 */
	@Override
	public String getSeparator() {
		return ".";
	}

	/**
	 * Returns <code>true</code> iff the other object is a defined model
	 * identifier with the same number of segments and segment contents
	 * as this identifier.
	 */
	@Override
	public boolean equals(final Object other) {
		if (other instanceof EntityPath) {
			return equalSegments((EntityPath)other);
		} else {
			return false;
		}
	}
}
