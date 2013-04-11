/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import name.martingeisse.ecosim.bus.Bus;
import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.cpu.Cpu;
import name.martingeisse.ecosim.cpu.IGeneralRegisterFile;
import name.martingeisse.ecosim.cpu.usermode.ClearMemoryAction;
import name.martingeisse.ecosim.cpu.usermode.ReadFromStreamAction;
import name.martingeisse.ecosim.cpu.usermode.ReadStringAction;
import name.martingeisse.ecosim.cpu.usermode.SparseMemory;
import name.martingeisse.ecosim.cpu.usermode.UsermodeCpu;
import name.martingeisse.ecosim.cpu.usermode.unix.FilePointerTable;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallErrorCode;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;

/**
 * This class encapsulates all components to simulate an EOS
 * usermode process.
 * 
 * This class install its own bus with only a (sparse) memory
 * attached. Switching the bus or attaching other devices is
 * currently not supported. The memory is grown using sbrk syscalls.
 */
public class ToolhostProcess implements Cloneable {

	/**
	 * the processSet
	 */
	private ToolhostProcessSet processSet;
	
	/**
	 * the parent
	 */
	private ToolhostProcess parent;
	
	/**
	 * the cpu
	 */
	private UsermodeCpu cpu;

	/**
	 * the bus
	 */
	private Bus bus;

	/**
	 * the memory
	 */
	private SparseMemory memory;
	
	/**
	 * the filePointerTable
	 */
	private FilePointerTable filePointerTable;
	
	/**
	 * the id
	 */
	private int id;
	
	/**
	 * the state
	 */
	private ToolhostProcessState state;
	
	/**
	 * the sleepHandler
	 */
	private IToolhostSleepHandler sleepHandler;
	
	/**
	 * the exitStatus
	 */
	private int exitStatus;
	
	/**
	 * the workingDirectory
	 */
	private File workingDirectory;
	
	/**
	 * the breakPointer
	 */
	private int breakPointer;

	/**
	 * Constructor.
	 * @param processSet the process set
	 * @param parent the parent process
	 * @param id the process ID
	 */
	ToolhostProcess(ToolhostProcessSet processSet, ToolhostProcess parent, int id) {
		this.processSet = processSet;
		this.parent = parent;

		// create the bus and devices
		this.bus = new Bus();
		this.memory = new SparseMemory();
		bus.add(0, memory, new int[0]);
		bus.buildBusMap();

		// create the CPU
		this.cpu = new UsermodeCpu();
		cpu.setBus(bus);
		cpu.setTrapHandler(new ToolhostTrapHandler(this));

		// create OS objects
		this.filePointerTable = new FilePointerTable(256);
		this.id = id;
		this.state = ToolhostProcessState.RUNNING;
		this.exitStatus = 0;
		this.workingDirectory = (parent == null) ? new File(".") : parent.getWorkingDirectory();
		this.breakPointer = 0;
		
	}

	/**
	 * Getter method for the processSet.
	 * @return the processSet
	 */
	public ToolhostProcessSet getProcessSet() {
		return processSet;
	}
	
	/**
	 * Setter method for the processSet.
	 * @param processSet the processSet to set
	 */
	void setProcessSet(ToolhostProcessSet processSet) {
		this.processSet = processSet;
	}
	
	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public ToolhostProcess getParent() {
		return parent;
	}
	
	/**
	 * Getter method for the cpu.
	 * @return the cpu
	 */
	public Cpu getCpu() {
		return cpu;
	}

	/**
	 * Getter method for the bus.
	 * @return the bus
	 */
	public Bus getBus() {
		return bus;
	}

	/**
	 * Getter method for the memory.
	 * @return the memory
	 */
	public SparseMemory getMemory() {
		return memory;
	}
	
	/**
	 * Getter method for the filePointerTable.
	 * @return the filePointerTable
	 */
	public FilePointerTable getFilePointerTable() {
		return filePointerTable;
	}
	
	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	void setId(int id) {
		this.id = id;
	}

	/**
	 * Performs n steps.
	 * @param n the number of steps
	 */
	public void step(int n) {
		if (state == ToolhostProcessState.RUNNING) {
			for (int i=0; i<n; i++) {
				if (state == ToolhostProcessState.RUNNING) {
					cpu.step();
				}
			}
		} else if (state == ToolhostProcessState.SLEEPING) {
			sleepHandler.handle(this);
		}
	}
	
	/**
	 * Getter method for the state.
	 * @return the state
	 */
	public ToolhostProcessState getState() {
		return state;
	}
	
	/**
	 * Getter method for the exitStatus.
	 * @return the exitStatus
	 */
	public int getExitStatus() {
		return exitStatus;
	}
	
	/**
	 * Getter method for the workingDirectory.
	 * @return the workingDirectory
	 */
	public File getWorkingDirectory() {
		return workingDirectory;
	}
	
