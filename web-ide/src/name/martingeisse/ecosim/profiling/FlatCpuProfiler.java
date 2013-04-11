/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.profiling;

import name.martingeisse.ecosim.cpu.ICpuProfiler;

/**
 * Controlling object for flat CPU profiling runs. This object
 * controls creation of {@link FlatCpuProfilingRun} instances.
 * Unlike those instances, this object is meant to last during
 * the whole lifetime of the simulator.
 */
public class FlatCpuProfiler implements ICpuProfiler {

	/**
	 * the startAddress
	 */
	private int startAddress;

	/**
	 * the endAddress
	 */
	private int endAddress;

	/**
	 * the granularity
	 */
	private int granularity;
	
	/**
	 * the currentRun
	 */
	private FlatCpuProfilingRun currentRun;

	/**
	 * Constructor.
	 */
	public FlatCpuProfiler() {
		this.startAddress = 0xc0000000;
		this.endAddress = 0xefffffff;
		this.granularity = 0x01000000;
	}

	/**
	 * Getter method for the startAddress.
	 * @return the startAddress
	 */
	public int getStartAddress() {
		return startAddress;
	}

	/**
	 * Setter method for the startAddress.
	 * @param startAddress the startAddress to set
	 */
	public void setStartAddress(final int startAddress) {
		this.startAddress = startAddress;
	}

	/**
	 * Getter method for the endAddress.
	 * @return the endAddress
	 */
	public int getEndAddress() {
		return endAddress;
	}

	/**
	 * Setter method for the endAddress.
	 * @param endAddress the endAddress to set
	 */
	public void setEndAddress(final int endAddress) {
		this.endAddress = endAddress;
	}

	/**
	 * Getter method for the granularity.
	 * @return the granularity
	 */
	public int getGranularity() {
		return granularity;
	}

	/**
	 * Setter method for the granularity.
	 * @param granularity the granularity to set
	 */
	public void setGranularity(final int granularity) {
		this.granularity = granularity;
	}

	/**
	 * Getter method for the currentRun.
	 * @return the currentRun
	 */
	public FlatCpuProfilingRun getCurrentRun() {
		return currentRun;
	}

	/**
	 * Setter method for the currentRun.
	 * @param currentRun the currentRun to set
	 */
	public void setCurrentRun(FlatCpuProfilingRun currentRun) {
		this.currentRun = currentRun;
	}
	
	/**
	 * Starts profiling.
	 */
	public void start() {
		setCurrentRun(new FlatCpuProfilingRun(startAddress, endAddress, granularity));
	}
	
	/**
	 * Stops profilings.
	 */
	public void stop() {
		setCurrentRun(null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.cpu.ICpuProfiler#recordInstructionAddress(int)
	 */
	@Override
	public void recordInstructionAddress(int address) {
		if (currentRun != null) {
			currentRun.sample(address);
		}
	}
	
	/**
	 * Generates a report about the current run.
	 * @param includeZeroBlocks whether to include address blocks in the report from which
	 * no instruction has been executed at all
	 * @return the report
	 * @throws IllegalStateException if no current run exists
	 */
	public String createReport(boolean includeZeroBlocks) throws IllegalStateException {
		if (currentRun == null) {
			throw new IllegalStateException("no current run");
		}
		return currentRun.createReport(includeZeroBlocks); 
	}
	
}
