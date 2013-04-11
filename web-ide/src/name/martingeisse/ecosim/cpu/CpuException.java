/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * This class represents an "exception" in the sense defined by the ECO32.
 * The exception uses a code in the range 0..15 (currently used: 0..9)
 * to denote the exception cause. The interrupt/exception priority
 * stored in the PSW on handler entry is equal to (code + 16).
 */
public class CpuException extends Exception {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bus timeout (no device attached to this address)
	 */
	public static final int CODE_BUS_TIMEOUT = 0;

	/**
	 * opcode does not denote an instruction
	 */
	public static final int CODE_ILLEGAL_INSTRUCTION = 1;

	/**
	 * this instruction can only be executed in kernel mode, but the cpu is in user mode
	 */
	public static final int CODE_PRIVILEGED_INSTRUCTION = 2;

	/**
	 * division by zero
	 */
	public static final int CODE_DIVISION = 3;

	/**
	 * trap (syscall) instruction executed
	 */
	public static final int CODE_TRAP = 4;

	/**
	 * TLB miss (no TLB entry found for page-mapped address)
	 */
	public static final int CODE_TLB_MISS = 5;

	/**
	 * TLB entry found and valid, but is write-protected for a write operation
	 */
	public static final int CODE_TLB_WRITE = 6;

	/**
	 * TLB entry found, but is marked invalid
	 */
	public static final int CODE_TLB_INVALID = 7;

	/**
	 * misaligned read or write operation
	 */
	public static final int CODE_ILLEGAL_ADDRESS = 8;

	/**
	 * read or write operation to an address that can only be accessed in kernel
	 * mode, but the cpu is in user mode
	 */
	public static final int CODE_PRIVILEGED_ADDRESS = 9;
	
	/**
	 * the names
	 */
	private static final String[] names = {
		"bus timeout",
		"illegal instruction",
		"privileged instruction",
		"division exception",
		"trap",
		"TLB miss",
		"TLB write",
		"TLB invalid",
		"illegal address",
		"privileged address",
		"--- invalid ---",
		"--- invalid ---",
		"--- invalid ---",
		"--- invalid ---",
		"--- invalid ---",
		"--- invalid ---",
	};

	/**
	 * the code
	 */
	private int code;
	
	/**
	 * Constructor
	 * @param code the exception code
	 */
	public CpuException(int code) {
		super();
		if (code < 0 || code > 9) {
			throw new IllegalArgumentException("invalid exception code: " + code);
		}
		this.code = code;
	}

	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return Returns the exception name for this exception.
	 */
	public String getName() {
		return getNameForCode(getCode());
	}
	
	/**
	 * Returns the exception name for the specified code
	 * @param code the exception code
	 * @return Returns the exception name.
	 */
	public static String getNameForCode(int code) {
		return names[code];
	}

}
