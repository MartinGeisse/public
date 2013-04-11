/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;


/**
 * The memory management unit.
 */
public interface IMemoryManagementUnit {

	/**
	 * This value is used to denote an invalid TLB index. For example, the TBS
	 * instruction results in this value if no entry can be found for a
	 * virtual address.
	 */
	public static int INVALID_TLB_INDEX = 0x80000000;

	/**
	 * The bit mask for the WRITE bit of a TLB entry low part.
	 */
	public static int TLB_ENTRY_WRITE_BIT = 2;

	/**
	 * The bit mask for the VALID bit of a TLB entry low part.
	 */
	public static int TLB_ENTRY_VALID_BIT = 1;

	/**
	 * @return Returns the userInterface.
	 */
	public ICpuUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(ICpuUserInterface userInterface);

	/**
	 * Returns the high part of a TLB entry.
	 * @param index the index of the entry
	 * @return Returns the high part of the specified entry.
	 */
	public int getTlbEntryHigh(int index);

	/**
	 * Returns the low part of a TLB entry.
	 * @param index the index of the entry
	 * @return Returns the low part of the specified entry.
	 */
	public int getTlbEntryLow(int index);

	/**
	 * Sets the value of a TLB entry.
	 * @param index the index of the entry to set
	 * @param highValue the high part of the entry value to set
	 * @param lowValue the low part of the entry value to set
	 * @param notifyUserInterface whether the onWriteTlb() method of the user interface shall be invoked
	 */
	public void setTlbEntry(int index, int highValue, int lowValue, boolean notifyUserInterface);

	/**
	 * @return Returns the randomCounter.
	 */
	public int getRandomCounter();

	/**
	 * Sets the randomCounter. The argument must be in the range 4..31.
	 * @param randomCounter the new value to set
	 */
	public void setRandomCounter(int randomCounter);

	/**
	 * This method is normally invoked on every page-mapped bus operation and
	 * cycles the random counter through the indices 4..31.
	 */
	public void updateRandomCounter();

	/**
	 * Finds a TLB entry for the specified virtual address and returns its index.
	 * @param virtualAddress the virtual address to look up.
	 * @return Returns the TLB index of the entry for that address, or
	 * INVALID_TLB_INDEX if no matching entry can be found.
	 */
	public int findTlbEntry(int virtualAddress);

	/**
	 * Maps the specified virtual address to a physical address. Throws a
	 * user TLB miss or kernel TLB miss exception (whichever is appropriate)
	 * if there is no matching entry. This method has a side-effect on the
	 * random index mechanism. NOTE: Currently throws a runtime exception
	 * for all mapped (non-direct) addresses.
	 * @param virtualAddress the virtual address to map
	 * @param write whether a write or read access is mapped. Write operations
	 * trigger an exception when an entry is found that 
	 * @return Returns the corresponding physical address.
	 * @throws CpuException for TLB misses
	 */
	public int mapAddressForCpu(int virtualAddress, boolean write) throws CpuException;

	/**
	 * Maps the specified virtual address to a physical address. This method
	 * ignores the write and valid flags of TLB entries, since such flags
	 * typically do not mean that the mapping is invalid, but rather that
	 * events shall be triggered on access. The random counter is not
	 * affected by this method. Throws a {@link MemoryVisualizationException}
	 * with an appropriate message for a TLB miss.
	 * @param virtualAddress the virtual address to map
	 * @return Returns the corresponding physical address.
	 * @throws MemoryVisualizationException on visualization problems
	 */
	public int mapAddressForVisualization(int virtualAddress) throws MemoryVisualizationException;

	/**
	 * Performs a TBS instruction on the special registers.
	 */
	public void executeTbsInstruction();

	/**
	 * Performs a TBWR instruction on the special registers.
	 */
	public void executeTbwrInstruction();

	/**
	 * Performs a TBRI instruction on the special registers.
	 */
	public void executeTbriInstruction();

	/**
	 * Performs a TBWI instruction on the special registers.
	 */
	public void executeTbwiInstruction();

	/**
	 * Clones this object.
	 * @return the clone
	 */
	public IMemoryManagementUnit clone();
	
}
