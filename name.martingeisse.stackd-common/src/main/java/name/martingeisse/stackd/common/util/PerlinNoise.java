/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.util;

import java.util.Random;

/**
 * Helper class to compute Perlin noise.
 */
public final class PerlinNoise {

	/**
	 * the permutationTable
	 */
	private final int[] permutationTable = new int[256];
	
	/**
	 * Constructor.
	 */
	public PerlinNoise() {
		this(System.currentTimeMillis());
	}
	
	/**
	 * Constructor.
	 * @param seed the random generator seed
	 */
	public PerlinNoise(long seed) {
		Random random = new Random(seed);
		for (int i=0; i<256; i++) {
			permutationTable[i] = i;
		}
		for (int i=255; i>=0; i--) {
			int j = random.nextInt(i + 1);
			int temp = permutationTable[i];
			permutationTable[i] = permutationTable[j];
			permutationTable[j] = temp;
		}
	}
	
	/**
	 * Computes the value of the 2d Perlin Noise function at the specified position.
	 * The granularity of the noise is around 1.0, randomly a bit more or less, and
	 * repeats every 256.0 units.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @return the noise function value
	 */
	public double computeNoise(double x, double y) {
		
		// compute grid-granular coordinates
		int majorX = (int)Math.floor(x);
		int majorY = (int)Math.floor(y);
		
		// compute sub-grid coordinates
		double minorX = (x - majorX);
		double minorY = (y - majorY);
		
		// compute eased interpolation positions
		double easedX = computeEasingFunction(minorX);
		double easedY = computeEasingFunction(minorY);
		
		// compute corner random values and contributions
		double contribution00 = computeGradientDotProduct(randomize(majorX, majorY), minorX, minorY);
		double contribution01 = computeGradientDotProduct(randomize(majorX, majorY + 1), minorX, minorY - 1.0);
		double contribution10 = computeGradientDotProduct(randomize(majorX + 1, majorY), minorX - 1.0, minorY);
		double contribution11 = computeGradientDotProduct(randomize(majorX + 1, majorY + 1), minorX - 1.0, minorY - 1.0);
		
		// interpolate the contributions based on the eased exact position
		double interpolatedX0 = computeLinearInterpolation(contribution00, contribution01, easedY);
		double interpolatedX1 = computeLinearInterpolation(contribution10, contribution11, easedY);
		double interpolated = computeLinearInterpolation(interpolatedX0, interpolatedX1, easedX);
		
		return interpolated;
	}
	
	/**
	 * Computes a deterministic pseudo-random function for the specified grid position.
	 */
	private int randomize(int majorX, int majorY) {
		return permutationTable[(permutationTable[majorX & 0xff] + majorY) & 0xff];
	}
	
	/**
	 * Computes the noise contribution from a grid point. The randomGradientIndex specifies a randomized
	 * gradient index computed from the grid point itself. The minorX and minorY are the exact position
	 * relative to the grid point, used to compute the dot product between the gradient vector and the
	 * vector from the grid point to the exact point.
	 */
	private double computeGradientDotProduct(int randomGradientIndex, double minorX, double minorY) {
		
		// compute x/y indices
		int gradientIndexX = (randomGradientIndex >> 4) & 15;
		int gradientIndexY = randomGradientIndex & 15;
		
		// compute the gradient
		double gradientX = (gradientIndexX / 7.5) - 1.0;
		double gradientY = (gradientIndexY / 7.5) - 1.0;
		
		// compute the dot product
		return (gradientX * minorX + gradientY * minorY);
		
	}
	
	/**
	 * Computes the linear interpolation between the specified start and end
	 * values, with t giving the interpolation position in the range 0..1.
	 * @param start the start value
	 * @param end the end value
	 * @param t the interpolation position, in the range 0..1
	 * @return the interpolated value
	 */
	private double computeLinearInterpolation(double start, double end, double t) {
		return start + (end - start) * t;
	}
	
	/**
	 * Computes the easing interpolation curve at the specified position from
	 * the range 0..1. In this input range, the easing curve maps to the output
	 * range 0..1 in a strictly monotonically increasing way.
	 * 
	 * @param t the position, in the range 0..1
	 * @return the easing curve value, in the range 0..1
	 */
	private double computeEasingFunction(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

}
