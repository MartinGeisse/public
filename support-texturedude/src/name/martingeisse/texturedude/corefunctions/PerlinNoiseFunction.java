/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;
import java.util.Random;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;
import name.martingeisse.texturedude.Layer;
import name.martingeisse.texturedude.TextureDude;

/**
 * Arguments:
 * 		color1 (R, G, B),
 * 		color2 (R, G, B),
 * 		wavelength,
 * 		amplitude,
 * 		seed
 * Input layers: layer
 * Output values: layer
 *
 * Fills the whole layer with Perlin noise.
 */
public final class PerlinNoiseFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final PerlinNoiseFunction INSTANCE = new PerlinNoiseFunction();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		TextureDude dude = host.getDude();
		Layer layer = dude.getLayerStack().peekReference();
		byte[] pixels = layer.getPixels();
		
		int r1 = host.fetchArgumentByte(), g1 = host.fetchArgumentByte(), b1 = host.fetchArgumentByte();
		int r2 = host.fetchArgumentByte(), g2 = host.fetchArgumentByte(), b2 = host.fetchArgumentByte();
		double wavelength = host.fetchArgumentByte() * 0.55;
		double amplitude = host.fetchArgumentByte() * 0.05;
		int seed = host.fetchArgumentByte();
		
		int[] permutationTable = computePermutationTable(seed);
		for (int x=0; x<layer.getWidth(); x++) {
			for (int y=0; y<layer.getHeight(); y++) {
				double perlinValue = computeNoise(permutationTable, x / wavelength, y / wavelength) * amplitude;
				double factor = (perlinValue + 1.0) / 2.0;
				factor = (factor < 0.0 ? 0.0 : factor > 1.0 ? 1.0 : factor);
				int baseIndex = (y * layer.getWidth() + x) * 4;
				pixels[baseIndex + 0] = (byte)(r1 + (r2 - r1) * factor);
				pixels[baseIndex + 1] = (byte)(g1 + (g2 - g1) * factor);
				pixels[baseIndex + 2] = (byte)(b1 + (b2 - b1) * factor);
				pixels[baseIndex + 3] = (byte)255;
			}
		}
		
	}
	
	/**
	 * Generates the permutation table for the noise function.
	 */
	private static int[] computePermutationTable(long seed) {
		Random random = new Random(seed);
		int[] permutationTable = new int[256];
		for (int i=0; i<256; i++) {
			permutationTable[i] = i;
		}
		for (int i=255; i>=0; i--) {
			int j = random.nextInt(i + 1);
			int temp = permutationTable[i];
			permutationTable[i] = permutationTable[j];
			permutationTable[j] = temp;
		}
		return permutationTable;
	}
	
	/**
	 * Computes the value of the 2d Perlin Noise function at the specified position.
	 * The granularity of the noise is around 1.0, randomly a bit more or less, and
	 * repeats every 256.0 units.
	 * 
	 * @param permutationTable the pre-computed permutation table
	 * @param x the x position
	 * @param y the y position
	 * @return the noise function value
	 */
	private static double computeNoise(int[] permutationTable, double x, double y) {
		
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
		double contribution00 = computeGradientDotProduct(randomize(permutationTable, majorX, majorY), minorX, minorY);
		double contribution01 = computeGradientDotProduct(randomize(permutationTable, majorX, majorY + 1), minorX, minorY - 1.0);
		double contribution10 = computeGradientDotProduct(randomize(permutationTable, majorX + 1, majorY), minorX - 1.0, minorY);
		double contribution11 = computeGradientDotProduct(randomize(permutationTable, majorX + 1, majorY + 1), minorX - 1.0, minorY - 1.0);
		
		// interpolate the contributions based on the eased exact position
		double interpolatedX0 = computeLinearInterpolation(contribution00, contribution01, easedY);
		double interpolatedX1 = computeLinearInterpolation(contribution10, contribution11, easedY);
		double interpolated = computeLinearInterpolation(interpolatedX0, interpolatedX1, easedX);
		
		return interpolated;
	}
	
	/**
	 * Computes a deterministic pseudo-random function for the specified grid position.
	 */
	private static int randomize(int[] permutationTable, int majorX, int majorY) {
		return permutationTable[(permutationTable[majorX & 0xff] + majorY) & 0xff];
	}
	
	/**
	 * Computes the noise contribution from a grid point. The randomGradientIndex specifies a randomized
	 * gradient index computed from the grid point itself. The minorX and minorY are the exact position
	 * relative to the grid point, used to compute the dot product between the gradient vector and the
	 * vector from the grid point to the exact point.
	 */
	private static double computeGradientDotProduct(int randomGradientIndex, double minorX, double minorY) {
		
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
	private static double computeLinearInterpolation(double start, double end, double t) {
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
	private static double computeEasingFunction(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
}
