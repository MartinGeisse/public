/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * This class contains static helper methods to deal with buffers.
 */
public class BufferTools {

	/**
	 * Reads a byte from a buffer and sign-extends it to 32 bits.
	 * @param buffer the buffer to read from
	 * @param address the address to read from
	 * @return Returns the sign-extended byte value from the specified address.
	 */
	public static int readSigned8(byte[] buffer, int address) {
		int value = buffer[address];
		return (byte)value;
	}

	/**
	 * Reads a byte from a buffer and zero-extends it to 32 bits.
	 * @param buffer the buffer to read from
	 * @param address the address to read from
	 * @return Returns the zero-extended byte value from the specified address.
	 */
	public static int readUnsigned8(byte[] buffer, int address) {
		int value = buffer[address];
		return value & 0xff;
	}
	
	/**
	 * Writes an 8-bit value to a buffer.
	 * @param buffer the buffer to write to
	 * @param address the address to write to
	 * @param value the value to write
	 */
	public static void write8(byte[] buffer, int address, int value) {
		buffer[address] = (byte)value;
	}
	
	/**
	 * Reads a big-endian 32-bit value from a buffer. This method allows mis-aligned
	 * accesses. However, the acces must not exceed the buffer size.
	 * @param buffer the buffer to read from
	 * @param address the start address
	 * @return Returns the big-endian value from that address.
	 */
	public static int readBigEndian32(byte[] buffer, int address) {
		int a = readUnsigned8(buffer, address);
		int b = readUnsigned8(buffer, address + 1);
		int c = readUnsigned8(buffer, address + 2);
		int d = readUnsigned8(buffer, address + 3);
		return (a << 24) + (b << 16) + (c << 8) + d;
	}

	/**
	 * Reads a little-endian 32-bit value from a buffer. This method allows mis-aligned
	 * accesses. However, the acces must not exceed the buffer size.
	 * @param buffer the buffer to read from
	 * @param address the start address
	 * @return Returns the little-endian value from that address.
	 */
	public static int readLittleEndian32(byte[] buffer, int address) {
		int a = readUnsigned8(buffer, address);
		int b = readUnsigned8(buffer, address + 1);
		int c = readUnsigned8(buffer, address + 2);
		int d = readUnsigned8(buffer, address + 3);
		return a + (b << 8) + (c << 16) + (d << 24);
	}
	
	/**
	 * Writes a big-endian 32-bit value to a buffer. This method allows mis-aligned
	 * accesses. However, the acces must not exceed the buffer size.
	 * @param buffer the buffer to write to
	 * @param address the address to write to
	 * @param value the value to write
	 */
	public static void writeBigEndian32(byte[] buffer, int address, int value) {
		write8(buffer, address + 0, value >> 24);
		write8(buffer, address + 1, value >> 16);
		write8(buffer, address + 2, value >> 8);
		write8(buffer, address + 3, value);
	}

	/**
	 * Writes a little-endian 32-bit value to a buffer. This method allows mis-aligned
	 * accesses. However, the acces must not exceed the buffer size.
	 * @param buffer the buffer to write to
	 * @param address the address to write to
	 * @param value the value to write
	 */
	public static void writeLittleEndian32(byte[] buffer, int address, int value) {
		write8(buffer, address + 0, value);
		write8(buffer, address + 1, value >> 8);
		write8(buffer, address + 2, value >> 16);
		write8(buffer, address + 3, value >> 24);
	}

}
