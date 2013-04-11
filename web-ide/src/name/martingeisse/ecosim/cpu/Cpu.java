/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusErrorException;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IBusMasterAccess;

/**
 * CPU simulation model.
 */
public class Cpu implements Cloneable {

	/**
	 * the bus
	 */
	private IBusMasterAccess bus;

	/**
	 * the generalRegisters
	 */
	private IGeneralRegisterFile generalRegisters;

	/**
	 * the specialRegisters
	 */
	private ISpecialRegisterFile specialRegisters;

	/**
	 * the MMU
	 */
	private IMemoryManagementUnit memoryManagementUnit;

	/**
	 * the pc
	 */
	private ProgramCounter pc;

	/**
	 * the userInterface
	 */
	private ICpuUserInterface userInterface;

	/**
	 * the profiler
	 */
	private ICpuProfiler profiler;

	/**
	 * the extensionHandler
	 */
	private ICpuExtensionHandler extensionHandler;

	/**
	 * Constructor
	 */
	public Cpu() {
		this(true);
	}

	/**
	 * Constructor.
	 * @param autoInitialize whether to initialize this object
	 */
	protected Cpu(boolean autoInitialize) {
		if (autoInitialize) {
			IGeneralRegisterFile generalRegisters = new GeneralRegisterFile();
			ISpecialRegisterFile specialRegisters = new SpecialRegisterFile();
			IMemoryManagementUnit memoryManagementUnit = new MemoryManagementUnit(specialRegisters);
			initialize(generalRegisters, specialRegisters, memoryManagementUnit);
		}
	}

	/**
	 * Constructor
	 * @param generalRegisters the general register file implementation
	 * @param specialRegisters the special register file implementation
	 * @param memoryManagementUnit the MMU implementation
	 */
	protected void initialize(IGeneralRegisterFile generalRegisters, ISpecialRegisterFile specialRegisters, IMemoryManagementUnit memoryManagementUnit) {
		this.generalRegisters = generalRegisters;
		this.specialRegisters = specialRegisters;
		this.memoryManagementUnit = memoryManagementUnit;
		this.pc = new ProgramCounter();
		reset();
	}
	
	/**
	 * @return Returns the bus.
	 */
	public IBusMasterAccess getBus() {
		return bus;
	}

	/**
	 * Sets the bus.
	 * @param bus the new value to set
	 */
	public void setBus(final IBusMasterAccess bus) {
		this.bus = bus;
	}

	/**
	 * @return Returns the generalRegisters.
	 */
	public IGeneralRegisterFile getGeneralRegisters() {
		return generalRegisters;
	}

	/**
	 * @return Returns the specialRegisters.
	 */
	public ISpecialRegisterFile getSpecialRegisters() {
		return specialRegisters;
	}

	/**
	 * @return Returns the memoryManagementUnit.
	 */
	public IMemoryManagementUnit getMemoryManagementUnit() {
		return memoryManagementUnit;
	}

	/**
	 * @return Returns the pc.
	 */
	public ProgramCounter getPc() {
		return pc;
	}

	/**
	 * @return Returns the userInterface.
	 */
	public ICpuUserInterface getUserInterface() {
		return userInterface;
	}

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(final ICpuUserInterface userInterface) {
		this.userInterface = userInterface;
		generalRegisters.setUserInterface(userInterface);
		specialRegisters.setUserInterface(userInterface);
		pc.setUserInterface(userInterface);
		// TODO: unit-test this 
		memoryManagementUnit.setUserInterface(userInterface);
	}

	/**
	 * Getter method for the profiler.
	 * @return the profiler
	 */
	public ICpuProfiler getProfiler() {
		return profiler;
	}

	/**
	 * Setter method for the profiler.
	 * @param profiler the profiler to set
	 */
	public void setProfiler(final ICpuProfiler profiler) {
		this.profiler = profiler;
	}

	/**
	 * Getter method for the extensionHandler.
	 * @return the extensionHandler
	 */
	public ICpuExtensionHandler getExtensionHandler() {
		return extensionHandler;
	}

	/**
	 * Setter method for the extensionHandler.
	 * @param extensionHandler the extensionHandler to set
	 */
	public void setExtensionHandler(final ICpuExtensionHandler extensionHandler) {
		this.extensionHandler = extensionHandler;
	}

