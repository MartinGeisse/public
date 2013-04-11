/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * Default implementation of {@link ISpecialRegisterFile}.
 */
public final class SpecialRegisterFile implements ISpecialRegisterFile {

	/**
	 * the retention masks
	 */
	public static final int[] retentionMasks;

	/**
	 * static initializer
	 */
	static {
		retentionMasks = new int[] {
			RETENTION_MASK_PSW, RETENTION_MASK_TLB_INDEX, RETENTION_MASK_TLB_ENTRY_HIGH, RETENTION_MASK_TLB_ENTRY_LOW, RETENTION_MASK_TLB_BAD_ADDRESS
		};
	}

	/**
	 * the values
	 */
	private int values[];

	/**
	 * the userInterface
	 */
	private ICpuUserInterface userInterface;

	/**
	 * Constructor
	 */
	public SpecialRegisterFile() {
		this.values = new int[5];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#getUserInterface()
	 */
	@Override
	public ICpuUserInterface getUserInterface() {
		return userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#setUserInterface(name.martingeisse.ecotools.simulator.cpu.ICpuUserInterface)
	 */
	@Override
	public void setUserInterface(ICpuUserInterface userInterface) {
		this.userInterface = userInterface;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#read(int, boolean)
	 */
	@Override
	public int read(int index, boolean notifyUserInterface) {
		int value = values[index];
		if (notifyUserInterface && userInterface != null) {
			userInterface.onReadSpecialRegister(index);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#write(int, int, boolean)
	 */
	@Override
	public void write(int index, int value, boolean notifyUserInterface) {
		values[index] = value & retentionMasks[index];
		if (notifyUserInterface && userInterface != null) {
			userInterface.onWriteSpecialRegister(index);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ISpecialRegisterFile#forceWrite(int, int, boolean)
	 */
	@Override
	public void forceWrite(int index, int value, boolean notifyUserInterface) {
		values[index] = value;
		if (notifyUserInterface && userInterface != null) {
			userInterface.onWriteSpecialRegister(index);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ISpecialRegisterFile clone() {
		throw new UnsupportedOperationException();
	}
	
}
