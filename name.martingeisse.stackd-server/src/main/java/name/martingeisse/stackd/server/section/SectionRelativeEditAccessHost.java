/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import name.martingeisse.stackd.common.edit.IEditAccessHost;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This is the {@link IEditAccessHost} implementation for a {@link Section}.
 * Editing uses coordinates that are relative to the section's origin.
 */
final class SectionRelativeEditAccessHost implements IEditAccessHost {

	/**
	 * the cacheEntry
	 */
	private final SectionCubesCacheEntry cacheEntry;
	
	/**
	 * Constructor.
	 * @param cacheEntry the cache entry for the section to edit
	 */
	public SectionRelativeEditAccessHost(final SectionCubesCacheEntry cacheEntry) {
		this.cacheEntry = cacheEntry;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.edit.IEditAccessHost#containsPosition(int, int, int)
	 */
	@Override
	public boolean containsPosition(int x, int y, int z) {
		int size = cacheEntry.getClusterSize().getSize();
		return (x >= 0 && y >= 0 && z >= 0 && x < size && y < size && z < size);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.edit.IEditAccessHost#getRegion()
	 */
	@Override
	public RectangularRegion getRegion() {
		int size = cacheEntry.getClusterSize().getSize();
		return new RectangularRegion(0, 0, 0, size, size, size);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.engine.edit.IEditAccessHost#getCube(int, int, int)
	 */
	@Override
	public byte getCube(int x, int y, int z) {
		return cacheEntry.getCubeRelative(x, y, z);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.engine.edit.IEditAccessHost#setCube(int, int, int, byte)
	 */
	@Override
	public void setCube(int x, int y, int z, byte value) {
		cacheEntry.setCubeRelative(x, y, z, value);
	}
	
}
