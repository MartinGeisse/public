/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.util;

/**
 * Represents an array of boolean values that use indices
 * 0 through n-1 as usual, but whose arrangement is
 * (n-1, ..., 0) and which are typically interpreted as numbers
 * in a big-endian way.
 * 
 * Note: the current implementation cannot handle sizes larger
 * than 64 bits.
 */
public final class BitVector {

	/**
	 * the size
	 */
	private final int size;
	
	/**
	 * the value
	 */
	private final long value;

	/**
	 * Constructor.
	 * @param size the size in bits
	 * @param value the value -- only the lowest (size) bits will
	 * be used
	 */
	public BitVector(int size, long value) {
		if (size < 0 || size >= 64) {
			throw new IllegalArgumentException("invalid BitVector size: " + size);
		}
		this.size = size;
		this.value = value;
	}

	/**
	 * Getter method for the size.
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Gets the value of this vector as a signed long int.
	 * 
	 * @return the value
	 */
	public long getLongValueSigned() {
		
	}
	
	/**
	 * Gets the value of this vector as an unsigned long int.
	 * 
	 * Throws an exception if the size is 64 since not all unsigned 64-bit
	 * values can be represented by the type long in Java. This happens even
	 * if the actual value *could* be represented by type long. 
	 * 
	 * @return the value
	 */
	public long getLongValueUnsigned() {
		
	}
	
	/**
	 * Gets the value of this vector as a signed int.
	 * 
	 * Throws an exception if the size is greater than 32 since
	 * not all such values can be represented by the type int in Java.
	 * This happens even if the actual value *could* be represented by type int.
	 * 
	 * @return the value
	 */
	public int getIntValueSigned() {
		
	}
	
	/**
	 * Gets the value of this vector as an unsigned int.
	 * 
	 * Throws an exception if the size is greater than 31 since
	 * not all such values can be represented by the type int in Java.
	 * This happens even if the actual value *could* be represented by type int.
	 * 
	 * @return the value
	 */
	public int getIntValueUnsigned() {
		
	}
	
	/**
	 * Gets the value of this vector as a signed short int.
	 * 
	 * Throws an exception if the size is greater than 16 since
	 * not all such values can be represented by the type short in Java.
	 * This happens even if the actual value *could* be represented by type short.
	 * 
	 * @return the value
	 */
	public short getShortValueSigned() {
		
	}
	
	/**
	 * Gets the value of this vector as an unsigned short int.
	 * 
	 * Throws an exception if the size is greater than 15 since
	 * not all such values can be represented by the type short in Java.
	 * This happens even if the actual value *could* be represented by type short.
	 * 
	 * @return the value
	 */
	public short getShortValueUnsigned() {
		
	}
	
	/**
	 * Gets the value of this vector as a signed byte.
	 * 
	 * Throws an exception if the size is greater than 8 since
	 * not all such values can be represented by the type byte in Java.
	 * This happens even if the actual value *could* be represented by type byte.
	 * 
	 * @return the value
	 */
	public byte getByteValueSigned() {
		
	}
	
	/**
	 * Gets the value of this vector as an unsigned byte.
	 * 
	 * Throws an exception if the size is greater than 7 since
	 * not all such values can be represented by the type byte in Java.
	 * This happens even if the actual value *could* be represented by type byte.
	 * 
	 * @return the value
	 */
	public byte getByteValueUnsigned() {
		
	}
	
	/**
	 * Gets a single bit from this vector.
	 * @param position the bit position
	 * @return the bit value
	 */
	public boolean getBit(int position) {
		
	}

	/**
	 * Gets a slice from this vector.
	 * @param highestPosition the highest bit position to include in the slice
	 * @param lowestPosition the lowest bit position to include in the slice
	 * @return the slice
	 */
	public BitVector getSlice(int highestPosition, int lowestPosition) {
		
	}

	/**
	 * Shifts left by one bit position.
	 * @param mode the shift mode
	 * @return the resulting bit vector
	 */
	public BitVector shiftLeft(ShiftMode mode) {
		
	}

	/**
	 * Shifts left by the specified number of bit positions.
	 * @param mode the shift mode
	 * @return the resulting bit vector
	 */
	public BitVector shiftLeft(ShiftMode mode, int n) {
		
	}
	
	/**
	 * Shifts right by one bit position.
	 * @param mode the shift mode
	 * @return the resulting bit vector
	 */
	public BitVector shiftRight(ShiftMode mode) {
		
	}
	
	/**
	 * Shifts right by the specified number of bit positions.
	 * @param mode the shift mode
	 * @return the resulting bit vector
	 */
	public BitVector shiftRight(ShiftMode mode, int n) {
		
	}
	
}
