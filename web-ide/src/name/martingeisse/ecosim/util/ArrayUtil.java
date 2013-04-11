/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

import java.lang.reflect.Array;

/**
 * 
 */
public class ArrayUtil {

	/**
	 * Returns the argument, cast to type T[] in an unsafe way.
	 * @param <T> the static element type of the array type to cast to
	 * @param a the value to cast
	 * @return Returns the argument, cast to type T[].
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] unsafeCastArray(Object[] a) {
		return (T[])a;
	}
	
	/**
	 * Returns an array of the same size as the indexArray and the class of
	 * the originalArray which contains the elements of the original array
	 * selected by the corresponding indices.
	 * @param <T> the static type of the original array
	 * @param originalArray the original array form which objects are selected
	 * @param indexArray the array of indices used to select elements of the
	 * original array
	 * @return Returns the selected elements.
	 */
	public static <T> T[] selectByIndexArray(T[] originalArray, int[] indexArray) {
		Object[] untypedSelectedElements = (Object[])Array.newInstance(originalArray.getClass().getComponentType(), indexArray.length);
		T[] selectedElements = ArrayUtil.<T>unsafeCastArray(untypedSelectedElements);
		for (int i=0; i<indexArray.length; i++) {
			selectedElements[i] = originalArray[indexArray[i]];
		}
		return selectedElements;
	}

	/**
	 * Returns an array of the same size as the indexArray and the class of
	 * the originalArray which contains the elements of the original array
	 * selected by the corresponding indices.
	 * @param originalArray the original array form which objects are selected
	 * @param indexArray the array of indices used to select elements of the
	 * original array
	 * @return Returns the selected elements.
	 */
	public static int[] selectByIndexArray(int[] originalArray, int[] indexArray) {
		int[] selectedElements = (int[])Array.newInstance(Integer.TYPE, indexArray.length);
		for (int i=0; i<indexArray.length; i++) {
			selectedElements[i] = originalArray[indexArray[i]];
		}
		return selectedElements;
	}

}
