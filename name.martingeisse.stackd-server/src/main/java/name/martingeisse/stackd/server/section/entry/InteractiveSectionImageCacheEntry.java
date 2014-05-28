/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.entry;

import name.martingeisse.stackd.common.cubes.Cubes;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.ClusterSize;
import name.martingeisse.stackd.common.network.SectionDataId;
import name.martingeisse.stackd.common.network.SectionDataType;
import name.martingeisse.stackd.server.section.SectionWorkingSet;

/**
 * A section cache entry for the INTERACTIVE section image.
 * 
 * TODO To allow the client to remove additional hidden faces, information about
 * the neighbor sections should be included in the image.
 */
public final class InteractiveSectionImageCacheEntry extends SectionDataCacheEntry {
	
	/**
	 * the imageData
	 */
	private byte[] imageData;

	/**
	 * Constructor that just stores an already serialized image.
	 * 
	 * @param sectionWorkingSet the working set from which this cached object comes from
	 * @param sectionDataId the section data id
	 * @param imageData the serialized image
	 */
	public InteractiveSectionImageCacheEntry(SectionWorkingSet sectionWorkingSet, SectionDataId sectionDataId, byte[] imageData) {
		super(sectionWorkingSet, sectionDataId);
		this.imageData = imageData;
	}

	/**
	 * Getter method for the imageData.
	 * @return the imageData
	 */
	public synchronized byte[] getImageData() {
		if (imageData == null) {
			SectionDataId cubeDataId = getSectionDataId().getWithType(SectionDataType.DEFINITIVE);
			SectionWorkingSet workingSet = getSectionWorkingSet();
			SectionCubesCacheEntry definitiveEntry = (SectionCubesCacheEntry)workingSet.get(cubeDataId);
			Cubes originalCubes = definitiveEntry.getSectionCubes();
			Cubes clonedCubes = originalCubes.clone();
			ClusterSize clusterSize = workingSet.getClusterSize();
			int size = clusterSize.getSize();
			CubeType[] cubeTypes = workingSet.getServer().getCubeTypes();
			AxisAlignedDirection[] directions = AxisAlignedDirection.values();
			for (int x=0; x<size; x++) {
				for (int y=0; y<size; y++) {
					zloop: for (int z=0; z<size; z++) {
						for (AxisAlignedDirection direction : directions) {
							int x2 = x + direction.getSignX();
							int y2 = y + direction.getSignY();
							int z2 = z + direction.getSignZ();
							CubeType neighborCubeType;
							if (direction.selectByAxis(x, y, z) == (direction.isNegative() ? 0 : size - 1)) {
								x2 -= direction.select(size, 0, 0);
								y2 -= direction.select(0, size, 0);
								z2 -= direction.select(0, 0, size);
								SectionDataId neighborCubeDataId = cubeDataId.getNeighbor(direction);
								SectionCubesCacheEntry neighborEntry = (SectionCubesCacheEntry)workingSet.get(neighborCubeDataId);
								Cubes neighborCubes = neighborEntry.getSectionCubes();
								neighborCubeType = cubeTypes[neighborCubes.getCubeRelative(clusterSize, x2, y2, z2) & 0xff];
							} else {
								neighborCubeType = cubeTypes[originalCubes.getCubeRelative(clusterSize, x2, y2, z2) & 0xff];
							}
							AxisAlignedDirection directionTowardsOriginal = direction.getOpposite();
							if (!neighborCubeType.obscuresNeighbor(directionTowardsOriginal) || !neighborCubeType.blocksMovementToNeighbor(directionTowardsOriginal)) {
								continue zloop;
							}
						}
						clonedCubes = clonedCubes.setCubeRelative(clusterSize, x, y, z, (byte)255);
					}
				}
			}
			this.imageData = clonedCubes.compressToByteArray(workingSet.getClusterSize());
			markModified();
		}
		return imageData;
	}
	
	/**
	 * Invalidates the data in this cache entry. Note that this does not make the
	 * cache entry itself invalid; it makes the data in it invalid and actually
	 * marks the cache entry as modified to write the invalidation back to storage.
	 */
	public synchronized void invalidateData() {
		this.imageData = null;
		markModified();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.AbstractSectionRelatedCachedObject#serializeForSave()
	 */
	@Override
	protected synchronized byte[] serializeForSave() {
		return getImageData();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.section.SectionDataCacheEntry#getDataForClient()
	 */
	@Override
	public byte[] getDataForClient() {
		return getImageData();
	}
	
}
