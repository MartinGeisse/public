/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.pixel.nonogram.misc;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy;

/**
 * Strategy that handles each slice by trying all possible combinations
 * of the spans in that slice, then merging the results.
 */
public class SliceSpanCombinationsStrategy extends NonogramSlicingStrategy {

	/**
	 * the target
	 */
	private Boolean[] target;
	
	/* (non-Javadoc)
	 * @see name.martingeisse.pixel.nonogram.common.NonogramSlicingStrategy#handleSlice(int, int[])
	 */
	@Override
	protected void handleSlice(int primaryLocation, int[] primaryHints) {
		
		// 
		if (primaryHints.length == 0) {
			return;
		}
		Boolean[] slice = getPrimarySlice(primaryLocation);
		
		// find possible span starts, taking the existing slice into account but not
		// the interaction with other spans
		@SuppressWarnings("unchecked")
		List<Integer>[] possibleSpanStarts = (List<Integer>[])(new List<?>[primaryHints.length]);
		for (int i=0; i<possibleSpanStarts.length; i++) {
			int spanLength = primaryHints[i];
			possibleSpanStarts[i] = new ArrayList<Integer>();
			
			// loop over possible start locations and add all to the list that are compatible with the existing slice
			possibleStarts: for (int j=0; j<slice.length - spanLength; j++) {
				
				// check for collision of the current start location with the existing slice
				for (int k=0; k<spanLength; k++) {
					Boolean existing = slice[j + k];
					if (existing != null && !existing) {
						continue possibleStarts;
					}
				}
				
				// check start delimiter except for the first span
				if (i > 0) {
					if (j < 2) {
						continue possibleStarts;
					}
					Boolean existing = slice[j - 1];
					if (existing != null && existing) {
						continue possibleStarts;
					}
				}

				// check end delimiter except for the last span
				if (i < primaryHints.length - 1) {
					if (j >= slice.length - 2) {
						continue possibleStarts;
					}
					Boolean existing = slice[j + spanLength];
					if (existing != null && existing) {
						continue possibleStarts;
					}
				}

				possibleSpanStarts[i].add(j);
			}
		}
		
		// 
		System.out.println("slice: " + (isTransposed() ? "column" : "row") + " " + primaryLocation);
		System.out.print("existing: ");
		for (int i=0; i<slice.length; i++) {
			Boolean existing = slice[i];
			System.out.print(existing == null ? '.' : existing ? '#' : 'x');
		}
		System.out.println();
		for (int i=0; i<primaryHints.length; i++) {
			System.out.print("span #" + i + ": ");
			for (int start : possibleSpanStarts[i]) {
				System.out.print("" + start + " ");
			}
			System.out.println();
		}
		
		target = null;
		findRecursive(slice, primaryHints, possibleSpanStarts, new int[primaryHints.length], 0);
		System.out.print("target: ");
		if (target == null) {
			System.out.println("null");
		} else {
			for (int i=0; i<slice.length; i++) {
				System.out.print("" + (target[i] == null ? '.' : target[i] ? '#' : 'x'));
			}
			System.out.println();
		}
		
		
//		if (target != null) {
//			for (int i=0; i<slice.length; i++) {
//				patchPixel(primaryLocation, i, target[i]);
//			}
//		}
		
		
		System.out.println();
	}

	/**
	 * 
	 */
	private void findRecursive(Boolean[] slice, int[] spanLengths, List<Integer>[] possibleSpanStarts, int[] actualSpanStarts, int consideredSpans) {
		
		// stop condition
		if (consideredSpans == actualSpanStarts.length) {
			
			// build possible solution
			Boolean[] possibleSolution = slice.clone();
			for (int i=0; i<actualSpanStarts.length; i++) {
				int start = actualSpanStarts[i];
				if (i > 0) {
					possibleSolution[start - 1] = false;
				}
				if (i < actualSpanStarts.length - 1) {
					possibleSolution[start + spanLengths[i]] = false;
				}
				for (int j=0; j<spanLengths[i]; j++) {
					possibleSolution[start + j] = true;
				}
			}
			
			// merge into target
			if (target == null) {
				target = possibleSolution;
			} else {
				for (int i=0; i<slice.length; i++) {
					if (target[i] != null) {
						if (!target[i].equals(possibleSolution[i])) {
							target[i] = null;
						}
					}
				}
			}
			
			return;
		}
		
		// try all locations for the next span
		int minStart = (consideredSpans == 0 ? 0 : (actualSpanStarts[consideredSpans - 1] + spanLengths[consideredSpans - 1] + 1));
		for (Integer possibleSpanStart : possibleSpanStarts[consideredSpans]) {
			if (possibleSpanStart < minStart) {
				continue;
			}
			actualSpanStarts[consideredSpans] = possibleSpanStart;
			findRecursive(slice, spanLengths, possibleSpanStarts, actualSpanStarts, consideredSpans + 1);
		}
		
	}

}