	/**
	 * Resets the CPU. This affects only the PSW and the PC.
	 * General-purpose registers, TLB registers and the TLB itself retain
	 * their value.
	 */
	public void reset() {
		pc.setValue(0xE0000000, true);
		specialRegisters.write(ISpecialRegisterFile.INDEX_PSW, ProcessorStatusWord.RESET_VALUE, true);
	}

	/**
	 * Executes a single execution step. This can be an instruction
	 * or interrupt entry.
	 */
	public void step() {

		/** this information is needed to determine interrupts **/
		final int interruptMask = (specialRegisters.read(ISpecialRegisterFile.INDEX_PSW, true) & 0xffff);
		final int interrupt = bus.getActiveInterrupt(interruptMask);
		final boolean interruptEnable = (specialRegisters.read(ISpecialRegisterFile.INDEX_PSW, true) & ProcessorStatusWord.INTERRUPT_ENABLE_BIT) != 0;

		/** handle interrupt or instruction **/
		if (interrupt != -1 && interruptEnable) {
			enterInterruptHandler(interrupt);
		} else {
			try {
				executeInstruction();
			} catch (final CpuException e) {
				handleCpuException(e.getCode());
			}
		}

	}
	
	/**
	 * This method is invoked when a CPU exception occurs. It allows subclasses to
	 * handle specific exceptions. The default implementation enters the
	 * interrupt handler for the exception.
	 * @param code the exception code
	 */
	public void handleCpuException(int code) {
		enterInterruptHandler(code + 16);
	}

	/**
	 * 
	 */
	private void enterInterruptHandler(final int priority) {
		final int oldPsw = specialRegisters.read(ISpecialRegisterFile.INDEX_PSW, true);
		final int newPsw = ProcessorStatusWord.transformOnException(oldPsw, priority);
		specialRegisters.write(ISpecialRegisterFile.INDEX_PSW, newPsw, true);
		generalRegisters.write(30, pc.getValue(), true);

		boolean userTlbMiss;
		if (priority == CpuException.CODE_TLB_MISS + 16) {
			final int badAddress = specialRegisters.read(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, false);
			userTlbMiss = ((badAddress & 0x80000000) == 0);
		} else {
			userTlbMiss = false;
		}

		final int handlerBaseAddress = ProcessorStatusWord.getVector(oldPsw) ? 0xC0000000 : 0xE0000000;
		final int handlerAddress = handlerBaseAddress + (userTlbMiss ? 8 : 4);
		pc.setValue(handlerAddress, true);
	}

	/**
	 * Executes the next instruction, regardless of the interrupt status.
	 */
	private void executeInstruction() throws CpuException {

		/** profiling **/
		if (profiler != null) {
			profiler.recordInstructionAddress(pc.getValue());
		}

		/** fetch the instruction **/
		final int instruction = read(pc.getValue(), BusAccessSize.WORD);

		/** decode it **/
		final int opcode = (instruction >> Instruction.OPCODE_SHIFT) & Instruction.OPCODE_VALUE_MASK;
		final int reg1 = (instruction >> Instruction.REG1_SHIFT) & Instruction.REG1_VALUE_MASK;
		final int reg2 = (instruction >> Instruction.REG2_SHIFT) & Instruction.REG2_VALUE_MASK;
		final int reg3 = (instruction >> Instruction.REG3_SHIFT) & Instruction.REG3_VALUE_MASK;
		final int unsignedImmediate = (instruction >> Instruction.IMMEDIATE_SHIFT) & Instruction.IMMEDIATE_VALUE_MASK;
		final int signedImmediate = (short)unsignedImmediate;
		final int unsignedOffset = (instruction >> Instruction.OFFSET_SHIFT) & Instruction.OFFSET_VALUE_MASK;
		final int signedOffset = (unsignedOffset << 6) >> 6;
		int nextPc = pc.getValue() + 4;
		final int branchTarget = nextPc + (signedImmediate << 2);
		final int jumpTarget = nextPc + (signedOffset << 2);

		/** read registers **/
		final int registerValue1 = generalRegisters.read(reg1, true);
		long unsignedRegisterValue1 = registerValue1;
		unsignedRegisterValue1 = unsignedRegisterValue1 & 0x00000000ffffffffL;

		final int registerValue2 = generalRegisters.read(reg2, true);
		long unsignedRegisterValue2 = registerValue2;
		unsignedRegisterValue2 = unsignedRegisterValue2 & 0x00000000ffffffffL;

		/** execute it **/
		switch (opcode) {

		case Instruction.OPCODE_ADD:
			generalRegisters.write(reg3, registerValue1 + registerValue2, true);
			break;

		case Instruction.OPCODE_ADDI:
			generalRegisters.write(reg2, registerValue1 + signedImmediate, true);
			break;

		case Instruction.OPCODE_SUB:
			generalRegisters.write(reg3, registerValue1 - registerValue2, true);
			break;

		case Instruction.OPCODE_SUBI:
			generalRegisters.write(reg2, registerValue1 - signedImmediate, true);
			break;

		case Instruction.OPCODE_MUL:
			generalRegisters.write(reg3, registerValue1 * registerValue2, true);
			break;

		case Instruction.OPCODE_MULI:
			generalRegisters.write(reg2, registerValue1 * signedImmediate, true);
			break;

		case Instruction.OPCODE_MULU:
			generalRegisters.write(reg3, (int)(unsignedRegisterValue1 * unsignedRegisterValue2), true);
			break;

		case Instruction.OPCODE_MULUI:
			generalRegisters.write(reg2, (int)(unsignedRegisterValue1 * unsignedImmediate), true);
			break;

		case Instruction.OPCODE_DIV:
			// TODO: the eco32 uses the same rounding modes as Java. unit-test that.
			if (registerValue2 == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg3, registerValue1 / registerValue2, true);
			}
			break;

		case Instruction.OPCODE_DIVI:
			if (signedImmediate == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg2, registerValue1 / signedImmediate, true);
			}
			break;

