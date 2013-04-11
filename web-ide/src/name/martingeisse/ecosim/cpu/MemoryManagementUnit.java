/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;


/**
 * Default implementation of {@link IMemoryManagementUnit}.
 */
public final class MemoryManagementUnit implements IMemoryManagementUnit {

	/**
	 * The bit mask of retained bits in TLB entries. This field is private
	 * because it refers to the internal entry format used in this class.
	 */
	private static long ENTRY_RETENTION_MASK = 0xfffff000fffff003L;

	/**
	 * the specialRegisters
	 */
	private ISpecialRegisterFile specialRegisters;

	/**
	 * the TLB entries by index
	 */
	private long[] tlb;

	/**
	 * the counter used for random indexing.
	 */
	private int randomCounter;

	/**
	 * the userInterface
	 */
	private ICpuUserInterface userInterface;

	/**
	 * Constructor
	 * @param specialRegisters the special register file (contains MMU registers)
	 */
	public MemoryManagementUnit(ISpecialRegisterFile specialRegisters) {
		this.specialRegisters = specialRegisters;
		this.tlb = new long[32];
		this.randomCounter = 4;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getUserInterface()
	 */
	@Override
	public ICpuUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setUserInterface(name.martingeisse.ecotools.simulator.cpu.ICpuUserInterface)
	 */
	@Override
	public void setUserInterface(ICpuUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	/**
	 * Returns a TLB entry in internal format.
	 * @param index the index of the entry
	 * @return Returns the specified entry.
	 */
	private long getTlbEntry(int index) {
		return tlb[index & 0x1f];
	}

	/**
	 * Sets a TLB entry in internal format.
	 * @param index the index of the entry
	 * @param value the value to set
	 * @param notifyUserInterface whether the onWriteTlb() method of the user interface shall be invoked
	 */
	private void setTlbEntry(int index, long value, boolean notifyUserInterface) {
		tlb[index & 0x1f] = value & ENTRY_RETENTION_MASK;
		if (notifyUserInterface && userInterface != null) {
			userInterface.onWriteTlb();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getTlbEntryHigh(int)
	 */
	@Override
	public int getTlbEntryHigh(int index) {
		return (int) (getTlbEntry(index) >>> 32);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getTlbEntryLow(int)
	 */
	@Override
	public int getTlbEntryLow(int index) {
		return (int) (getTlbEntry(index));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setTlbEntry(int, int, int, boolean)
	 */
	@Override
	public void setTlbEntry(int index, int highValue, int lowValue, boolean notifyUserInterface) {
		long high = highValue;
		long low = lowValue;
		setTlbEntry(index, (high << 32) | (low & 0xffffffffL), notifyUserInterface);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#getRandomCounter()
	 */
	@Override
	public int getRandomCounter() {
		return randomCounter;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#setRandomCounter(int)
	 */
	@Override
	public void setRandomCounter(int randomCounter) {
		if (randomCounter < 4 || randomCounter > 31) {
			throw new IllegalArgumentException("TLB random counter value must be in the range 4..31.");
		}
		this.randomCounter = randomCounter;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#updateRandomCounter()
	 */
	@Override
	public void updateRandomCounter() {
		randomCounter = (randomCounter == 31) ? 4 : (randomCounter + 1);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#findTlbEntry(int)
	 */
	@Override
	public int findTlbEntry(int virtualAddress) {
		int result = INVALID_TLB_INDEX;
		for (int i = 0; i < tlb.length; i++) {
			int entryHigh = (int) (tlb[i] >>> 32);
			if ((entryHigh & 0xfffff000) == (virtualAddress & 0xfffff000)) {
				result = i;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#mapAddressForCpu(int, boolean)
	 */
	@Override
	public int mapAddressForCpu(int virtualAddress, boolean write) throws CpuException {
		boolean kernel = (virtualAddress & 0x80000000) != 0;
		boolean direct = kernel & ((virtualAddress & 0x40000000) != 0);
		if (!direct) {

			/** cycle through TLB entries for random updating **/
			updateRandomCounter();

			/** find a matching TLB entry **/
			int index = findTlbEntry(virtualAddress);
			if (index == INVALID_TLB_INDEX) {
				/** TLB miss **/
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, virtualAddress, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
				throw new CpuException(CpuException.CODE_TLB_MISS);
			}

			/** check if the page is valid **/
			int low = getTlbEntryLow(index);
			if ((low & TLB_ENTRY_VALID_BIT) == 0) {
				/** TLB invalid **/
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_INDEX, index, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, virtualAddress, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_LOW, low, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
				throw new CpuException(CpuException.CODE_TLB_INVALID);
			}

			/** check if this access violates write protection **/
			if (write && (low & TLB_ENTRY_WRITE_BIT) == 0) {
				/** TLB invalid **/
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_INDEX, index, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, virtualAddress, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_LOW, low, true);
				specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
				throw new CpuException(CpuException.CODE_TLB_WRITE);
			}

			/** successfully mapped **/
			return (low & 0xfffff000) | (virtualAddress & 0x00000fff);

		} else {

			/** direct-mapped address **/
			return virtualAddress & 0x3fffffff;

		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#mapAddressForVisualization(int)
	 */
	@Override
	public int mapAddressForVisualization(int virtualAddress) throws MemoryVisualizationException {
		boolean kernel = (virtualAddress & 0x80000000) != 0;
		boolean direct = kernel & ((virtualAddress & 0x40000000) != 0);
		if (!direct) {

			/** find a matching TLB entry **/
			int index = findTlbEntry(virtualAddress);
			if (index == INVALID_TLB_INDEX) {
				throw new MemoryVisualizationException("--- TLB miss ---");
			} else {
				int low = getTlbEntryLow(index);
				return (low & 0xfffff000) | (virtualAddress & 0x00000fff);
			}

		} else {

			/** direct-mapped address **/
			return virtualAddress & 0x3fffffff;

		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbsInstruction()
	 */
	@Override
	public void executeTbsInstruction() {
		int virtualAddress = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, true);
		int index = findTlbEntry(virtualAddress);
		specialRegisters.forceWrite(ISpecialRegisterFile.INDEX_TLB_INDEX, index, true);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbwrInstruction()
	 */
	@Override
	public void executeTbwrInstruction() {
		int high = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, true);
		int low = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_ENTRY_LOW, true);
		setTlbEntry(randomCounter, high, low, true);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbriInstruction()
	 */
	@Override
	public void executeTbriInstruction() {
		int index = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_INDEX, true);
		specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, getTlbEntryHigh(index), true);
		specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_ENTRY_LOW, getTlbEntryLow(index), true);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.IMemoryManagementUnit#executeTbwiInstruction()
	 */
	@Override
	public void executeTbwiInstruction() {
		int index = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_INDEX, true);
		int high = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_ENTRY_HIGH, true);
		int low = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_ENTRY_LOW, true);
		setTlbEntry(index, high, low, true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public IMemoryManagementUnit clone() {
		throw new UnsupportedOperationException();
	}
	
}
