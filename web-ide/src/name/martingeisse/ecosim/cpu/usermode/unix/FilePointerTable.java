/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

/**
 * This table keeps the file pointers of a single process.
 * The current implementation allows up to N referenced
 * file pointers (i.e. up to N valid file descriptors)
 * per table, where N is specified as a constructor argument.
 */
public final class FilePointerTable implements Cloneable {

	/**
	 * the table
	 */
	private IFilePointer[] table;
	
	/**
	 * Constructor.
	 * @param tableSize the number of table entries
	 */
	public FilePointerTable(int tableSize) {
		if (tableSize < 0) {
			throw new IllegalArgumentException("table size is negative");
		}
		this.table = new IFilePointer[tableSize];
	}
	
	/**
	 * Copy constructor. Note that this constructor increases the
	 * reference count of all file pointers.
	 * 
	 * @param original the original instance to copy
	 */
	public FilePointerTable(FilePointerTable original) {
		this.table = original.table.clone();
		for (IFilePointer filePointer : table) {
			if (filePointer != null) {
				filePointer.acquireReference();
			}
		}
	}
	
	/**
	 * Returns the table entry at the specified index.
	 * Returns null for empty entries as well as indices
	 * outside the table range, since the actual table size
	 * is considered an implementation detail.
	 * 
	 * This method does not call acquireReference() on the
	 * file pointer. If the caller wants to keep the reference,
	 * it must do so itself.
	 * 
	 * @param index the index
	 * @return the entry or null
	 */
	public IFilePointer tryGetEntry(int index) {
		return (index >= 0 && index < table.length) ? table[index] : null; 
	}
	
	/**
	 * Returns the table entry at the specified index. If the
	 * index is outside the range of the table or contains
	 * a null value, then a {@link UnixSyscallException} is
	 * thrown.
	 * 
	 * This method does not call acquireReference() on the
	 * file pointer. If the caller wants to keep the reference,
	 * it must do so itself.
	 * 
	 * @param index the index
	 * @return the entry
	 * @throws UnixSyscallException if the entry wasn't found
	 */
	public IFilePointer getValidEntry(int index) throws UnixSyscallException {
		IFilePointer result = tryGetEntry(index);
		if (result == null) {
			throw new UnixSyscallException(UnixSyscallErrorCode.INVALID_FILE_DESCRIPTOR);
		}
		return result;
	}
	
	/**
	 * Replaces the entry at the specified index with a new value. The new
	 * entry receives a call to acquireReference() after it has been stored
	 * in the table (unless it is null). If the old entry was not null, it
	 * gets a call to releaseReference(). This order also guarantees that
	 * if the old and new entry are the same, the reference count does not
	 * temporarily drop to zero.
	 * 
	 * This also implies that whenever a caller first creates a new file
	 * pointer, then uses this method to store it in the table, the caller
	 * must release its own reference to the file pointer afterwards.
	 * 
	 * @param index the table index to store the new value at
	 * @param newValue the value to store
	 * @throws UnixSyscallException if the index is invalid
	 */
	public void replaceEntry(int index, IFilePointer newValue) throws UnixSyscallException {
		if (index < 0 || index >= table.length) {
			throw new UnixSyscallException(UnixSyscallErrorCode.INVALID_FILE_DESCRIPTOR);
		}
		IFilePointer oldValue = table[index];
		table[index] = newValue;
		if (newValue != null) {
			newValue.acquireReference();
		}
		if (oldValue != null) {
			oldValue.releaseReference();
		}
	}

	/**
	 * Finds an empty table entry, then stores the specified value in
	 * that entry. The value gets a call to acquireReference() after it
	 * has been stored in the table.
	 * 
	 * This also implies that whenever a caller first creates a new file
	 * pointer, then uses this method to store it in the table, the caller
	 * must release its own reference to the file pointer afterwards.
	 * 
	 * @param value the value to store (must not be null)
	 * @return the index at which the entry was stored
	 * @throws UnixSyscallException if the table is full
	 */
	public int insertEntry(IFilePointer value) throws UnixSyscallException {
		if (value == null) {
			throw new IllegalArgumentException("argument is null");
		}
		for (int i=0; i<table.length; i++) {
			if (table[i] == null) {
				table[i] = value;
				value.acquireReference();
				return i;
			}
		}
		throw new UnixSyscallException(UnixSyscallErrorCode.FILE_POINTER_TABLE_FULL);
	}

	/**
	 * Duplicates the file pointer at the specified index.
	 * @param index the source index
	 * @return the index of the generated duplicate entry
	 * @throws UnixSyscallException if the index is invalid or if the table is full
	 */
	public int dup(int index) throws UnixSyscallException {
		return insertEntry(getValidEntry(index));
	}

	/**
	 * Closes a table entry.
	 * @param index the index of the file pointer to close
	 * @throws UnixSyscallException if the index is invalid
	 */
	public void close(int index) throws UnixSyscallException {
		getValidEntry(index); // just to check that index is valid
		replaceEntry(index, null);
	}

	/**
	 * Releases all file pointers as well as the internal table. This method
	 * should only be called when this object is no longer needed.
	 */
	public void dispose() {
		for (IFilePointer filePointer : table) {
			if (filePointer != null) {
				filePointer.releaseReference();
			}
		}
		table = null;
	}
	
}
