/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.edit;

import name.martingeisse.stackd.common.geometry.RectangularRegion;

/**
 * This implementation of {@link IEditAccessHost} does nothing and
 * returns cube type 0 for all cubes.
 */
public class NullEditAccessHost implements IEditAccessHost {

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * Constructor.
	 * @param region the region covered by this host
	 */
	public NullEditAccessHost(RectangularRegion region) {
		this.region = region;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#containsPosition(int, int, int)
	 */
	@Override
	public boolean containsPosition(int x, int y, int z) {
		return region.contains(x, y, z);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#getRegion()
	 */
	@Override
	public RectangularRegion getRegion() {
		return region;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#getCube(int, int, int)
	 */
	@Override
	public byte getCube(int x, int y, int z) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.edit.IEditAccessHost#setCube(int, int, int, byte)
	 */
	@Override
	public void setCube(int x, int y, int z, byte value) {
	}
	
}
