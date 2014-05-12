/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section;

import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.edit.EditAccess;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.geometry.SectionId;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;

/**
 * A section cache entry for the section data.
 */
public final class SectionCubesCacheEntry extends SectionDataCacheEntry {

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the sectionCubes
	 */
	private Cubes sectionCubes;

	/**
	 * Constructor.
	 * @param sectionWorkingSet the working set from which this cached object comes from
	 * @param sectionDataId the section data id
	 * @param sectionCubes the section data
	 */
	public SectionCubesCacheEntry(final SectionWorkingSet sectionWorkingSet, final SectionDataId sectionDataId, final Cubes sectionCubes) {
		super(sectionWorkingSet, sectionDataId);
		SectionId sectionId = sectionDataId.getSectionId();
		this.clusterSize = sectionWorkingSet.getClusterSize();
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(clusterSize);
		this.sectionCubes = sectionCubes;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}

	/**
	 * Getter method for the sectionCubes.
	 * @return the sectionCubes
	 */
	public Cubes getSectionCubes() {
		return sectionCubes;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionRelatedCachedObject#serializeForSave()
	 */
	@Override
	protected byte[] serializeForSave() {
		return sectionCubes.compressToByteArray(getSectionWorkingSet().getClusterSize());
	}

	/**
	 * Returns the cube value for the specified absolute position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public final byte getCubeAbsolute(final int x, final int y, final int z) {
		return getCubeRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ());
	}

	/**
	 * Returns the cube value for the specified section-relative position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public final byte getCubeRelative(final int x, final int y, final int z) {
		return sectionCubes.getCubeRelative(clusterSize, x, y, z);
	}

	/**
	 * Sets the cube value for the specified absolute position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public final void setCubeAbsolute(final int x, final int y, final int z, final byte value) {
		setCubeRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ(), value);
	}

	/**
	 * Sets the cube value for the specified section-relative position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public final void setCubeRelative(final int x, final int y, final int z, final byte value) {
		this.sectionCubes = sectionCubes.setCubeRelative(clusterSize, x, y, z, value);
		markCubeModifiedRelative(x, y, z);
	}

	/**
	 * Marks this cache entry as modified, and possibly neighbor sections as well. The cube
	 * position is specified in absolute coordinates.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 */
	public final void markCubeModifiedAbsolute(final int x, final int y, final int z) {
		markCubeModifiedRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ());
	}

	/**
	 * Marks this cache entry as modified, and possibly neighbor sections as well. The cube
	 * position is specified relative to this section.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 */
	public synchronized final void markCubeModifiedRelative(final int x, final int y, final int z) {

		// mark modified as usual
		markModified();
		
		// mark neighbor sections modified if this cube is on the border
		SectionDataId sectionDataId = getSectionDataId();
		for (AxisAlignedDirection neighborDirection : clusterSize.getBorderDirections(x, y, z)) {
			// TODO there should be a way to do this without loading the to-be-invalidated object from storage
			
			SectionDataId neighborInteractiveDataId = sectionDataId.getNeighbor(neighborDirection, SectionDataType.INTERACTIVE);
			InteractiveSectionImageCacheEntry neighborInteractiveDataEntry = (InteractiveSectionImageCacheEntry)getSectionWorkingSet().getSectionDataCache().get(neighborInteractiveDataId);
			neighborInteractiveDataEntry.invalidateData();
			
		}

	}

	/**
	 * Returns an {@link EditAccess} for this section. Editing uses coordinates that
	 * are absolute to the world.
	 * 
	 * @return the edit access
	 */
	public EditAccess getAbsoluteEditAccess() {
		return new EditAccess(new SectionAbsoluteEditAccessHost(this));
	}

	/**
	 * Returns an {@link EditAccess} for this section. Editing uses coordinates that
	 * are relative to this section's origin.
	 * 
	 * @return the edit access
	 */
	public EditAccess getRelativeEditAccess() {
		return new EditAccess(new SectionRelativeEditAccessHost(this));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionRelatedCacheEntry#onModification()
	 */
	@Override
	protected void onModification() {

		// invalidate render model and collider
		// TODO there should be a way to do this without loading the to-be-invalidated object from storage
		SectionDataId sectionDataId = getSectionDataId();
		((InteractiveSectionImageCacheEntry)getSectionWorkingSet().getSectionDataCache().get(sectionDataId.getWithType(SectionDataType.INTERACTIVE))).invalidateData();

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.SectionDataCacheEntry#getDataForClient()
	 */
	@Override
	public byte[] getDataForClient() {
		// this object cannot be sent to the client to prevent information cheating
		return new byte[0];
	}
	
}
