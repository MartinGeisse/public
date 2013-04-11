/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;

import name.martingeisse.ecosim.cpu.Cpu;
import name.martingeisse.ecosim.cpu.CpuException;
import name.martingeisse.ecosim.cpu.GeneralRegisterFile;
import name.martingeisse.ecosim.cpu.IGeneralRegisterFile;
import name.martingeisse.ecosim.cpu.IMemoryManagementUnit;
import name.martingeisse.ecosim.cpu.ISpecialRegisterFile;

/**
 * A special-purpose CPU type that is used for pure usermode simulation.
 * 
 * This implementation does not support save/restore features
 * commonly used for context switching. To simulate a multi-process
 * system, use multiple CPUs.
 * 
 * This CPU uses a {@link GeneralRegisterFile}, {@link UsermodeSpecialRegisterFile}
 * and a customizable MMU (default is {@link IdentityMemoryManagementUnit}).
 * 
 * Handling of TRAP and other exceptions can be customized by using an
 * {@link IUsermodeCpuTrapHandler} and/or subclassing.
 * 
 */
public class UsermodeCpu extends Cpu {

	/**
	 * the trapHandler
	 */
	private IUsermodeCpuTrapHandler trapHandler;

	/**
	 * Default constructor -- uses an {@link IdentityMemoryManagementUnit}.
	 */
	public UsermodeCpu() {
		this(new IdentityMemoryManagementUnit());
	}

	/**
	 * Constructor.
	 * @param memoryManagementUnit the MMU to use
	 */
	public UsermodeCpu(IMemoryManagementUnit memoryManagementUnit) {
		super(false);

		// initialize the CPU components
		final IGeneralRegisterFile generalRegisters = new GeneralRegisterFile();
		final ISpecialRegisterFile specialRegisters = new UsermodeSpecialRegisterFile();
		initialize(generalRegisters, specialRegisters, memoryManagementUnit);
	}

	/**
	 * Getter method for the trapHandler.
	 * @return the trapHandler
	 */
	public IUsermodeCpuTrapHandler getTrapHandler() {
		return trapHandler;
	}

	/**
	 * Setter method for the trapHandler.
	 * @param trapHandler the trapHandler to set
	 */
	public void setTrapHandler(final IUsermodeCpuTrapHandler trapHandler) {
		this.trapHandler = trapHandler;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.Cpu#handleCpuException(int)
	 */
	@Override
	public void handleCpuException(final int code) {
		if (code == CpuException.CODE_TRAP) {
			handleTrapException();
		} else {
			handleNonTrapException(code);
		}
	}
	
	/**
	 * This method is invoked for each TRAP exception. The default implementation
	 * uses the configured {@link IUsermodeCpuTrapHandler}. If none is set,
	 * this method exits with an error message.
	 */
	public void handleTrapException() {
		if (trapHandler != null) {
			trapHandler.handleTrap();
		} else {
			error("TRAP exception occurred and no trap handler was installed");
		}
	}

	/**
	 * This method is invoked for each exception that is not a TRAP. The default
	 * implementation exits with an error message.
	 * @param code the exception code
	 */
	public void handleNonTrapException(int code) {
		error("a CPU exception occurred at PC = " + getPc().getValue() + ": " + CpuException.getNameForCode(code));
	}
	
	/**
	 * Exits the simulator with an error message.
	 * @param the error message
	 */
	private void error(String message) {
		System.out.println("* ERROR: " + message);
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.Cpu#clone()
	 */
	@Override
	public UsermodeCpu clone() {
		UsermodeCpu clone = (UsermodeCpu)super.clone();
		clone.trapHandler = null;
		return clone;
	}
	
}
