/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

import name.martingeisse.stackd.common.collision.IAxisAlignedCollider;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.geometry.SectionId;

/**
 * This class wraps an {@link IAxisAlignedCollider} for a section.
 * Instances of this class are stored for sections in the {@link WorldWorkingSet}.
 */
public final class CollidingSection implements IAxisAlignedCollider {

	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the sectionId
	 */
	private final SectionId sectionId;

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the RenderableSection.java
	 */
	private final IAxisAlignedCollider collider;

	/**
	 * Constructor.
	 * @param workingSet the working set that contains this object
	 * @param sectionId the section id
	 * @param collider the collider
	 */
	public CollidingSection(final WorldWorkingSet workingSet, final SectionId sectionId, final IAxisAlignedCollider collider) {
		this.workingSet = workingSet;
		this.clusterSize = workingSet.getClusterSize();
		this.sectionId = sectionId;
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(clusterSize);
		this.collider = collider;
	}

	/**
	 * Getter method for the workingSet.
	 * @return the workingSet
	 */
	public WorldWorkingSet getWorkingSet() {
		return workingSet;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Getter method for the sectionId.
	 * @return the sectionId
	 */
	public SectionId getSectionId() {
		return sectionId;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}

	/**
	 * Getter method for the collider.
	 * @return the collider
	 */
	public IAxisAlignedCollider getCollider() {
		return collider;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.ICubeCollidingObject#getCurrentCollider()
	 */
	@Override
	public IAxisAlignedCollider getCurrentCollider() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.collision.ICubeCollider#collides(name.martingeisse.stackd.common.geometry.RectangularRegion)
	 */
	@Override
	public boolean collides(final RectangularRegion region) {
		return collider.collides(region);
	}

}
