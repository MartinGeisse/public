/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.cubes;

import java.io.IOException;
import java.io.OutputStream;

import name.martingeisse.stackd.common.geometry.ClusterSize;

/**
 * Cubes object that is filled uniformly with a single cube type. Only the cube
 * type is stored. Any modification that sets a cube to another type requires
 * another {@link Cubes} object to be built.
 */
public final class UniformCubes extends Cubes {

	/**
	 * the cubeType
	 */
	private final byte cubeType;

	/**
	 * Constructor.
	 * @param cubeType the cube type
	 */
	public UniformCubes(final byte cubeType) {
		this.cubeType = cubeType;
	}

	/**
	 * Getter method for the cubeType.
	 * @return the cubeType
	 */
	public byte getCubeType() {
		return cubeType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#compressToStreamInternal(name.martingeisse.stackd.common.geometry.ClusterSize, java.io.OutputStream)
	 */
	@Override
	protected void compressToStreamInternal(final ClusterSize clusterSize, final OutputStream stream) throws IOException {
		// do not write anything if cubeType is 0 because "uniform" and "type 0" are implicit
		if (cubeType != 0) {
			stream.write(0);
			stream.write(cubeType);
		}
	}

	/**
	 * Decompresses and deserializes an object of this type from the specified array,
	 * skipping the first byte since it is assumed to contain the compression scheme.
	 * 
	 * @param clusterSize the cluster size
	 * @param compressedData the compressed data
	 * @return the cubes object, or null if not successful
	 */
	public static UniformCubes decompress(final ClusterSize clusterSize, final byte[] compressedData) {
		if (compressedData.length < 2) {
			return new UniformCubes((byte)0);
		} else {
			return new UniformCubes(compressedData[1]);
		}
	}

	/**
	 * Tries to build an instance of this type from the specified cube data.
	 * 
	 * @param cubes the cube data
	 * @return the cubes object, or null if not successful
	 */
	public static UniformCubes tryBuild(final byte[] cubes) {
		if (!isUniform(cubes)) {
			return null;
		}
		return new UniformCubes(cubes[0]);
	}

	/**
	 * 
	 */
	private static boolean isUniform(final byte[] array) {
		final byte value = array[0];
		for (final byte otherValue : array) {
			if (value != otherValue) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#getCubeTypeIndicesUsed()
	 */
	@Override
	public byte[] getCubeTypeIndicesUsed() {
		return new byte[] { cubeType };
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#getCubeRelative(name.martingeisse.stackd.common.geometry.ClusterSize, int, int, int)
	 */
	@Override
	public byte getCubeRelative(final ClusterSize clusterSize, final int x, final int y, final int z) {
		return cubeType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#setCubeRelative(name.martingeisse.stackd.common.geometry.ClusterSize, int, int, int, byte)
	 */
	@Override
	public Cubes setCubeRelative(final ClusterSize clusterSize, final int x, final int y, final int z, final byte value) {
		if (value == cubeType) {
			return this;
		}
		final RawCubes newData = RawCubes.buildUniform(clusterSize, cubeType);
		return newData.setCubeRelative(clusterSize, x, y, z, value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#convertToRawCubes(name.martingeisse.stackd.common.geometry.ClusterSize)
	 */
	@Override
	public RawCubes convertToRawCubes(final ClusterSize clusterSize) {
		return RawCubes.buildUniform(clusterSize, cubeType);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.cubes.Cubes#clone()
	 */
	@Override
	public UniformCubes clone() {
		return new UniformCubes(cubeType);
	}

}
