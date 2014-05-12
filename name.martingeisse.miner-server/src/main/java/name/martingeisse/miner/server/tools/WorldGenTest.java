/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;
import name.martingeisse.stackd.common.util.PerlinNoise;

/**
 *
 */
public class WorldGenTest {

	/**
	 * @param args ...
	 * @throws Exception ... 
	 */
	public static void main(String[] args) throws Exception {
		
		// height field parameters
		int radius = 2000;
		int size = 2*radius;
		double amplitude = 200.0;
		double wavelength = radius;
		int octaveCount = 10;
		double octaveFactor = 0.4;
		double[] values = new double[size * size];
		
		// generate height field
		for (int octave = 0; octave < octaveCount; octave++) {
			PerlinNoise.seed();
			for (int x=0; x<size; x++) {
				for (int y=0; y<size; y++) {
					double contribution = PerlinNoise.computeNoise((x - radius) / wavelength, (y - radius) / wavelength) * amplitude;
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
				int height = (int)values[y * size + x];
				height = (height + 0x80);
				if (height < 0) {
					height = 0;
				} else if (height > 255) {
					height = 255;
				}
				for (int band=0; band<3; band++) {
					raster.setSample(x, y, band, height);
				}
				raster.setSample(x, y, 3, 255);
			}
		}
		FileOutputStream fileOutputStream = new FileOutputStream(new File("world.png"));
		ImageIO.write(bufferedImage, "png", fileOutputStream);
		fileOutputStream.close();
		
	}

}
