/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.util;

/**
 * {@link CharSequence} adapter for char arrays.
 */
public final class CharArrayCharSequence implements CharSequence {

	/**
	 * the array
	 */
	private final char[] array;
	
	/**
	 * the start
	 */
	private final int start;
	
	/**
	 * the length
	 */
	private final int length;

	/**
	 * Constructor.
	 * @param array the char array to wrap
	 */
	public CharArrayCharSequence(char[] array) {
		this(array, 0, array.length);
	}
	
	/**
	 * Constructor.
	 * @param array the char array to wrap
	 * @param start the index of the first character of the sequence
	 * @param length the length of the sequence
	 */
	public CharArrayCharSequence(char[] array, int start, int length) {
		this.array = array;
		this.start = start;
		this.length = length;
		if (start < 0 || start + length > array.length) {
			throw new IndexOutOfBoundsException("invalid range: range start: " + start + ", range length: " + length + "; array length: " + array.length);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	@Override
	public char charAt(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("index " + index + " out of bounds; length: " + length);
		}
		return array[start + index];
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	@Override
	public int length() {
		return length;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	@Override
	public CharSequence subSequence(int start, int end) {
		if (start < 0 || end < start || end > this.length) {
			throw new IndexOutOfBoundsException("invalid subrange. subrange start: " + start + ", subrange end: " + end + "; parent length: " + length);
		}
		return new CharArrayCharSequence(array, this.start + start, end - start);
	}
	
}
