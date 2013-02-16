/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import name.martingeisse.webide.entity.WorkspaceResourceDeltas;
import name.martingeisse.webide.resources.ResourcePath;

/**
 * This class is used to pass resource deltas to a builder.
 */
public final class BuilderResourceDelta {

	/**
	 * the path
	 */
	private final ResourcePath path;

	/**
	 * the deep
	 */
	private final boolean deep;

	/**
	 * Constructor for a delta from the database.
	 */
	BuilderResourceDelta(final WorkspaceResourceDeltas delta) {
		this(new ResourcePath(delta.getPath()), delta.getIsDeep());
	}
	
	/**
	 * Constructor.
	 * @param path the path where the delta has occurred
	 * @param deep whether the delta also affects child resources
	 */
	public BuilderResourceDelta(final ResourcePath path, final boolean deep) {
		this.path = path;
		this.deep = deep;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public ResourcePath getPath() {
		return path;
	}

	/**
	 * Getter method for the deep.
	 * @return the deep
	 */
	public boolean isDeep() {
		return deep;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{BuilderResourceDelta ").append(path).append(' ').append(deep ? "deep" : "flat").append('}');
		return builder.toString();
	}
	
}
