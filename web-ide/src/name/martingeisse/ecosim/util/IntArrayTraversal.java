/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * This class takes an existing int array or creates a new one, then
 * steps through its elements in sequential order. A field of this
 * class -- the cursor -- keeps the current position, which conceptually
 * lies between two array elements. The processing methods provide ways
 * to access the element following the cursor and/or move the cursor to
 * the next position.
 */
public class IntArrayTraversal {

	/**
	 * the array
	 */
	private int[] array;

	/**
	 * the cursor
	 */
	private int cursor;

	/**
	 * Constructor for a new int array of the specified size.
	 * @param size the size of the array to create
	 */
	public IntArrayTraversal(int size) {
		this(new int[size]);
	}

	/**
	 * Constructor for an existing int array.
	 * @param array the array to traverse
	 */
	public IntArrayTraversal(int[] array) {
		this.array = array;
		this.cursor = 0;
	}

	/**
	 * @return Returns the array.
	 */
	public int[] getArray() {
		return array;
	}

	/**
	 * @return Returns the cursor.
	 */
	public int getCursor() {
		return cursor;
	}

	/**
	 * Sets the cursor.
	 * @param cursor the new value to set. Must be in
	 * the range (0..array.length).
	 */
	public void setCursor(int cursor) {
		if (cursor < 0 || cursor > array.length) {
			throw new IllegalArgumentException("invalid cursor position: " + cursor + ", array size: " + array.length);
		}
		this.cursor = cursor;
	}

	/**
	 * @return Returns true if the cursor is at the beginning (i.e. just before
	 * the first array element), false if not.
	 */
	public boolean isAtBeginning() {
		return cursor == 0;
	}

	/**
	 * @return Returns true if the cursor is at the end (i.e. just after
	 * the last array element), false if not.
	 */
	public boolean isAtEnd() {
		return cursor == array.length;
	}

	/**
	 * Moves the cursor to the position before the first array element.
	 */
	public void setCursorToBeginning() {
		cursor = 0;
	}

	/**
	 * Moves the cursor to the position after the last array element.
	 */
	public void setCursorToEnd() {
		cursor = array.length;
	}

	/**
	 * @return Returns an exception that complains about the cursor being at
	 * the beginning of the traversed array.
	 */
	private IllegalStateException createAtBeginningError() {
		return new IllegalStateException("the cursor is at the beginning of the array");
	}

	/**
	 * Ensures that the cursor is not at the beginning of the array, and throws
	 * an {@link IllegalArgumentException} with an appropriate message otherwise.
	 */
	private void ensureNotAtBeginning() {
		if (cursor == 0) {
			throw createAtBeginningError();
		}
	}

	/**
	 * @return Returns an exception that complains about the cursor being at
	 * the end of the traversed array.
	 */
	private IllegalStateException createAtEndError() {
		return new IllegalStateException("the cursor is at the end of the array");
	}

	/**
	 * Ensures that the cursor is not at the end of the array, and throws
	 * an {@link IllegalArgumentException} with an appropriate message otherwise.
	 */
	private void ensureNotAtEnd() {
		if (cursor == array.length) {
			throw createAtEndError();
		}
	}

	/**
	 * Moves the cursor forward by one position and returns the array
	 * element across which it was moved.
	 * @return Returns the read element.
	 */
	public int read() {
		ensureNotAtEnd();
		int value = array[cursor];
		cursor++;
		return value;
	}

	/**
	 * Returns the array element that would be returned by read(), but
	 * does not move the cursor.
	 * @return Returns the read element.
	 */
	public int readDontMove() {
		ensureNotAtEnd();
		return array[cursor];
	}

	/**
	 * Moves the cursor backward by one position and returns the array
	 * element across which it was moved.
	 * @return Returns the read element.
	 */
	public int readBackwards() {
		ensureNotAtBeginning();
		cursor--;
		return array[cursor];
	}

	/**
	 * Returns the array element that would be returned by readBackwards(), but
	 * does not move the cursor.
	 * @return Returns the read element.
	 */
	public int readBackwardsDontMove() {
		ensureNotAtBeginning();
		return array[cursor - 1];
	}

	/**
	 * Moves the cursor forward by one position and places the specified
	 * value into the array element across which it was moved.
	 * @param value the value to write into the array element
	 */
	public void write(int value) {
		ensureNotAtEnd();
		array[cursor] = value;
		cursor++;
	}

	/**
	 * Places the specified value into the array cell following the cursor
	 * just as write() would do, but does not move the cursor.
	 * @param value the value to write into the array element
	 */
	public void writeDontMove(int value) {
		ensureNotAtEnd();
		array[cursor] = value;
	}

	/**
	 * Moves the cursor backward by one position and places the specified
	 * value into the array element across which it was moved.
	 * @param value the value to write into the array element
	 */
	public void writeBackwards(int value) {
		ensureNotAtBeginning();
		cursor--;
		array[cursor] = value;
	}

	/**
	 * Places the specified value into the array cell preceding the cursor
	 * just as writeBackwards() would do, but does not move the cursor.
	 * @param value the value to write into the array element
	 */
	public void writeBackwardsDontMove(int value) {
		ensureNotAtBeginning();
		array[cursor - 1] = value;
	}

	/**
	 * Moves the cursor forward across elements for which the specified predicate
	 * returns true, and stops at the end of the array or just before crossing the first
	 * element for which the predicate returns false. 
	 * @param predicate the predicate that determines which elements to skip
	 */
	public void skip(IIntPredicate predicate) {
		while (cursor < array.length && predicate.evaluate(array[cursor])) {
			cursor++;
		}
	}

	/**
	 * Moves the cursor backward across elements for which the specified predicate
	 * returns true, and stops at the beginning of the array or just before crossing the first
	 * element for which the predicate returns false (i.e. just after that element in
	 * normal traversal order).
	 * @param predicate the predicate that determines which elements to skip
	 */
	public void skipBackward(IIntPredicate predicate) {
		while (cursor > 0 && predicate.evaluate(array[cursor - 1])) {
			cursor--;
		}
	}
	
	/**
	 * @return Returns an array containing the elements before the cursor.
	 */
	public int[] getSubArrayBeforeCursor() {
		int[] result = new int[cursor];
		System.arraycopy(array, 0, result, 0, cursor);
		return result;
	}

	/**
	 * @return Returns an array containing the elements after the cursor.
	 */
	public int[] getSubArrayAfterCursor() {
		int[] result = new int[array.length - cursor];
		System.arraycopy(array, cursor, result, 0, array.length - cursor);
		return result;
	}

}