	/**
	 * Setter method for the workingDirectory.
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	/**
	 * Convenience method for exec(File, String...)
	 * @param executable the executable to run
	 * @param args command-line arguments
	 * @throws BusTimeoutException if a bus timeout occurs
	 * @throws IOException on I/O errors
	 */
	public void exec(final String executable, final String... args) throws BusTimeoutException, IOException {
		exec(new File(executable), args);
	}

	
	/**
	 * Convenience method for exec(File, String...)
	 * @param executable the executable to run
	 * @param args command-line arguments
	 * @param environment the environment
	 * @throws BusTimeoutException if a bus timeout occurs
	 * @throws IOException on I/O errors
	 */
	public void exec(final String executable, final String[] args, final String[] environment) throws BusTimeoutException, IOException {
		exec(new File(executable), args, environment);
	}

	
	/**
	 * Loads the specified executable into memory and resets the PC.
	 * @param executable the executable to load
	 * @param args command-line arguments
	 * @throws BusTimeoutException if a bus timeout occurs
	 * @throws IOException on I/O errors
	 */
	public void exec(final File executable, final String... args) throws BusTimeoutException, IOException {
		exec(executable, args, new String[0]);
	}
	
	/**
	 * Loads the specified executable into memory and resets the PC.
	 * @param executable the executable to load
	 * @param args command-line arguments
	 * @param environment the environment
	 * @throws BusTimeoutException if a bus timeout occurs
	 * @throws IOException on I/O errors
	 */
	public void exec(final File executable, final String[] args, String[] environment) throws BusTimeoutException, IOException {

		// sanitize environment entries
		environment = environment.clone();
		for (int i=0; i<environment.length; i++) {
			if (environment[i].indexOf('=') == -1) {
				environment[i] = (environment[i] + '=');
			}
		}
		
		// open the input file
		final FileInputStream fileInputStream = new FileInputStream(executable);
		try {

			final DataInputStream dataInputStream = new DataInputStream(fileInputStream);

			// read the executable header
			if (dataInputStream.readInt() != 0x1AA09232) {
				throw new IOException("exec(): wrong magic number");
			}
			final int codeSize = dataInputStream.readInt();
			if (codeSize < 0 || codeSize > 0x20000000) {
				throw new IOException("invalid code size: " + codeSize);
			}
			final int dataSize = dataInputStream.readInt();
			if (dataSize < 0 || dataSize > 0x20000000) {
				throw new IOException("invalid data size: " + dataSize);
			}
			final int bssSize = dataInputStream.readInt();
			if (bssSize < 0 || bssSize > 0x20000000) {
				throw new IOException("invalid bss size: " + bssSize);
			}
			if (dataInputStream.readInt() != 0) {
				throw new IOException("code relocation info size must be 0");
			}
			if (dataInputStream.readInt() != 0) {
				throw new IOException("data relocation info size must be 0");
			}
			if (dataInputStream.readInt() != 0) {
				throw new IOException("symbol table size must be 0");
			}
			if (dataInputStream.readInt() != 0) {
				throw new IOException("string space size must be 0");
			}

			// determine section base addresses
			final int codeBaseAddress = 0;
			final int dataBaseAddress = ((codeBaseAddress + codeSize + 4095) & ~4095);
			final int bssBaseAddress = (dataBaseAddress + dataSize);
			
			// read sections / clear BSS
			new ReadFromStreamAction(dataInputStream, memory, true).execute(codeBaseAddress, codeSize);
			new ReadFromStreamAction(dataInputStream, memory, true).execute(dataBaseAddress, dataSize);
			new ClearMemoryAction(memory, true).executeWrapExceptions(bssBaseAddress, bssSize);

			// prepare CPU state
			cpu.getPc().setValue(0, false);
			for (int i=0; i<32; i++) {
				cpu.getGeneralRegisters().write(i, 0, false);
			}
			
			// prepare the stack
			cpu.getGeneralRegisters().write(29, 0x80000000, false);
			int[] environmentAddresses = pushAsciiz(environment);
			int[] argumentAddresses = pushAsciiz(args);
			pushWordAlign();
			push(BusAccessSize.WORD, 0); // environ
			pushInts(environmentAddresses);
			push(BusAccessSize.WORD, 0); // environ
			pushInts(argumentAddresses); // argv[...]
			push(BusAccessSize.WORD, args.length); // argc

		} finally {
			fileInputStream.close();
		}
	}
	
	/**
	 * "pushes" bytes until the stack pointer is word-aligned. No bytes are actually
	 * written to memory, only the SP is modified.
	 */
	private void pushWordAlign() {
		IGeneralRegisterFile registers = cpu.getGeneralRegisters();
		registers.write(29, registers.read(29, false) & ~3, false);
	}
	
	/**
	 * Pushes the specified strings as zero-terminated ASCII strings onto the stack.
	 * Non-ASCII characters are suppressed.
	 * 
	 * @param strings the strings to push. Must not be null.
	 * @return the start addresses of the strings
	 */
	private int[] pushAsciiz(String[] strings) throws BusTimeoutException {
		int[] addresses = new int[strings.length];
		for (int i=0; i<strings.length; i++) {
			addresses[i] = pushAsciiz(strings[i]);
		}
		return addresses;
	}

