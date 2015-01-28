/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.entry;

import name.martingeisse.stackd.common.edit.IEditAccessHost;
import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This is the {@link IEditAccessHost} implementation for a {@link Section}.
 * Editing uses coordinates that are absolute to the world.
 */
final class SectionAbsoluteEditAccessHost implements IEditAccessHost {

	/**
	 * the cacheEntry
	 */
	private final SectionCubesCacheEntry cacheEntry;
	
	/**
	 * Constructor.
	 * @param cacheEntry the cache entry for the section to edit
	 */
	public SectionAbsoluteEditAccessHost(final SectionCubesCacheEntry cacheEntry) {
		this.cacheEntry = cacheEntry;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.edit.IEditAccessHost#containsPosition(int, int, int)
	 */
	@Override
	public boolean containsPosition(int x, int y, int z) {
		return cacheEntry.getRegion().contains(x, y, z);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.edit.IEditAccessHost#getRegion()
	 */
	@Override
	public RectangularRegion getRegion() {
		return cacheEntry.getRegion();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.engine.edit.IEditAccessHost#getCube(int, int, int)
	 */
	@Override
	public byte getCube(int x, int y, int z) {
		return cacheEntry.getCubeAbsolute(x, y, z);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.engine.edit.IEditAccessHost#setCube(int, int, int, byte)
	 */
	@Override
	public void setCube(int x, int y, int z, byte value) {
		cacheEntry.setCubeAbsolute(x, y, z, value);
	}
	
}
