/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import name.martingeisse.stackd.common.util.PerlinNoise;

/**
 *
 */
public class CaveTest {

	/**
	 * @param args ...
	 * @throws Exception ... 
	 */
	public static void main(String[] args) throws Exception {
		
		// height field parameters
		int radius = 200;
		int size = 2*radius;
		double amplitude = 200.0;
		double wavelength = radius / 4;
		int octaveCount = 5;
		double octaveFactor = 0.4;
		double[] values = new double[size * size];
		
		// generate height field
		for (int octave = 0; octave < octaveCount; octave++) {
			PerlinNoise noise = new PerlinNoise(octave);
			for (int x=0; x<size; x++) {
				for (int y=0; y<size; y++) {
					double contribution = noise.computeNoise((x - radius) / wavelength, (y - radius) / wavelength) * amplitude;
					values[y * size + x] += contribution;
				}
			}
			amplitude *= octaveFactor;
			wavelength /= 2.0;
		}
		
		// generate image file
		BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		WritableRaster raster = bufferedImage.getRaster();
		for (int x=0; x<size; x++) {
			for (int y=0; y<size; y++) {

				int perlinValue1 = (int)values[y * size + x];
				int perlinValue2 = (int)values[((x + 50) % size) * size + y];
				int value = (perlinValue1 + perlinValue2) / 2;
				int brightness = (value < 30 ? 255 : 0);
				
				int r = brightness & 0xff;
				int g = brightness & 0xff;
				int b = brightness & 0xff;
				
				raster.setSample(x, y, 0, r);
				raster.setSample(x, y, 1, g);
				raster.setSample(x, y, 2, b);
				raster.setSample(x, y, 3, 255);
			}
		}
		new ImagePreviewFrame(bufferedImage).show();
//		try (FileOutputStream fileOutputStream = new FileOutputStream(new File("world.png"))) {
//			ImageIO.write(bufferedImage, "png", fileOutputStream);
//		}
		
	}

}
