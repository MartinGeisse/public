/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.section.rendermodel;

/**
 * Represents modifications to the underlying section that have not
 * yet been incorporated into the render model. Each modification
 * to a section produces damage, which builds up in the render
 * model and is represented by this class. Eventually, a complete
 * render model is needed for rendering and the damage must either
 * be repaired (using implementation-specific logic from the render
 * model) or the render model be thrown away and a new one be built.
 * 
 * Damage for a single section is represented by a mutable instance
 * of this class that gets modified for each modification to the
 * underlying section.
 * 
 * This class stores modified cubes for this section and border
 * cubes for neighboring sections. Both types of modifications are
 * stored in the same array, using a flag to distinguish. All
 * coordinates, including those for neighbor sections, are relative
 * to the origin of this section (so they can be -1 for negative
 * direction neighbors).
 * 
 * This class handles at most MAX_NUMBER_OF_MODIFIED_CUBES modified
 * cubes before entering a "too much damage" state that leaves no
 * indication about modified cubes, and hence should be treated by
 * all {@link ISectionRenderModel} implementations by returning
 * null to rebuild the render model from scratch. Being in this
 * "too much" / "infinite" damage state is represented as having
 * Integer.MAX_VALUE modifications. The reason for using an "infinite"
 * state is that most modifications won't need as many cubes, so we
 * want this class to optimize for few modifications. We also don't
 * want to cross the line where incrementally rebuilding the render
 * model is more expensive than rebuilding from scratch, but the
 * latter is actually handled better by the {@link ISectionRenderModel}
 * itself and not by this class.
 */
public final class RenderModelDamageAccumulator {

	/**
	 * The maximum number of modifications before damage becomes "infinite".
	 */
	public static final int MAX_NUMBER_OF_MODIFICATIONS = 20;

	/**
	 * the numberOfModifications
	 */
	private int numberOfModifications;
	
	/**
	 * the modificationIsNeighborFlags
	 */
	private final boolean[] modificationIsNeighborFlags;
	
	/**
	 * the modificationPositionsX
	 */
	private final int[] modificationPositionsX;

	/**
	 * the modificationPositionsY
	 */
	private final int[] modificationPositionsY;

	/**
	 * the modificationPositionsZ
	 */
	private final int[] modificationPositionsZ;
	
	/**
	 * the previousCubeTypes
	 */
	private final byte[] previousCubeTypes;
	
	/**
	 * the newCubeTypes
	 */
	private final byte[] newCubeTypes;
	
	/**
	 * Constructor.
	 */
	public RenderModelDamageAccumulator() {
		this.numberOfModifications = 0;
		this.modificationIsNeighborFlags = new boolean[MAX_NUMBER_OF_MODIFICATIONS];
		this.modificationPositionsX = new int[MAX_NUMBER_OF_MODIFICATIONS];
		this.modificationPositionsY = new int[MAX_NUMBER_OF_MODIFICATIONS];
		this.modificationPositionsZ = new int[MAX_NUMBER_OF_MODIFICATIONS];
		this.previousCubeTypes = new byte[MAX_NUMBER_OF_MODIFICATIONS];
		this.newCubeTypes = new byte[MAX_NUMBER_OF_MODIFICATIONS];
	}
	
	/**
	 * Getter method for the number of modifications.
	 * @return the number of modifications
	 */
	public int getNumberOfModifications() {
		return numberOfModifications;
	}
	
	/**
	 * Checks whether this damage accumulator is empty. This is the case if the
	 * number of modifications is 0.
	 * @return true if empty (render model is intact), false if non-empty (render model is damaged).
	 */
	public boolean isEmpty() {
		return (numberOfModifications == 0);
	}

	/**
	 * Checks whether this damage accumulator is in "infinite damage" state. This is
	 * the case if the number of modifications is greater than its maximum possible value.
	 * @return true if infinite damage (repair is impossible), false if finite damage (or even no damage at all)
	 */
	public boolean isInfinite() {
		return (numberOfModifications > MAX_NUMBER_OF_MODIFICATIONS);
	}
	
	/**
	 * Returns true if the specified modification occurred in a neighbor section.
	 * @param index the index of the modification
	 * @return true for neighbor modifications, false for modifications to this section
	 */
	public boolean getModificationIsNeighbor(int index) {
		return modificationIsNeighborFlags[index];
	}

	/**
	 * Returns the x coordinate of the modification with the specified index.
	 * @param index the index of the modification
	 * @return the x coordinate of the modification
	 */
	public int getModificationX(int index) {
		return modificationPositionsX[index];
	}
	
	/**
	 * Returns the y coordinate of the modification with the specified index.
	 * @param index the index of the modification
	 * @return the y coordinate of the modification
	 */
	public int getModificationY(int index) {
		return modificationPositionsY[index];
	}
	
	/**
	 * Returns the z coordinate of the modification with the specified index.
	 * @param index the index of the modification
	 * @return the z coordinate of the modification
	 */
	public int getModificationZ(int index) {
		return modificationPositionsZ[index];
	}

	/**
	 * Returns the previous cube type of the modification with the specified index.
	 * @param index the index of the modification
	 * @return the previous cube type of the modification
	 */
	public byte getPreviousCubeType(int index) {
		return previousCubeTypes[index];
	}
	
	/**
	 * Returns the new cube type of the modification with the specified index.
	 * @param index the index of the modification
	 * @return the new cube type of the modification
	 */
	public byte getNewCubeType(int index) {
		return newCubeTypes[index];
	}
	
	/**
	 * Adds damage for a modified cube.
	 * @param isNeighbor whether the modification happened in a neighbor section or in this section
	 * @param x the x coordinate of the modification, relative to the origin of this section
	 * @param y the y coordinate of the modification, relative to the origin of this section
	 * @param z the z coordinate of the modification, relative to the origin of this section
	 * @param previousCubeType the cube type before the modification
	 * @param newCubeType the cube type after the modification
	 */
	public void addDamage(boolean isNeighbor, int x, int y, int z, byte previousCubeType, byte newCubeType) {
		if (numberOfModifications < MAX_NUMBER_OF_MODIFICATIONS) {
			modificationIsNeighborFlags[numberOfModifications] = isNeighbor;
			modificationPositionsX[numberOfModifications] = x;
			modificationPositionsY[numberOfModifications] = y;
			modificationPositionsZ[numberOfModifications] = z;
			previousCubeTypes[numberOfModifications] = previousCubeType;
			newCubeTypes[numberOfModifications] = newCubeType;
			numberOfModifications++;
		} else {
			numberOfModifications = Integer.MAX_VALUE;
		}
	}

	/**
	 * Removes all accumulated damage from this object.
	 */
	public void clear() {
		numberOfModifications = 0;
	}
	
}