		case Instruction.OPCODE_DIVU:
			if (registerValue2 == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg3, (int)(unsignedRegisterValue1 / unsignedRegisterValue2), true);
			}
			break;

		case Instruction.OPCODE_DIVUI:
			if (unsignedImmediate == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg2, (int)(unsignedRegisterValue1 / unsignedImmediate), true);
			}
			break;

		case Instruction.OPCODE_REM:
			if (registerValue2 == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg3, registerValue1 % registerValue2, true);
			}
			break;

		case Instruction.OPCODE_REMI:
			if (signedImmediate == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg2, registerValue1 % signedImmediate, true);
			}
			break;

		case Instruction.OPCODE_REMU:
			if (registerValue2 == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg3, (int)(unsignedRegisterValue1 % unsignedRegisterValue2), true);
			}
			break;

		case Instruction.OPCODE_REMUI:
			if (unsignedImmediate == 0) {
				throw new CpuException(CpuException.CODE_DIVISION);
			} else {
				generalRegisters.write(reg2, (int)(unsignedRegisterValue1 % unsignedImmediate), true);
			}
			break;

		case Instruction.OPCODE_AND:
			generalRegisters.write(reg3, registerValue1 & registerValue2, true);
			break;

		case Instruction.OPCODE_ANDI:
			generalRegisters.write(reg2, registerValue1 & unsignedImmediate, true);
			break;

		case Instruction.OPCODE_OR:
			generalRegisters.write(reg3, registerValue1 | registerValue2, true);
			break;

		case Instruction.OPCODE_ORI:
			generalRegisters.write(reg2, registerValue1 | unsignedImmediate, true);
			break;

		case Instruction.OPCODE_XOR:
			generalRegisters.write(reg3, registerValue1 ^ registerValue2, true);
			break;

		case Instruction.OPCODE_XORI:
			generalRegisters.write(reg2, registerValue1 ^ unsignedImmediate, true);
			break;

		case Instruction.OPCODE_XNOR:
			generalRegisters.write(reg3, ~(registerValue1 ^ registerValue2), true);
			break;

		case Instruction.OPCODE_XNORI:
			generalRegisters.write(reg2, ~(registerValue1 ^ unsignedImmediate), true);
			break;

		case Instruction.OPCODE_SLL:
			generalRegisters.write(reg3, registerValue1 << registerValue2, true);
			break;

		case Instruction.OPCODE_SLLI:
			generalRegisters.write(reg2, registerValue1 << unsignedImmediate, true);
			break;

		case Instruction.OPCODE_SLR:
			generalRegisters.write(reg3, registerValue1 >>> registerValue2, true);
			break;

		case Instruction.OPCODE_SLRI:
			generalRegisters.write(reg2, registerValue1 >>> unsignedImmediate, true);
			break;

		case Instruction.OPCODE_SAR:
			generalRegisters.write(reg3, registerValue1 >> registerValue2, true);
			break;

		case Instruction.OPCODE_SARI:
			generalRegisters.write(reg2, registerValue1 >> unsignedImmediate, true);
			break;

		case Instruction.OPCODE_LDHI:
			generalRegisters.write(reg2, unsignedImmediate << 16, true);
			break;

		case Instruction.OPCODE_BEQ:
			if (registerValue1 == registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BNE:
			if (registerValue1 != registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BLE:
			if (registerValue1 <= registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BLEU:
			if (unsignedRegisterValue1 <= unsignedRegisterValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BLT:
			if (registerValue1 < registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BLTU:
			if (unsignedRegisterValue1 < unsignedRegisterValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BGE:
			if (registerValue1 >= registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BGEU:
			if (unsignedRegisterValue1 >= unsignedRegisterValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BGT:
			if (registerValue1 > registerValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_BGTU:
			if (unsignedRegisterValue1 > unsignedRegisterValue2) {
				nextPc = branchTarget;
			}
			break;

		case Instruction.OPCODE_J:
			nextPc = jumpTarget;
			break;

		case Instruction.OPCODE_JR:
			nextPc = registerValue1;
			break;

		case Instruction.OPCODE_JAL:
			generalRegisters.write(31, nextPc, true);
			nextPc = jumpTarget;
			break;

		case Instruction.OPCODE_JALR:
			generalRegisters.write(31, nextPc, true);
			nextPc = registerValue1;
			break;

		case Instruction.OPCODE_TRAP:
			throw new CpuException(CpuException.CODE_TRAP);

		case Instruction.OPCODE_RFX: {
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			final int oldPsw = specialRegisters.read(ISpecialRegisterFile.INDEX_PSW, true);
			final int newPsw = ProcessorStatusWord.transformOnReturnFromException(oldPsw);
			specialRegisters.write(ISpecialRegisterFile.INDEX_PSW, newPsw, true);
			nextPc = generalRegisters.read(30, true);
			break;
		}

		case Instruction.OPCODE_LDW: {
			final int rawValue = read(registerValue1 + signedImmediate, BusAccessSize.WORD);
			generalRegisters.write(reg2, rawValue, true);
			break;
		}

		case Instruction.OPCODE_LDH: {
			final int rawValue = read(registerValue1 + signedImmediate, BusAccessSize.HALFWORD);
			generalRegisters.write(reg2, (short)rawValue, true);
			break;
		}

		case Instruction.OPCODE_LDHU: {
			final int rawValue = read(registerValue1 + signedImmediate, BusAccessSize.HALFWORD);
			generalRegisters.write(reg2, rawValue & 0xffff, true);
			break;
		}

		case Instruction.OPCODE_LDB: {
			final int rawValue = read(registerValue1 + signedImmediate, BusAccessSize.BYTE);
			generalRegisters.write(reg2, (byte)rawValue, true);
			break;
		}

		case Instruction.OPCODE_LDBU: {
			final int rawValue = read(registerValue1 + signedImmediate, BusAccessSize.BYTE);
			generalRegisters.write(reg2, rawValue & 0xff, true);
			break;
		}

		case Instruction.OPCODE_STW:
			write(registerValue1 + signedImmediate, BusAccessSize.WORD, registerValue2);
			break;

		case Instruction.OPCODE_STH:
			write(registerValue1 + signedImmediate, BusAccessSize.HALFWORD, registerValue2);
			break;

		case Instruction.OPCODE_STB:
			write(registerValue1 + signedImmediate, BusAccessSize.BYTE, registerValue2);
			break;

		case Instruction.OPCODE_MVFS:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			if (unsignedImmediate >= ISpecialRegisterFile.SIZE) {
				throw new CpuException(CpuException.CODE_ILLEGAL_INSTRUCTION);
			}
			generalRegisters.write(reg2, specialRegisters.read(unsignedImmediate, true), true);
			break;

		case Instruction.OPCODE_MVTS:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			if (unsignedImmediate >= ISpecialRegisterFile.SIZE) {
				throw new CpuException(CpuException.CODE_ILLEGAL_INSTRUCTION);
			}
			specialRegisters.write(unsignedImmediate, registerValue2, true);
			break;

		case Instruction.OPCODE_TBS:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			memoryManagementUnit.executeTbsInstruction();
			break;

		case Instruction.OPCODE_TBWR:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			memoryManagementUnit.executeTbwrInstruction();
			break;

		case Instruction.OPCODE_TBRI:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			memoryManagementUnit.executeTbriInstruction();
			break;

		case Instruction.OPCODE_TBWI:
			checkKernelMode(CpuException.CODE_PRIVILEGED_INSTRUCTION);
			memoryManagementUnit.executeTbwiInstruction();
			break;

		default:
			if (extensionHandler != null) {
				if (extensionHandler.handleIllegalOpcode(instruction)) {
					// prevent auto-increment
					nextPc = pc.getValue();
					break;
				}
			}
			throw new CpuException(CpuException.CODE_ILLEGAL_INSTRUCTION);

		}

		/** update the pc **/
		pc.setValue(nextPc, true);

	}

	/**
	 * Performs an MMU-mapped read operation.
	 * @param virtualAddress the virtual address to read from
	 * @param size the bus access size
	 * @return Returns the value read.
	 */
	private int read(final int virtualAddress, final BusAccessSize size) throws CpuException {

		/** check alignment **/
		if (!size.isAligned(virtualAddress)) {
			specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
			throw new CpuException(CpuException.CODE_ILLEGAL_ADDRESS);
		}

		/** kernel mode check **/
		if ((virtualAddress & 0x80000000) != 0 && isUserMode()) {
			specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
			throw new CpuException(CpuException.CODE_PRIVILEGED_ADDRESS);
		}

		/** let the MMU handle address mapping **/
		final int physicalAddress = memoryManagementUnit.mapAddressForCpu(virtualAddress, false);

		/** perform read operation **/
		try {
			return bus.read(physicalAddress, size);
		} catch (final BusTimeoutException e) {
			throw new CpuException(CpuException.CODE_BUS_TIMEOUT);
		} catch (final BusErrorException e) {
			throw new RuntimeException("unexpected bus error", e);
		}

	}

	/**
	 * Performs an MMU-mapped write operation.
	 * @param virtualAddress the virtual address to write to
	 * @param size the bus access size
	 * @param value the value to write
	 */
	private void write(final int virtualAddress, final BusAccessSize size, final int value) throws CpuException {

		/** check alignment **/
		if (!size.isAligned(virtualAddress)) {
			specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
			throw new CpuException(CpuException.CODE_ILLEGAL_ADDRESS);
		}

		/** kernel mode check **/
		if ((virtualAddress & 0x80000000) != 0 && isUserMode()) {
			specialRegisters.write(ISpecialRegisterFile.INDEX_TLB_BAD_ADDRESS, virtualAddress, true);
			throw new CpuException(CpuException.CODE_PRIVILEGED_ADDRESS);
		}

		/** let the MMU handle address mapping **/
		final int physicalAddress = memoryManagementUnit.mapAddressForCpu(virtualAddress, true);

		/** perform write operation **/
		try {
			bus.write(physicalAddress, size, value);
		} catch (final BusTimeoutException e) {
			throw new CpuException(CpuException.CODE_BUS_TIMEOUT);
		} catch (final BusErrorException e) {
			throw new RuntimeException("unexpected bus error", e);
		}

		/** notify the user interface if present **/
		if (userInterface != null) {
			userInterface.onStore();
		}

	}

	/**
	 * @return Returns true if the CPU is in user mode, false if not.
	 */
	private boolean isUserMode() {
		return ProcessorStatusWord.getUserMode(specialRegisters.read(ISpecialRegisterFile.INDEX_PSW, true));
	}

	/**
	 * Ensures that the CPU is in kernel mode, and throws a CPU exception otherwise.
	 * Note that this method cannot be used for CPU exceptions that have other side
	 * effects such as setting the bad address register.
	 * @param code the exception code
	 */
	private void checkKernelMode(final int code) throws CpuException {
		if (isUserMode()) {
			throw new CpuException(code);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Cpu clone() {
		try {
			Cpu clone = (Cpu)super.clone();
			clone.bus = null;
			clone.generalRegisters = clone.generalRegisters.clone();
			clone.specialRegisters = clone.specialRegisters.clone();
			clone.memoryManagementUnit = clone.memoryManagementUnit.clone();
			clone.pc = clone.pc.clone();
			clone.userInterface = null;
			clone.profiler = null;
			clone.extensionHandler = null;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
