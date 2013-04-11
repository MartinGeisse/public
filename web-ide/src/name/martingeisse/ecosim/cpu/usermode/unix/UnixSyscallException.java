/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

/**
 * This exception type indicates an error that occurred
 * during a Unix syscall and should be reported to the
 * usermode program via an error code.
 */
public class UnixSyscallException extends Exception {

	/**
	 * the errorCode
	 */
	private UnixSyscallErrorCode errorCode;
	
	/**
	 * Constructor.
	 * @param errorCode the error code
	 */
	public UnixSyscallException(UnixSyscallErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * Getter method for the errorCode.
	 * @return the errorCode
	 */
	public UnixSyscallErrorCode getErrorCode() {
		return errorCode;
	}
	
}
