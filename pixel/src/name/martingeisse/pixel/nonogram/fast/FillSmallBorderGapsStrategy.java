/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.pixel.nonogram.fast;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
 * Fills "gaps" between known empty cells at the border that
 * are too small for the border spans.
 */
public class FillSmallBorderGapsStrategy extends NonogramSlicingStrategy {

	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {
		if (primaryHints.length == 0) {
			return;
		}
		Boolean[] slice = getPrimarySlice(primaryLocation);
		
//		System.out.println("primary: " + primaryLocation);
		
		// first span
		{
			int spanLength = primaryHints[0];
//			System.out.println("first span length: " + spanLength);
			int marker = 0;
			for (int i=0; i < marker + spanLength; i++) {
//				System.out.println("* " + i);
				if (slice[i] != null) {
					if (slice[i]) {
						break;
					} else {
						marker = i;
					}
				}
			}
			for (int i=0; i<marker; i++) {
				setPixel(primaryLocation, i, false);
			}
		}
		
		// last span
		{
			int spanLength = primaryHints[primaryHints.length - 1];
//			System.out.println("last span length: " + spanLength);
			int marker = slice.length-1;
			for (int i=marker; i > marker - spanLength; i--) {
//				System.out.println("* " + i);
				if (slice[i] != null) {
					if (slice[i]) {
						break;
					} else {
						marker = i;
					}
				}
			}
			for (int i=marker+1; i<slice.length; i++) {
				setPixel(primaryLocation, i, false);
			}
			
		}
		
	}

	
}
