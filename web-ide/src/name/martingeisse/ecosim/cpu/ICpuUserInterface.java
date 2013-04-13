/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;


/**
 * This interface represents the "CPU" part of the user interface.
 * It provides callback methods that are invoked on various CPU
 * actions. The callback methods are usually *NOT* invoked when the
 * corresponding functionality is used by other means; for example,
 * onReadGeneralRegister() is not invoked when a general register
 * is read by another client than the CPU. Clients other than the
 * CPU have the choice whether to notify the user interface or not.
 */
public interface ICpuUserInterface {

	/**
	 * This callback is invoked when the CPU reads from a general-purpose
	 * register.
	 * @param index the index of the register.
	 */
	public void onReadGeneralRegister(int index);

	/**
	 * This callback is invoked when the CPU writes to a general-purpose
	 * register.
	 * @param index the index of the register.
	 */
	public void onWriteGeneralRegister(int index);

	/**
	 * This callback is invoked when the CPU reads from a special-purpose
	 * register.
	 * @param index the index of the register.
	 */
	public void onReadSpecialRegister(int index);

	/**
	 * This callback is invoked when the CPU writes to a special-purpose
	 * register.
	 * @param index the index of the register.
	 */
	public void onWriteSpecialRegister(int index);

	/**
	 * This callback is invoked when the CPU writes to the PC register.
	 */
	public void onWritePc();
	
	/**
	 * This callback is invoked when the CPU stores a value through the bus.
	 */
	public void onStore();
	
	/**
	 * This callback is invoked when the CPU modifies the contents of the TLB table.
	 */
	public void onWriteTlb();

}