	/**
	 * Pushes the specified string as a zero-terminated ASCII string onto the stack.
	 * Non-ASCII characters are suppressed.
	 * 
	 * @param s the string to push. Must not be null.
	 * @return the start address of the string
	 */
	private int pushAsciiz(String s) throws BusTimeoutException {
		int result = push(BusAccessSize.BYTE, 0);
		for (int i=s.length()-1; i >= 0; i--) {
			char c = s.charAt(i);
			if (c < 256) {
				result = push(BusAccessSize.BYTE, c);
			}
		}
		return result;
	}
	
	/**
	 * Pushes a byte, short or int value onto the stack. The stack pointer must be
	 * aligned to the access size.
	 * 
	 * @param value the value to push
	 * @return the address of the pushed value, i.e. the new stack pointer
	 */
	private int push(BusAccessSize busAccessSize, int value) throws BusTimeoutException {
		IGeneralRegisterFile registers = cpu.getGeneralRegisters();
		int oldSp = registers.read(29, false);
		int newSp = oldSp - busAccessSize.getByteCount();
		registers.write(29, newSp, false);
		memory.write(newSp, busAccessSize, value);
		return newSp;
	}

	/**
	 * Pushes multiple int values on the stack. The stack pointer must be 4-aligned. The values
	 * will be in memory in the same order as in the specified array.
	 * 
	 * @param ints the ints to push
	 * @return the address of the pushed values, i.e. the new stack pointer
	 */
	private int pushInts(int[] ints) throws BusTimeoutException {
		if (ints.length == 0) {
			return cpu.getGeneralRegisters().read(29, false);
		}
		int result = 0;
		for (int i = ints.length-1; i >= 0; i--) {
			result = push(BusAccessSize.WORD, ints[i]);
		}
		return result;
	}

	/**
	 * Terminates this process with the specified status.
	 * @param status the process status
	 * @throws UnixSyscallException on errors
	 */
	public void terminate(int status) throws UnixSyscallException {
		if (state == ToolhostProcessState.ZOMBIE) {
			throw new UnixSyscallException(UnixSyscallErrorCode.INVALID_PROCESS);
		}
		filePointerTable.dispose();
		filePointerTable = null;
		state = ToolhostProcessState.ZOMBIE;
		exitStatus = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected ToolhostProcess clone() {
		try {
			ToolhostProcess clone = (ToolhostProcess)super.clone();
			clone.cpu = clone.cpu.clone();
			clone.memory = clone.memory.clone();
			clone.bus = new Bus();
			clone.bus.add(0, clone.memory, new int[0]);
			clone.bus.buildBusMap();
			clone.cpu.setBus(clone.bus);
			clone.filePointerTable = new FilePointerTable(clone.filePointerTable);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Forks this process. The returned process is identical to this one, except for
	 * its process ID. The new process is added to the process set of this process.
	 * File pointers are shared as counted references.
	 * 
	 * @return the forked process
	 */
	public ToolhostProcess fork() {
		ToolhostProcess clone = clone();
		processSet.addProcess(clone);
		clone.parent = this;
		IGeneralRegisterFile cloneRegisters = clone.getCpu().getGeneralRegisters();
		cloneRegisters.write(8, 0, false);
		cloneRegisters.write(2, 0, false);
		clone.cpu.setTrapHandler(new ToolhostTrapHandler(clone));
		clone.cpu.getPc().setValue(clone.cpu.getPc().getValue() + 4, false);
		return clone;
	}
	
	/**
	 * Sets this process to "sleeping" state using the specified sleep
	 * handler. This process must be in "running" or "sleeping" state.
	 * @param sleepHandler the sleep handler
	 */
	public void setSleeping(IToolhostSleepHandler sleepHandler) {
		if (state != ToolhostProcessState.RUNNING && state != ToolhostProcessState.SLEEPING) {
			throw new IllegalStateException("invalid process state: " + state);
		}
		this.state = ToolhostProcessState.SLEEPING;
		this.sleepHandler = sleepHandler;
	}

	/**
	 * Sets this process to "running" state. The process must be in "running"
	 * or "sleeping" state.
	 */
	public void setRunning() {
		this.state = ToolhostProcessState.RUNNING;
		this.sleepHandler = null;
	}

	/**
	 * Convenience method to read a 0-terminated ASCII string from the process's
	 * memory. This method just wraps a {@link ReadStringAction}.
	 * @param address the address to read from
	 * @return the string
	 */
	public String readString(int address) {
		ReadStringAction readStringAction = new ReadStringAction(memory, false);
		readStringAction.execute(address);
		return readStringAction.getResult();
	}
	
	/**
	 * Increases the allocated memory to at least the specified break
	 * pointer. The actual allcoated memory will be rounded up to a
	 * full page, and no memory is ever de-allocated by this method.
	 * 
	 * @param minimumBreakPointer the requested minimum allocation size
	 */
	public void increaseMemoryAllocation(int minimumBreakPointer) {
		while (breakPointer < minimumBreakPointer) {
			memory.getOrCreatePage(breakPointer);
			breakPointer += 4096;
		}
	}

}
