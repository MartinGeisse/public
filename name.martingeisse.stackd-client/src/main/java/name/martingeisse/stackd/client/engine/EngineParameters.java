/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.engine;

import java.util.Arrays;
import name.martingeisse.stackd.client.engine.renderer.ISectionRenderer;
import name.martingeisse.stackd.client.system.StackdTexture;
import name.martingeisse.stackd.common.cubetype.CubeType;

/**
 * Static parameters for the engine, such as various strategies.
 */
public final class EngineParameters {

	/**
	 * the sectionRenderer
	 */
	private final ISectionRenderer sectionRenderer;

	/**
	 * the cubeTextures
	 */
	private final StackdTexture[] cubeTextures;

	/**
	 * the cubeTypes
	 */
	private final CubeType[] cubeTypes;

	/**
	 * Constructor.
	 * @param sectionRenderer the renderer for sections
	 * @param cubeTextures the cube textures
	 * @param cubeTypes defines how different cubes behave
	 */
	public EngineParameters(final ISectionRenderer sectionRenderer, final StackdTexture[] cubeTextures, final CubeType[] cubeTypes) {
		this.sectionRenderer = sectionRenderer;
		this.cubeTextures = cubeTextures;
		this.cubeTypes = cubeTypes;
	}

	/**
	 * Getter method for the sectionRenderer.
	 * @return the sectionRenderer
	 */
	public ISectionRenderer getSectionRenderer() {
		return sectionRenderer;
	}

	/**
	 * @return the number of cube textures
	 */
	public int getCubeTextureCount() {
		return cubeTextures.length;
	}
	
	/**
	 * Returns the cube texture for the specified cube texture index.
	 * @param cubeTextureIndex the cube texture index
	 * @return the cube texture
	 */
	public StackdTexture getCubeTexture(final int cubeTextureIndex) {
		return cubeTextures[cubeTextureIndex];
	}

	/**
	 * Returns a copy of the internal cube texture array.
	 * @return a new array containing all cube textures
	 */
	public StackdTexture[] getCubeTextures() {
		return Arrays.copyOf(cubeTextures, cubeTextures.length);
	}

	/**
	 * @return the number of cube types
	 */
	public int getCubeTypeCount() {
		return cubeTypes.length;
	}
	
	/**
	 * Returns the cube type for the specified type index.
	 * @param cubeTypeIndex the cube type index
	 * @return the cube type
	 */
	public CubeType getCubeType(final int cubeTypeIndex) {
		return cubeTypes[cubeTypeIndex];
	}

	/**
	 * Returns a copy of the internal cube type array.
	 * @return a new array containing all cube types
	 */
	public CubeType[] getCubeTypes() {
		return Arrays.copyOf(cubeTypes, cubeTypes.length);
	}

}
